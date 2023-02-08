package com.square.acube.model.forceupdate

import com.google.gson.annotations.SerializedName

data class ForceUpdateResponse (

    @SerializedName("status" ) var status : Boolean? = null,
    @SerializedName("force_update" ) var force_update : Boolean? = null

)