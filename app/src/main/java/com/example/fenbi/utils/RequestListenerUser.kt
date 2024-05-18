package com.example.fenbi.utils

class RequestListenerUser {
    lateinit var requestListener: RequestCallback
    fun goToPage(position: Int) {
        requestListener.request(position)
    }
}