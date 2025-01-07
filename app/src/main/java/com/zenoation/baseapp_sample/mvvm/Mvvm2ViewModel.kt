package com.zenoation.baseapp_sample.mvvm

import android.app.Application
import android.view.View
import com.zenoation.ksbaseapp.base.BaseUtils
import com.zenoation.ksbaseapp.kt.base.BaseViewModel
import com.zenoation.ksbaseapp.kt.rx.RxEvent

class Mvvm2ViewModel(application: Application) : BaseViewModel(application) {

    fun onClick(v: View) {
        startActivityRx(RxEvent.ActivityEvent(MvvmActivity::class.java, animation = BaseUtils.ANIM_RIGHT))
    }
}