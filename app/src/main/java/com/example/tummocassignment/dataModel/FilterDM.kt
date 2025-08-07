package com.example.tummocassignment.dataModel

import androidx.annotation.ColorRes
import com.example.tummocassignment.R
import com.example.tummocassignment.extensions.isTrue

class FilterDM {
    var title: String? = null
    var checkBoxShowImage: Boolean = true
    var checkBoxRes: Int = R.drawable.remeber_me_uncheck_box
    var isSelected: Boolean = false
    var fontResId: Int = R.font.satoshi_bold

    @ColorRes
    var backGroundColor: Int = R.color.white

    var textColorResId: Int = R.color._020617
//        get() = if (isSelected) R.color._020617 else R.color._1381FF

    fun checkStatus2() = if (isSelected.isTrue()) setStatusOn2() else setStatusOff2()
    fun setStatusOn2(): FilterDM {
        backGroundColor = R.color._F8FAFC
        checkBoxRes =  R.drawable.remeber_me_box
        fontResId = R.font.satoshi_bold
        return this
    }
    fun setStatusOff2(): FilterDM {
//        backGroundColor = R.color.white
        backGroundColor = R.color._F8FAFC
        checkBoxRes =  R.drawable.remeber_me_uncheck_box
        fontResId = R.font.satoshi_medium
        return this
    }

    fun checkStatus1() = if (isSelected.isTrue()) setStatusOn1() else setStatusOff1()
    fun setStatusOn1(): FilterDM {
        textColorResId = R.color._1381FF
        backGroundColor = R.color.white
        fontResId = R.font.satoshi_bold
        return this
    }
    fun setStatusOff1(): FilterDM {
        textColorResId = R.color._020617
        backGroundColor = R.color.white
        fontResId = R.font.satoshi_medium
        return this
    }
}