package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileEditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UploadEditDelegator
 *
 * @author junyu
 * @date 2022/5/11
 */
@Slf4j
@Delegator("uploadEditDelegator")
public class UploadEditDelegator {

    @Autowired
    private UploadFileEditService uploadFileService;
    /*
     * start step
     * */
    public  void start(BaseProcessClass bpc){

        logAbout("start");

    }

    public void preparetionData (BaseProcessClass bpc){
        logAbout("preparetionData");
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        //get all data of need Carry from DB
        start();

    }

    public void jobStart(){
        start();
    }
    /**********************************/

    private void start(){

            String data = uploadFileService.getData();
            log.info("------------------- getData  end --------------");
            //Parse the
            if (data==null){
                return;
            }
            List<ApplicationListFileDto> parse = uploadFileService.parse(data);
            if (parse.isEmpty()){
                return;
            }
            AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
            for(ApplicationListFileDto applicationListFileDto :parse){
                applicationListFileDto.setAuditTrailDto(intenet);
                Map<String,List<String>> map=new HashMap();
                List<String> oldStatus= IaisCommonUtils.genNewArrayList();
                oldStatus.add(ApplicationConsts.APPLICATION_GROUP_PENDING_ZIP);
                try {
                    uploadFileService.getRelatedDocuments(applicationListFileDto);
                    String grpId = uploadFileService.saveFile(applicationListFileDto);
                    if(StringUtil.isEmpty(grpId)){
                        continue;
                    }
                    log.info("------------------- saveFile  end --------------");
                    String compressFileName = uploadFileService.compressFile(grpId);
                    boolean rename = uploadFileService.renameAndSave(compressFileName,grpId);
                    log.info("------------------- compressFile  end --------------");
                    if(rename){
                        List<String> newStatus=IaisCommonUtils.genNewArrayList();
                        newStatus.add(ApplicationConsts.APPLICATION_SUCCESS_ZIP);
                        map.put("oldStatus",oldStatus);
                        map.put("newStatus",newStatus);
                        uploadFileService.changeStatus(applicationListFileDto,map);
                    }
                }catch (Throwable e){
                    Map<String,List<String>> errorMap=new HashMap();
                    List<String> newStatus=IaisCommonUtils.genNewArrayList();
                    List<String> errorOldStatus= IaisCommonUtils.genNewArrayList();
                    errorOldStatus.add(ApplicationConsts.APPLICATION_GROUP_PENDING_ZIP);
                    newStatus.add(ApplicationConsts.APPLICATION_GROUP_STATUS_PEND_TO_FE);
                    errorMap.put("oldStatus",errorOldStatus);
                    errorMap.put("newStatus",newStatus);
                    uploadFileService.changeStatus(applicationListFileDto,errorMap);
                }
            }

    }
    private  void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}
