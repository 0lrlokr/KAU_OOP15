package com.example.kaustudyroom.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

class CameraRepository {

    private var imageCapture: ImageCapture? = null

    fun startCamera(cameraProvider: ProcessCameraProvider, surfaceProvider: Preview.SurfaceProvider, lifecycleOwner: LifecycleOwner) {
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed")
        }
    }

    fun captureImage(outputDirectory: File, executor: Executor, onImageCaptured: (File) -> Unit) {
        val photoFile = createImageFile(outputDirectory)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageCaptured(photoFile)
            }

            override fun onError(exc: ImageCaptureException) {
                onError(exc)
            }
        })
    }

    private fun createImageFile(outputDirectory: File): File {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        return File(outputDirectory, "studyRoom_checkIn_${timestamp}.jpg")
    }

    fun uploadImageToFirebaseStorage(imageFile: File, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child(imageFile.name)
        val uploadTask = storageRef.putFile(Uri.fromFile(imageFile))

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }
}