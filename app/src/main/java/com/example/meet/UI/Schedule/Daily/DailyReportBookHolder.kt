package com.example.meet.UI.Schedule.Daily

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.meet.R

class DailyReportBookHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var titleTextView: TextView = itemView.findViewById(R.id.daily_item_title)
    var infoTextView: TextView = itemView.findViewById(R.id.daily_item_information)
    var timeTextView: TextView = itemView.findViewById(R.id.daily_item_time)

    @SuppressLint("SetTextI18n")
    fun setView(dailyItem: DailyReportItem){
        titleTextView.text = dailyItem.title
        infoTextView.text = dailyItem.info
        timeTextView.text = dailyItem.time + " - " + dailyItem.timeArrival
    }
}
