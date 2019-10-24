package com.tw.commonsdk.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guchenyang
 * @Date 2019/5/17
 * @time 15:01
 */
public class ConTestMode implements Serializable {
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String content;
    private String imageUrl;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ConTestMode() {
        this.name = "王大锤";
        this.content = "啊建设南大街撒";
        this.title = "测试1";
        this.time = "12:00";
        this.imageUrl = "http://img2.imgtn.bdimg.com/it/u=2850936076,2080165544&fm=206&gp=0.jpg";
        this.className = "后E10班";
    }

    public static List<ConTestMode> generateTestModeData(int size) {
        List<ConTestMode> cattModeList = new ArrayList<>();

        for (int i = 0 ; i < size ; i ++ ){
            cattModeList.add(new ConTestMode());
        }

        return  cattModeList;
    }

}
