package com.example.sofaful.tpmanager.subfragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.example.sofaful.tpmanager.R
import com.example.sofaful.tpmanager.constantValue.CategoryCode
import com.example.sofaful.tpmanager.db.DBAdapter
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.service.AddApiConnector
import com.example.sofaful.tpmanager.service.ReadApiConnector


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val btn: Button? = view?.findViewById(R.id.btnUpload)
        btn?.setOnClickListener(onUploadClicked)

        return view
    }

    val onUploadClicked = View.OnClickListener {
            // 기존 데이터 모두 서버에 업로드
            uploadAll()
    }

    fun uploadAll() {

        val mDbHelper = DBAdapter(context)
        mDbHelper.createDB()
        mDbHelper.open()

        for (category in CategoryCode.values()) {

            if (category == CategoryCode.DEFAULT)
                continue

            val cursor = mDbHelper.getData(category.toString())
            do {

                AddApiConnector.AddProcess(category.ordinal.toString(), cursor!!.getString(1), cursor!!.getString(2), this)

            } while(cursor!!.moveToNext())
        }

        mDbHelper.close()
    }

    fun printResult(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

}// Required empty public constructor
