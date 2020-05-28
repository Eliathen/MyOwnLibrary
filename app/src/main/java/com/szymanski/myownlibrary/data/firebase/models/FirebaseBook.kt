package com.szymanski.myownlibrary.data.firebase.models

class FirebaseBook(val isbn: String = "",
                   val title: String = "",
                   val authors: List<String> = listOf(),
                   val publishedYear: String = "",
                   val pageCount: Int = 0,
                   val cover: String,
                   val isBorrow: Boolean = false)