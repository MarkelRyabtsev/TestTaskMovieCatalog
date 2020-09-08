package com.markel.testtaskmoviecatalog.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.markel.testtaskmoviecatalog.R
import kotlin.math.ln
import kotlin.math.pow

fun Activity.openFragment(
    fragment: Fragment,
    addToBackStack: Boolean
) {
    this as AppCompatActivity
    supportFragmentManager.beginTransaction().apply {
        add(R.id.frameLayout_fragmentContainer_activityMain, fragment)
        if (addToBackStack) addToBackStack(fragment::class.java.simpleName)
        commit()
    }
}

fun Int.format(): String {
    if (this < 1000) return toString()
    val exp = ln(this.toDouble()).div(ln(1000.0)).toInt()
    return String.format("%.2f %c", this.div(1000.0.pow(exp)), "kMGTPE"[exp - 1])
}

fun Int.formatHourMinutes(): String {
    val hours = this.div(60)
    val minutes = this % 60
    return String.format("%d: %02d: 00", hours, minutes)
}