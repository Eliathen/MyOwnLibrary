package com.szymanski.myownlibrary.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    }


    private fun onLoginFailed() {
        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
    }

    private fun onLoginSuccess() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    private fun validate(email: String, password: String): Boolean {
        //TODO implements login
        return true
    }

}
