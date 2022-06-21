package com.example.mypharmapp.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Symptoms

class SympAdapter(private var sympList: MutableList<Symptoms>, private var context: Context) :
    RecyclerView.Adapter<SympAdapter.SympViewHolder>() {
    private lateinit var sympListenerFromCheckBox: SympListenerFromCheckBox
    var checkBoxStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SympViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.symp_layout,parent,false)
        return SympViewHolder(view)
    }

    override fun getItemCount(): Int = sympList.size

    override fun onBindViewHolder(holder: SympViewHolder, position: Int) {
        holder.sympTxt.text = sympList[position].symptomsName
        if(!checkBoxStateArray.get(position,false))
        {//checkbox unchecked.
            holder.sympCheckBox.isChecked = false
        }
        else
        {//checkbox checked
            holder.sympCheckBox.isChecked = true
        }
//        holder.medCheckBox.setOnCheckedChangeListener { compoundButton, b ->
////            if (b==true)
////                listenerFromCheckBox.medFromCheckBox(medList[position])
////            else
////                listenerFromCheckBox.removeMedFromCheckBox(medList[position])
//            if(!checkBoxStateArray.get(position,false)){
//                listenerFromCheckBox.medFromCheckBox(medList[position])
//            }else
//                listenerFromCheckBox.removeMedFromCheckBox(medList[position])
//
//        }
    }

    fun midCheckListener(sympListenerFromCheckBox: SympListenerFromCheckBox){
        this.sympListenerFromCheckBox=sympListenerFromCheckBox
    }


    inner class SympViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sympTxt=itemView.findViewById<TextView>(R.id.sympTxt)
        val sympCheckBox=itemView.findViewById<CheckBox>(R.id.sympCheckBox)

        init{
            sympCheckBox.setOnClickListener {

                if(!checkBoxStateArray.get(adapterPosition,false))
                {//checkbox checked
                    sympCheckBox.isChecked = true
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition,true)

                    sympListenerFromCheckBox.sympFromCheckBox(sympList[adapterPosition])
                }
                else
                {//checkbox unchecked
                    sympCheckBox.isChecked = false
                    //stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition,false)

                    sympListenerFromCheckBox.removeSympFromCheckBox(sympList[adapterPosition])

                }
            }

        }
    }

    interface SympListenerFromCheckBox{
        fun sympFromCheckBox(symp: Symptoms)
        fun removeSympFromCheckBox(symp: Symptoms)
    }
}