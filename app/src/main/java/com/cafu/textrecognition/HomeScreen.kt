package com.cafu.textrecognition

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafu.textrecognition.ui.theme.TextRecognitionTheme

@Composable
fun HomeScreen(viewModel: TextRecognitionViewModel) {
    val uiState by viewModel.viewState.collectAsState()

    var showScanScreen by remember { mutableStateOf(false) }
    if (!showScanScreen)
        HomeScreenComponent(
            uiState,
            openCamera = {
                showScanScreen = true
            }
        )

    if (showScanScreen)
        ScanScreen(
            scanResult = { scanResult ->
                viewModel.updateScannedData(scanResult)
                showScanScreen = false
            }
        )

}

@Composable
fun HomeScreenComponent(
    scannedData: ScannedData,
    openCamera: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Emirates:")
            Text(text = scannedData.emirate)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "PlateCode:")
            Text(text = scannedData.plateCode)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "PlateNumber:")
            Text(text = scannedData.plateNumber)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp), onClick = openCamera
        ) {
            Text(text = "Scan plate number")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TextRecognitionTheme {
        HomeScreenComponent(
            scannedData = ScannedData(
                emirate = "Dubai",
                plateCode = "AA",
                plateNumber = "12345"
            ),
            openCamera = {}
        )
    }
}