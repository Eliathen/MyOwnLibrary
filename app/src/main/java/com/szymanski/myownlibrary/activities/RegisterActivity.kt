package com.szymanski.myownlibrary.activities

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

import com.szymanski.myownlibrary.R
import kotlinx.android.synthetic.main.activity_login.*

import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        signInButton.setOnClickListener {
            attemptRegistration()
        }
    }


    private fun attemptRegistration(){
        val email = registerEmail.text.toString()
        val password = registerPassword.text.toString()
        val username = registerUsername.text.toString()
        var cancel = false
        var focusView: View = View(baseContext)

        if(TextUtils.isEmpty(password)||!isPasswordValid(password)){
            registerPassword.error = "Password too short or does not match"
            cancel = true
            focusView = registerPassword
        }
        if(TextUtils.isEmpty(email)){
            registerEmail.error = "This field is required"
            cancel = true
            focusView = registerEmail
        } else if(!isEmailValid(email)){
            registerEmail.error = "This email address is invalid"
            cancel = true
            focusView = registerEmail
        }

        if(cancel){
            focusView.requestFocus()
        } else {
            registerUser(email, username, password)
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        return password == registerConfirmPassword.text.toString() && password.length > 7
    }
    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }
    private fun registerUser(email: String, name: String, password: String){
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    saveUsername(name)
                    startMainActivity()
                } else {
                    showErrorDialog("Registration attempt failed")
                }
            }
    }
    private fun startMainActivity(){
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun showErrorDialog(message: String){
        AlertDialog.Builder(this)
            .setTitle("Oops")
            .setMessage(message)
            .setNeutralButton("Ok", null)
            .show()
    }
    private fun saveUsername(name: String){
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        user!!.updateProfile(profileUpdates)
    }
}
