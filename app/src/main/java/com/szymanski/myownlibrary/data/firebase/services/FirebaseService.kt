package com.szymanski.myownlibrary.data.firebase.services

import com.google.firebase.database.DatabaseReference
import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook
import com.szymanski.myownlibrary.data.firebase.models.FirebaseRent

interface FirebaseService {

    fun saveMyBook(book: FirebaseBook): String

    fun getMyBookReference(): DatabaseReference

    fun getBorrowedBookReference(): DatabaseReference

    fun removeBook(book: FirebaseBook): String

    fun markBookAsBorrowed(rent: FirebaseRent): String

    fun markBookAsLent(rent: FirebaseRent): String

}