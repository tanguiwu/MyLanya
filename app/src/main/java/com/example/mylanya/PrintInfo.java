package com.example.mylanya;

import java.util.ArrayList;
import java.util.List;

public class PrintInfo {
    private String code = "";
    private String type = "";
    private List<String> textlist = new ArrayList<String>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTextlist() {
        return this.textlist;
    }

    public void setTextlist(List<String> textlist) {
        this.textlist = textlist;
    }
}