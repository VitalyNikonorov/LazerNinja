package net.nikonorov.lazerninja.logic

import android.content.Context
import android.content.Loader
import net.nikonorov.lazerninja.logic.api.RecoveryRequest
import net.nikonorov.lazerninja.logic.api.SignAPI
import net.nikonorov.lazerninja.logic.api.TextResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 28.02.16.
 */

class LoaderRecovery: Loader<TextResponse>{

    private var retrofit : Retrofit
    private val recoveryObject: RecoveryRequest
    private var data: TextResponse? = null

    constructor(context: Context, request: RecoveryRequest) : super(context){

        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        this.recoveryObject = request
    }

    override fun deliverResult(response: TextResponse) {
        data = response
        super.deliverResult(data)
    }


    override fun onStartLoading(){
        if(data != null){
            deliverResult(data as TextResponse)
        }else{
            forceLoad()
        }
    }

    override fun onForceLoad() {

        val signApi = retrofit.create(SignAPI::class.java)

        val call: Call<TextResponse> = signApi.resetPass(recoveryObject)

        call.enqueue(object : Callback<TextResponse> {
            override fun onResponse(p0: Call<TextResponse>?, response: Response<TextResponse>?) {
                if(response != null) {
                    val error = response.errorBody().string() //TODO
                    deliverResult(response.body())
                }
            }

            override fun onFailure(p0: Call<TextResponse>?, p1: Throwable?) {
                throw UnsupportedOperationException()
            }

        })

    }

}