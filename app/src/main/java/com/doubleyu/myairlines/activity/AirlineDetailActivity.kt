package com.doubleyu.myairlines.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.doubleyu.myairlines.R
import com.doubleyu.myairlines.databinding.ActivityAirlineDetailBinding
import com.doubleyu.myairlines.manager.AirlineManager
import com.doubleyu.myairlines.model.Airline
import kotlinx.android.synthetic.main.activity_airline_detail.*

class AirlineDetailActivity : AppCompatActivity() {
	private val airline: Airline by lazy {
		intent.getParcelableExtra(AIRLINE_EXTRA) as Airline
	}
	private var favoriteEdited: Boolean = false
	private val airlineManager = AirlineManager
	val isAirlineFavorite: Boolean
		get() = AirlineManager.isFavorite(airline)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityAirlineDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_airline_detail)
		binding.airline = airline
		binding.activity = this
		savedInstanceState?.getBoolean(FAVORITE_EDITED)?.let { favoriteEdited = it }
		if (favoriteEdited) {
			setResult(Activity.RESULT_OK)
		}
	}

	override fun onStop() {
		super.onStop()
		if (favoriteEdited) {
			airlineManager.saveFavoriteAirlines()
		}
	}

	fun onFavoriteClick() {
		setResult(Activity.RESULT_OK)
		favoriteEdited = true
		if (isAirlineFavorite) {
			airline_detail_favorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_border))
			airlineManager.removeFavorite(airline)
		} else {
			airline_detail_favorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star))
			airlineManager.addFavorite(airline)
		}
	}

	fun openWebpageInExternalBrowser(url: String) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(prepareUrl(url)))
		this.startActivity(browserIntent)
	}

	private fun prepareUrl(url: String): String {
		return "http://$url"
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		outState?.putBoolean(FAVORITE_EDITED, favoriteEdited)
		super.onSaveInstanceState(outState)
	}

	companion object {
		fun startActivityForResult(activity: Activity, airline: Airline) {
			val intent = Intent(activity, AirlineDetailActivity::class.java)
			intent.putExtra(AIRLINE_EXTRA, airline)
			activity.startActivityForResult(intent, FAVORITE_SET_CHANGED_REQUEST)
		}

		private const val AIRLINE_EXTRA = "AIRLINE_EXTRA"
		private const val FAVORITE_EDITED = "FAVORITE_EDITED"
		const val FAVORITE_SET_CHANGED_REQUEST = 1
	}
}