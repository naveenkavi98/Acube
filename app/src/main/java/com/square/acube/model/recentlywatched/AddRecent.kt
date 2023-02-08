package com.square.acube.model.recentlywatched

import com.google.gson.annotations.SerializedName

data class AddRecent(
    @SerializedName("data"          ) var data         : AddRecentData?              = AddRecentData(),
    @SerializedName("msg"           ) var msg          : String?                 = null
)