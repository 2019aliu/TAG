package com.example.tag;

public class MyItem {
    private String text;
    private boolean isSelected = false;

    public MyItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
