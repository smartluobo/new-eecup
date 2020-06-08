package com.ibay.tea.api.service.cart;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbCartMapper;
import com.ibay.tea.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApiCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCartService.class);

    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private StoreCache storeCache;

    @Resource
    private ApiGoodsService apiGoodsService;


    @Resource
    private TbActivityMapper tbActivityMapper;

    public List<TbItem> findCartGoodsListByOppenId(String oppenId,int storeId) {
        List<TbCart> cartGoodsList = tbCartMapper.findCartGoodsListByOppenId(oppenId);
        TbStore store = storeCache.findStoreById(storeId);
        List<TbItem> result = new ArrayList<>();
        if (cartGoodsList != null && cartGoodsList.size() > 0){
            for (TbCart tbCart : cartGoodsList) {
                TbItem goods = buildCartGoodsInfo(tbCart,store);
                goods.setCartItemId(tbCart.getId());
                goods.setSkuDetailDesc(tbCart.getSkuDetailDesc());
                result.add(goods);
            }
            return result;
        }else {
            return null;
        }
    }

    public TbItem findCartGoodsById(int id,int storeId) {
        TbCart tbCart = tbCartMapper.selectByPrimaryKey(id);
        TbStore store = storeCache.findStoreById(storeId);
        if (tbCart != null){
            return buildCartGoodsInfo(tbCart,store);
        }
        return null;
    }

    public void addCartItem(TbCart tbCart) {
        TbItem goods = goodsCache.findGoodsById(tbCart.getGoodsId());
        if (goods == null){
            return ;
        }
        tbCart.setCreateTime(new Date());
        tbCartMapper.insert(tbCart);
    }

    public TbItem buildCartGoodsInfo(TbCart tbCart, TbStore store) {
        TbItem goodsById = goodsCache.findGoodsById(tbCart.getGoodsId());
        TbItem goodsInfo = null;
        if (goodsById != null){
            goodsInfo = goodsById.copy();
            LOGGER.info("cache goods price : {}",goodsInfo.getPrice());
            String cartSkuDetailIds = tbCart.getSkuDetailIds();
            goodsInfo.setCartSkuDetailIds(cartSkuDetailIds);
            TbActivity fullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(), store.getId());
            apiGoodsService.calculateGoodsPrice(goodsInfo,store.getExtraPrice(),fullActivity);
            if (StringUtils.isNotBlank(cartSkuDetailIds)){
                int skuPrice = goodsCache.calculateSkuPrice(cartSkuDetailIds);
                if (skuPrice != 0){
                    goodsInfo.setPrice(goodsInfo.getPrice() + skuPrice);
                }
                setSelectedSkuDetail(goodsInfo,cartSkuDetailIds);
            }
            goodsInfo.setCartPrice(goodsInfo.getPrice());
            goodsInfo.setCartItemCount(tbCart.getItemCount());
            goodsInfo.setCartTotalPrice(PriceCalculateUtil.multiply(goodsInfo.getPrice(),goodsInfo.getCartItemCount()));
            goodsInfo.setSkuDetailDesc(tbCart.getSkuDetailDesc());
        }
        return goodsInfo;
    }

    public void cartGoodsDelete(String oppenId, int cartItemId) {
        tbCartMapper.cartGoodsDelete(oppenId,cartItemId);
    }

    public void checkGoodsInventory(List<TbItem> cartGoodsList, Integer integer) {

    }

    public int getCartItemCountByOppenId(String oppenId) {
        Integer cartItemCount = tbCartMapper.getCartItemCountByOppenId(oppenId);
        if (cartItemCount == null){
            return 0;
        }
        return cartItemCount;
    }

    public void updateCartItemCount(String oppenId, int id, int count) {
        tbCartMapper.updateCartItemCount(oppenId,id,count);
    }


    private void setSelectedSkuDetail(TbItem goods,String skuDetailIds) {
        List<TbSkuType> skuShowInfos = goods.getSkuShowInfos();
        if (skuShowInfos == null || skuShowInfos.size() == 0){
            return;
        }
        if (skuDetailIds != null || skuDetailIds.trim().length() > 0){
            String[] skuDetailArr = skuDetailIds.split(",");
            for (String s : skuDetailArr) {
                for (TbSkuType skuShowInfo : skuShowInfos) {
                    for (TbSkuDetail tbSkuDetail : skuShowInfo.getSkuDetails()) {
                        if (s.equals(String.valueOf(tbSkuDetail.getId()))){
                            tbSkuDetail.setIsSelected(1);
                        }
                    }
                }
            }
        }
    }
}
