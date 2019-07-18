package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCarousel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbCarouselMapper {

    List<TbCarousel> findAll();

    void saveCarousel(TbCarousel tbCarousel);

    void deleteCarousel(int id);

    void saveUpdateCarousel(TbCarousel tbCarousel);

    List<TbCarousel> findCarouselByStoreId(int storeId);
}