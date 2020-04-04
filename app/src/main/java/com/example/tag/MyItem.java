package com.example.tag;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MyItem {
    private String name;
    private String description;
    private String btAddress;
    private String wifiMAC;
    private double[] location = new double[2];
    private boolean isSelected = false;

    public MyItem(String name, String description, String btAddress, String wifiMAC) {
        this.name = name;
        this.description = description;
        this.btAddress = btAddress;
        this.wifiMAC = wifiMAC;
    }

    public MyItem(HashMap<String, String> data) {
        if (data == null) {
            throw new IllegalArgumentException("Provided mapping for MyItem is null");
        }
        this.name = data.get("name");
        this.description = data.get("description");
        this.btAddress = data.get("btAddress");
        this.wifiMAC = data.get("wifiMAC");
        String locationRaw = data.get("location");
        String[] locationCoordsRaw = locationRaw.replaceAll("[^0-9\\.\\- ]", "").toLowerCase().split("\\s+");
        location[0] = Double.parseDouble(locationCoordsRaw[0]);
        location[1] = Double.parseDouble(locationCoordsRaw[1]);
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

    public double[] getLocation() {
        return this.location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public double getLatitude() {
        return this.location[0];
    }

    public void setLatitude(double latitude) {
        this.location[0] = latitude;
    }

    public double getLongitude() {
        return this.location[1];
    }

    public void setLongitude(double longitude) {
        this.location[1] = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("/name", name);
        result.put("/description", description);
        result.put("/btAddress", btAddress);
        result.put("/wifiMAC", wifiMAC);
        result.put("/selected", isSelected);
        return result;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
