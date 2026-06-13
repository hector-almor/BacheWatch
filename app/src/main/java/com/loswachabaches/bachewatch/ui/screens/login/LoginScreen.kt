package com.loswachabaches.bachewatch.ui.screens.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    // Colores
    val negro = Color(0xFF0B0B0B)
    val amarillo = Color(0xFFFFDA25)
    val blanco = Color(0xFFFFFFFF)
    val gris = Color(0xFF4B4A4A)
    val rojo = Color(0xFFFF4D4D)
    var usuario by remember { mutableStateOf("") } //Aqui los valores de firebase
    var password by remember { mutableStateOf("") }
    val correoValido = Patterns.EMAIL_ADDRESS
        .matcher(usuario)
        .matches()
    val usuarioTieneError = usuario.isNotBlank() && !correoValido
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

            Text(
                text = "Aquí va el icono de BacheWatch",
                color = amarillo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 45.dp)
            )

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "Inicio de Sesión",
                color = blanco,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Correo electrónico")
                },
                singleLine = true,
                isError = usuarioTieneError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                supportingText = {
                    if (usuarioTieneError) {
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
                    Text("Contraseña")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = blanco,
                    unfocusedTextColor = blanco,
                    focusedBorderColor = amarillo,
                    unfocusedBorderColor = blanco.copy(alpha = 0.45f),
                    focusedLabelColor = amarillo,
                    unfocusedLabelColor = blanco.copy(alpha = 0.70f),
                    cursorColor = amarillo
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {},
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = amarillo,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = correoValido && password.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = amarillo,
                    contentColor = negro,
                    disabledContainerColor = gris,
                    disabledContentColor = blanco.copy(alpha = 0.60f)
                )
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = "Cambiar contraseña",
                    color = blanco,
                    fontSize = 14.sp
                )
            }
        }
    }
}