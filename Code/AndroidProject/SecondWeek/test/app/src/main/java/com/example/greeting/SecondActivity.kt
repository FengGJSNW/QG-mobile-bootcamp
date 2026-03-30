package com.example.greeting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.greeting.ui.theme.GreetingTheme

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFE2", "Second onCreate")

        val id = intent.getIntExtra("id", -1)
        val initialText = intent.getStringExtra("text") ?: ""

        setContent {
            GreetingTheme {
                var text by remember { mutableStateOf(initialText) }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(text = "Edit Message #$id", modifier = Modifier.padding(bottom = 12.dp))

                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Text") }
                    )

                    Button(
                        onClick = {
                            val result = Intent().apply {
                                putExtra("id", id)
                                putExtra("text", text)
                            }
                            setResult(Activity.RESULT_OK, result)
                            finish()
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}