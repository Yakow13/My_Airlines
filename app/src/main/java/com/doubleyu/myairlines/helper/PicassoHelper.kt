package com.doubleyu.myairlines.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.doubleyu.myairlines.R
import com.squareup.picasso.Picasso

@BindingAdapter("app:imageUrl")
fun setImage(view: ImageView, imageUrl: String) {
	PicassoHelper.loadImage(view, imageUrl)
}

object PicassoHelper {
	private const val kayakUrl = "https://www.kayak.com"

	fun loadImage(view: ImageView, imageUrl: String) {
		Picasso.get()
				.load(kayakUrl + imageUrl)
				.placeholder(R.drawable.ic_file_download_black)
				.into(view)
	}
}