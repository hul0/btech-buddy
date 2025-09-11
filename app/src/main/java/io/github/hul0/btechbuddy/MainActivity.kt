package io.github.hul0.btechbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.hul0.btechbuddy.ui.AppHost
import io.github.hul0.btechbuddy.ui.theme.BtechBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BtechBuddyTheme {
                AppHost()
            }
        }
    }
}
