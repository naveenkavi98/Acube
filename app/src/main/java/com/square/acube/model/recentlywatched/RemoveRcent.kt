package com.square.acube.model.recentlywatched

import com.google.gson.annotations.SerializedName

data class RemoveRcent(
    @SerializedName("msg"           ) var msg          : String?                 = null
)