package com.szymanski.myownlibrary.data.models

import java.io.Serializable
import java.time.LocalDate

data class Rent(val book: Book, val startDate: LocalDate, val endDate: LocalDate, val unit: String): Serializable