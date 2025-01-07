package com.zenoation.baseapp_sample.mvvm

import com.zenoation.baseapp_sample.R
import com.zenoation.baseapp_sample.databinding.ActivityMvvvm2Binding
import com.zenoation.ksbaseapp.kt.base.BaseMvvmActivity

class MvvmActivity2(
    override val layoutId: Int = R.layout.activity_mvvvm2,
    override val viewModelClass: Class<Mvvm2ViewModel> = Mvvm2ViewModel::class.java
) : BaseMvvmActivity<ActivityMvvvm2Binding, Mvvm2ViewModel>() {
}