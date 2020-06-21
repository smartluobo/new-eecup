package com.ibay.tea.cms.service.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ResultInfo findByStoreId(Map<String,String> params) {
        Integer pageSize = Integer.valueOf(params.get("pageSize"));
        Integer pageNum = Integer.valueOf(params.get("pageNum"));
        Map<String,Object> condition = new HashMap<>();
        condition.put("pageSize",pageSize);
        condition.put("startIndex",(pageNum-1)*pageSize);
        condition.put("storeId",params.get("storeId"));
        condition.put("activityType",params.get("activityType"));
        long total = tbActivityMapper.countByCondition(condition);
        List<TbActivity> activityList = tbActivityMapper.findActivityByCondition(condition);
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        resultInfo.setTotal(total);
        resultInfo.setData(activityList);
        return resultInfo;
    }
}
