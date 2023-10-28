package com.example.kaustudyroom

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kaustudyroom.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val navController = binding.frgStudyroom.getFragment<NavHostFragment>().navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment, R.id.studyRoomFragment, R.id.pointFragment, R.id.reservationConfirmFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = binding.frgStudyroom.getFragment<NavHostFragment>().navController

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun hideBottomNavigation(bool: Boolean) {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        if (bool) {
            bottomNavigation.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
        }
    }
}