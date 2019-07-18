package com.ibay.tea.cms.controller.carousel;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.carousel.CmsCarouselService;
import com.ibay.tea.entity.TbCarousel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/carousel")
public class CmsCarouselController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCarouselController.class);

    @Resource
    private CmsCarouselService cmsCarouselService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbCarousel> list = cmsCarouselService.findAll();
            resultInfo.setData(list);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" cms carousel findAll happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo saveCarousel(@RequestBody TbCarousel tbCarousel){
        if (tbCarousel == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsCarouselService.saveCarousel(tbCarousel);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCarousel(@PathVariable("id") int id){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsCarouselService.deleteCarousel(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/update")
    public ResultInfo updateCarousel(@RequestBody TbCarousel tbCarousel){
        if (tbCarousel == null || tbCarousel.getId() == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsCarouselService.updateCarousel(tbCarousel);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("save carousel happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
