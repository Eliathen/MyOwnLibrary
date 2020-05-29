package com.szymanski.myownlibrary.data.firebase.services

import com.google.firebase.database.DatabaseReference
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

interface FirebaseService {

    fun saveMyBook(book: FirebaseBook): String

    fun getMyBookReference(): DatabaseReference


}