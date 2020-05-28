package com.szymanski.myownlibrary.data.firebase.models

import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book

data class FirebaseRent(val book: Book, val startDate: String, val endDate: String, val unit: String)