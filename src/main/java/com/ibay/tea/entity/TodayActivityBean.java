package com.ibay.tea.entity;

import java.util.List;
import java.util.Map;

public class TodayActivityBean {

    private TbActivity tbActivity;

    private Map<Integer,TbCoupons> couponsMap;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordList;

    public TbActivity getTbActivity() {
        return tbActivity;
    }

    public void setTbActivity(TbActivity tbActivity) {
        this.tbActivity = tbActivity;
    }

    public Map<Integer, TbCoupons> getCouponsMap() {
        return couponsMap;
    }

    public void setCouponsMap(Map<Integer, TbCoupons> couponsMap) {
        this.couponsMap = couponsMap;
    }

    public List<TbActivityCouponsRecord> getTbActivityCouponsRecordList() {
        return tbActivityCouponsRecordList;
    }

    public void setTbActivityCouponsRecordList(List<TbActivityCouponsRecord> tbActivityCouponsRecordList) {
        this.tbActivityCouponsRecordList = tbActivityCouponsRecordList;
    }
}
