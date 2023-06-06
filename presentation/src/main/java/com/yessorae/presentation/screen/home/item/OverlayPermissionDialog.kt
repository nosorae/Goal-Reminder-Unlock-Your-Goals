package com.yessorae.presentation.screen.home.item

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.yessorae.presentation.R
import com.yessorae.presentation.dialogs.ConfirmDialog

@Composable
fun OverlayPermissionDialog(
    showDialog: Boolean,
    onOverlayConfirmed: (Boolean) -> Unit,
    onCancelDialog: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        onOverlayConfirmed(Settings.canDrawOverlays(context))
    }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            onOverlayConfirmed(Settings.canDrawOverlays(context))
        }

    if (showDialog) {
        ConfirmDialog(
            title = stringResource(id = R.string.common_permission_request),
            body = stringResource(id = R.string.dialog_body_overlay_permission_request),
            onClickCancel = {
                onCancelDialog()
            },
            onClickConfirm = {
                if (!Settings.canDrawOverlays(context)) {
                    val intent =
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        )
                    launcher.launch(intent)
                }
            },
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    }
}
