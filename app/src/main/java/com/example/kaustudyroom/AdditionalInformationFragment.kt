package com.example.kaustudyroom

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.databinding.FragmentAdditionalInformationBinding
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel

class AdditionalInformationFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    var binding: FragmentAdditionalInformationBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdditionalInformationBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.combinedFloorAndRoomName.observe(viewLifecycleOwner) { combinedText ->
            binding?.txtStudyroom?.text = combinedText
        }
        // viewmodel 확인용 Logcat print
        viewModel.timeSlots.observe(viewLifecycleOwner) { timeSlots ->
            Log.d("Fragment3", "Time Slots: $timeSlots")
        }

        binding?.btnReservation?.setOnClickListener {
            viewModel.updateUserDetails(binding?.txtName?.text.toString(), binding?.txtCompanion?.text.toString(), binding?.txtPurpose?.text.toString())
            // 버튼 클릭 시, 데이터베이스에 데이터 넣기 함수
            viewModel.addReserveData()
            viewModel.addUserReservedData()

            findNavController().navigate(R.id.action_additionalInformationFragment_to_reservationConfirmFragment)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}