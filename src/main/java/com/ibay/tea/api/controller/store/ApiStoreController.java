package com.ibay.tea.api.controller.store;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.map.ApiMapService;
import com.ibay.tea.api.service.store.ApiStoreService;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store")
public class ApiStoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiStoreController.class);

    @Resource
    private StoreCache storeCache;

    @Resource
    private ApiMapService apiMapService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbStore> all = storeCache.getStoreList();
            resultInfo.setData(all);
            return  resultInfo;
        }catch (Exception e){
            LOGGER.error("findAll store info happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/selectStore")
    public ResultInfo selectStore(@RequestBody Map<String,String> params){

     if (params == null){
     	return ResultInfo.newEmptyParamsResultInfo();
     }

     try {
     	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
         LOGGER.info("oppenId : {}",params.get("oppenId"));
         List<TbStore> storeList = storeCache.getStoreList();
         TbStore store = apiMapService.selectStore(storeList, params);
         resultInfo.setData(store);
         return resultInfo;
     }catch (Exception e){
     	return ResultInfo.newExceptionResultInfo();
     }

    }
}
