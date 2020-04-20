package com.cat.bots.util;

public class ChangeableString {
    private String str;

    public ChangeableString(String str) {
        this.str = str;
    }

    public void setString(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }
}