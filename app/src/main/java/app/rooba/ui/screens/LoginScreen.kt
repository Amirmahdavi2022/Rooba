package app.rooba.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.rooba.R
import app.rooba.data.FxaAuthRepository
import app.rooba.ui.components.pressScale
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authRepository: FxaAuthRepository,
    onSignedIn: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }
    var awaitingTwoFactor by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val buttonInteraction = remember { MutableInteractionSource() }
    val fieldShape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.rooba_mark),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Rooba",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Sign in with a Firefox account. Every account comes with 50 GB of free VPN traffic each month.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))

        if (!awaitingTwoFactor) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text("Email") },
                singleLine = true,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                shape = fieldShape,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    errorMessage = null
                    isLoading = true
                    scope.launch {
                        val result = authRepository.startLogin(email, password)
                        isLoading = false
                        result.onSuccess { needsVerification ->
                            if (needsVerification) awaitingTwoFactor = true else onSignedIn()
                        }.onFailure { errorMessage = it.message ?: "Sign-in failed" }
                    }
                },
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                interactionSource = buttonInteraction,
                shape = fieldShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .pressScale(buttonInteraction),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Sign in", fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = onCreateAccount,
            ) {
                Text("No account? Create one for free")
            }
        } else {
            Text(
                "We sent a confirmation code to your email. Enter it below.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = code,
                onValueChange = { code = it.trim() },
                label = { Text("Confirmation code") },
                singleLine = true,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    errorMessage = null
                    isLoading = true
                    scope.launch {
                        val result = authRepository.submitTwoFactorCode(code)
                        isLoading = false
                        result.onSuccess { onSignedIn() }
                            .onFailure { errorMessage = it.message ?: "Verification failed" }
                    }
                },
                enabled = !isLoading && code.isNotBlank(),
                interactionSource = buttonInteraction,
                shape = fieldShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .pressScale(buttonInteraction),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Verify", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(28.dp))
        Text(
            "Rooba is an independent app and is not affiliated with Mozilla.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
