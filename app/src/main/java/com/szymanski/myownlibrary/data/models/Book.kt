package com.szymanski.myownlibrary.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    val title: String = "",
    val authors: ArrayList<String> = arrayListOf<String>(),
    val publishedDate: String = "",
    val pageCount: Int = 0,
    val cover: String = ""
): Serializable
