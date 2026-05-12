package com.JN.carnetrecettes.api

import com.JN.carnetrecettes.model.Recette
import retrofit2.http.*

interface ApiService {

    @GET("recettes")
    suspend fun getRecettes(): List<Recette>

    @POST("recettes")
    suspend fun createRecette(@Body recette: Recette): Recette

    @PUT("recettes/{id}")
    suspend fun updateRecette(@Path("id") id: Int, @Body recette: Recette): Recette

    @DELETE("recettes/{id}")
    suspend fun deleteRecette(@Path("id") id: Int)
}