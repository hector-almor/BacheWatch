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

data class RegisterUserData(
    val nombre: String,
    val correo: String,
    val password: String
)

@Composable
fun RegisterScreen(
    uiState: AuthUiState = AuthUiState.Idle,
    onRegisterClick: (RegisterUserData) -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val negro    = Color(0xFF0B0B0B)
    val amarillo = Color(0xFFFFDA25)
    val blanco   = Color(0xFFFFFFFF)
    val gris     = Color(0xFF4B4A4A)
    val rojo     = Color(0xFFFF4D4D)
    val blancoSutil = blanco.copy(alpha = 0.08f)

    var nombre            by remember { mutableStateOf("") }
    var correo            by remember { mutableStateOf("") }
    var password          by remember { mutableStateOf("") }
    var repetirPassword   by remember { mutableStateOf("") }
    var verPassword       by remember { mutableStateOf(false) }
    var verRepetirPassword by remember { mutableStateOf(false) }

    val nombreTieneError          = nombre.isNotBlank() && nombre.trim().length < 2
    val correoValido              = Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    val correoTieneError          = correo.isNotBlank() && !correoValido
    val passwordTieneError        = password.isNotBlank() && password.length < 6
    val repetirPasswordTieneError = repetirPassword.isNotBlank() && repetirPassword != password

    val puedeRegistrarse =
        nombre.trim().length >= 2 &&
                correoValido &&
                password.length >= 6 &&
                repetirPassword == password

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
            Spacer(modifier = Modifier.height(52.dp))

            // Logo
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier              = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter            = painterResource(id = R.drawable.logo_largo),
                    contentDescription = "BacheWatch",
                    modifier           = Modifier.size(width = 140.dp, height = 70.dp),
                    contentScale       = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Card formulario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(blancoSutil)
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text       = "Crear cuenta",
                        color      = blanco,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text      = "Completa los datos",
                        color     = blanco.copy(alpha = 0.5f),
                        fontSize  = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Nombre
                    RegisterTextField(
                        value         = nombre,
                        onValueChange = { nombre = it },
                        label         = "Nombre",
                        isError       = nombreTieneError,
                        errorText     = "Mínimo 2 caracteres",
                        blanco        = blanco,
                        amarillo      = amarillo,
                        rojo          = rojo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Correo
                    RegisterTextField(
                        value         = correo,
                        onValueChange = { correo = it },
                        label         = "Correo electrónico",
                        isError       = correoTieneError,
                        errorText     = "Correo no valido, ej: usuario@email.com",
                        keyboardType  = KeyboardType.Email,
                        blanco        = blanco,
                        amarillo      = amarillo,
                        rojo          = rojo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contraseña
                    RegisterTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = "Contraseña",
                        isError              = passwordTieneError,
                        errorText            = "Mínimo 6 caracteres",
                        keyboardType         = KeyboardType.Password,
                        visualTransformation = if (verPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon         = {
                            IconButton(onClick = { verPassword = !verPassword }) {
                                Icon(
                                    imageVector        = if (verPassword) Icons.Outlined.VisibilityOff
                                    else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint               = amarillo
                                )
                            }
                        },
                        blanco   = blanco,
                        amarillo = amarillo,
                        rojo     = rojo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Repetir contraseña
                    RegisterTextField(
                        value                = repetirPassword,
                        onValueChange        = { repetirPassword = it },
                        label                = "Repetir contraseña",
                        isError              = repetirPasswordTieneError,
                        errorText            = "Las contraseñas no coinciden",
                        keyboardType         = KeyboardType.Password,
                        visualTransformation = if (verRepetirPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon         = {
                            IconButton(onClick = { verRepetirPassword = !verRepetirPassword }) {
                                Icon(
                                    imageVector        = if (verRepetirPassword) Icons.Outlined.VisibilityOff
                                    else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint               = amarillo
                                )
                            }
                        },
                        blanco   = blanco,
                        amarillo = amarillo,
                        rojo     = rojo
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón registrar
                    Button(
                        onClick  = {
                            onRegisterClick(
                                RegisterUserData(
                                    nombre   = nombre.trim(),
                                    correo   = correo.trim(),
                                    password = password
                                )
                            )
                        },
                        enabled  = puedeRegistrarse && uiState !is AuthUiState.Cargando,
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
                            Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

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

            // Link a login
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text     = "Ya tienes cuenta?, ",
                    color    = blanco.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick        = onLoginClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text       = "Inicia sesión",
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
private fun RegisterTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    blanco: Color,
    amarillo: Color,
    rojo: Color
) {
    OutlinedTextField(
        value               = value,
        onValueChange       = onValueChange,
        modifier            = modifier.fillMaxWidth(),
        label               = { Text(label) },
        singleLine          = true,
        isError             = isError,
        visualTransformation = visualTransformation,
        trailingIcon        = trailingIcon,
        keyboardOptions     = KeyboardOptions(keyboardType = keyboardType),
        supportingText      = {
            if (isError) Text(errorText, color = rojo, fontSize = 11.sp)
        },
        shape  = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor        = blanco,
            unfocusedTextColor      = blanco,
            focusedBorderColor      = amarillo,
            unfocusedBorderColor    = blanco.copy(alpha = 0.3f),
            focusedLabelColor       = amarillo,
            unfocusedLabelColor     = blanco.copy(alpha = 0.6f),
            cursorColor             = amarillo,
            errorTextColor          = blanco,
            errorBorderColor        = rojo,
            errorLabelColor         = rojo,
            errorCursorColor        = rojo,
            focusedContainerColor   = blanco.copy(alpha = 0.05f),
            unfocusedContainerColor = blanco.copy(alpha = 0.05f)
        )
    )
}