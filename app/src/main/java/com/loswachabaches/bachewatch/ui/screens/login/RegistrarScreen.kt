package com.loswachabaches.bachewatch.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loswachabaches.bachewatch.R

data class RegisterUserData(
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val ciudad: String,
    val telefono: String,
    val correo: String,
    val password: String
)

@Composable
fun RegisterScreen(
    onRegisterClick: (RegisterUserData) -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val negro = Color(0xFF0B0B0B)
    val amarillo = Color(0xFFFFDA25)
    val blanco = Color(0xFFFFFFFF)
    val gris = Color(0xFF4B4A4A)
    val rojo = Color(0xFFFF4D4D)

    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }

    var verPassword by remember { mutableStateOf(false) }
    var verRepetirPassword by remember { mutableStateOf(false) }

    val nombreTieneError = nombre.isNotBlank() && nombre.trim().length < 2
    val apellidoPaternoTieneError =
        apellidoPaterno.isNotBlank() && apellidoPaterno.trim().length < 2
    val ciudadTieneError = ciudad.isNotBlank() && ciudad.trim().length < 2

    val telefonoTieneError =
        telefono.isNotBlank() && telefono.length != 10

    val correoValido = Patterns.EMAIL_ADDRESS
        .matcher(correo.trim())
        .matches()

    val correoTieneError = correo.isNotBlank() && !correoValido

    val passwordTieneError =
        password.isNotBlank() && password.length < 6

    val repetirPasswordTieneError =
        repetirPassword.isNotBlank() && repetirPassword != password

    val puedeRegistrarse =
        nombre.trim().length >= 2 &&
                apellidoPaterno.trim().length >= 2 &&
                ciudad.trim().length >= 2 &&
                telefono.length == 10 &&
                correoValido &&
                password.length >= 6 &&
                repetirPassword == password

    val backgroundGrad = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to negro,
            0.78f to gris,
            1.0f to amarillo
        )
    )

    Scaffold(
        containerColor = negro
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGrad)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bachewatch_logo),
                    contentDescription = "Logo BacheWatch",
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_largo),
                    contentDescription = "Nombre BacheWatch",
                    modifier = Modifier.size(width = 145.dp, height = 72.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Crear cuenta",
                color = blanco,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Nombre
            RegisterTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre",
                isError = nombreTieneError,
                errorText = "Ingresa mínimo 2 caracteres",
                blanco = blanco,
                amarillo = amarillo,
                rojo = rojo
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Apellidos en par
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RegisterTextField(
                    modifier = Modifier.weight(1f),
                    value = apellidoPaterno,
                    onValueChange = { apellidoPaterno = it },
                    label = "Ap. paterno",
                    isError = apellidoPaternoTieneError,
                    errorText = "Mínimo 2 caracteres",
                    blanco = blanco,
                    amarillo = amarillo,
                    rojo = rojo
                )
                RegisterTextField(
                    modifier = Modifier.weight(1f),
                    value = apellidoMaterno,
                    onValueChange = { apellidoMaterno = it },
                    label = "Ap. materno",
                    isError = false,
                    errorText = "",
                    blanco = blanco,
                    amarillo = amarillo,
                    rojo = rojo
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ciudad y teléfono en par
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RegisterTextField(
                    modifier = Modifier.weight(1f),
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = "Ciudad",
                    isError = ciudadTieneError,
                    errorText = "Ciudad inválida",
                    blanco = blanco,
                    amarillo = amarillo,
                    rojo = rojo
                )
                RegisterTextField(
                    modifier = Modifier.weight(1f),
                    value = telefono,
                    onValueChange = { newValue ->
                        telefono = newValue
                            .filter { it.isDigit() }
                            .take(10)
                    },
                    label = "Teléfono",
                    isError = telefonoTieneError,
                    errorText = "10 dígitos",
                    keyboardType = KeyboardType.Phone,
                    blanco = blanco,
                    amarillo = amarillo,
                    rojo = rojo
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Correo
            RegisterTextField(
                value = correo,
                onValueChange = { correo = it },
                label = "Correo electrónico",
                isError = correoTieneError,
                errorText = "Correo inválido, ej: usuario@email.com",
                keyboardType = KeyboardType.Email,
                blanco = blanco,
                amarillo = amarillo,
                rojo = rojo
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Contraseña
            RegisterTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isError = passwordTieneError,
                errorText = "Mínimo 6 caracteres",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { verPassword = !verPassword }) {
                        Text(
                            text = if (verPassword) "Ocultar" else "Ver",
                            color = amarillo,
                            fontSize = 12.sp
                        )
                    }
                },
                blanco = blanco,
                amarillo = amarillo,
                rojo = rojo
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Repetir contraseña
            RegisterTextField(
                value = repetirPassword,
                onValueChange = { repetirPassword = it },
                label = "Repetir contraseña",
                isError = repetirPasswordTieneError,
                errorText = "Las contraseñas no coinciden",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (verRepetirPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { verRepetirPassword = !verRepetirPassword }) {
                        Text(
                            text = if (verRepetirPassword) "Ocultar" else "Ver",
                            color = amarillo,
                            fontSize = 12.sp
                        )
                    }
                },
                blanco = blanco,
                amarillo = amarillo,
                rojo = rojo
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onRegisterClick(
                        RegisterUserData(
                            nombre = nombre.trim(),
                            apellidoPaterno = apellidoPaterno.trim(),
                            apellidoMaterno = apellidoMaterno.trim(),
                            ciudad = ciudad.trim(),
                            telefono = telefono.trim(),
                            correo = correo.trim(),
                            password = password
                        )
                    )

                    onLoginClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = true,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = amarillo,
                    contentColor = negro,
                    disabledContainerColor = gris,
                    disabledContentColor = blanco.copy(alpha = 0.60f)
                )
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onLoginClick
            ) {
                Text(
                    text = "¿Ya tienes cuenta? Iniciar sesión",
                    color = amarillo,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
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
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(if (isError) 78.dp else 62.dp),
        label = {
            Text(label)
        },
        singleLine = true,
        isError = isError,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        supportingText = {
            if (isError) {
                Text(
                    text = errorText,
                    color = rojo,
                    fontSize = 11.sp
                )
            }
        },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = blanco,
            unfocusedTextColor = blanco,
            focusedBorderColor = amarillo,
            unfocusedBorderColor = blanco.copy(alpha = 0.45f),
            focusedLabelColor = amarillo,
            unfocusedLabelColor = blanco.copy(alpha = 0.70f),
            cursorColor = amarillo,
            errorTextColor = blanco,
            errorBorderColor = rojo,
            errorLabelColor = rojo,
            errorCursorColor = rojo
        )
    )
}