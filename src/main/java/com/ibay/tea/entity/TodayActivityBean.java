package com.ibay.tea.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TodayActivityBean {

    private TbActivity tbActivity;

    private Map<Integer,TbCoupons> couponsMap;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordList;
}
