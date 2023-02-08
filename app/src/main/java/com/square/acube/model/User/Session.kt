package com.square.acube.model.User

import com.google.gson.annotations.SerializedName


data class Session(@SerializedName("status"    ) var status  : Boolean? = null)
