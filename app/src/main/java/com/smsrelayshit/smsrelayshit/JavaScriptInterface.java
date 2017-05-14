package com.smsrelayshit.smsrelayshit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.Toast;
import com.smsrelayshit.smsrs.smsRelay;

import org.json.JSONException;
import org.json.JSONObject;

public class JavaScriptInterface{
    Activity mActivity;
    Context mContext;
    //        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    smsRelay sr = new smsRelay();

    /**
     * Instantiate the interface and set the context
     */
    JavaScriptInterface(Activity a, Context c) {
        mContext = c;
        mActivity = a;
        sr.setActivity(mActivity);
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) throws InterruptedException {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        sr.request("12035338331", "post", "woah turtles");
    }

    @JavascriptInterface
    public String showResp(){
        String resp = "";
        try {
            JSONObject jo = new JSONObject(sr.getResponse("+12035338331"));
            if((int) jo.get("code") == 200) {
                Toast.makeText(mContext, "Order Successful", Toast.LENGTH_SHORT).show();
                resp = jo.toString();
            } else {
                resp = showResp();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;
    }
}