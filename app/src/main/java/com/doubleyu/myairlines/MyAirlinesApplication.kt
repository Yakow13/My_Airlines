package com.doubleyu.myairlines

import android.app.Application

class MyAirlinesApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		instance = this
	}

	companion object {
		lateinit var instance: MyAirlinesApplication
			private set

	}
}
