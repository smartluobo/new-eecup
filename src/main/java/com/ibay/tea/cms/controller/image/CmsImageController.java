package com.ibay.tea.cms.controller.image;

import com.ibay.tea.common.CommonService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("cms/image")
public class CmsImageController {

    @Resource
    private CommonService commonService;

    @PostMapping("upload")
    public String imageUpload(@RequestParam(value = "file") MultipartFile file){
        return commonService.imageUpload(file);
    }
}
