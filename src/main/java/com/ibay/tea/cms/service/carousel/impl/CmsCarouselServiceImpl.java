package com.ibay.tea.cms.service.carousel.impl;

import com.ibay.tea.cms.service.carousel.CmsCarouselService;
import com.ibay.tea.dao.TbCarouselMapper;
import com.ibay.tea.entity.TbCarousel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CmsCarouselServiceImpl implements CmsCarouselService{

    @Resource
    private TbCarouselMapper tbCarouselMapper;

    @Override
    public List<TbCarousel> findAll() {
        return tbCarouselMapper.findAll();
    }

    @Override
    public void saveCarousel(TbCarousel tbCarousel) {
        tbCarousel.setCreateTime(new Date());
        tbCarouselMapper.saveCarousel(tbCarousel);
    }

    @Override
    public void deleteCarousel(int id) {
        tbCarouselMapper.deleteCarousel(id);
    }

    @Override
    public void updateCarousel(TbCarousel tbCarousel) {
        tbCarouselMapper.deleteCarousel(tbCarousel.getId());
        tbCarouselMapper.saveUpdateCarousel(tbCarousel);
    }
}
