package com.example.kaustudyroom.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.Executor

class CameraRepository {
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val authViewModel = AuthViewModel()
    private val userId: String
        get() = authViewModel.getUserIdDirectly()

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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImageToFirebaseStorage(imageFile: File, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) = withContext(
        Dispatchers.IO) {
        val storageRef = FirebaseStorage.getInstance().reference.child(imageFile.name)
        val uploadTask = storageRef.putFile(Uri.fromFile(imageFile))

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onError(exception)
        }

        val currentDate: LocalDate = LocalDate.now()
        println("currentDate: $currentDate")
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatterForDb = DateTimeFormatter.ofPattern("HH:mm")
        val currentTime: String = LocalTime.now().format(formatter)
        val currentTimeForDb: String = LocalTime.now().format(formatterForDb)
        println("Formatted time: $currentTime")

        for (i in 1 until 4) {
            val startTime: String = currentTime
            val endTime: String = if ((currentTime.toInt() + i) >= 10) {(currentTime.toInt() + i).toString()}
            else { "0"+(currentTime.toInt() + i).toString()}
            val currentTimeSlot: String = "$startTime-$endTime"
            println("Checking for currentTimeSlot: $currentTimeSlot")

            try {
                val reference = databaseReference.child("User/$userId/$currentDate/$currentTimeSlot")

                // Check if the node exists
                val snapshot = reference.get().await()
                if (snapshot.exists()) {
                    // Node exists, update checkInTime and point columns
                    reference.child("checkInTime").setValue(currentTimeForDb)
                    reference.child("point").setValue(0) // Replace with the actual point value
                    return@withContext true
                }
            } catch (e: Exception) {
                println("Error checking time slot: $e")
            }
        }
        return@withContext false
    }
}