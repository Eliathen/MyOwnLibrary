package com.szymanski.myownlibrary.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    val isbn: String = "",
    val title: String = "",
    val authors: ArrayList<String> = arrayListOf(),
    val publishedDate: String = "",
    val pageCount: Int = 0,
    val cover: String = ""
): Serializable
