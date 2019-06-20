package com.example.meet.Manager

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker

import android.R
import android.content.res.Resources
import android.os.Build
import android.support.annotation.RequiresApi

@Suppress("DEPRECATION")
class TimeDialog
/**
 * Creates a new time picker dialog with the specified theme.
 *
 *
 * The theme is overlaid on top of the theme of the parent `context`.
 * If `themeResId` is 0, the dialog will be inflated using the theme
 * specified by the
 * [android:timePickerDialogTheme][android.R.attr.timePickerDialogTheme]
 * attribute on the parent `context`'s theme.
 *
 * @param context the parent context
 * @param themeResId the resource ID of the theme to apply to this dialog
 * @param listener the listener to call when the time is set
 * @param hourOfDay the initial hour
 * @param minute the initial minute
 * @param is24HourView Whether this is a 24 hour view, or AM/PM.
 */
    (
    context: Context, themeResId: Int, private val mTimeSetListener: TimePickerDialog.OnTimeSetListener?,
    private val mInitialHourOfDay: Int, private val mInitialMinute: Int, private val mIs24HourView: Boolean
) :
    android.app.AlertDialog(context, resolveDialogTheme(context, themeResId)), DialogInterface.OnClickListener,
    TimePicker.OnTimeChangedListener {

    /**
     * @return the time picker displayed in the dialog
     * @hide For testing only.
     */
    val timePicker: TimePicker

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (e.g. they clicked on the 'OK' button).
     */
    interface OnTimeSetListener {
        /**
         * Called when the user is done setting a new time and the dialog has
         * closed.
         *
         * @param view the view associated with this listener
         * @param hourOfDay the hour that was set
         * @param minute the minute that was set
         */
        fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int)
    }

    /**
     * Creates a new time picker dialog.
     *
     * @param context the parent context
     * @param listener the listener to call when the time is set
     * @param hourOfDay the initial hour
     * @param minute the initial minute
     * @param is24HourView whether this is a 24 hour view or AM/PM
     */
    constructor(
        context: Context, listener: TimePickerDialog.OnTimeSetListener, hourOfDay: Int, minute: Int,
        is24HourView: Boolean
    ) : this(context, 0, listener, hourOfDay, minute, is24HourView) {
    }

    init {

        val themeContext = getContext()
        val inflater = LayoutInflater.from(themeContext)

        val view = inflater.inflate(
            Resources.getSystem().getIdentifier("time_picker_dialog", "layout", "android"), null)
        setView(view)
        setButton(DialogInterface.BUTTON_POSITIVE, themeContext.getString(R.string.ok), this)
        setButton(DialogInterface.BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this)

        timePicker = view.findViewById(
            Resources.getSystem().getIdentifier("timePicker", "id", "android"))
        timePicker.setIs24HourView(mIs24HourView)
        timePicker.currentHour = mInitialHourOfDay
        timePicker.currentMinute = mInitialMinute
        timePicker.setOnTimeChangedListener(this)
    }

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        /* do nothing */
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun show() {
        super.show()
        getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            if (timePicker.validateInput()) {
                this@TimeDialog.onClick(this@TimeDialog, DialogInterface.BUTTON_POSITIVE)
                // Clearing focus forces the dialog to commit any pending
                // changes, e.g. typed text in a NumberPicker.
                timePicker.clearFocus()
                dismiss()
            }
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                // Note this skips input validation and just uses the last valid time and hour
                // entry. This will only be invoked programmatically. User clicks on BUTTON_POSITIVE
                // are handled in show().
                mTimeSetListener?.onTimeSet(
                    timePicker, timePicker.currentHour,
                    timePicker.currentMinute
                )
            DialogInterface.BUTTON_NEGATIVE -> cancel()
        }
    }

    /**
     * Sets the current time.
     *
     * @param hourOfDay The current hour within the day.
     * @param minuteOfHour The current minute within the hour.
     */
    fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        timePicker.currentHour = hourOfDay
        timePicker.currentMinute = minuteOfHour
    }

    override fun onSaveInstanceState(): Bundle {
        val state = super.onSaveInstanceState()
        state.putInt(HOUR, timePicker.currentHour)
        state.putInt(MINUTE, timePicker.currentMinute)
        state.putBoolean(IS_24_HOUR, timePicker.is24HourView)
        return state
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val hour = savedInstanceState.getInt(HOUR)
        val minute = savedInstanceState.getInt(MINUTE)
        timePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR))
        timePicker.currentHour = hour
        timePicker.currentMinute = minute
    }

    companion object {
        private val HOUR = "hour"
        private val MINUTE = "minute"
        private val IS_24_HOUR = "is24hour"

        internal fun resolveDialogTheme(context: Context, resId: Int): Int {
            if (resId == 0) {
                val outValue = TypedValue()
                context.theme.resolveAttribute(R.attr.timePickerDialogTheme, outValue, true)
                return outValue.resourceId
            } else {
                return resId
            }
        }
    }
}
