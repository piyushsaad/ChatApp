package com.example.mysocial.modelPost

import com.example.mysocial.model.User
import com.example.mysocial.model.UserDao
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ModelDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection=db.collection("post")
    val auth=FirebaseAuth.getInstance()

    fun addPost(postText:String){
        /* current user from firebase auth*/
        val currTime=System.currentTimeMillis()
        val currentUser=auth.currentUser!!.uid  //if no user present then app will crash
        /*to get firestore data of this user*/
        GlobalScope.launch {
            val dao = UserDao()
            val user = dao.getUserById(currentUser).await().toObject(User::class.java)!!    //parse task to user
            val post=Post(postText,user,currTime)
            postCollection.document().set(post)  //document will auto generate with unique id
        }

    }
    fun getPostByPostId(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }
    fun updateLike(postId:String){
        val currentUser=auth.currentUser!!.uid

        GlobalScope.launch {
             val postDetails = getPostByPostId(postId).await().toObject(Post::class.java)
             val isLiked:Boolean= postDetails!!.likedBy.contains(currentUser)

            if(isLiked){
                postDetails.likedBy.remove(currentUser)

            }
            else{
                postDetails.likedBy.add(currentUser)
            }
            postCollection.document(postId).set(postDetails)

        }

    }

}