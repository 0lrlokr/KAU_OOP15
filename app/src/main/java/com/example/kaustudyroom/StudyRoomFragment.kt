package com.example.kaustudyroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.databinding.FragmentStudyRoomBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyRoomFragment : Fragment() {

    var binding: FragmentStudyRoomBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRoomBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateTextView = binding?.txtToday
        val currentDate = Date()            // 현재 날짜 가져오기
        val sdf = SimpleDateFormat("MM월 dd일 EEEE", Locale.KOREAN)       // 텍스트 형식 포맷
        val formattedDate = sdf.format(currentDate)

        dateTextView?.text = formattedDate              // TextView에 현재 날짜 설정

        val radioGroup = binding?.radiogroupFloor
        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_floor2 -> {
                    updateRoomButtons("2층")
                }
                R.id.radio_floor3 -> {
                    updateRoomButtons("3층")
                }
            }
        }

        binding?.btnRoomA?.setOnClickListener {
            navTimeTableFrag()
        }
        binding?.btnRoomB?.setOnClickListener {
            navTimeTableFrag()
        }
        binding?.btnRoomC?.setOnClickListener {
            navTimeTableFrag()
        }
    }

    private fun updateRoomButtons(floor: String) {
        binding?.btnRoomA?.text = "$floor 스터디룸 A"
        binding?.btnRoomB?.text = "$floor 스터디룸 B"
        binding?.btnRoomC?.text = "$floor 스터디룸 C"
    }

    private fun navTimeTableFrag() {
        findNavController().navigate(R.id.action_studyRoomFragment_to_studyRoomTimeTableFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}