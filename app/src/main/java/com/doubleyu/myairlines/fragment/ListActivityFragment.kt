package com.doubleyu.myairlines.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.doubleyu.myairlines.Airline
import com.doubleyu.myairlines.AirlinesListAdapter
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.api.KayakAPI
import com.doubleyu.myairlines.manager.AirlineManager
import com.doubleyu.myairlines.manager.FilterOption
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_airlines_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListActivityFragment : androidx.fragment.app.Fragment() {

	private val airlineManager: AirlineManager = AirlineManager
	private lateinit var selectedFilter: FilterOption
	private lateinit var listAdapter: AirlinesListAdapter
	private val kayakAPI by lazy {
		KayakAPI.create()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// list of allAirlines is too big to save via saveInstanceState
		retainInstance = true
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		return inflater.inflate(R.layout.fragment_airlines_list, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		listAdapter = AirlinesListAdapter(context!!)
		selectedFilter = FilterOption.ALL
		initUI()

		if (!airlineManager.hasData) {
			refreshData()
		}
	}

	override fun onResume() {
		super.onResume()
		if (selectedFilter == FilterOption.FAVORITE) {
			listAdapter.setData(airlineManager.filter(selectedFilter))
		}
	}

	fun setFilter(selection: FilterOption) {
		selectedFilter = selection
		listAdapter.setData(airlineManager.filter(selectedFilter))
	}


	private fun initUI() {
		swipeRefreshLayout.setOnRefreshListener { refreshData() }
		airlines_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
		airlines_rv.adapter = listAdapter
		airlines_rv.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, LinearLayout.VERTICAL))
	}

	private fun refreshData() {
		swipeRefreshLayout.isRefreshing = true
		if (isNetworkAvailable(context)) {
			val callback = object : Callback<List<Airline>> {

				override fun onResponse(call: Call<List<Airline>>, response: Response<List<Airline>>) {
					if (response.isSuccessful && response.body() != null) {
						onSuccess(response.body()!!)
					} else {
						onFailure()
					}
				}

				override fun onFailure(call: Call<List<Airline>>, t: Throwable) {
					onFailure()
				}
			}
			val call = kayakAPI.getAirlines()

			call.enqueue(callback)
		} else {
			onFailure()
		}
	}

	private fun isNetworkAvailable(context: Context?): Boolean {
		val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}

	private fun updateAllAirlines(airlines: List<Airline>) {
		airlineManager.updateAllAirlines(airlines)
		listAdapter.setData(airlineManager.filter(selectedFilter))
	}

	private fun onSuccess(result: List<Airline>) {
		swipeRefreshLayout.isRefreshing = false
		updateAllAirlines(result as ArrayList)
	}

	private fun onFailure() {
		swipeRefreshLayout.isRefreshing = false
		Snackbar.make(main_layout, R.string.no_connection, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry).toUpperCase()) { refreshData() }
				.show()
	}
}