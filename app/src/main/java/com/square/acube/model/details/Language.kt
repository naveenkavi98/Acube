package com.square.acube.model.details

import com.google.gson.annotations.SerializedName


data class Language (

  @SerializedName("id"            ) var id           : String? = null,
  @SerializedName("language_name" ) var languageName : String? = null,
  @SerializedName("status"        ) var status       : String? = null

)