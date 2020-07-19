package com.ibay.tea.api.service.coupons;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.dao.TbShoppingCardMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbShoppingCard;
import com.ibay.tea.entity.TbUserCoupons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class ApiCouponsService {


    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
   private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbShoppingCardMapper tbShoppingCardMapper;

    @Resource
    private ApiActivityService apiActivityService;

    public TbUserCoupons findCouponsByCondition(Map<String, Object> condition) {
        return tbUserCouponsMapper.findCouponsByCondition(condition);
    }

    public TbUserCoupons findOneCouponsByOppenId(String oppenId,String currentDate) {
        return tbUserCouponsMapper.findOneCouponsByOppenId(oppenId,currentDate);
    }

    public List<TbUserCoupons> findUserValidCoupons(String oppenId,String useWay) {
        List<TbUserCoupons> userCouponsList = tbUserCouponsMapper.findUserValidCoupons(oppenId,useWay);

        if (CollectionUtils.isEmpty(userCouponsList)){
            return new ArrayList<>();
        }
        coverUserCoupon(userCouponsList);
        return userCouponsList;
    }

    private void coverUserCoupon(List<TbUserCoupons> userCouponsList) {
        Iterator<TbUserCoupons> iterator = userCouponsList.iterator();
        while (iterator.hasNext()){
            TbUserCoupons tbUserCoupons = iterator.next();
            String yyyyMMdd = DateUtil.getDateYyyyMMdd();
            String expireDate = DateUtil.getDateYyyyMMdd(tbUserCoupons.getExpireDate());
            if (yyyyMMdd.equals(expireDate)){
                tbUserCoupons.setCurrentDayExpire(1);
            }
            if (ApiConstant.USER_COUPONS_TYPE_RATIO == tbUserCoupons.getCouponsType() || ApiConstant.USER_COUPONS_TYPE_GENERAL == tbUserCoupons.getCouponsType()){
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_RATIO);
                String couponsRatio = tbUserCoupons.getCouponsRatio();
                log.info("current ratio :{}",couponsRatio);
                int index = couponsRatio.indexOf(".");
                String bigNumStr = couponsRatio.substring(index+1, index + 2);
                String smallNumStr = "0";
                if (couponsRatio.length() == 4){
                    smallNumStr = couponsRatio.substring(index+2, index + 3);
                }
                log.info("bigNumStr : {} ,smallNumStr : {}",bigNumStr,smallNumStr);
                tbUserCoupons.setBigNum(bigNumStr);
                tbUserCoupons.setSmallNum(smallNumStr);
            }else if(ApiConstant.USER_COUPONS_TYPE_CASH == tbUserCoupons.getCouponsType()){
                String cashAmount = tbUserCoupons.getCashAmount();
                if (cashAmount == null || Double.valueOf(cashAmount) <= 0){
                    iterator.remove();
                }else{
                    int index = cashAmount.indexOf(".");
                    if (index <= 0){
                        tbUserCoupons.setBigNum(cashAmount);
                        tbUserCoupons.setSmallNum("00");
                    }else{
                        tbUserCoupons.setBigNum(cashAmount.substring(0,index));
                        tbUserCoupons.setSmallNum(cashAmount.substring(index+1));
                        if (tbUserCoupons.getSmallNum().length() == 1){
                            tbUserCoupons.setSmallNum(tbUserCoupons.getSmallNum()+"0");
                        }
                    }
                }
            }else if (ApiConstant.USER_COUPONS_TYPE_FREE == tbUserCoupons.getCouponsType()){
                tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_FREE);
            }
        }

    }

    public List<TbUserCoupons> getUserCouponsByOppenId(String oppenId) {
        List<TbUserCoupons> userCouponsList = tbUserCouponsMapper.getUserCouponsByOppenId(oppenId);
        if (CollectionUtils.isEmpty(userCouponsList)){
            return null;
        }
        coverUserCoupon(userCouponsList);
        return userCouponsList;
    }

    public TbUserCoupons findCurrentDayUserCoupons(String oppenId, String currentDate,String activityId) {

        return tbUserCouponsMapper.findCurrentDayUserCoupons(oppenId,currentDate,activityId);
    }

    public TbUserCoupons findCurrentDayExperienceCoupons(String oppenId, String currentDate,String activityId) {
        return tbUserCouponsMapper.findCurrentDayExperienceCoupons(oppenId,currentDate,activityId);
    }

    public List<TbCoupons> getCouponsCenterList() {
        return tbCouponsMapper.getCouponsCenterList();
    }

    public TbShoppingCard queryShoppingCardInfo(String couponsCode) {
        return tbShoppingCardMapper.selectByCouponsCode(couponsCode);
    }

    public boolean rechargeByShoppingCard(String couponsCode, String oppenId,TbShoppingCard tbShoppingCard) {
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        String yyyyMMdd = DateUtil.getDateYyyyMMdd();
        tbUserCoupons.setOppenId(oppenId);
        tbUserCoupons.setCouponsId(tbShoppingCard.getId());
        tbUserCoupons.setCouponsName("现金券");
        tbUserCoupons.setReceiveDate(Integer.valueOf(yyyyMMdd));
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setStatus(ApiConstant.USER_COUPONS_STATUS_NO_USE);

        tbUserCoupons.setIsReferrer(0);
        tbUserCoupons.setCouponsRatio("0.0");
        tbUserCoupons.setCouponsType(ApiConstant.USER_COUPONS_TYPE_CASH);
        tbUserCoupons.setUseRules("不可与其他优惠叠加使用");
        tbUserCoupons.setUseScope("任意商品");
        tbUserCoupons.setCouponsSource(ApiConstant.COUPONS_SOURCE_SHOPPING_CARD_RECHARGE);
        tbUserCoupons.setCouponsCode(tbShoppingCard.getCardCode());
        tbUserCoupons.setSourceName("购物卡充值");
        tbUserCoupons.setUseWay(ApiConstant.COUPONS_USE_WAY_APPLET);
        tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_DEFAULT);
        tbUserCoupons.setExpireDate(DateUtil.getExpireDate(Integer.valueOf(yyyyMMdd),180));
        if (tbShoppingCard.getType() == 1){//充值卡为现金券充值后仅当天可以使用
            tbUserCoupons.setExpireType(ApiConstant.COUPONS_EXPIRE_TYPE_CURRENT_DAY);
            tbUserCoupons.setExpireDate(new Date());
        }
        tbUserCoupons.setCashAmount(String.valueOf(tbShoppingCard.getAmount()));
        apiActivityService.saveUserCouponsToDb(tbUserCoupons);
        tbShoppingCardMapper.updateUseStatusById(tbShoppingCard.getId(),1);
        return true;
    }
}
