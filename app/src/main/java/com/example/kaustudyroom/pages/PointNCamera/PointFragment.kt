package com.example.kaustudyroom.pages.PointNCamera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.databinding.FragmentPointBinding
import com.bumptech.glide.Glide
import com.example.kaustudyroom.R

class PointFragment : Fragment() {
    var binding: FragmentPointBinding?= null

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val points = arrayOf(
        Point("2023-10-10", "2층 스터디룸 A", 1, "지각 입실"),
        Point("2023-10-15", "2층 스터디룸 B-1", 2, "당일 미입실"),
        Point("2023-10-23", "3층 스터디룸 C-1", 2, "당일 미입실")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointBinding.inflate(layoutInflater)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnTakepicture?.setOnClickListener {
            findNavController().navigate(R.id.action_pointFragment_to_cameraFragment)
        }
        binding?.recPoints?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recPoints?.adapter = PointAdapter(points)

        sharedViewModel.latestImageUrl.observe( viewLifecycleOwner, Observer { imageUrl ->
            if( imageUrl.isNotEmpty() ) {
                binding?.imgPhoto?.let { Glide.with(this).load(imageUrl).into(it) }
                binding?.txtIsCheckIn?.text = "입실이 확인되었습니다."
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}