package com.szymanski.myownlibrary.data.firebase.models


data class FirebaseRent(var firebaseBook: FirebaseBook = FirebaseBook(),
                        var startDate: String = "",
                        var endDate: String = "",
                        var unit: String = "",
                        var isFinished: Boolean = false)