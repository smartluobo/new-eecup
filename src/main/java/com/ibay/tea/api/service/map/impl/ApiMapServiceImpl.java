package com.ibay.tea.api.service.map.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.api.responseVo.ApiAddressVo;
import com.ibay.tea.api.service.map.ApiMapService;
import com.ibay.tea.common.utils.EecupMapCalculateUtil;
import com.ibay.tea.common.utils.HttpUtil;
import com.ibay.tea.config.MapSysProperties;
import com.ibay.tea.entity.TbStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiMapServiceImpl implements ApiMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiMapServiceImpl.class);

    @Resource
    private MapSysProperties mapSysProperties;



    @Override
    public TbStore selectStore(List<TbStore> storeList, Map<String, String> params) {
        if (CollectionUtils.isEmpty(storeList)){
            return null;
        }
        int minDistance = 10000000;
        TbStore targetStore = null;

        String longitude = params.get("longitude");
        String latitude = params.get("latitude");
        LOGGER.info("longitude : {},latitude : {}",longitude,latitude);
        double endLng = new BigDecimal(longitude).doubleValue();
        double endLat = new BigDecimal(latitude).doubleValue();

        //本地计算距离 调用接口选择最近的店铺进行路劲规划

        for (TbStore store : storeList) {
            double startLng = new BigDecimal(store.getLongitude()).doubleValue();
            double startLat = new BigDecimal(store.getLatitude()).doubleValue();
            int storeDistance = EecupMapCalculateUtil.getDistanceFromTwoPoints(startLat, startLng, endLat, endLng);
            LOGGER.info("当前用户与店铺"+store.getStoreName()+"相距"+storeDistance+"米");
            if (storeDistance < minDistance){
                minDistance = storeDistance;
                targetStore = store;
            }
        }
        //获取到目标店铺后计算骑行距离
        if (targetStore != null){
            LOGGER.info("当前用户最终选择店铺"+targetStore.getStoreName()+"相距"+minDistance+"米");
            targetStore.setDistance(String.valueOf(minDistance));
            if (minDistance < 3000){
                String origin = targetStore.getLongitude()+","+targetStore.getLatitude();
                String destination = longitude+","+latitude;

                int distance = calculateDistance(origin, destination);
                if (distance > 0){
                    targetStore.setDistance(String.valueOf(minDistance));
                }
                LOGGER.info("当前用户与店铺 : {} ,本地计算距离: {} 米,骑行距离:{}",targetStore.getStoreName(),minDistance,distance);
            }
        }
        return targetStore;
    }

    public int calculateDistance(String origin, String destination) {
        try {
            String url = mapSysProperties.getStoreDistanceUrl()+mapSysProperties.getKey()
                    +"&origin="+origin+"&destination="+destination;
            String result = HttpUtil.get(url);
            Map map = JSON.parseObject(result, Map.class);
            LOGGER.info("calculateDistance return result : {}",map);
            if ("0".equals(String.valueOf(map.get("errcode")))){
                Map<String,Object> dataMap = (Map<String, Object>) map.get("data");
                if (dataMap != null){
                    List<Map<String,Object>> dataList = (List<Map<String, Object>>) dataMap.get("paths");
                    if (dataList != null){
                        Map<String, Object> detailMap = dataList.get(0);
                        Object distance = detailMap.get("distance");
                        if (distance != null){
                            int distanceInt = Integer.parseInt(String.valueOf(distance));
                            if (distanceInt > 0){
                                return distanceInt;
                            }
                        }
                    }
                }
            }
            return 0;
        }catch (Exception e){
            LOGGER.error("calculateDistance happen exception origin : {}, destination : {}",origin,destination,e);
            return 0;
        }
    }

    @Override
    public List<ApiAddressVo> getAddressList(Map<String, String> params) {
        String cityId = params.get("cityId");
        String longitude = params.get("longitude");
        String latitude = params.get("latitude");
        String location = longitude+","+latitude;
        String keywords = params.get("keywords");
        String url;
        if (StringUtils.isEmpty(keywords)){
            url = mapSysProperties.getMapAroundUrl()+mapSysProperties.getKey()+"&location="+location+"&keywords=&types=&offset=20&page=1&extensions=all";
        }else {
            url = mapSysProperties.getMapAddressListUrl()+mapSysProperties.getKey()+"&keywords="+keywords+"&location="+location+"&city="+cityId+"&datatype=all";
        }

        String result = HttpUtil.get(url);
        Map map = JSON.parseObject(result, Map.class);
        if ("OK".equals(map.get("info"))){
            List<Map<String ,Object>> poisList = null;
            if (StringUtils.isEmpty(keywords)){
                poisList = (List<Map<String, Object>>) map.get("pois");
                if (CollectionUtils.isEmpty(poisList)){
                    return null;
                }
                List<ApiAddressVo> apiAddressVoList = new ArrayList<>();
                for (Map<String, Object> itemMap : poisList) {
                    ApiAddressVo apiAddressVo = new ApiAddressVo();
                    apiAddressVo.setAddress(String.valueOf(itemMap.get("address")));
                    apiAddressVo.setAdname(String.valueOf(itemMap.get("adname")));
                    apiAddressVo.setName(String.valueOf(itemMap.get("name")));
                    apiAddressVo.setLocation(String.valueOf(itemMap.get("location")));
                    apiAddressVoList.add(apiAddressVo);
                }
                return apiAddressVoList;
            }else {
                poisList = (List<Map<String, Object>>) map.get("tips");
                if (CollectionUtils.isEmpty(poisList)){
                    return null;
                }
                List<ApiAddressVo> apiAddressVoList = new ArrayList<>();
                for (Map<String, Object> itemMap : poisList) {
                    ApiAddressVo apiAddressVo = new ApiAddressVo();
                    apiAddressVo.setAddress(String.valueOf(itemMap.get("address")));
                    apiAddressVo.setAdname(String.valueOf(itemMap.get("district")));
                    apiAddressVo.setName(String.valueOf(itemMap.get("name")));
                    apiAddressVo.setLocation(String.valueOf(itemMap.get("location")));
                    apiAddressVoList.add(apiAddressVo);
                }
                return apiAddressVoList;
            }
        }
        return null;
    }


}
