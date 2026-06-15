package com.loswachabaches.bachewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.google.firebase.firestore.GeoPoint
import com.loswachabaches.bachewatch.data.model.Reporte
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.Locale

private val Primary = Color(0xFF1A1A2E)
private val Accent = Color(0xFFFFDA25)
private val TextMuted = Color(0xFF9CA3AF)
private val BgLight = Color(0xFFF5F4F0)
private val Surface = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleReporteScreen(
    reporte: Reporte,
    onBackClick: () -> Unit = {}
) {
    val fechaFormateada = remember(reporte.timestamp) {
        SimpleDateFormat("dd 'de' MMMM 'de' yyyy  •  HH:mm", Locale("es", "MX"))
            .format(reporte.timestamp)
    }

    Scaffold(
        containerColor = BgLight,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Bache", color = Accent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Watch", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Detalle del Reporte",
                color = Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            // ── Foto ──────────────────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(24.dp),
                color = Surface,
                shadowElevation = 5.dp
            ) {
                AsyncImage(
                    model = reporte.fotoUrl,
                    contentDescription = "Foto del bache",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                )
            }

            // ── Descripción ───────────────────────────────────────────────
            DetalleCard(
                icon = Icons.Outlined.EditNote,
                title = "Descripción",
                value = reporte.descripcion
            )

            // ── Dirección ─────────────────────────────────────────────────
            DetalleCard(
                icon = Icons.Outlined.LocationOn,
                title = "Dirección aproximada",
                value = reporte.direccionAproximada.ifEmpty { "No disponible" }
            )

            // ── Fecha ─────────────────────────────────────────────────────
            DetalleCard(
                icon = Icons.Outlined.CalendarMonth,
                title = "Fecha y hora",
                value = fechaFormateada
            )

            // ── Mini mapa ─────────────────────────────────────────────────
            Text(
                text = "Ubicación",
                color = Primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(22.dp),
                shadowElevation = 5.dp
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(22.dp)),
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(false)
                            zoomController.setVisibility(
                                CustomZoomButtonsController.Visibility.NEVER
                            )
                            controller.setZoom(17.0)
                            val punto = org.osmdroid.util.GeoPoint(reporte.latitud, reporte.longitud)
                            controller.setCenter(punto)
                            val marker = Marker(this).apply {
                                position = punto
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            overlays.add(marker)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DetalleCard(icon: ImageVector, title: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Surface,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Primary, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Primary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))
                Text(value, color = TextMuted, fontSize = 13.sp)
            }
        }
    }
}