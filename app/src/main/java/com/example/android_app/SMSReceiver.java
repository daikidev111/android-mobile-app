package com.example.android_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {

        // since it uses Telephony class that has only one method to retrieve message from the intent
        // it has to be converted into an array and execute for loop
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage currentMessage : messages) {
            String message = currentMessage.getDisplayMessageBody();
            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER);
            msgIntent.putExtra(SMS_MSG_KEY, message);
            context.sendBroadcast(msgIntent);
        }
    }
}
