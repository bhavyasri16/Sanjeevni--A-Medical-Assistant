package com.finalproject.it.sanjeevni.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.finalproject.it.sanjeevni.activities.bloodBank.BloodDonationRequests;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class NotificationHandler implements OneSignal.NotificationOpenedHandler {

    private Context myContext;

    public NotificationHandler(Context context){
        this.myContext=context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        //Log.i("OSNotificationPayload", "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString());

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null) ;
                //Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) ;
            //Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            Intent intent = new Intent(myContext, BloodDonationRequests.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
            myContext.startActivity(intent);


    }
}
