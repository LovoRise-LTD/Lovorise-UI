package com.lovorise.app.profile.domain.models

data class GetCoinTask(
    val taskTitle:String,
    val individualTasks: List<IndividualTask>
)
