package com.example.sofaful.tpmanager.service

import android.os.AsyncTask
import com.example.sofaful.tpmanager.entity.EnglishStudyData
import com.example.sofaful.tpmanager.subfragments.DeleteFragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Seil on 2020-11-06.
 */
object DeleteApiConnector: ApiConnector() {

    private var url = "$address/studydata/admin/delete"

    fun DeleteProcess(id : String, context: DeleteFragment) {

        AsyncTask.execute( Runnable {

            try {

                url += ("?id=" + id)
                val urlObject = URL(url)
                val connection = urlObject.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"
                connection.setRequestProperty("X-AUTH-TOKEN", jwtToken)

                val responseCode = connection.responseCode

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

                        val responseMsg = "Response Code: " + responseCode + " msg: " + sb.toString()
                        context.deleteResult(responseMsg)
                        connection.disconnect()
                    }
                }

            }
            catch(e: Exception) {
                e.printStackTrace()
            }

        })
    }

}