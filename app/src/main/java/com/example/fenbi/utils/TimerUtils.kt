package com.example.fenbi.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimerUtils {

    private val compositeDisposable = CompositeDisposable()
    var intervalMillis: Long = 1000
    var count: Long = 0

    /**
     * 开始计时器，每隔指定时间执行一次
     *
     * @param timerListener  计时器回调
     */
    fun startIntervalTimer(timerListener: TimerListener?) {
        val disposable: Disposable = Observable.interval(intervalMillis, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                timerListener?.onTick(count)
                this.count = count
            }

        compositeDisposable.add(disposable)
    }

    /**
     * 开始倒计时
     *
     * @param countdownMillis 倒计时时间（毫秒）
     * @param intervalMillis  间隔时间（毫秒）
     * @param timerListener   计时器回调
     */
    fun startCountdownTimer(
        countdownMillis: Long,
        intervalMillis: Long,
        timerListener: TimerListener?
    ) {
        val disposable: Disposable = Observable.interval(0, intervalMillis, TimeUnit.MILLISECONDS)
            .take(countdownMillis / intervalMillis + 1)
            .map { elapsed -> countdownMillis - elapsed * intervalMillis }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { timeRemaining ->
                timerListener?.onTick(timeRemaining)
                if (timeRemaining <= 0) {
                    timerListener?.onFinish()
                }
            }

        compositeDisposable.add(disposable)
    }

    /**
     * 取消所有计时任务
     */
    fun stopAllTimers() {
        compositeDisposable.clear()
    }

    /**
     * 计时器回调接口
     */
    interface TimerListener {
        fun onTick(millisUntilFinished: Long)
        fun onFinish()
    }
}