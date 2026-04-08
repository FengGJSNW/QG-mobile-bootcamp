package com.example.chat.msg


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R


class MsgAdapter(val msgList: List<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class LeftsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftMsg: TextView = view.findViewById(R.id.leftMsg)
    }

    inner class RightsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rightMsg: TextView = view.findViewById(R.id.rightMsg)
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == Msg.TYPE_RECEIVED) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_left_bubbles, parent, false)
            LeftsViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_right_bubbles, parent, false)
            RightsViewHolder(view)
        }

    override fun getItemCount() = msgList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is LeftsViewHolder -> holder.leftMsg.text = msg.content
            is RightsViewHolder -> holder.rightMsg.text = msg.content
            else -> throw IllegalArgumentException("Invalid ViewHolder type")
        }
    }

}