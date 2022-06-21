package com.example.mypharmapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypharmapp.adapter.DailyPlanAdaper
import com.example.mypharmapp.model.Medicine
import com.example.mypharmapp.model.User
import com.example.mypharmapp.model.Visit2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_daily_plan.*
import java.util.*
import kotlin.collections.HashMap

class DailyPlanActivity : AppCompatActivity(), DailyPlanAdaper.UserDailyListenerFromCheckBox {
    private var dateRef=FirebaseDatabase.getInstance().reference.child("visit")
    private var userRef=FirebaseDatabase.getInstance().reference.child("users")
    private var usersList= mutableListOf<User>()
    private var dailyPlanAdaper=DailyPlanAdaper(usersList,this)
    private  var mapTest: java.util.HashMap<String, User> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_plan)
        val i = intent
        if (i == null) {
            finish()
            return
        }
        val year = i.getIntExtra("year", 0)
        val month = i.getIntExtra("month", -1)
        val day = i.getIntExtra("day", 0)


        userDailyPlanRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        userDailyPlanRecycler.setHasFixedSize(true)
        userDailyPlanRecycler.adapter=dailyPlanAdaper
        dailyPlanAdaper.midCheckListener(this)

        val c = Calendar.getInstance()

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        Log.d("date","${year} + $month + $day+ $hour+ $minute")

        var date=year.toString()+month+day+hour+minute
        Log.d("date", date)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                snapshot.children.forEach({
                    usersList.add(it.getValue(User::class.java)!!)
                })
                dailyPlanAdaper.notifyDataSetChanged()
                Log.d("date",usersList.toString())
            }
        })
        unselectAllUsersBtn.setOnClickListener {
            dailyPlanAdaper.deleteAllSelected()
        }

        daily_plan_add_fab.setOnClickListener{
//            dateRef.child(date).setValue()
            var visitKey=dateRef.push().key
            var visit2=Visit2(visitKey,date,mapTest)
            dateRef.child(visitKey!!).setValue(visit2).addOnCompleteListener {

            }

        }
        submitFButton.setOnClickListener{
//            if (visitKey==null){
//
//            }
        }
    }

    override fun userDailyFromCheckBox(user: User) {
        Log.d("date", user.username!!)
        mapTest.set(user.uid.toString(),user)
        Log.d("date", mapTest.toString())
    }

    override fun removeuserDailyFromCheckBox(user: User) {
        Log.d("date", "User :- "+user.username!!+" removed")
        mapTest.remove(user.uid.toString())
        Log.d("date", mapTest.toString())


    }
}