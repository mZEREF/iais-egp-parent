package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
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
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/27 9:52
 **/
@Delegator("inspecSaveBeRecByFeDelegator")
@Slf4j
public class InspecSaveBeRecByFeBatchjob {

    @Autowired
    private InspecSaveBeRecByService inspecSaveBeRecByService;

    /**
     * StartStep: inspecSaveBeRecByFeStart
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFeStart(BaseProcessClass bpc){
        logAbout("Be Create Rec File");
    }

    /**
     * StartStep: inspecSaveBeRecByFePre
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFePre(BaseProcessClass bpc){
        logAbout("inspecSaveBeRecByFePre");
        List<ProcessFileTrackDto> processFileTrackDtos = inspecSaveBeRecByService.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        inspecSaveBeRecByService.deleteUnZipFile();
        Boolean saveDataFlag = false;
        if(!(IaisCommonUtils.isEmpty(processFileTrackDtos))) {
            inspecSaveBeRecByService.compressFile(processFileTrackDtos);
            saveDataFlag = inspecSaveBeRecByService.saveData(intranet, processFileTrackDtos);
        }

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
