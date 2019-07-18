package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCart record);

    TbCart selectByPrimaryKey(Integer id);

    List<TbCart> findCartGoodsListByOppenId(String oppenId);

    List<TbCart> findCartItemByIds(List<String> strings);

    void cartGoodsDelete(@Param("oppenId") String oppenId, @Param("id") int cartItemId);

    void deleteCartItemByIds(List<String> strings);

    Integer getCartItemCountByOppenId(String oppenId);

    void updateCartItemCount(@Param("oppenId") String oppenId, @Param("id") int id, @Param("count") int count);
}