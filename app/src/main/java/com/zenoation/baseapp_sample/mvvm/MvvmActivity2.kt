package com.zenoation.baseapp_sample.mvvm

import com.zenoation.baseapp_sample.R
import com.zenoation.baseapp_sample.databinding.ActivityMvvvm2Binding
import com.zenoation.ksbaseapp.kt.base.BaseMvvmActivity

class MvvmActivity2 : BaseMvvmActivity<ActivityMvvvm2Binding, Mvvm2ViewModel>(
    ActivityMvvvm2Binding::class.java,
    Mvvm2ViewModel::class.java
) {

}