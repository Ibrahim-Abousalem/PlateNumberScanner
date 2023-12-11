package com.cafu.textrecognition

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor)
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    scan: MutableState<Boolean>,
//    time: Int = 0,
    context: Context,
    xCoordinate: Int,
    yCoordinate: Int,
    coroutineScope: CoroutineScope,
    onRecognizerTextChange: (value: Text) -> Unit,
) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = scaleType
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    LaunchedEffect(key1 = scan.value) {
        if (scan.value) {
            previewView.bitmap?.let { it ->
                val bitmap = Bitmap.createBitmap(
                    it,
                    xCoordinate,
                    yCoordinate,
                    450.dp.toPx(),
                    300.dp.toPx()
                )

                recognizer.process(InputImage.fromBitmap(bitmap, 0)).addOnSuccessListener {
                    onRecognizerTextChange(it)
                }
            }
        }
    }
    Box(contentAlignment = Alignment.Center) {
        AndroidView(
            modifier = modifier,
            factory = { context ->

                val previewUseCase = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                coroutineScope.launch {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, previewUseCase
                        )
                    } catch (ex: Exception) {
                        Log.e("CameraPreview", "Use case binding failed", ex)
                    }
                }

                previewView
            }

        )
    }
}

fun Dp.toPx(): Int {
    return (this.value.toInt() * Resources.getSystem().displayMetrics.density.toInt())
}
