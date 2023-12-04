package com.example.kaustudyroom.pages.StudyRoomTimeTable

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentStudyRoomTimeTableBinding
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudyRoomTimeTableFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    private val selectedTimeSlots = mutableSetOf<String>()

    var binding: FragmentStudyRoomTimeTableBinding?= null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimetableAdapter










    private var timeTables = arrayOf(
        StudyroomTimeTable("09-10", 1, Estate.Available, false),
        StudyroomTimeTable("10-11", 2, Estate.Available,false),
        StudyroomTimeTable("11-12", 3, Estate.Available,false),
        StudyroomTimeTable("12-13", 4, Estate.Available,false),
        StudyroomTimeTable("13-14", 5, Estate.Available,false),
        StudyroomTimeTable("14-15", 6, Estate.Available,false),
        StudyroomTimeTable("15-16", 7, Estate.Available,false),
        StudyroomTimeTable("16-17", 8, Estate.Available,false),
        StudyroomTimeTable("17-18", 9, Estate.Available,false),
        StudyroomTimeTable("18-19", 10, Estate.Available,false),
        StudyroomTimeTable("19-20", 11, Estate.Available,false),
        StudyroomTimeTable("20-21", 12, Estate.Available,false),
        StudyroomTimeTable("21-22", 13, Estate.Available,false)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRoomTimeTableBinding.inflate(layoutInflater)
        return binding?.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localDate = viewModel.localDate
        val floor = viewModel.floor.value
        val roomName = viewModel.studyRoomName.value
        Log.d("localDate : ","$localDate")
        Log.d("floor : ","$floor")
        Log.d("roomName : ","$roomName")


        val timeTableDBRef = FirebaseDatabase.getInstance().reference.child("floor").child("$floor").child("$roomName").child("$localDate")

        timeTableDBRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (timeTableSnapshot in dataSnapshot.children) {
                    val timeSlot = timeTableSnapshot.key.toString()
                    Log.d("timeTable 상태 변화 timeSlot", "$timeSlot")
                    for (timeTable in timeTables) {
                        if (timeTable.time == timeSlot) {
                            timeTable.state = Estate.InUse
                        }
                    }
                }
                binding?.recTimetable?.adapter = TimetableAdapter(timeTables) { timeSlot, isSelected ->
                    handleTimeSlotSelection(timeSlot, isSelected)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })


        binding?.recTimetable?.layoutManager = LinearLayoutManager(requireContext())
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