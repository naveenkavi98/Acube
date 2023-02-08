package com.square.acube.model.search

import com.google.gson.annotations.SerializedName
import com.square.acube.model.search.Data


data class SearchResponse (

  @SerializedName("msg"  ) var msg  : String?         = null,
  @SerializedName("data" ) var data : ArrayList<Data> = arrayListOf()

)