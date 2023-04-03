package cn.lancet.navigation.cloudstorage

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.io.FileInputStream

class CloudStorageManager {

    companion object {

        fun init(context: Context){
            val storage = Firebase.storage

            val storageReference = storage.reference

            val imageReference = storageReference.child("images")

            val spaceRef = storageReference.child("images/space.jpg")

            val file = Uri.fromFile(File("path"))

            val metadata = storageMetadata {
                contentType = "image/jpeg"
            }

            val uploadTask = storageReference.child("")
                .putFile(file, metadata)

            uploadTask.addOnProgressListener {

            }.addOnPausedListener {

            }.addOnFailureListener {

            }.addOnSuccessListener {

            }

        }

    }

}