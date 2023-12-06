package com.example.kaustudyroom.pages.PointNCamera

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.databinding.FragmentPointBinding
import com.bumptech.glide.Glide
import com.example.kaustudyroom.R
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.example.kaustudyroom.viewmodel.PointViewModel

class PointFragment : Fragment() {
    var binding: FragmentPointBinding?= null

    private val pointViewModel = PointViewModel()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authViewModel = AuthViewModel()
    private val userId: String = authViewModel.getUserIdDirectly()

    private val pointList = mutableListOf<Point>(
        Point("","",0, "")
    )
    var pointSum = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "입실확인 사진 촬영"버튼을 누르면 현재 시간에 해당하는 timeslot이 있는지 확인
        binding?.btnTakepicture?.setOnClickListener {
            pointViewModel.checkTimeSlotAndNavigate(userId)
        }

        // firebase에서 현재시간에 해당하는 timeslot이 있으면 입실시간이므로 CameraFragment로 이동
        pointViewModel.navigateToCameraFragment.observe( viewLifecycleOwner, Observer { shouldNavigate ->
            if(shouldNavigate) {
                findNavController().navigate(R.id.action_pointFragment_to_cameraFragment)
            }
        })

        // firebase에서 현재시간에 해당하는 timeslot 없다면 입실시간이 아님
        pointViewModel.showToastEvent.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // 벌점조회리스트 layout 생성
        binding?.recPoints?.layoutManager = LinearLayoutManager(requireContext())

        // 사진을 촬영한 후에 다시 돌아왔을때 정상적으로 이미지가 firebase에 업로드 되었다면 해당 이미지와 "입실확인" 메시지 보여주기
        sharedViewModel.latestImageUrl.observe( viewLifecycleOwner, Observer { imageUrl ->
            if( imageUrl.isNotEmpty() ) {
                binding?.imgPhoto?.let { Glide.with(this).load(imageUrl).into(it) }
                binding?.txtIsCheckIn?.text = "입실이 확인되었습니다."
            }
        })

        // pointList를 관찰하여 벌점 리스트를 렌더링
        pointViewModel.pointList.observe(viewLifecycleOwner) { points ->
            binding?.recPoints?.adapter = PointAdapter(points)

            val pointSum = points.sumOf { it.point }
            binding?.txtPointSum?.text = "$pointSum 점"
        }

        // firebase에서 point 조회
        pointViewModel.loadPoints(userId)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}