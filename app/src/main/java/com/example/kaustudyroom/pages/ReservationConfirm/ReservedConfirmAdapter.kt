package com.example.kaustudyroom.pages.ReservationConfirm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.R
import com.example.kaustudyroom.databinding.ItemReservedListBinding
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.google.firebase.database.FirebaseDatabase

class ReservedConfirmAdapter(val rList: MutableList<ReservedRoomVO>):
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
        // 로그인 후, userId가져오기
        private val authViewModel = AuthViewModel()
        val userId: String
            get() = authViewModel.getUserIdDirectly()


        val databaseReference = FirebaseDatabase.getInstance().reference

        fun bind(rList : ReservedRoomVO){
            binding.reservedTime.text = "| "+ rList.timeSlot
            binding.reservedRoom.text = rList.floor + "층 " + rList.room
            binding.roomCompanion.text = "동반인: "+rList.companions
            binding.roomPurpose.text = "목적 : "+rList.purpose
            binding.deleteBtn.setOnClickListener {
                val clickedTimeSlot = rList.timeSlot
                //1. User Table에서 삭제
                val userReservationRef = databaseReference.child("User").child("$userId").child("${rList.date}").child("${rList.timeSlot}")
                userReservationRef.removeValue()
                Log.d("userId :: ","$userId")

                //2.floor table에서 삭제
                val timeTableReservationRef = databaseReference.child("floor").child("${rList.floor}").child("${rList.room}").child("${rList.date}").child("${rList.timeSlot}")
                timeTableReservationRef.removeValue()

            }

            when(rList.room){
                "C1" -> binding.roomImgView.setImageResource(R.drawable._c1)
                "C2" -> binding.roomImgView.setImageResource(R.drawable._c2)
                "C3" -> binding.roomImgView.setImageResource(R.drawable._c3)
                "A" -> binding.roomImgView.setImageResource(R.drawable._a1)
                "B1" -> binding.roomImgView.setImageResource(R.drawable._b1)
                "B2" -> binding.roomImgView.setImageResource(R.drawable._b2)
            }
        }
    }
    fun addItems(items: List<ReservedRoomVO>) {
        rList.clear()
        rList.addAll(items)
        notifyDataSetChanged()
    }

}