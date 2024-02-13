package com.example.jpmorganissapp.data.repository

import com.example.jpmorganissapp.data.source.db.AppDatabase
import com.example.jpmorganissapp.data.source.db.entity.IssEntity
import com.example.jpmorganissapp.data.source.db.entity.toModel
import com.example.jpmorganissapp.data.source.remote.dto.ISSPositionDto
import com.example.jpmorganissapp.data.source.remote.dto.toEntity
import com.example.jpmorganissapp.data.source.remote.endpoint.ApiService
import com.example.jpmorganissapp.domain.model.IssModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ISSRepo @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {

     fun getCurrentIssPosition(
        ):Flow<Result<IssModel>> {
          return flow {
              val response = apiService.getISSPosition()
              if(response.message =="success") {
                  val entity = response.toEntity()
                  appDatabase.issPositionsDao().insert(entity)
                  emit(Result.success(entity.toModel()))
              }
              else{
                  emit(Result.failure(Exception(response.message)))
              }
          }.catch {
              emit(Result.failure(it))
          }.flowOn(Dispatchers.IO)
     }
}