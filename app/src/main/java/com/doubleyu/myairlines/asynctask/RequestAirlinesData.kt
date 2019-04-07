package com.doubleyu.myairlines.asynctask

import android.os.AsyncTask
import com.doubleyu.myairlines.Airline
import com.doubleyu.myairlines.listener.NetworkTaskListener
import com.google.gson.Gson
import java.net.URL

class RequestAirlinesData(private val listener: NetworkTaskListener<List<Airline>>) : AsyncTask<Void, Void, Array<Airline>>() {


	override fun onPostExecute(result: Array<Airline>) {
		super.onPostExecute(result)
		if (result.isEmpty()) listener.onFailure() else listener.onSuccess(result.toList())
	}

	override fun doInBackground(vararg params: Void?): Array<Airline> {
		val result = URL(apiEndpoint).readText()
		val airlines: Array<Airline> = Gson().fromJson(result, Array<Airline>::class.java)
		return airlines
	}

	companion object {
		private const val apiEndpoint: String = "https://www.kayak.com/h/mobileapis/directory/airlines"
	}

}