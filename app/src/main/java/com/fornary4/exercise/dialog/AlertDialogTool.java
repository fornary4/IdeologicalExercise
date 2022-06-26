package com.fornary4.exercise.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.fornary4.exercise.R;


public class AlertDialogTool {

    public static AlertDialog buildCustomStylePopupDialogGravity(Context context, View v, int gravity, int anim) {
        return buildCustomStylePopupDialogGravity(context, v, gravity, anim, true);
    }
    public static AlertDialog buildCustomStylePopupDialogGravity(Context context, View v, int gravity, int anim, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.WhiteRoundDialog)
                .setView(v)
                .setCancelable(cancelable)
                .create();

        Window window = dialog.getWindow();
        window.setGravity(gravity);
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        window.setWindowAnimations(anim);

        return dialog;
    }

}
