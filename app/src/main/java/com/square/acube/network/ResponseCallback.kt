package com.square.acube.network;

interface ResponseCallback {
    fun onResponse(t: Any?)
    fun onFailure(t: Any?)
    fun onNetworkFailure()
}