package com.square.acube.model.favourite

import com.google.gson.annotations.SerializedName

data class FavouriteData(
    @SerializedName("cast"               ) var cast               : String?                = null,
    @SerializedName("category"           ) var category           : String?                = null,
    @SerializedName("certificate"        ) var certificate        : String?                = null,
    @SerializedName("direacter"          ) var direacter          : String?                = null,
    @SerializedName("dor"                ) var dor                : String?                = null,
    @SerializedName("freepaid"           ) var freepaid           : String?                = null,
    @SerializedName("gener"              ) var gener              : String?                = null,
    @SerializedName("id"                 ) var id                 : String?                = null,
    @SerializedName("image1"             ) var image1             : String?                = null,
    @SerializedName("image2"             ) var image2             : String?                = null,
    @SerializedName("lang"               ) var lang               : String?                = null,
    @SerializedName("musicdireacter"     ) var musicdireacter     : String?                = null,
    @SerializedName("producer"           ) var producer           : String?                = null,
    @SerializedName("relesingsoon"       ) var relesingsoon       : String?                = null,
    @SerializedName("shortdescription"   ) var shortdescription   : String?                = null,
    @SerializedName("status"             ) var status             : String?                = null,
    @SerializedName("title"              ) var title              : String?                = null,
    @SerializedName("url"                ) var url                : String?                = null,
    @SerializedName("language"           ) var language           : Language?              = Language()

)