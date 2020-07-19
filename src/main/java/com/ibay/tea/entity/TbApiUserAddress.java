package com.ibay.tea.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
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

    public void setLocation(String location) {
        if (StringUtils.isNotEmpty(location)){
            String[] split = location.split(",");
            this.longitude = split[0];
            this.latitude = split[1];

        }
        this.location = location;
    }

}
