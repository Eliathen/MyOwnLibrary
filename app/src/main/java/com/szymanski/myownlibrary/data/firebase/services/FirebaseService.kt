package com.szymanski.myownlibrary.data.firebase.services

import com.szymanski.myownlibrary.data.firebase.models.FirebaseBook

interface FirebaseService {

    fun saveMyBook(book: FirebaseBook)

    fun getMyBookList(): MutableList<FirebaseBook>
}