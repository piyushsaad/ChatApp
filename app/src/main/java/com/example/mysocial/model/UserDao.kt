package com.example.mysocial.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/*
The difference between these two is that an object represents a set of instances while an instance is a certain, specific representation.
In simple words, Instance refers to the copy of the object at a particular time whereas object refers to the memory address of the class.
*/


class UserDao {
    private val db=FirebaseFirestore.getInstance()
    private val userCollection=db.collection("user54")

    fun addUser(user:User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid1).set(it).await()          //document section with name of uid we get from google
            }
        }
    }

    fun getUserById(uid:String):Task<DocumentSnapshot>{
        return userCollection.document(uid).get()          //returns user information from user id from firestore
    }
}