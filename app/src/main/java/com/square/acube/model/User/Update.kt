package com.square.acube.model.User

import com.google.gson.annotations.SerializedName
import com.square.acube.model.User.Data

data class Update(
    @SerializedName("msg"    ) var msg          : String?    = null,
    @SerializedName("data"   ) var data         : Data = Data()
)
