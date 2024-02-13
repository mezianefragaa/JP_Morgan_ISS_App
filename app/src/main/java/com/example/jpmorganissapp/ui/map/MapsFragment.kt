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
import com.example.jpmorganissapp.domain.model.IssModel
import com.example.jpmorganissapp.domain.model.UiState
import com.example.jpmorganissapp.util.addMarker
import com.example.jpmorganissapp.util.locationPermissionsGranted
import com.example.jpmorganissapp.util.toLatLng
import com.example.jpmorganissapp.util.toLatlng
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

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

        /**
         * check if satellite coordinates if available then Onclick display Satellite marker
         * check if astros names are available then click on the Satellite displayed in the map to show the list of astros
         *  note : addmarker() is another extension function that handles markers and onclick Listners
         */
        binding.btnLocateSatellite.setOnClickListener {
            lifecycleScope.launch {

                mapViewModel.issModelsState.firstOrNull().let { isslocation ->
                    when (isslocation) {
                        is UiState.Success ->
                            mapViewModel.astrosModelsState.firstOrNull().let { astros ->
                                when (astros) {
                                    is UiState.Success -> addMarker(
                                        (isslocation.data as IssModel).toLatlng(),
                                        requireContext(),
                                        this@MapsFragment.childFragmentManager,
                                        R.drawable.satellite,
                                        astros.data,
                                        R.string.done
                                    )

                                    else -> {

                                        addMarker(
                                            (isslocation.data as IssModel).toLatlng(),
                                            requireContext(),
                                            this@MapsFragment.childFragmentManager,
                                            R.drawable.satellite,
                                            null,
                                            0
                                        )
                                    }
                                }
                            }

                        else -> {
                            val toast = Toast.makeText(
                                requireContext(),
                                "ISS Coordinates not found",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }

                    }
                }

            }

        }
        /**
         * Locate me is the green button to show the my current location
         * we can add a polyline to display  the distance between the iss and the current location
         *
         */
        binding.btnLocateMe.setOnClickListener {
            lifecycleScope.launch{
                mapViewModel.locationModelState.firstOrNull().let { myLocation ->
                    when(myLocation){
                        is UiState.Success -> addMarker((myLocation.data as Location).toLatLng(),requireContext(),this@MapsFragment.childFragmentManager,R.drawable.drawable_location,null,0 )
                        else -> {
                            val toast = Toast.makeText(
                                requireContext(),
                                "My Location not found",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }


                }
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