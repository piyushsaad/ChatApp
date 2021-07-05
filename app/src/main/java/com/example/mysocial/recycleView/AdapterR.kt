package com.example.mysocial.recycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocial.R
import com.example.mysocial.Utilities.Utils
import com.example.mysocial.modelPost.ModelDao
import com.example.mysocial.modelPost.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

//this adapter automatically listen change in data and update it automatically
class AdapterR(options: FirestoreRecyclerOptions<Post>,context:Context):FirestoreRecyclerAdapter<Post,AdapterR.PostViewViewHolder>(options) {

 class PostViewViewHolder(item:View):RecyclerView.ViewHolder(item){  //we can also keep it empty and use itemView.findViewById in on binding
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return PostViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewViewHolder, position: Int, model: Post) {  //here we get model
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName1
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl1).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        holder.likeButton.setOnClickListener {
            val index=holder.adapterPosition
            val postId=snapshots.getSnapshot(index).id
            val Dao=ModelDao()
            Dao.updateLike(postId)

        }
        val currUser=FirebaseAuth.getInstance().currentUser?.uid
        val isLiked=model.likedBy.contains(currUser)
        if(!isLiked)
         holder.likeButton.setImageResource(R.drawable.empty_heart)
        else
            holder.likeButton.setImageResource(R.drawable.full_heart)
    }

}