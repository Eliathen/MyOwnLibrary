package com.szymanski.myownlibrary.data.openLibraryAPI.models

data class SearchResult(
    var searchBooks: MutableList<SearchBook> = mutableListOf()
)