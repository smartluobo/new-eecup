package com.ibay.tea.entity;

import org.apache.commons.lang3.StringUtils;

public class TbApiUserAddress {

    private int id;

    private String oppenId;

    private String addressName;

    private String longitude;

    private String latitude;

    private String phoneNum;

    private String bindNum;

    private String userName;

    private int storeId;

    private int isDefault;

    private String verificationCode;

    private String houseNumber;

    private String location;

    private String address;

    private String adname;

    private String name;

    private int distance;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        if (StringUtils.isNotEmpty(longitude)){
            this.longitude = longitude;
        }

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        if (StringUtils.isNotEmpty(latitude)){
            this.latitude = latitude;
        }
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getBindNum() {
        return bindNum;
    }

    public void setBindNum(String bindNum) {
        this.bindNum = bindNum == null ? null : bindNum.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (StringUtils.isNotEmpty(location)){
            String[] split = location.split(",");
            this.longitude = split[0];
            this.latitude = split[1];

        }
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}