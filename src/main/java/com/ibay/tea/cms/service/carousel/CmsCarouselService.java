package com.ibay.tea.cms.service.carousel;

import com.ibay.tea.entity.TbCarousel;

import java.util.List;

public interface CmsCarouselService {

    List<TbCarousel> findAll();

    void saveCarousel(TbCarousel tbCarousel);

    void deleteCarousel(int id);

    void updateCarousel(TbCarousel tbCarousel);
}
