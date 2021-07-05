package com.example.mysocial.modelPost

import com.example.mysocial.model.User


data class Post(
   val text:String="",
   val createdBy: User =User(),  //user class which we created
   val createdAt:Long=0L,
   val likedBy:ArrayList<String> = ArrayList() //list of uid
)