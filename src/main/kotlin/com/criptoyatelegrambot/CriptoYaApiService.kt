package com.criptoyatelegrambot

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL : String = "https://criptoya.com/api/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
        .also { it.level = HttpLoggingInterceptor.Level.BODY }
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface CriptoYaApiService {

    @GET("{exchange}/dai/ars")
    suspend fun getDaiPrices(@Path("exchange") exchange: String): CurrencyValuesDTO?

}

object CriptoYaApi {

    val criptoYaApiService : CriptoYaApiService by lazy {
        retrofit.create(CriptoYaApiService::class.java)
    }
}