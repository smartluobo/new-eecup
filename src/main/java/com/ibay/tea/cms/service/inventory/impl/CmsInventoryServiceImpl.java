package com.ibay.tea.cms.service.inventory.impl;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.inventory.CmsInventoryService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.common.utils.CommonUtil;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TbStoreGoods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CmsInventoryServiceImpl implements CmsInventoryService {

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private TbItemMapper tbItemMapper;

    @Override
    public ResultInfo findAll(Map<String,String> params) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String,Object> condition = new HashMap<>();
        int pageNum = Integer.valueOf(params.get("pageNum"));
        int pageSize = Integer.valueOf(params.get("pageSize"));
        String storeId = params.get("storeId");
        String goodsName = params.get("goodsName");
        int startIndex = (pageNum-1) * pageSize;
        condition.put("startIndex",startIndex);
        condition.put("pageSize",pageSize);
        condition.put("storeId",storeId);
        condition.put("goodsName", CommonUtil.getLikeQueryStr(goodsName));
        long total = tbStoreGoodsMapper.countByCondition(condition);
        List<TbStoreGoods> allByStoreId = tbStoreGoodsMapper.findAllByStoreId(condition);
        resultInfo.setData(allByStoreId);
        resultInfo.setTotal(total);
        return resultInfo;
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
        tbStoreGoodsMapper.updateInventoryByStoreGoods(storeGoods);
    }

    @Override
    public void initStoreGoods(int storeId) {
        TbStore store = tbStoreMapper.selectByPrimaryKey(storeId);

        if (store == null){
            return ;
        }
        tbStoreGoodsMapper.updateInventoryByStoreId(storeId);
    }

    @Override
    public void clearStoreGoods(int storeId) {
        tbStoreGoodsMapper.clearStoreGoods(storeId);
    }
}
