package com.estrrado.vinner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.estrrado.vinner.R;
import com.estrrado.vinner.VinnerRespository;
import com.estrrado.vinner.retrofit.ApiClient;
import com.estrrado.vinner.vm.HomeVM;
import com.estrrado.vinner.vm.MainViewModel;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.sdk.android.dependancies.models.FortRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

public class PayFortSdkSample extends Activity {

    private FortCallBackManager fortCallback = null;
    String deviceId = "", sdkToken = "";
    HomeVM vModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// create Fort callback instance
        fortCallback = FortCallback.Factory.create();

// Generating deviceId
        deviceId = FortSdk.getDeviceId(PayFortSdkSample.this);
        Log.d("DeviceId ", deviceId);

// prepare payment request
        String signature = getSignature();

        vModel = new ViewModelProvider(
                (ViewModelStoreOwner) this,
                new MainViewModel(
                        new HomeVM(
                                VinnerRespository.getInstance(
                                        PayFortSdkSample.this,
                                        ApiClient.INSTANCE.getApiServices()
                    )
                )
            )
        ).get(HomeVM.class);

        FortRequest fortrequest = new FortRequest();
        fortrequest.setRequestMap(collectRequestMap("PASS_THE_GENERATED_SDK_TOKEN_ HERE"));
        fortrequest.setShowResponsePage(true); // to [display/use] the SDK response page

// execute payment request
        callSdk(fortrequest);
    }

    private Map<String, Object> collectRequestMap(String sdkToken) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("command", "PURCHASE");
        requestMap.put("customer_email", "Sam @gmail.com");
        requestMap.put("currency", "SAR");
        requestMap.put("amount", "100");
        requestMap.put("language", "en");
        requestMap.put("merchant_reference", "ORD - 0000007682");
        requestMap.put("customer_name", "Sam");
        requestMap.put("customer_ip", " 172.150 .16 .10");
        requestMap.put("payment_option", "VISA");
        requestMap.put("eci", "ECOMMERCE");
        requestMap.put("order_description", "DESCRIPTION");
        requestMap.put("sdk_token", sdkToken);
        return requestMap;
    }

    private void callSdk(FortRequest fortrequest) {

        try {
            FortSdk.getInstance().registerCallback(PayFortSdkSample.this, fortrequest, FortSdk.ENVIRONMENT.TEST, 5, fortCallback, new FortInterfaces.OnTnxProcessed() {
                @Override
                public void onCancel(Map<String, Object> requestParamsMap, Map<String,
                        Object> responseMap) {
//TODO: handle me
                    Log.d("Cancelled ", responseMap.toString());
                }

                @Override
                public void onSuccess(Map<String, Object> requestParamsMap, Map<String,
                        Object> fortResponseMap) {
//TODO: handle me
                    Log.i("Success ", fortResponseMap.toString());
                }

                @Override
                public void onFailure(Map<String, Object> requestParamsMap, Map<String,
                        Object> fortResponseMap) {
//TODO: handle me
                    Log.e("Failure ", fortResponseMap.toString());
                }

            });
        } catch (Exception e) {
            Log.e("execute Payment", "call FortSdk", e);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fortCallback.onActivityResult(requestCode, resultCode, data);
    }

    public static String getSignature() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("command", "PURCHASE");
        requestMap.put("merchant_reference", "Test010");
        requestMap.put("amount", "1000");
        requestMap.put("access_code", "SILgpo7pWbmzuURp2qri");
        requestMap.put("merchant_identifier", "MxvOupuG");
        requestMap.put("currency", "USD");
        requestMap.put("language", "en");
        requestMap.put("customer_email", "test@gmail.com");

//order by key
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestMap = requestMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }

        String requestString = "PASS";
        for (Map.Entry<String, Object> entry : requestMap.entrySet())
            requestString += entry.getKey() + "=" + entry.getValue();
        requestString += "PASS";

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashed = new byte[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hashed = digest.digest(requestString.getBytes(StandardCharsets.UTF_8));
        }

        return DatatypeConverter.printHexBinary(hashed);
    }
}
