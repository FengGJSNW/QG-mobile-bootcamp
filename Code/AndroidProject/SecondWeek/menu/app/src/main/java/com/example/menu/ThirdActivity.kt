package com.example.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)

        val button6 : Button = findViewById<Button>(R.id.button6)
        button6.setOnClickListener {
            val intent = Intent()
            intent.putExtra("data_return", "Hello FirstActivity")
            setResult(RESULT_OK, intent)
            finish()
        }

        val button : Button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val editText : EditText = findViewById<EditText>(R.id.input_text)
            val inputText = editText.getText().toString()
            Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show()
        }


        val handler = Handler(Looper.getMainLooper())

        val buttonControlProgressBar : Button = findViewById<Button>(R.id.buttonShowProgressBar)
        buttonControlProgressBar.setOnClickListener {
            val progressBar : ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
            if(progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE)
                val runnable = object : Runnable {
                    override fun run() {
                        progressBar.progress = progressBar.progress + 1
                        handler.postDelayed(this, 1000)
                    }
                }

                handler.postDelayed(runnable, 1000)
            } else {
                progressBar.setVisibility(View.GONE)
            }
        }

        val buttonControlAlertDialog : Button = findViewById<Button>(R.id.buttonShowAlertDialog)
        buttonControlAlertDialog.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("This is a Dialog")
                setMessage("Something important.")
                setCancelable(false)
                setPositiveButton("OK") { dialog, which -> }
                setNegativeButton("Cancel") { dialog, which -> }
                show()
            }
        }




    }
}