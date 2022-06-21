package com.example.mypharmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypharmapp.adapter.MedAdapter
import com.example.mypharmapp.adapter.SympAdapter
import com.example.mypharmapp.model.Medicine
import com.example.mypharmapp.model.Symptoms
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), MedAdapter.ListenerFromCheckBox,
    SympAdapter.SympListenerFromCheckBox {

    private  var mapTest: java.util.HashMap<String, Medicine> = HashMap()
    private var medList= mutableListOf<Medicine>()
    private var sympList= mutableListOf<Symptoms>()
    private val ref=FirebaseDatabase.getInstance().reference.child("Medicine")
    private val refSym=FirebaseDatabase.getInstance().reference.child("Symptoms")
    private val medAdapter=MedAdapter(medList,this@MainActivity)
    private val sympAdapter= SympAdapter(sympList,this@MainActivity)

    private var selectedMed= mutableListOf<Medicine>()
    private var selectedSymp= mutableListOf<Symptoms>()

    private val linearLayoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        medRecycler.layoutManager=linearLayoutManager
        medRecycler.adapter=medAdapter
        medAdapter.midCheckListener(this)

        sympRecycler.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
        sympRecycler.adapter=sympAdapter
        sympAdapter.midCheckListener(this)

        addMedBtn.setOnClickListener {
            addMedToFirebase()
        }

        addSymtBtn.setOnClickListener {
            addSymtToFirbase()
        }

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHILD",snapshot.value.toString())
                    val medicine=snapshot.getValue(Medicine::class.java)
                    medList.add(medicine!!)
                medAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })

        refSym.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("CHILD",snapshot.value.toString())
                val symp=snapshot.getValue(Symptoms::class.java)
                sympList.add(symp!!)
                sympAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("CHILD_DELETED",snapshot.value.toString())
                val symp=snapshot.getValue(Symptoms::class.java)
                Log.d("CHILD_DELETED", symp!!.symptomsName!!)
                sympList.remove(symp)
                sympAdapter.notifyDataSetChanged()
            }

        })


        saveBtn.setOnClickListener {
            if (selectedMed.isNullOrEmpty() && selectedSymp.isNullOrEmpty())
                return@setOnClickListener

            // the below testRef for testing
//            val testRef=FirebaseDatabase.getInstance().reference.child("TESTING")
            for(symp in selectedSymp){
                val map= mapOf("listOfMedicine" to selectedMed)
                refSym.child(symp.keySymp!!).updateChildren(map)

//                testRef.child(symp.keySymp!!).child("Medicines").setValue(mapTest as Map<String, Any>).addOnCompleteListener {
//                    // clear the HashMap here
////                    mapTest= HashMap()
//                }
            }


        }

//        searchBtn.setOnClickListener {
////            startActivity(Intent(this@MainActivity,SearchActivity::class.java))
//        }

        pharmBtn.setOnClickListener {
            val intent=Intent(this@MainActivity,PharmacyActivity::class.java)
            startActivity(intent)
        }

        eventBtn.setOnClickListener {
            val intent=Intent(this,EventActivity::class.java)
            startActivity(intent)
        }

        visitBtn.setOnClickListener {
            val intent=Intent(this,VisitActivity::class.java)
            startActivity(intent)
        }

        allEventsBtn.setOnClickListener {
            val intent=Intent(this,AllEventsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun addSymtToFirbase() {
        val symp=symptomsEdt.text.toString()
        when{
            symp.isEmpty()->{symptomsEdt.error="Error Enter A Symptoms"
                return}
        }
        val key=refSym.push().key
        val symptoms=Symptoms(key,symp,null)
        refSym.child(key!!).setValue(symptoms).addOnCompleteListener {
            Toast.makeText(
                this@MainActivity,
                "Added Successfully",
                Toast.LENGTH_SHORT
            ).show()
            symptomsEdt.setText("")}
    }

    private fun addMedToFirebase() {
        val med=medicineEdt.text.toString()
        when{
            med.isEmpty()->{medicineEdt.error="Error Enter A Medicine"
                return}
        }
        val key=ref.push().key
        val medicine=Medicine(key,med)
        ref.child(key!!).setValue(medicine).addOnCompleteListener {
            Toast.makeText(
                this@MainActivity,
                "Added Successfully",
                Toast.LENGTH_SHORT
            ).show()
            medicineEdt.setText("")
            medRecycler.scrollToPosition(medAdapter.itemCount-1)

        }
    }

    override fun medFromCheckBox(medicine: Medicine) {
        selectedMed.add(medicine)
        mapTest.set(medicine.key.toString(),medicine)
        Log.d("SELECTED",selectedMed.toString())
    }

    override fun removeMedFromCheckBox(medicine: Medicine) {
        selectedMed.remove(medicine)
        mapTest.remove(medicine.key.toString())

        Log.d("SELECTED",selectedMed.toString())

    }

    override fun sympFromCheckBox(symp: Symptoms) {
        selectedSymp.add(symp)
        Log.d("SELECTED",selectedSymp.toString())

    }

    override fun removeSympFromCheckBox(symp: Symptoms) {
        selectedSymp.remove(symp)
        Log.d("SELECTED",selectedSymp.toString())
    }
}