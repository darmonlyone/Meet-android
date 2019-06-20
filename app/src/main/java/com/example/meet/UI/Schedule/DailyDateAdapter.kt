package com.example.meet.UI.Schedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meet.R



class DailyDateAdapter(val dailyFragment: DailyFragment) : RecyclerView.Adapter<DailyDateHolder>() {

    var listDailyItems :ArrayList<DailyDateItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyDateHolder {

        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_date, parent, false)

        return DailyDateHolder(v,this)
    }

    override fun onBindViewHolder(holder: DailyDateHolder, position: Int) {
        val dailyItem = listDailyItems[position]
        holder.setView(dailyItem)
    }

    override fun getItemCount(): Int {
        if (listDailyItems.isEmpty())
            return 0
        return listDailyItems.size
    }


}
