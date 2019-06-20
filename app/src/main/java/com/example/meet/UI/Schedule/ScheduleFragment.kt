package com.example.meet.UI.Schedule

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.meet.Manager.Validator
import com.example.meet.Manager.TimeDialog
import com.example.meet.Models.DateModel
import com.example.meet.Models.RoomBookerModel
import com.example.meet.Models.TimeModel
import com.example.meet.R
import com.example.meet.Services.BookService
import com.example.meet.UI.Schedule.Daily.DailyFragment
import com.example.meet.UI.Schedule.Weekly.WeeklyFragment
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NAME_SHADOWING")
class ScheduleFragment : AppCompatDialogFragment() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var titleEditText: EditText
    private lateinit var infoEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var cancelButton: Button
    private lateinit var confirmButton: Button
    private lateinit var activeTimeSpinner: Spinner
    private lateinit var popupInputDialogView: View
    private lateinit var dailyFragment: DailyFragment
    private lateinit var weeklyFragment: WeeklyFragment

    private lateinit var validator: Validator
    private lateinit var bookService: BookService

    @SuppressLint("SetTextI18n")
    private val datePickerListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->

            dateTextView.text = "$day/ $month/ $year"
        }

    @SuppressLint("SetTextI18n")
    private val timePickerListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->

            timeTextView.text = "${"%02d".format(hour)}: ${"%02d".format(minute)}"
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookService = BookService()
        validator = Validator()

        dailyFragment = DailyFragment()
        weeklyFragment = WeeklyFragment()

        setupFragment()
        setupBookingDialogPopup()
        setupActiveTimeSpinner()
        setupAction()
    }

    private fun setupFragment() {
        setFragment(dailyFragment)
    }

    private fun setFragment(fragment:AppCompatDialogFragment){
        if (fragment is WeeklyFragment)
            fragmentManager!!.beginTransaction()
                .replace(R.id.schedule_report_frameLayout, weeklyFragment)
                .commit()
        else if(fragment is DailyFragment)
            fragmentManager!!.beginTransaction()
                .replace(R.id.schedule_report_frameLayout, dailyFragment)
                .commit()
    }


    private fun setupActiveTimeSpinner() {
        activeTimeSpinner = popupInputDialogView.findViewById(R.id.book_active_time) as Spinner

        val arraMin = RoomBookerModel.activeMin.toList().sortedBy { (_, value) ->
            value
        }.reversed().toMap()
        val spinnerArray = ArrayList<String>()
        for (i in arraMin)
            spinnerArray.add(i.key)

        spinnerArray.reverse()
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            spinnerArray
        )

        activeTimeSpinner.adapter = adapter
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun setupAction() {

        daily_button.setOnClickListener {
            setFragment(dailyFragment)
        }
        weekly_button.setOnClickListener {
            setFragment(weeklyFragment)
        }

        add_booking.setOnClickListener {
            alertDialog.show()
            titleEditText.requestFocus()
        }
        cancelButton.setOnClickListener {

            titleEditText.setText("")
            infoEditText.setText("")
            dateTextView.text = "Select Date"
            timeTextView.text = "Select Time"

            alertDialog.cancel()
        }
        confirmButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val info = infoEditText.text.toString()
            val dateText = dateTextView.text.toString()
            val timeText = timeTextView.text.toString()
            val activeTime = activeTimeSpinner.selectedItem.toString()

            if (validator.validateEmpty(title)) {
                titleEditText.requestFocus()
                return@setOnClickListener
            } else if (validator.validateEmpty(info)) {
                infoEditText.requestFocus()
                return@setOnClickListener
            } else if (validator.validateEquals(dateText, "Select Date")) {
                dateTextView.callOnClick()
                return@setOnClickListener
            } else if (validator.validateEquals(timeText, "Select Time")) {
                timeTextView.callOnClick()
                return@setOnClickListener
            }

            val date = DateModel(dateText)
            val time = TimeModel(timeText)

            val roomBookerModel = RoomBookerModel(title, info, date, time, activeTime)

            if (dailyFragment.updateAdapter(roomBookerModel)) {
                bookService.insert(roomBookerModel, onSuccessListener = {
                    titleEditText.setText("")
                    infoEditText.setText("")
                    dateTextView.text = "Select Date"
                    timeTextView.text = "Select Time"
                    alertDialog.cancel()
                })
            } else {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("This time on this have booked")
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ ->
                        timeTextView.callOnClick()
                    }
                val alert = builder.create()
                alert.show()
            }

        }
        dateTextView.setOnClickListener {
            val calmin: Calendar = Calendar.getInstance()
            val day = calmin.get(Calendar.DAY_OF_MONTH)
            val year = calmin.get(Calendar.YEAR)
            val month = calmin.get(Calendar.MONTH)
            val calmax = Calendar.getInstance()
            calmax.add(Calendar.DATE, 29)
            val datePickerDialog = DatePickerDialog(
                context!!,
                datePickerListener, year, month, day
            )
            datePickerDialog.datePicker.minDate = calmin.timeInMillis
            datePickerDialog.datePicker.maxDate = calmax.timeInMillis
            datePickerDialog.datePicker.updateDate(year, month, day)
            datePickerDialog.show()
        }

        timeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimeDialog(context!!, timePickerListener, hour, minute, true)
            timePickerDialog.updateTime(10,0)
            timePickerDialog.timePicker.setOnTimeChangedListener { timePicker: TimePicker, hour: Int, minute: Int ->

                val minutes: Int = when (minute) {
                    in 0..7 -> 0
                    in 8..22 -> 15
                    in 23..37 -> 30
                    in 37..52 -> 45
                    in 53..59 -> 0
                    else -> 0
                }
                timePickerDialog.updateTime(hour, minutes)
            }

            timePickerDialog.show()
        }
    }

    @SuppressLint("InflateParams")
    private fun setupBookingDialogPopup() {

        val layoutInflater = LayoutInflater.from(context!!)
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_add_booking, null)
        titleEditText = popupInputDialogView.findViewById(R.id.book_title) as EditText
        infoEditText = popupInputDialogView.findViewById(R.id.book_info) as EditText
        dateTextView = popupInputDialogView.findViewById(R.id.book_date) as TextView
        timeTextView = popupInputDialogView.findViewById(R.id.book_time) as TextView
        cancelButton = popupInputDialogView.findViewById(R.id.button_cancel_booking) as Button
        confirmButton = popupInputDialogView.findViewById(R.id.button_confirm_booking) as Button

        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Booking Meeting Room")
        builder.setCancelable(false)
        builder.setView(popupInputDialogView)
        alertDialog = builder.create()

    }
}

