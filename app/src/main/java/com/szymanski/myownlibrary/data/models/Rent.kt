package com.szymanski.myownlibrary.data.models

import java.io.Serializable

data class Rent(val book: Book, val startDate: String, val endDate: String, val unit: String): Serializable