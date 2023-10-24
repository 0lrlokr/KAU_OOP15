package com.example.kaustudyroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kaustudyroom.databinding.ActivityMainBinding
import com.example.kaustudyroom.databinding.LoginPageBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : LoginPageBinding

    private val id = "asdf"   //로그인 할 수 있는 유일한 아이디
    private val password = "1234"   //로그인 할 수 있는 유일한 비밀번호
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            var textId = binding.id.text.toString()
            var textPw = binding.password.text.toString()

            if (textId == id && textPw == password) {
                val nextIntent = Intent(this, MainActivity::class.java)
                startActivity(nextIntent)
            } else {
                if (textId.isNullOrEmpty() || textPw.isNullOrEmpty()) {
                    Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (textId != id) {
                    Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                } else if (textPw != password) {
                    Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}