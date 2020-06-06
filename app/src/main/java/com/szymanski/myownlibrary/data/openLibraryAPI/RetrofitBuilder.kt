package com.szymanski.myownlibrary.data.openLibraryAPI

import com.google.gson.GsonBuilder
import com.szymanski.myownlibrary.data.openLibraryAPI.jsonDeserializers.BookJsonDeserializer
import com.szymanski.myownlibrary.data.openLibraryAPI.jsonDeserializers.SearchResultJsonDeserializer

import com.szymanski.myownlibrary.data.openLibraryAPI.models.BookResult
import com.szymanski.myownlibrary.data.openLibraryAPI.models.SearchResult
import com.szymanski.myownlibrary.data.openLibraryAPI.services.BookService
import com.szymanski.myownlibrary.data.openLibraryAPI.services.SearchService

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {
    private const val BASE_URL = "https://openlibrary.org/"

    private val bookRetrofitBuilder: Retrofit.Builder by lazy{
        val gson = GsonBuilder()
            .registerTypeAdapter(
                BookResult::class.java,
                BookJsonDeserializer()
            )
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
    private val searchRetrofitBuilder: Retrofit.Builder by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                SearchResult::class.java,
                SearchResultJsonDeserializer()
            ).create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
    val bookService: BookService by lazy {
        bookRetrofitBuilder
            .client(getOkHttpClient())
            .build()
            .create(BookService::class.java)
    }
    val searchService: SearchService by lazy {
        searchRetrofitBuilder
            .client(getOkHttpClient())
            .build()
            .create(SearchService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}