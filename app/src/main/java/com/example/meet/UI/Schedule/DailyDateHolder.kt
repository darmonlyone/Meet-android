package com.example.meet.UI.Schedule

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.meet.R
import java.util.*

class DailyDateHolder(itemView: View, dailyDateAdapter: DailyDateAdapter) : RecyclerView.ViewHolder(itemView) {

    lateinit var dailyItem: DailyDateItem

    var dayTextView: TextView = itemView.findViewById(R.id.daily_item_day)
    var dateTextView: TextView = itemView.findViewById(R.id.daily_item_date)
    var view:LinearLayout = itemView.findViewById(R.id.view_linearLayout)

    private val onClickListener:View.OnClickListener = View.OnClickListener {
        dailyDateAdapter.dailyFragment.choosingDate = dailyItem.date
        dailyDateAdapter.dailyFragment.updateReport()
    }

    init {
        view.setOnClickListener(onClickListener)
    }

    @SuppressLint("SetTextI18n")
    fun setView(dailyItem: DailyDateItem) {
        this.dailyItem = dailyItem
        dayTextView.text = dailyItem.day
        dateTextView.text = dailyItem.dateOfDay
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        val ot = other as DailyDateHolder
        return dailyItem == ot.dailyItem
    }

    override fun hashCode(): Int {
        var result = dailyItem.hashCode()
        result = 31 * result + dayTextView.hashCode()
        result = 31 * result + dateTextView.hashCode()
        result = 31 * result + onClickListener.hashCode()
        return result
    }

}

