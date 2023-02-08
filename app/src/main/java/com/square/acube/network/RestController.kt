package com.square.acube.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
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
import com.square.acube.network.RestProvider
import com.square.acube.utils.CommonUtils
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RestController @Throws(Exception::class) constructor(headerModel: HeaderModel?) {
    var mRestServiceProvider: RestProvider? = null

    init {
        val okHttpClient: OkHttpClient?
        okHttpClient = RestUtils.getOkkHttpClient(headerModel)
        val retrofit: Retrofit? = RestUtils.makeHttpRequest(okHttpClient)
        mRestServiceProvider = retrofit?.create(RestProvider::class.java)
    }

    fun getDashboard(context: Context?, responseCallback: ResponseCallback, phoneno:String) {
        val call: Call<DashboardResponse>? = mRestServiceProvider?.getDashboard(phoneno)
        call?.enqueue(object : Callback<DashboardResponse?> {
            override fun onResponse(call: Call<DashboardResponse?>, response: Response<DashboardResponse?>) {
                Log.e("getDashboard", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<DashboardResponse?>, t: Throwable) {
                Log.e("getDashboard BODY Fail", t.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun createUser(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String, country:String, mobileid:String, devicename:String) {
        val call: Call<New>? = mRestServiceProvider?.createUser( method,phoneno,country,mobileid,devicename)
        call?.enqueue(object : Callback<New?> {
            override fun onResponse(call: Call<New?>, response: Response<New?>) {
                Log.e("CreateUser", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<New?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun logoutUser(context: Context?, responseCallback: ResponseCallback, mobileid:String, method:String, phoneno:String, logoutlast:String) {
        val call: Call<LogoutLast>? = mRestServiceProvider?.logoutUser( mobileid,method,phoneno,logoutlast)
        call?.enqueue(object : Callback<LogoutLast?> {
            override fun onResponse(call: Call<LogoutLast?>, response: Response<LogoutLast?>) {
                Log.e("logoutUser", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<LogoutLast?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }
    fun loginOutStatus(context: Context?, responseCallback: ResponseCallback, mobileid:String, method:String, phoneno:String) {
        val call: Call<Session>? = mRestServiceProvider?.loginOutStatus( mobileid,method,phoneno)
        call?.enqueue(object : Callback<Session?> {
            override fun onResponse(call: Call<Session?>, response: Response<Session?>) {
                Log.e("loginOutStatus", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<Session?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun checkUser(context: Context?, responseCallback: ResponseCallback, mobileid:String, method:String, phoneno:String) {
        val call: Call<CheckUsers>? = mRestServiceProvider?.checkUser( mobileid,method,phoneno)
        call?.enqueue(object : Callback<CheckUsers?> {
            override fun onResponse(call: Call<CheckUsers?>, response: Response<CheckUsers?>) {
                Log.e("CheckUser", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<CheckUsers?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getUser(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String) {
        val call: Call<profile>? = mRestServiceProvider?.getUser( method,phoneno)
        call?.enqueue(object : Callback<profile?> {
            override fun onResponse(call: Call<profile?>, response: Response<profile?>) {
                Log.e("GetUser", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<profile?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun updateUser(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String, name:String, email:String) {
        val call: Call<Update>? = mRestServiceProvider?.updateUser( method,phoneno, name, email)
        call?.enqueue(object : Callback<Update?> {
            override fun onResponse(call: Call<Update?>, response: Response<Update?>) {
                Log.e("UpdateUser", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<Update?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun addFavourite(context: Context?, responseCallback: ResponseCallback, method:String, vieoid:String, phoneno:String) {
        val call: Call<AddFavourite>? = mRestServiceProvider?.addFavourite( method,vieoid,phoneno)
        call?.enqueue(object : Callback<AddFavourite?> {
            override fun onResponse(call: Call<AddFavourite?>, response: Response<AddFavourite?>) {
                Log.e("addFavourite", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<AddFavourite?>, t: Throwable) {
                Log.e("addFavouriteBODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getMyFavourite(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String) {
        val call: Call<MyFavourite>? = mRestServiceProvider?.getMyFavourite( method,phoneno)
        call?.enqueue(object : Callback<MyFavourite?> {
            override fun onResponse(call: Call<MyFavourite?>, response: Response<MyFavourite?>) {
                Log.e("getMyFavourite", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<MyFavourite?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun viewAll(context: Context?, responseCallback: ResponseCallback, id:String) {
        val call: Call<ViewAllResponse>? = mRestServiceProvider?.viewAll( id )
        call?.enqueue(object : Callback<ViewAllResponse?> {
            override fun onResponse(call: Call<ViewAllResponse?>, response: Response<ViewAllResponse?>) {
                Log.e("viewAll", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<ViewAllResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun searchViewAll(context: Context?, responseCallback: ResponseCallback, id:String) {
        val call: Call<searhViewAllResponce>? = mRestServiceProvider?.searhViewAll( id )
        call?.enqueue(object : Callback<searhViewAllResponce?> {
            override fun onResponse(call: Call<searhViewAllResponce?>, response: Response<searhViewAllResponce?>) {
                Log.e("searchViewAll", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<searhViewAllResponce?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getPlan(context: Context?, responseCallback: ResponseCallback, phoneno:String, method:String) {
        val call: Call<CheckUserPlan>? = mRestServiceProvider?.getPlan( phoneno , method)
        call?.enqueue(object : Callback<CheckUserPlan?> {
            override fun onResponse(call: Call<CheckUserPlan?>, response: Response<CheckUserPlan?>) {
                Log.e("getPlan", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<CheckUserPlan?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun upgradePlan(context: Context?, responseCallback: ResponseCallback, phoneno:String, method:String, mid:String) {
        val call: Call<UpgradePlan>? = mRestServiceProvider?.upgradePlan( phoneno , method,mid)
        call?.enqueue(object : Callback<UpgradePlan?> {
            override fun onResponse(call: Call<UpgradePlan?>, response: Response<UpgradePlan?>) {
                Log.e("getPlan", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<UpgradePlan?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun addRecentlyWatched(context: Context?, responseCallback: ResponseCallback, method:String, videoid:String, phoneno:String) {
        val call: Call<AddRecent>? = mRestServiceProvider?.addRecentlyWatched( method,videoid,phoneno)
        call?.enqueue(object : Callback<AddRecent?> {
            override fun onResponse(call: Call<AddRecent?>, response: Response<AddRecent?>) {
                Log.e("addRecentlyWatched", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<AddRecent?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun removeRecentlyWatched(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String, videoid:String) {
        val call: Call<RemoveRcent>? = mRestServiceProvider?.removeRecentlyWatched( method,phoneno,videoid)
        call?.enqueue(object : Callback<RemoveRcent?> {
            override fun onResponse(call: Call<RemoveRcent?>, response: Response<RemoveRcent?>) {
                Log.e("removeRecentlyWatched", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<RemoveRcent?>, t: Throwable) {
                Log.e("removeRecentlyWatchedBODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getRecentlyWatched(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String) {
        val call: Call<Recent>? = mRestServiceProvider?.getRecentlyWatched( method,phoneno)
        call?.enqueue(object : Callback<Recent?> {
            override fun onResponse(call: Call<Recent?>, response: Response<Recent?>) {
                Log.e("getRecentlyWatched", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<Recent?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getCategoryDetails(context: Context?, responseCallback: ResponseCallback, id:String) {
        val call: Call<CategoryResponse>? = mRestServiceProvider?.getCategoryDetails( id)
        call?.enqueue(object : Callback<CategoryResponse?> {
            override fun onResponse(call: Call<CategoryResponse?>, response: Response<CategoryResponse?>) {
                Log.e("getCategoryDetails", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<CategoryResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun getVideoDetails(context: Context?, phoneno: String, id:String, responseCallback: ResponseCallback, SEARCH:String) {
        val call: Call<VideoDetailsResponse>? = mRestServiceProvider?.getVideoDetails(phoneno,id,SEARCH)
        call?.enqueue(object : Callback<VideoDetailsResponse?> {
            override fun onResponse(
                call: Call<VideoDetailsResponse?>,
                response: Response<VideoDetailsResponse?>
            ) {
                Log.e("getVideoDetails", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<VideoDetailsResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun search(context: Context?, search: String, responseCallback: ResponseCallback) {
        val call: Call<SearchResponse>? = mRestServiceProvider?.search(search)
        call?.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                Log.e("search", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun recentSearch(context: Context?, search: String, responseCallback: ResponseCallback) {
        val call: Call<RecentSearchResponse>? = mRestServiceProvider?.recentSearch(search)
        call?.enqueue(object : Callback<RecentSearchResponse?> {
            override fun onResponse(
                call: Call<RecentSearchResponse?>,
                response: Response<RecentSearchResponse?>
            ) {
                Log.e("search", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<RecentSearchResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun forceUpdate(context: Context?, version: String, responseCallback: ResponseCallback) {
        val call: Call<ForceUpdateResponse>? = mRestServiceProvider?.forceUpdate(version)
        call?.enqueue(object : Callback<ForceUpdateResponse?> {
            override fun onResponse(
                call: Call<ForceUpdateResponse?>,
                response: Response<ForceUpdateResponse?>
            ) {
                Log.e("forceUpdate", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<ForceUpdateResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }


    fun sendAmountToStripe(context: Context?, responseCallback: ResponseCallback, country:String, amount:String) {
        val call: Call<StripeResponse>? = mRestServiceProvider?.sendAmountToStripe(country,amount)
        call?.enqueue(object : Callback<StripeResponse?> {
            override fun onResponse(
                call: Call<StripeResponse?>,
                response: Response<StripeResponse?>
            ) {
                Log.e("getStripe", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<StripeResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        },)
    }

    fun sendStripeResponse(context: Context?, responseCallback: ResponseCallback, phoneno:String, paymentIntents:String) {
        val call: Call<StripeResponse>? = mRestServiceProvider?.sendStripeResponse(phoneno,paymentIntents)
        call?.enqueue(object : Callback<StripeResponse?> {
            override fun onResponse(
                call: Call<StripeResponse?>,
                response: Response<StripeResponse?>
            ) {
                Log.e("sendStripe", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        handleErrorBody(response, responseCallback)
                    }
                    else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<StripeResponse?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        },)
    }

    fun deleteAccount(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String) {
        val call: Call<DeleteAccount>? = mRestServiceProvider?.deleteAccount(method,phoneno)
        call?.enqueue(object : Callback<DeleteAccount?> {
            override fun onResponse(call: Call<DeleteAccount?>, response: Response<DeleteAccount?>) {
                Log.e("deleteAccount:", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<DeleteAccount?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    fun checkUserAccount(context: Context?, responseCallback: ResponseCallback, method:String, phoneno:String) {
        val call: Call<CheckAccount>? = mRestServiceProvider?.checkUserAccount(method,phoneno)
        call?.enqueue(object : Callback<CheckAccount?> {
            override fun onResponse(call: Call<CheckAccount?>, response: Response<CheckAccount?>) {
                Log.e("checkUserAccount:", response.toString())
                when (response.code()) {
                    200 -> try {
                        responseCallback.onResponse(response.body())
                    } catch (e: java.lang.Exception) {
                        //handleErrorBody(response, responseCallback)
                    }
                    //else -> handleErrorBody(response, responseCallback)
                }
            }

            override fun onFailure(call: Call<CheckAccount?>, t: Throwable) {
                Log.e("BODY Fail", call.toString())
                if (!CommonUtils.isNetworkAvailable(context)) {
                    responseCallback.onNetworkFailure()
                } else {
                    responseCallback.onFailure(t)
                }
                handleFailure(t)
            }
        }
        )
    }

    companion object {
        private fun handleFailure(o: Any?) {
            if (o != null) {
                try {
                    if (o is Response<*>) {
//                    ResponseBody responseBody = ((Response) o).errorBody();
//                    if (responseBody != null) {
//                        String value = responseBody.string();
//                        ErrorBodyModel errorBodyModel = new Gson().fromJson(value, ErrorBodyModel.class);
//                        if (errorBodyModel != null) {
//                            Log.e(RestServiceController.class.getName(), errorBodyModel.getMessage());
//                            Log.e(RestServiceController.class.getName(), errorBodyModel.getCode().toString());
//                        }
//
//                    }
                    } else if (o is Throwable) {
                        val data = Gson().toJson(o)
                    }
                } catch (e: java.lang.Exception) {

                }
            }
        }

        private fun handleErrorResponse(o: Any?): Any? {
            val errorObj: Any? = null
            var value: String? = null
            if (o != null) {
                try {
                    if (o is Response<*>) {
                        try {
                            if (o.errorBody() != null) {
                                value = o.errorBody()!!.string()
                            }
                            if (value != null) {
                                //TODO: create error obj model
//                            JSONObject errorObjct = new JSONObject(value);
//                            int code = errorObjct.getInt("code");
//                            String message = errorObjct.getString("message");
//                            ErrorBodyModel errorBodyModel = new ErrorBodyModel();
//                            errorBodyModel.setCode(code);
//                            errorBodyModel.setMessage(message);
//                            if (errorBodyModel != null) {
//                                errorObj = errorBodyModel;
//                            }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: java.lang.Exception) {

                }
            }
            return errorObj
        }

        private fun handleErrorBody(
            response: Any?,
            responseCallback: ResponseCallback
        ) {
//            try {
//                var errorObject: ErrorModel? = null
//                val encrypted_decoded_bytes = Base64.decode(
//                    response.errorBody()!!.string().toByteArray(), Base64.DEFAULT
//                )
//                val decodedData: ByteArray = CommonUtils.encodeOrDecode(
//                    encrypted_decoded_bytes,
//                    Cipher.DECRYPT_MODE,
//                    Constants.AES_KEY
//                )
//                val result = String(decodedData)
//                errorObject = CommonUtils.toObject(result, ErrorModel::class.java) as ErrorModel?
//                if (errorObject == null) responseCallback.onNetworkFailure() else responseCallback.onFailure(
//                    errorObject
//                )
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
        }

    }

}