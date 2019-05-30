package com.doubleyu.myairlines.model

import androidx.lifecycle.MutableLiveData
import com.doubleyu.myairlines.api.ApiCallback
import com.doubleyu.myairlines.api.KayakAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirlinesDataModel {
	val allAirlines : MutableLiveData<List<Airline>> by lazy {
		MutableLiveData<List<Airline>>()
	}
	private val kayakAPI by lazy {
		KayakAPI.create()
	}

	fun refreshAirlines(apiCallback: ApiCallback?) {
		val call = kayakAPI.getAirlines()
		val callback = object : Callback<List<Airline>> {
			override fun onResponse(call: Call<List<Airline>>, response: Response<List<Airline>>) {
				if (response.isSuccessful && response.body() != null) {
					allAirlines.value = response.body()
					apiCallback?.onSuccess()
				} else {
					apiCallback?.onFailure()
				}
			}

			override fun onFailure(call: Call<List<Airline>>, t: Throwable) {
				apiCallback?.onFailure()
			}
		}
		call.enqueue(callback)
	}

	companion object {
		fun instance() : AirlinesDataModel = AirlinesDataModel()
	}
}