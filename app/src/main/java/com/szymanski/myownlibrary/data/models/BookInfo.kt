package com.szymanski.myownlibrary.data.models

import com.google.gson.annotations.SerializedName

data class BookInfo (
    @SerializedName("details")
    val book: Book
)