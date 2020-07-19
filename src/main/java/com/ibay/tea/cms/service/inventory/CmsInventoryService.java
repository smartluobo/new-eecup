package com.ibay.tea.cms.service.inventory;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.utils.CommonUtil;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TbStoreGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CmsInventoryService {

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

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

    public void addStoreGoods(TbStoreGoods storeGoods) {
        tbStoreGoodsMapper.insert(storeGoods);
    }

    public void deleteStoreGoods(int id) {
        tbStoreGoodsMapper.deleteByPrimaryKey(id);
    }

    public void updateStoreGoods(TbStoreGoods storeGoods) {

        TbStoreGoods dbStoreGoods = tbStoreGoodsMapper.selectByPrimaryKey(storeGoods.getId());
        if (dbStoreGoods == null){
            return ;
        }
        tbStoreGoodsMapper.updateInventoryByStoreGoods(storeGoods);
    }

    public void initStoreGoods(int storeId) {
        TbStore store = tbStoreMapper.selectByPrimaryKey(storeId);

        if (store == null){
            return ;
        }
        tbStoreGoodsMapper.updateInventoryByStoreId(storeId);
    }

    public void clearStoreGoods(int storeId) {
        tbStoreGoodsMapper.clearStoreGoods(storeId);
    }


}
