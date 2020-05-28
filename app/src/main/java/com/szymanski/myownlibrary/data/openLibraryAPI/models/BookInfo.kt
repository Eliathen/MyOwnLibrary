package com.szymanski.myownlibrary.data.openLibraryAPI.models

import com.google.gson.annotations.SerializedName

data class BookInfo (
    @SerializedName("details")
    val book: Book
)