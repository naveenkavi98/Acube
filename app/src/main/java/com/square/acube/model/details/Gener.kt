package com.square.acube.model.details

import com.google.gson.annotations.SerializedName


data class Gener (

  @SerializedName("id"           ) var id           : String? = null,
  @SerializedName("categoryname" ) var categoryname : String? = null,
  @SerializedName("status"       ) var status       : String? = null

)