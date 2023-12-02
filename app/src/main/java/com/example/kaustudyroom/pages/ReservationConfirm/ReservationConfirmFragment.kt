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
import com.example.kaustudyroom.viewmodel.StudyRoomDataViewModel
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ReservationConfirmFragment : Fragment() {
    val viewModel: StudyRoomDataViewModel by activityViewModels()
    var binding: FragmentReservationConfirmBinding? = null

    lateinit var calendarAdapter: CalenderAdapter
    private var calendarList = ArrayList<CalendarVO>()

    private lateinit var reservedRoomAdapter: ReservedConfirmAdapter
    private lateinit var reservedRoomList : ArrayList<ReservedRoomVO>

    private val reservedRoom = arrayOf(
        ReservedRoomVO("nJetJfyNZ5Sztj69ny940W2LJUc2","eunju lee", "lee" ," 공부해라", "사람 1 사람 2 사람 3 ", "2023-12-03","C1",2 ),
        ReservedRoomVO("nJetJfyNZ5Sztj69ny940W2LJUc2","eunju lee", "lee" ," 공부해라", "사람 1 사람 2 사람 3 ", "2023-12-03","C1",2 ),
        ReservedRoomVO("nJetJfyNZ5Sztj69ny940W2LJUc2","eunju lee", "lee" ," 공부해라", "사람 1 사람 2 사람 3 ", "2023-12-03","C1",2 ),

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

                calendarList.apply {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), week_day[i]))
                }
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