package com.example.admingocart

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.admingocart.Adapter.AdapterSelectedImage
import com.example.admingocart.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private lateinit var binding : FragmentProductBinding
    private val imageUris : ArrayList<Uri> = arrayListOf()

    val selectedImage = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        val fiveImages = it.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)
        binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentProductBinding.inflate(layoutInflater)


        onAddButtonClicked()
        setAutoCompleteTextView()
        onImageSelectClicked()

        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.addbutton.setOnClickListener {

        }

    }

    private fun onImageSelectClicked() {
        binding.selectImage.setOnClickListener{
            selectedImage.launch("image/*")
        }
    }

    private fun setAutoCompleteTextView(){
        val units =ArrayAdapter(requireContext(),R.layout.show_list,Constants.allUnitsOfProducts)
        val category =ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductsCategory)
        val product_type =ArrayAdapter(requireContext(),R.layout.show_list,Constants.allProductTypes)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(product_type)
        }
    }
}