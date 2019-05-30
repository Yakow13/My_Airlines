package com.doubleyu.myairlines

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doubleyu.myairlines.databinding.AirlineListItemBinding
import com.doubleyu.myairlines.listener.OnAirlineSelectedListener
import com.doubleyu.myairlines.model.Airline

class AirlinesListAdapter(private val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<AirlinesListAdapter.ViewHolder>() {

	private var airlines: List<Airline> = ArrayList()
	var onAirlineSelectedListener: OnAirlineSelectedListener? = null

	override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
		val inflater = LayoutInflater.from(context)
		val binding: AirlineListItemBinding = DataBindingUtil.inflate(inflater,R.layout.airline_list_item, p0, false)
		return ViewHolder(binding, onAirlineSelectedListener)
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

	class ViewHolder(private val binding: AirlineListItemBinding, private var onAirlineSelectedListener: OnAirlineSelectedListener?)
		: androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

		fun bind(airline: Airline) {
			binding.airline = airline
			binding.listener = onAirlineSelectedListener
		}
	}
}