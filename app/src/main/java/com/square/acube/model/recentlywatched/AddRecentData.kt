package com.square.acube.model.recentlywatched

import com.google.gson.annotations.SerializedName

data class AddRecentData(
    @SerializedName("email"           ) var email          : String?                 = null,
    @SerializedName("id"              ) var id             : String?                 = null,
    @SerializedName("img"             ) var img            : String?                 = null,
    @SerializedName("membership"      ) var membership     : String?                 = null,
    @SerializedName("name"            ) var name           : String?                 = null,
    @SerializedName("phoneno"         ) var phoneno        : String?                 = null,
    @SerializedName("recently"        ) var recently       : String?                 = null,
    @SerializedName("validation"      ) var validation     : String?                 = null,
    @SerializedName("watchlater"      ) var watchlater     : String?                 = null
)