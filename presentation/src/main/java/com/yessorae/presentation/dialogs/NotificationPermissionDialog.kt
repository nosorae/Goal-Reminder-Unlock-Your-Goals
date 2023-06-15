package com.yessorae.presentation.dialogs

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.yessorae.presentation.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionDialog(
    needRequestPermission: Boolean = false,
    onCompleteNotificationPermissionLogic: (result: Boolean) -> Unit
) {
    val context = LocalContext.current

    if (context.checkNotificationEnabled() || needRequestPermission.not()) return

    var showRationaleDialog by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            showRationaleDialog = false
            onCompleteNotificationPermissionLogic(context.checkNotificationEnabled())
        }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS) { result ->
                if (result) {
                    onCompleteNotificationPermissionLogic(true)
                } else {
                    showRationaleDialog = true
                }
            }

        LaunchedEffect(key1 = Unit) {
            if (notificationPermissionState.status.isGranted.not()) {
                if (notificationPermissionState.status.shouldShowRationale) {
                    showRationaleDialog = true
                } else {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }

    }

    ConfirmDialog(
        showDialog = showRationaleDialog,
        title = stringResource(id = R.string.common_permission_request),
        body = stringResource(id = R.string.dialog_body_overlay_permission_request),
        cancelText = stringResource(id = R.string.common_cancel),
        onClickCancel = {
            showRationaleDialog = false
        },
        onClickConfirm = {
            if (context.checkNotificationEnabled().not()) {
                val intent = Intent().apply {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    //for Android 5-7
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)

                    // for Android 8 and above
                    putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                }
                launcher.launch(intent)
            }
        },
        dismissOnClickOutside = true,
        dismissOnBackPress = true
    )
}

@ExperimentalPermissionsApi
val PermissionStatus.isGranted: Boolean
    get() = this == PermissionStatus.Granted

@ExperimentalPermissionsApi
val PermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        PermissionStatus.Granted -> false
        is PermissionStatus.Denied -> shouldShowRationale
    }


fun Context.checkNotificationEnabled(): Boolean {
    return (this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).areNotificationsEnabled()
}