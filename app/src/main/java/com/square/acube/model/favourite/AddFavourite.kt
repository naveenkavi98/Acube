package com.square.acube.model.favourite

import com.google.gson.annotations.SerializedName


data class AddFavourite(
    @SerializedName("data"   ) var data         : AddFavouriteData?  = AddFavouriteData(),
    @SerializedName("msg"    ) var msg          : String?                      = null,
)