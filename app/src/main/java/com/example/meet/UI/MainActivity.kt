package com.example.meet.UI

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.meet.R
import com.example.meet.UI.Home.HomeFragment
import com.example.meet.UI.Menu.MenuFragment
import com.example.meet.UI.Schedule.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_add -> {
                addScheduleView()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_calendar -> {
                calendarView()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_menu -> {
                menuView()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addScheduleView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, HomeFragment())
            .commit()
    }

    private fun calendarView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, ScheduleFragment())
            .commit()
    }

    private fun menuView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, MenuFragment())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        addScheduleView()
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
