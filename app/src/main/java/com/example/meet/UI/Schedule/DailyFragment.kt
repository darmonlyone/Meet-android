package com.example.meet.UI.Schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meet.Models.RoomBookerModel
import com.example.meet.R
import com.example.meet.Services.BookService
import kotlinx.android.synthetic.main.fragment_schedule_daily.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.support.v7.widget.LinearSmoothScroller
import android.util.DisplayMetrics



class DailyFragment : AppCompatDialogFragment() {

    private lateinit var bookService: BookService
    private lateinit var dailyReportRecyclerView: RecyclerView
    private lateinit var dailyDateRecyclerView: RecyclerView
    private lateinit var dailyReportAdapter: DailyReportAdapter
    private lateinit var dailyDateAdapter: DailyDateAdapter

    lateinit var choosingDate: Date

    private val reportDates: ArrayList<RoomBookerModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyReportRecyclerView = view.findViewById(R.id.daily_report_recycleView)
        dailyDateRecyclerView = view.findViewById(R.id.daily_date_recycleView)

        bookService = BookService()
        choosingDate = Date()
        choosing_date_textView.text = formattedDate("dd EEE yyyy", choosingDate)

        initDailyView()
        setupDateSelector()

    }

    private fun initDailyView() {
        bookService.get{
            updateAdapter(it)
        }

        dailyReportAdapter = DailyReportAdapter()
        dailyReportRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dailyReportRecyclerView.adapter = dailyReportAdapter

        dailyDateAdapter = DailyDateAdapter(this)
        dailyDateRecyclerView.layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        dailyDateRecyclerView.adapter = dailyDateAdapter
    }

    @SuppressLint("SimpleDateFormat")
    fun updateAdapter(roomBookerModel: RoomBookerModel): Boolean {
        if (reportDates.contains(roomBookerModel)){
            return false
        }

        reportDates.add(roomBookerModel)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        if (dateFormat.format(choosingDate) == dateFormat.format(roomBookerModel.calendar.time)) {
            dailyReportAdapter.listDailyItems.add(DailyReportItem(roomBookerModel))
            dailyReportAdapter.notifyDataSetChanged()
        }
        return true
    }

    @SuppressLint("SimpleDateFormat")
    fun updateReport(){
        dailyReportAdapter.listDailyItems.clear()
        for (roomBookerModel in reportDates) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            if (dateFormat.format(choosingDate) == dateFormat.format(roomBookerModel.calendar.time)) {
                dailyReportAdapter.listDailyItems.add(DailyReportItem(roomBookerModel))
            }
        }
        dailyReportAdapter.notifyDataSetChanged()

        choosing_date_textView.text = formattedDate("dd EEE yyyy",choosingDate)
    }

    private fun setupDateSelector(){
        val calendar = Calendar.getInstance()
        for (i in 0..29){
            dailyDateAdapter.listDailyItems.add(DailyDateItem(date = calendar.time))
            calendar.add(Calendar.DATE,1)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formattedDate(simpleFormat:String, date: Date ):String{
        val dateFormat = SimpleDateFormat(simpleFormat)
        return dateFormat.format(date)
    }
}
