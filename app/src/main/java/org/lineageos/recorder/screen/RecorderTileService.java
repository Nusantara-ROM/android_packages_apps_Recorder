/*
 * Copyright (C) 2019 The Syberia Project
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

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.service.quicksettings.Tile;
import android.widget.Toast;

import org.lineageos.recorder.screen.OverlayService;
import org.lineageos.recorder.utils.Utils;
import org.lineageos.recorder.R;

@TargetApi(24)
public class RecorderTileService extends TileService {

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        if (checkScreenRecPermissions()) {
            return;
        }

        if (Tile.STATE_ACTIVE == getQsTile().getState())
              changeTileState(Tile.STATE_INACTIVE);
        else if (Tile.STATE_INACTIVE == getQsTile().getState())
              changeTileState(Tile.STATE_ACTIVE);
        Intent intent = new Intent(this, RecordTileReceiver.class);
        sendBroadcast(intent);

        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(closeIntent);
    }

    private void updateTile() {
        if (!isActive(this))
            changeTileState(Tile.STATE_INACTIVE);
        else
            changeTileState(Tile.STATE_ACTIVE);
    }

    private boolean isActive(Context context) {
        return OverlayService.isRunning || Utils.isScreenRecording(context);
    }

    private void changeTileState(int newState) {
        getQsTile().setState(newState);
        getQsTile().updateTile();
    }

    private boolean checkScreenRecPermissions() {
        if (!hasDrawOverOtherAppsPermission()) {
            Toast.makeText(this, R.string.dialog_permissions_overlay, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!hasStoragePermission()) {
            Toast.makeText(this, R.string.dialog_permissions_storage, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean hasDrawOverOtherAppsPermission() {
        return Settings.canDrawOverlays(this);
    }

    private boolean hasStoragePermission() {
        int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}