package com.example.fenbi.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

class PracticeUtils(private val practiceObserver: Observer<Int>) {
    fun goToPage(position: Int) {
        val observable: Observable<Int> = Observable.just(position)
        observable.subscribe(practiceObserver)
    }
}