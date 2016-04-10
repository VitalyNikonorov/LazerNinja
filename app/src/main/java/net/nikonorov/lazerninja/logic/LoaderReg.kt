package net.nikonorov.lazerninja.logic

import android.content.Context
import android.content.Loader
import android.widget.Toast
import net.nikonorov.lazerninja.logic.api.TextResponse
import net.nikonorov.lazerninja.logic.api.RegRequest
import net.nikonorov.lazerninja.logic.api.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by vitaly on 27.02.16.
 */

class LoaderReg : Loader<TextResponse> {

    private var retrofit : Retrofit
    private val regObject : RegRequest
    private var data : TextResponse? = null


    constructor(context: Context, regRequest: RegRequest) : super(context){
        retrofit = Retrofit.Builder()
                .baseUrl("http://ec2-52-23-209-14.compute-1.amazonaws.com:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        this.regObject = regRequest
    }

    override fun deliverResult(token: TextResponse) {
        super.deliverResult(token)
        data = token
    }

    override fun onStartLoading(){
        if(data != null){
            deliverResult(data as TextResponse)
        }else{
            forceLoad()
        }
    }

    override fun onForceLoad() {

        val signupApi = retrofit.create(UserAPI::class.java)

        val call: Call<TextResponse> = signupApi.registrate(regObject)

        call.enqueue(object : Callback<TextResponse> {
            override fun onResponse(p0: Call<TextResponse>?, response: Response<TextResponse>?) {
                if(response != null) {
                    if(response.errorBody() != null){
                        Toast.makeText(context, "Ошибка. Проверьте данные", Toast.LENGTH_SHORT).show()
                    }else if (response.body() != null) {
                        deliverResult(response.body())
                    }
                }
            }

            override fun onFailure(p0: Call<TextResponse>?, p1: Throwable?) {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                //throw UnsupportedOperationException()
            }

        })

    }

}
