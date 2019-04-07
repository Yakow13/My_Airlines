package com.doubleyu.myairlines.asynctask

import android.os.AsyncTask
import com.doubleyu.myairlines.Airline
import com.doubleyu.myairlines.listener.NetworkTaskListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

class RequestAirlinesData(private val listener: NetworkTaskListener<List<Airline>>) : AsyncTask<Void, Void, List<Airline>>() {


	override fun onPostExecute(result: List<Airline>) {
		super.onPostExecute(result)
		if (result.isEmpty()) listener.onFailure() else listener.onSuccess(result)
	}

	override fun doInBackground(vararg params: Void?): List<Airline> {
		val result = URL(apiEndpoint).readText()
		val airlines: List<Airline> = Gson().fromJson(result, typeToken)
		return airlines
	}

	companion object {
		private const val apiEndpoint: String = "https://www.kayak.com/h/mobileapis/directory/airlines"
		private val typeToken = object : TypeToken<List<Airline>>() {}.type
	}

}