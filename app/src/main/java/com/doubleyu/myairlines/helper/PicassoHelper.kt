package com.doubleyu.myairlines.helper

import android.widget.ImageView
import com.doubleyu.myairlines.R
import com.squareup.picasso.Picasso

object PicassoHelper {
	private const val kayakUrl = "https://www.kayak.com"

	fun loadImageInto(url: String, imageView: ImageView) {
		Picasso.get()
				.load(kayakUrl + url)
				.placeholder(R.drawable.ic_file_download_black)
				.into(imageView)
	}
}