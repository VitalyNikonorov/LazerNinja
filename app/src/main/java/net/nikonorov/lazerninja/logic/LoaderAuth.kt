package net.nikonorov.lazerninja.logic

import android.content.Context
import android.content.Loader
import android.widget.Toast
import net.nikonorov.lazerninja.logic.api.AuthRequest
import net.nikonorov.lazerninja.logic.api.TextResponse
import net.nikonorov.lazerninja.logic.api.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 27.02.16.
 */

class LoaderAuth: Loader<TextResponse> {

    private var retrofit : Retrofit
    private val authObject: AuthRequest
    private var data: TextResponse? = null

    constructor(context: Context, authRequest: AuthRequest): super(context){

        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        this.authObject = authRequest
    }

    override fun deliverResult(token: TextResponse) {
        data = token
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

        val signApi = retrofit.create(UserAPI::class.java)

        val call: Call<TextResponse> = signApi.auth(authObject)

        call.enqueue(object : Callback<TextResponse> {
            override fun onResponse(p0: Call<TextResponse>?, response: Response<TextResponse>?) {
                if(response != null) {
                    if(response.errorBody() != null){
                        Toast.makeText(context, "Неверные данные", Toast.LENGTH_SHORT).show()
                    }else if (response.body() != null) {
                        deliverResult(response.body())
                    }
                }
            }

            override fun onFailure(p0: Call<TextResponse>?, p1: Throwable?) {
                //throw UnsupportedOperationException()
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }

        })

    }

}