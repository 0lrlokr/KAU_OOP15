package com.example.kaustudyroom.pages.StudyRoomTimeTable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.ListTimetableBinding

class TimetableAdapter(private val timeTable: Array<StudyroomTimeTable>, private val onTimeSlotSelected: (String, Boolean) -> Unit) :RecyclerView.Adapter<TimetableAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListTimetableBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(timeTable[position])

        val timeSlot = timeTable[position]
        holder.toggleButton.setOnCheckedChangeListener(null)
        holder.toggleButton.isChecked = timeSlot.isSelected
        holder.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            onTimeSlotSelected(timeSlot.time, isChecked)
        }
    }

    override fun getItemCount() = timeTable.size


    class Holder(private val binding : ListTimetableBinding) : RecyclerView.ViewHolder(binding.root){
        val toggleButton: ToggleButton = binding.btnStudyroomState
        fun bind(timeTable: StudyroomTimeTable){
            binding.btnTime.text = timeTable.time

            // btnStudyroomState에 관한 것으로 묶을 수 있다면 묶기
            binding.btnStudyroomState.text = timeTable.state.toString()

            binding.btnStudyroomState.isClickable = timeTable.state == Estate.Available


            //이 코드 어떻게 클린하게 하는지 .. (질문)
            when(timeTable.state){
                Estate.InUse -> {
                    binding.btnStudyroomState.setBackgroundResource(R.drawable.gray_color)
                }

                Estate.NotAvailable ->{
                    binding.btnStudyroomState.setBackgroundResource(R.drawable.gray_color)
                }
                Estate.Available ->{
                    binding.btnStudyroomState.setBackgroundResource(R.drawable.skyblue_color)
                    binding.btnStudyroomState.textOff = timeTable.state.toString()
                }

            }
        }
    }
}