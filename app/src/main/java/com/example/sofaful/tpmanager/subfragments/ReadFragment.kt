package com.example.sofaful.tpmanager.subfragments


import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sofaful.tpmanager.MainActivity

import com.example.sofaful.tpmanager.R
import com.example.sofaful.tpmanager.constantValue.CategoryCode
import com.example.sofaful.tpmanager.constantValue.getCategoryStr
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.enum.ReadType
import com.example.sofaful.tpmanager.service.ReadApiConnector


/**
 * A simple [Fragment] subclass.
 */
class ReadFragment : CustomFragment() {

    var rGroup: RadioGroup? = null
    var curRBtn : ReadType = ReadType.ALL // 현재 선택된 Radio Button

    var layout_input: LinearLayout? = null
    var tv: TextView? = null
    //var tv_result: TextView? = null
    var et: EditText? = null
    var searchBtn: Button? = null

    //var dataList : ArrayList<EnglishStudyData> = ArrayList<EnglishStudyData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_read, container, false)

        rGroup = view.findViewById(R.id.rGroup_read)
        rGroup?.setOnCheckedChangeListener(radioBtnChangeListener)

        tv = view?.findViewById(R.id.tv_read)
        tv_result = view?.findViewById(R.id.tv_result)
        et = view?.findViewById(R.id.et_read)
        layout_input = view?.findViewById(R.id.layout_input)
        searchBtn = view?.findViewById(R.id.btn_search)
        searchBtn?.setOnClickListener(onSearchClick)

        // Inflate the layout for this fragment
        return view
    }


    val radioBtnChangeListener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when {
            checkedId == R.id.rbtn1_read -> {
                layout_input?.visibility = View.GONE
                curRBtn = ReadType.ALL
            }
            checkedId == R.id.rbtn2_read -> {
                layout_input?.visibility = View.VISIBLE;
                curRBtn = ReadType.Category
                tv?.setText("Category Code")
            }
            checkedId == R.id.rbtn3_read -> {
                layout_input?.visibility = View.VISIBLE
                curRBtn = ReadType.Key
                tv?.setText("Data Key")
            }
        }
    }

    val onSearchClick: View.OnClickListener = View.OnClickListener { view ->
        Toast.makeText(this.context, "" + curRBtn, Toast.LENGTH_LONG).show()

        // 기존 데이터 초기화
        dataList.clear()

        when {
            curRBtn == ReadType.ALL -> ReadApiConnector.ReadProcess(curRBtn, null, null, this)
            curRBtn == ReadType.Category -> ReadApiConnector.ReadProcess(curRBtn, et?.text.toString().trim(), null, this)
            curRBtn == ReadType.Key -> ReadApiConnector.ReadProcess(curRBtn, null, et?.text.toString().trim(), this)
        }
    }

    override fun readResult() {

        var printData = ""

        for (data in dataList)
            printData += ("[ ID: " + data.getId() +
                    " Category: " + data.getCategoryCode() + "," + getCategoryStr(data.getCategoryCode()?.toInt()) +
                    " Question: " + data.getQuestion() +
                    " Answer: " + data.getAnswer() + " ]\n")

        // UI thread 내부에서 UI 업데이트
        activity?.runOnUiThread {
            tv_result?.text = printData
        }
    }

}// Required empty public constructor
