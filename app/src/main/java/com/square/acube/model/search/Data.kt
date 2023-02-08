package com.square.acube.model.search

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("id"     ) var id     : String?          = null,
  @SerializedName("title"  ) var title  : String?          = null,
  @SerializedName("sort"   ) var sort   : String?          = null,
  @SerializedName("icon"   ) var icon   : String?          = null,
  @SerializedName("status" ) var status : String?          = null,
  @SerializedName("video"  ) var video  : ArrayList<Video> = arrayListOf()

)