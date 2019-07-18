package com.ibay.tea.api.service.cart;

import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;

import java.util.List;

public interface ApiCartService {

    List<TbItem> findCartGoodsListByOppenId(String oppenId,int storeId);

    TbItem findCartGoodsById(int id,int storeId);

    void addCartItem(TbCart tbCart);

    TbItem buildCartGoodsInfo(TbCart tbCart, TbStore tbStore);

    void cartGoodsDelete(String oppenId, int cartItemId);

    void checkGoodsInventory(List<TbItem> cartGoodsList, Integer integer);

    int getCartItemCountByOppenId(String oppenId);

    void updateCartItemCount(String oppenId, int id, int count);
}
