package com.example.sofaful.tpmanager.subfragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.example.sofaful.tpmanager.R
import com.example.sofaful.tpmanager.constantValue.getCategoryStr
import com.example.sofaful.tpmanager.enum.ReadType
import com.example.sofaful.tpmanager.service.DeleteApiConnector
import com.example.sofaful.tpmanager.service.ReadApiConnector


/**
 * A simple [Fragment] subclass.
 */
class DeleteFragment : CustomFragment() {

    var et_id: EditText? = null

    var btn_search: Button? = null
    var btn_delete: Button? = null
    var btn_refresh: Button? = null

    var searchedId = "-1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delete, container, false)

        tv_result = view.findViewById(R.id.tv_result)

        et_id = view.findViewById(R.id.et_id)
        // fragment를 갱신할 때 edittest의 text를 초기화 해주기 위한 코드
        et_id?.post { et_id?.text = Editable.Factory.getInstance().newEditable("") }

        btn_search = view.findViewById(R.id.btn_search)
        btn_delete = view.findViewById(R.id.btn_delete)
        btn_refresh = view.findViewById(R.id.btn_refresh)

        btn_search?.setOnClickListener(onSearchClicked)
        btn_delete?.setOnClickListener(onDeleteClicked)
        btn_refresh?.setOnClickListener(onRefreshClicked)

        return view
    }

    // 데이터 검색 버튼 리스너
    val onSearchClicked = View.OnClickListener {

        dataList.clear()

        // read api 호출
        ReadApiConnector.ReadProcess(ReadType.Key, null, et_id?.text.toString().trim(), this)
    }

    // 데이터 삭제 버튼 리스너
    val onDeleteClicked = View.OnClickListener {

        val id = et_id?.text.toString().trim()

        if (searchedId != id) {
            tv_result?.text = "입력 값을 확인하거나 해당 id의 데이터를 검색한 적이 없다면 검색하세요."
            return@OnClickListener
        }

        // delete api 호출
        DeleteApiConnector.DeleteProcess(id, this)
    }

    // 화면 초기화 버튼 리스너
    val onRefreshClicked = View.OnClickListener {
        activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

    override fun readResult() {

        var printStr = ""

        if (dataList.isNotEmpty())
            printStr += "[ ID: " + dataList[0].getId() +
                    " CATEGORY: " + dataList[0].getCategoryCode() + "," + getCategoryStr(dataList[0].getCategoryCode()?.toInt()) +
                    " Q: " + dataList[0].getQuestion() +
                    " A: " + dataList[0].getAnswer()

        activity?.runOnUiThread {
            tv_result?.text = printStr

            // 검색 완료 후 삭제 버튼 보이기
            btn_delete?.visibility = View.VISIBLE
        }

        searchedId = dataList[0].getId().toString()
    }

    // 삭제 성공 후 프로세스
    fun deleteResult(msg: String) {

        printStr(msg)

        searchedId = "-1"
        et_id?.text = Editable.Factory.getInstance().newEditable("")
    }

}// Required empty public constructor
