package com.square.acube.model.details

import com.google.gson.annotations.SerializedName


data class RelatedVideo (

  @SerializedName("id"               ) var id               : String? = null,
  @SerializedName("category"         ) var category         : String? = null,
  @SerializedName("title"            ) var title            : String? = null,
  @SerializedName("shortdescription" ) var shortdescription : String? = null,
  @SerializedName("lang"             ) var lang             : String? = null,
  @SerializedName("image1"           ) var image1           : String? = null,
  @SerializedName("image2"           ) var image2           : String? = null,
  @SerializedName("dor"              ) var dor              : String? = null,
  @SerializedName("freepaid"         ) var freepaid         : String? = null,
  @SerializedName("cast"             ) var cast             : String? = null,
  @SerializedName("gener"            ) var gener            : String? = null,
  @SerializedName("musicdireacter"   ) var musicdireacter   : String? = null,
  @SerializedName("direacter"        ) var direacter        : String? = null,
  @SerializedName("producer"         ) var producer         : String? = null,
  @SerializedName("certificate"      ) var certificate      : String? = null,
  @SerializedName("relesingsoon"     ) var relesingsoon     : String? = null,
  @SerializedName("url"              ) var url              : String? = null,
  @SerializedName("embed_url"        ) var embed_url        : String? = null,
  @SerializedName("status"           ) var status           : String? = null,
  @SerializedName("age"              ) var age              : String? = null

)