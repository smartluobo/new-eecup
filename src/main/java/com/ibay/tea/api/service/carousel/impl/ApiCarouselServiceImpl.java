package com.ibay.tea.api.service.carousel.impl;

import com.ibay.tea.api.service.carousel.ApiCarouselService;
import com.ibay.tea.dao.TbCarouselMapper;
import com.ibay.tea.entity.TbCarousel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiCarouselServiceImpl implements ApiCarouselService {

    @Resource
    private TbCarouselMapper tbCarouselMapper;

    @Override
    public List<TbCarousel> findCarouselByStoreId(int storeId) {
        return tbCarouselMapper.findCarouselByStoreId(storeId);
    }
}
