package com.fornary4.exercise.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.fornary4.exercise.R;

public class SourceUtil {
    public static Drawable getDrawable(Context context, int resId){
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(0, 0, 48, 48);
        return drawable;
    }
}
