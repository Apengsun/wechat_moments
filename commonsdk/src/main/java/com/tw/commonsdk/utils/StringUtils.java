package com.tw.commonsdk.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author hzz
 * @功能 字符串处理工具类
 */
public class StringUtils {

    /**
     * 用户身份证号码的打码隐藏加星号加*
     * <p>18位和非18位身份证处理均可成功处理</p>
     * <p>参数异常直接返回null</p>
     *
     * @param idCardNum 身份证号码
     * @param front     需要显示前几位
     * @param end       需要显示末几位
     *
     * @return 处理完成的身份证
     */
    public static String idMask(String idCardNum, int front, int end) {
        //身份证不能为空
        if (TextUtils.isEmpty(idCardNum)) {
            return null;
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return null;
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return null;
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }
    public static boolean hasEmoji(String content) {

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    //截取班级名字
    public static String getClassName(String content) {
        if (content == null || content.trim().length() == 0) {
            return "";
        }

        if (content.startsWith("新领袖") && content.endsWith("班")) {
            return content.substring(content.indexOf("新领袖") + 3, content.indexOf("班"));
        }

        if (content.startsWith("后E") && content.endsWith("班")) {
            return content.substring(content.indexOf("后E") + 2, content.indexOf("班"));
        }

        return content.trim();
    }

    //班级名字样式处理，数字两段加空格
    public static String getClassNameSpace(String content) {
        if (content == null || content.trim().length() == 0) {
            return "";
        }

        if (content.startsWith("新领袖") && content.endsWith("班")) {
            String name = content.substring(content.indexOf("新领袖") + 3, content.indexOf("班")).trim();
            return "新领袖 " + name + " 班";
        }

        if (content.startsWith("后E") && content.endsWith("班")) {
            String name = content.substring(content.indexOf("后E") + 2, content.indexOf("班")).trim();
            return "后E " + name + " 班";
        }

        return content.trim();
    }

    // 截取非数字
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * @param para 指定字符串
     * @return 处理后字符串
     * @功能 指定字符串的首尾加上指定子串
     */
    public static String appendSubStr(String para, String startStr, String endStr) {
        if (startStr != null && null != endStr) {
            return startStr + para + endStr;
        } else {
            return "[" + para + "]";
        }
    }

    /**
     * 对传入的空等，返回true；否则false
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @param items 一个或多个String对象
     * @return 为空或者空字符串 返回true；否则false
     * @功能：判断传入的一个或多个String对象为空或者空字符串
     */
    public static boolean isEmpty(String... items) {
        for (String item : items) {
            if (item == null || "".equals(item) || "null".equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(String... items) {
        return !isEmpty(items);
    }

    /**
     * 过滤表情
     *
     * @param codePoint
     * @return
     */
    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            }
        }
        return buf.toString();
    }

    /**
     * 转换为UTF-8
     */
    public static String tranCodeGBK(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * 转换为UTF-8
     */
    public static String tranCodeUTF8(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public static String dateStrHand(String str) {
        if (!StringUtils.isEmpty(str)) {
            if (str.indexOf(".") != -1) {
                return str.substring(0, str.indexOf("."));
            } else {
                return str;
            }
        } else {
            return "";
        }

    }

    //截串取值  {t1U:30,t1D:10,t2U:30,t2D:5,luxU:60000,luxD:10}
    public static String getValueForShow(String param, String key) {
        String retunStr = "0";
        try {
            int index = param.indexOf(key);
            if (index != -1) {
                String temp = param.substring(index);
                int indexD = temp.indexOf(",");
                if (indexD == -1) {
                    indexD = temp.indexOf("}");
                }
                retunStr = temp.substring(key.length() + 1, indexD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return retunStr;
    }

    //截串取值  {t1U:30,t1D:10,t2U:30,t2D:5,luxU:60000,luxD:10}
    public static String getValueForEdit(String param, String key) {
        if (isEmpty(param)) {
            return "0";
        }
        String retunStr = "";
        try {
            int index = param.indexOf(key);
            if (index != -1) {
                String temp = param.substring(index);
                int indexD = temp.indexOf(",");
                if (indexD == -1) {
                    indexD = temp.indexOf("}");
                }
                retunStr = temp.substring(key.length() + 1, indexD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return retunStr;
    }

    public static String saveFolat(float f) {
        String str;
        int temp = (int) f;
        if (temp == f) {
            str = String.valueOf(temp);
        } else {
            str = String.valueOf(f);
        }
        return str;
    }

    public static String getYearOfDate(String fromDate) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        Date mydate = null;
        try {
            mydate = myFormatter.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) + 1;
        String year = new DecimalFormat("#").format(day / 365f);
        if ("0".equals(year)) {
            year = "1";
        }
        return year;
    }


    /**
     * 金额千分位转化
     *
     * @param text
     * @return String    返回类型
     * @Title: fmtMicrometer
     * @Description: 格式化数字为千分位
     */
    public static String conventMoney(String text) {
        if (StringUtils.isEmpty(text)) {
            return "0";
        }
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    //数字一位的时候前面补位0

    public static String changeNum(String str) {
        if (str.length() < 2) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 替换字符为**
     */
    public static String conStr(String rep) {
        if (rep.length() < 7) {
            return rep;
        } else {
            String strFirst = rep.substring(0, 3);
            String strEnd = rep.substring(rep.length() - 4, rep.length());
            return strFirst + "****" + strEnd;
        }
    }

    /**
     * 获取当前时间的时间戳
     */
    public static String getCurrDateTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取当前时间的时间戳
     */
    public static String minti() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    //获取当前时间的年月
    public static String getCurrDate() {
        return new SimpleDateFormat("yyyyMM").format(new Date());
    }

    //获取当前时间的年月
    public static String getCurrDateMsg() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static Date convertDate(String sta) {
        if (StringUtils.isEmpty(sta)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(sta);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date convertDateSec(String sta) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = sdf.parse(sta);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 当前时间添加小时
     *
     * @param date
     * @param i
     * @return
     */
    public static Date addHourOfDate(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, i);
        Date newDate = c.getTime();
        return newDate;
    }


    public static String unitFormat(int i) {
        String retStr = "";
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 整数转换时分秒
     */
    public static String secToTime(String time1) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int time = Integer.parseInt(time1);
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }


    /**
     * 将日期格式字符串 yyyy-MM-dd HH:mm:ss 格式化为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDateDay(String date) {
        if (!isEmpty(date)) {
            if (date.length() < 10) {
                return date;
            } else {
                return date.substring(0, 10);
            }
        } else {
            return date;
        }
    }

    /**
     * 将日期格式化去掉秒
     * yyyy-MM-dd HH:mm:ss 格式化为yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    public static String formatDateSec(String date) {
        if (!isEmpty(date)) {
            if (date.length() < 16) {
                return date;
            } else {
                return date.substring(0, 16);
            }
        } else {
            return date;
        }
    }

    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + ":" + changeNum(String.valueOf(minute)) + ":" + changeNum(String.valueOf(second));
        return strtime;
    }


    /**
     * @param
     * @param
     * @return
     * @功能 日期加减指定秒数
     */
    public static Long minuteNowDiff(int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt;
        long diff = 0L;
        try {
            dt = sdf.parse(getCurrDateTimeStr());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.SECOND, second);// 日期加减
            diff = rightNow.getTimeInMillis() / 1000;
        } catch (Exception e) {
            //            Logger.e("msg", e.getMessage());
        }
        return diff;
    }

    //long==> 616.19KB,3.73M
    public static String sizeFormatNum2String(long size) {
        String s = "";
        if (size > 1024 * 1024) {
            s = String.format("%.2f", (double) size / 1048576) + "M";
        } else {
            s = String.format("%.2f", (double) size / (1024)) + "KB";
        }
        return s;
    }

    /**
     * 当前日期后的 N 天日期 传入负数就是当前日期的前的 日期时间
     *
     * @return yyyyMMddHHmmss
     */
    public static String getDays(int count) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, count);
        Date date = cal.getTime();
        SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddHHmmss");
        return fd.format(date);
    }

    //转换视频
    public static String getVideoEncryptUrl(String videoUrl, String token) {
        if (StringUtils.isEmpty(token) || !videoUrl.contains(".m3u8")) {
            return videoUrl;
        }
        String firstUrl = videoUrl.substring(0, videoUrl.lastIndexOf("/") + 1);
        String lastUrl = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        return firstUrl + "voddrm.token." + token + "." + lastUrl;
    }

    /**
     * 根据济安评级的等级进行判断显示的内容
     */
    public static String getSafeDesc(String level) {
        String str = "";
        if ("AAA".equals(level)) {
            str = "国家政策风险小，行业前景明朗，公司规模实力强；公司行业垄断程度高，在供应链中处于有利地位；公司战略布局合理，运营管理效率高；盈利能力强，利润率远高于行业平均利润率；经营现金流量充裕，偿债能力强，经营亏损风险低，具有长期投资价值。";
        } else if ("AA".equals(level)) {
            str = "国家政策风险较小，行业前景清晰，公司规模实力较强；公司行业垄断程度较高，在供应链中处于比较有利地位；公司战略布局基本合理，运营管理效率较高；盈利能力较强，利润率高于行业平均利润率；经营现金流量比较充裕，具备偿债能力，经营亏损风险较低，投资价值较高。";

        } else if ("A".equals(level)) {
            str = "行业前景比较清晰，公司具有发展潜力；公司业务发展稳定，产品市场供求平衡，盈利能力可以预期，具备一定的偿债能力，经营亏损风险较低，具备投资价值。";

        } else if ("BBB".equals(level)) {
            str = "行业前景具有不确定性，公司发展具有一定的潜力；产品市场供求存在失衡风险，盈利能力及未来发展受到一些不确定因素的影响，偿债能力一般，存在一定的经营亏损风险，具有一定的投资价值。";

        } else if ("BB".equals(level)) {
            str = "公司在所处行业的前景具有不确定性，公司发展可能遇到瓶颈，正常经营产生的现金流量不足，偿债能力较弱，经营亏损风险较高，投资价值较低。";

        } else if ("B".equals(level)) {
            str = "公司在所处行业的地位较低，经营状况不稳定，公司盈利能力不稳定，偿债能力弱，经营亏损风险较高，不具备投资价值，存在投机风险。";

        } else if ("CCC".equals(level)) {
            str = "公司在所处行业的地位低，经营状况很不稳定，公司盈利能力很差，现金流量短缺，经营亏损与违约风险较高，投机风险高。";

        } else if ("CC".equals(level)) {
            str = "公司经营状况非常不稳定，公司盈利能力极差，现金流极差，投资风险和损失的严重程度极大，投机风险很高。";

        } else if ("C".equals(level)) {
            str = "公司经营状况恶化，业绩大幅度下滑，偿债能力逐渐丧失，投机风险极高。";

        } else if ("D".equals(level)) {
            str = "公司业绩持续亏损，财务状况处于资不抵债，濒临或已经是ST或ST*，公司濒临破产，存在退市风险，切勿投机。";

        } else if ("ST".equals(level)) {
            str = "连续两年亏损，被进行特别处理。";

        }
        return str;
    }

    /**
     * 给出url，获取视频的第一帧     *     * @param url     * @return
     */
    public static Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        // 的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            //            retriever.setDataSource(filePath);
            retriever.setDataSource(url, new HashMap());
            // 获得第一帧图片
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    //给选中字体设置颜色
    public static SpannableString setStringColor(String type, String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (type.equals("1")) {
            stringBuilder.append("资源 | ");
        } else if (type.equals("2")) {
            stringBuilder.append("需求 | ");
        }
        stringBuilder.append(s);

        SpannableString spannableString = new SpannableString(stringBuilder.toString());

        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFA52B2F"));
        spannableString.setSpan(styleSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 根据空格分隔字符串
     */
    public static String[] splitSpace(String label) {
//        String dest;
//        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//
//        Matcher m = p.matcher(label.trim());
//        dest = m.replaceAll("");

        String[] words = label.trim().split("\\s+");
        return words;
    }

    /**
     * emoji标签验证
     */
    public static boolean hasEmoji(CharSequence input) {

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * 过滤掉表情
     */
    public static void setEtFilter(EditText et) {
        if (et == null) {
            return;
        }
        //表情过滤器
        InputFilter emojiFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (hasEmoji(source) || containsEmoji(source.toString())) {
                    ToastUtils.showToast("不支持输入表情");
                    return "";
                }

                return null;
            }
        };
        et.setFilters(new InputFilter[]{emojiFilter});
    }

    /**
     * 过滤掉表情、换行
     */
    public static void setEtAndEnterFilter(EditText et) {
        if (et == null) {
            return;
        }
        //表情过滤器
        InputFilter emojiFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (hasEmoji(source) || containsEmoji(source.toString())) {
                    ToastUtils.showToast("不支持输入表情");
                    return "";
                }

                return null;
            }
        };

        //换行过滤器
        InputFilter enterFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if ("\n".equals(source.toString())) {

                    return "";
                }

                return null;
            }
        };

        et.setFilters(new InputFilter[]{emojiFilter, enterFilter});
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
