package com.square.acube.model.plan

import com.google.gson.annotations.SerializedName

data class CheckUserPlan(
    @SerializedName("allplane"           ) var allplane           : ArrayList<Allplane>     = arrayListOf(),
    @SerializedName("currency"           ) var currency           : Currency?               = Currency()
)