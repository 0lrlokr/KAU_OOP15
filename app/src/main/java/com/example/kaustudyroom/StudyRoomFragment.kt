package com.example.kaustudyroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.databinding.FragmentStudyRoomBinding

class StudyRoomFragment : Fragment() {

    var binding: FragmentStudyRoomBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRoomBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnStudyRoomTimeTable?.setOnClickListener{
            findNavController().navigate(R.id.action_studyRoomFragment_to_studyRoomTimeTableFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}