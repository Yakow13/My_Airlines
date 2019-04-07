package com.doubleyu.myairlines.listener

interface NetworkTaskListener<T> {
    fun onSuccess(result : T)
    fun onFailure()
}