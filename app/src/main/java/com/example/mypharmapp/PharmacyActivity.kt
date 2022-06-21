package com.example.mypharmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypharmapp.adapter.PharmacyAdapter
import com.example.mypharmapp.model.Pharmacy
import com.example.mypharmapp.utils.Constants
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pharmacy.*

class PharmacyActivity : AppCompatActivity(), PharmacyAdapter.PharmacyListener {
    private var pharmRef=FirebaseDatabase.getInstance().reference.child("pharmacy")
    private var pharmacyList= mutableListOf<Pharmacy>()
    private var pharmacyAdapter=PharmacyAdapter(this,pharmacyList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy)

        pharmacyRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        pharmacyRecycler.setHasFixedSize(true)
        pharmacyRecycler.adapter=pharmacyAdapter
        pharmacyAdapter.setOnPharmacyListener(this)

        addPharmacyBtn.setOnClickListener {
            val pharmacyName=pharmacyNameEdt.text.toString()
            val pharmacyGovernorate=pharmacyGovernorateEdt.text.toString()
            val pharmacyCity=pharmacyCityEdt.text.toString()
            when{
                pharmacyName.isEmpty()->{
                    pharmacyNameEdt.error="Please enter name for pharmacy"
                    pharmacyNameEdt.requestFocus()
                    return@setOnClickListener
                }
                pharmacyGovernorate.isEmpty()->{
                    pharmacyGovernorateEdt.error="Please enter governorate for pharmacy"
                    pharmacyGovernorateEdt.requestFocus()
                    return@setOnClickListener
                }
                pharmacyCity.isEmpty()->{
                    pharmacyCityEdt.error="Please enter City for pharmacy"
                    pharmacyCityEdt.requestFocus()
                    return@setOnClickListener
                }
            }
            val key=pharmRef.push().key
            val pharmacy=Pharmacy(pharmacyName, pharmacyGovernorate,pharmacyCity,null,key)
            pharmRef.child(key!!).setValue(pharmacy).addOnCompleteListener {
                Toast.makeText(this@PharmacyActivity, "Pharmacy is inserted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        pharmRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                pharmacyList.add(snapshot.getValue(Pharmacy::class.java)!!)
                pharmacyAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })

    }

    override fun pharmacyListenerData(pharmacy: Pharmacy) {
        Constants.pharmacy=pharmacy
        val intent=Intent(this@PharmacyActivity,PharmacyDetailsActivity::class.java)
        startActivity(intent)
    }
}