package com.example.sofaful.tpmanager.service

/**
 * Created by Seil on 2020-11-03.
 */
abstract class ApiConnector {

    //protected val address = "http://172.30.1.22:10000"

    companion object {
        var jwtToken: String? = null
        val address = "http://172.30.1.22:10000"
    }

}