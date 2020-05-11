package com.szymanski.myownlibrary.data

import com.google.gson.GsonBuilder

import com.szymanski.myownlibrary.data.models.BookResult
import com.szymanski.myownlibrary.data.services.BookService

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {
    private const val BASE_URL = "https://openlibrary.org/api/"

    private val retrofitBuilder: Retrofit.Builder by lazy{
        val gson = GsonBuilder()
            .registerTypeAdapter(BookResult::class.java, BookJsonDeserializer())
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
    val bookService: BookService by lazy {
        retrofitBuilder
            .client(getOkHttpClient())
            .build()
            .create(BookService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}