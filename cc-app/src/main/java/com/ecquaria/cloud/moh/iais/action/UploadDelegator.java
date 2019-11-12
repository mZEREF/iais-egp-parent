package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;

import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


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
        uploadFileService.uploadFile(data);
        String s = uploadFileService.changeStatus();
        System.out.println(s+"==========================");

    }


    /**********************************/

    private  void logAbout(String methodName){
       log.debug("**** The " +methodName +" Start ****");
    }
}
