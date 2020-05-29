package com.szymanski.myownlibrary.activities

import android.content.Intent

import android.os.Bundle
import android.text.TextUtils

import android.view.View

import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import com.szymanski.myownlibrary.R

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            startMainActivity()
        }

        signUpButton.setOnClickListener {
            validate(emailTextView.text.toString(),passwordTextView.text.toString())
        }
        registerButton.setOnClickListener{
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }


    }

    private fun onLoginFailed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error_text))
            .setMessage(getString(R.string.registration_attempt_failed))
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun validate(email: String, password: String) {
        var focusView = View(baseContext)
        var cancel = false
        if(TextUtils.isEmpty(email)){
            emailTextView.error = getString(R.string.required_field_message)
            focusView = emailTextView
            cancel = true
        }
        if(TextUtils.isEmpty(password)){
            passwordTextView.error = getString(R.string.required_field_message)
            focusView = passwordTextView
            cancel = true
        }
        if(!cancel) {
            signIn(email, password)
        } else {
            focusView.requestFocus()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                startMainActivity()
            } else {
                onLoginFailed()
            }
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

}
