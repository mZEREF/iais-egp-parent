package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/27 9:52
 **/
@JobHandler(value="inspecSaveBeRecByFeJobHandler")
@Component
@Slf4j
public class InspecSaveBeRecByFeJobHandler extends IJobHandler {

    @Autowired
    private InspecSaveBeRecByService inspecSaveBeRecByService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("inspecSaveBeRecByFePre");
            List<ProcessFileTrackDto> processFileTrackDtos = inspecSaveBeRecByService.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                    ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
            AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            inspecSaveBeRecByService.deleteUnZipFile();
            if(!(IaisCommonUtils.isEmpty(processFileTrackDtos))) {
                List<String> reportIds = inspecSaveBeRecByService.compressFile(processFileTrackDtos);
                if(!IaisCommonUtils.isEmpty(reportIds)) {
                    inspecSaveBeRecByService.saveData(intranet, processFileTrackDtos, reportIds);
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }
}
