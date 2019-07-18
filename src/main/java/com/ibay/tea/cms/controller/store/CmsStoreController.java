package com.ibay.tea.cms.controller.store;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.store.CmsStoreService;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/store")
public class CmsStoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsStoreController.class);

    @Resource
    private CmsStoreService cmsStoreService;


    @RequestMapping("/list/{storeIds}")
    public ResultInfo list(@PathVariable("storeIds") String storeIds){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbStore> tbStoreList;
        	if ("-1".equals(storeIds)){
                tbStoreList = cmsStoreService.findAll();
            }else {
                tbStoreList = cmsStoreService.findByIds(storeIds);
            }
            resultInfo.setData(tbStoreList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addStore(@RequestBody TbStore tbStore){

        if (tbStore == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsStoreService.addStore(tbStore);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteStore(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsStoreService.deleteStore(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateStore(@RequestBody TbStore tbStore){

        if (tbStore == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsStoreService.updateStore(tbStore);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }
}
