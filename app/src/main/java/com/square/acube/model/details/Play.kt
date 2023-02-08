package com.square.acube.model.details

import com.google.gson.annotations.SerializedName


data class Play (

  @SerializedName("status"           ) var status  : Boolean? = null,
  @SerializedName("msg"              ) var msg     : String? = null

)