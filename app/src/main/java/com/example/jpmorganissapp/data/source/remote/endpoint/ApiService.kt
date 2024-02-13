package com.example.jpmorganissapp.data.source.remote.endpoint

import com.example.jpmorganissapp.data.source.remote.dto.AstrosResponseDto
import com.example.jpmorganissapp.data.source.remote.dto.ISSPositionDto
import retrofit2.http.GET

interface ApiService {
    @GET("/iss-now")
    suspend fun getISSPosition() : ISSPositionDto

    @GET("/astros")
    suspend fun getAstros() : AstrosResponseDto
}