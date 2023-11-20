package com.example.kaustudyroom.pages.ReservationConfirm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.FragmentReservationConfirmBinding
import com.example.kaustudyroom.modelFront.CalendarVO
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ReservationConfirmFragment : Fragment() {
    var binding: FragmentReservationConfirmBinding? = null

    lateinit var calendarAdapter: CalenderAdapter
    private var calendarList = ArrayList<CalendarVO>()

    private lateinit var reservedRoomAdapter: ReservedConfirmAdapter
    private lateinit var reservedRoomList : ArrayList<ReservedRoomVO>

    private val reservedRoom = arrayOf(
        ReservedRoomVO(1,23,"9-10", "2F C1", "사람 1 사람 2 사람 3 ", "공부 좀 하자 " ),
        ReservedRoomVO(2,23,"10-11","2F C1","사람 1 사람 2 사람 3 ", "공부공부"),
        ReservedRoomVO(3,23,"11-12", "2F C1", "사람4 사람 5 사람 6 ", "공부해라 ~ " )
    )




    companion object{
        fun newInstance() = ReservationConfirmFragment()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
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

        reservedRoomAdapter = ReservedConfirmAdapter(reservedRoom)
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
                Log.d("날짜만 :: ", week_day[i])

                calendarList.apply {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
                Log.d("저번 주 일요일을 기준으로 시작합니다. ", preSunday.plusDays(i.toLong()).format(dateFormat))
            }
        }
        binding?.weekRecycler?.adapter = calendarAdapter
        binding?.weekRecycler?.layoutManager = GridLayoutManager(context, 7)



        binding?.reservationRecycler?.layoutManager = LinearLayoutManager(requireContext())

        binding?.reservationRecycler?.adapter = reservedRoomAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}