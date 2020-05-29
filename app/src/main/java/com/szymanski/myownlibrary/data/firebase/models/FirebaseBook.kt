package com.szymanski.myownlibrary.data.firebase.models

import java.io.Serializable

data class FirebaseBook(var isbn: String = "",
                   var title: String = "",
                   var authors: List<String> = listOf(),
                   var publishedYear: String = "",
                   var pageCount: Int = 0,
                   var cover: String = "",
                   var isBorrow: Boolean = false): Serializable