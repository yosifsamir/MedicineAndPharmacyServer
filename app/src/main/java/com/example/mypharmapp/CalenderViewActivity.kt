package com.example.mypharmapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calender_view.*

class CalenderViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender_view)

//        calendar_view.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
//            Intent dailyPlanIntent = new Intent(this, DailyPlanActivity.class);
//            dailyPlanIntent.putExtra("year", year);
//            dailyPlanIntent.putExtra("month", month);
//            dailyPlanIntent.putExtra("day", dayOfMonth);
//            startActivity(dailyPlanIntent);
//        });

        calendar_view.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(calenderView: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                Log.d("date","${year} + $month + $dayOfMonth")
                val dailyPlanIntent = Intent(this@CalenderViewActivity, DailyPlanActivity::class.java)
                dailyPlanIntent.putExtra("year", year)
                dailyPlanIntent.putExtra("month", month)
                dailyPlanIntent.putExtra("day", dayOfMonth)
                startActivity(dailyPlanIntent)
            }

        })
    }
}