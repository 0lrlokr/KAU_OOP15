package com.example.kaustudyroom.pages.StudyRoomTimeTable

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentStudyRoomTimeTableBinding
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudyRoomTimeTableFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    private val selectedTimeSlots = mutableSetOf<String>()
    private val authViewModel = AuthViewModel()

    val userId: String
        get() = authViewModel.getUserIdDirectly()
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


        val timeTableDBRef = FirebaseDatabase.getInstance().reference.child("floor").child("$floor").child("$roomName").child("$localDate").child("")



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
                // 에러 처리(수정..)
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

        val userDBRef = FirebaseDatabase.getInstance().reference.child("User").child("$userId").child("$localDate")

        var totalReservedTime = 0

        //유저의 total reserved time을 나타내는 함수
        userDBRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                println("자식 노드 : $dataSnapshot")
                totalReservedTime = dataSnapshot.childrenCount.toInt()
                println("자식 노드의 수: $totalReservedTime")
                binding?.reservedTotalTime?.text = "Total Reserved Time :"+totalReservedTime.toString()+"h"+" / 3h"
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })



        binding?.recTimetable?.layoutManager = LinearLayoutManager(requireContext())
        viewModel.combinedFloorAndRoomName.observe(viewLifecycleOwner) { combinedText ->
            binding?.txtStudyroom?.text = combinedText
        }



        // Btn Click시,
        binding?.btnChoose?.setOnClickListener {
            Log.d("selectedTimeSlots.size+totalReservedTime","${totalReservedTime+selectedTimeSlots.size}")
            if (selectedTimeSlots.size <= 3 && selectedTimeSlots.size!=0 && totalReservedTime+selectedTimeSlots.size<4) {
                viewModel.updateTimeSlots(selectedTimeSlots.toList())
                findNavController().navigate(R.id.action_studyRoomTimeTableFragment_to_additionalInformationFragment)
                selectedTimeSlots.clear()
            } else if(selectedTimeSlots.size == 0){
                Log.d("selectedTimeSlots.size = 0이야 ","${selectedTimeSlots.size}")
                Toast.makeText(requireContext(), "1개 이상의 시간을 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(totalReservedTime+selectedTimeSlots.size>3){
                Log.d("selectedTimeSlots.size+totalReservedTime 초과 예약 불가에 걸림","${totalReservedTime+selectedTimeSlots.size}")
                Toast.makeText(requireContext(), "3시간을 초과하여 예약할 수 없습니다. ", Toast.LENGTH_SHORT).show()
            }
            else{
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