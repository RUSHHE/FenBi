package com.example.fenbi.utils

class PageRequestListenerUser {
    lateinit var requestListener: RequestCallback
    fun goToPage(position: Int) {
        requestListener.request(position)
    }
}