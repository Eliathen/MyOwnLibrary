package com.szymanski.myownlibrary.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUpButton.setOnClickListener {
            if(validate(emailTextView.text.toString(),passwordTextView.text.toString()))
                onLoginSuccess()
            else
                onLoginFailed()
        }

        registerButton.setOnClickListener{
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
        displayShowCaseView()

    }

    private fun displayShowCaseView(){
        val loginShowCase = BubbleShowCaseBuilder(this)
            .title("SIGN IN")
            .description("Click to sign in")
            .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM)
            .targetView(signUpButton)
            .backgroundColorResourceId(R.color.colorPrimaryLight)
            .textColorResourceId(R.color.colorPrimaryText)
        val registerShowCase = BubbleShowCaseBuilder(this)
            .title("REGISTER")
            .description("Click here to create your account.")
            .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
            .targetView(registerButton)
            .backgroundColorResourceId(R.color.colorPrimaryLight)
            .textColorResourceId(R.color.colorPrimaryText)
        BubbleShowCaseSequence()
            .addShowCase(loginShowCase)
            .addShowCase(registerShowCase)
            .show()
    }

    private fun onLoginFailed() {
        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
    }

    private fun onLoginSuccess() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    private fun validate(email: String, password: String): Boolean {
        Log.d("LoginActivity", "email: $email password: $password")
        //TODO implements login
        return true
    }

}
