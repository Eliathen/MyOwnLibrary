package com.szymanski.myownlibrary.data.firebase

import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book
import java.io.Serializable

data class Rent(val book: Book, val startDate: String, val endDate: String, val unit: String): Serializable