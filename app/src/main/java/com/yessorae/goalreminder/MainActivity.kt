package com.yessorae.goalreminder

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yessorae.designsystem.theme.GoalReminderTheme
import com.yessorae.goalreminder.util.startScreenOnOffService
import com.yessorae.goalreminder.util.stopScreenOnOffService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val requestOverlayPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (Settings.canDrawOverlays(this)) {
                Log.d("SR-N", "requestOverlayPermission ok")
            } else {
                Log.d("SR-N", "requestOverlayPermission cancel")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SR-N", "MainActivity onCreate")
        setContent {
            GoalReminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }


        observe()

        if (!Settings.canDrawOverlays(this)) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            requestOverlayPermission.launch(intent)
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isServiceOn.collectLatest { isServiceOn ->
                    Log.d("SR-N", "MainActivity isServiceOn $isServiceOn")
                    if (!isServiceOn) {
                        startScreenOnOffService()
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    com.yessorae.designsystem.theme.GoalReminderTheme {
        Greeting("Android")
    }
}
