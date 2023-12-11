package com.cafu.textrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.Text

@Composable
fun ScanScreen(scanResult: (Text) -> Unit) {
    CheckCameraPermission()
    CameraCardScanning(
        xCoordinate = 100f,
        yCoordinate = 800f,
        onXCoordinateChange = {
            Log.e("Ibrahim", "onCreate: xCoordinateChange = $it")
        },
        onYCoordinateChange = {
            Log.e("Ibrahim", "onCreate: yCoordinateChange = $it")
        },
        onScanningButtonStateChanged = {
            Log.e("Ibrahim", "onScanningButtonStateChanged: $it")
        },
        coroutineScope = rememberCoroutineScope(),
        onRecognizerTextChange = {
            scanResult(it)
            Log.e("Ibrahim", "onRecognizerTextChange: ${it.text}")
        }
    )
}


@Composable
fun CheckCameraPermission() {
    val isStateGrantedCamera = ContextCompat.checkSelfPermission(
        LocalContext.current, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    var hasCameraPermission by remember {
        mutableStateOf(isStateGrantedCamera)
    }
    val permissionLauncherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasCameraPermission = isGranted
            // Handle the camera permission result here
        }
    )

    LaunchedEffect(hasCameraPermission) {
        permissionLauncherCamera.launch(Manifest.permission.CAMERA)
    }
}
