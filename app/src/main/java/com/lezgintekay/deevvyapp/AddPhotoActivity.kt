package com.lezgintekay.deevvyapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.net.URI
import java.sql.Timestamp
import java.time.Instant.now
import java.util.UUID

class AddPhotoActivity : AppCompatActivity() {

    var pickedImage : Uri? = null
    var pickedBitmap : Bitmap? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun goBack(view: View){
        val intent = Intent(this,NewsActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun addPhoto(view: View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)


        }else{
            val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.size >= 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2 && resultCode == Activity.RESULT_OK && data!=null){

            pickedImage= data.data

            if (pickedImage !=null) {
                if (Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(this.contentResolver,pickedImage!!)
                    pickedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView6.setImageBitmap(pickedBitmap)

                }else{
                    pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedImage)
                    imageView6.setImageBitmap(pickedBitmap)
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



    fun share(view: View) {

        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if(pickedImage != null) {
            imageReference.putFile(pickedImage!!).addOnSuccessListener {

                val uploadededImageReference = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadededImageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val userDescription = descriptionText.text.toString()
                    val time = com.google.firebase.Timestamp.now()
                    //database

                    val postHashMap = hashMapOf<String , Any>()
                    postHashMap.put("imageurl",downloadUrl)
                    postHashMap.put("useremail",currentUserEmail)
                    postHashMap.put("description",userDescription)
                    postHashMap.put("time",time)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if(it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }


    }

}