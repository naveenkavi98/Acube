package com.square.acube.model.User

import com.google.gson.annotations.SerializedName

data class DeleteAccount(
    @SerializedName("msg"       ) var msg            : String? = null
)