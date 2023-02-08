package com.square.acube.model.User

import com.google.gson.annotations.SerializedName

data class CheckUsers(
    @SerializedName("lessUser"  ) var lessUser       : Boolean? = null,
    @SerializedName("msg"       ) var msg            : String? = null
)