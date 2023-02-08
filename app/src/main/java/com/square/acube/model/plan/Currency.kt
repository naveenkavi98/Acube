package com.square.acube.model.plan

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("id"                ) var id                    : String?                 = null,
    @SerializedName("countryname"       ) var countryname           : String?                 = null,
    @SerializedName("countrycode"       ) var countrycode           : String?                 = null,
    @SerializedName("currencycode"      ) var currencycode          : String?                 = null,
    @SerializedName("symbol"            ) var symbol                : String?                 = null,
    @SerializedName("price"             ) var price                 : String?                 = null
)