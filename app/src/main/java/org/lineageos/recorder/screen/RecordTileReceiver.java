/*
 * Copyright (C) 2019 Syberia Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lineageos.recorder.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import org.lineageos.recorder.screen.OverlayService;
import org.lineageos.recorder.screen.ScreencastService;
import org.lineageos.recorder.utils.Utils;

public class RecordTileReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        toggleScreenRecord(context);
    }

    private void toggleScreenRecord(Context context) {

        // handle tile click with running overlay service:
        // if overlay already shown but not recording, we need to stop service
        if (OverlayService.isRunning && !(Utils.isScreenRecording(context))) {
             context.stopService(new Intent(context, OverlayService.class));
             return;
        }

        if (Utils.isScreenRecording(context)) {
            // Stop
            Utils.setStatus(context, Utils.UiStatus.NOTHING);
            context.startService(new Intent(ScreencastService.ACTION_STOP_SCREENCAST)
                    .setClass(context, ScreencastService.class));
        } else {
            // Start
            if (!OverlayService.isRunning) {
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(context, OverlayService.class);
                    intent.putExtra(OverlayService.EXTRA_HAS_AUDIO, isAudioAllowedWithScreen(context));
                    context.startService(intent);
                }, 500);
            }
        }
    }

    private boolean isAudioAllowedWithScreen(Context context) {
        return context.getSharedPreferences(Utils.PREFS, 0).getBoolean(Utils.PREF_SCREEN_WITH_AUDIO, false);
    }
}
