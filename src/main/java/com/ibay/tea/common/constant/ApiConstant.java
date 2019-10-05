package com.ibay.tea.common.constant;

public class ApiConstant {

    //常规活动标识
    public static final int ACTIVITY_TYPE_NORMAL = 1;
    //节假日活动标识
    public static final int ACTIVITY_TYPE_HOLIDAY = 2;
    //全场折扣体现在商品价格上的活动类型
    public static final int ACTIVITY_TYPE_FULL = 3;
    //买一送一活动
    public static final int ACTIVITY_TYPE_1_1 = 4;
    //第二杯半价活动
    public static final int ACTIVITY_TYPE_TWO_HALF = 5;
    //门店专享活动
    public static final int ACTIVITY_TYPE_STORE = 6;

    //活动未开始
    public static final int ACTIVITY_STATUS_NOT_START = 0;
    //活动进行中
    public static final int ACTIVITY_STATUS_STARTING = 1;
    //活动已经结束
    public static final int ACTIVITY_STATUS_END = 2;

    //优惠券未使用
    public static final int USER_COUPONS_STATUS_NO_USE = 0;
    //优惠券锁定中
    public static final int USER_COUPONS_STATUS_LOCK = 1;
    //优惠券已使用
    public static final int USER_COUPONS_STATUS_USED = 2;

    //折扣券
    public static final int USER_COUPONS_TYPE_RATIO = 1;
    //满减券
    public static final int USER_COUPONS_TYPE_FULL_REDUCE = 2;
    //团购券
    public static final int USER_COUPONS_TYPE_GROUP = 3;
    //免费券
    public static final int USER_COUPONS_TYPE_FREE = 4;
    //通用券
    public static final int USER_COUPONS_TYPE_GENERAL = 5;
    //现金券
    public static final int USER_COUPONS_TYPE_CASH = 6;

    //订单提取方式 自取
    public static final int ORDER_TAKE_WAY_SELF_GET = 0;

    //订单提取方式 派送
    public static final int ORDER_TAKE_WAY_SEND = 1;

    //订单外送价格
    public static final int ORDER_SEND_PRICE = 2;

    //订单状态 未支付
    public static final int ORDER_STATUS_NO_PAY = 0;
    //已支付
    public static final int ORDER_STATUS_PAYED = 1;
    //已完成
    public static final int ORDER_STATUS_MAKE_COMPLETE = 2;
    //已关闭
    public static final int ORDER_STATUS_CLOSED = 3;

    //token cache key
    public static final String WECHAT_ACCESS_TOKEN_GUAVA_KEY = "wechat:access:token:guava:key";//access_token guava缓存key

    //订单制作完成通知表示
    public static final int ORDER_MAKE_COMPLETE_MESSAGE_SEND = 1;
    //订单关闭通知
    public static final int ORDER_CLOSE_MESSAGE_SEND = 2;
    //优惠券过期时间 默认领取券后7天
    public static final int USER_COUPONS_EXPIRE_LIMIT = 7;
    //打印类型 1打印订单
    public static final int PRINT_TYPE_ORDER = 1;
    //打印类型 2 打印订单明细
    public static final int PRINT_TYPE_ORDER_ITEM = 2;
    //打印类型  3 打印订单和打印订单明细
    public static final int PRINT_TYPE_ORDER_ALL = 3;

    //用户优惠券获取渠道  幸运抽奖
    public static final int COUPONS_SOURCE_ACTIVITY = 0;
    //分享专属
    public static final int COUPONS_SOURCE_SHARE = 1;
    //新用户大礼包
    public static final int COUPONS_SOURCE_NEW_USER = 2;
    //系统派发
    public static final int COUPONS_SOURCE_SYS = 3;
    //每日领券
    public static final int COUPONS_SOURCE_RECEIVE = 4;

    public static final int COUPONS_SOURCE_SHOPPING_CARD_RECHARGE = 5;

    //优惠策略类型
    public static final int COUPONS_STRATEGY_TYPE_NO = 0;
    //使用团购券 如满五送一
    public static final int COUPONS_STRATEGY_TYPE_GROUP = 1;
    //使用满减券 如 满100立减10元
    public static final int COUPONS_STRATEGY_TYPE_FULL_REDUCE = 2;
    //一般优惠券
    public static final int COUPONS_STRATEGY_TYPE_COUPONS = 3;
    //全场折扣
    public static final int COUPONS_STRATEGY_TYPE_ALL_RATIO = 5;
    //特殊活动买一送一
    public static final int COUPONS_STRATEGY_TYPE_1_1 = 6;
    //特殊活动第二杯半价
    public static final int COUPONS_STRATEGY_TYPE_TWO_HALF = 7;
    //合作企业福利
    public static final int COUPONS_STRATEGY_TYPE_COMPANY = 8;
    //使用代金券
    public static final int COUPONS_STRATEGY_TYPE_CASH = 9;
    //使用积分抵扣
    public static final int COUPONS_STRATEGY_TYPE_INTEGRAL = 10;

    //优惠券使用方式 0 小程序专享
    public static final int COUPONS_USE_WAY_APPLET = 0;
    //门店专享
    public static final int COUPONS_USE_WAY_STORE = 1;

    //优惠券过期类型 默认 超过有效期过期
     public static final int COUPONS_EXPIRE_TYPE_DEFAULT = 0;
     //仅当日有效
     public static final int COUPONS_EXPIRE_TYPE_CURRENT_DAY = 1;
     //永远有效
     public static final int COUPONS_EXPIRE_TYPE_FOREVER = 2;


}
