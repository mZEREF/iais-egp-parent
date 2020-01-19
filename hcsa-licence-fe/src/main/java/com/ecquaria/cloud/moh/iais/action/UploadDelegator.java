package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.cloud.moh.iais.service.impl.UploadFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;


/**
 * @author Wenkang
 * @date 2019/11/6 20:54
 */
@Slf4j
@Delegator("uploadDelegator")
public class UploadDelegator {
    @Autowired
    private UploadFileService uploadFileService;

    /*
    * start step
    * */
    public  void start(BaseProcessClass bpc){

        logAbout("start");

    }

    public void preparetionData (BaseProcessClass bpc){
        logAbout("preparetionData");
        String data = uploadFileService.getData();
        log.info("------------------- getData  end --------------");
        List<ApplicationListFileDto> parse = UploadFileServiceImpl.parse(data);
        for(ApplicationListFileDto applicationListFileDto :parse){

            String s = JsonUtil.parseToJson(applicationListFileDto);
            uploadFileService.saveFile(s);
            log.info("------------------- saveFile  end --------------");
            boolean b = uploadFileService.compressFile();
            log.info("------------------- compressFile  end --------------");
            try {
                if(b){
                    ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(data, ApplicationListFileDto.class);
                    uploadFileService.changeStatus(applicationListDto);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }


        }


    }


    /**********************************/

    private  void logAbout(String methodName){
       log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}
