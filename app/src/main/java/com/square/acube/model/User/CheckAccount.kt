package com.square.acube.model.User

import com.google.gson.annotations.SerializedName

data class CheckAccount(
    @SerializedName("status"  ) var status       : Boolean? = null,
    @SerializedName("msg"     ) var msg          : String?  = null
)