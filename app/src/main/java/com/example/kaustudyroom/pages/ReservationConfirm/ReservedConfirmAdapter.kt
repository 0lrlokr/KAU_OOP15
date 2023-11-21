package com.example.kaustudyroom.pages.ReservationConfirm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.kaustudyroom.databinding.ItemReservedListBinding
import com.example.kaustudyroom.modelFront.ReservedRoomVO

class ReservedConfirmAdapter(val rList: Array<ReservedRoomVO>):
    RecyclerView.Adapter<ReservedConfirmAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemReservedListBinding.inflate(LayoutInflater.from(parent.context))
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(rList[position])
    }

    override fun getItemCount(): Int = rList.size

    class ReservationViewHolder(private val binding: ItemReservedListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(rList : ReservedRoomVO){
            binding.reservedTime.text = "| "+ rList.time_slot
            binding.reservedRoom.text = rList.floor.toString() + "층 " + rList.room
            binding.roomCompanion.text = "동반인: "+rList.companion
            binding.roomPurpose.text = "목적 : "+rList.purpose
        }
    }

}