package com.doubleyu.myairlines.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.fragment.ListActivityFragment
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

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		spinner = item.actionView as Spinner
		spinner.adapter = adapter
		spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {/*NOTHING*/
			}

			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				listActivityFragment?.setFilter(FilterOption.values()[position])
			}
		}

		if (spinnerPosition != null) {
			spinner.setSelection(spinnerPosition!!)
		}

		return super.onCreateOptionsMenu(menu)
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		outState?.putInt(FILTER_SPINNER_POSITION, spinner.selectedItemPosition)
		super.onSaveInstanceState(outState)
	}

	companion object {
		private const val AIRLINES_LIST_FRAGMENT = "AIRLINES_LIST_FRAGMENT"
		private const val FILTER_SPINNER_POSITION = "FILTER_SPINNER_POSITION"
	}
}
