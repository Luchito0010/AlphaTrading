package com.mb.neox.com

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mb.neox.com.api.ApiClient
import com.mb.neox.com.api.ApiService
import com.mb.neox.com.models.ItemUpdateRequest
import com.mb.neox.com.ui.theme.HQTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        setContent {
            HQTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ItemScreen(
                        modifier = Modifier.padding(innerPadding),
                        onAddClick = { itemCode, locationCode ->
                            performPatch(itemCode, locationCode)
                        }
                    )
                }
            }
        }
    }

    private fun performPatch(itemCode: String, locationCode: String) {
        val sessionId = sharedPreferences.getString("B1Session", null)
        Log.i("B1SESSION_SP_IA", sessionId.toString())

        if (sessionId.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión no encontrada", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val requestBody = ItemUpdateRequest(locationCode)

        apiService.updateItem(sessionId, itemCode, requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful && response.code() == 204) {
                    Toast.makeText(this@ItemActivity, "Ítem actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ItemActivity, "Error al actualizar el ítem", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ItemActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

@Composable
fun ItemScreen(
    modifier: Modifier = Modifier,
    onAddClick: (String, String) -> Unit
) {
    var itemCode by remember { mutableStateOf("") }
    var locationCode by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = itemCode,
            onValueChange = { itemCode = it },
            label = { Text("ItemCode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = locationCode,
            onValueChange = { locationCode = it },
            label = { Text("LocationCode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onAddClick(itemCode, locationCode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemScreenPreview() {
    HQTheme {
        ItemScreen { _, _ -> }
    }
}
