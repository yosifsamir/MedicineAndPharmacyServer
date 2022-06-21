package com.example.mypharmapp.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.collection.SparseArrayCompat
import androidx.collection.set
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.User
import com.example.mypharmapp.model.VisitUser

class VisitAdapter(private val userList: MutableList<User>) :
    RecyclerView.Adapter<VisitAdapter.VisitViewHolder>() {
    private lateinit var clickListener: AddSendClickListener
    private var visitState = SparseArrayCompat<String>()
    private var visitUserList= mutableListOf<VisitUser>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.visit_layout, parent, false)
        return VisitViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {

        holder.visitUserTxt.text = userList[position].username

//        Log.d("ddd",position.toString())
//        Log.d("ddd", "Size "+visitState.size().toString())
        if(visitState.get(position) != null)
        {
            holder.dateVisitEdt.setText(visitState.get(position)!!)
        }

        else {
            holder.dateVisitEdt.setText("")
        }
        if (visitUserList.size>0) {
            visitUserList.forEach {
                if (it.uid==userList[position].uid)
                {
                    Log.d("ddd",it.uid.toString())
                    Log.d("ddd","Full Visit User "+it)
                    holder.visitTypeTxt.text = it.visit_type
                    holder.dateVisitEdt.setText(it.visit_date)
                    return@forEach
                }
            }

        }


    }

    fun addSendClickListener(clickListener: AddSendClickListener){
        this.clickListener=clickListener
    }

    fun addAllRemainingUserData(visitUserList: MutableList<VisitUser>) {
        this.visitUserList.clear()
        this.visitUserList.addAll(visitUserList)
//        notifyDataSetChanged()
//        notifyItemRangeChanged(0,this.visitUserList.size-1)
//        notifyItemRangeChanged(0,itemCount)
//        notifyItemRangeInserted(0,visitUserList.size)

        notifyDataSetChanged()
//        notifyDataSetChanged()
//        notifyItemChanged()
    }

    inner class VisitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val visitUserTxt = itemView.findViewById<TextView>(R.id.visitUserTxt)
        val dateVisitEdt = itemView.findViewById<EditText>(R.id.dateVisitEdt)
        val sendVistBtn = itemView.findViewById<Button>(R.id.sendVistBtn)
        val visitTypeTxt = itemView.findViewById<TextView>(R.id.visitTypeTxt)

        init {

            dateVisitEdt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(data: Editable?) {

                    if (visitState.containsKey(adapterPosition) || !data.isNullOrEmpty()) {
                        Log.d("ddd","afterText "+data.toString())
                        Log.d("ddd","adapterPosition "+adapterPosition)
                        visitState.set(adapterPosition, dateVisitEdt.text.toString())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            })
            sendVistBtn.setOnClickListener {
                if (visitState.containsKey(adapterPosition)){
                    val visit=VisitUser(null,userList[adapterPosition].uid,visitState.get(adapterPosition),"Face To Face")
                    clickListener.sendClick(visit)
                }

            }
        }
    }

    interface AddSendClickListener{
        fun sendClick(visit: VisitUser)
    }
}