package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @Description VssDataSubmissionServiceImpl
 * @Auther chenlei on 12/15/2021.
 */
@Service
@Slf4j
public class VssDataSubmissionServiceImpl implements VssDataSubmissionService {
    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Autowired
    private  VssFeClient vssFeClient;

    @Autowired
    private DsLicenceService dsLicenceService;

    @Autowired
    private AppCommService appSubmissionService;
    @Autowired
    private DocInfoService docInfoService;

    @Override
    public Map<String, PremisesDto> getVssCenterPremises(String licenseeId) {
        return dsLicenceService.getVssCenterPremises(licenseeId);
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
    public VssSuperDataSubmissionDto saveDataSubmissionDraft(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog("do the saveVssSuperDataSubmissionDto ..."));
        return vssFeClient.doUpdateDataSubmissionDraft(vssSuperDataSubmissionDto).getEntity();
    }

    @Override
    public VssSuperDataSubmissionDto getVssSuperDataSubmissionDto(String submissionNo) {
        log.info(StringUtil.changeForLog("----- Param - Sumission No.: " + submissionNo));
        if (StringUtil.isEmpty(submissionNo) ) {
            return null;
        }
        return vssFeClient.getVssSuperDataSubmissionDto(submissionNo).getEntity();
    }

    @Override
    public String getSubmissionNo(String dsType) {
        String submissionNo = systemAdminClient.submissionID(dsType).getEntity();
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public VssSuperDataSubmissionDto saveVssSuperDataSubmissionDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog("do the saveVssSuperDataSubmissionDto ..."));
        return vssFeClient.saveVssSuperDataSubmissionDto(vssSuperDataSubmissionDto).getEntity();
    }
    public VssSuperDataSubmissionDto saveBeVssSuperDataSubmissionDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        return feEicGatewayClient.saveBeVssSuperDataSubmissionDto(vssSuperDataSubmissionDto).getEntity();
    }


    @Override
    public VssSuperDataSubmissionDto saveVssSuperDataSubmissionDtoToBE(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog(" the saveVssSuperDataSubmissionDtoToBE start ..."));
        vssSuperDataSubmissionDto.setFe(false);
        DataSubmissionDto dataSubmission = vssSuperDataSubmissionDto.getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveVssSuperDataSubmissionDtoToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            Date now = new Date();
            eicRequestTrackingDto.setFirstActionAt(now);
            eicRequestTrackingDto.setLastActionAt(now);
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            try {
                vssSuperDataSubmissionDto = saveBeVssSuperDataSubmissionDto(vssSuperDataSubmissionDto);
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                licEicClient.saveEicTrack(eicRequestTrackingDto);
            } catch (Throwable e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
                licEicClient.saveEicTrack(eicRequestTrackingDto);
            }
        } else {
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:" + refNo));
        }
        log.info(StringUtil.changeForLog(" the saveVssSuperDataSubmissionDtoToBE end ..."));

        return vssSuperDataSubmissionDto;
    }

    @Override
    public void updateDataSubmissionDraftStatus(String draftId, String status) {
        log.info(StringUtil.changeForLog("------Draft ID: " + draftId + " - Status: " + status + "------"));
        if (StringUtil.isEmpty(draftId) || StringUtil.isEmpty(status)) {
            return;
        }
        vssFeClient.updateDataSubmissionDraftStatus(draftId, status);
    }

    @Override
    public VssSuperDataSubmissionDto getVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String userId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return null;
        }
        return vssFeClient.getVssSuperDataSubmissionDtoDraftByConds(orgId, submissionType, userId).getEntity();
    }

    @Override
    public void deleteVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return;
        }
        vssFeClient.deleteVssSuperDataSubmissionDtoDraftByConds(orgId, submissionType);
    }

    @Override
    public VssSuperDataSubmissionDto getVssSuperDataSubmissionDtoByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Param - Draft No.: " + draftNo));
        if (StringUtil.isEmpty(draftNo) ) {
            return null;
        }
        return vssFeClient.getVssSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
    }

    @Override
    public void displayToolTipJudgement(HttpServletRequest request) {
        VssSuperDataSubmissionDto currentVssDataSubmission = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        VssTreatmentDto vssTreatmentDto = currentVssDataSubmission.getVssTreatmentDto();
        if (vssTreatmentDto != null){
            SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto();
            if (sexualSterilizationDto != null){
                ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(sexualSterilizationDto.getDoctorReignNo());
                DoctorInformationDto doctorInformationDto = docInfoService.getDoctorInformationDtoByConds(sexualSterilizationDto.getDoctorReignNo(), DataSubmissionConsts.DOCTOR_SOURCE_ELIS_VSS, currentVssDataSubmission.getCycleDto().getHciCode());
                if (professionalResponseDto != null && doctorInformationDto != null
                        && ("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()))) {
                    //PRN number doesn't exist in PRS but exist in eLis
                    sexualSterilizationDto.setToolTipShow("N");
                } else if (professionalResponseDto != null && doctorInformationDto == null && StringUtils.hasLength(professionalResponseDto.getName())) {
                    //PRN number doesn't exist in eLis but exist in PRS
                    sexualSterilizationDto.setToolTipShow("N");
                } else if (professionalResponseDto != null && doctorInformationDto == null
                        && ("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()))){
                    // PRN number doesn't exist in both (PRS and eLis)
                    sexualSterilizationDto.setToolTipShow("N");
                } else {
                    sexualSterilizationDto.setToolTipShow("Y");
                }
            }
        }
        DataSubmissionHelper.setCurrentVssDataSubmission(currentVssDataSubmission,request);
    }
}
