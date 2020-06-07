package com.szymanski.myownlibrary.data.openLibraryAPI.models

class SearchBook(
    var title: String = "",
    var cover: String = "",
    var authors: MutableList<String> = mutableListOf(),
    var year: String = "",
    var isbn: String = "",
    var pages: Int = 0,
    var isBookSaved: Boolean = false,
    var isBookInWishList: Boolean = false
    )