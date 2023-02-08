package com.square.acube.model.details

import com.google.gson.annotations.SerializedName


data class VideoDetailsResponse (

    @SerializedName("video"            ) var video        : Video?                  = Video(),
    @SerializedName("language"         ) var language     : Language?               = Language(),
    @SerializedName("gener"            ) var gener        : Gener?                  = Gener(),
    @SerializedName("iswishlist"       ) var iswishlist   : Boolean?                = null,
    @SerializedName("relatedvideo"     ) var relatedvideo : ArrayList<RelatedVideo> = arrayListOf(),
    @SerializedName("play"             ) var play         : Play?                   = Play()

)