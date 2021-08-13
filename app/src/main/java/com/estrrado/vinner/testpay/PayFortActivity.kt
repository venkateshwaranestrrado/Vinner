package com.estrrado.vinner.testpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estrrado.vinner.R
import com.estrrado.vinner.VinnerRespository
import com.estrrado.vinner.activity.LoginActivity
import com.estrrado.vinner.data.models.request.RequestModel
import com.estrrado.vinner.helper.Constants
import com.estrrado.vinner.helper.Constants.ACCESS_TOKEN
import com.estrrado.vinner.helper.Constants.ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.BUILDINGNAME
import com.estrrado.vinner.helper.Constants.CITY
import com.estrrado.vinner.helper.Constants.CONTACTNO
import com.estrrado.vinner.helper.Constants.COUNTRY
import com.estrrado.vinner.helper.Constants.EMAIL
import com.estrrado.vinner.helper.Constants.HOUSENAME
import com.estrrado.vinner.helper.Constants.LANDMARK
import com.estrrado.vinner.helper.Constants.NAME
import com.estrrado.vinner.helper.Constants.OPERATOR_ID
import com.estrrado.vinner.helper.Constants.PINCODE
import com.estrrado.vinner.helper.Constants.ROAD_NAME
import com.estrrado.vinner.helper.Constants.STATUS
import com.estrrado.vinner.helper.Constants.SUCCESS
import com.estrrado.vinner.helper.Constants.S_ADDDRESS_TYPE
import com.estrrado.vinner.helper.Constants.S_BUILDINGNAME
import com.estrrado.vinner.helper.Constants.S_CITY
import com.estrrado.vinner.helper.Constants.S_CONTACTNO
import com.estrrado.vinner.helper.Constants.S_COUNTRY
import com.estrrado.vinner.helper.Constants.S_EMAIL
import com.estrrado.vinner.helper.Constants.S_HOUSENAME
import com.estrrado.vinner.helper.Constants.S_LANDMARK
import com.estrrado.vinner.helper.Constants.S_NAME
import com.estrrado.vinner.helper.Constants.S_PINCODE
import com.estrrado.vinner.helper.Constants.S_ROAD_NAME
import com.estrrado.vinner.helper.Constants.TOTAL_PAYABLE
import com.estrrado.vinner.helper.Preferences
import com.estrrado.vinner.helper.priceFormat
import com.estrrado.vinner.retrofit.ApiClient
import com.estrrado.vinner.vm.HomeVM
import com.estrrado.vinner.vm.MainViewModel
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.models.FortRequest
import kotlinx.android.synthetic.main.activity_main.*
import webconnect.com.webconnect.WebConnect
import webconnect.com.webconnect.listener.OnWebCallback
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * this activity is PayFort Demo
 * you need to download FORTSDK and add dependencies
 * https://docs.payfort.com/docs/mobile-sdk/build/index.html#before-starting-the-integration
 *
 * */
class PayFortActivity : AppCompatActivity(), OnWebCallback {

    var vModel: HomeVM? = null

    var address_type: String? = ""
    var housename: String? = ""
    var roadname: String? = ""
    var landmark: String? = ""
    var pincode: String? = ""
    var country_name: String? = ""
    var city: String? = ""
    var name: String? = ""
    var phone: String? = ""
    var email: String = ""
    var building: String? = ""

    var s_address_type: String? = ""
    var s_housename: String? = ""
    var s_roadname: String? = ""
    var s_landmark: String? = ""
    var s_pincode: String? = ""
    var s_country_name: String? = ""
    var s_city: String? = ""
    var s_name: String? = ""
    var s_phone: String? = ""
    var s_email: String? = ""
    var s_building: String? = ""

    var operator_id: String? = ""
    var merchant_reference = ""

    var fortCallback: FortCallBackManager? = null
    val TAG = "payTag"
    lateinit var deviceId: String

    //Demo
    val access_code = "WGG6Avj6KSL4SX4zfWGQ"
    val merchant_identifier = "879b45fb"
    val baseUrl = "https://sbpaymentservices.payfort.com/FortAPI/"
    val signature1 = "27aVEaXzC8qDf5aHJhze6o?}"
    val signature2 = "27aVEaXzC8qDf5aHJhze6o?}"
    val environment = FortSdk.ENVIRONMENT.TEST

    //Live
//    val access_code = "6lUbMI3TtImE92epfeJ1"
//    val merchant_identifier = "WaGobKuL"
//    val baseUrl = "https://paymentservices.payfort.com/FortAPI/"
//    val signature1 = "94QSRWC0rNrBtlZokOc6xe?)"
//    val signature2 = "94QSRWC0rNrBtlZokOc6xe?)"//71zW3My3/M9lT2M3aCQca6(!
//    val environment = FortSdk.ENVIRONMENT.PRODUCTION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        address_type = getIntent().getExtras()!!.getString(ADDDRESS_TYPE)
        housename = getIntent().getExtras()!!.getString(HOUSENAME)
        roadname = getIntent().getExtras()!!.getString(ROAD_NAME)
        landmark = getIntent().getExtras()!!.getString(LANDMARK)
        pincode = getIntent().getExtras()!!.getString(PINCODE)
        country_name = getIntent().getExtras()!!.getString(COUNTRY)
        city = getIntent().getExtras()!!.getString(CITY)
        name = getIntent().getExtras()!!.getString(NAME)
        phone = getIntent().getExtras()!!.getString(CONTACTNO)
        email = getIntent().getExtras()!!.getString(EMAIL) ?: "test123@gmail.com"
        building = getIntent().getExtras()!!.getString(BUILDINGNAME)

        s_address_type = getIntent().getExtras()!!.getString(S_ADDDRESS_TYPE)
        s_housename = getIntent().getExtras()!!.getString(S_HOUSENAME)
        s_roadname = getIntent().getExtras()!!.getString(S_ROAD_NAME)
        s_landmark = getIntent().getExtras()!!.getString(S_LANDMARK)
        s_pincode = getIntent().getExtras()!!.getString(S_PINCODE)
        s_country_name = getIntent().getExtras()!!.getString(S_COUNTRY)
        s_city = getIntent().getExtras()!!.getString(S_CITY)
        s_name = getIntent().getExtras()!!.getString(S_NAME)
        s_phone = getIntent().getExtras()!!.getString(S_CONTACTNO)
        s_email = getIntent().getExtras()!!.getString(S_EMAIL)
        s_building = getIntent().getExtras()!!.getString(S_BUILDINGNAME)

        operator_id = getIntent().getExtras()!!.getString(OPERATOR_ID)

        vModel = ViewModelProvider(
            this,
            MainViewModel(
                HomeVM(
                    VinnerRespository.getInstance(
                        this,
                        ApiClient.apiServices!!
                    )
                )
            )
        ).get(HomeVM::class.java)

        deviceId = FortSdk.getDeviceId(this)

        initFortCallback()

        getSdkTokenFromApi()
        //getToknSdk()

        btnBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra(STATUS, SUCCESS)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    fun getHashString(t: String): String {
        val bytes = t.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashSt = digest.fold("", { str, it -> str + "%02x".format(it) })
        return hashSt
    }

    private fun getSdkTokenFromApi() {

        val requestModel = RequestModel()
        requestModel.accessToken = Preferences.get(this, ACCESS_TOKEN)
        requestModel.countryCode = Preferences.get(this, Preferences.REGION_NAME)
        requestModel.device_id = deviceId
        requestModel.payment_status = "due"
        requestModel.payment_method = "Payfort"
        requestModel.operator_id = operator_id

        requestModel.address_type = address_type
        requestModel.housename = housename
        requestModel.road_name = roadname
        requestModel.landmark = landmark
        requestModel.pincode = pincode
        requestModel.country = country_name
        requestModel.city = city
        requestModel.name = name
        requestModel.phone = phone
        requestModel.email = email
        requestModel.building = building
        requestModel.s_address_type = s_address_type
        requestModel.s_housename = s_housename
        requestModel.s_road_name = s_roadname
        requestModel.s_landmark = s_landmark
        requestModel.s_pincode = s_pincode
        requestModel.s_country = s_country_name
        requestModel.s_city = s_city
        requestModel.s_name = s_name
        requestModel.s_phone = s_phone
        requestModel.s_email = s_email
        requestModel.s_building = s_building

        vModel?.getsdktoken(requestModel)?.observe(
            this,
            Observer {
                it?.let {
                    if (it.status == "success") {
                        it.data?.sdk_token?.let {
                            if (it.sdk_token != "") {
                                it.merchant_reference?.let {
                                    merchant_reference = it
                                }
                                it.sdk_token?.let {
                                    startPayFortSdk(it)
                                }
                            } else {
                                Toast.makeText(this, it.response_message, Toast.LENGTH_LONG).show()
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            })

    }

    private fun getToknSdk() {

        val param = linkedMapOf<String, String>()

        param.put("access_code", access_code)
        param.put("device_id", deviceId)
        param.put("language", "en")
        /**
         * you should get 'merchant_identifier' from your payfort account
         * */
        param.put("merchant_identifier", merchant_identifier)
        param.put("service_command", "SDK_TOKEN")

        val sb = StringBuilder()
        /**
         * change 'Hello' by what you got from payFort.
        you will find it in your account*/
        sb.append(signature1)
        param.forEach { (k, v) ->
            sb.append("$k=$v")
        }
        sb.append(signature2)
        param.put("signature", getHashString(sb.toString()))
        /**
         * calling api https://sbpaymentservices.payfort.com/FortAPI/paymentApi
         *
         * */
        Log.e("param", param.toString())
        //Log.e("EMAIL : ", intent.getStringExtra(Constants.EMAIL)!!)
        WebConnect.with(this, "paymentApi")
            .post()
            /**
             * put url depending on the Environment you targeting
             * */
            .baseUrl(baseUrl)
            .bodyParam(param)
            .taskId(11)
            .callback(WebCallback(this, this), ResponsePay::class.java, ErrorModel::class.java)
            .connect()

    }

    /**
     * start buy operation via SDK
     * put required keys and there values
     * */
    fun startPayFortSdk(sign: String) {
        val model = FortRequest()
        /**
         * to see response in log
         * */
        model.isShowResponsePage = false
        val hash = hashMapOf<String, String>()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        hash.put("command", "PURCHASE")
        //hash.put("command", "AUTHORIZATION")
        hash.put(
            "customer_email", if (email == "") "test123@gmail.com" else email
        )
        hash.put("currency", intent.getStringExtra(Constants.CCURRENCY)!!)
        /**
        = 10000 => 100, should be multi by some value depending on your currency, check payfort Docs fore more detail
         */

        var amount = getIntent().getExtras()!!.getString(TOTAL_PAYABLE)!!.toDouble()
        if (intent.getStringExtra(Constants.CCURRENCY) == "BHD") {
            amount = amount * 1000
        } else {
            amount = amount * 100
        }

        hash.put("amount", amount.toInt().toString())
        hash.put("language", "en")
        /**
         * merchant_reference represented purchase id, it should be unique
         * here we let user to entered as our test requirement.
         * */
//        val x = editOne.text.toString()
        hash.put("merchant_reference", merchant_reference)
        /**
         * you can also add any option key-value pairs
         * */
//        getHashString.put("customer_name", "Sam")
//        getHashString.put("customer_ip", "172.150.16.10")
//        getHashString.put("payment_option", "VISA")
//        getHashString.put("eci", "ECOMMERCE")
//        getHashString.put("order_description", "DESCRIPTION")
        hash.put("sdk_token", sign)
        //hash.put("token_name", "8A70320AF209")
        //hash.put("payment_option", "VISA")

        model.requestMap = hash.toMap()

        /**
         * start SDK
         *
         * user will input his info e.g card number...
         * then will  receive the result in callbacks bellow
         * */
        FortSdk
            .getInstance()
            .registerCallback(this, model,
                environment, 5,
                fortCallback, true, object : FortInterfaces.OnTnxProcessed {
                    override fun onSuccess(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {
                        Log.e("MutableMap", p1.toString())
                        vModel!!.PaymentResponse(
                            RequestModel(
                                accessToken = Preferences.get(this@PayFortActivity, ACCESS_TOKEN),
                                payment_status = "paid",
                                payment_method = "payfort",
                                operatorId = getIntent().getExtras()!!.getString(OPERATOR_ID),
                                address_type = getIntent().getExtras()!!.getString(ADDDRESS_TYPE),
                                housename = getIntent().getExtras()!!.getString(HOUSENAME),
                                road_name = getIntent().getExtras()!!.getString(ROAD_NAME),
                                landmark = getIntent().getExtras()!!.getString(LANDMARK),
                                pincode = getIntent().getExtras()!!.getString(PINCODE),
                                country = getIntent().getExtras()!!.getString(COUNTRY),
                                city = getIntent().getExtras()!!.getString(CITY),
                                name = getIntent().getExtras()!!.getString(NAME),
                                phone = getIntent().getExtras()!!.getString(CONTACTNO),
                                email = getIntent().getExtras()!!.getString(EMAIL),
                                building = getIntent().getExtras()!!.getString(BUILDINGNAME),
                                s_address_type = getIntent().getExtras()!!
                                    .getString(S_ADDDRESS_TYPE),
                                s_housename = getIntent().getExtras()!!.getString(S_HOUSENAME),
                                s_road_name = getIntent().getExtras()!!.getString(S_ROAD_NAME),
                                s_landmark = getIntent().getExtras()!!.getString(S_LANDMARK),
                                s_pincode = getIntent().getExtras()!!.getString(S_PINCODE),
                                s_country = getIntent().getExtras()!!.getString(S_COUNTRY),
                                s_city = getIntent().getExtras()!!.getString(S_CITY),
                                s_name = getIntent().getExtras()!!.getString(S_NAME),
                                s_phone = getIntent().getExtras()!!.getString(S_CONTACTNO),
                                s_email = getIntent().getExtras()!!.getString(S_EMAIL),
                                s_building = getIntent().getExtras()!!.getString(S_BUILDINGNAME),
                                merchant_reference = merchant_reference,
                                payment_details = "Payfort ID:" + p1?.get("fort_id")
                                    .toString() + " Payment Option:" + p1?.get("payment_option")
                                    .toString(),
                                countryCode = Preferences.get(
                                    this@PayFortActivity,
                                    Preferences.REGION_NAME
                                )

                            )
                        ).observe(this@PayFortActivity,
                            Observer {
                                if (it?.status.equals(SUCCESS)) {

                                    clSuccess.isVisible = true
                                    llTop.isVisible = true
                                    tvTransaction.text =
                                        String.format("Transaction Number : %s", p1?.get("fort_id"))
                                    tvAmount.text = String.format(
                                        "%s %s",
                                        intent.getStringExtra(Constants.CCURRENCY)!!,
                                        priceFormat(intent.getExtras()!!.getString(TOTAL_PAYABLE)!!)
                                    )
                                    tvDate.text =
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                            Date()
                                        )
                                    Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                                        override fun run() {
                                            val intent = Intent()
                                            intent.putExtra(STATUS, SUCCESS)
                                            setResult(Activity.RESULT_OK, intent)
                                            finish()
                                        }
                                    }, 10000)
                                } else {
                                    if (it?.message.equals("Invalid access token")) {
                                        startActivity(
                                            Intent(
                                                this@PayFortActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                        this@PayFortActivity.finish()
                                    }
                                }
                            })
                        Log.d(TAG, "onSuccess")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                    }

                    override fun onFailure(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {
                        Log.d(TAG, "onFailure")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        Toast.makeText(
                            this@PayFortActivity,
                            "Error: ${p1?.get("response_message")}",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    }

                    override fun onCancel(
                        p0: MutableMap<String, Any>?,
                        p1: MutableMap<String, Any>?
                    ) {
                        Log.d(TAG, "onCancel")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        finish()
                    }

                })
    }

    fun initFortCallback() {
        fortCallback = FortCallBackManager.Factory.create();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fortCallback!!.onActivityResult(requestCode, resultCode, data);
    }

    //region apis callback
    override fun <T : Any?> onSuccess(`object`: T?, taskId: Int) {
        if (`object` is ResponsePay) {
            if (`object`.sdk_token.isEmpty()) {
                Log.e("123", `object`.response_message)
                Toast.makeText(this, "Error: ${`object`.response_message}", Toast.LENGTH_LONG)
                    .show()
            } else {
                startPayFortSdk(`object`.sdk_token)
            }
        }
    }

    override fun <T : Any?> onError(`object`: T?, error: String?, taskId: Int) {
        Toast.makeText(this, "onError is $error", Toast.LENGTH_SHORT).show()
    }

    //endregion
}
