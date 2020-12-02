package com.estrrado.vinner.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


import com.estrrado.vinner.R
import com.estrrado.vinner.`interface`.AlertCallback
import com.estrrado.vinner.data.models.Region
import com.estrrado.vinner.helper.Constants.regions
import kotlinx.android.synthetic.main.dialog_signout.*
import kotlinx.android.synthetic.main.moree_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream


object Helper {

    private var dialog: Dialog? = null

    //
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
            // CheckInternet().execute().get()true
            true
        } else {
            false
        }
    }

    fun convertBitmap(bitmap: Bitmap): String {
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        var b: ByteArray = baos.toByteArray()
        var temp = ""
        try {
            System.gc()
            temp = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            b = baos.toByteArray()
            temp = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
            Log.e("EWN", "Out of memory error catched")
        }
        return temp
    }

    fun getImageUri(inContext: FragmentActivity?, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext?.contentResolver, inImage, "ping", null)
        return Uri.parse(path)
    }

    fun printAny(request: Any?) {
//        if (BaseApp.appInstance?.getAppEnvironment() == PayEnvironment.STAGING) {
//            try {
//                println(GsonBuilder().setPrettyPrinting().create().toJson(request))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }

    fun checkIfPermissionsGranted(context: FragmentActivity?, array: Array<String>): Boolean {
        if (context != null && !context.isFinishing) {
            val valid = arrayOfNulls<Boolean>(array.size)
            for (i in 0 until array.size) valid[i] = false
            for (i in 0 until array.size) {
                valid[i] = ContextCompat.checkSelfPermission(
                    context, array[i]
                ) == PackageManager.PERMISSION_GRANTED
            }
            return !valid.contains(false) && !valid.contains(null)
        } else {
            return false
        }
    }

    fun getRealPathFromURI(activity: FragmentActivity?, uri: Uri): String {
        /*val cursor = activity?.contentResolver?.query(uri, null, null, null, null) as Cursor
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val ksnvn = cursor.getString(idx)
        cursor.close()*/
        var ksnvn = ""
        activity?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            ksnvn = cursor.getString(idx)
        }
        return ksnvn
    }

    fun showSettingspermissionDialog(
        activity: FragmentActivity?, extra: String?, title: String?, content: String?
    ) {
        AlertDialog.Builder(activity).setTitle(title).setMessage(content).setCancelable(true)
            .setPositiveButton("ok") { p0, p1 ->
                p0.dismiss()
                activity?.startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", activity.packageName, null)
                    activity.intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.intent?.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    activity.intent?.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                })
            }.show()
    }

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
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "ping", null)
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

    fun setLocation(spinner: Spinner, context: Context) {
        val region: ArrayList<String> = ArrayList<String>()
        for (i in 0 until regions!!.size) {
            region.add(regions.get(i).countryName!!)
        }

        val aa = ArrayAdapter(context, R.layout.spinner_item, region!!.toTypedArray())
        spinner.adapter = aa
    }

    fun hideKeyboard(activity: Activity?) {
        try {
            activity?.let {
                val inputManager =
                    it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val currentFocusedView = it.currentFocus
                if (currentFocusedView != null) {
                    inputManager.hideSoftInputFromWindow(
                        currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showAlert(msg: String, from: Int, alertCallback: AlertCallback, context: Context) {
        val mbuilder = AlertDialog.Builder(context)
        val dialogview =
            LayoutInflater.from(context).inflate(R.layout.dialog_signout, null, false);
        mbuilder.setView(dialogview)
        val malertDialog = mbuilder.show()
        malertDialog.txt_msg.setText(msg)
        malertDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        malertDialog?.yes?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                alertCallback.alertSelected(true)
                malertDialog.cancel()
            }

        })
        malertDialog?.no?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                alertCallback.alertSelected(false)
                malertDialog.cancel()
            }

        })
    }

}