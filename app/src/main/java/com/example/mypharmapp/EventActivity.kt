package com.example.mypharmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mypharmapp.api.APIService
import com.example.mypharmapp.model.Event
import com.example.mypharmapp.notification.Client
import com.example.mypharmapp.notification.Data
import com.example.mypharmapp.notification.Sender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_event.*
import java.util.*

class EventActivity : AppCompatActivity() {
    private lateinit var apiService: APIService
    private var compositeDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        apiService= Client.getClient().create(APIService::class.java)

        eventSubmitBtn.setOnClickListener {
            var eventName=eventNameEdt.text.toString()
            var eventPlace=eventPlaceEdt.text.toString()
            when{
                eventName.isNullOrEmpty()->{
                    eventNameEdt.error="Please enter an event name"
                    return@setOnClickListener
                }
                eventPlace.isNullOrEmpty()->{
                    eventPlaceEdt.error="Please enter a place for event"
                    return@setOnClickListener
                }
            }
//            obj.year = year
//            obj.month = month
//            obj.day = day
//            obj.hour = hour
//            obj.minute = minute
//            val cal = GregorianCalendar()
//            cal.set(year, month, day, hour, minute)
//            obj.date = cal.time

            var year=datePicker.year
            var month=datePicker.month
            var day=datePicker.dayOfMonth
            var hour=timePicker.hour
            var minute=timePicker.minute
            val cal = GregorianCalendar()
            cal.set(year, month, day, hour, minute)
            var date=Date()
            var date2=cal.time
            date.time=cal.time.time

            Log.d("ddd",date.toString())
            Log.d("ddd",date2.toString())
            Log.d("ddd", date.time.toString())
            Log.d("ddd",date2.time.toString())

            var date3=Date(date2.time)
            Log.d("ddd","Date 3 day :-"+ date3.day.toString())
            Log.d("ddd", "Date 3 month :-"+date3.month.toString())
            Log.d("ddd", "Date 3 year :-"+date3.year.toString())
            Log.d("ddd", "Date 3 hours :-"+date3.hours.toString())
            Log.d("ddd", "Date 3 minutes :-"+date3.minutes.toString())
            var calender=Calendar.getInstance()
            calender.time=date3
            Log.d("ddd","Calender day :-"+ calender.get(Calendar.DAY_OF_MONTH).toString())
            Log.d("ddd", "Calender month :-"+(calender.get(Calendar.MONTH)+1).toString())
            Log.d("ddd", "Calender year :-"+calender.get(Calendar.YEAR).toString())
            Log.d("ddd", "Calender hours :-"+calender.get(Calendar.HOUR).toString())
            Log.d("ddd", "Calender minutes :-"+calender.get(Calendar.MINUTE).toString())
            var key=FirebaseDatabase.getInstance().reference.push().key
//            var event=Event(key,eventName,calender.time.time,eventPlace,null)
            var event=Event(key,eventName,calender.time.time,eventPlace)
            FirebaseDatabase.getInstance().reference.child("Event").child(key!!).setValue(event).addOnCompleteListener {
                sendNotificationToTopics()
            }

        }

    }

    private fun sendNotificationToTopics() {
//        compositeDisposable.add(apiService.sendNotification(Sender(Common.token,R.mipmap.ic_launcher,"","",null,null))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()).subscribeOn({onNext->},{onError->}))
        compositeDisposable.add(apiService.sendNotification(
            Sender(
            Data(null,R.mipmap.ic_launcher
            ,""
            ,""
            ,null
            ,"/topics/events")
            ,"/topics/events")
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({onNext->
                Toast.makeText(this@EventActivity, "onNext.success -> ${onNext.success}", Toast.LENGTH_LONG).show()
                if (onNext.success == 200){
//                                    if (response.body().success != 1){
//                                        Toast.makeText(context1, "Failed!", Toast.LENGTH_SHORT).show();
//                                    }

                }
            },{onError->
                Toast.makeText(this@EventActivity, "${onError.message}}", Toast.LENGTH_LONG).show()

            }))
    }
}