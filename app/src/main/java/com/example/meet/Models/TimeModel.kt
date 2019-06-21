package com.example.meet.Models

class TimeModel{
    public var hour: Int
    public var minute: Int

    constructor(time :String){
        val timestamp = time.split(": ")
        hour = timestamp[0].toInt()
        minute = timestamp[1].toInt()
    }

    constructor(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
    }

    override fun toString(): String {
        return "${"%02d".format(hour)}: ${"%02d".format(minute)}"
    }
}