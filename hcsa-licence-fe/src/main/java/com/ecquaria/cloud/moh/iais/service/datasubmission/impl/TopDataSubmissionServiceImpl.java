package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TopDataSubmissionServiceImpl
 * @Auther chenlei on 12/22/2021.
 */
@Slf4j
@Service
public class TopDataSubmissionServiceImpl implements TopDataSubmissionService {
    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private TopFeClient topFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Override
    public Map<String, PremisesDto> getTopCenterPremises(String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)) {
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
        return premisesDtoMap;
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
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
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
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String svcName, String hciCode) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : "
                + svcName + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        return topFeClient.getTopSuperDataSubmissionDtoDraftByConds(orgId, submissionType, svcName, hciCode).getEntity();
    }

    @Override
    public void deleteTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType) || StringUtil.isEmpty(hciCode)) {
            return;
        }
        topFeClient.deleteTopSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
    }

    @Override
    public TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Param - Draft No.: " + draftNo));
        if (StringUtil.isEmpty(draftNo) ) {
            return null;
        }
        return topFeClient.getTopSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
    }
}
