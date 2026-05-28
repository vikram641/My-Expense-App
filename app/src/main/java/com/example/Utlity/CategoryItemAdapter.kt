package com.example.Utlity
//
//import android.annotation.SuppressLint
//import com.android.volley.toolbox.ImageLoader
//import com.example.data.model.CatDataResponse
//import com.example.expense.R
//import com.example.expense.databinding.ItemCategoryBinding
//
//
//class CategoryItemAdapter : BaseRecyclerAdapter<ItemCategoryBinding, CatDataResponse>() {
//
//    override val layoutId: Int get() = R.layout.item_home_banner_bg
//
//    @SuppressLint("SetTextI18n")
//    override fun bind(holder: ViewHolder, item: CatDataResponse, position: Int) {
//        ImageLoader.loadImage(imageUrl = item.bgBannerUrl, imageView = holder.binding.imageView)
//    }
//
//}