package com.example.meet.Services

import android.content.ContentValues.TAG
import android.util.Log
import com.example.meet.Models.RoomBookerModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class BookService {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun insert(roomBookerModel: RoomBookerModel, onSuccessListener: () -> Unit = {}, onFailureListener: () -> Unit = {}){

        val timestamp = Timestamp(roomBookerModel.calendar.time)
        val book = hashMapOf(
            "title" to roomBookerModel.title,
            "info" to roomBookerModel.info,
            "date" to timestamp,
            "active_time" to roomBookerModel.activeTime
        )

        db.collection("booked")
            .add(book)
            .addOnSuccessListener { documentReference ->
                onSuccessListener()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                onFailureListener()
                Log.w(TAG, "Error adding document", e)
            }.addOnCompleteListener{
                Log.w(TAG, "complete")
            }.addOnCanceledListener {
                Log.w(TAG, "on cancel")
            }
    }

    fun get(onSuccessListener: (RoomBookerModel) -> Unit){

        db.collection("booked").orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val date = Date((document["date"] as Timestamp).seconds * 1000)
                    val cal = Calendar.getInstance()
                    cal.time = date
                    onSuccessListener(RoomBookerModel(
                        document["title"].toString(),
                        document["info"].toString(),
                        cal,
                        document["active_time"].toString().toInt())
                    )

                    Log.d("successed", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}