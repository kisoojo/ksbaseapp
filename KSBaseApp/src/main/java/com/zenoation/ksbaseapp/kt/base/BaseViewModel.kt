package com.zenoation.ksbaseapp.kt.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.RxUtils
import com.zenoation.ksbaseapp.kt.rx.RxEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val title = MutableLiveData<String>()

    protected val context: Application = application
    protected val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    protected fun startActivityRx(className: Class<*>) {
        RxUtils.startActivity(RxEvent.ActivityEvent(className))
    }

    protected fun startActivityRx(activityEvent: RxEvent.ActivityEvent) {
        RxUtils.startActivity(activityEvent)
    }

    protected fun startActivityAndFinishRx(className: Class<*>) {
        RxUtils.startActivity(RxEvent.ActivityEvent(className, isFinish = true))
    }

    protected fun finish() {
        RxUtils.finish()
    }
}
