package com.example.mypharmapp.model

data class Event(var eventKey : String ? =null
            ,var eventName:String ? =null
            ,var eventDate:Long ? = 0
            ,var eventPlace:String ? =null
            ,var registerUsers:HashMap<String,String> =HashMap()) {
}