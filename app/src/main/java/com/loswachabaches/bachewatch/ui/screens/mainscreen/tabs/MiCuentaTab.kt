package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel

private val Primary   = Color(0xFF1A1A2E)
private val Accent    = Color(0xFFFFDA25)
private val TextMuted = Color(0xFF9CA3AF)
private val BgLight   = Color(0xFFF5F4F0)

@Composable
fun MiCuentaTab(
    authViewModel: AuthViewModel,
    reporteViewModel: ReporteViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val usuario       by authViewModel.usuario.collectAsState()
    val totalReportes by reporteViewModel.totalReportes.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.obtenerUsuarioActual()?.uid?.let { uid ->
            authViewModel.obtenerDatosUsuario(uid)
            reporteViewModel.contarMisReportes(uid)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Mi cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Primary)

        // Avatar + info
        Surface(
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(16.dp),
            color           = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Iniciales
                val iniciales = usuario?.nombre
                    ?.split(" ")
                    ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    ?.take(2)
                    ?.joinToString("") ?: "?"

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Accent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(iniciales, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Primary)
                }

                Column {
                    Text(
                        text       = usuario?.nombre ?: "Cargando...",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Primary
                    )
                    Text(
                        text     = usuario?.correo ?: "",
                        fontSize = 13.sp,
                        color    = TextMuted
                    )
                }
            }
        }

        // Reportes enviados
        Surface(
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(16.dp),
            color           = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = null,
                    tint     = Accent,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text       = "$totalReportes reporte${if (totalReportes != 1) "s" else ""} enviado${if (totalReportes != 1) "s" else ""}",
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color      = Primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Cerrar sesión
        Button(
            onClick   = onLogoutClick,
            modifier  = Modifier.fillMaxWidth().height(50.dp),
            shape     = RoundedCornerShape(14.dp),
            colors    = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFE0E0),
                contentColor   = Color(0xFFDC2626)
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
        }
    }
}