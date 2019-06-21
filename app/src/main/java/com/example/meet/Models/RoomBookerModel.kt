package com.example.meet.Models

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar

class RoomBookerModel{
    public var title: String
    public var info: String
    public var calendar: Calendar
    public var activeTime: Int

    companion object {
        val activeMin: HashMap<String, Int> = hashMapOf(
            "30 min" to 30,
            "1 hour" to 60,
            "1 half hour" to 90,
            "2 hours" to 120,
            "2 half hours" to 150,
            "3 hours" to 180
        )
    }

    constructor(title: String, info: String, calendar: Calendar, activeTime: String) {
        this.title = title
        this.info = info
        this.calendar = calendar
        this.activeTime = activeMin[activeTime]!!

    }

    constructor(title: String, info: String, date: DateModel, time: TimeModel, activeTime: String) {
        this.title = title
        this.info = info
        this.calendar = Calendar.getInstance()
        this.calendar.set(date.year,date.month,date.day,time.hour,time.minute)
        this.activeTime = activeMin[activeTime]!!
    }

    constructor(title: String, info: String, calendar: Calendar, activeTime: Int) {
        this.title = title
        this.info = info
        this.calendar = calendar
        this.activeTime = activeTime
    }

    @SuppressLint("SimpleDateFormat")
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is RoomBookerModel) return false
        val oth = other

        val simpleDateFormat = SimpleDateFormat("yyyy MM dd")

        val otherActive: Calendar = Calendar.getInstance()
        otherActive.timeInMillis = 0
        otherActive.timeInMillis += oth.calendar.timeInMillis + (oth.activeTime * 60 * 1000)

        val thisActive: Calendar = Calendar.getInstance()
        thisActive.timeInMillis = 0
        thisActive.timeInMillis += this.calendar.timeInMillis + (activeTime * 60 * 1000)

        this.calendar.set(Calendar.SECOND, 0)
        otherActive.set(Calendar.SECOND, 0)
        oth.calendar.set(Calendar.SECOND, 0)
        thisActive.set(Calendar.SECOND,0)

        if (simpleDateFormat.format(oth.calendar.time) == simpleDateFormat.format(this.calendar.time)) {
             if(this.calendar.timeInMillis >= oth.calendar.timeInMillis
                    && this.calendar.timeInMillis <= otherActive.timeInMillis){
                 return true
             }
            if (thisActive.timeInMillis >= oth.calendar.timeInMillis
                && this.calendar.timeInMillis <= oth.calendar.timeInMillis) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + calendar.hashCode()
        result = 31 * result + activeTime
        result = 31 * result + activeMin.hashCode()
        return result
    }

}