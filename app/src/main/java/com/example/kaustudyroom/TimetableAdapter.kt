package com.example.kaustudyroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.databinding.ListTimetableBinding

class TimetableAdapter(val timeTable : Array<StudyroomTimeTable>) :RecyclerView.Adapter<TimetableAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableAdapter.Holder {
        val binding = ListTimetableBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: TimetableAdapter.Holder, position: Int) {
        holder.bind(timeTable[position])
    }

    override fun getItemCount() = timeTable.size


    class Holder(private val binding : ListTimetableBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(timeTable:StudyroomTimeTable){
//             binding.요소 = when( timeTable.state){
//                 Estate.InUse -> R...
//                 Estate.NotAvailable->R...
//                 Estate.InUse -> R.. //     }
            binding.btnTime.text = timeTable.time
            binding.btnStudyroomState.text = timeTable.state.toString()
        }
    }
}