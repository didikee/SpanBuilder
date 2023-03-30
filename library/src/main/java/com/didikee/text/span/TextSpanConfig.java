package com.didikee.text.span;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

/**
 *
 * description: 
 */
public final class TextSpanConfig {
    private int textColor, textSizeDip;
    private int textStyle = -1;// 0-3
    private Typeface typeface;
    private TextView textView;
    private View.OnClickListener onClickListener;
    private boolean underline;

    public TextSpanConfig textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public TextSpanConfig textSize(int textSizeDip) {
        this.textSizeDip = textSizeDip;
        return this;
    }

    public TextSpanConfig textStyle(int textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public TextSpanConfig font(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public TextSpanConfig font(Context context, int fontResId, int typefaceStyle) {
        typeface = Typeface.create(ResourcesCompat.getFont(context, fontResId), typefaceStyle);
        return this;
    }

    public TextSpanConfig click(View.OnClickListener clickListener) {
        return click(null, clickListener, false);
    }

    public TextSpanConfig click(TextView textView, View.OnClickListener clickListener) {
        return click(textView, clickListener, false);
    }

    public TextSpanConfig click(TextView textView, View.OnClickListener clickListener, boolean underline) {
        this.textView = textView;
        this.onClickListener = clickListener;
        this.underline = underline;
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSizeDip() {
        return textSizeDip;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    ArrayList<Object> getSpans() {
        ArrayList<Object> whats = new ArrayList<>();
        if (textColor != 0) {
            whats.add(new ForegroundColorSpan(textColor));
        }
        if (textSizeDip > 0) {
            whats.add(new AbsoluteSizeSpan(textSizeDip, true));
        }
        if (textStyle >= Typeface.NORMAL && textStyle <= Typeface.BOLD_ITALIC) {
            whats.add(new StyleSpan(textStyle));
        }
        if (typeface != null) {
            whats.add(new TypefaceSpan(typeface));
        }
        if (onClickListener != null) {
            whats.add(new MyClickableSpan(onClickListener, underline));
            if (textView != null) {
                SpanBuilder.enableClickSpan(textView);
            }
        } else {
            // 单独处理underline
            if (underline) {
                whats.add(new UnderlineSpan());
            }
        }
        return whats;
    }

    private static class MyClickableSpan extends ClickableSpan {
        private final View.OnClickListener onClickListener;
        private final boolean underline;

        public MyClickableSpan(View.OnClickListener onClickListener, boolean underline) {
            this.onClickListener = onClickListener;
            this.underline = underline;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (onClickListener != null) {
                onClickListener.onClick(widget);
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(underline);
        }
    }
}
