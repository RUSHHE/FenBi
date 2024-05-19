package com.example.fenbi.utils

class AnswerSheetRequestListenerUser {
    var requestListener: RequestCallback? = null
    fun refresh(position: Int) {
        requestListener?.request(position)
    }
}