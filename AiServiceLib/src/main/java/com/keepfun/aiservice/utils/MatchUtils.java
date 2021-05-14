package com.keepfun.aiservice.utils;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.keepfun.blankj.util.StringUtils;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author yang
 * @description
 * @date 2020/9/3 11:48 AM
 */
public class MatchUtils {
    public static CharSequence matcherSearchText(int color, String string, String keyWord) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (StringUtils.isEmpty(keyWord)) {
            keyWord = "";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        int indexOf = string.indexOf(keyWord);
        if (indexOf != -1) {
            builder.setSpan(new ForegroundColorSpan(color), indexOf, indexOf + keyWord.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
}
