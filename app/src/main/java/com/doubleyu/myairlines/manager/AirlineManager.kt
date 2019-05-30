package com.doubleyu.myairlines.manager

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.doubleyu.myairlines.MyAirlinesApplication
import com.doubleyu.myairlines.model.Airline
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AirlineManager {
	private const val FAVORITE_AIRLINES = "FAVORITE_AIRLINES"
	private val typeToken = object : TypeToken<MutableSet<String>>() {}.type
	private var favoriteAirlines: MutableSet<String> = HashSet()
	private val preferences: SharedPreferences
		get() = PreferenceManager.getDefaultSharedPreferences(MyAirlinesApplication.instance)

	var selectedFilter : FilterOption? = null

	init {
		val restoredData = getString(FAVORITE_AIRLINES, "")
		if (!restoredData.isNullOrEmpty()) {
			favoriteAirlines = Gson().fromJson(restoredData, typeToken)
		}
	}

	fun isFavorite(airline: Airline): Boolean {
		return favoriteAirlines.contains(airline.code)
	}

	fun addFavorite(airline: Airline) {
		favoriteAirlines.add(airline.code)
	}

	fun removeFavorite(airline: Airline) {
		favoriteAirlines.remove(airline.code)
	}

	fun filter(allAirlines : List<Airline>): List<Airline> {
		return allAirlines.filter { airline -> meetsPredicate(airline) }
	}

	fun saveFavoriteAirlines() {
		setString(FAVORITE_AIRLINES, Gson().toJson(favoriteAirlines, typeToken))
	}

	private fun meetsPredicate(airline: Airline) : Boolean {
		if(selectedFilter == null) {
			throw IllegalStateException("Selected filter must be defined")
		}
		val result: Boolean = when (selectedFilter!!) {
			FilterOption.ALL -> true
			FilterOption.FAVORITE -> favoriteAirlines.contains(airline.code)
		}

		return result
	}

	private fun getString(key: String, defaultValue: String?): String? {
		return preferences.getString(key, defaultValue)
	}

	private fun setString(key: String, value: String?) {
		preferences.edit().putString(key, value).apply()
	}

}