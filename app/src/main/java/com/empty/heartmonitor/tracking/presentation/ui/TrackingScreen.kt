package com.empty.heartmonitor.tracking.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.empty.heartmonitor.tracking.data.Watcher
import com.empty.heartmonitor.tracking.presentation.TrackingViewModel

@Composable
fun TrackingScreen(trackingViewModel: TrackingViewModel) {
    val watchers by trackingViewModel.allWatchers.collectAsState(initial = listOf())
    val isTracking by trackingViewModel.trackingState.collectAsState(initial = false)
    val isMonitoring by trackingViewModel.isMonitoring.collectAsState(initial = false)
    TrackingUi(
        watchers = watchers,
        isTracking = isTracking,
        isMonitoring = isMonitoring,
        onNewWatcher = trackingViewModel::addWatcher,
        onRemoveWatcher = trackingViewModel::removeWatcher,
        onTrackingClick = trackingViewModel::onTrackingClick,
        changeMonitoring = trackingViewModel::changeMonitoring
    )
}

@Composable
fun TrackingUi(
    watchers: List<Watcher>,
    isTracking: Boolean,
    isMonitoring: Boolean,
    onNewWatcher: (guid: String, name: String) -> Unit,
    onRemoveWatcher: (guid: String) -> Unit,
    onTrackingClick: () -> Unit,
    changeMonitoring: (Boolean) -> Unit
) {
    var isShowDialog by remember {
        mutableStateOf(false)
    }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = if (isTracking) "Моніторинг запущено" else "Моніторинг зупинено"
            )
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onTrackingClick.invoke() }) {
                Text(text = if (isTracking) "Зупинити моніторинг" else "Запустити моніторинг")
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally))
            {

                Checkbox(checked = isMonitoring, onCheckedChange = {
                    changeMonitoring.invoke(it)
                })
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "- Відправляти критичні показники"
                )
            }

            Spacer(modifier = Modifier.size(4.dp))
            Text(text = "Список відстежувачів:", Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.size(4.dp))
            LazyColumn {
                items(watchers) {
                    WatcherRow(it) {
                        onRemoveWatcher.invoke(it.guid)
                    }
                }
            }
        }
        FloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp), onClick = { isShowDialog = true }) {
            Image(imageVector = Icons.Default.Add, contentDescription = "delete")
        }
        if (isShowDialog)
            AddWatcherDialog({ guid, name ->
                onNewWatcher.invoke(guid, name)
                isShowDialog = false
            }) {
                isShowDialog = false
            }
    }
}

@Composable
fun AddWatcherDialog(onAddNewWatcher: (guid: String, name: String) -> Unit, onClose: () -> Unit) {
    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var guid by remember {
        mutableStateOf(TextFieldValue(""))
    }
    AlertDialog(onDismissRequest = {},
        text = {
            Column {
                Text(text = "Новий відстежувач", fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.size(8.dp))
                TextField(
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                    label = { Text(text = "Ім'я", color = Color.Black) },
                    value = name,
                    onValueChange = {
                        name = it
                    })
                Spacer(modifier = Modifier.size(4.dp))
                TextField(
                    colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
                    label = { Text(text = "Токен", color = Color.Black) },
                    value = guid,
                    onValueChange = {
                        guid = it
                    })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAddNewWatcher.invoke(guid.text, name.text)
            }) {
                Text(text = "Додати")
            }
        },
        dismissButton = {
            TextButton(onClick = { onClose.invoke() }) {
                Text(text = "Закрити")
            }
        }
    )
}

@Composable
fun WatcherRow(watcher: Watcher, onRemoveClick: () -> Unit) {
    Card(elevation = 8.dp, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            Surface(
                Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically), shape = CircleShape, color = Color.Gray
            ) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "device",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Column(
                Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(watcher.name, fontWeight = FontWeight.Bold)
                Text(watcher.guid, fontSize = 10.sp)
            }
            IconButton(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),
                onClick = { onRemoveClick.invoke() }) {
                Surface(Modifier.size(32.dp), shape = CircleShape, color = Color.Red) {
                    Image(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
            }

        }
    }

}