package com.tw.commonsdk.widget;

import java.io.Serializable;

public class SortModel implements Serializable {
    private String letters;//显示拼音的首字母
    private String name;
    private String position;
    private String gradeName;
    private String companyName;
    private String userId;
    private String imgUrl;
    private String type;

    public SortModel() {
    }

    public SortModel(String name, String position, String gradeName, String companyName, String userId, String imgUrl, String type) {
        this.name = name;
        this.position = position;
        this.gradeName = gradeName;
        this.companyName = companyName;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
