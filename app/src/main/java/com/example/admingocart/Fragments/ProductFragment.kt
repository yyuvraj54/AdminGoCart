package com.example.admingocart.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.admingocart.Activity.AdminMainActivity
import com.example.admingocart.Adapter.AdapterSelectedImage
import com.example.admingocart.Constants
import com.example.admingocart.Models.Product
import com.example.admingocart.R
import com.example.admingocart.Utils
import com.example.admingocart.databinding.FragmentProductBinding
import com.example.admingocart.viewModel.AdminViewModel
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {
    private lateinit var binding : FragmentProductBinding
    private val imageUris : ArrayList<Uri> = arrayListOf()

    private  val viewModel : AdminViewModel by viewModels()

    val selectedImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        val fiveImages = it.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)
        binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding  = FragmentProductBinding.inflate(layoutInflater)

        onAddButtonClicked()
        setAutoCompleteTextView()
        onImageSelectClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.addbutton.setOnClickListener {
            Utils.showDialog(requireContext(), "Uploading images...")

            val productTitle = binding.etproduct.text.toString()
            val productQuantity = binding.etquantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etstock.text.toString()
            val productStock = binding.etprice.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if(productCategory.isEmpty() || productTitle.isEmpty()  || productQuantity.isEmpty()  || productUnit.isEmpty() ||  productPrice.isEmpty() ||  productStock.isEmpty() ||  productType.isEmpty() ){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(),"Empty fields are not allowed")
                }

            }
            else if(imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(),"Please upload Images")
                }
            }
            else{
                val product = Product(
                    productTitle = productTitle,
                    productQuantity= productQuantity.toInt(),
                    productUnit= productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getUserId(),
                    productRandomId = Utils.getRandomId()

                    )

                saveImage(product)
            }

        }

    }

    private fun saveImage(product: Product) {

        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUpload.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "images Saved")
                }
                getUrls(product)
            }
        }

    }

    private fun getUrls(product: Product){
        Utils.showDialog(requireContext(), "Publishing product....")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
                val urls =it
                product.productImageUris = urls
                saveProduct(product)

            }
        }
    }

    private fun saveProduct(product: Product){
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect{
                if(it){
                    Utils.hideDialog()
                    startActivity(Intent(requireActivity(),AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Your product is live")
                }
            }
        }

    }


    private fun onImageSelectClicked() {
        binding.selectImage.setOnClickListener{
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView(){
        val units =ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category =ArrayAdapter(requireContext(),
            R.layout.show_list,
            Constants.allProductsCategory
        )
        val product_type =ArrayAdapter(requireContext(),
            R.layout.show_list,
            Constants.allProductTypes
        )

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(product_type)
        }
    }
}