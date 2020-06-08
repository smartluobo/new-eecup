package com.ibay.tea.api.service.carousel;

import com.ibay.tea.dao.TbCarouselMapper;
import com.ibay.tea.entity.TbCarousel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ApiCarouselService {

    @Resource
    private TbCarouselMapper tbCarouselMapper;

    public List<TbCarousel> findCarouselByStoreId(int storeId) {
        return tbCarouselMapper.findCarouselByStoreId(storeId);
    }
}
