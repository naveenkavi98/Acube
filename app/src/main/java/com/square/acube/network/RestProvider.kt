package com.square.acube.network

import com.square.acube.model.User.*
import com.square.acube.model.category.CategoryResponse
import com.square.acube.model.dashboard.DashboardResponse
import com.square.acube.model.details.VideoDetailsResponse
import com.square.acube.model.search.SearchResponse
import com.square.acube.model.favourite.AddFavourite
import com.square.acube.model.favourite.MyFavourite
import com.square.acube.model.forceupdate.ForceUpdateResponse
import com.square.acube.model.plan.CheckUserPlan
import com.square.acube.model.plan.UpgradePlan
import com.square.acube.model.recentlywatched.AddRecent
import com.square.acube.model.recentlywatched.Recent
import com.square.acube.model.recentlywatched.RemoveRcent
import com.square.acube.model.search.RecentSearchResponse
import com.square.acube.model.searchviewall.searhViewAllResponce
import com.square.acube.model.stripes.StripeResponse
import com.square.acube.model.viewall.ViewAllResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

interface RestProvider {
    @GET("dashboad.php/")
    fun getDashboard(@Query("phoneno") phoneno:String
    ): Call<DashboardResponse>

    @GET("updateProfile.php/")
    fun createUser(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
        @Query("country") country:String,
        @Query("mobileid") mobileid:String,
        @Query("devicename")devicename:String
    ): Call<New>

    @GET("updateProfile.php/")
    fun logoutUser(
        @Query("mobileid") mobileid:String,
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
        @Query("logoutlast")logoutlast:String
    ): Call<LogoutLast>

    @GET("updateProfile.php/")
    fun checkUser(
        @Query("mobileid") mobileid:String,
        @Query("method") method:String,
        @Query("phoneno") phoneno:String
    ): Call<CheckUsers>

    @GET("updateProfile.php/")
    fun loginOutStatus(
        @Query("mobileid") mobileid:String,
        @Query("method") method:String,
        @Query("phoneno") phoneno:String
    ): Call<Session>

    @GET("updateProfile.php/")
    fun getUser(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String
    ): Call<profile>

    @GET("updateProfile.php/")
    fun updateUser(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
        //@Query("img") img: File,
        @Query("name") name:String,
        @Query("email") email:String,
    ): Call<Update>

    @GET("wishlist.php/")
    fun addFavourite(
        @Query("method") method:String,
        @Query("videoid") videoid:String,
        @Query("phoneno") phoneno:String
    ): Call<AddFavourite>

    @GET("wishlist.php/")
    fun getMyFavourite(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
    ): Call<MyFavourite>

    @GET("viewallvideos.php/")
    fun viewAll(
        @Query("id") id:String
    ): Call<ViewAllResponse>

    @GET("viewallcaregory.php/")
    fun searhViewAll(
        @Query("id") id:String
    ): Call<searhViewAllResponce>

    @GET("plane.php/")
    fun getPlan(
        @Query("phoneno") phoneno:String,
        @Query("method") method:String
    ): Call<CheckUserPlan>

    @GET("plane.php/")
    fun upgradePlan(
        @Query("phoneno") phoneno:String,
        @Query("method") method:String,
        @Query("mid") mid:String
    ): Call<UpgradePlan>

    @GET("recently.php/")
    fun addRecentlyWatched(
        @Query("method") method:String,
        @Query("videoid") videoid:String,
        @Query("phoneno") phoneno:String
    ): Call<AddRecent>

    @GET("recently.php/")
    fun removeRecentlyWatched(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
        @Query("id") id:String
    ): Call<RemoveRcent>

    @GET("recently.php/")
    fun getRecentlyWatched(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String,
    ): Call<Recent>

    @GET("categort.php/")
    fun getCategoryDetails(
        @Query("id") id:String
    ): Call<CategoryResponse>

    @GET("videodetails.php/")
    fun getVideoDetails(
        @Query("phoneno") phoneno:String,
        @Query("id") id:String,
        @Query("search") SEARCH:String
    ): Call<VideoDetailsResponse>

    @GET("search.php/")
    fun search(
        @Query("search") id:String,
    ): Call<SearchResponse>

    @GET("search.php/")
    fun recentSearch(
        @Query("phoneno") id:String,
    ): Call<RecentSearchResponse>

    @GET("update.php/")
    fun forceUpdate(
        @Query("version") id:String,
    ): Call<ForceUpdateResponse>

    @GET("stripes.php/")
    fun sendAmountToStripe(
        @Query("country") country:String,
        @Query("amount") amount:String
    ): Call<StripeResponse>

    @GET("stripesresponse.php/")
    fun sendStripeResponse(
        @Query("phoneno") phoneno:String,
        @Query("paymentIntents") paymentIntents:String
    ): Call<StripeResponse>

    @GET("updateProfile.php")
    fun deleteAccount(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String
    ): Call<DeleteAccount>

    @GET("updateProfile.php")
    fun checkUserAccount(
        @Query("method") method:String,
        @Query("phoneno") phoneno:String
    ): Call<CheckAccount>
}