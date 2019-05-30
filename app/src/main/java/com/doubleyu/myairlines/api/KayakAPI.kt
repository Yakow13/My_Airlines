package com.doubleyu.myairlines.api

import com.doubleyu.myairlines.model.Airline
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface KayakAPI {
	@GET("airlines")
	fun getAirlines(): Call<List<Airline>>

	companion object {
		fun create(): KayakAPI {
			val retrofit = Retrofit.Builder()
					.baseUrl("https://www.kayak.com/h/mobileapis/directory/")
					.addConverterFactory(GsonConverterFactory.create())
					.build()

			return retrofit.create(KayakAPI::class.java)
		}
	}
}