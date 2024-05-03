package com.example.admingocart.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.admingocart.Adapter.AdapterProduct
import com.example.admingocart.Adapter.CategoriesAdapter
import com.example.admingocart.Constants
import com.example.admingocart.Models.Categories
import com.example.admingocart.databinding.FragmentHomeBinding
import com.example.admingocart.viewModel.AdminViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
private  lateinit var binding:FragmentHomeBinding
    private  val viewModel : AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentHomeBinding.inflate(layoutInflater)

        setCategories()
        getAllProducts("All")

        return binding.root
    }

    private fun getAllProducts(category: String) {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllTheProduct(category).collect{

                if(it.isEmpty()){
                    binding.rvProduct.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
                else {
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                    val adapterProduct = AdapterProduct()
                    binding.rvProduct.adapter = adapterProduct
                    adapterProduct.differ.submitList(it)
                    binding.shimmerViewContainer.visibility = View.GONE
            }
        }

    }

    fun onCategoryClicked(categories: Categories){
        getAllProducts(categories.category)
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()

        for (i in 0 until Constants.allProductTypesIcons.size) {
            categoryList.add(Categories(Constants.allProductsCategory[i], Constants.allProductTypesIcons[i]))
        }
        binding.rvCategory.adapter = CategoriesAdapter(categoryList,::onCategoryClicked)
    }
}
