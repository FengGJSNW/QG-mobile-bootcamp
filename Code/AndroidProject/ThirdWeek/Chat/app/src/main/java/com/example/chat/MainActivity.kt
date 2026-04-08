package com.example.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.msg.Msg
import com.example.chat.msg.MsgAdapter

class MainActivity : AppCompatActivity() {

    private val msgList = ArrayList<Msg>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 设置布局文件
        setContentView(R.layout.activity_main)

        // 1. 初始化模拟数据
        initMsgs()

        // 2. 获取 RecyclerView 实例
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // 3. 设置布局管理器
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // 4. 设置适配器
        val adapter = MsgAdapter(msgList)
        recyclerView.adapter = adapter
    }

    private fun initMsgs() {
        msgList.add(Msg("Hello guy.", Msg.TYPE_RECEIVED))
        msgList.add(Msg("Hello. Who is that?", Msg.TYPE_SENT))
        msgList.add(Msg("This is Tom. Nice talking to you.", Msg.TYPE_RECEIVED))
    }
}