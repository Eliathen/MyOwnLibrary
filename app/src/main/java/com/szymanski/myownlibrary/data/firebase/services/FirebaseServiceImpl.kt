package com.szymanski.myownlibrary.data.firebase.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

class FirebaseServiceImpl:
    FirebaseService {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun saveMyBook(book: FirebaseBook) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val firebaseReference = database.reference
        firebaseReference.child("users").child(auth.uid.toString()).child("myBooks").child(book.title).setValue(book)
    }

    override fun getMyBookList(): MutableList<FirebaseBook> {
        return mutableListOf()
    }

}