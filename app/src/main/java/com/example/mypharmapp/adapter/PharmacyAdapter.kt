package com.example.mypharmapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Pharmacy

class PharmacyAdapter(context: Context, pharmacyList: MutableList<Pharmacy>) :
    RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>() {
    private lateinit var pharmacyListener: PharmacyListener
    private var context:Context?= context
    private var pharmacyList:MutableList<Pharmacy>?= pharmacyList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.pharmacy_layout,parent,false)
        return PharmacyViewHolder(view)
    }

    override fun getItemCount(): Int = pharmacyList!!.size

    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        holder.pharmTxt.text= this.pharmacyList!![position].pharmacy_name
        holder.itemView.setOnClickListener {
            pharmacyListener.pharmacyListenerData(pharmacyList!![position])
        }
    }

    fun setOnPharmacyListener(pharmacyListener:PharmacyListener){
        this.pharmacyListener=pharmacyListener
    }

    inner class PharmacyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pharmTxt=itemView.findViewById<TextView>(R.id.pharmacyTxt)

    }

    interface PharmacyListener{
        fun pharmacyListenerData(pharmacy: Pharmacy)
    }

}