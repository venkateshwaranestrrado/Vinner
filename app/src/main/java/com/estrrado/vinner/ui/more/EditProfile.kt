package com.estrrado.vinner.ui.more

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.VinnerActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.*
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Helper.checkIfPermissionsGranted
import com.estrrado.vinner.helper.Helper.getImageUri
import com.estrrado.vinner.helper.Helper.getRealPathFromURI
import com.estrrado.vinner.helper.Helper.showSettingspermissionDialog
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MultipartBody
import java.io.*

class EditProfile : Fragment() {
    var vModel: HomeVM? = null
    private var editable = false
    private var imageUri: Uri? = null
    val neededPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        activity,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.edit_profile, container, false)
        // val textView: TextView = root.findViewById(R.id.text_home)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VinnerActivity).close()
        initControll()
        getProfile()
        textView5.text = "Profile"
        ivprofilephoto.setOnClickListener {
            getImage()
        }
    }

    private fun initControll() {

        ivprofilephoto.setOnClickListener {
            getImage()
        }


        btnSubmit.setOnClickListener(object : ClickListener() {
            override fun onOneClick(v: View) {
                if (Helper.isNetworkAvailable(activity!!)) {
                    if (validate(
                            arrayOf(
                                name,
                                housename,
                                area,
                                post,
                                city,
                                mobile,
                                email
                            )
                        )
                    ) {
                        var multiImg: MultipartBody.Part? = null
                        imageUri?.let { fileUri ->

                            multiImg = getMultipartImage(fileUri, "profile_pic", activity)
                        }
                        progressprofile.visibility = View.VISIBLE
                        vModel?.getUpdatedProfile(
                            RequestModel(
                                accessToken = Preferences.get(activity, ACCESS_TOKEN),
                                name = name.text.toString(),
                                address1 = housename.text.toString(),
                                address2 = area.text.toString(),
                                post = post.text.toString(),
                                profile_pic = multiImg,
                                state = city.text.toString(),
                                mobile = mobile.text.toString(),
                                email = email?.text.toString(),
                                city = city?.text.toString()
                            )
                        )?.observe(viewLifecycleOwner, Observer {
                            progressprofile.visibility = View.GONE
                            Validation.printToast(requireContext(), it!!.message.toString())
                            if (it.status.equals(SUCCESS)) {
                                getProfile()
                            }
                        })
                    }
                }

            }
        })
    }


    private fun getProfile() {
        if (Helper.isNetworkAvailable(requireContext())) {
            progressprofile.visibility = View.VISIBLE
            vModel!!.getProfile(
                RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))
            ).observe(requireActivity(),
                Observer {
                    progressprofile.visibility = View.GONE
                    if (it!!.status == "success") {
                        Glide.with(this)
                            .load(it.data!!.path)
                            .thumbnail(0.1f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivprofilephoto)

                        Helper.hideKeyboard(activity)
                        name.setText(it.data!!.name)
                        housename.setText(it.data.address1)
                        area.setText(it.data.address2)
                        post.setText(it.data.post)
                        city.setText(it.data.city)
                        mobile.setText(it.data.country_code + it.data.mobile)
                        email.setText(it.data.email)
                        ProfileName.setText(it.data.name)
                    }
                })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            10003 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getImage()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun validate(elements: Array<EditText>): Boolean {
        val boolean: ArrayList<Boolean> = ArrayList()
        for (i in 0 until elements.size) {
            boolean.add(hasText(elements[i]))
        }
        return !boolean.contains(false)
    }

    private fun hasText(editText: EditText?): Boolean {
        if (editText != null) {
            val text = editText.text.toString().trim { it <= ' ' }
            editText.error = null
            if (text.isEmpty()) {
                editText.error = "Mandatory Field"
                return false
            }
        } else {
            return false
        }
        return true
    }

    private fun getImage() {

        if (checkIfPermissionsGranted(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        ) {
            TedBottomPicker.with(activity)
                //.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2)
//                .setSelectedUri(selectedUri) //.showVideoMedia()
                .setPeekHeight(1200)
                .show({ uri ->
                    if (uri != null) {
                        val bitmap = BitmapFactory.decodeStream(
                            requireContext().getContentResolver().openInputStream(uri)
                        )
                        ivprofilephoto.setImageBitmap(bitmap)
                        imageUri = uri
                    }
                })

        } else {
            ActivityCompat.requestPermissions(requireActivity(), neededPermissions, 10003)
        }

    }

}
