package com.example.sofaful.tpmanager.subfragments

import android.support.v4.app.Fragment
import android.widget.TextView
import com.example.sofaful.tpmanager.entity.EnglishStudyData

/**
 * Created by Seil on 2020-11-06.
 */
abstract class CustomFragment: Fragment() {

    var tv_result: TextView? = null
    var dataList: ArrayList<EnglishStudyData> = ArrayList<EnglishStudyData>()

    abstract fun readResult()
    fun printStr(str: String) {
        activity?.runOnUiThread {
            tv_result?.text = str
        }
    }
}