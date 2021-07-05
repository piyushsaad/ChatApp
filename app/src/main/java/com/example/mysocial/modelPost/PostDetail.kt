package com.example.mysocial.modelPost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysocial.MainActivity
import com.example.mysocial.R
import com.example.mysocial.model.UserDao
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetail : AppCompatActivity() {
    lateinit var postDao: ModelDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val Posttext=posteditText3.text
        postDao=ModelDao()

            postButton.setOnClickListener {
                  if(Posttext!=null) {
                      postDao.addPost(Posttext.toString())

                      finish()
                  }
            }


    }
}