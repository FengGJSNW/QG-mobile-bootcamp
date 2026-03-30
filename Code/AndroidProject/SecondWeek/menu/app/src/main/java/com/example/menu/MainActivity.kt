package com.example.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_item -> Toast.makeText(this, "You Clicked Add",
                Toast.LENGTH_SHORT).show()
            R.id.remove_item -> Toast.makeText(this, "You Clicked Remove",
                Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. 开启边到边体验
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

        // 查找 button1 并设置跳转到 SecondActivity
        val button1 : Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "You Click the button1",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        val button2 : Button = findViewById(R.id.button2)
        button2.setOnClickListener {
            Toast.makeText(this, "You Click the button2",
                Toast.LENGTH_SHORT).show()
            finish()
        }

        val button3 : Button = findViewById(R.id.button4)
        button3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.baidu.com")
            startActivity(intent)
        }

        val button4 : Button = findViewById(R.id.button5)
        button4.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val buttonGotoPracticeLayout : Button = findViewById(R.id.buttonGotoPracticeLayout)
        buttonGotoPracticeLayout.setOnClickListener {
            val intent = Intent(this, PracticeLayout::class.java)
            startActivity(intent)
        }
    }
}
