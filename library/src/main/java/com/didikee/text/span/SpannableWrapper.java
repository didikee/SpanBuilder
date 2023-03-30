package com.didikee.text.span;

import android.text.SpannableString;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 * description: 
 */
public class SpannableWrapper {
    @NonNull
    protected CharSequence text;
    protected List<Object> spans;

    public SpannableWrapper(@NonNull CharSequence text, List<Object> spans) {
        this.text = text;
        this.spans = spans;
    }

    public int getLength() {
        return text.length();
    }

    public CharSequence getContent() {
        return text;
    }

    @Nullable
    public List<Object> getSpans() {
        return spans;
    }

    public void apply(SpannableString ss, int startIndex, int endIndex, int flag) {
        List<Object> spans = getSpans();
        if (spans != null) {
            for (Object span : spans) {
                if (span != null) {
                    ss.setSpan(span, startIndex, endIndex, flag);
                }
            }
        }
    }

}
