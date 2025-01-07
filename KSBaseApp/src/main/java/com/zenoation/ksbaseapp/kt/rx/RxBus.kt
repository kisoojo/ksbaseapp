package com.zenoation.ksbaseapp.kt.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class RxBus private constructor() {

    private val publishSubject = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publishSubject.onNext(event)
    }

    fun <T : Any> listen(eventType: Class<T>): Observable<T> {
        return publishSubject.ofType(eventType)
    }

    companion object {
        val instance: RxBus by lazy { RxBus() }
    }
}
