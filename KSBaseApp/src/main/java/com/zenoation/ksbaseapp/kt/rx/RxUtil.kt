package com.zenoation.ksbaseapp.kt.rx

import android.app.Activity
import android.content.Intent
import com.zenoation.ksbaseapp.base.BaseUtils
import com.zenoation.ksbaseapp.kt.common.GlobalVariable.Companion.RxEvents

class RxUtil private constructor() {

    fun startActivity(event: RxEvent.ActivityEvent) {
        RxEvents.publish(event)
    }

    fun startActivityAndFinish(event: RxEvent.ActivityEvent) {
        RxEvents.publish(event.apply { isFinish = true })
    }

    fun startActivity(activity: Activity, event: RxEvent.ActivityEvent) {
        if (event.className == null && event.isFinish) {
            activity.finish()
            return
        }

        val intent = Intent(activity, event.className)
        if (event.flags == -1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else {
            intent.addFlags(event.flags)
        }
        if (event.params != null) {
            intent.putExtra(INTENT_PARAMS, event.params)
        }
        if (event.isFinish) {
            activity.finish()
        }
        if (event.animation != BaseUtils.ANIM_NONE) {
            intent.putExtra(INTENT_ANIM, event.animation)
        }
        activity.startActivity(intent)
        if (event.animation != BaseUtils.ANIM_NONE) {
            BaseUtils.getInstance().setAnim(activity, event.animation)
        }
    }

    fun finish() {
        RxEvents.publish(RxEvent.ActivityEvent(null, isFinish = true))
    }

    companion object {
        const val INTENT_PARAMS: String = "params"
        const val INTENT_ANIM: String = "anim"

        val instance: RxUtil by lazy { RxUtil() }
    }
}