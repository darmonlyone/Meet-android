package com.example.meet.UI.Schedule.Daily

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meet.R

class DailyReportAdapter: RecyclerView.Adapter<DailyReportBookHolder>() {

    var listDailyItems :ArrayList<DailyReportItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyReportBookHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_report, parent, false)

        return DailyReportBookHolder(v)
    }

    override fun getItemCount(): Int {
        if (listDailyItems.isEmpty())
            return 0
        return listDailyItems.size
    }

    override fun onBindViewHolder(holder: DailyReportBookHolder, position: Int) {
        val dailyItem = listDailyItems[position]
        holder.setView(dailyItem)
    }

}