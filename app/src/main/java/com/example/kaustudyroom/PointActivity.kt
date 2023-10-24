package com.example.kaustudyroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaustudyroom.databinding.ActivityPointBinding

class PointActivity : AppCompatActivity() {
    val points = arrayOf(
        Point("2023-10-10", "2층 스터디룸 A", 1, "지각 입실"),
        Point("2023-10-15", "2층 스터디룸 B-1", 2, "당일 미입실"),
        Point("2023-10-23", "3층 스터디룸 C-1", 2, "당일 미입실")
    )

    lateinit var binding: ActivityPointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recPoints.layoutManager = LinearLayoutManager(this)
        binding.recPoints.adapter = PointAdapter(points)
    }
}