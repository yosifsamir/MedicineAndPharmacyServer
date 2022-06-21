package com.example.mypharmapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Event
import java.text.SimpleDateFormat
import java.util.*

class AllEventsAdapter(private var eventList: MutableList<Event>) : RecyclerView.Adapter<AllEventsAdapter.AllEventsViewHolder>() {


    private lateinit var addEventDatailsListener: AddEventDatailsListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllEventsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.all_events_layout,parent,false)
        return AllEventsViewHolder(view)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: AllEventsViewHolder, position: Int) {
        holder.eventNameTxt.text=eventList[position].eventName
        holder.eventPlaceTxt.text=eventList[position].eventPlace
        val date=Date(eventList[position].eventDate!!)
        val formatter = SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.US) // check Locale.US later on .
        holder.eventDateTxt.text=formatter.format(date)
        holder.itemView.setOnClickListener {
            addEventDatailsListener.addEventListener(eventList[position])
        }
    }

    fun addEventListener(addEventDatailsListener: AddEventDatailsListener){
        this.addEventDatailsListener=addEventDatailsListener
    }

    inner class AllEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTxt=itemView.findViewById<TextView>(R.id.eventNameTxt)
        val eventPlaceTxt=itemView.findViewById<TextView>(R.id.eventPlaceTxt)
        val eventDateTxt=itemView.findViewById<TextView>(R.id.eventDateTxt)
    }

    interface AddEventDatailsListener{
        fun addEventListener(event: Event)
    }

}