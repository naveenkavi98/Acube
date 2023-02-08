package com.square.acube.model.favourite

import com.google.gson.annotations.SerializedName


data class Language (
  @SerializedName("id"                  ) var id   : String?                    = null,
  @SerializedName("language_name"       ) var language_name   : String?         = null,
  @SerializedName("status"              ) var status   : String?                = null

)