package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @Description TopDataSubmissionServiceImpl
 * @Auther chenlei on 12/22/2021.
 */
@Slf4j
@Service
public class TopDataSubmissionServiceImpl implements TopDataSubmissionService {
    /*@Autowired
    private LicenceClient licenceClient;
*/
    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private TopFeClient topFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Autowired
    private DsLicenceService dsLicenceService;

    @Autowired
    private AppCommService appSubmissionService;
    @Autowired
    private DocInfoService docInfoService;

    @Override
    public Map<String, PremisesDto> getTopCenterPremises(String licenseeId) {
        /*if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        List<String> svcNames = new ArrayList<>();
        //TODO
        //svcNames.add(DataSubmissionConsts.SVC_NAME_AR_CENTER);
        List<PremisesDto> premisesDtos = licenceClient.getLatestPremisesByConds(licenseeId, svcNames, false).getEntity();
        Map<String, PremisesDto> premisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (premisesDtos == null || premisesDtos.isEmpty()) {
            return premisesDtoMap;
        }
        for (PremisesDto premisesDto : premisesDtos) {
            premisesDtoMap.put(DataSubmissionHelper.getPremisesMapKey(premisesDto), premisesDto);
        }
        return premisesDtoMap;*/
        return dsLicenceService.getTopCenterPremises(licenseeId);
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
    public TopSuperDataSubmissionDto saveDataSubmissionDraft(TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        return topFeClient.doUpdateDataSubmissionDraft(topSuperDataSubmissionDto).getEntity();
    }

    @Override
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDto(String submissionNo) {
        return topFeClient.getTopSuperDataSubmissionDto(submissionNo).getEntity();
    }

    @Override
    public String getSubmissionNo(String dsType) {
        String submissionNo = systemAdminClient.submissionID(dsType).getEntity();
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public TopSuperDataSubmissionDto saveTopSuperDataSubmissionDto(TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog("do the saveTopSuperDataSubmissionDto ..."));
        return topFeClient.saveTopSuperDataSubmissionDto(topSuperDataSubmissionDto).getEntity();
    }

    public TopSuperDataSubmissionDto saveBeTopSuperDataSubmissionDto(TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        return feEicGatewayClient.saveBeTopSuperDataSubmissionDto(topSuperDataSubmissionDto).getEntity();
    }

    @Override
    public TopSuperDataSubmissionDto saveTopSuperDataSubmissionDtoToBE(TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog(" the saveTopSuperDataSubmissionDtoToBE start ..."));
        topSuperDataSubmissionDto.setFe(false);
        topSuperDataSubmissionDto = saveBeTopSuperDataSubmissionDto(topSuperDataSubmissionDto);

        DataSubmissionDto dataSubmission = topSuperDataSubmissionDto.getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveTopSuperDataSubmissionDtoToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            Date now = new Date();
            eicRequestTrackingDto.setFirstActionAt(now);
            eicRequestTrackingDto.setLastActionAt(now);
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            try {
                topSuperDataSubmissionDto = saveBeTopSuperDataSubmissionDto(topSuperDataSubmissionDto);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
            } catch (Throwable e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
                licEicClient.saveEicTrack(eicRequestTrackingDto);
            }
        } else {
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:" + refNo));
        }
        log.info(StringUtil.changeForLog(" the saveTopSuperDataSubmissionDtoToBE end ..."));
        return topSuperDataSubmissionDto;
    }

    @Override
    public void updateDataSubmissionDraftStatus(String draftId, String status) {
        log.info(StringUtil.changeForLog("------Draft ID: " + draftId + " - Status: " + status + "------"));
        if (StringUtil.isEmpty(draftId) || StringUtil.isEmpty(status)) {
            return;
        }
        topFeClient.updateDataSubmissionDraftStatus(draftId, status);
    }

    @Override
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String userId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return null;
        }
        return topFeClient.getTopSuperDataSubmissionDtoDraftByConds(orgId, submissionType, userId).getEntity();
    }

    @Override
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType,String dataSubmissionId, String userId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : " + dataSubmissionId +"-----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return null;
        }
        return topFeClient.getTopSuperDataSubmissionDtoRfcDraftByConds(orgId, submissionType, dataSubmissionId, userId).getEntity();
    }

    @Override
    public void deleteTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String appType) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return;
        }
        topFeClient.deleteTopSuperDataSubmissionDtoDraftByConds(orgId, submissionType, appType);
    }

    @Override
    public void deleteTopSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String dataSubmissionId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return;
        }
        topFeClient.deleteTopSuperDataSubmissionDtoRfcDraftByConds(orgId, submissionType, dataSubmissionId);
    }

    @Override
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Param - Draft No.: " + draftNo));
        if (StringUtil.isEmpty(draftNo) ) {
            return null;
        }
        return topFeClient.getTopSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
    }

    @Override
    public PatientInformationDto getTopPatientSelect(String idType, String idNumber, String orgId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + idType
                + " : " + idNumber + " -----"));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return topFeClient.getTopPatientSelect(idType, idNumber, orgId).getEntity();
    }

    @Override
    public void displayToolTipJudgement(HttpServletRequest request) {
        TopSuperDataSubmissionDto currentTopDataSubmission = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = currentTopDataSubmission.getTerminationOfPregnancyDto();
        if (terminationOfPregnancyDto != null) {
            TerminationDto terminationDto = terminationOfPregnancyDto.getTerminationDto();
            if (terminationDto != null) {
                ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(terminationDto.getDoctorRegnNo());
                DoctorInformationDto doctorInformationDto = docInfoService.getDoctorInformationDtoByConds(terminationDto.getDoctorRegnNo(), DataSubmissionConsts.DOCTOR_SOURCE_ELIS_TOP, currentTopDataSubmission.getHciCode());
                if (professionalResponseDto != null && doctorInformationDto != null
                        && ("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()))) {
                    //PRN number doesn't exist in PRS but exist in eLis
                    terminationDto.setToolTipShow("N");
                } else if (professionalResponseDto != null && doctorInformationDto == null && StringUtils.hasLength(professionalResponseDto.getName())) {
                    //PRN number doesn't exist in eLis but exist in PRS
                    terminationDto.setToolTipShow("N");
                } else if (professionalResponseDto != null && doctorInformationDto == null
                        && ("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode()))){
                    // PRN number doesn't exist in both (PRS and eLis)
                    terminationDto.setToolTipShow("N");
                } else {
                    terminationDto.setToolTipShow("Y");
                }
            }
        }
        DataSubmissionHelper.setCurrentTopDataSubmission(currentTopDataSubmission,request);
    }

    /* @Override
    public String getDraftNo() {
        String draftNo = systemAdminClient.draftNumber(ApplicationConsts.DATA_SUBMISSION).getEntity();
        log.info(StringUtil.changeForLog("The Draft No: " + draftNo));
        return draftNo;
    }

    @Override
    public String getSubmissionNo() {
        String submissionNo = systemAdminClient.submissionID(DataSubmissionConsts.DS_TOP).getEntity();
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }*/
}
