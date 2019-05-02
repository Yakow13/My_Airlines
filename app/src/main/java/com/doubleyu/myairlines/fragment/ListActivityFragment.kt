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
import com.doubleyu.myairlines.asynctask.RequestAirlinesData
import com.doubleyu.myairlines.listener.NetworkTaskListener
import com.doubleyu.myairlines.manager.AirlineManager
import com.doubleyu.myairlines.manager.FilterOption
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_airlines_list.*
import java.util.*

class ListActivityFragment : androidx.fragment.app.Fragment(), NetworkTaskListener<List<Airline>> {

	private val airlineManager: AirlineManager = AirlineManager
	private lateinit var selectedFilter: FilterOption
	private lateinit var listAdapter: AirlinesListAdapter

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
			RequestAirlinesData(this).execute()
		} else {
			onFailure()
		}
	}

	private fun isNetworkAvailable(context: Context?): Boolean {
		val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}

	private fun updateAllAirlines(airlines: ArrayList<Airline>) {
		airlineManager.updateAllAirlines(airlines)
		listAdapter.setData(airlineManager.filter(selectedFilter))
	}

	override fun onSuccess(result: List<Airline>) {
		swipeRefreshLayout.isRefreshing = false
		updateAllAirlines(result as ArrayList)
	}

	override fun onFailure() {
		swipeRefreshLayout.isRefreshing = false
		Snackbar.make(main_layout, R.string.no_connection, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry).toUpperCase()) { refreshData() }
				.show()
	}
}