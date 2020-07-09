package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import java.util.List;

import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
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
    @Autowired
    private ApplicationClient applicationClient;
    /*
    * start step
    * */
    public  void start(BaseProcessClass bpc){

        logAbout("start");

    }

    public void preparetionData (BaseProcessClass bpc){
        logAbout("preparetionData");
        //get all data of need Carry from DB
        String data = uploadFileService.getData();
        log.info("------------------- getData  end --------------");
        //Parse the
        List<ApplicationListFileDto> parse = uploadFileService.parse(data);
        for(ApplicationListFileDto applicationListFileDto :parse){
            uploadFileService.getRelatedDocuments(applicationListFileDto);
            String grpId = uploadFileService.saveFile(applicationListFileDto);
            if(StringUtil.isEmpty(grpId)){
                continue;
            }
            log.info("------------------- saveFile  end --------------");
            String compressFileName = uploadFileService.compressFile(grpId);
            boolean rename = uploadFileService.renameAndSave(compressFileName,grpId);
            log.info("------------------- compressFile  end --------------");
            try {
                if(rename){
                    uploadFileService.changeStatus(applicationListFileDto);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        /*for(ApplicationListFileDto applicationListFileDto : parse){
            List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
            List<ApplicationDto> application = applicationListFileDto.getApplication();
            for(ApplicationDto applicationDto : application){
                String status = applicationDto.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(status)){
                    applicationDtoList.add(applicationDto);
                }
            }
            for( ApplicationDto applicationDto : applicationDtoList){
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            }
            try {
                log.info(applicationDtoList.toString());
                applicationClient.saveApplicationDtos(applicationDtoList);
                log.info("update application status");
            }catch (Exception e){
                log.error("update applcaition status is error",e);
            }
        }*/

    }

    /**********************************/

    private  void logAbout(String methodName){
       log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}
