package com.zenoation.ksbaseapp.kt.rx

import com.zenoation.ksbaseapp.base.BaseUtils
import lombok.AllArgsConstructor
import lombok.Getter

open class RxEvent {
    @Getter
    @AllArgsConstructor
    class ActivityEvent(
        val className: Class<*>?,
        val flags: Int = 0,
        val params: String? = null,
        var isFinish: Boolean = false,
        var animation: Int = BaseUtils.ANIM_NONE
    )
}
