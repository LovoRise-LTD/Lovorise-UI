package com.lovorise.app.accounts.presentation.signup.age

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun validateAge(value:String?,monthsName:List<String>): ValidateAgeResult? {
    if (value.isNullOrBlank()) return null
    if (value.length != 8) return null
    return try {
        val day = "${value[0]}${value[1]}"
        val dayInt = "${value[0]}${value[1]}".toInt()
        val month = "${value[2]}${value[3]}"
        val monthInt = "${value[2]}${value[3]}".toInt()
        val year = "${value[4]}${value[5]}${value[6]}${value[7]}"
        val yearInt = "${value[4]}${value[5]}${value[6]}${value[7]}".toInt()


        // Ensure the year is 1924 or later
        if (yearInt < 1930) return null


        val birthDate = LocalDate(yearInt, monthInt, dayInt)

        // Get the current date
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Calculate preliminary age
        val age = currentDate.year - birthDate.year

        // Adjust age based on whether the current date is before the user's birthday this year
        val adjustedAge = if (currentDate < (birthDate + DatePeriod(years = age))) age - 1 else age

        if (adjustedAge < 1) return null

        // If the date creation succeeds, the date is valid
        ValidateAgeResult(
            age = adjustedAge,
            formatted = formatDate(dayInt,monthInt,yearInt,monthsName),
            day = dayInt,
            year = yearInt,
            month = monthInt,
            yyyyMMDD = "$year-$month-$day",
            dobText = value
        )
    } catch (e: Exception) {
        // If any exception occurs (e.g., invalid day/month), return false
        null
    }
}



fun formatDate(day: Int, month: Int, year: Int,monthNames:List<String>): String {
    return try {
        // Create LocalDate from user input
        val birthDate = LocalDate(year, month, day)

        // Map month number to month name
        val monthName = monthNames[birthDate.monthNumber - 1]

        // Format the date
        "${birthDate.dayOfMonth} $monthName ${birthDate.year}"
    } catch (e: Exception) {
        // Handle invalid dates
        "Invalid Date"
    }
}

data class ValidateAgeResult(
    val formatted:String="",
    val age:Int=0,
    val year: Int=0,
    val month: Int=0,
    val day: Int=0,
    val yyyyMMDD:String="",
    val dobText:String=""
)