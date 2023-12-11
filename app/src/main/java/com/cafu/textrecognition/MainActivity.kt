package com.cafu.textrecognition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.cafu.textrecognition.ui.theme.TextRecognitionTheme

class MainActivity : ComponentActivity() {

    private val viewModel: TextRecognitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextRecognitionTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
