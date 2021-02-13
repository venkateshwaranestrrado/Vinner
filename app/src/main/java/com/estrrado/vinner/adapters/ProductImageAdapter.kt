package com.estrrado.vinner.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.estrrado.vinner.R

class ProductImageAdapter(
    private var activity: FragmentActivity,
    private var images: List<String>?
) : PagerAdapter() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout =
            LayoutInflater.from(activity).inflate(R.layout.item_pager, container, false)
        val imageView = imageLayout.findViewById(R.id.image) as ImageView

        Glide.with(activity)
            .load(images!!.get(position))
            .thumbnail(0.1f)
            .into(imageView)

        imageView.clipToOutline = true
        container.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return images!!.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}