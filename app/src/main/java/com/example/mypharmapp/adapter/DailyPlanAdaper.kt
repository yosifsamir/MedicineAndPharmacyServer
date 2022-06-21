package com.example.mypharmapp.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Symptoms
import com.example.mypharmapp.model.User

class DailyPlanAdaper :RecyclerView.Adapter<DailyPlanAdaper.DailyPlanViewHolder> {


    private lateinit var userDailyListenerFromCheckBox: UserDailyListenerFromCheckBox
    private lateinit var userList:MutableList<User>
    private lateinit var context: Context
    var checkBoxStateArray = SparseBooleanArray()

    constructor(userList:MutableList<User> , context: Context){
        this.userList=userList
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyPlanViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.daily_plan_layout,parent,false)
        return DailyPlanViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: DailyPlanViewHolder, position: Int) {
        holder.userDailyTxt.setText(userList[position].username)
        if(!checkBoxStateArray.get(position,false))
        {//checkbox unchecked.
            holder.userDailyCheckBox.isChecked = false
        }
        else
        {//checkbox checked
            holder.userDailyCheckBox.isChecked = true
        }


    }

    fun midCheckListener(userDailyListenerFromCheckBox: UserDailyListenerFromCheckBox){
        this.userDailyListenerFromCheckBox=userDailyListenerFromCheckBox
    }


    // deleteAllSelected for testing . please check it later .
    fun deleteAllSelected(){
        checkBoxStateArray.forEach { key, value ->
            checkBoxStateArray.set(key,false)
            userDailyListenerFromCheckBox.removeuserDailyFromCheckBox(userList[key])

        }
        notifyDataSetChanged()
    }


    inner class DailyPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userDailyTxt=itemView.findViewById<TextView>(R.id.userDailyTxt)
        val userDailyCheckBox=itemView.findViewById<CheckBox>(R.id.userDailyCheckBox)

        init{
            userDailyCheckBox.setOnClickListener {

                if(!checkBoxStateArray.get(adapterPosition,false))
                {//checkbox checked
                    userDailyCheckBox.isChecked = true
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition,true)

                    userDailyListenerFromCheckBox.userDailyFromCheckBox(userList[adapterPosition])
                }
                else
                {//checkbox unchecked
                    userDailyCheckBox.isChecked = false
                    //stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition,false)

                    userDailyListenerFromCheckBox.removeuserDailyFromCheckBox(userList[adapterPosition])

                }
            }

        }



    }

    interface UserDailyListenerFromCheckBox{
        fun userDailyFromCheckBox(user: User)
        fun removeuserDailyFromCheckBox(user: User)
    }


}