package com.example.networkretrofit.retrofit

import com.example.networkretrofit.RecyclerViewAdapter
import com.example.networkretrofit.model.Repository
import com.example.networkretrofit.retrofit.Url.BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class GitUserRetrofit : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    suspend fun retrofitCreate(adapter: RecyclerViewAdapter) = withContext(coroutineContext) {
        try {
            retrofit()
                .create(RetrofitService::class.java) //레트로핏 인터페이스를 기반으로 레트로핏을 생성
                .gitUsers() //Call<Repository> 반환
                .enqueue(object : Callback<Repository> { //Call 객체의 enqueue() 함수를 호출 해 네트워크 통신 수행
                    override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                        adapter.userList = response.body() as Repository
                        adapter.notifyDataSetChanged()
                    }
                    override fun onFailure(call: Call<Repository>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

