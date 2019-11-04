package com.ibay.tea.cms.service.recommend;

import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbRecommendMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbRecommend;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsRecommendService {

    @Resource
    private TbRecommendMapper tbRecommendMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private TbItemMapper tbItemMapper;

    public List<TbRecommend> findRecommendByStoreId(int storeId) {

        return tbRecommendMapper.findRecommendByStoreId(storeId);
    }

    public boolean addRecommend(TbRecommend recommend) {
        if (recommend.getStoreId() == 0){
            return false;
        }
        if (recommend.getStoreId() == -1){
            recommend.setStoreName("所有店铺");
        }else{
            TbStore tbStore = tbStoreMapper.selectByPrimaryKey(recommend.getStoreId());
            if (tbStore == null){
                return false;
            }
            recommend.setStoreName(tbStore.getStoreName());
        }
        TbItem tbItem = tbItemMapper.selectByPrimaryKey((long) recommend.getGoodsId());
        recommend.setGoodsName(tbItem.getTitle());
        recommend.setGoodsPoster(tbItem.getPosterImage());
        tbRecommendMapper.insert(recommend);
        return true;

    }

    public void deleteRecommend(int id) {
        tbRecommendMapper.deleteByPrimaryKey(id);
    }

    public void updateRecommend(TbRecommend recommend) {

    }
}
