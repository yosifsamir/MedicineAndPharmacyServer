package com.example.mypharmapp.model

data class Pharmacy(var pharmacy_name:String ?=null
                    ,var governorate:String ?=null
                    ,var city:String ?=null
//                    ,var medicines:MutableList<Medicine> ? =null
                    ,var medicines:HashMap<String,Medicine> ? =null
                    ,var key:String ? =null)