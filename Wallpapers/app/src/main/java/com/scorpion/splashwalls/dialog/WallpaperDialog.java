package com.scorpion.splashwalls.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.scorpion.splashwalls.R;

public class WallpaperDialog extends DialogFragment {

    public interface WallpaperDialogListener {
        void onCancel();
    }

    private WallpaperDialogListener listener;
    private boolean downloadFinished = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wallpaper, null, false);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.setting_wallpaper)
                .setNegativeButton(R.string.cancel, null)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    public void setListener(WallpaperDialogListener listener) {
        this.listener = listener;
    }

    public void setDownloadFinished(boolean downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (!downloadFinished && listener != null) {
            listener.onCancel();
        }
        downloadFinished = false;
    }

}
