package com.ibay.tea.api.controller.recommend;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.recommend.ApiRecommendService;
import com.ibay.tea.entity.TbItem;
import org.apache.ibatis.annotations.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class ApiRecommendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRecommendController.class);

    @Resource
    private ApiRecommendService apiRecommendService;

    @RequestMapping("/list")
    public ResultInfo findRecommendList(@RequestBody Map<String,String> params){

        if (params == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        String storeId = params.get("storeId");

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> goodsList = apiRecommendService.findRecommendList(storeId);
            resultInfo.setData(goodsList);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("findRecommendList interface happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }
}
