package com.example.kaustudyroom.viewmodel

import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.repository.CameraRepository
import java.io.File
import java.util.concurrent.Executor

class CameraViewModel() : ViewModel() {
    private val repository = CameraRepository()

    fun startCamera(cameraProvider: ProcessCameraProvider, surfaceProvider: Preview.SurfaceProvider, lifecycleOwner: LifecycleOwner) {
        repository.startCamera(cameraProvider, surfaceProvider, lifecycleOwner)
    }
    fun captureImage(outputDirectory: File, executor: Executor, onImageCaptured: (File) -> Unit, onError: (ImageCaptureException) -> Unit) {
        repository.captureImage(outputDirectory, executor, onImageCaptured, onError)
    }

    fun uploadImage(imageFile: File, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        repository.uploadImageToFirebaseStorage(imageFile, onSuccess, onError)
    }
}