package com.square.acube.model.search

import com.google.gson.annotations.SerializedName
import com.square.acube.model.search.RecentSearchData

data class RecentSearchResponse(
    @SerializedName("data"    ) var data        : ArrayList<RecentSearchData>     = arrayListOf(),
    @SerializedName("msg"     ) var msg         : String?                         = null
)