package com.square.acube.model.User

import com.google.gson.annotations.SerializedName
import com.square.acube.model.User.Data


data class profile(
    @SerializedName("data"   ) var data         : Data?  = Data()
)