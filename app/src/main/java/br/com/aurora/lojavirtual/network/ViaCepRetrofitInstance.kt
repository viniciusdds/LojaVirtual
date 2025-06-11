package br.com.aurora.lojavirtual.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ViaCepRetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ViaCepService by lazy {
        retrofit.create(ViaCepService::class.java)
    }
}