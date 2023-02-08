package com.square.acube.model.plan

import com.google.gson.annotations.SerializedName

data class Allplane(
    @SerializedName("amount"              ) var amount             : String?                 = null,
    @SerializedName("casting"             ) var casting            : String?                 = null,
    @SerializedName("containads"          ) var containads         : String?                 = null,
    @SerializedName("id"                  ) var id                 : String?                 = null,
    @SerializedName("logins"              ) var logins             : String?                 = null,
    @SerializedName("planname"            ) var planname           : String?                 = null,
    @SerializedName("sortorder"           ) var sortorder          : String?                 = null,
    @SerializedName("status"              ) var status             : String?                 = null,
    @SerializedName("validity"            ) var validity           : String?                 = null,
    @SerializedName("activated"           ) var activated          : Boolean?                = null,
    @SerializedName("symbol"              ) var symbol             : String?                 = null,

)