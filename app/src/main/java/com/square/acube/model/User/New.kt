package com.square.acube.model.User

import com.google.gson.annotations.SerializedName

data class New(  @SerializedName("msg"            ) var msg                 : Boolean? = null,
                 @SerializedName("lessUser"       ) var lessUser            : String? = null)
