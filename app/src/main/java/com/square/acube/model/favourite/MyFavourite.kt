package com.square.acube.model.favourite

import com.google.gson.annotations.SerializedName
import com.square.acube.model.favourite.FavouriteData

data class MyFavourite(
    @SerializedName("data"   ) var data         : ArrayList<FavouriteData>  = arrayListOf(),
)