package com.doubleyu.myairlines.listener

import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView

abstract class SpinnerInteractionListener : AdapterView.OnItemSelectedListener, View.OnTouchListener {
	var userSelect : Boolean = false
	override fun onNothingSelected(parent: AdapterView<*>?) {}
	override fun onTouch(v: View?, event: MotionEvent?): Boolean {
		userSelect = true
		return false
	}

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		if(userSelect) {
			userSelect = false
		}
	}
}