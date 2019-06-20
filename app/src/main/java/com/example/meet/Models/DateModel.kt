package com.example.meet.Models

class DateModel {
    public var year: Int
    public var month: Int
    public var day: Int

    constructor(date: String){
        // thai format: day/ month/ year
        var dateStamp = date.split("/ ")
        day = dateStamp[0].toInt()
        month = dateStamp[1].toInt()
        year = dateStamp[2].toInt()
    }

    constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

}