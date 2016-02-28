package net.nikonorov.lazerninja.logic

import android.content.Context
import android.content.Loader
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.UserProfile
import net.nikonorov.lazerninja.logic.api.TextResponse
import net.nikonorov.lazerninja.logic.api.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 28.02.16.
 */

class LoaderUserProfile: Loader<UserProfile>{

    private var retrofit : Retrofit
    private var data: UserProfile? = null

    constructor(context: Context) : super(context){

        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    override fun deliverResult(response: UserProfile) {
        data = response
        super.deliverResult(data)
    }

    override fun onStartLoading(){
        if(data != null){
            deliverResult(data as UserProfile)
        }else{
            forceLoad()
        }
    }

    override fun onForceLoad() {

        val api = retrofit.create(UserAPI::class.java)

        val call: Call<UserProfile> = api.getInfo("Token "+App.token)

        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(p0: Call<UserProfile>?, response: Response<UserProfile>?) {
                if(response != null) {
                    //val error = response.errorBody().string() //TODO
                    deliverResult(response.body())
                }
            }

            override fun onFailure(p0: Call<UserProfile>?, p1: Throwable?) {
                throw UnsupportedOperationException()
            }

        })

    }

}