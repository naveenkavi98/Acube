package com.square.acube.model.viewall

import com.google.gson.annotations.SerializedName
import com.square.acube.model.viewall.SectionViewAll


data class ViewAllResponse(
    @SerializedName("section"     ) var section : SectionViewAll?  = SectionViewAll()
)