package com.doubleyu.myairlines.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.fragment.ListActivityFragment
import com.doubleyu.myairlines.listener.SpinnerInteractionListener
import com.doubleyu.myairlines.manager.FilterOption


class MainListActivity : AppCompatActivity() {
	private var listActivityFragment: ListActivityFragment? = null
	private lateinit var spinner: Spinner
	private var spinnerPosition: Int? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		listActivityFragment = supportFragmentManager.findFragmentByTag(AIRLINES_LIST_FRAGMENT) as ListActivityFragment?
		if (listActivityFragment == null) {
			listActivityFragment = ListActivityFragment()
			supportFragmentManager.beginTransaction().replace(R.id.main_layout, listActivityFragment!!, AIRLINES_LIST_FRAGMENT).commit()
		}
		spinnerPosition = savedInstanceState?.getInt(FILTER_SPINNER_POSITION)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main_menu, menu)
		val item = menu!!.findItem(R.id.filter_spinner)
		val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, FilterOption.values())
		val spinnerInteractionListener = object : SpinnerInteractionListener() {
			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				listActivityFragment?.setFilter(FilterOption.values()[position], userSelect)
				super.onItemSelected(parent, view, position, id)
			}
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner = item.actionView as Spinner
		spinner.adapter = adapter
		spinner.onItemSelectedListener = spinnerInteractionListener
		spinner.setOnTouchListener(spinnerInteractionListener)

		if (spinnerPosition == null) {
			spinnerPosition = DEFAULT_FILTER_POSITION
		}
		spinner.setSelection(spinnerPosition!!)

		return super.onCreateOptionsMenu(menu)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == AirlineDetailActivity.FAVORITE_SET_CHANGED_REQUEST
				&& resultCode == Activity.RESULT_OK) {
			listActivityFragment?.favoriteSetChanged()
		}
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		outState?.putInt(FILTER_SPINNER_POSITION, spinner.selectedItemPosition)
		super.onSaveInstanceState(outState)
	}

	companion object {
		private const val AIRLINES_LIST_FRAGMENT = "AIRLINES_LIST_FRAGMENT"
		private const val FILTER_SPINNER_POSITION = "FILTER_SPINNER_POSITION"
		private const val DEFAULT_FILTER_POSITION = 0
	}
}
