package com.square.acube.model.stripes

import com.google.gson.annotations.SerializedName

data class StripeResponse(
    @SerializedName("id"             ) var id                : String?                         = null,
    @SerializedName("customer"       ) var customer          : String?                         = null,
    @SerializedName("ephemeralKey"   ) var ephemeralKey      : String?                         = null,
    @SerializedName("paymentIntent"  ) var paymentIntent     : String?                         = null,
    @SerializedName("publishableKey" ) var publishableKey    : String?                         = null
)