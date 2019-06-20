package com.example.meet.Manager

class Validator{

    fun validateEmpty(string: String): Boolean{
        return string.trim() == "" || string.trim().isEmpty()
    }

    fun validateEquals(string: String, otherStr: String): Boolean{
        return string.trim() == otherStr.trim()
    }
    fun validatNotEquals(string: String, otherStr: String): Boolean{
        return string.trim() != otherStr.trim()
    }
}