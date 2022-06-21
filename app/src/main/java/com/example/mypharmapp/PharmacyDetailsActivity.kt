package com.example.mypharmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypharmapp.adapter.MedAdapter
import com.example.mypharmapp.adapter.PharmacyDetailsAdapter
import com.example.mypharmapp.model.Medicine
import com.example.mypharmapp.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pharmacy_details.*

class PharmacyDetailsActivity : AppCompatActivity(), MedAdapter.ListenerFromCheckBox {
    private var pharmRef=FirebaseDatabase.getInstance().reference.child("pharmacy")
    private var medicineList= mutableListOf<Medicine>()
    private var pharmacyDetailsAdapter=PharmacyDetailsAdapter(this,medicineList)
    private  var mapMedicine: java.util.HashMap<String, Medicine> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_details)
        var pharmacy=Constants.pharmacy

        pharmacyDetailsRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        pharmacyDetailsRecycler.setHasFixedSize(true)
        pharmacyDetailsRecycler.adapter=pharmacyDetailsAdapter
        pharmacyDetailsAdapter.medCheckListener(this)

        FirebaseDatabase.getInstance().reference.child("Medicine").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    medicineList.clear()
                    snapshot.children.forEach({
                        var medicine=it.getValue(Medicine::class.java)
                        medicineList.add(medicine!!)
                    })
                    pharmacyDetailsAdapter.notifyDataSetChanged()

                    pharmRef.child(Constants.pharmacy.key!!).child("medicines").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val medList= mutableListOf<Medicine>()
                            if (snapshot.exists()){
                                snapshot.children.forEach({
                                    // you can limit data here by using snapshot.getValue("name") ......
                                    val medicine=it.getValue(Medicine::class.java)
                                    medList.add(medicine!!)
                                })
                                pharmacyDetailsAdapter.addAllMedicine(medList)
                            }
                        }
                    })
                }
            })

        savePharmDetailsBtn.setOnClickListener {
            pharmRef.child(Constants.pharmacy.key!!).child("medicines").setValue(mapMedicine as Map<String, Any>).addOnCompleteListener {
            // clear the HashMap here
//                    mapTest= HashMap()

                    }
        }
    }

    override fun medFromCheckBox(medicine: Medicine) {
        mapMedicine.set(medicine.key.toString(),medicine)
    }

    override fun removeMedFromCheckBox(medicine: Medicine) {
        mapMedicine.remove(medicine.key.toString())

    }
}