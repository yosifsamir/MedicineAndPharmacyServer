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
import com.example.mypharmapp.model.Medicine

class MedAdapter(private var medList: MutableList<Medicine>, private var context: Context) :
    RecyclerView.Adapter<MedAdapter.MedViewHolder>() {
    private lateinit var listenerFromCheckBox: ListenerFromCheckBox
    var checkBoxStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.med_layout,parent,false)
        return MedViewHolder(view)
    }

    override fun getItemCount(): Int = medList.size

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        holder.medTxt.text = medList[position].name
        if(!checkBoxStateArray.get(position,false))
        {//checkbox unchecked.
            holder.medCheckBox.isChecked = false
        }
        else
        {//checkbox checked
            holder.medCheckBox.isChecked = true
        }

    }

    fun midCheckListener(listenerFromCheckBox: ListenerFromCheckBox){
        this.listenerFromCheckBox=listenerFromCheckBox
    }

    inner class MedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medTxt=itemView.findViewById<TextView>(R.id.medTxt)
        val medCheckBox=itemView.findViewById<CheckBox>(R.id.medCheckBox)

        init{
            medCheckBox.setOnClickListener {

                if(!checkBoxStateArray.get(adapterPosition,false))
                {//checkbox checked
                    medCheckBox.isChecked = true
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition,true)

                    listenerFromCheckBox.medFromCheckBox(medList[adapterPosition])
                }
                else
                {//checkbox unchecked
                    medCheckBox.isChecked = false
//stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition,false)

                    listenerFromCheckBox.removeMedFromCheckBox(medList[adapterPosition])

                }
            }

    }
}

    interface ListenerFromCheckBox{
        fun medFromCheckBox(medicine: Medicine)
        fun removeMedFromCheckBox(medicine: Medicine)
    }

}