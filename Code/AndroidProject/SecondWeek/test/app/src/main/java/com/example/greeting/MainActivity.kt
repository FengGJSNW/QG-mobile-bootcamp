package com.example.greeting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greeting.ui.theme.GreetingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFE", "Main onCreate")

        setContent {
            GreetingTheme {
                val scope = rememberCoroutineScope()

                // ✅ 启动时读取本地保存的数据
                val savedMessages by MessageStorage.messagesFlow(this@MainActivity)
                    .collectAsState(initial = emptyList())

                // ✅ UI 使用的状态列表
                var messages by remember { mutableStateOf(emptyList<Message>()) }

                // 当 DataStore 读到数据后，同步到 UI 状态（只要 savedMessages 变化就更新）
                LaunchedEffect(savedMessages) {
                    messages = if (savedMessages.isNotEmpty()) savedMessages else listOf(
                        Message(1, "Hello"),
                        Message(2, "Compose"),
                        Message(3, "Android"),
                    )
                }

                // ✅ 任何 messages 变化时，自动保存到本地
                LaunchedEffect(messages) {
                    MessageStorage.save(this@MainActivity, messages)
                }

                // 编辑回传
                val editLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result: ActivityResult ->
                    if (result.resultCode == RESULT_OK) {
                        val data = result.data ?: return@rememberLauncherForActivityResult
                        val id = data.getIntExtra("id", -1)
                        val newText = data.getStringExtra("text") ?: return@rememberLauncherForActivityResult
                        if (id != -1) {
                            messages = messages.map { if (it.id == id) it.copy(text = newText) else it }
                        }
                    }
                }

                Column {

                    Button(
                        onClick = {
                            val nextId = (messages.maxOfOrNull { it.id } ?: 0) + 1
                            messages = messages + Message(nextId, "New Message $nextId")
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Add")
                    }

                    Button(
                        onClick = {
                            messages = emptyList()
                            scope.launch { MessageStorage.clear(this@MainActivity) }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("Clear Saved")
                    }

                    LazyColumn {
                        items(messages, key = { it.id }) { msg ->
                            Text(
                                text = "#${msg.id}  ${msg.text}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = {
                                            val intent = Intent(
                                                this@MainActivity,
                                                SecondActivity::class.java
                                            ).apply {
                                                putExtra("id", msg.id)
                                                putExtra("text", msg.text)
                                            }
                                            editLauncher.launch(intent)
                                        },
                                        onLongClick = {
                                            messages = messages.filter { it.id != msg.id }
                                        }
                                    )
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}