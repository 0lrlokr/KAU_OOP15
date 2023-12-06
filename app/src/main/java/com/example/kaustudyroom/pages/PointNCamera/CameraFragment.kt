package com.example.kaustudyroom.pages.PointNCamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentCameraBinding
import com.example.kaustudyroom.viewmodel.CameraViewModel
import java.io.File
import java.util.concurrent.Executor


class CameraFragment : Fragment() {

    private var binding: FragmentCameraBinding ?= null
    private val cameraViewModel: CameraViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var executor: Executor
    private lateinit var outputDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        executor = ContextCompat.getMainExecutor(requireContext())
        outputDirectory = getOutputDirectory()

        // 카메라 접근 permission을 했는지 확인 후에 카메라 시작
        if (allPermissionsGranted()) {
            binding?.cameraPreview?.let { cameraViewModel.startCamera(cameraProvider, it.surfaceProvider,viewLifecycleOwner) }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // 촬영 버튼을 누르면 1.사진촬영, 2.이미지업로드, 3.이미지url을 viewmodel에 저장 후 다시 PointFragment로 이동
        binding?.captureButton?.setOnClickListener {
            cameraViewModel.captureImage(outputDirectory, executor) { file ->
                cameraViewModel.uploadImage(file, { downloadUrl ->
                    Log.d("firebase upload url", downloadUrl)
                    sharedViewModel.setLatestImageUrl(downloadUrl)
                    findNavController().navigate(R.id.action_cameraFragment_to_pointFragment)
                }, { exception ->
                    Log.d("firebase upload", exception.toString())
                })
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireContext().filesDir
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}