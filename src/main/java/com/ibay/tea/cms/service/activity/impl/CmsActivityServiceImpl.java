package com.ibay.tea.cms.service.activity.impl;

import com.ibay.tea.cms.service.activity.CmsActivityService;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.entity.TbActivity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsActivityServiceImpl implements CmsActivityService {

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Override
    public List<TbActivity> findAll() {
        return tbActivityMapper.findAll();
    }

    @Override
    public void addActivity(TbActivity tbActivity) {
        tbActivityMapper.addActivity(tbActivity);
    }

    @Override
    public void deleteActivity(int id) {
        tbActivityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateActivity(TbActivity tbActivity) {
        TbActivity dbActivity = tbActivityMapper.selectByPrimaryKey(tbActivity.getId());
        if (dbActivity == null){
            return;
        }
        tbActivityMapper.deleteByPrimaryKey(tbActivity.getId());
        tbActivityMapper.saveUpdateActivity(tbActivity);
    }
}
