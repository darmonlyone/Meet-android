package com.example.meet.UI.Schedule.Daily

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DailyDateItem @SuppressLint("SimpleDateFormat") constructor(date: Date) {
    var date: Date
    var day: String
    var dateOfDay: String
    var year: String
    var month: String

   init {

        val dayFormat = SimpleDateFormat("EEE")
        val dateFormat = SimpleDateFormat("dd")
        val yearFormat = SimpleDateFormat("yyyy")
        val monthFormat = SimpleDateFormat("MM")
        day = (dayFormat.format(date)).toUpperCase()
       dateOfDay = dateFormat.format(date)
       year = yearFormat.format(date)
       month = monthFormat.format(date)
        this.date = date
   }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is DailyDateItem)
            return false
        val ob: DailyDateItem = other
        return day == ob.day &&
                dateOfDay == ob.dateOfDay &&
                year == ob.year &&
                month == ob.month
    }

    override fun hashCode(): Int {
        var result = day.hashCode()
        result = 31 * result + dateOfDay.hashCode()
        result = 31 * result + year.hashCode()
        result = 31 * result + month.hashCode()
        return result
    }
}