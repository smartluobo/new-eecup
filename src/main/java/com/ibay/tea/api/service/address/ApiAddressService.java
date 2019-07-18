package com.ibay.tea.api.service.address;

import com.ibay.tea.entity.TbApiUserAddress;

import java.util.List;
import java.util.Map;

public interface ApiAddressService {

    List<TbApiUserAddress> findUserAddressByOppenId(String oppenId,String storeId);

    TbApiUserAddress findUserAddressById(int id);

    void insertApiUserAddress(TbApiUserAddress tbApiUserAddress);

    void updateUserAddress(TbApiUserAddress tbApiUserAddress);

    void deleteApiUserAddress(Map<String, String> params);

}
