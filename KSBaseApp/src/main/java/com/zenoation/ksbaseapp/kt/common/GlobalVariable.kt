package com.zenoation.ksbaseapp.kt.common

import com.zenoation.ksbaseapp.kt.rx.RxBus
import com.zenoation.ksbaseapp.kt.rx.RxUtil
import com.zenoation.ksbaseapp.kt.util.JsonUtil
import com.zenoation.ksbaseapp.utils.Utils
import com.zenoation.ksbaseapp.utils.dialog.DialogUtils
import com.zenoation.ksbaseapp.utils.pref.PrefUtils

interface GlobalVariable {
    companion object {
        val mPrefUtils = PrefUtils.getInstance()
        val mDialogUtils = DialogUtils.getInstance()
        val mUtils = Utils.getInstance()
        val JsonUtils = JsonUtil.instance
        val RxEvents: RxBus = RxBus.instance
        val RxUtils: RxUtil = RxUtil.instance
    }
}