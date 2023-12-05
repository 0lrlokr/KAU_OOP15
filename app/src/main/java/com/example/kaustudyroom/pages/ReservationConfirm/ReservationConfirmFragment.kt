package com.example.kaustudyroom.pages.ReservationConfirm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentReservationConfirmBinding
import com.example.kaustudyroom.modelFront.CalendarVO
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ReservationConfirmFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    var binding: FragmentReservationConfirmBinding? = null

    // 로그인 후, userId가져오기
    private val authViewModel = AuthViewModel()
    val userId: String
        get() = authViewModel.getUserIdDirectly()

    //DB에서 정보 가져오기
    private val reservedRoomsList = mutableListOf<ReservedRoomVO>(
        ReservedRoomVO("eunju lee", "lee" ," 공부해라","10-11","2023-12-04","C1","2"),
    )


    lateinit var calendarAdapter: CalenderAdapter
    private var calendarList = ArrayList<CalendarVO>()

    private lateinit var reservedRoomAdapter: ReservedConfirmAdapter
    private lateinit var reservedRoomList : ArrayList<ReservedRoomVO>





    companion object{
        fun newInstance() = ReservationConfirmFragment()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        //onCreateView에서 List에 add해야 UI에서 리스트 반환
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationConfirmBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // viewmodel 확인용 Logcat print
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            Log.d("Fragment3", "name: $name")
        }
        viewModel.companions.observe(viewLifecycleOwner) { companions ->
            Log.d("Fragment3", "companions: $companions")
        }
        viewModel.purpose.observe(viewLifecycleOwner) { purpose ->
            Log.d("Fragment3", "purpose: $purpose")
        }

        reservedRoomAdapter = ReservedConfirmAdapter(reservedRoomsList)
        val week_day : Array<String> = resources.getStringArray(R.array.calendar_day)

        calendarAdapter = CalenderAdapter(calendarList)

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("dd")
                .withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 MM월")
                .withLocale(Locale.forLanguageTag("ko"))

            val localDate = LocalDateTime.now().format(monthFormat)
            binding?.textYearMonth?.text = localDate

            var preSunday : LocalDateTime = LocalDateTime.now()
                .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
            for(i in 0..6){

                calendarList.apply {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
            }
        }
        binding?.weekRecycler?.adapter = calendarAdapter
        binding?.weekRecycler?.layoutManager = GridLayoutManager(context, 7)

        binding?.reservationRecycler?.layoutManager = LinearLayoutManager(requireContext())

        // DB에서 값 가져오기
        val reservedRoomDBRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("User").child("$userId")

        reservedRoomDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reservedRoomsList.clear() // 기존 데이터를 모두 제거
                for (userSnapshot in dataSnapshot.children) {
                    val date = userSnapshot.key.toString()
                    for (dateSnapshot in userSnapshot.children) {
                        val timeSlot = dateSnapshot.key.toString()
                        val userName = dateSnapshot.child("userName").getValue(String::class.java) ?: ""
                        val companions = dateSnapshot.child("companions").getValue(String::class.java) ?: ""
                        val purpose = dateSnapshot.child("purpose").getValue(String::class.java) ?: ""
                        val roomName = dateSnapshot.child("room").getValue(String::class.java) ?: ""
                        val floor = dateSnapshot.child("floor").getValue(String::class.java) ?: ""
                        val reservedRoom = ReservedRoomVO("$userName", "$companions", "$purpose", "$timeSlot", "$date", "$roomName", "$floor")
                        reservedRoomsList.add(reservedRoom)
                        Log.d("reservedRoom뭐야","$reservedRoom")
                    }
                }

                // 데이터가 변경될 때마다 어댑터에 새로운 데이터를 설정
                reservedRoomAdapter.notifyDataSetChanged()
                Log.d("reservedRooms List : ","$reservedRoomsList")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

        binding?.reservationRecycler?.adapter = reservedRoomAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}