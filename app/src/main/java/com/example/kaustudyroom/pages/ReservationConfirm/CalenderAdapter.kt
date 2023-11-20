package com.example.kaustudyroom.pages.ReservationConfirm

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.ItemCalendarListBinding
import com.example.kaustudyroom.databinding.ItemCalendarListBinding.inflate
import com.example.kaustudyroom.modelFront.CalendarVO
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalenderAdapter(private val cList :List<CalendarVO>):
        RecyclerView.Adapter<CalenderAdapter.CalendarViewHolder>(){
        class CalendarViewHolder(private val binding : ItemCalendarListBinding):
                RecyclerView.ViewHolder(binding.root){
                    @RequiresApi(Build.VERSION_CODES.O)
                    fun bind(item: CalendarVO){
                        binding.date.text = item.cl_date
                        binding.day.text = item.cl_day

                        var today = binding.date.text

                        //오늘 날짜 적용하기
                        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))
                        //캘린더에 오늘 날짜 색상 적용
                        if(today == now){
                            binding.weekCardview.setBackgroundResource(R.drawable.background_blue)
                        }
                    }
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(cList[position])
    }

    override fun getItemCount(): Int {
        return cList.size
    }

}
