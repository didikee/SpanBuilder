package com.didikee.text.span;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 * description: 
 */
public class TypefaceSpan extends MetricAffectingSpan  {
    @Nullable
    private final String mFamily;

    @Nullable
    private final Typeface mTypeface;

    /**
     * Constructs a {@link android.text.style.TypefaceSpan} based on the font family. The previous style of the
     * TextPaint is kept. If the font family is null, the text paint is not modified.
     *
     * @param family The font family for this typeface.  Examples include
     *               "monospace", "serif", and "sans-serif"
     */
    public TypefaceSpan(@Nullable String family) {
        this(family, null);
    }

    /**
     * Constructs a {@link android.text.style.TypefaceSpan} from a {@link Typeface}. The previous style of the
     * TextPaint is overridden and the style of the typeface is used.
     *
     * @param typeface the typeface
     */
    public TypefaceSpan(@NonNull Typeface typeface) {
        this(null, typeface);
    }

    private TypefaceSpan(@Nullable String family, @Nullable Typeface typeface) {
        mFamily = family;
        mTypeface = typeface;
    }


    /**
     * Returns the font family name set in the span.
     *
     * @return the font family name
     * @see #TypefaceSpan(String)
     */
    @Nullable
    public String getFamily() {
        return mFamily;
    }

    /**
     * Returns the typeface set in the span.
     *
     * @return the typeface set
     * @see #TypefaceSpan(Typeface)
     */
    @Nullable
    public Typeface getTypeface() {
        return mTypeface;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        updateTypeface(ds);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        updateTypeface(paint);
    }

    private void updateTypeface(@NonNull Paint paint) {
        if (mTypeface != null) {
            paint.setTypeface(mTypeface);
        } else if (mFamily != null) {
            applyFontFamily(paint, mFamily);
        }
    }

    private void applyFontFamily(@NonNull Paint paint, @NonNull String family) {
        int style;
        Typeface old = paint.getTypeface();
        if (old == null) {
            style = Typeface.NORMAL;
        } else {
            style = old.getStyle();
        }
        final Typeface styledTypeface = Typeface.create(family, style);
        int fake = style & ~styledTypeface.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(styledTypeface);
    }
}
