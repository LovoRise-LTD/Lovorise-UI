package com.lovorise.app.accounts.presentation.utils

fun validatePassword(password: String): List<PasswordValidationResult>? {

    val errors = mutableListOf<PasswordValidationResult>()

    // Check length
    if (password.length !in 6..20) {
        errors.add(PasswordValidationResult.MAX_MIN_CHAR)
    }

    // Check for at least one uppercase letter
    if (!password.any { it.isLetter() }) {
        errors.add(PasswordValidationResult.ONE_LETTER_ONE_DIGIT)
    }

    // Check for at least one digit
    if (!password.any { it.isDigit() }) {
        errors.add(PasswordValidationResult.ONE_LETTER_ONE_DIGIT)
    }

    // Check for at least one special character
    val specialChars = "!@#$%^&*()-_=+[{]}|;:'\",<.>/?"
    if (!password.any { it in specialChars }) {
        errors.add(PasswordValidationResult.ONE_SPECIAL_CHAR)
    }

    // If all conditions are met, return null (no error)
    return errors.ifEmpty { null }
}

enum class PasswordValidationResult{
    MAX_MIN_CHAR,ONE_LETTER_ONE_DIGIT,ONE_SPECIAL_CHAR
}

fun isEmailValid(email:String):Boolean{
    val regex = Regex("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})\$")
    return regex.matches(email)
}