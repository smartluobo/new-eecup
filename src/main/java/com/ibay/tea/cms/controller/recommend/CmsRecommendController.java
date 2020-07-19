package com.ibay.tea.cms.controller.recommend;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.recommend.CmsRecommendService;
import com.ibay.tea.entity.TbRecommend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("cms/recommend")
public class CmsRecommendController {


    @Resource
    private CmsRecommendService cmsRecommendService;

    @RequestMapping("/list/{storeId}")
    public ResultInfo list(@PathVariable("storeId") int storeId){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbRecommend> recommends = cmsRecommendService.findRecommendByStoreId(storeId);
            resultInfo.setData(recommends);
            return resultInfo;
        }catch (Exception e){
            log.error("list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addRecommend(@RequestBody TbRecommend recommend){
        if (recommend == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsRecommendService.addRecommend(recommend);
            return resultInfo;
        }catch (Exception e){
            log.error("cms recommend add happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteRecommend(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsRecommendService.deleteRecommend(id);
            return resultInfo;
        }catch (Exception e){
            log.error("cms recommend delete id : {} happen exception ",id,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateRecommend(@RequestBody TbRecommend recommend){

        if (recommend == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsRecommendService.updateRecommend(recommend);
            return resultInfo;
        }catch (Exception e){
            log.error("cms printer update happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

}
