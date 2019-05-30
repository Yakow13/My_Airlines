package com.doubleyu.myairlines.listener

import com.doubleyu.myairlines.model.Airline

abstract class OnAirlineSelectedListener {
	 abstract fun onClick(airline: Airline)
}