package com.ibay.tea.cms.controller.recommend;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.recommend.CmsRecommendService;
import com.ibay.tea.entity.TbRecommend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/recommend")
public class CmsRecommendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsRecommendController.class);

    @Resource
    private CmsRecommendService cmsRecommendService;

    @RequestMapping("/list/{storeId}")
    public ResultInfo list(@PathVariable("storeId") int storeId){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbRecommend> recommends = cmsRecommendService.findRecommendByStoreId(storeId);
            resultInfo.setData(recommends);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addRecommend(@RequestBody TbRecommend recommend){
        if (recommend == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsRecommendService.addRecommend(recommend);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms recommend add happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteRecommend(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsRecommendService.deleteRecommend(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms recommend delete id : {} happen exception ",id,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateRecommend(@RequestBody TbRecommend recommend){

        if (recommend == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsRecommendService.updateRecommend(recommend);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms printer update happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

}
