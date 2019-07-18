package com.ibay.tea.api.service.carousel;

import com.ibay.tea.entity.TbCarousel;

import java.util.List;

public interface ApiCarouselService {

    List<TbCarousel> findCarouselByStoreId(int storeId);

}
