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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loswachabaches.bachewatch.R

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {}
) {
    // Colores
    val negro = Color(0xFF0B0B0B)
    val amarillo = Color(0xFFFFDA25)
    val blanco = Color(0xFFFFFFFF)
    val gris = Color(0xFF4B4A4A)
    val rojo = Color(0xFFFF4D4D)

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repetirPassword by remember { mutableStateOf("") }

    val correoValido = Patterns.EMAIL_ADDRESS
        .matcher(correo)
        .matches()

    val correoTieneError = correo.isNotBlank() && !correoValido

    val passwordTieneError = password.isNotBlank() && password.length < 6

    val repetirPasswordTieneError =
        repetirPassword.isNotBlank() && repetirPassword != password

    val puedeRegistrarse =
        correoValido &&
                password.length >= 6 &&
                repetirPassword == password

    val backgroundGrad = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to negro,
            0.80f to gris,
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bachewatch_logo),
                    contentDescription = "Logo BacheWatch",
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_largo),
                    contentDescription = "Nombre BacheWatch",
                    modifier = Modifier.size(width = 160.dp, height = 90.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Crear cuenta",
                color = blanco,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Correo electrónico")
                },
                singleLine = true,
                isError = correoTieneError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                supportingText = {
                    if (correoTieneError) {
                        Text(
                            text = "Ingresa un correo válido",
                            color = rojo
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

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Nueva contraseña")
                },
                singleLine = true,
                isError = passwordTieneError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                supportingText = {
                    if (passwordTieneError) {
                        Text(
                            text = "La contraseña debe tener mínimo 6 caracteres",
                            color = rojo
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

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = repetirPassword,
                onValueChange = { repetirPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Repetir contraseña")
                },
                singleLine = true,
                isError = repetirPasswordTieneError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                supportingText = {
                    if (repetirPasswordTieneError) {
                        Text(
                            text = "Las contraseñas no coinciden",
                            color = rojo
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = puedeRegistrarse,
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

            Spacer(modifier = Modifier.height(12.dp))

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
        }
    }
}