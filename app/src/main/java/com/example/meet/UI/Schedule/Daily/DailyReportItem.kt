package com.example.meet.UI.Schedule.Daily

import android.annotation.SuppressLint
import com.example.meet.Models.RoomBookerModel
import java.text.SimpleDateFormat
import java.util.*

class DailyReportItem @SuppressLint("SimpleDateFormat") constructor(roomBookerModel: RoomBookerModel) {
    var title: String = roomBookerModel.title
    var info: String = roomBookerModel.info
    var time: String
    var timeArrival: String

    init {
        val calendar = roomBookerModel.calendar
        val dateFormat = SimpleDateFormat("HH:mm")
        val calendar2 = Calendar.getInstance()
        val dateFormat2 = SimpleDateFormat("HH:mm")
        calendar2.timeInMillis = calendar.timeInMillis + (roomBookerModel.activeTime * 60 * 1000)
        time = dateFormat.format(calendar.time)
        timeArrival = dateFormat2.format(calendar2.time)
    }
}
