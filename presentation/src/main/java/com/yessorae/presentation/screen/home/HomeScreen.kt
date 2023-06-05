package com.yessorae.presentation.screen.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yessorae.designsystem.theme.Dimen
import com.yessorae.designsystem.util.BasePreview
import com.yessorae.designsystem.util.Margin
import com.yessorae.presentation.R
import com.yessorae.presentation.dialogs.ConfirmDialog
import com.yessorae.presentation.model.Goal
import com.yessorae.presentation.model.goals
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: HomeViewModel = viewModel()) {
    val model by viewModel.state.collectAsState()

    OverlayPermissionDialog(
        showDialog = model.showOverlayConfirmDialog,
        onOverlayConfirmed = { confirmed ->
            viewModel.onOverlayConfirmed(confirmed)
        },
        onCancelDialog = {
            viewModel.onCancelDialog()
        }
    )
}

@Composable
private fun TodoListItem() {

}

enum class TodoState {

}



@Composable
private fun OverlayPermissionDialog(
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
