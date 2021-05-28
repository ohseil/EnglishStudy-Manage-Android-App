package com.example.sofaful.tpmanager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.sofaful.tpmanager.service.ApiConnector
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import okhttp3.*

abstract class AuthActivity : AppCompatActivity() {

    val RC_SIGN_IN = 1
    private var account: GoogleSignInAccount? = null

    abstract fun handleAuthVerifyResult()

    fun startLogin() {

        /*account = GoogleSignIn.getLastSignedInAccount(this);
        // 기존에 로그인이 되어있다면 바로 다음 프로세스 진행.
        if (account != null) {
            updateUI();
            return;
        }*/
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("919073491758-fe2t234io7anmb07cju1hcg1r96r1jp2.apps.googleusercontent.com")
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        //mGoogleSignInClient.silentSignIn();
        startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)
    }

    private fun updateUI() {

        println(account?.email)
        println(account?.displayName)
        println(account?.id)
        println(account?.idToken)

        // Server로 token을 보내 인증받아 접근권한에 해당하는 jwt 받기.
        object : Thread() {
            override fun run() {
                try {

                    val body: RequestBody = FormBody.Builder()
                            .add("accessToken", account?.idToken as String)
                            .build()
                    val client = OkHttpClient()
                    val request: Request = Request.Builder()
                            .url(ApiConnector.address + "/v1/signin")
                            .post(body)
                            .build()
                    val response: Response = client.newCall(request).execute()
                    val tk: String? = response.body?.string()
                    println("response body: $tk")
                    ApiConnector.jwtToken = tk
                    handleAuthVerifyResult()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignIn", "signInResult:failed code=" + e.getStatusCode())
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
}