package com.doubleyu.myairlines.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doubleyu.myairlines.MyAirlinesApplication
import com.doubleyu.myairlines.api.ApiCallback
import com.doubleyu.myairlines.manager.AirlineManager
import com.doubleyu.myairlines.manager.FilterOption
import com.doubleyu.myairlines.model.Airline
import com.doubleyu.myairlines.model.AirlinesDataModel

class ListViewModel(application: Application) : AndroidViewModel(application) {
	private val dataModel = AirlinesDataModel.instance()
	private var allAirlines: LiveData<List<Airline>> = dataModel.allAirlines
	var selectedAirlines = MutableLiveData<List<Airline>>()
	val taskStatus = MutableLiveData<TaskStatus>()
	val hasData: Boolean
		get() {
			return !allAirlines.value.isNullOrEmpty()
		}

	fun refreshAllAirlines() {
		if (!isNetworkAvailable()) {
			taskStatus.value = TaskStatus.FAIL
			return
		}
		taskStatus.value = TaskStatus.RUNNING
		dataModel.refreshAirlines(object : ApiCallback {
			override fun onSuccess() {
				taskStatus.value = TaskStatus.SUCCESS
				filter()
			}

			override fun onFailure() {
				taskStatus.value = TaskStatus.FAIL
			}
		})
	}

	fun favoriteSetChanged() {
		if (AirlineManager.selectedFilter == FilterOption.FAVORITE) {
			filter()
		}
	}

	fun setFilter(selectedFilterOption: FilterOption) {
		AirlineManager.selectedFilter = selectedFilterOption
		filter()
	}

	fun filter() {
		allAirlines.value?.let {
			selectedAirlines.value = AirlineManager.filter(it)
		}
	}

	private fun isNetworkAvailable(): Boolean {
		val connectivityManager = MyAirlinesApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}

	enum class TaskStatus {
		RUNNING,
		SUCCESS,
		FAIL
	}

}