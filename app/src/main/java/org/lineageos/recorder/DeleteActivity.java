package org.lineageos.recorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import org.lineageos.recorder.utils.LastRecordHelper;

public class DeleteActivity extends Activity {
    public static final String EXTRA_LAST_SOUND = "lastSoundItem";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        deleteLastItem(getIntent().getBooleanExtra(EXTRA_LAST_SOUND, false));
    }

    private void deleteLastItem(boolean isSound) {
        Uri uri = LastRecordHelper.getLastItemUri(this, isSound);
        AlertDialog dialog = LastRecordHelper.deleteFile(this, uri, isSound);
        dialog.setOnDismissListener(d -> finish());
        dialog.show();
    }
}
