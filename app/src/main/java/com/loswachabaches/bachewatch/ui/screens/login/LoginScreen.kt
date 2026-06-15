package com.loswachabaches.bachewatch.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loswachabaches.bachewatch.R
import com.loswachabaches.bachewatch.ui.viewmodels.AuthUiState

@Composable
fun LoginScreen(
    uiState: AuthUiState = AuthUiState.Idle,
    onLoginClick: (correo: String, password: String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {},
) {
    val negro    = Color(0xFF0B0B0B)
    val amarillo = Color(0xFFFFDA25)
    val blanco   = Color(0xFFFFFFFF)
    val gris     = Color(0xFF4B4A4A)
    val rojo     = Color(0xFFFF4D4D)
    val blancoSutil = blanco.copy(alpha = 0.08f)

    var usuario     by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }

    val correoValido     = Patterns.EMAIL_ADDRESS.matcher(usuario).matches()
    val usuarioTieneError = usuario.isNotBlank() && !correoValido

    val gradiente = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f  to negro,
            0.65f to Color(0xFF1a1a2e),
            1.0f  to Color(0xFF2a2200)
        )
    )

    Scaffold(containerColor = negro) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradiente)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter            = painterResource(id = R.drawable.logo_largo),
                    contentDescription = "BacheWatch",
                    modifier           = Modifier.size(width = 150.dp, height = 80.dp),
                    contentScale       = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Card de formulario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(blancoSutil)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text       = "Iniciar sesión",
                        color      = blanco,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text      = "Ingresa tus datos para continuar",
                        color     = blanco.copy(alpha = 0.5f),
                        fontSize  = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    // Correo
                    OutlinedTextField(
                        value         = usuario,
                        onValueChange = { usuario = it },
                        modifier      = Modifier.fillMaxWidth(),
                        label         = { Text("Correo electrónico") },
                        singleLine    = true,
                        isError       = usuarioTieneError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        supportingText = {
                            if (usuarioTieneError) {
                                Text("Ingresa un correo válido", color = rojo)
                            }
                        },
                        shape  = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(amarillo, blanco, rojo)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contraseña
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        modifier      = Modifier.fillMaxWidth(),
                        label         = { Text("Contraseña") },
                        singleLine    = true,
                        visualTransformation = if (verPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon  = {
                            IconButton(onClick = { verPassword = !verPassword }) {
                                Icon(
                                    imageVector        = if (verPassword) Icons.Outlined.VisibilityOff
                                    else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint               = amarillo
                                )
                            }
                        },
                        shape  = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(amarillo, blanco, rojo)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón login
                    Button(
                        onClick  = { onLoginClick(usuario, password) },
                        enabled  = correoValido && password.isNotBlank() && uiState !is AuthUiState.Cargando,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(16.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = amarillo,
                            contentColor           = negro,
                            disabledContainerColor = gris,
                            disabledContentColor   = blanco.copy(alpha = 0.5f)
                        )
                    ) {
                        if (uiState is AuthUiState.Cargando) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(20.dp),
                                color       = negro,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Error
                    if (uiState is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text      = uiState.mensaje,
                            color     = rojo,
                            fontSize  = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Registro
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text     = "No tienes una cuenta?, ",
                    color    = blanco.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick          = onRegisterClick,
                    contentPadding   = PaddingValues(0.dp)
                ) {
                    Text(
                        text       = "Regístrate",
                        color      = amarillo,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun outlinedFieldColors(
    amarillo: Color,
    blanco: Color,
    rojo: Color
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor       = blanco,
    unfocusedTextColor     = blanco,
    focusedBorderColor     = amarillo,
    unfocusedBorderColor   = blanco.copy(alpha = 0.3f),
    focusedLabelColor      = amarillo,
    unfocusedLabelColor    = blanco.copy(alpha = 0.6f),
    cursorColor            = amarillo,
    errorTextColor         = blanco,
    errorBorderColor       = rojo,
    errorLabelColor        = rojo,
    errorCursorColor       = rojo,
    focusedContainerColor  = blanco.copy(alpha = 0.05f),
    unfocusedContainerColor = blanco.copy(alpha = 0.05f)
)