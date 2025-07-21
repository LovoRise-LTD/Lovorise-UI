package com.lovorise.app.invite

data class InviteFriendData(
    val users:List<InvitedUser>,
    val inviteUrl:String
){
    data class InvitedUser(
        val name:String,
        val profileUrl:String
    )
}
