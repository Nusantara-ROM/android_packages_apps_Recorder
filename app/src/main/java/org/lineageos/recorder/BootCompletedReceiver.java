package org.lineageos.recorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.lineageos.recorder.utils.Utils;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.refreshShowTouchesState(context);
    }
}