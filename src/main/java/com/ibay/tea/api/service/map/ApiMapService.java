package com.ibay.tea.api.service.map;

import com.ibay.tea.api.responseVo.ApiAddressVo;
import com.ibay.tea.entity.TbStore;

import java.util.List;
import java.util.Map;

public interface ApiMapService {


    TbStore selectStore(List<TbStore> storeList, Map<String, String> params);

    List<ApiAddressVo> getAddressList(Map<String, String> params);
}
