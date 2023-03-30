package com.didikee.text.span;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * description: 代替 {@link @SpannableStringBuilder},创建一些文字组合效果
 */
public final class SpanBuilder {
    private final ArrayList<SpannableWrapper> spannableWrappers = new ArrayList<>();
    public static final int NO_COLOR = 0;
    public static final int FLAG = 1;
    private TextView textView;

    public SpanBuilder appendText(CharSequence text) {
        spannableWrappers.add(new SpannableWrapper(text, null));
        return this;
    }

    public SpanBuilder appendText(CharSequence text, TextSpanConfig spanStyle) {
        spannableWrappers.add(new SpannableWrapper(text, spanStyle.getSpans()));
        return this;
    }

    public SpanBuilder appendText(CharSequence text, Integer textColor) {
        return this.appendText(text, textColor, null, null);
    }

    public SpanBuilder appendText(CharSequence text, Integer textColor, Integer textSizeDip) {
        return this.appendText(text, textColor, textSizeDip, null);
    }

    public SpanBuilder appendText(CharSequence text, Integer textColor, Integer textSizeDip, Integer textStyle) {
        return this.appendText(text, textColor, textSizeDip, textStyle, (Object) null);
    }

    public SpanBuilder appendText(CharSequence text, Integer textColor, Integer textSizeDip, Integer textStyle, Object... spans) {
        ArrayList<Object> whats = new ArrayList<>();
        if (textColor != null && textColor != NO_COLOR) {
            whats.add(new ForegroundColorSpan(textColor));
        }
        if (textSizeDip != null && textSizeDip > 0) {
            whats.add(new AbsoluteSizeSpan(textSizeDip, true));
        }
        if (textStyle != null && textStyle != Typeface.NORMAL) {
            whats.add(new StyleSpan(textStyle));
        }
        if (spans != null && spans.length > 0) {
            whats.addAll(Arrays.asList(spans));
        }
        spannableWrappers.add(new SpannableWrapper(text, whats));
        return this;
    }

    public SpanBuilder appendImage(BitmapDrawable bitmapDrawable, Integer tintColor, int width, int height, int align) {
        BitmapSpan bitmapSpan = new BitmapSpan(bitmapDrawable, tintColor, width, height, align);
        ArrayList<Object> spans = new ArrayList<>();
        spans.add(bitmapSpan);
        spannableWrappers.add(createImageSpannableWrapper(spans));
        return this;
    }

    /**
     * 添加一个样式
     * @param text
     * @param spans
     * @return
     */
    public SpanBuilder appendSpan(CharSequence text, Object... spans) {
        spannableWrappers.add(new SpannableWrapper(text, Arrays.asList(spans)));
        return this;
    }

    public SpanBuilder enableClickableSpan(TextView textView) {
        this.textView = textView;
        return this;
    }

    public SpannableStringBuilder build() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (SpannableWrapper wrapper : spannableWrappers) {
            CharSequence content = wrapper.getContent();
            SpannableString ss = new SpannableString(content);
            int startIndex = 0;
            int endIndex = startIndex + wrapper.getLength();
            wrapper.apply(ss, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(ss);
        }
        if (textView != null) {
            enableClickSpan(textView);
        }
        return builder;
    }


    private int getStartIndex(SpannableWrapper wrapper) {
        int count = 0;
        for (SpannableWrapper item : spannableWrappers) {
            if (item == wrapper) {
                break;
            }
            count += item.getLength();
        }
        return count;
    }

    private SpannableWrapper createImageSpannableWrapper(List<Object> spans) {
        return new SpannableWrapper(" ", spans);
    }

    public static CharSequence findAndSpan(String text, String target, TextSpanConfig spanStyle) {
        return findAndSpan(text, new String[]{target}, spanStyle);
    }

    /**
     * 从文本中找到特定的字符，并给它应用样式
     * @param text 完成的文本
     * @param target 需要寻找的目标文字片段
     * @param spanStyle 需要应用的样式
     * @return
     */
    public static CharSequence findAndSpan(String text, String[] target, TextSpanConfig spanStyle) {
        if (text == null || text.length() == 0) {
            return text;
        }
        if (target == null || target.length == 0) {
            return text;
        }
        SpannableString spannableString = new SpannableString(text);
        ArrayList<Object> spans = spanStyle.getSpans();
        for (String s : target) {
            int indexOf = text.indexOf(s);
            if (indexOf != -1) {
                int startIndex = indexOf;
                int endIndex = startIndex + s.length();
                for (Object span : spans) {
                    spannableString.setSpan(span, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
        }
        return spannableString;
    }

    /**
     * 使用正则表达式从文本中找到特定的字符，并给它应用样式
     * @param text 完成的文本
     * @param regex 正则表达式
     * @param spanStyle 需要应用的样式
     * @return
     */
    public static CharSequence regexAndSpan(String text, String regex, TextSpanConfig spanStyle) {
        if (text == null || text.length() == 0) {
            return text;
        }
        if (regex == null || regex.length() == 0) {
            return text;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
//        while (matcher.find()){
//            String group = matcher.group();
//            LogUtils.d("find: "+ group);
//        }
        if (matcher.find()) {
            //只处理第一处匹配到的数字
            String target = matcher.group();
            int indexOf = text.indexOf(target);
            if (indexOf != -1) {
                SpannableString spannableString = new SpannableString(text);
                ArrayList<Object> spans = spanStyle.getSpans();
                int startIndex = indexOf;
                int endIndex = startIndex + target.length();
                for (Object span : spans) {
                    spannableString.setSpan(span, startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                return spannableString;
            }
        }
        return text;
    }


    // 当添加了点击事件时需要应用一下该方法
    public static void enableClickSpan(TextView textView) {
        if (textView != null) {
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
