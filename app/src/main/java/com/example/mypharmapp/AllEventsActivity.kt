package com.example.mypharmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.mypharmapp.adapter.AllEventsAdapter
import com.example.mypharmapp.model.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_all_events.*

class AllEventsActivity : AppCompatActivity(), AllEventsAdapter.AddEventDatailsListener {
    private val eventRef= FirebaseDatabase.getInstance().reference.child("Event")
    private val events= mutableListOf<Event>()
    private val eventAdapter: AllEventsAdapter by lazy { AllEventsAdapter(events) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)
        allEventsRecycler.setHasFixedSize(true)
        allEventsRecycler.adapter=eventAdapter
        eventAdapter.addEventListener(this)

        eventRef.limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    events.clear()
                    snapshot.children.forEach{
                        val event=it.getValue(Event::class.java)
                        events.add(0,event!!)
                    }
                    eventAdapter.notifyDataSetChanged()
                }
            }
        })


    }

    override fun addEventListener(event: Event) {
        val intent=Intent(this,EventDetailsActivity::class.java)
        intent.putExtra("eventKey",event.eventKey)
        startActivity(intent)
    }
}