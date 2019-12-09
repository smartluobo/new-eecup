package com.ibay.tea.api.controller.category;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.category.ApiCategoryService;
import com.ibay.tea.entity.TbItemCat;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/category")
public class ApiCategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCategoryController.class);

    @Resource
    private ApiCategoryService apiCategoryService;

    @RequestMapping("list")
    public ResultInfo list(HttpServletRequest request){
        try {
            String storeId = request.getParameter("storeId");
            LOGGER.info("ApiCategoryService list param : {}", storeId);
            List<TbItemCat> catList;
            if (StringUtils.isNotEmpty(storeId)){
                catList = apiCategoryService.findByStoreId(storeId);
            }else{
                catList = apiCategoryService.findByStoreId("-1");
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(catList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("ApiCategoryController list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
