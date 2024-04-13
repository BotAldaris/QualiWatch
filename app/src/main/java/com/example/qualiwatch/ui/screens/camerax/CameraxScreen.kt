package com.example.qualiwatch.ui.screens.camerax

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.qualiwatch.R
import com.example.qualiwatch.ui.screens.products.utils.ChooseDateDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraxScreen(
    viewmodel: CameraxViewModel,
    context: Context,
    onConfirmation: (String?) -> Unit
) {
    val uiState by viewmodel.uiState.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = remember {
        LifecycleCameraController(context).apply { setEnabledUseCases(CameraController.IMAGE_CAPTURE) }
    }
    BottomSheetScaffold(scaffoldState = scaffoldState, sheetPeekHeight = 0.dp, sheetContent = {

    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(controller = controller, Modifier.fillMaxSize())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = { takePhoto(controller, viewmodel::onPhotoTaken, context) },
                    Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = stringResource(
                            R.string.take_photo
                        ),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            if (uiState.showDialog) {
                ChooseDateDialog(
                    onDismissRequest = {
                        viewmodel.updateShowDialog(false)
                    },
                    onConfirmation = onConfirmation,
                    imageResponses = uiState.imageResponseList
                )
            }
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (ImageProxy) -> Unit,
    context: Context,
) {
    controller.takePicture(ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take a photo: ", exception)
            }

        })
}