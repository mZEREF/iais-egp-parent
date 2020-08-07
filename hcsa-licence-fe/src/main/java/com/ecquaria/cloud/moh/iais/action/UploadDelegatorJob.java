package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/7/21 10:10
 */
@JobHandler(value = "UploadDelegatorJob")
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

        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
