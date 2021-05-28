package com.example.sofaful.tpmanager.subfragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.example.sofaful.tpmanager.R
import com.example.sofaful.tpmanager.constantValue.CategoryCode
import com.example.sofaful.tpmanager.constantValue.getCategoryStr
import com.example.sofaful.tpmanager.service.AddApiConnector


/**
 * A simple [Fragment] subclass.
 */
class AddFragment : Fragment() {

    var et_category: EditText? = null
    var et_question: EditText? = null
    var et_answer: EditText? = null

    var tv_result: TextView? = null

    var btn_add: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        et_category = view.findViewById(R.id.et_category)
        et_question = view.findViewById(R.id.et_question)
        et_answer = view.findViewById(R.id.et_answer)

        tv_result = view.findViewById(R.id.tv_result)

        btn_add = view.findViewById(R.id.btn_add)
        btn_add?.setOnClickListener(btnClicked)

        return view
    }

    val btnClicked = View.OnClickListener { view ->

        val category = et_category?.text.toString().trim()
        val question = et_question?.text.toString()
        val answer = et_answer?.text.toString()

        // 조건 검사
        if (isRight(category, question, answer) == false) {
            tv_result?.text = "입력 값을 다시 확인하세요."
            return@OnClickListener
        }

        // add api 호출
        AddApiConnector.AddProcess(category, question, answer, this)
    }

    fun isRight(category: String, question: String, answer: String): Boolean = when {
            category == "" -> false
            getCategoryStr(category.toInt()) == CategoryCode.DEFAULT -> false
            question.trim() == "" -> false
            answer.trim() == "" -> false
            else -> true
        }


    fun printResult(resultStr: String) {

        activity?.runOnUiThread {
            tv_result?.text = resultStr
        }
    }

}// Required empty public constructor
