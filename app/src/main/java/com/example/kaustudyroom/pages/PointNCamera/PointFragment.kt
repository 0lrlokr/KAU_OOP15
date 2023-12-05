package com.example.kaustudyroom.pages.PointNCamera

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.databinding.FragmentPointBinding
import com.bumptech.glide.Glide
import com.example.kaustudyroom.R
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.example.kaustudyroom.viewmodel.PointViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PointFragment : Fragment() {
    var binding: FragmentPointBinding?= null
    private val authViewModel = AuthViewModel()
    val userId: String
        get() = authViewModel.getUserIdDirectly()

    private val pointViewModel = PointViewModel()

    private val sharedViewModel: SharedViewModel by activityViewModels()

//    private val points = arrayOf(
//        Point("2023-10-10", "2층 스터디룸 A", 1),
//        Point("2023-10-15", "2층 스터디룸 B-1", 2),
//        Point("2023-10-23", "3층 스터디룸 C-1", 2)
//    )

    private val pointList = mutableListOf<Point>(
        Point("","",0, "")
    )
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
        binding?.btnTakepicture?.setOnClickListener {
            pointViewModel.checkTimeSlotAndNavigate(userId)
        }

        // db에서 현재시간에 해당하는 timeslot이 있으면 입실시간이므로 camera로 이동
        pointViewModel.navigateToCameraFragment.observe( viewLifecycleOwner, Observer { shouldNavigate ->
            if(shouldNavigate) {
                findNavController().navigate(R.id.action_pointFragment_to_cameraFragment)
            }
        })
        // db에서 현재시간에 해당하는 timeslot 없다면 입실시간이 아님
        pointViewModel.showToastEvent.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        binding?.recPoints?.layoutManager = LinearLayoutManager(requireContext())
//        binding?.recPoints?.adapter = PointAdapter(points)

        sharedViewModel.latestImageUrl.observe( viewLifecycleOwner, Observer { imageUrl ->
            if( imageUrl.isNotEmpty() ) {
                binding?.imgPhoto?.let { Glide.with(this).load(imageUrl).into(it) }
                binding?.txtIsCheckIn?.text = "입실이 확인되었습니다."
            }
        })

        // DB에서 값 가져오기
        val pointDBRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("User").child(userId)
        pointDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var pointSum = 0
                pointList.clear()
                for ( userSnapshot in dataSnapshot.children ) {
                    val date = userSnapshot.key.toString()
                    for ( dateSnapshot in userSnapshot.children ) {
                        val point = dateSnapshot.child("point").value.toString().toInt()
                        if (point > 0) {
                            pointSum += point
                            val timeslot = dateSnapshot.key.toString()
                            val floor = dateSnapshot.child("floor").value
                            val room = dateSnapshot.child("room").value
                            val roomName = floor.toString() + "층 스터디룸" + room
                            val pointListSet = Point(date, roomName, point, timeslot)
                            pointList.add(pointListSet)
                            Log.d("pointListSet", "$pointListSet")
                        }
                    }
                }
                binding?.recPoints?.adapter = PointAdapter(pointList)
                binding?.txtPointSum?.text = pointSum.toString() + "점"
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}