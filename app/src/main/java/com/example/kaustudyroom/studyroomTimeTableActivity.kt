package com.example.kaustudyroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaustudyroom.databinding.ActivityMainBinding
import com.example.kaustudyroom.databinding.ActivityStudyroomTimeTableBinding

class studyroomTimeTableActivity: AppCompatActivity() {

    val timeTable = arrayOf(
        StudyroomTimeTable("09-10", 1, Estate.Available),
        StudyroomTimeTable("10-11", 2, Estate.Available),
        StudyroomTimeTable("11-12", 3, Estate.InUse),
        StudyroomTimeTable("12-13", 4, Estate.InUse),
        StudyroomTimeTable("13-14", 5, Estate.InUse),
        StudyroomTimeTable("14-15", 6, Estate.Available),
        StudyroomTimeTable("15-16", 7, Estate.InUse),
        StudyroomTimeTable("16-17", 8, Estate.Available),
        StudyroomTimeTable("17-18", 9, Estate.NotAvailable),
        StudyroomTimeTable("18-19", 10, Estate.NotAvailable),
        StudyroomTimeTable("19-20", 11, Estate.Available),
        StudyroomTimeTable("20-21", 12, Estate.InUse),
        StudyroomTimeTable("21-22", 13, Estate.InUse),

    )

    lateinit var binding : ActivityStudyroomTimeTableBinding
    // Android Data Binding 라이브러리를 사용하여 생성된 클래스 ( 뷰 요소와 바인딩하고 상호작용 하기 위함 )
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimetableAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyroomTimeTableBinding.inflate(layoutInflater) // 바인딩 초기화

        setContentView(binding.root) // binding으로 XML 레이아웃 설정하기

        binding.recTimetable.layoutManager = LinearLayoutManager(this) // 어떻게 item들을 쌓을 것인가 (차곡차곡 쌓을 것)
        binding.recTimetable.adapter = TimetableAdapter(timeTable)
    }
}