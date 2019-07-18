package com.ibay.tea.common;

import com.ibay.tea.cms.config.CmsSysProperties;
import com.ibay.tea.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    @Resource
    private CmsSysProperties cmsSysProperties;

    public String imageUpload(MultipartFile file){
        if (file.isEmpty()){
            return null;
        }
        long currentTime = System.currentTimeMillis();
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String dateYyyyMMdd = DateUtil.getDateYyyyMMdd();
        fileName = currentTime+suffixName; // 新文件名
        String imagePath = cmsSysProperties.getImagePath() + dateYyyyMMdd + File.separator + fileName;
        LOGGER.info("imagePath : {}",imagePath);
        File dest = new File(cmsSysProperties.getImagePath() + dateYyyyMMdd+File.separator+ fileName);
        if (!dest.getParentFile().exists()) {
            boolean mkdirs = dest.getParentFile().mkdirs();
            if (!mkdirs){
                return null;
            }
            LOGGER.info("parent file mkdirs success.....");
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = cmsSysProperties.getImageUrlPrefix() + dateYyyyMMdd+"/" + fileName;
        return filename;
    }
}
