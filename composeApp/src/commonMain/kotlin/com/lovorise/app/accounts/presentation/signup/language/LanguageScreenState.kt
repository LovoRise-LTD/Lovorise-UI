package com.lovorise.app.accounts.presentation.signup.language

data class LanguageScreenState(
    val selectedLanguages: List<String> = emptyList(),
    val allLanguages: List<String> = emptyList(),
    val searchResults:List<String> = emptyList(),
    val searchQuery:String = ""
)
