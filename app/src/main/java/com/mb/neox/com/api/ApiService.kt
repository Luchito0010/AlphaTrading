package com.mb.neox.com.api

import com.mb.neox.com.models.ItemResponse
import com.mb.neox.com.models.ItemUpdateRequest
import com.mb.neox.com.models.LoginRequest
import com.mb.neox.com.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("Login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("Items")
    fun getItems(@Header("B1Session") B1Session: String): Call<ItemResponse>

    //  @PATCH("Items/{itemCode}") // posiblemente haya que alterarlo para poder enviar peticiones
    @Headers("Content-Type: application/json")
    @PATCH("Items('{itemCode}')")
    fun updateItem(
        @Header("B1Session") B1Session: String,
        @Path("itemCode") itemCode: String,
        @Body updateRequest: ItemUpdateRequest
    ): Call<Void>

}
