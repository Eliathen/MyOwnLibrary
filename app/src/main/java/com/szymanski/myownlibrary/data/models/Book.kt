package com.szymanski.myownlibrary.data.models

import java.io.Serializable

data class Book(val title: String, val authors: ArrayList<String>, val yearOfPublished: String, val coverUrl: String): Serializable