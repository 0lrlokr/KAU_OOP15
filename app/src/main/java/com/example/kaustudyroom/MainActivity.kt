package com.example.kaustudyroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kaustudyroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSwitchTimetablePage.setOnClickListener{
            binding.txtConfirm.text = "타임테이블 페이지로 이동합니다."
            val nextIntent = Intent(this, studyroomTimeTableActivity::class.java)
            startActivity(nextIntent)
        }
        binding.btnSwitchPicturePage.setOnClickListener{
            binding.txtConfirm.text = "입실확인 및 벌점조회 페이지로 이동합니다."
            val nextIntent = Intent(this, PointActivity::class.java)
            startActivity(nextIntent)
        }
    }
}