package com.example.admingocart.viewModel

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.admingocart.Models.Product
import com.example.admingocart.Utils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import kotlin.math.log

class AdminViewModel: ViewModel() {

    private val _isImagesUploaded = MutableStateFlow(false)
    var isImagesUpload : StateFlow<Boolean> = _isImagesUploaded

    private val _downloadUrls = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadedUrls : StateFlow<ArrayList<String?>> = _downloadUrls


    private val _isProductSaved = MutableStateFlow(false)
    var isProductSaved : StateFlow<Boolean> = _isProductSaved


    fun saveImageInDB(imageUri : ArrayList<Uri>){
        val downloadUrls = ArrayList<String?>()

        imageUri.forEach {
            val imageRef = FirebaseStorage.getInstance().reference.child(Utils.getUserId()).child("Images")
                .child(UUID.randomUUID().toString())
            imageRef.putFile(it).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener {
                val url = it.result
                downloadUrls.add(url.toString())


                if(downloadUrls.size  == imageUri.size){
                    _isImagesUploaded.value = true
                    _downloadUrls.value = downloadUrls
                }

            }

        }



    }
    fun saveProduct(product:Product){
        FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/${product.productRandomId}").setValue(product)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${product.productRandomId}").setValue(product)
                    .addOnSuccessListener {
                        FirebaseDatabase.getInstance().getReference("Admins").child("ProductType/${product.productRandomId}").setValue(product)
                            .addOnSuccessListener {
                                _isProductSaved.value =true
                            }
                    }
            }

    }
}