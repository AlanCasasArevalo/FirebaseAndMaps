package com.example.acasasar.firebaseandmaps;

import android.os.Bundle;
import android.util.Log;

import net.indigitall.pushsdk.broadcast.PushBroadcastReceiver;
import net.indigitall.pushsdk.model.PushModel;

public class SampleBroadcast extends PushBroadcastReceiver {
    @Override
    public void onIndigitallPushReceived(PushModel push) {
        Log.d("SampleBroadcast", "IndigitallPush");
    }

    @Override
    public void onOtherPushReceived(Bundle data) {
        Log.d("SampleBroadcast", "OtherPush");
    }
}