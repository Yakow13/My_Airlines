package com.doubleyu.myairlines.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.doubleyu.myairlines.Airline
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.helper.PicassoHelper
import com.doubleyu.myairlines.manager.AirlineManager
import kotlinx.android.synthetic.main.activity_airline_detail.*

class AirlineDetailActivity : AppCompatActivity() {

	private lateinit var airline: Airline
	private var favoriteEdited: Boolean = false
	private val listener: View.OnClickListener
		get() = View.OnClickListener {
			favoriteEdited = true
			if (AirlineManager.isFavorite(airline)) {
				airline_detail_favorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_border))
				AirlineManager.removeFavorite(airline)
			} else {
				airline_detail_favorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star))
				AirlineManager.addFavorite(airline)
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(com.doubleyu.myairlines.R.layout.activity_airline_detail)
		airline = intent.getParcelableExtra(AIRLINE_EXTRA) as Airline
		bindUI()
	}

	override fun onPause() {
		super.onPause()
		if (favoriteEdited) {
			AirlineManager.saveFavoriteAirlines()
		}
	}

	private fun bindUI() {
		PicassoHelper.loadImageInto(airline.logoURL, airline_detail_logo)
		airline_detail_name.text = airline.defaultName
		airline_detail_phone_number.text = airline.phone
		airline_detail_web_address.text = airline.site

		airline_detail_favorite.setImageDrawable(if (AirlineManager.isFavorite(airline))
			getDrawable(R.drawable.ic_baseline_star)
		else getDrawable(R.drawable.ic_baseline_star_border))

		airline_detail_favorite.setOnClickListener(listener)

		if (airline.site.isEmpty()) {
			airline_detail_web_button.isEnabled = false
		} else {
			airline_detail_web_button.setOnClickListener { openWebpageInExternalBrowser(airline.site) }
		}
	}

	private fun openWebpageInExternalBrowser(url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(prepareUrl(url)))
		this.startActivity(browserIntent)
	}

	private fun prepareUrl(url: String): String {
		return "http://$url"
	}

	companion object {
		fun startActivity(context: Context, airline: Airline) {
			val intent = Intent(context, AirlineDetailActivity::class.java)
			intent.putExtra(AIRLINE_EXTRA, airline)
			context.startActivity(intent)
		}

		private const val AIRLINE_EXTRA = "AIRLINE_EXTRA"
	}
}