package com.example.mypharmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mypharmapp.adapter.EventDatailsAdapter
import com.example.mypharmapp.model.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_event_details.*

class EventDetailsActivity : AppCompatActivity() {
    private val eventRef=FirebaseDatabase.getInstance().reference.child("Event")
    private val eventsDatails= hashMapOf<String,String>()
    private val eventDatailsAdapter: EventDatailsAdapter by lazy { EventDatailsAdapter(eventsDatails) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        var intent=intent
        if (intent==null)
            return

        eventDatailsRecycler.setHasFixedSize(true)
        eventDatailsRecycler.adapter=eventDatailsAdapter
        val eventKey=intent.getStringExtra("eventKey")

        eventRef.child(eventKey!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val event=snapshot.getValue(Event::class.java)
                eventsDatails.clear()
                Log.d("ddd",event.toString())
                eventsDatails.putAll(event!!.registerUsers)
                eventDatailsAdapter.addAllEventDatails(eventsDatails.toList())
//                eventDatailsAdapter.notifyDataSetChanged()
            }
        })

    }
}