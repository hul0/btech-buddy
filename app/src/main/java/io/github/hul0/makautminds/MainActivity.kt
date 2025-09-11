package io.github.hul0.makautminds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.hul0.makautminds.ui.AppHost
import io.github.hul0.makautminds.ui.theme.MAKAUTMINDSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAKAUTMINDSTheme {
                AppHost()
            }
        }
    }
}
