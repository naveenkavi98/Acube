package com.square.acube.model.category

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("age"               ) var age                : String? = null,
    @SerializedName("cast"              ) var cast               : String? = null,
    @SerializedName("category"          ) var category           : String? = null,
    @SerializedName("certificate"       ) var certificate        : String? = null,
    @SerializedName("direacter"         ) var direacter          : String? = null,
    @SerializedName("dor"               ) var dor                : String? = null,
    @SerializedName("film"              ) var film               : String? = null,
    @SerializedName("filmsort"          ) var filmsort           : String? = null,
    @SerializedName("freepaid"          ) var freepaid           : String? = null,
    @SerializedName("gener"             ) var gener              : String? = null,
    @SerializedName("id"                ) var id                 : String? = null,
    @SerializedName("image1"            ) var image1             : String? = null,
    @SerializedName("image2"            ) var image2             : String? = null,
    @SerializedName("imdb"              ) var imdb               : String? = null,
    @SerializedName("lang"              ) var lang               : String? = null,
    @SerializedName("menu"              ) var menu               : String? = null,
    @SerializedName("musicdireacter"    ) var musicdireacter     : String? = null,
    @SerializedName("plans"             ) var plans              : String? = null,
    @SerializedName("producer"          ) var producer           : String? = null,
    @SerializedName("relesingsoon"      ) var relesingsoon       : String? = null,
    @SerializedName("section"           ) var section            : String? = null,
    @SerializedName("shortdescription"  ) var shortdescription   : String? = null,
    @SerializedName("sort"              ) var sort               : String? = null,
    @SerializedName("status"            ) var status             : String? = null,
    @SerializedName("svid"              ) var svid               : String? = null,
    @SerializedName("title"             ) var title              : String? = null,
    @SerializedName("url"               ) var url                : String? = null,
    @SerializedName("vid"               ) var vid                : String? = null,
    @SerializedName("video"             ) var video              : String? = null
)