package com.example.kaustudyroom.pages.PointNCamera

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.databinding.ListPointBinding

class PointAdapter(val point: MutableList<Point>): RecyclerView.Adapter<PointAdapter.Holder>() {
    // ViewHolder 객체를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPointBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    // 데이터를 가져와 ViewHolder안의 내용을 채워줌
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(point[position])
    }

    // 총 데이터의 갯수를 반환
    override fun getItemCount() = point.size

    class Holder(private val binding: ListPointBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(point: Point) {
            binding.txtDate.text = point.date
            binding.txtRoom.text = point.room
            binding.txtPoint.text = point.point.toString() + "점"
            binding.txtTimeslot.text = point.timeslot
        }
    }
}