package org.freedu.retrofitb6.netwrok

import org.freedu.retrofitb6.Model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>
}