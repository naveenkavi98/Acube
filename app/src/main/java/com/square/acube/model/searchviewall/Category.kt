package com.square.acube.model.searchviewall

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("botom"            ) var botom           : String?                 = null,
    @SerializedName("icon"             ) var icon            : String?                 = null,
    @SerializedName("id"               ) var id              : String?                 = null,
    @SerializedName("sort"             ) var sort            : String?                 = null,
    @SerializedName("status"           ) var status          : String?                 = null,
    @SerializedName("title"            ) var title           : String?                 = null,
    @SerializedName("top"              ) var top             : String?                 = null
)