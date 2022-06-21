package com.example.mypharmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mypharmapp.adapter.VisitAdapter
import com.example.mypharmapp.api.APIService
import com.example.mypharmapp.model.User
import com.example.mypharmapp.model.VisitUser
import com.example.mypharmapp.notification.Client
import com.example.mypharmapp.notification.Data
import com.example.mypharmapp.notification.Sender
import com.example.toothpaste.notification.TokenModel
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_visit.*

class VisitActivity : AppCompatActivity(), VisitAdapter.AddSendClickListener {
//    private var visitRef = FirebaseDatabase.getInstance().reference.child("VisitUser")
    private var visitRef2 = FirebaseDatabase.getInstance().reference.child("VisitUser")
    private var userRef = FirebaseDatabase.getInstance().reference.child("users")
    private var tokenRef = FirebaseDatabase.getInstance().reference.child("Tokens")
    private var userListAd = mutableListOf<User>()
    private var visitAdapter = VisitAdapter(userListAd)
    private lateinit var apiService: APIService
    private var compositeDisposable = CompositeDisposable()
    private var visitUserList= mutableListOf<VisitUser>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        apiService = Client.getClient().create(APIService::class.java)

        visitRecyclerView.setHasFixedSize(true)
        visitRecyclerView.adapter = visitAdapter
        visitAdapter.addSendClickListener(this)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                snapshot.children.forEach({
                    var user = it.getValue(User::class.java)
                    userList.add(user!!)
                })
                userListAd.addAll(userList)
                visitAdapter.notifyDataSetChanged()
            }
        })

        visitRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                visitUserList.clear()
                snapshot.children.forEach({
                    val userVisit=it.children.last().getValue(VisitUser::class.java)
                    if (userVisit != null) {
                        Log.d("ddd",userVisit.toString())
                        visitUserList.add(userVisit)
                    }
                })
                visitAdapter.addAllRemainingUserData(visitUserList)
            }
        })
    }

    override fun sendClick(visit: VisitUser) {
        if (!visit.visit_date!!.isEmpty() && (visit.visit_date!!.split("/").size - 1 == 2
                    || visit.visit_date!!.split("-").size - 1 == 2)
        ) {
            Log.d("ddd", visit.uid!!)
            Toast.makeText(this@VisitActivity, visit.visit_date, Toast.LENGTH_SHORT).show()
//            val key=visitRef.push().key
//            visit.visit_key=key
//            visitRef.child(visit.visit_key!!).setValue(visit).addOnCompleteListener {
//                tokenRef.child(visit.uid!!).addListenerForSingleValueEvent(object :
//                    ValueEventListener {
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val token=snapshot.getValue(TokenModel::class.java)
//                        Log.d("ddd",token!!.uid!!)
//                        Log.d("ddd", token.token!!)
//                        sendNotification(token)
//                    }
//                })
//            }
            val key = visitRef2.push().key
            visit.visit_key = key
            visitRef2.child(visit.uid!!).child(visit.visit_key!!).setValue(visit)
                .addOnCompleteListener {
                    tokenRef.child(visit.uid!!).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            Toast.makeText(this@VisitActivity, "${visit.uid}", Toast.LENGTH_SHORT).show()

                            val token=snapshot.getValue(TokenModel::class.java)
                            Log.d("ddd",token!!.uid!!)
                            Log.d("ddd", token.token!!)
                            sendNotification(token)
                        }
                    })
                }


        }

    }

    private fun sendNotification(token: TokenModel) {
        compositeDisposable.add(apiService.sendNotification(
            Sender(
                Data(
                    "Server"
                    , R.mipmap.ic_launcher
                    , ""
                    , ""
                    , token!!.uid, null
                )
                , token!!.token
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onNext ->
                Toast.makeText(
                    this@VisitActivity,
                    "onNext.success -> ${onNext.success}",
                    Toast.LENGTH_LONG
                ).show()
                if (onNext.success == 200) {


                }
            }, { onError ->
                Toast.makeText(this@VisitActivity, "${onError.message}}", Toast.LENGTH_LONG).show()

            })
        )
    }
}