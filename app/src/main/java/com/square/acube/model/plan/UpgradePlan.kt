package com.square.acube.model.plan

import com.google.gson.annotations.SerializedName

data class UpgradePlan(
    @SerializedName("userdetail"          ) var userdetail         : Userdetail?              = Userdetail(),
    @SerializedName("userplane"           ) var userplane          : Userplane?               = Userplane()
)