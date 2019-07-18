package com.ibay.tea.api.service.activity;

import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbUserCoupons;

import java.util.List;

public interface ApiActivityService {

    TbActivity getTodayActivity(int storeId);

    TbActivityCouponsRecord extractPrize(String oppenId,int storeId);

    void saveUserCouponsToDb(TbUserCoupons tbUserCoupons);

    TbUserCoupons buildUserCoupons(String oppenId, TbActivityCouponsRecord record);

    int checkActivityStatus(TbActivity activityInfo);

    void setExtractTime(TbActivity activityInfo);

    List<TbActivityCouponsRecord> getJackpotInfo(int activityId);
}
