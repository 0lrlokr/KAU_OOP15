package com.example.kaustudyroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.databinding.FragmentStudyRoomBinding
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyRoomFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    var binding: FragmentStudyRoomBinding?= null
    var selectedFloor: String = "2층"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRoomBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = Date()            // 현재 날짜 가져오기
        val sdf = SimpleDateFormat("MM월 dd일 EEEE", Locale.KOREAN)       // 텍스트 형식 포맷
        binding?.txtToday?.text = sdf.format(currentDate)        // TextView에 현재 날짜 설정

        val radioGroup = binding?.radiogroupFloor
        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_floor2 -> {
                    updateRoomBtn("2층")
                    selectedFloor = "2층"
                }
                R.id.radio_floor3 -> {
                    updateRoomBtn("3층")
                    selectedFloor = "3층"
                }
            }
        }

        binding?.btnRoomA?.setOnClickListener {
            setupRoomButton(binding?.btnRoomA!!, "C1", "A")
        }
        binding?.btnRoomB?.setOnClickListener {
            setupRoomButton(binding?.btnRoomB!!, "C2", "B1")
        }
        binding?.btnRoomC?.setOnClickListener {
            setupRoomButton(binding?.btnRoomC!!, "C3", "B2")
        }
    }

    private fun setupRoomButton(button: Button, roomIfSecondFloor: String, roomIfThirdFloor: String) {
        button.setOnClickListener {
            val selectedRoom = if (selectedFloor === "2층") roomIfSecondFloor else roomIfThirdFloor
            selectedFloor?.let { floor ->
                viewModel.updateRoomDetails(floor, selectedRoom)
            }
            navTimeTableFrag()
        }
    }

    private fun updateRoomBtn(floor: String) {
        if (floor == "2층") {
            binding?.btnRoomA?.text = "$floor 스터디룸 C1"
            binding?.btnRoomB?.text = "$floor 스터디룸 C2"
            binding?.btnRoomC?.text = "$floor 스터디룸 C3"
        } else if (floor == "3층") {
            binding?.btnRoomA?.text = "$floor 스터디룸 A"
            binding?.btnRoomB?.text = "$floor 스터디룸 B1"
            binding?.btnRoomC?.text = "$floor 스터디룸 B2"
        }
    }

    private fun navTimeTableFrag() {
        findNavController().navigate(R.id.action_studyRoomFragment_to_studyRoomTimeTableFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}