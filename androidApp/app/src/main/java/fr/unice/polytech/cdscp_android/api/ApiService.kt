package fr.unice.polytech.cdscp_android.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("helloWorld")
    fun getHelloWorld(): Call<String>

    @GET("getCo2")
    fun getCo2(): Call<String>

    @GET("getOpenState")
    fun getOpenState(): Call<String>
}