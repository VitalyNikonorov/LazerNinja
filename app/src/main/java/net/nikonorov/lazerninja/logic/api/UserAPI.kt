package net.nikonorov.lazerninja.logic.api

import net.nikonorov.lazerninja.UserProfile
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by vitaly on 27.02.16.
 */

interface UserAPI {

    @Headers("Content-Type: application/json")
    @POST("users/auth/")
    fun registrate(@Body body: RegRequest): Call<TextResponse>


    @Headers("Content-Type: application/json")
    @POST("users/auth/login/")
    fun auth(@Body body: AuthRequest): Call<TextResponse>

    @Headers("Content-Type: application/json")
    @POST("users/auth/password/reset/")
    fun resetPass(@Body body: RecoveryRequest): Call<SuccessResponse>

    @GET("users/")
    fun getInfo(@Header("Authorization") token : String): Call<UserProfile>
}