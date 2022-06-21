package com.example.mypharmapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypharmapp.R
import com.example.mypharmapp.model.Symptoms

class SearchAdapter(private var sympList: MutableList<Symptoms>, private var context: Context) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.search_layout,parent,false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = sympList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Log.d("data", sympList[position].symptomsName.toString())
        holder.sympTxt.text = sympList[position].symptomsName
        holder.itemView.setOnClickListener{
            Log.d("search", sympList[position].toString())
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sympTxt=itemView.findViewById<TextView>(R.id.sympSearchTxt)
    }
}