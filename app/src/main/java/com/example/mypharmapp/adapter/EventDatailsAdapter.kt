package com.example.mypharmapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R

class EventDatailsAdapter(private val eventUsers:HashMap<String,String>) : RecyclerView.Adapter<EventDatailsAdapter.EventDatailsViewHolder>() {

//    private var eventUsersList: List<Pair<String, String>>
    private var eventUsersList= mutableListOf<Pair<String, String>>()

    init {
//        eventUsersList=eventUsers.toList()
        eventUsersList.addAll(eventUsers.toList())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDatailsViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.event_datails_layout,parent,false)
        return EventDatailsViewHolder(view)
    }

    override fun getItemCount(): Int = eventUsers.size

    override fun onBindViewHolder(holder: EventDatailsViewHolder, position: Int) {
        holder.eventUserTxt.text=eventUsersList[position].second
    }

    fun addAllEventDatails(toList: List<Pair<String, String>>) {
        eventUsersList.clear()
        eventUsersList.addAll(toList)
        notifyDataSetChanged()
    }

    inner class EventDatailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventUserTxt=itemView.findViewById<TextView>(R.id.eventUserTxt)

    }

}