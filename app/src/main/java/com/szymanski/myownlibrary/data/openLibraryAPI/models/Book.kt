package com.szymanski.myownlibrary.data.openLibraryAPI.models

import java.io.Serializable

data class Book(
    val isbn: String = "",
    val title: String = "",
    val authors: ArrayList<String> = arrayListOf(),
    val publishedYear: String = "",
    val pageCount: Int = 0,
    val cover: String = ""
): Serializable
