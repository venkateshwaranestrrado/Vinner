package com.estrrado.vinner.ui.more

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.estrrado.vinner.helper.Helper.checkIfPermissionsGranted
import com.estrrado.vinner.helper.Helper.getImageUri
import com.estrrado.vinner.helper.Helper.getRealPathFromURI
import com.estrrado.vinner.helper.Helper.showSettingspermissionDialog
import com.estrrado.vinner.helper.Preferences.NAME
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.fragment_address_update.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.moree_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.*

class EditProfile : Fragment() {
    var vModel: HomeVM? = null
    private var editable = false
    private var image: String? = null
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
        cam.setOnClickListener {
            openCamera()
        }
        ivprofilephoto.setOnClickListener {
            OpenLibrary()
        }

        edit.setOnClickListener {
            editable = !editable
            setEditable(editable)
        }
        progressprofile.visibility = View.VISIBLE

        // TODO: Use the ViewModel
    }

    private fun initControll() {

        cam.setOnClickListener {
            openCamera()
        }
        ivprofilephoto.setOnClickListener {
            OpenLibrary()
        }

        edit.setOnClickListener {
            editable = !editable
            setEditable(editable)
        }


        btnSubmit.setOnClickListener(object : ClickListener() {
            override fun onOneClick(v: View) {
                progressprofile.visibility = View.VISIBLE
                if (Helper.isNetworkAvailable(activity!!)) {
                    if (validate(
                            arrayOf(
                                name,
                                housename,
                                area,
                                post,
                                state,
                                mobile,
                                email, district
                            )
                        )
                    )
                        vModel?.getUpdatedProfile(
                            RequestModel(
                                accessToken = Preferences.get(activity, ACCESS_TOKEN),
                                name = name.text.toString(),
                                address1 = housename.text.toString(),
                                address2 = area.text.toString(),
                                post = post.text.toString(),
                                profile_pic = image,
                                state = state.text.toString(),
                                mobile = mobile.text.toString(),
                                email = email?.text.toString(),
                                district = district?.text.toString()
                            )
                        )?.observe(viewLifecycleOwner, Observer {

                            if (it != null) {
                                Validation.printToast(requireContext(), "Successful")
                                getProfile()
                            }
                        })

                    progressprofile.visibility = View.GONE
                    editable = false
                    setEditable(editable)
                } else {
                    progressprofile.visibility = View.GONE
                }

            }
        })
    }


    private fun getProfile() {
        if (Helper.isNetworkAvailable(requireContext())) {
            vModel!!.getProfile(
                RequestModel(accessToken = Preferences.get(activity, ACCESS_TOKEN))

            ).observe(requireActivity(),
                Observer {
                    if (it!!.status == "success") {

                        progressprofile.visibility = View.GONE
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
                        state.setText(it.data.state)
                        mobile.setText(it.data.mobile+it.data.mobile)
                        email.setText(it.data.email)
                        district.setText(it.data.district)
                        ProfileName.setText(it.data.name)


                    }
                })
        }
    }


    private fun setEditable(editable: Boolean) {
        if (!editable) {
            edit.setImageResource(android.R.drawable.ic_menu_edit)
        } else {
            edit.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        }

        name.isClickable = editable
        name.isEnabled = editable
        name.isFocusableInTouchMode = editable
        housename.isClickable = editable
        housename.isEnabled = editable
        housename.isFocusableInTouchMode = editable
        area.isClickable = editable
        area.isEnabled = editable
        area.isFocusableInTouchMode = editable
        post.isEnabled = editable
        post.isClickable = editable
        state.isClickable = editable
        state.isEnabled = editable
        state.isFocusableInTouchMode = editable
        mobile.isEnabled = editable
        mobile.isClickable = editable
        mobile.isFocusableInTouchMode = editable
        email.isEnabled = editable
        email.isClickable = editable
        email.isFocusableInTouchMode = editable
        district.isEnabled = editable
        district.isClickable = editable
        district.isFocusableInTouchMode = editable


    }

    private fun openCamera() {
        activity?.let {
            if (checkIfPermissionsGranted(
                    it,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            ) {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.CAMERA
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showSettingspermissionDialog(
                        activity,
                        "",
                        "Grant Permission",
                        "You need permission to open camera"
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        100
                    )
                }
            }
        }


    }

    private fun OpenLibrary() {
        activity?.let {
            if (checkIfPermissionsGranted(
                    it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            ) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select File"), 1)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        it, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showSettingspermissionDialog(
                        activity, "", "Grant Permission", "You need permission open file storage"
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                0 -> fromCamera(data)
                1 -> fromLib(data)
            }
            Activity.RESULT_CANCELED -> Log.e("TAG", "Selecting picture cancelled")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }
            }
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OpenLibrary()
                }
            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fromCamera(data: Intent?) {
        val thumbnail = data!!.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        setImage(thumbnail)
    }

    private fun fromLib(data: Intent?) {
//        val thumbnail = data!!.extras!!.get("data") as Bitmap
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        setImage(bm)
    }

    private fun setImage(bm: Bitmap?) {
        try {
            bm?.let {
                ivprofilephoto.setImageBitmap(bm)
                Helper.printAny(getRealPathFromURI(activity, getImageUri(activity, bm)))
                image = Helper.convertBitmap(bm)
                Validation.printToast(requireContext(), image.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

}
