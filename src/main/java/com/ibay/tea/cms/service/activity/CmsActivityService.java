package com.ibay.tea.cms.service.activity;

import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.entity.TbActivity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsActivityService {

    @Resource
    private TbActivityMapper tbActivityMapper;

    public List<TbActivity> findAll() {
        return tbActivityMapper.findAll();
    }

    public void addActivity(TbActivity tbActivity) {
        tbActivityMapper.addActivity(tbActivity);
    }

    public void deleteActivity(int id) {
        tbActivityMapper.deleteByPrimaryKey(id);
    }

    public void updateActivity(TbActivity tbActivity) {
        TbActivity dbActivity = tbActivityMapper.selectByPrimaryKey(tbActivity.getId());
        if (dbActivity == null){
            return;
        }
        tbActivityMapper.deleteByPrimaryKey(tbActivity.getId());
        tbActivityMapper.saveUpdateActivity(tbActivity);
    }

    public List<TbActivity> findByStoreId(int storeId) {
        if (storeId <= 0){
            return findAll();
        }
        return tbActivityMapper.findByStoreId(storeId);
    }
}
