package com.ibay.tea.cms.service.activity;

import com.ibay.tea.entity.TbActivity;

import java.util.List;

public interface CmsActivityService {

    List<TbActivity> findAll();

    void addActivity(TbActivity tbActivity);

    void deleteActivity(int id);

    void updateActivity(TbActivity tbActivity);
}
