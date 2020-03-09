package com.example.tag;

public class MyItem {
    private String name;
    private String description;
    private String btAddress;
    private String wifiMAC;
    private boolean isSelected = false;

    public MyItem(String name, String description, String btAddress, String wifiMAC) {
        this.name = name;
        this.description = description;
        this.btAddress = btAddress;
        this.wifiMAC = wifiMAC;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBtAddress() {
        return this.btAddress;
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    public String getWifiMAC() {
        return this.wifiMAC;
    }

    public void setWifiMAC(String wifiMAC) {
        this.wifiMAC = wifiMAC;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
