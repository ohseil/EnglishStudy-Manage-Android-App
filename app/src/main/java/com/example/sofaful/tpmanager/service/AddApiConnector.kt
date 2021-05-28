package com.example.sofaful.tpmanager.service

import android.os.AsyncTask
import android.support.v4.app.Fragment
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.subfragments.AddFragment
import com.example.sofaful.tpmanager.subfragments.MainFragment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Created by Seil on 2020-11-05.
 */
object AddApiConnector: ApiConnector() {

    private val url = "$address/studydata/admin/create"

    fun AddProcess(category: String, question: String, answer: String, context: Fragment) {

        AsyncTask.execute ( Runnable {

            try {

                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("X-AUTH-TOKEN", jwtToken)
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                connection.connectTimeout = 15000

                var jsonObject = JSONObject()
                jsonObject.accumulate("categoryCode", category)
                jsonObject.accumulate("question", question)
                jsonObject.accumulate("answer", answer)

                val osw = OutputStreamWriter(connection.outputStream)
                osw.write(jsonObject.toString())
                osw.flush()
                osw.close()

                var responseMsg = "responseCode: " + connection.responseCode + " Key: "
                try {
                    val ins = connection.inputStream
                    responseMsg += when {
                        ins != null -> {
                            val br = BufferedReader(InputStreamReader(ins))
                            var sb = StringBuilder()
                            var line: String?

                            while (true) {
                                line = br.readLine()
                                if (line == null) break
                                sb.append(line)
                            }
                            sb.toString()
                        }
                        else -> ""
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }

                println(responseMsg)

                when {
                    context.activity?.supportFragmentManager?.findFragmentByTag("main").toString() != null -> {
                        (context as MainFragment).printResult(responseMsg)
                    }
                    context.activity?.supportFragmentManager?.findFragmentByTag("add").toString() != null -> {
                        (context as AddFragment).printResult(responseMsg)
                    }
                }
                connection.disconnect()

            }
            catch (e: Exception) {
                e.printStackTrace()
            }

        })

    }

    fun AddListProcess(dataList: ArrayList<EnglishStudyData>, context: Fragment) {

        AsyncTask.execute ( Runnable {

            try {

                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("accessToken", jwtToken)
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                connection.connectTimeout = 15000

                val osw = OutputStreamWriter(connection.outputStream)

                for (data in dataList) {
                    var jsonObject = JSONObject()
                    jsonObject.accumulate("categoryCode", data.getCategoryCode())
                    jsonObject.accumulate("question", data.getQuestion())
                    jsonObject.accumulate("answer", data.getAnswer())

                    osw.write(jsonObject.toString())
                }
                osw.flush()
                osw.close()

                var responseMsg = "responseCode: " + connection.responseCode + " Key: "
                try {
                    val ins = connection.inputStream
                    responseMsg += when {
                        ins != null -> {
                            val br = BufferedReader(InputStreamReader(ins))
                            var sb = StringBuilder()
                            var line: String?

                            while (true) {
                                line = br.readLine()
                                if (line == null) break
                                sb.append(line)
                            }
                            sb.toString()
                        }
                        else -> ""
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }

                println(responseMsg)

                when {
                    context.activity?.supportFragmentManager?.findFragmentByTag("main").toString() != null -> {
                        (context as MainFragment).printResult(responseMsg)
                    }
                    context.activity?.supportFragmentManager?.findFragmentByTag("add").toString() != null -> {
                        (context as AddFragment).printResult(responseMsg)
                    }
                }
                connection.disconnect()

            }
            catch (e: Exception) {
                e.printStackTrace()
            }

        })

    }

}