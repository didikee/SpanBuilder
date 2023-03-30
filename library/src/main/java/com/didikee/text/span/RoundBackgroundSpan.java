package com.didikee.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 * description: 圆角背景
 */
public final class RoundBackgroundSpan extends ReplacementSpan {
    private int backgroundColor;
    private int textColor;
    private float cornerRadius;
    private int gravity;
    private float backgroundWidth, backgroundHeight;

    public RoundBackgroundSpan(int textColor, int backgroundColor, float cornerRadius) {
        this(backgroundColor, textColor, cornerRadius, Gravity.NO_GRAVITY, 0f, 0f);
    }

    public RoundBackgroundSpan(int textColor, int backgroundColor, float cornerRadius,
                               int gravity, float backgroundWidth, float backgroundHeight) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.cornerRadius = cornerRadius;
        this.gravity = gravity;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        final float textWidth = measureText(paint, text, start, end);

        final float textHeight = bottom - top;
        RectF rect = null;
        //确定要画的背景的位置和大小
        if (backgroundWidth == 0 && backgroundHeight == 0) {
            // 背景占满
            rect = new RectF(x, top, x + textWidth, bottom);
        } else {
            float w = calculateBackgroundWidth(textWidth, backgroundWidth);
            float h = calculateBackgroundHeight(textHeight, backgroundHeight);
            switch (gravity) {
                case Gravity.CENTER:
                    break;
                case Gravity.LEFT:
                    break;
                case Gravity.TOP:
                    break;
                case Gravity.RIGHT:
                    break;
                case Gravity.BOTTOM:
                    final float b = bottom - paint.descent();
                    rect = new RectF(x, b - h, x + w, b);
                    break;
                default:
                    rect = new RectF(x, top, x + textWidth, bottom);
                    break;
            }
        }
        if (rect != null) {
            paint.setColor(backgroundColor);
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
        }
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }

    private float calculateBackgroundWidth(float textWidth, float backgroundWidth) {
        if (backgroundWidth > 1f) {
            return backgroundWidth;
        } else if (backgroundWidth == 0) {
            return textWidth;
        }
        return textWidth * backgroundWidth;
    }

    private float calculateBackgroundHeight(float textHeight, float backgroundHeight) {
        if (backgroundHeight > 1f) {
            return backgroundHeight;
        } else if (backgroundHeight == 0) {
            return textHeight;
        }
        return textHeight * backgroundHeight;
    }
}
