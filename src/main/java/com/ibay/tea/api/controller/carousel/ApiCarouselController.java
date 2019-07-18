package com.ibay.tea.api.controller.carousel;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.carousel.ApiCarouselService;
import com.ibay.tea.entity.TbCarousel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/carousel")
public class ApiCarouselController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCarouselController.class);

    @Resource
    private ApiCarouselService apiCarouselService;

    @RequestMapping("/findAll")
    public ResultInfo findAll(@RequestBody Map<String,Integer> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try{
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            Integer storeId = params.get("storeId");
            if (storeId == null){
                storeId = -1;
            }
            storeId = params.get("storeId");
            List<TbCarousel> list = apiCarouselService.findCarouselByStoreId(storeId);
            resultInfo.setData(list);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("carousel findAll happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }
}
