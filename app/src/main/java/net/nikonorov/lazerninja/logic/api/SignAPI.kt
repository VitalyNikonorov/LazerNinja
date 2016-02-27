package net.nikonorov.lazerninja.logic.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by vitaly on 27.02.16.
 */

interface SignAPI {

    @Headers("Content-Type: application/json")
    @POST("users/auth/")
    fun registrate(@Body body: RegRequest): Call<AuthToken>

}