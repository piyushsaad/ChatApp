package com.example.mysocial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocial.modelPost.ModelDao
import com.example.mysocial.modelPost.Post
import com.example.mysocial.modelPost.PostDetail
import com.example.mysocial.recycleView.AdapterR
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

//coroutines are smaller unit of threads
//suspend function will suspend all work until it is completed,and can be called by coroutines
//coroutines can switch threads(context)

class MainActivity : AppCompatActivity() {
    lateinit var postDao:ModelDao
    lateinit var aDapter:AdapterR
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floatingActionButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Intent(this,signinPage::class.java).also {
                startActivity(it)
            }
            finish()
        }

              floatingButton.setOnClickListener {
                  Intent(this,PostDetail::class.java).also {
                      startActivity(it)
                  }

              }

        postDao = ModelDao()
        val postsCollections = postDao.postCollection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING) //sorted according to this query
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()


        aDapter=AdapterR(recyclerViewOptions,this)
        recycler05.adapter=aDapter
        recycler05.layoutManager=LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        aDapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        aDapter.stopListening()
    }

}