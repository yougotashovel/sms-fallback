package com.smsrelayshit.smsrs;
/**
 * Created by ryan on 5/13/17.
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

public class smsRelay {
    private Activity app;
    public static final String ACTION_SMS_SENT = "com.techblogon.android.apis.os.SMS_SENT_ACTION";
    public void setActivity(Activity application){
        app = application;
    }
    public String request(String endPoint, String method, String body) {
        String id = UUID.randomUUID().toString();
        JSONObject message = new JSONObject();
        try {
            message.put("m", method);
            message.put("b", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] messages = addHeader(divideMessage(message.toString(), 100), id);
        SmsManager sms = SmsManager.getDefault();
        sendMultiple(endPoint, messages, app);
        return id;
    }
    private static String[] divideMessage(String message, int size) {
        List<String> messages = new ArrayList<>();
        int length = message.length();
        for (int i = 0; i < length; i += size)
        {
            messages.add(message.substring(i, Math.min(length, i + size)));
        }
        return messages.toArray(new String[0]);
    }
    private String[] addHeader(String[] messages, String header) {
        messages[0] =  header + "." + messages.length + "." + messages[0];
        String head = header.substring(header.length() - 4);
        for (int i = 1; i < messages.length; i++) {
            messages[i] = head + "." + i + "." + messages[i];
        }
        ;
        return messages;
    }
    public void sendMultiple(String ep, String[] mess, Activity app) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (int i = 0; i < mess.length;  i++) {
                smsManager.sendTextMessage(ep, null, mess[i], PendingIntent.getBroadcast(
                        app, 0, new Intent(ACTION_SMS_SENT), 0), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getResponse(String ep) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        Uri uri = Uri.parse(SMS_URI_INBOX);
        String[] projection = new String[]{"_id", "address", "body", "date",};
        Cursor cur = app.getContentResolver().query(uri, null, "address='+12035338331'", null, "date desc");
        if (cur.moveToFirst()) {
            int index_Address = cur.getColumnIndex("body");
            smsBuilder.append(cur.getString(index_Address));
            if (!cur.isClosed()) {
                cur.close();
                cur = null;
            }
        } else {
            smsBuilder.append("no result!");
        }
        String resp = smsBuilder.toString();
        String[] res = resp.split("\\.");
        byte[] decoded = Base64.decode(res[2], 0);
        String dec = new String(decoded);
        return new String(decoded);
    }
}