package com.lezgintekay.deevvyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var recyclerAdapter: NewsRecyclerAdapter
    var postList = ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()

        gettingData()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerAdapter = NewsRecyclerAdapter(postList)
        recyclerView.adapter = recyclerAdapter

    }

    fun gettingData() {

        database.collection("Post").orderBy("time", Query.Direction.DESCENDING  )
            .addSnapshotListener { value, error ->

            if(error !=null) {
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value !=null) {
                    if (!value.isEmpty){

                        val documents = value.documents
                        postList.clear()
                        for(document in documents) {
                            val useremail = document.get("useremail") as String
                            val description = document.get("description") as String
                            val imageUrl = document.get("imageurl") as String

                            val downloadedPost = Post(useremail,description,imageUrl)
                            postList.add(downloadedPost)
                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    fun logOut(view: View) {

        auth.signOut()

        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        finish()

    }
    fun addPhoto(view: View) {

        val intent = Intent(this, AddPhotoActivity::class.java)
        startActivity(intent)


    }
}