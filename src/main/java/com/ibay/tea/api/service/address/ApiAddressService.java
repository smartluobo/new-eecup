package com.ibay.tea.api.service.address;

import com.ibay.tea.dao.TbApiUserAddressMapper;
import com.ibay.tea.entity.TbApiUserAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ApiAddressService {

    @Resource
    private TbApiUserAddressMapper tbApiUserAddressMapper;


    public List<TbApiUserAddress> findUserAddressByOppenId(String oppenId,String storeId) {
        return tbApiUserAddressMapper.findUserAddressByOppenId(oppenId,storeId);
    }

    public TbApiUserAddress findUserAddressById(int id) {
        return tbApiUserAddressMapper.selectByPrimaryKey(id);
    }

    public void insertApiUserAddress(TbApiUserAddress tbApiUserAddress) {
        //判断地址是否是默认地址，如果是将数据库中已经存在的默认地址换成非默认地址
        if (StringUtils.isEmpty(tbApiUserAddress.getName())){
            tbApiUserAddress.setAddressName(tbApiUserAddress.getName());
        }else if (StringUtils.isEmpty(tbApiUserAddress.getAddress())){
            tbApiUserAddress.setAddressName(tbApiUserAddress.getAddress());
        }

        if (tbApiUserAddress.getIsDefault() == 1){
            tbApiUserAddressMapper.updateAddressNotDefault(tbApiUserAddress.getOppenId(),tbApiUserAddress.getStoreId());
        }
        tbApiUserAddressMapper.insert(tbApiUserAddress);
    }

    public void updateUserAddress(TbApiUserAddress tbApiUserAddress) {
        TbApiUserAddress dbAddress = tbApiUserAddressMapper.selectByPrimaryKey(tbApiUserAddress.getId());
        if (dbAddress == null){
            return;
        }
        if (tbApiUserAddress.getIsDefault() == 1){
            tbApiUserAddressMapper.updateAddressNotDefault(tbApiUserAddress.getOppenId(),tbApiUserAddress.getStoreId());
        }
        tbApiUserAddressMapper.deleteByPrimaryKey(tbApiUserAddress.getId());
        tbApiUserAddressMapper.saveUpdateApiUserAddress(tbApiUserAddress);
    }

    public void deleteApiUserAddress(Map<String, String> params) {
        tbApiUserAddressMapper.deleteApiUserAddress(params);
    }
}
