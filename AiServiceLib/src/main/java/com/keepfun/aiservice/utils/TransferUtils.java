package com.keepfun.aiservice.utils;

import com.keepfun.blankj.util.StringUtils;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yang
 * @description
 * @date 2021/1/13 8:21 PM
 */
public class TransferUtils {

    /**
     * 去除html代码中含有的标签
     */
    public static String html2Text(String htmlStr) {
        if (StringUtils.isEmpty(htmlStr)) return "";

        // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

        // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

        // 定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";
        String regEx_image = "<img[^>]+>";
        String regEx_link = "<a[^>]+>";

        // 过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        // 过滤style标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        // 过滤html标签
        Pattern p_image = Pattern.compile(regEx_image, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(htmlStr);
        htmlStr = m_image.replaceAll("[图片]");
        // 过滤html标签
        Pattern p_link = Pattern.compile(regEx_link, Pattern.CASE_INSENSITIVE);
        Matcher m_link = p_link.matcher(htmlStr);
        htmlStr = m_link.replaceAll("[链接]");
        // 过滤html标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        // 剔除空格行
        htmlStr = htmlStr.replaceAll("[ ]+", " ");
        htmlStr = htmlStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return translation(htmlStr);
//        return StringEscapeUtils.unescapeHtml4(htmlStr);// 解析转义符号后返回文本字符串
    }

    public static String translation(String content) {
        String replace = content.replace("&lt;", "<");
        String replace1 = replace.replace("&gt;", ">");
        String replace2 = replace1.replace("&amp;", "&");
        String replace3 = replace2.replace("&quot;", "\"");
        return replace3.replace("&copy;", "©");
    }
}
