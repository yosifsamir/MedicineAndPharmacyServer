package com.example.mypharmapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypharmapp.adapter.SearchAdapter
import com.example.mypharmapp.model.Medicine
import com.example.mypharmapp.model.Symptoms
import com.example.mypharmapp.model.Visit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_search.*
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {
    private val refSym= FirebaseDatabase.getInstance().reference.child("Symptoms")
    private val resultList= mutableListOf<Symptoms>()
    private val linearLayoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    private val searchAdapter=SearchAdapter(resultList,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchResultRecycler.layoutManager=linearLayoutManager
        searchResultRecycler.adapter=searchAdapter
        searchResultRecycler.setHasFixedSize(true)

        searchResultBtn.setOnClickListener {
            var search=searchEdt.text.toString()
            when{
                search.isNullOrEmpty()-> {
                    searchEdt.setError("Enter Some Text For Searching")
                    return@setOnClickListener
                }
            }
            if (search.length==0)
                return@setOnClickListener
            var firstLetter=search[0].toUpperCase().toString()

            var remainingLetters=search.substring(1)
            search=firstLetter+remainingLetters
            refSym.orderByChild("symptomsName").startAt(search).endAt(search+"\uf8ff").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        resultList.clear()
                        snapshot.children.forEach({
                            val symptoms=it.getValue(Symptoms::class.java)
                            resultList.add(symptoms!!)
                        })
                        Log.d("SEARCH_RESULT",resultList.toString())
                        searchAdapter.notifyDataSetChanged()
                    }
                })
        }

        val dataKeys= listOf<String>("-N2OkG8GmaXdBkBiPzOH","-N2OmoXkEuEAeahb7t2v")
        val myMed= mutableListOf<Medicine>()
        var medRef=FirebaseDatabase.getInstance().reference.child("Medicine")
        for (data in dataKeys){
            medRef.child(data).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val med=snapshot.getValue(Medicine::class.java)
                    myMed.add(med!!)
                    Log.d("MED",myMed.toString())
                }
            })
        }
        val myMed2= mutableListOf<Medicine>()
        var medRef2=FirebaseDatabase.getInstance().reference.child("Medicine")
        medRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach({
                    if (it.key in dataKeys){
                        val med=it.getValue(Medicine::class.java)
                        myMed2.add(med!!)
                    }
                })
                Log.d("MED2",myMed2.toString())
            }
        })
        val date=Date()
        Log.d("date", date.toString())
        val myTime=date.time
        Log.d("date", myTime.toString())
        val calender=Calendar.getInstance()
        val date2=Date(myTime)
        calender.time=date2
        Log.d("date",calender.get(Calendar.DAY_OF_WEEK).toString()) // you should add 1 here
        Log.d("date",calender.get(Calendar.HOUR_OF_DAY).toString()) // you should add 1 here
//        Log.d("date",calender.get())
        Log.d("date", calender.time.toString())
//        Log.d("date", calender.time.day)
        var day=calender.get(Calendar.DAY_OF_MONTH)
        var month=calender.get(Calendar.MONTH)
        var year=calender.get(Calendar.YEAR)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ")
        simpleDateFormat.format(date2)

        addDateBtn.setOnClickListener {
            var datePickerDialog=DatePickerDialog(this@SearchActivity, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    dateTxt.setText("${dayOfMonth}/${month+1}/${year}")
                }
            },year,month,day)
            datePickerDialog.show()
        }
        val visitRef=FirebaseDatabase.getInstance().reference.child("Visit")
        addVisitBtn.setOnClickListener {
            val visit=Visit(calender.time.time.toString(),null)
            visitRef.push().setValue(visit).addOnCompleteListener {

            }
        }
        visitRef.limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val databaseReference=snapshot.children.iterator().next()
                    val visit=databaseReference.getValue(Visit::class.java)
//                    val visit=snapshot.getValue(Visit::class.java)
                    dateDataTxt.setText(visit!!.date.toString())
                    dateUidTxt.setText(databaseReference.key)
                    Log.d("visit", visit!!.date.toString())
                    snapshot.key?.let { Log.d("visit", it) }
                }

            }
        })

    }
}