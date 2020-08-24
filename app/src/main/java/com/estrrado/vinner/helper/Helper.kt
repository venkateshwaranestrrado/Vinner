package com.estrrado.vinner.helper

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity


import com.estrrado.vinner.R
import com.estrrado.vinner.data.models.Region
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream


object Helper {

    private var dialog: Dialog? = null

  /*  fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
           // CheckInternet().execute().get()
        } else {
            false
        }
    }*/

    fun printT(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

   /* fun showLoading(context: FragmentActivity?) {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        dialog = Dialog(context!!, R.style.DialogTheme)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.popup_loading)
        dialog!!.show()
    }*/

    /*fun getRealPath(selectedImage: Uri, activity: FragmentActivity): String {

      *//*  var path = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = activity!!.contentResolver.query(selectedImage, path, null, null, null)
        c.moveToFirst()
        var columnIndex = c.getColumnIndex(path[0])
        var picturePath = c.getString(columnIndex)
        c.close()
        return picturePath*//*
        return null
    }*/

   /* fun hideLoading() {
        if (dialog?.isShowing!!) {
            dialog!!.dismiss()
        }
    }*/

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "ping", null)
        return Uri.parse(path)
    }

    fun isUserIsLogined(activity: FragmentActivity): Boolean {
       /* if (AppPreference?.getInstance().readBoolean(IS_LOGGED)) {
            return true
        }

        var intent = Intent(activity, LaunchActivity::class.java)
        intent.putExtra(DATA, LOGIN_PAGE)
        activity.startActivity(intent)
*/
        return false
    }

    fun setLocation(spinner:Spinner, context: Context) {
        val region:ArrayList<String> = ArrayList<String>()
        for(i in 0 until regions!!.size){
            region.add(regions.get(i).countryName!!)
        }

        val aa = ArrayAdapter(context, R.layout.spinner_item, region!!.toTypedArray())
        spinner.adapter = aa
    }
}