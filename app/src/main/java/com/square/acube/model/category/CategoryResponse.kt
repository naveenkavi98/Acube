package com.square.acube.model.category

import com.google.gson.annotations.SerializedName
import com.square.acube.model.dashboard.Section

data class CategoryResponse(

    @SerializedName("data"     ) var data           : ArrayList<Data> = arrayListOf(),
    @SerializedName("banner"   ) var banner         : ArrayList<Banner>  = arrayListOf(),
    @SerializedName("section"  ) var section        : ArrayList<Section> = arrayListOf(),
)