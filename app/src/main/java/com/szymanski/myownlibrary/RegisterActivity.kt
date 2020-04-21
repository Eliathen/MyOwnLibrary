package com.szymanski.myownlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signInButton.setOnClickListener {
            attemptRegistration()
        }
    }
    private fun attemptRegistration(){
        //TODO("Implement registration")
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        finish()
        startActivity(intent)

    }
}
