package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val context = LocalContext.current
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
            .padding(horizontal = 18.dp)
            .padding(top = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "Estadísticas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        // ── Baches en mi zona ─────────────────────────────────────────────
        Tarjeta(
            titulo = "En mi zona",
            subtitulo = "Radio de 2 km"
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${reportesEnMiZona.size}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    lineHeight = 50.sp
                )

                Text(
                    text = if (reportesEnMiZona.size == 1) "Bache" else "Baches",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    lineHeight = 50.sp
                )
            }

            if (reportesEnMiZona.isEmpty()) {
                Text(
                    text = "No hay reportes cerca de ti",
                    fontSize = 13.sp,
                    color = TextMutedColor
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))

                reportesEnMiZona.take(3).forEach { reporte ->
                    Text(
                        text = reporte.direccionAproximada.ifBlank { "Dirección no disponible" },
                        fontSize = 12.sp,
                        color = TextMutedColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }

        // ── Zonas con más baches ──────────────────────────────────────────
        Tarjeta(
            titulo = "Zonas con más baches",
            subtitulo = "En la CDMX"
        ) {

            if (zonasTop.isEmpty()) {
                Text(
                    text = "Sin datos aún",
                    fontSize = 13.sp,
                    color = TextMutedColor
                )
            } else {
                zonasTop.forEachIndexed { index, (zona, cantidad) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "#${index + 1}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (index == 0) AccentColor else TextMutedColor,
                            modifier = Modifier.width(32.dp)
                        )

                        Text(
                            text = zona,
                            fontSize = 13.sp,
                            color = PrimaryColor,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(22.dp)
                                .background(Color(0xFFE0E0E0))
                        )

                        Text(
                            text = "$cantidad",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor,
                            modifier = Modifier.width(34.dp)
                        )
                    }

                    if (index < zonasTop.lastIndex) {
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
                }
            }
        }

        // ── Botón llamada ─────────────────────────────────────────────────
        Button(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:*0311")
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Reportar por teléfono  •  *0311",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun Tarjeta(
    titulo: String,
    subtitulo: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryColor
                )

                Text(
                    text = "·",
                    fontSize = 14.sp,
                    color = TextMutedColor
                )

                Text(
                    text = subtitulo,
                    fontSize = 12.sp,
                    color = TextMutedColor
                )
            }

            content()
        }
    }
}