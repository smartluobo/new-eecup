package com.ibay.tea.cms.controller.carousel;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.carousel.CmsCarouselService;
import com.ibay.tea.entity.TbCarousel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("cms/carousel")
public class CmsCarouselController {


    @Resource
    private CmsCarouselService cmsCarouselService;

    @RequestMapping("/list")
    public ResultInfo list(int storeId){
        if (storeId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            if (storeId == -1){
                List<TbCarousel> list = cmsCarouselService.findAll();
                resultInfo.setData(list);
            }else {
                List<TbCarousel> list = cmsCarouselService.findCarouselByStoreId(storeId);
                resultInfo.setData(list);
            }

            return resultInfo;
        }catch (Exception e){
            log.error(" cms carousel findAll happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo saveCarousel(@RequestBody TbCarousel tbCarousel){
        if (tbCarousel == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsCarouselService.saveCarousel(tbCarousel);
            return resultInfo;
        }catch (Exception e){
            log.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCarousel(@PathVariable("id") int id){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsCarouselService.deleteCarousel(id);
            return resultInfo;
        }catch (Exception e){
            log.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/update")
    public ResultInfo updateCarousel(@RequestBody TbCarousel tbCarousel){
        if (tbCarousel == null || tbCarousel.getId() == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsCarouselService.updateCarousel(tbCarousel);
            return resultInfo;
        }catch (Exception e){
            log.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
