package com.szymanski.myownlibrary.data.firebase.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

class FirebaseServiceImpl:
    FirebaseService {

    private var database: FirebaseDatabase
    private var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }
    override fun saveMyBook(book: FirebaseBook): String {
        val firebaseReference = database.reference
        var result = "Success"
        firebaseReference.child("users")
            .child(auth.uid.toString())
            .child("myBooks")
            .child(book.title)
            .setValue(book, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if(databaseError != null){
                    result = databaseError.message
                }
            })
        return result
    }

    override fun getMyBookReference(): DatabaseReference {
        return database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("myBooks")
    }

}