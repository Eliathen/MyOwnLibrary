package com.szymanski.myownlibrary.viewModels

import android.app.Application

import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchBook
import com.szymanski.myownlibrary.data.openLibraryAPI.repositories.SearchRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KeywordSearchResultViewModel(application: Application): AndroidViewModel(application) {

    private val searchResult = MutableLiveData<MutableList<SearchBook>>()

    private val isDataLoaded = MutableLiveData<Boolean>().apply{ value = false}

    fun findBooks(keyword: String){
        isDataLoaded.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = kotlin.runCatching {
                    SearchRepository.findBooksByKeyword(keyword)
                }
                result.onFailure {
                    isDataLoaded.postValue(true)
                }
                result.onSuccess {
                    searchResult.postValue(it.searchBooks)
                    isDataLoaded.postValue(true)
                }
            }

        }
    }
    fun getSearchResult(): MutableLiveData<MutableList<SearchBook>>{
        return searchResult
    }
    fun getDataLoaded(): MutableLiveData<Boolean>{
        return isDataLoaded
    }
}