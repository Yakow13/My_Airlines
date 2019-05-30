package com.doubleyu.myairlines.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doubleyu.myairlines.AirlinesListAdapter
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.activity.AirlineDetailActivity
import com.doubleyu.myairlines.databinding.FragmentAirlinesListBinding
import com.doubleyu.myairlines.listener.OnAirlineSelectedListener
import com.doubleyu.myairlines.manager.FilterOption
import com.doubleyu.myairlines.model.Airline
import com.doubleyu.myairlines.viewmodel.ListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_airlines_list.*

class ListActivityFragment : androidx.fragment.app.Fragment() {
	private lateinit var listAdapter: AirlinesListAdapter
	private val viewModel: ListViewModel by lazy {
		ViewModelProviders.of(this).get(ListViewModel::class.java)
	}
	private val taskStatusObserver = Observer<ListViewModel.TaskStatus> {
		if (it == ListViewModel.TaskStatus.FAIL) {
			onFailure()
		} else if (it == ListViewModel.TaskStatus.RUNNING) {
			swipeRefreshLayout.isRefreshing = true
		}
	}
	private val onAirlineSelectedListener = object : OnAirlineSelectedListener() {
		override fun onClick(airline: Airline) {
			AirlineDetailActivity.startActivityForResult(activity!!, airline)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// list of allAirlines is too big to save via saveInstanceState
		retainInstance = true
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		val binding: FragmentAirlinesListBinding =
				DataBindingUtil.inflate(inflater, R.layout.fragment_airlines_list, container, false)
		binding.viewModel = viewModel
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initUI()
		viewModel.selectedAirlines.observe(this, Observer<List<Airline>> { t -> onSuccess(t) })
		viewModel.taskStatus.observe(this, taskStatusObserver)
		if (!viewModel.hasData) {
			viewModel.refreshAllAirlines()
		}
	}

	fun favoriteSetChanged() {
		viewModel.favoriteSetChanged()
	}

	fun setFilter(selection: FilterOption, userSelect: Boolean) {
		viewModel.setFilter(selection)
		if (userSelect) viewModel.filter()
	}

	private fun initUI() {
		swipeRefreshLayout.isRefreshing = viewModel.taskStatus.value == ListViewModel.TaskStatus.RUNNING
		airlines_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
		listAdapter = AirlinesListAdapter(context!!)
		listAdapter.onAirlineSelectedListener = onAirlineSelectedListener
		airlines_rv.adapter = listAdapter
		airlines_rv.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, LinearLayout.VERTICAL))
		viewModel.selectedAirlines.value?.let {
			listAdapter.setData(it)
		}
	}

	private fun onSuccess(result: List<Airline>) {
		swipeRefreshLayout.isRefreshing = false
		listAdapter.setData(result)
	}

	private fun onFailure() {
		swipeRefreshLayout.isRefreshing = false
		Snackbar.make(main_layout, R.string.no_connection, Snackbar.LENGTH_LONG)
				.setAction(getString(R.string.retry).toUpperCase()) { viewModel.refreshAllAirlines() }
				.show()
	}
}