package com.cafu.textrecognition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CameraCardScanningOuterLayer(
    onXCoordinateChange: (xCoordinate: Float) -> Unit,
    onYCoordinateChange: (yCoordinate: Float) -> Unit,
    scanNow: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (imageView, view1, view2, view3, view4, button) = createRefs()
        val backgroundColor = Color.Black.copy(alpha = 0.3f)

        Card(
            border = BorderStroke(2.dp, Color.White),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            modifier = Modifier
                .constrainAs(imageView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .onGloballyPositioned { coordinates ->
                    onXCoordinateChange(coordinates.positionInParent().x)
                    onYCoordinateChange(coordinates.positionInParent().y)
                }
                .width(328.dp)
                .height(218.dp)
        ) {
        }
        Box(
            modifier = Modifier
                .constrainAs(view1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(imageView.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .background(backgroundColor)
        )

        Box(
            modifier = Modifier
                .constrainAs(view2) {
                    top.linkTo(imageView.top)
                    bottom.linkTo(imageView.bottom)
                    start.linkTo(imageView.end)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .background(backgroundColor)
        )
        Box(
            modifier = Modifier
                .constrainAs(view3) {
                    top.linkTo(imageView.top)
                    bottom.linkTo(imageView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(imageView.start)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .background(backgroundColor)
        )

        Box(
            modifier = Modifier
                .constrainAs(view4) {
                    top.linkTo(imageView.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .background(backgroundColor)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp), onClick = scanNow
        ) {
            Text(text = "Scan")
        }
    }
}

@Composable
fun CameraCardScanning(
    modifier: Modifier = Modifier,
    xCoordinate: Float,
    yCoordinate: Float,
    onXCoordinateChange: (xCoordinate: Float) -> Unit,
    onYCoordinateChange: (yCoordinate: Float) -> Unit,
    onScanningButtonStateChanged: (showing: Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    onRecognizerTextChange: (value: Text) -> Unit,
) {
    var scan = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            scan = scan,
            context = LocalContext.current,
            xCoordinate = xCoordinate.toInt(),
            yCoordinate = yCoordinate.toInt(),
            onRecognizerTextChange = onRecognizerTextChange,
            coroutineScope = coroutineScope,
        )
        CameraCardScanningOuterLayer(
            onXCoordinateChange, onYCoordinateChange,
            scanNow = {
                scan.value = true
            }
        )
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        onScanningButtonStateChanged(false)
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_scanning),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview
@Composable
fun CameraCardScanningOuterLayerPreview() {
    CameraCardScanningOuterLayer({}, {}, {})
}