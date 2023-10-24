package com.example.kaustudyroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kaustudyroom.databinding.ActivityMainBinding
import com.example.kaustudyroom.databinding.LoginPageBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : LoginPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }


    }
}