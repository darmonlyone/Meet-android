package com.example.meet.UI.Schedule.Weekly

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.meet.Models.TimeModel
import com.example.meet.R
import kotlinx.android.synthetic.main.fragment_schedule_weekly.*
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment : AppCompatDialogFragment() {

    private lateinit var choosingCalenWeek: Calendar
    private lateinit var timeTableRow: TableRow
    private lateinit var timeTable: TableLayout
    private var limitWeekClick: Int = 4

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_weekly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choosingCalenWeek = Calendar.getInstance()
        setupWeekView()
        setUpListener()
        setupTextTable(view)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setupWeekView() {
        val calendar2 = Calendar.getInstance()
        choosingCalenWeek.set(Calendar.DAY_OF_WEEK, choosingCalenWeek.firstDayOfWeek)

        calendar2.time = choosingCalenWeek.time
        choosingCalenWeek.add(Calendar.DATE,1)
        calendar2.add(Calendar.DATE,7)

        val sampleFormat = SimpleDateFormat("dd/MM/yyyy")
        week_select_text_view.text = sampleFormat.format(choosingCalenWeek.time) + "  -  " + sampleFormat.format(calendar2.time)
    }
    private fun setUpListener(){
        right_imageButton.setOnClickListener{
            if (limitWeekClick > 0) {
                choosingCalenWeek.add(Calendar.WEEK_OF_MONTH, 1)
                setupWeekView()
                limitWeekClick -= 1
            }
        }
        left_imageButton.setOnClickListener{
            if (limitWeekClick < 5) {
                choosingCalenWeek.add(Calendar.WEEK_OF_MONTH, -1)
                setupWeekView()
                limitWeekClick += 1
            }
        }
    }

    @SuppressLint( "InflateParams")
    private fun setupTextTable(view: View) {
        timeTable = view.findViewById(R.id.time_tableLayout)
        timeTableRow = view.findViewById(R.id.time_table_row)
        timeTable.removeAllViews()
        val start = 9
        val end = 18
        for(i in start..end){
            val duliplicate = LayoutInflater.from(view.context).inflate(R.layout.time_table_row, null)
            (duliplicate.findViewById(R.id.weekly_time_textView) as TextView).text = TimeModel(i,0).toString()
            timeTable.addView(duliplicate)

            if (i != end) {
                val duliplicate30 = LayoutInflater.from(view.context).inflate(R.layout.time_table_row, null)
                val timeTextView30 = duliplicate30.findViewById(R.id.weekly_time_textView) as TextView
                timeTextView30.text = TimeModel(i, 30).toString()
                timeTable.addView(duliplicate30)
            }
        }
    }
}