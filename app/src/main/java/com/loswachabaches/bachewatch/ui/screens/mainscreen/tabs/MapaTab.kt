package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.loswachabaches.bachewatch.ui.screens.mainscreen.PrimaryColor
import com.loswachabaches.bachewatch.ui.screens.mainscreen.SurfaceColor
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

// Ubicación inicial: CDMX
private val CDMX = GeoPoint(19.4326, -99.1332)

@SuppressLint("MissingPermission")
@Composable
fun MapaTab(
    reporteViewModel: ReporteViewModel,
    onReporteClick: (String) -> Unit = {}
) {
    val context = LocalContext.current

    val reportes by reporteViewModel.reportes.collectAsState()
    LaunchedEffect(Unit) {
        reporteViewModel.obtenerTodosLosReportes()
    }

    var mapView by remember { mutableStateOf<MapView?>(null) }
    var locationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            locationOverlay?.enableMyLocation()
            locationOverlay?.enableFollowLocation()

            locationOverlay?.runOnFirstFix {
                mapView?.post {
                    val point = locationOverlay?.myLocation

                    if (point != null) {
                        mapView?.controller?.animateTo(point)
                        mapView?.controller?.setZoom(18.0)
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            locationOverlay?.disableMyLocation()
            mapView?.onPause()
            mapView?.onDetach()
        }
    }

    LaunchedEffect(reportes) {
        val map = mapView ?: return@LaunchedEffect

        // Limpiar marcadores anteriores conservando el overlay de ubicación
        map.overlays.removeIf { it is Marker }

        // Agregar un marcador por cada reporte
        reportes.forEach { reporte ->
            val marker = Marker(map).apply {
                position = GeoPoint(reporte.latitud, reporte.longitud)
                title = reporte.descripcion
                snippet = reporte.direccionAproximada.ifEmpty { "Sin dirección" }
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                setOnMarkerClickListener { _, _ ->
                    onReporteClick(reporte.id)
                    true
                }
            }
            map.overlays.add(marker)
        }

        map.invalidate() // refresca el mapa
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->

                Configuration.getInstance().load(
                    ctx,
                    ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                )

                Configuration.getInstance().userAgentValue =
                    "BacheWatch/${ctx.packageName}"

                val newMapView = MapView(ctx)

                newMapView.setTileSource(TileSourceFactory.MAPNIK)
                newMapView.setMultiTouchControls(true)

                newMapView.zoomController.setVisibility(
                    CustomZoomButtonsController.Visibility.NEVER
                )

                newMapView.controller.setZoom(18.0)
                newMapView.controller.setCenter(CDMX)

                val overlay = MyLocationNewOverlay(
                    GpsMyLocationProvider(ctx),
                    newMapView
                )

                newMapView.overlays.add(overlay)

                locationOverlay = overlay
                mapView = newMapView

                newMapView.onResume()

                newMapView
            },
            update = {
                if (hasLocationPermission) {
                    locationOverlay?.enableMyLocation()
                    locationOverlay?.enableFollowLocation()
                }
            }
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            shape = CircleShape,
            color = SurfaceColor,
            shadowElevation = 4.dp
        ) {
            IconButton(
                onClick = {
                    if (hasLocationPermission) {
                        val point = locationOverlay?.myLocation

                        if (point != null) {
                            mapView?.controller?.animateTo(point)
                            mapView?.controller?.setZoom(18.0)
                        }
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.MyLocation,
                    contentDescription = "Mi ubicación",
                    tint = PrimaryColor
                )
            }
        }
    }
}