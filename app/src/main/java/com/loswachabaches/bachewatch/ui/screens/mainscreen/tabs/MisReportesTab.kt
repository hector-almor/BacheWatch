package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loswachabaches.bachewatch.data.model.Reporte
import com.loswachabaches.bachewatch.ui.screens.mainscreen.AccentColor
import com.loswachabaches.bachewatch.ui.screens.mainscreen.BackgroundLight
import com.loswachabaches.bachewatch.ui.screens.mainscreen.PrimaryColor
import com.loswachabaches.bachewatch.ui.screens.mainscreen.TextMutedColor
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val SurfaceColor = Color.White

@Composable
fun MisReportesTab(
    authViewModel: AuthViewModel,
    reporteViewModel: ReporteViewModel,
    onVerDetalleClick: (String) -> Unit = {},
    onEditarClick: (String) -> Unit = {}
) {
    val misReportes by reporteViewModel.misReportes.collectAsState()
    val uid = remember { authViewModel.obtenerUsuarioActual()?.uid }

    LaunchedEffect(uid) {
        uid?.let {
            reporteViewModel.obtenerMisReportes(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Mis reportes",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${misReportes.size} reporte${if (misReportes.size != 1) "s" else ""} enviado${if (misReportes.size != 1) "s" else ""}",
                fontSize = 13.sp,
                color = TextMutedColor
            )
        }

        if (misReportes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aún no tienes reportes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Toca + para reportar un bache",
                        fontSize = 13.sp,
                        color = TextMutedColor
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(misReportes, key = { it.id }) { reporte ->
                    ReporteCard(
                        reporte = reporte,
                        onVerClick = { onVerDetalleClick(reporte.id) },
                        onEditarClick = { onEditarClick(reporte.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ReporteCard(
    reporte: Reporte,
    onVerClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val fecha = remember(reporte.timestamp) {
        SimpleDateFormat("dd MMM yyyy  •  HH:mm", Locale("es", "MX"))
            .format(reporte.timestamp)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceColor,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, Color(0xFFE8E8E8))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            if (reporte.fotoUrl.isNotBlank()) {
                AsyncImage(
                    model = reporte.fotoUrl,
                    contentDescription = "Foto del bache",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = reporte.descripcion.ifBlank { "Sin descripción" },
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = reporte.direccionAproximada.ifBlank { "Sin dirección" },
                fontSize = 12.sp,
                color = TextMutedColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = fecha,
                fontSize = 11.sp,
                color = TextMutedColor.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(color = Color(0xFFEDEDED))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onVerClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrimaryColor),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Ver",
                        fontSize = 12.sp,
                        color = PrimaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onEditarClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor,
                        contentColor = PrimaryColor
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Editar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}