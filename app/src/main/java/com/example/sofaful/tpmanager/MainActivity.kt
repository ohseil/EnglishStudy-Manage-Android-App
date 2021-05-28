package com.example.sofaful.tpmanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.example.sofaful.tpmanager.service.ReadApiConnector
import com.example.sofaful.tpmanager.subfragments.*

class MainActivity : AuthActivity() {

    var tbar: Toolbar? = null
    var tv_title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tbar = findViewById(R.id.toolbar)
        setSupportActionBar(tbar)

        tv_title = findViewById(R.id.tv_title)
        tv_title?.text = "Main"

        setPage(MainFragment(), "main")
        startLogin()
    }

    override fun handleAuthVerifyResult() {

    }

    fun btnClick(view : View) {
        when {
            view.id == R.id.btnMain -> { setPage(MainFragment(), "main"); tv_title?.text = "Main" }
            view.id == R.id.btnRead -> { setPage(ReadFragment(), "read"); tv_title?.text = "Read" }
            view.id == R.id.btnAdd -> { setPage(AddFragment(), "add"); tv_title?.text = "Add" }
            view.id == R.id.btnUpdate -> {setPage(UpdateFragment(), "update"); tv_title?.text = "Update" }
            view.id == R.id.btnDelete -> {setPage(DeleteFragment(), "delete"); tv_title?.text = "Delete" }
        }
    }

    fun setPage(fragment : Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment, tag).commit()
    }
}
