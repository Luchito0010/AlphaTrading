package com.mb.neox.com

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mb.neox.com.api.ApiClient
import com.mb.neox.com.api.ApiService
import com.mb.neox.com.models.LoginRequest
import com.mb.neox.com.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class Login : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        setContent {
            LoginScreen(
                onLoginSuccess = { sessionId ->
                    saveSession(sessionId)
                    navigateToNextActivity()
                },
                onLoginError = {
                    showErrorToast()
                }
            )
        }
    }

    private fun saveSession(SessionId: String) {
        sharedPreferences.edit().putString("B1Session", SessionId).apply()
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, ItemActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Contraseña o usuario inválido", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onLoginError: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var companyDB by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = companyDB,
            onValueChange = { companyDB = it },
            label = { Text("CompanyDB") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                performLogin(username, password, companyDB, context, onLoginSuccess, onLoginError)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}

fun performLogin(
    UserName: String,
    Password: String,
    CompanyDB: String,
    context: Context,
    onLoginSuccess: (String) -> Unit,
    onLoginError: () -> Unit
) {
    val loginRequest = LoginRequest(CompanyDB.trim(), Password.trim(), UserName.trim())
    val apiService = ApiClient.getClient().create(ApiService::class.java)

    apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.code() == 200 && response.body() != null) {
                val SessionId = response.body()?.SessionId ?: ""
                onLoginSuccess(SessionId)
            } else {
                onLoginError()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(context, "Error de conexión login: ${t.message}", Toast.LENGTH_LONG).show()
            Log.e("LoginError", "Error en login: ${t.message}")
        }
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onLoginSuccess = {},
        onLoginError = {}
    )
}
