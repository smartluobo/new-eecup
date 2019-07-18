package com.ibay.tea.entity;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TbItem implements Comparable<TbItem>{
    private Long id;

    private String title;

    private String sellPoint;

    private double price;

    //活动价
    private double activityPrice;

    //是否显示活动价 0-不现实 1-显示
    private int showActivityPrice;

    private int num;

    private int limitNum;

    private String image;

    private Long cid;

    private int status;

    private Date created;

    private Date updated;

    private String simpleDesc;

    private String skuTypeIds;

    private List<TbSkuType> skuShowInfos;

    private double cartPrice;

    private double cartTotalPrice;

    private int cartItemCount = 1;

    private int cartItemId;

    private String skuDetailDesc;

    private String defaultSkuDetailIds;

    private String cartSkuDetailIds;

    private String posterImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint == null ? null : sellPoint.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public double getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(double activityPrice) {
        this.activityPrice = activityPrice;
    }

    public int getShowActivityPrice() {
        return showActivityPrice;
    }

    public void setShowActivityPrice(int showActivityPrice) {
        this.showActivityPrice = showActivityPrice;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public String getSkuTypeIds() {
        return skuTypeIds;
    }

    public void setSkuTypeIds(String skuTypeIds) {
        this.skuTypeIds = skuTypeIds;
    }

    public List<TbSkuType> getSkuShowInfos() {
        return skuShowInfos;
    }

    public void setSkuShowInfos(List<TbSkuType> skuShowInfos) {
        this.skuShowInfos = skuShowInfos;
    }

    public double getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(double cartPrice) {
        this.cartPrice = cartPrice;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public double getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(double cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public String getSkuDetailDesc() {
        return skuDetailDesc;
    }

    public void setSkuDetailDesc(String skuDetailDesc) {
        this.skuDetailDesc = skuDetailDesc;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getDefaultSkuDetailIds() {
        return defaultSkuDetailIds;
    }

    public void setDefaultSkuDetailIds(String defaultSkuDetailIds) {
        this.defaultSkuDetailIds = defaultSkuDetailIds;
    }

    public String getCartSkuDetailIds() {
        return cartSkuDetailIds;
    }

    public void setCartSkuDetailIds(String cartSkuDetailIds) {
        this.cartSkuDetailIds = cartSkuDetailIds;
    }

    public TbItem copy() {
        String thisStr = JSONObject.toJSONString(this);
        return JSONObject.parseObject(thisStr, TbItem.class);
    }

    @Override
    public int compareTo(TbItem o) {
        return Double.compare(this.getCartPrice(),o.getCartPrice());
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    @Override
    public String toString() {
        return "TbItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sellPoint='" + sellPoint + '\'' +
                ", price=" + price +
                ", activityPrice=" + activityPrice +
                ", showActivityPrice=" + showActivityPrice +
                ", num=" + num +
                ", limitNum=" + limitNum +
                ", image='" + image + '\'' +
                ", cid=" + cid +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                ", simpleDesc='" + simpleDesc + '\'' +
                ", skuTypeIds='" + skuTypeIds + '\'' +
                ", skuShowInfos=" + skuShowInfos +
                ", cartPrice=" + cartPrice +
                ", cartTotalPrice=" + cartTotalPrice +
                ", cartItemCount=" + cartItemCount +
                ", cartItemId=" + cartItemId +
                ", skuDetailDesc='" + skuDetailDesc + '\'' +
                ", defaultSkuDetailIds='" + defaultSkuDetailIds + '\'' +
                ", cartSkuDetailIds='" + cartSkuDetailIds + '\'' +
                ", posterImage='" + posterImage + '\'' +
                '}';
    }
}