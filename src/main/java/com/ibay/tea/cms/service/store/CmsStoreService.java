package com.ibay.tea.cms.service.store;

import com.ibay.tea.cms.service.system.UserCacheService;
import com.ibay.tea.common.utils.StringSplitUtil;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.system.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class CmsStoreService {

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private UserCacheService userCacheService;

    public List<TbStore> findAll() {
        return tbStoreMapper.findAll();
    }

    public void addStore(TbStore tbStore) {
        if (tbStore.getDistributionDistance() == 0){
            tbStore.setDistributionDistance(2000);
        }
        tbStoreMapper.addStore(tbStore);
    }

    public void deleteStore(int id) {
        tbStoreMapper.deleteStore(id);
    }

    public void updateStore(TbStore tbStore) {
        TbStore dbStore = tbStoreMapper.selectByPrimaryKey(tbStore.getId());
        if (dbStore == null){
            return;
        }
        tbStoreMapper.deleteStore(tbStore.getId());
        tbStoreMapper.saveUpdateStore(tbStore);
    }

    public List<TbStore> findByIds(String storeIds) {
        String[] split = storeIds.split(",");
        return tbStoreMapper.findByIds(Arrays.asList(split));
    }

    public List<TbStore> getStoreByUser() {
        SysUser sysUser = userCacheService.getSysUser();
        if (sysUser == null){
            return null;
        }
        if (sysUser.getIsAdmin() == 1){
            //超级用户返回所有店铺列表
            return tbStoreMapper.findAll();
        }else{
            String storeIds = sysUser.getStoreIds();
            if (StringUtils.isNotEmpty(storeIds)){
                //根据用户授权的店铺id查看店铺列表
                List<String> split = StringSplitUtil.split(storeIds);
                return tbStoreMapper.findByIds(split);
            }
        }
        return null;
    }
}
