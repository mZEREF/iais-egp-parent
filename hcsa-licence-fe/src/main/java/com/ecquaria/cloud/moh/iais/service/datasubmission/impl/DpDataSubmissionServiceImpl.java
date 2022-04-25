package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugMedicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description DpDataSubmissionServiceImpl
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
@Service
public class DpDataSubmissionServiceImpl implements DpDataSubmissionService {


    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private DpFeClient dpFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private DsLicenceService dsLicenceService;

    @Override
    public Map<String, PremisesDto> getDpCenterPremises(String licenseeId) {
        return dsLicenceService.getDpCenterPremises(licenseeId);
    }

    @Override
    public String getDraftNo(String dsType, String draftNo) {
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = systemAdminClient.draftNumber(ApplicationConsts.DATA_SUBMISSION).getEntity();
        }
        log.info(StringUtil.changeForLog("The Draft No: " + draftNo));
        return draftNo;
    }

    @Override
    public DpSuperDataSubmissionDto saveDataSubmissionDraft(DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDto ..."));
        return dpFeClient.doUpdateDataSubmissionDraft(dpSuperDataSubmissionDto).getEntity();
    }

    @Override
    public String getSubmissionNo(String dsType) {
        String submissionNo = systemAdminClient.submissionID(dsType).getEntity();
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public DpSuperDataSubmissionDto saveDpSuperDataSubmissionDto(DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDto ..."));
        return dpFeClient.saveDpSuperDataSubmissionDto(dpSuperDataSubmissionDto).getEntity();
    }

    public DpSuperDataSubmissionDto saveBeArSuperDataSubmissionDto(DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        return feEicGatewayClient.saveBeDpSuperDataSubmissionDto(dpSuperDataSubmissionDto).getEntity();
    }

    @Override
    public DpSuperDataSubmissionDto saveDpSuperDataSubmissionDtoToBE(DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE start ..."));
        dpSuperDataSubmissionDto.setFe(false);
        DataSubmissionDto dataSubmission = dpSuperDataSubmissionDto.getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            Date now = new Date();
            eicRequestTrackingDto.setFirstActionAt(now);
            eicRequestTrackingDto.setLastActionAt(now);
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            try {
                dpSuperDataSubmissionDto = saveBeArSuperDataSubmissionDto(dpSuperDataSubmissionDto);
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                licEicClient.saveEicTrack(eicRequestTrackingDto);
            } catch (Throwable e) {
                licEicClient.saveEicTrack(eicRequestTrackingDto);
            }
        } else {
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:" + refNo));
        }
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE end ..."));




        return dpSuperDataSubmissionDto;
    }

    @Override
    public void updateDataSubmissionDraftStatus(String draftId, String status) {
        log.info(StringUtil.changeForLog("------Draft ID: " + draftId + " - Status: " + status + "------"));
        if (StringUtil.isEmpty(draftId) || StringUtil.isEmpty(status)) {
            return;
        }
        dpFeClient.updateDataSubmissionDraftStatus(draftId, status);
    }

    @Override
    public DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType,
            String svcName, String hciCode) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : "
                + svcName + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        return dpFeClient.getDpSuperDataSubmissionDtoDraftByConds(orgId, submissionType, svcName, hciCode).getEntity();
    }

    @Override
    public void deleteDpSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        log.info(StringUtil.changeForLog("-----Draft Param: " + orgId + " : " + submissionType + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType) || StringUtil.isEmpty(hciCode)) {
            return;
        }
        dpFeClient.deleteDpSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
    }

    @Override
    public ProfessionalResponseDto retrievePrsInfo(String regNo) {
        return appSubmissionService.retrievePrsInfo(regNo);
    }

    @Override
    public DpSuperDataSubmissionDto getDpSuperDataSubmissionDto(String submissionNo) {
        log.info(StringUtil.changeForLog("----- Param - Sumission No.: " + submissionNo));
        if (StringUtil.isEmpty(submissionNo) ) {
            return null;
        }
        return dpFeClient.getDpSuperDataSubmissionDto(submissionNo).getEntity();
    }

    @Override
    public DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Param - Draft No.: " + draftNo));
        if (StringUtil.isEmpty(draftNo) ) {
            return null;
        }
        return dpFeClient.getDpSuperDataSubmissionDtoByDraftNo(draftNo).getEntity();
    }

    @Override
    public List<DrugMedicationDto> getDrugMedicationDtoBySubmissionNo(String submissionNo) {
        log.info(StringUtil.changeForLog("The getDrugMedicationDtoBySubmissionNo submissionNo is -->:"+submissionNo));
        if (StringUtil.isEmpty(submissionNo) ) {
            return null;
        }
        return dpFeClient.getDrugMedicationDtoBySubmissionNo(submissionNo).getEntity();
    }

    @Override
    public List<DrugMedicationDto> getDrugMedicationDtoBySubmissionNoForDispensed(String submissionNo,String rfcSubmissionNo) {
        log.info(StringUtil.changeForLog("The getDrugMedicationDtoBySubmissionNoForDispensed submissionNo is -->:"+submissionNo));
        log.info(StringUtil.changeForLog("The submissionNo is -->:"+submissionNo));
        log.info(StringUtil.changeForLog("The rfcSubmissionNo is -->:"+rfcSubmissionNo));
        if (StringUtil.isEmpty(submissionNo) ) {
            return null;
        }
        return dpFeClient.getDrugMedicationDtoBySubmissionNoForDispensed(submissionNo,rfcSubmissionNo).getEntity();
    }


}
