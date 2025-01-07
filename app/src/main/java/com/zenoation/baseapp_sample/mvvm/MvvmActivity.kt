package com.zenoation.baseapp_sample.mvvm

import android.os.Bundle
import com.zenoation.baseapp_sample.R
import com.zenoation.baseapp_sample.databinding.ActivityMvvvmBinding
import com.zenoation.ksbaseapp.kt.base.BaseMvvmActivity
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.RxEvents
import io.reactivex.rxkotlin.addTo

class MvvmActivity(
    override val layoutId: Int = R.layout.activity_mvvvm,
    override val viewModelClass: Class<MvvmViewModel> = MvvmViewModel::class.java
) : BaseMvvmActivity<ActivityMvvvmBinding, MvvmViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxEvents.listen(MvvmViewModel.TextEvent::class.java).subscribe {
            mUtils.showToast(this, it.value)
        }.addTo(disposable)
    }
}