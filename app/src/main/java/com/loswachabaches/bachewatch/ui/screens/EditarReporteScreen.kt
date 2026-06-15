package com.loswachabaches.bachewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loswachabaches.bachewatch.data.model.Reporte

private val Primary = Color(0xFF1A1A2E)
private val Accent = Color(0xFFFFDA25)
private val TextMuted = Color(0xFF9CA3AF)
private val BgLight = Color(0xFFF5F4F0)
private val SurfaceColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarReporteScreen(
    reporte: Reporte,
    onBackClick: () -> Unit = {},
    onGuardarClick: (descripcion: String) -> Unit = {},
    onEliminarClick: () -> Unit = {}
) {
    var descripcion by remember { mutableStateOf(reporte.descripcion) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = {
                Text(
                    text = "Eliminar reporte",
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            },
            text = {
                Text(
                    text = "¿Seguro que quieres eliminar este reporte?",
                    color = TextMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onEliminarClick()
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text(
                        text = "Cancelar",
                        color = Primary
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = BgLight,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        Text(
                            text = "Bache",
                            color = Accent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Watch",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Primary
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = SurfaceColor,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = Primary
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { onGuardarClick(descripcion) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = descripcion.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = Primary
                        )
                    ) {
                        Text(
                            text = "Guardar",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Editar reporte",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            if (reporte.fotoUrl.isNotBlank()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 3.dp
                ) {
                    AsyncImage(
                        model = reporte.fotoUrl,
                        contentDescription = "Foto del bache",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceColor,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "Dirección",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = reporte.direccionAproximada.ifBlank { "Sin dirección" },
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = "Descripción",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                shape = RoundedCornerShape(14.dp),
                placeholder = {
                    Text(
                        text = "Describe el bache…",
                        color = TextMuted
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedTextColor = Primary,
                    unfocusedTextColor = Primary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(
                onClick = { mostrarDialogo = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFDC2626)
                )
            ) {
                Text(
                    text = "Eliminar reporte",
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}