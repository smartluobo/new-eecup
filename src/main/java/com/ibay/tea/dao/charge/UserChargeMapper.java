package com.ibay.tea.dao.charge;

import com.ibay.tea.entity.charge.TbChargeConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserChargeMapper {

    List<TbChargeConfig> findAllChargeConfig();

    TbChargeConfig findChargeConfigById(int chargeConfigId);
}
