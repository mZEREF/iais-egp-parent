package com.ecquaria.cloud.moh.iais.batch;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wenkang
 * @date 2020/7/21 10:10
 */
@JobHandler(value = "uploadDelegatorJob")
@Component
@Slf4j
public class UploadDelegatorJob extends IJobHandler {
    @Autowired
    private UploadFileService uploadFileService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            //get all data of need Carry from DB
            String data = uploadFileService.getData();
            log.info("------------------- getData  end --------------");
            //Parse the
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<ApplicationListFileDto> parse = uploadFileService.parse(data);
            AuditTrailDto intenet = AuditTrailHelper.getBatchJobAuditTrail();
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
                        List<String> newStatus= IaisCommonUtils.genNewArrayList();
                        newStatus.add(ApplicationConsts.APPLICATION_SUCCESS_ZIP);
                        map.put("oldStatus",oldStatus);
                        map.put("newStatus",newStatus);
                        uploadFileService.changeStatus(applicationListFileDto,map);
                    }
                }catch (Exception e){

                    List<String> newStatus=IaisCommonUtils.genNewArrayList();
                    newStatus.add(ApplicationConsts.APPLICATION_GROUP_ERROR_ZIP);
                    map.put("oldStatus",oldStatus);
                    map.put("newStatus",newStatus);
                    uploadFileService.changeStatus(applicationListFileDto,map);
                }

            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
