package com.square.acube.model.viewall

import com.google.gson.annotations.SerializedName

data class SectionViewAll(
    @SerializedName("category"              ) var category             : String?                 = null,
    @SerializedName("film"                  ) var film                 : String?                 = null,
    @SerializedName("filmsort"              ) var filmsort             : String?                 = null,
    @SerializedName("gener"                 ) var gener                : String?                 = null,
    @SerializedName("id"                    ) var id                   : String?                 = null,
    @SerializedName("lang"                  ) var lang                 : String?                 = null,
    @SerializedName("menu"                  ) var menu                 : String?                 = null,
    @SerializedName("sort"                  ) var sort                 : String?                 = null,
    @SerializedName("status"                ) var status               : String?                 = null,
    @SerializedName("title"                 ) var title                : String?                 = null,
    @SerializedName("video"                 ) var video                : ArrayList<VideoViewAll> = arrayListOf()
)