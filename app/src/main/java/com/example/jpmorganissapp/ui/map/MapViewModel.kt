package com.example.jpmorganissapp.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jpmorganissapp.data.repository.AstrosRepo
import com.example.jpmorganissapp.data.repository.ISSRepo
import com.example.jpmorganissapp.domain.model.AstrosModel
import com.example.jpmorganissapp.domain.model.IssModel
import com.example.jpmorganissapp.domain.model.UiState
import com.example.jpmorganissapp.domain.usecase.LocationTrackerUseCase
import com.example.jpmorganissapp.util.formatDistance
import com.example.jpmorganissapp.util.toLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val astrosRepo: AstrosRepo,
    private val issRepo: ISSRepo,
    private val locationTrackerUseCase: LocationTrackerUseCase,
) : ViewModel() {


    init {
        startGetIssPositions()
    }


    private val _astrosModelsState = MutableStateFlow<UiState<List<AstrosModel>>?>(null)
    val astrosModelsState: StateFlow<UiState<List<AstrosModel>>?> get() = _astrosModelsState

    private val _issModelsState = MutableStateFlow<UiState<IssModel>?>(UiState.Loading)
    val issModelsState : StateFlow<UiState<IssModel>?> get() = _issModelsState

    private val _locationModelState : MutableStateFlow<UiState<Location>?> = MutableStateFlow(null)
    val locationModelState : StateFlow<UiState<Location>?>  get() = _locationModelState

    val distanceState:StateFlow<String> = _issModelsState.combine(_locationModelState){ iss, loc->
        if (iss?.isSuccess()==true){
            val issModel:IssModel? = (((iss as? UiState.Success)?.data) as? IssModel)

            val currentLocation:Location? = (loc as? UiState.Success)?.data as? Location

            val distance:Float? =  currentLocation?.let {
                issModel?.let {
                    currentLocation.distanceTo(issModel.toLocation())
                }
            }
            distance?.toDouble()?.let { formatDistance(it) }?:"measuring..."
        }else "measuring..."
    }.stateIn(viewModelScope, SharingStarted.Eagerly,"measuring...")

    /**
     * Added a delay but there may be other solutions thinking of job scheduler or service
     */
    private fun startGetIssPositions(intervalSeconds:Int = 5 ) { //todo use constant
        viewModelScope.launch {
            while (true){
                getIssLocations()
                delay(intervalSeconds * 1000L)
            }
        }
    }
    // get astros names
    /**
     * this function is run only once since the values do not change
     */
    fun getAstrosNames(){
        astrosRepo.getAstros().onStart {
            _astrosModelsState.value = UiState.Loading
        }.onEach {
            if (it.isSuccess &&  it.getOrNull()!=null){
                _astrosModelsState.value = UiState.Success(it)
            }else{
                // Handle error to display
                _astrosModelsState.value = UiState.Error(it.exceptionOrNull()?.message?:"Error happened")
            }
        }.launchIn(viewModelScope)
    }
    // get satelites location every 5 seconds
     fun getIssLocations() {
        issRepo.getCurrentIssPosition().onEach {
            if (it.isSuccess) {
                _issModelsState.value = it.getOrNull()?.let { iss -> UiState.Success(iss) }
            } else {
                _issModelsState.value = UiState.Error(
                    it.exceptionOrNull()?.message ?: "Error finding new ISS coordinates"
                )
            }
        }.launchIn(viewModelScope)
    }

    // find user location
    @OptIn(ExperimentalCoroutinesApi::class)
    fun startLocationTracker() {
        locationTrackerUseCase.execute().onEach { location ->
            _locationModelState.emit(UiState.Success(location))
        }.launchIn(viewModelScope)
    }

}