package com.square.acube.model.category

import com.google.gson.annotations.SerializedName
import com.square.acube.model.dashboard.Video

data class Section(
    @SerializedName("id"       ) var id       : String?          = null,
    @SerializedName("title"    ) var title    : String?          = null,
    @SerializedName("menu"     ) var menu     : String?          = null,
    @SerializedName("category" ) var category : String?          = null,
    @SerializedName("lang"     ) var lang     : String?          = null,
    @SerializedName("gener"    ) var gener    : String?          = null,
    @SerializedName("sort"     ) var sort     : String?          = null,
    @SerializedName("film"     ) var film     : String?          = null,
    @SerializedName("filmsort" ) var filmsort : String?          = null,
    @SerializedName("status"   ) var status   : String?          = null,
    @SerializedName("video"    ) var video    : ArrayList<Video> = arrayListOf()
)