package com.unsa.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.unsa.lab10.ui.theme.Lab10Theme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10Theme {
                WorkersScreen()
            }
        }
    }
}

@Composable
fun WorkersScreen() {
    val context = LocalContext.current
    var currentWork by remember { mutableStateOf<UUID?>(null) }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "WorkManager cada 15 segundos",
            style = MaterialTheme.typography.headlineSmall
        )
        Button(onClick = {
            val request = OneTimeWorkRequestBuilder<LoggingWorker>().addTag("logging_worker").build()
            currentWork = request.id
            WorkManager.getInstance(context).enqueue(request)
        }) {
            Text("Crear Worker")
        }
        Button(onClick = {
            WorkManager.getInstance(context).cancelAllWorkByTag("logging_worker")
        }) {
            Text("Detener Worker(s)")
        }
        currentWork?.let { id ->
            val workInfo = WorkManager.getInstance(context).getWorkInfoByIdLiveData(id).observeAsState()
            Text("Estado: ${workInfo.value?.state}")
        }
    }
}