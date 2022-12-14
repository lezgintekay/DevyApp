package com.lezgintekay.deevvyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class NewsRecyclerAdapter(val postList : ArrayList<Post> ):RecyclerView.Adapter<NewsRecyclerAdapter.PostHolder>() {

    class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {

        return postList.size

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.itemView.recyler_row_user_email.text = postList[position].userEmail
        holder.itemView.recyler_row_description.text = postList[position].description
        Picasso.get().load(postList[position].imageUrl).into(holder.itemView.recyler_row_image_view)

    }


}