package com.example.sofaful.tpmanager.service

import android.os.AsyncTask
import com.example.sofaful.tpmanager.subfragments.UpdateFragment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Seil on 2020-11-06.
 */
object UpdateApiConnector: ApiConnector() {

    private val url = "$address/studydata/admin/update"

    fun UpdateProcess(id: String, category: String, question: String, answer: String, context: UpdateFragment) {

        AsyncTask.execute ( Runnable {

            try {

                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("X-AUTH-TOKEN", jwtToken)
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                connection.connectTimeout = 15000

                var jsonObject = JSONObject()
                jsonObject.accumulate("id", id)
                jsonObject.accumulate("categoryCode", category)
                jsonObject.accumulate("question", question)
                jsonObject.accumulate("answer", answer)

                val os = connection.outputStream
                os.write(jsonObject.toString().toByteArray())
                os.flush()
                os.close()

                var responseMsg = "responseCode: " + connection.responseCode + " Key: "

                when (connection.responseCode) {
                    200 -> {
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


                            context.updateResult(responseMsg)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    else -> {
                        context.printStr(responseMsg)
                    }

                }

                println(responseMsg)
                connection.disconnect()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        })

    }
}