package com.ibay.tea.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "map.sys")
@Component
public class MapSysProperties {

    private String keyName;

    private String key;

    private String storeDistanceUrl;

    private String mapAddressListUrl;

    private String mapAroundUrl;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStoreDistanceUrl() {
        return storeDistanceUrl;
    }

    public void setStoreDistanceUrl(String storeDistanceUrl) {
        this.storeDistanceUrl = storeDistanceUrl;
    }

    public String getMapAddressListUrl() {
        return mapAddressListUrl;
    }

    public void setMapAddressListUrl(String mapAddressListUrl) {
        this.mapAddressListUrl = mapAddressListUrl;
    }

    public String getMapAroundUrl() {
        return mapAroundUrl;
    }

    public void setMapAroundUrl(String mapAroundUrl) {
        this.mapAroundUrl = mapAroundUrl;
    }
}
