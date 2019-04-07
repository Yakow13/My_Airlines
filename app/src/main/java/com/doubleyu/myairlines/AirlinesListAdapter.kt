package com.doubleyu.myairlines

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doubleyu.myairlines.activity.AirlineDetailActivity
import com.doubleyu.myairlines.helper.PicassoHelper
import kotlinx.android.synthetic.main.airline_list_item.view.*

class AirlinesListAdapter(private val context: Context) : RecyclerView.Adapter<AirlinesListAdapter.ViewHolder>() {

	var airlines: List<Airline> = ArrayList()

	override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.airline_list_item, p0, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return airlines.size
	}

	override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
		p0.bind(airlines[p1])
	}

	fun setData(airlines: List<Airline>) {
		this.airlines = airlines
		notifyDataSetChanged()
	}

	class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
		private val airlineLogo = view.airline_list_item_logo
		private val airlineName = view.airline_list_item_name

		private fun onItemSelected(airline: Airline, context: Context) {
			AirlineDetailActivity.startActivity(context, airline)
		}

		fun bind(airline: Airline) {
			view.setOnClickListener { onItemSelected(airline, view.context) }
			airlineName.text = airline.defaultName
			PicassoHelper.loadImageInto(airline.logoURL, airlineLogo)
		}
	}
}