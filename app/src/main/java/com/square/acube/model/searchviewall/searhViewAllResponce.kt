package com.square.acube.model.searchviewall

import com.google.gson.annotations.SerializedName

data class searhViewAllResponce(
    @SerializedName("category"         ) var category        : ArrayList<Category>     = arrayListOf(),
    @SerializedName("msg"              ) var msg             : String?                 = null,
    @SerializedName("video"            ) var video           : ArrayList<Video>        = arrayListOf()

    )