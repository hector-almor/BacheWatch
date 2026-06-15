package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel

private val PrimaryColor = Color(0xFF1A1A2E)
private val AccentColor = Color(0xFFFFDA25)
private val TextMutedColor = Color(0xFF9CA3AF)

@Composable
fun MiCuentaTab(
    authViewModel: AuthViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val usuario by authViewModel.usuario.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.obtenerUsuarioActual()?.uid?.let {
            authViewModel.obtenerDatosUsuario(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Mi cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Text(
            text = "Tu información: ",
            color = TextMutedColor
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = usuario?.nombre ?:"Cargando...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Correo: ${usuario?.correo ?: ""}",
                    color = PrimaryColor
                )

                Text(
                    text = "Reportes realizados: 0",
                    color = PrimaryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = onLogoutClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentColor,
                contentColor = PrimaryColor
            )
        ) {
            Text(
                text = "Cerrar sesión",
                fontWeight = FontWeight.Bold
            )
        }
    }
}