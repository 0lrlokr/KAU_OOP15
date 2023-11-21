package com.example.kaustudyroom.pages.StudyRoomTimeTable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentStudyRoomTimeTableBinding

class StudyRoomTimeTableFragment : Fragment() {
    var binding: FragmentStudyRoomTimeTableBinding?= null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimetableAdapter

    private val timeTable = arrayOf(
        StudyroomTimeTable("09-10", 1, Estate.Available),
        StudyroomTimeTable("10-11", 2, Estate.Available),
        StudyroomTimeTable("11-12", 3, Estate.InUse),
        StudyroomTimeTable("12-13", 4, Estate.InUse),
        StudyroomTimeTable("13-14", 5, Estate.InUse),
        StudyroomTimeTable("14-15", 6, Estate.Available),
        StudyroomTimeTable("15-16", 7, Estate.InUse),
        StudyroomTimeTable("16-17", 8, Estate.Available),
        StudyroomTimeTable("17-18", 9, Estate.NotAvailable),
        StudyroomTimeTable("18-19", 10, Estate.NotAvailable),
        StudyroomTimeTable("19-20", 11, Estate.Available),
        StudyroomTimeTable("20-21", 12, Estate.InUse),
        StudyroomTimeTable("21-22", 13, Estate.InUse)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRoomTimeTableBinding.inflate(layoutInflater)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recTimetable?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recTimetable?.adapter = TimetableAdapter(timeTable)

        binding?.btnChoose?.setOnClickListener {
            findNavController().navigate(R.id.action_studyRoomTimeTableFragment_to_additionalInformationFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}