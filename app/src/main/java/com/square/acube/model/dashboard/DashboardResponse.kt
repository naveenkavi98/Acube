package com.square.acube.model.dashboard

import com.google.gson.annotations.SerializedName


data class DashboardResponse (

    @SerializedName("banner"   ) var banner         : ArrayList<Banner>  = arrayListOf(),
    @SerializedName("section"  ) var section        : ArrayList<Section> = arrayListOf(),
    @SerializedName("category" ) var category       : ArrayList<Category> = arrayListOf(),
    @SerializedName("categorytop" ) var categorytop    : ArrayList<Category> = arrayListOf(),
    @SerializedName("categorybottom" ) var categorybottom : ArrayList<Category> = arrayListOf(),
    @SerializedName("recently" ) var recently : ArrayList<Recently> = arrayListOf(),
    @SerializedName("RazKey"   ) var RazKey   : String? = null
)