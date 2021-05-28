package com.example.sofaful.tpmanager.service

import android.os.AsyncTask
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.enum.ReadType
import com.example.sofaful.tpmanager.subfragments.CustomFragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Seil on 2020-11-03.
 */
object ReadApiConnector: ApiConnector() {

    private val urlMap = mapOf(Pair(ReadType.ALL, "$address/studydata/all"),
                                Pair(ReadType.Category, "$address/studydata/byCategory"),
                                Pair(ReadType.Key, "$address/studydata/byIds"))


    fun ReadProcess(type: ReadType, code: String?, key: String?, context: CustomFragment) {

        // =======================================================================
        // connect to server and read data

        AsyncTask.execute( Runnable {

            try {

                var url = urlMap[type]

                url += when {
                    type == ReadType.Category -> ("?categoryCode=$code")
                    type == ReadType.Key -> ("?ids=$key")
                    else -> ""
                }

                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("X-AUTH-TOKEN", jwtToken)

                val responseCode = connection.responseCode

                println("responseCode : $responseCode")
                println("responseMsg : " + connection.responseMessage)

                when (responseCode) {
                    400, 401, 500 -> {
                        context.printStr("Error\n" + connection.responseMessage)
                    }
                    404 -> {
                        context.printStr("No Data\n" + connection.responseMessage)
                    }
                    200 -> {

                        val br = BufferedReader(InputStreamReader(connection.inputStream))
                        var sb = StringBuilder()
                        var line: String?

                        while (true) {
                            line = br.readLine()
                            if (line == null) break
                            sb.append(line)
                        }

                        val jo = JSONObject(sb.toString())
                        val jr = JSONArray(jo.getString("dataList"))

                        for (idx in 0..(jr.length() - 1)) {
                            val obj = jr.getJSONObject(idx)
                            context.dataList.add(EnglishStudyData(obj.getString("id"),
                                    obj.getString("categorycode"),
                                    obj.getString("question"),
                                    obj.getString("answer")))
                        }

                        context.readResult()
                        connection.disconnect()
                    }
                }

            }
            catch(e : Exception) {
                e.printStackTrace()
            }

        })

        // ========================================================================
    }
}