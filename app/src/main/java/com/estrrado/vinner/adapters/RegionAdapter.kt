package com.estrrado.vinner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.estrrado.vinner.R
import com.estrrado.vinner.data.RegionSpinner


class RegionAdapter(val context: Context, var dataSource: List<RegionSpinner>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_region_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.region.text = dataSource.get(position).name
        vh.code.text = dataSource.get(position).code

        val id = context.resources.getIdentifier(dataSource.get(position).url, "drawable", context.packageName)
        vh.img.setBackgroundResource(id)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val region: TextView
        val code: TextView
        val img: ImageView

        init {
            region = row?.findViewById(R.id.region) as TextView
            img = row?.findViewById(R.id.img) as ImageView
            code = row?.findViewById(R.id.region_code) as TextView
        }
    }

}