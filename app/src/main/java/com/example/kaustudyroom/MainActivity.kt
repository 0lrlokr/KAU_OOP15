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

        binding.btnSwitch.setOnClickListener{
            binding.txtConfirm.text = "click confrimed "
            val nextIntent = Intent(this, studyroomTimeTableActivity::class.java)
            startActivity(nextIntent)
        }
    }
}