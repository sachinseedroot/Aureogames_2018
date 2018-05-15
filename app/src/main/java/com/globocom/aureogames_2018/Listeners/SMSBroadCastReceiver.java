package com.globocom.aureogames_2018.Listeners;

/**
 * Created by Ritu on 22-01-2018.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import com.globocom.aureogames_2018.Utilities.AppConstants;

public class SMSBroadCastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                sms_str += "\r\nMessage: ";
                sms_str += smsm[i].getMessageBody().toString();
                sms_str+= "\r\n";

                String Sender = smsm[i].getOriginatingAddress();
                //Toast.makeText(getApplicationContext(), "Verify PIN: ", Toast.LENGTH_LONG).show();
                //Just to fetch otp sent from WNRCRP
                if(Sender.startsWith("9978") ||Sender.startsWith("+91") || Sender.startsWith("MD-GLOBCM")) {
                    Intent smsIntent = new Intent(AppConstants.OTP_receiver);
                    smsIntent.putExtra("message", sms_str);
                    smsIntent.putExtra("source",Sender);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                }else  if(Sender.startsWith("1111") ||Sender.startsWith("+91") || Sender.startsWith("MD-GLOBCM")) {
                    Intent smsIntent = new Intent(AppConstants.OTP_receiver);
                    smsIntent.putExtra("message", sms_str);
                    smsIntent.putExtra("source",Sender);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                } else {
                    Intent smsIntent = new Intent(AppConstants.OTP_receiver);
                    smsIntent.putExtra("message", sms_str);
                    smsIntent.putExtra("source",Sender);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }
            }
        }
    }
}