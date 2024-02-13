package com.example.jpmorganissapp.data.repository

import com.example.jpmorganissapp.data.source.db.AppDatabase
import com.example.jpmorganissapp.data.source.db.entity.toModel
import com.example.jpmorganissapp.data.source.remote.dto.AstrosDto
import com.example.jpmorganissapp.data.source.remote.dto.toEntity
import com.example.jpmorganissapp.data.source.remote.endpoint.ApiService
import com.example.jpmorganissapp.domain.model.AstrosModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AstrosRepo @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
    ) {
    /**
     * check if offline data is available then return from DB
     * */
    fun getAstros(forceRefresh:Boolean = false) :Flow<Result<List<AstrosModel>>>{
        return flow {
            val offlineResponse = appDatabase.astrosDao().getAll()
            if (forceRefresh || offlineResponse.isNullOrEmpty()){
                val response = apiService.getAstros()
                if (response.message=="success"){
                    val entities = response.toEntity()
                    appDatabase.astrosDao().insertAll(entities)
                    emit(Result.success(entities.map { it.toModel() }))
                }else{
                    emit(Result.failure(Exception(response.message)))
                }
            }else{
                emit(Result.success(offlineResponse.map { it.toModel() }))
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }

}