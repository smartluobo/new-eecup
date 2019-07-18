package com.ibay.tea.cms.service.inventory.impl;

import com.ibay.tea.cms.service.inventory.CmsInventoryService;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TbStoreGoods;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CmsInventoryServiceImpl implements CmsInventoryService {

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private TbItemMapper tbItemMapper;

    @Override
    public List<TbStoreGoods> findAll(int storeId) {
        return tbStoreGoodsMapper.findAllByStoreId(storeId);

    }

    @Override
    public void addStoreGoods(TbStoreGoods storeGoods) {
        tbStoreGoodsMapper.insert(storeGoods);
    }

    @Override
    public void deleteStoreGoods(int id) {
        tbStoreGoodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateStoreGoods(TbStoreGoods storeGoods) {

        TbStoreGoods dbStoreGoods = tbStoreGoodsMapper.selectByPrimaryKey(storeGoods.getId());
        if (dbStoreGoods == null){
            return ;
        }
        tbStoreGoodsMapper.deleteByPrimaryKey(storeGoods.getId());
        tbStoreGoodsMapper.saveUpdateMenu(storeGoods);

    }

    @Override
    public void initStoreGoods(int storeId) {
        TbStore store = tbStoreMapper.selectByPrimaryKey(storeId);

        if (store == null){
            return ;
        }

        List<TbItem> goodsList = tbItemMapper.findAll();

        if (CollectionUtils.isEmpty(goodsList)){
            return ;
        }
        List<TbStoreGoods> storeGoodsList = new ArrayList<>();

        for (TbItem tbItem : goodsList) {
            TbStoreGoods tbStoreGoods = new TbStoreGoods();
            tbStoreGoods.setCreateTime(new Date());
            Long goodsId = tbItem.getId();
            tbStoreGoods.setGoodsId(goodsId.intValue());
            tbStoreGoods.setGoodsName(tbItem.getTitle());
            tbStoreGoods.setGoodsInventory(30);
            tbStoreGoods.setStoreId(store.getId());
            tbStoreGoods.setStoreName(store.getStoreName());
            storeGoodsList.add(tbStoreGoods);
        }
        tbStoreGoodsMapper.deleteByStoreId(storeId);
        tbStoreGoodsMapper.insertBatch(storeGoodsList);


    }
}
