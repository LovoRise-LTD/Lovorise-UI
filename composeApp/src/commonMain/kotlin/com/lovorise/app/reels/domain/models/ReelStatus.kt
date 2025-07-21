package com.lovorise.app.reels.domain.models


enum class ReelStatus(val code: Int) {
    REPORTED(-2),
    NOT_INTERESTED(-1),
    WATCHED(0);

    companion object {
        fun fromCode(code: Int): ReelStatus? {
            return entries.find { it.code == code }
        }
    }
}