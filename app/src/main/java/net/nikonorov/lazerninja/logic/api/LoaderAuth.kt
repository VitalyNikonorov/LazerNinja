package net.nikonorov.lazerninja.logic.api

import android.content.Context
import android.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 27.02.16.
 */

class LoaderAuth: Loader<AuthToken>{

    private var retrofit : Retrofit
    private val authObject: AuthRequest
    private var data: AuthToken? = null

    constructor(context: Context, authRequest: AuthRequest): super(context){

        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        this.authObject = authRequest
    }

    override fun deliverResult(token: AuthToken) {
        data = token
        super.deliverResult(data)
    }

    override fun onStartLoading(){
        if(data != null){
            deliverResult(data as AuthToken)
        }else{
            forceLoad()
        }
    }

    override fun onForceLoad() {

        val signApi = retrofit.create(SignAPI::class.java)

        val call: Call<AuthToken> = signApi.auth(authObject)

        call.enqueue(object : Callback<AuthToken> {
            override fun onResponse(p0: Call<AuthToken>?, response: Response<AuthToken>?) {
                if(response != null) {
                    deliverResult(response.body())
                }
            }

            override fun onFailure(p0: Call<AuthToken>?, p1: Throwable?) {
                throw UnsupportedOperationException()
            }

        })

    }

}