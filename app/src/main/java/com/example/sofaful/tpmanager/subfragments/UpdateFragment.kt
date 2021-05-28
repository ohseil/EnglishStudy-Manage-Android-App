package com.example.sofaful.tpmanager.subfragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

import com.example.sofaful.tpmanager.R
import com.example.sofaful.tpmanager.constantValue.CategoryCode
import com.example.sofaful.tpmanager.constantValue.getCategoryStr
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.enum.ReadType
import com.example.sofaful.tpmanager.service.ReadApiConnector
import com.example.sofaful.tpmanager.service.UpdateApiConnector


/**
 * A simple [Fragment] subclass.
 */
class UpdateFragment : CustomFragment() {

    var layout_update: LinearLayout? = null

    var et_id: EditText? = null
    var et_category: EditText? = null
    var et_question: EditText? = null
    var et_answer: EditText? = null

    var btn_search: Button? = null
    var btn_update: Button? = null
    var btn_refresh: Button? = null

    // 수정하려 하는 id의 데이터를 이전에 검색했는지 확인하기 위한 변수
    var searchedId = "-1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        tv_result = view.findViewById(R.id.tv_result)

        layout_update = view.findViewById(R.id.layout_update)

        et_id = view.findViewById(R.id.et_id)
        // fragment를 갱신할 때 edittest의 text를 초기화 해주기 위한 코드
        et_id?.post { et_id?.text = Editable.Factory.getInstance().newEditable("") }
        et_category = view.findViewById(R.id.et_category)
        et_question = view.findViewById(R.id.et_question)
        et_answer = view.findViewById(R.id.et_answer)

        btn_search = view.findViewById(R.id.btn_search)
        btn_update = view.findViewById(R.id.btn_update)
        btn_refresh = view.findViewById(R.id.btn_refresh)

        // Attach listener to button
        btn_search?.setOnClickListener(onSearchClicked)
        btn_update?.setOnClickListener(onUpdateClicked)
        btn_refresh?.setOnClickListener(onRefreshClicked)

        return view
    }

    // 검색 버튼 리스너
    val onSearchClicked = View.OnClickListener { view ->

        // 기존 데이터 초기화
        dataList.clear()

        // read api 호출
        ReadApiConnector.ReadProcess(ReadType.Key, null, et_id?.text.toString().trim(), this)
    }

    // 업데이트 버튼 리스너
    val onUpdateClicked = View.OnClickListener { view ->

        val id = et_id?.text.toString().trim()
        val category = et_category?.text.toString().trim()
        val question = et_question?.text.toString()
        val answer = et_answer?.text.toString()

        // 조건 검사
        if (isRight(id, category, question, answer) == false) {
            printStr("입력 값을 확인하거나 해당 id의 데이터를 검색한 적이 없다면 검색하세요.")
            return@OnClickListener
        }

        // update api 호출
        UpdateApiConnector.UpdateProcess(id, category, question, answer, this)
    }

    // 페이지 초기화 버튼 리스너
    val onRefreshClicked = View.OnClickListener { view ->

        activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

    fun isRight(id: String, category: String, question: String, answer: String): Boolean = when {
        id == "" -> false
        id != searchedId -> false // 검색하여 불러온 데이터만 수정이 가능하다.
        category == "" -> false
        getCategoryStr(category.toInt()) == CategoryCode.DEFAULT -> false
        question.trim() == "" -> false
        answer.trim() == "" -> false

        // 변경한 데이터가 없는 경우
        category == dataList[0].getCategoryCode().toString()
        && question.trim() == dataList[0].getQuestion()?.trim()
        && answer.trim() == dataList[0].getAnswer()?.trim() -> false

        else -> true
    }

    // read api 요청 후 데이터를 성공적으로 받았을 때 실행되는 프로세스
    override fun readResult() {

        var printStr = "";

        if (dataList.isNotEmpty())
            printStr += "[ ID: " + dataList[0].getId() +
                    " CATEGORY: " + dataList[0].getCategoryCode() + "," + getCategoryStr(dataList[0].getCategoryCode()?.toInt()) +
                    " Q: " + dataList[0].getQuestion() +
                    " A: " + dataList[0].getAnswer()

        activity?.runOnUiThread {

            tv_result?.text = printStr

            et_id?.text = Editable.Factory.getInstance().newEditable(dataList[0].getId().toString())
            et_category?.text = Editable.Factory.getInstance().newEditable(dataList[0].getCategoryCode().toString())
            et_question?.text = Editable.Factory.getInstance().newEditable(dataList[0].getQuestion().toString())
            et_answer?.text = Editable.Factory.getInstance().newEditable(dataList[0].getAnswer().toString())

            // 데이터 검색 완료 후 업데이트 위한 view 보이기
            layout_update?.visibility = View.VISIBLE
            btn_update?.visibility = View.VISIBLE
        }

        searchedId = dataList[0].getId().toString()
    }

    fun updateResult(str: String) {

        printStr(str)
    }

}// Required empty public constructor
