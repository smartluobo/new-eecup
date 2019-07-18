package com.ibay.tea.cms.controller.menu;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.menu.CmsMenuService;
import com.ibay.tea.entity.TbCmsMenu;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/menu")
public class CmsMenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsMenuController.class);

    @Resource
    private CmsMenuService cmsMenuService;

    @RequestMapping("/findUserMenu/{menuIds}")
    public ResultInfo findUserMenu(@PathVariable("menuIds") String menuIds){
        if (StringUtils.isEmpty(menuIds)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms findUserMenu happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbCmsMenu> menuList = cmsMenuService.findAll();
            resultInfo.setData(menuList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/findByIds/{menuIds}")
    public ResultInfo findByIds(@PathVariable("menuIds") String menuIds){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbCmsMenu> menuList = cmsMenuService.findByIds(menuIds);
            resultInfo.setData(menuList);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }



    @RequestMapping("/add")
    public ResultInfo addMenu(@RequestBody TbCmsMenu tbCmsMenu){
        if (tbCmsMenu == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsMenuService.addMenu(tbCmsMenu);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteMenu(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsMenuService.deleteMenu(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateMenu(@RequestBody TbCmsMenu tbCmsMenu){

        if (tbCmsMenu == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsMenuService.updateMenu(tbCmsMenu);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }
}
