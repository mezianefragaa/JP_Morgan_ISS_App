package com.example.jpmorganissapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.jpmorganissapp.R
import com.example.jpmorganissapp.data.source.remote.dto.AstrosDto
import com.example.jpmorganissapp.databinding.FragmentMapsBinding
import com.example.jpmorganissapp.domain.model.AstrosModel
import com.example.jpmorganissapp.domain.model.IssModel
import com.example.jpmorganissapp.domain.model.UiState
import com.example.jpmorganissapp.util.getMapMarkerCallback
import com.example.jpmorganissapp.util.locationPermissionsGranted
import com.example.jpmorganissapp.util.toLatLng
import com.example.jpmorganissapp.util.toLatlng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel by viewModels<MapViewModel>()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // locationPermissionsGranted is an extension function
        if (locationPermissionsGranted(permissions)) {
            mapViewModel.startLocationTracker()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        return binding.root
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Astros Names is set to run offline
        mapViewModel.astrosModelsState.onSubscription {
            mapViewModel.getAstrosNames()
        }.onEach {
            when (it) {
                is UiState.Error -> {
                    showError(message = it.failure)
                }

                UiState.Loading -> {
                    showLoading()
                }

                is UiState.Success -> {
                    showAstros((it.data as? List<AstrosDto>))
                }

                null -> {
                    showError()
                }
            }
        }.launchIn(lifecycleScope)

        /**
         * displaying a distance using data binding
         * note that the distance is not accurate since we are missing the elevation in the response
         */
        mapViewModel.distanceState.onEach {
            binding.distance = "distance: ".plus(it)
        }.launchIn(lifecycleScope)

        mapViewModel.issModelsState.onEach{ isslocation ->
            when (isslocation) {
                is UiState.Loading -> Unit
                is UiState.Success -> Unit
                is UiState.Error -> {
                     Toast.makeText(
                        requireContext(),
                        "ISS Coordinates not found",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                else -> Unit
            }
        }.launchIn(lifecycleScope)

        mapViewModel.locationModelState.onEach { myLocation ->
            when(myLocation){
                is UiState.Loading -> Unit
                is UiState.Success -> Unit
                is UiState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "My Location not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }.launchIn(lifecycleScope)

        /**
         * check if satellite coordinates if available then Onclick display Satellite marker
         * check if astros names are available then click on the Satellite displayed in the map to show the list of astros
         *  note : addmarker() is another extension function that handles markers and onclick Listners
         */
        binding.btnLocateSatellite.setOnClickListener {
            val issLatLong: LatLng? = (mapViewModel.issModelsState.value?.getDataOrNull<IssModel>())?.toLatlng()
            val astros: List<AstrosModel>? = (mapViewModel.astrosModelsState.value?.getDataOrNull<List<AstrosModel>>())
            if (issLatLong!=null){
                val dialogMessage = astros?.joinToString(separator = ",\n") { it.name }
                val callback = getMapMarkerCallback(
                    location = issLatLong,
                    context = requireContext(),
                    satellite = R.drawable.satellite,
                    dialogMessage = dialogMessage,
                    btnOk = R.string.done
                )
                (this@MapsFragment.childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(callback)
            }else if (mapViewModel.issModelsState.value?.isSuccess() != true){
                //todo using a snack bar with an action button for try again
                     Toast.makeText(
                        requireContext(),
                        "ISS Coordinates not available, working on it ....", //todo use string resource value
                        Toast.LENGTH_SHORT
                    ).show()

                mapViewModel.getIssLocations()
            }
        }
        /**
         * Locate me is the green button to show the my current location
         * we can add a polyline to display  the distance between the iss and the current location
         *
         */
        binding.btnLocateMe.setOnClickListener {
            val issLatLong: LatLng?= (mapViewModel.locationModelState.value?.getDataOrNull<Location>())?.toLatLng()
            val astros: List<AstrosModel>? = (mapViewModel.astrosModelsState.value?.getDataOrNull<List<AstrosModel>>())
            if (issLatLong!=null){
                val dialogMessage = astros?.joinToString(separator = ",\n") { it.name }
                val callback = getMapMarkerCallback(
                    location = issLatLong,
                    context = requireContext(),
                    satellite = R.drawable.drawable_location,
                    dialogMessage = dialogMessage,
                    btnOk = R.string.done
                )
                (this@MapsFragment.childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(callback)
            }else if (mapViewModel.issModelsState.value?.isSuccess() != true){
                //todo using a snack bar with an action button for try again
                Toast.makeText(
                    requireContext(),
                    "your location not available, working on it ....", //todo use string resource value
                    Toast.LENGTH_SHORT
                ).show()

                mapViewModel.startLocationTracker()
            }
        }


    }

    private fun showAstros(astros: List<AstrosDto>?) {
        //todo
    }

    private fun showLoading() {
        //todo
    }

    private fun showError(message: String? = null) {
        //todo
    }
    private fun showHistory(){
        /**
         *
         * Create new Fragment named History
         * Add button to the UI show History
         * Implement HistoryFragment in Nav_Graph
         * Add Direction and a Bundle with Data Class Model
         * receive the argument inside History Fragment
         * Create recycler view to display data
         *
         *
         * **/
    }


}