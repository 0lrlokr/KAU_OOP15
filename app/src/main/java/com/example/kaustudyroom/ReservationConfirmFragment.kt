package com.example.kaustudyroom

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kaustudyroom.databinding.FragmentReservationConfirmBinding
import com.example.kaustudyroom.modelFront.CalendarVO
import com.example.kaustudyroom.pages.ReservationConfirm.CalenderAdapter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ReservationConfirmFragment : Fragment() {

    private var _binding: FragmentReservationConfirmBinding? = null
    private val binding get() = _binding!!

    lateinit var calendarAdapter: CalenderAdapter
    private var calendarList = ArrayList<CalendarVO>()

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
        _binding = FragmentReservationConfirmBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val week_day : Array<String> = resources.getStringArray(R.array.calendar_day)

        calendarAdapter = CalenderAdapter(calendarList)

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 MM월").withLocale(Locale.forLanguageTag("ko"))

            val localDate = LocalDateTime.now().format(monthFormat)
            binding.textYearMonth.text = localDate

            var preSunday : LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
            for(i in 0..6){
                Log.d("날짜만 :: ", week_day[i])

                calendarList.apply {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat),week_day[i]))
                }
                Log.d("저번 주 일요일을 기준으로 시작합니다. ", preSunday.plusDays(i.toLong()).format(dateFormat))
            }
        }
        binding.weekRecycler.adapter = calendarAdapter
        binding.weekRecycler.layoutManager = GridLayoutManager(context,7)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}