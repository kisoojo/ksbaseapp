package com.zenoation.ksbaseapp.kt.base

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.zenoation.ksbaseapp.BR
import com.zenoation.ksbaseapp.R
import com.zenoation.ksbaseapp.base.BaseActivity
import com.zenoation.ksbaseapp.base.BaseUtils
import com.zenoation.ksbaseapp.base.BaseUtils.ANIM_NONE
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.JsonUtils
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.RxEvents
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.RxUtils
import com.zenoation.ksbaseapp.kt.rx.RxEvent
import com.zenoation.ksbaseapp.kt.rx.RxUtil.Companion.INTENT_ANIM
import com.zenoation.ksbaseapp.kt.rx.RxUtil.Companion.INTENT_PARAMS
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class BaseMvvmActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    private val bindingClass: Class<VB>,
    private val viewModelClass: Class<VM>
) : BaseActivity() {

    private val TAG: String = this::class.java.simpleName

    protected lateinit var binder: VB
    protected lateinit var viewModel: VM
    protected val disposable = CompositeDisposable()

    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val method = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        @Suppress("UNCHECKED_CAST")
        binder = method.invoke(null, layoutInflater) as VB
        setContentView(binder.root)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(viewModelClass)

        binder.lifecycleOwner = this
        binder.setVariable(BR.data, viewModel)

        this.enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binder.root) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getIntentParams()
        addCommonRxEvent()
    }

    private fun addCommonRxEvent() {
        // Activity 실행/종료
        RxEvents.listen(RxEvent.ActivityEvent::class.java).subscribe { activityEvent ->
            if (isPaused) return@subscribe
            Log.d(TAG, "Rx ActivityEvent : ${JsonUtils.objectToString(activityEvent)}")
            RxUtils.startActivity(this, activityEvent)
        }.addTo(disposable)
    }

    protected fun startActivityRx(className: Class<*>) {
        RxUtils.startActivity(this, RxEvent.ActivityEvent(className))
    }

    protected fun startActivityRx(activityEvent: RxEvent.ActivityEvent) {
        RxUtils.startActivity(this, activityEvent)
    }

    protected fun startActivityAndFinishRx(className: Class<*>) {
        RxUtils.startActivity(this, RxEvent.ActivityEvent(className, isFinish = true))
    }

    protected fun startActivityAndFinishRx(event: RxEvent.ActivityEvent) {
        RxUtils.startActivity(this, event.apply { isFinish = true })
    }


    protected var intentParams: String? = null
    protected var intentAnim: Int = ANIM_NONE

    private fun getIntentParams(intent: Intent = this.intent) {
        intentParams = intent.getStringExtra(INTENT_PARAMS)
        intentAnim = intent.getIntExtra(INTENT_ANIM, ANIM_NONE)
        Log.d(TAG, "intentParams : $intentParams")
    }

    protected fun checkRequiredParams() {
        if (TextUtils.isEmpty(intentParams)) {
            Toast.makeText(this, R.string.wrong_access, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: $localClassName")
        super.onResume()
        isPaused = false
    }

    override fun onPause() {
        Log.d(TAG, "onPause: $localClassName")
        super.onPause()
        isPaused = true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: $localClassName")
        disposable.dispose()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        if (intentAnim != ANIM_NONE) {
            BaseUtils.getInstance().finish(this, intentAnim)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        getIntentParams(intent)
    }
}