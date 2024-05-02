package com.example.admingocart.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.admingocart.Constants
import com.example.admingocart.Models.Categories
import com.example.admingocart.Models.Product
import com.example.admingocart.databinding.ItemViewProductBinding
import com.example.admingocart.databinding.ItemViewProductCategoriesBinding

class AdapterProduct:RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemViewProductBinding ): RecyclerView.ViewHolder(binding.root)


    /// check this diffUtils this is for recycle view list of images // agar koi image frr se load ho rahi h to
    val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffutil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product =differ.currentList[position]


        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()

            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!) {
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }
            ivImageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle

            val quantity = product.productQuantity.toString() + product.productUnit
            tvProductQuantity.text = quantity

            tvProductPrice.text = "₹"+product.productPrice
        }




    }


}