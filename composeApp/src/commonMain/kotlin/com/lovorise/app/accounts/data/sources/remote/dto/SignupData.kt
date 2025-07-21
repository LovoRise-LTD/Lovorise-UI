package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupData(
    val action: String = "signup",
    val signup: Signup
){
    @Serializable
    data class Signup(
        val page: Int,
        val email: String? = null,
        val password: String? = null,
        val name: String? = null,
        val age: Int? = null,
        val birthDate: String? = null,
        val gender: String? = null,
        val anotherGender:String? = null,
        val whoWouldYouLikeToMeet: List<String>? = null,
        val typeOfRelation: List<String>? = null,
        val interest: Map<String, List<String>>? = null,
        val height: Double? = null,
        val education: String? = null,
        val family: String? = null,
        val drinking: String? = null,
        val smoking: String? = null,
        val religion: String? = null,
        val language: List<String>? = null,
        val mediaIds: List<String>? = null,
        val inviteCode:String? = null
    )
}

enum class SignupPages(val pageNumber: Int) {
    EMAIL(1),
    PASSWORD(2),
    NAME(3),
    AGE(4),
    GENDER(5),
    WHO_WOULD_YOU_LIKE_TO_MEET(6),
    DATING_PREFS(7),
    INTEREST(8),
    HEIGHT(9),
    EDUCATION(10),
    FAMILY_PLAN(11),
    DRINK(12),
    SMOKE(13),
    RELIGION(14),
    LANGUAGE(15),
    IMAGE_UPLOAD_CONFIRMATION(16);
}


