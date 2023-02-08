package com.square.acube.model.category

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("id"       ) var id       : String? = null,
    @SerializedName("title"    ) var title    : String? = null,
    @SerializedName("sort"     ) var sort     : String? = null,
    @SerializedName("category" ) var category : String? = null,
    @SerializedName("url"      ) var url      : String? = null,
    @SerializedName("image"    ) var image    : String? = null,
    @SerializedName("status"   ) var status   : String? = null,
    @SerializedName("videoid"  ) var videoid  : String? = null
)