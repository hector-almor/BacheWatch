package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loswachabaches.bachewatch.ui.screens.mainscreen.AccentColor
import com.loswachabaches.bachewatch.ui.screens.mainscreen.BackgroundLight
import com.loswachabaches.bachewatch.ui.screens.mainscreen.PrimaryColor
import com.loswachabaches.bachewatch.ui.screens.mainscreen.TextMutedColor
import com.loswachabaches.bachewatch.ui.viewmodels.EstadisticasViewModel
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel

@Composable
fun EstadisticasTab(
    reporteViewModel: ReporteViewModel = viewModel(),
    estadisticasViewModel: EstadisticasViewModel = viewModel()
) {
    val context          = LocalContext.current
    val reportesEnMiZona by reporteViewModel.reportesEnMiZona.collectAsState()
    val zonasTop by reporteViewModel.zonasTop.collectAsState()

    LaunchedEffect(Unit) {
        reporteViewModel.obtenerZonaConMasBaches()
        estadisticasViewModel.obtenerUbicacion(context) { lat, lon ->
            reporteViewModel.obtenerReportesCercanos(lat, lon)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Estadísticas", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = PrimaryColor)

        // Baches en mi zona
        SimpleCard(
            icon    = Icons.Outlined.LocationOn,
            titulo  = "Baches en mi zona",
            subtitulo = "Radio de 2 km"
        ) {
            Text(
                text       = "${reportesEnMiZona.size}",
                fontSize   = 40.sp,
                fontWeight = FontWeight.Bold,
                color      = PrimaryColor
            )
            Text(
                text     = if (reportesEnMiZona.isEmpty()) "Sin reportes cercanos"
                else "reporte${if (reportesEnMiZona.size != 1) "s" else ""} registrado${if (reportesEnMiZona.size != 1) "s" else ""}",
                fontSize = 13.sp,
                color    = TextMutedColor
            )

            if (reportesEnMiZona.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(8.dp))
                reportesEnMiZona.take(3).forEach { reporte ->
                    Text(
                        text     = "• ${reporte.direccionAproximada.ifBlank { "Dirección no disponible" }}",
                        fontSize = 12.sp,
                        color    = TextMutedColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }

        // Zona con más baches
        SimpleCard(
            icon      = Icons.Outlined.BarChart,
            titulo    = "Zonas con más baches",
            subtitulo = "CDMX"
        ) {
            if (zonasTop.isEmpty()) {
                Text("Sin datos aún", fontSize = 13.sp, color = TextMutedColor)
            } else {
                zonasTop.forEachIndexed { index, (zona, cantidad) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Número de ranking
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (index) {
                                        0 -> AccentColor
                                        1 -> Color(0xFFE5E7EB)
                                        2 -> Color(0xFFFED7AA)
                                        else -> Color(0xFFF3F4F6)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = "#${index + 1}",
                                fontSize   = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color      = PrimaryColor
                            )
                        }
                        Text(
                            text     = zona,
                            fontSize = 13.sp,
                            color    = PrimaryColor,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text       = "$cantidad",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color      = PrimaryColor
                        )
                    }
                    if (index < zonasTop.lastIndex) {
                        HorizontalDivider(
                            color    = Color(0xFFE5E7EB),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Botón llamada
        Button(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:*3111") }
                )
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape    = RoundedCornerShape(14.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor   = Color.White
            )
        ) {
            Icon(Icons.Outlined.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reportar por teléfono  •  *3111", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SimpleCard(
    icon: ImageVector,
    titulo: String,
    subtitulo: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(16.dp),
        color           = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement  = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = AccentColor, modifier = Modifier.size(20.dp))
                Column {
                    Text(titulo,    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PrimaryColor)
                    Text(subtitulo, fontSize = 11.sp, color = TextMutedColor)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}