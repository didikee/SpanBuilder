package com.didikee.text.span;

import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 *
 * description: 
 */
public class BitmapSpan extends DynamicDrawableSpan {
    @Nullable
    private Drawable mDrawable;

    public BitmapSpan(@NonNull BitmapDrawable bitmapDrawable) {
        this(bitmapDrawable, null, 0, 0, ALIGN_BOTTOM);
    }

    public BitmapSpan(
            @NonNull BitmapDrawable bitmapDrawable,
            Integer tintColor,
            int width, int height,
            int verticalAlignment) {
        super(verticalAlignment);
        if (tintColor != null) {
            mDrawable = tintDrawable(bitmapDrawable, ColorStateList.valueOf(tintColor));
        } else {
            mDrawable = bitmapDrawable;
        }
        int drawableWidth = mDrawable.getIntrinsicWidth();
        int drawableHeight = mDrawable.getIntrinsicHeight();
        if (width == 0 || height == 0) {
            mDrawable.setBounds(0, 0, Math.max(drawableWidth, 0), Math.max(drawableHeight, 0));
        } else {
            mDrawable.setBounds(0, 0, width, height);
        }
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = null;
        if (mDrawable != null) {
            drawable = mDrawable;
        }
        return drawable;
    }


    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
