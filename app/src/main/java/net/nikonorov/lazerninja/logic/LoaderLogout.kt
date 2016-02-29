package net.nikonorov.lazerninja.logic

import android.content.Context
import android.content.Loader
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.logic.api.SuccessResponse
import net.nikonorov.lazerninja.logic.api.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 29.02.16.
 */

class LoaderLogout : Loader<SuccessResponse>{

    private var retrofit : Retrofit
    private var data: SuccessResponse? = null


    constructor(context: Context) : super(context){
        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    override fun deliverResult(response : SuccessResponse) {
        data = response
        super.deliverResult(data)
    }

    override fun onStartLoading(){
        if(data != null){
            deliverResult(data as SuccessResponse)
        }else{
            forceLoad()
        }
    }

    override fun onForceLoad() {

        val api = retrofit.create(UserAPI::class.java)

        val call: Call<SuccessResponse> = api.logout("Token "+ App.token)

        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(p0: Call<SuccessResponse>?, response: Response<SuccessResponse>?) {
                if(response != null) {
                    //val error = response.errorBody().string() //TODO
                    deliverResult(response.body())
                }
            }

            override fun onFailure(p0: Call<SuccessResponse>?, p1: Throwable?) {
                throw UnsupportedOperationException()
            }

        })

    }

}
