package com.example.mypharmapp.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Medicine

class PharmacyDetailsAdapter(context: Context, medicineList: MutableList<Medicine>) :
    RecyclerView.Adapter<PharmacyDetailsAdapter.PharmacyViewHolder>() {

    private var context: Context?= context
    private var medicineList:MutableList<Medicine>?= medicineList
    var checkBoxStateArray = SparseBooleanArray()
    private lateinit var listenerFromCheckBox: MedAdapter.ListenerFromCheckBox


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.pharmacy_details_layout,parent,false)
        return PharmacyViewHolder(view)
    }

    override fun getItemCount(): Int= medicineList!!.size

    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        holder.pharmMedCheckBox.text= this.medicineList!![position].name
        if(!checkBoxStateArray.get(position,false))
        {//checkbox unchecked.
            holder.pharmMedCheckBox.isChecked = false
        }
        else
        {//checkbox checked
            holder.pharmMedCheckBox.isChecked = true
        }
    }

    fun medCheckListener(listenerFromCheckBox: MedAdapter.ListenerFromCheckBox){
        this.listenerFromCheckBox=listenerFromCheckBox
    }



    fun addAllMedicine(medicineList2: MutableList<Medicine>){

        medicineList2.forEach {
            if (it in medicineList!!){
                val index=medicineList!!.indexOf(it)
                checkBoxStateArray.set(index,true)
                listenerFromCheckBox.medFromCheckBox(it)

            }
        }
        notifyDataSetChanged()

    }



    inner class PharmacyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pharmMedCheckBox=itemView.findViewById<CheckBox>(R.id.pharmMedCheckBox)
        init{
            pharmMedCheckBox.setOnClickListener {

                if(!checkBoxStateArray.get(adapterPosition,false))
                {//checkbox checked
                    pharmMedCheckBox.isChecked = true
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition,true)

                    listenerFromCheckBox.medFromCheckBox(medicineList?.get(adapterPosition)!!)
                }
                else
                {//checkbox unchecked
                    pharmMedCheckBox.isChecked = false
                    //stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition,false)

                    listenerFromCheckBox.removeMedFromCheckBox(medicineList?.get(adapterPosition)!!)

                }
            }

        }
    }
//    interface ListenerFromCheckBox{
//        fun medFromCheckBox(medicine: Medicine)
//        fun removeMedFromCheckBox(medicine: Medicine)
//    }
}