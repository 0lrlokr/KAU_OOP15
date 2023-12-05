package com.example.kaustudyroom.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaustudyroom.repository.CameraRepository
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor

class CameraViewModel() : ViewModel() {
    private val repository = CameraRepository()

    fun startCamera(cameraProvider: ProcessCameraProvider, surfaceProvider: Preview.SurfaceProvider, lifecycleOwner: LifecycleOwner) {
        repository.startCamera(cameraProvider, surfaceProvider, lifecycleOwner)
    }
    fun captureImage(outputDirectory: File, executor: Executor, onImageCaptured: (File) -> Unit) {
        repository.captureImage(outputDirectory, executor, onImageCaptured)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(imageFile: File, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            repository.uploadImageToFirebaseStorage(imageFile, onSuccess, onError)
        }
    }
}