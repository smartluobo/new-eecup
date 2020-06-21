package com.ibay.tea.cms.service.carousel;

import com.ibay.tea.dao.TbCarouselMapper;
import com.ibay.tea.entity.TbCarousel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CmsCarouselService {

    @Resource
    private TbCarouselMapper tbCarouselMapper;

    public List<TbCarousel> findAll() {
        return tbCarouselMapper.findAll();
    }

    public void saveCarousel(TbCarousel tbCarousel) {
        tbCarousel.setCreateTime(new Date());
        tbCarouselMapper.saveCarousel(tbCarousel);
    }

    public void deleteCarousel(int id) {
        tbCarouselMapper.deleteCarousel(id);
    }

    public void updateCarousel(TbCarousel tbCarousel) {
        tbCarouselMapper.deleteCarousel(tbCarousel.getId());
        tbCarouselMapper.saveUpdateCarousel(tbCarousel);
    }

    public List<TbCarousel> findCarouselByStoreId(int storeId) {
        return tbCarouselMapper.findCmsCarouselByStoreId(storeId);
    }
}
