package com.szymanski.myownlibrary.data.firebase.models

import com.google.firebase.database.Exclude
import java.util.*


data class FirebaseRent(
    @Exclude var key: String="",
    var firebaseBook: FirebaseBook = FirebaseBook(),
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var unit: String = "",
    var isBorrowed: Boolean = false,
    var isLent: Boolean = false,
    var isFinished: Boolean = false)