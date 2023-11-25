package com.example.kaustudyroom.pages.StudyRoomTimeTable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentStudyRoomTimeTableBinding
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel

class StudyRoomTimeTableFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    private val selectedTimeSlots = mutableSetOf<String>()

    var binding: FragmentStudyRoomTimeTableBinding?= null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimetableAdapter

    private val timeTable = arrayOf(
        StudyroomTimeTable("09-10", 1, Estate.Available, false),
        StudyroomTimeTable("10-11", 2, Estate.Available,false),
        StudyroomTimeTable("11-12", 3, Estate.InUse,false),
        StudyroomTimeTable("12-13", 4, Estate.InUse,false),
        StudyroomTimeTable("13-14", 5, Estate.InUse,false),
        StudyroomTimeTable("14-15", 6, Estate.Available,false),
        StudyroomTimeTable("15-16", 7, Estate.InUse,false),
        StudyroomTimeTable("16-17", 8, Estate.Available,false),
        StudyroomTimeTable("17-18", 9, Estate.NotAvailable,false),
        StudyroomTimeTable("18-19", 10, Estate.NotAvailable,false),
        StudyroomTimeTable("19-20", 11, Estate.Available,false),
        StudyroomTimeTable("20-21", 12, Estate.InUse,false),
        StudyroomTimeTable("21-22", 13, Estate.InUse,false)
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
        binding?.recTimetable?.adapter = TimetableAdapter(timeTable) { timeSlot, isSelected ->
            handleTimeSlotSelection(timeSlot, isSelected)
        }

        binding?.recTimetable?.layoutManager = LinearLayoutManager(requireContext())
        // binding?.recTimetable?.adapter = TimetableAdapter(timeTable)
        viewModel.combinedFloorAndRoomName.observe(viewLifecycleOwner) { combinedText ->
            binding?.txtStudyroom?.text = combinedText
        }

        binding?.btnChoose?.setOnClickListener {
            if (selectedTimeSlots.size <= 3) {
                viewModel.updateTimeSlots(selectedTimeSlots.toList())
                findNavController().navigate(R.id.action_studyRoomTimeTableFragment_to_additionalInformationFragment)
                selectedTimeSlots.clear()
            } else {
                // Handle error: more than 3 time slots selected
            }
        }
    }
    private fun handleTimeSlotSelection(timeSlot: String, isSelected: Boolean) {
        if (isSelected) {
            if (selectedTimeSlots.size < 3) {
                selectedTimeSlots.add(timeSlot)
            } else {
                // Show an error message or revert the selection
            }
        } else {
            selectedTimeSlots.remove(timeSlot)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}