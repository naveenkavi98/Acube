package com.square.acube.model.recentlywatched

import com.google.gson.annotations.SerializedName


data class Recent(
    @SerializedName("data"    ) var data        : ArrayList<RecentData>     = arrayListOf()
)