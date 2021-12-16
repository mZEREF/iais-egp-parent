package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description DpDataSubmissionServiceImpl
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
@Service
public class DpDataSubmissionServiceImpl implements DpDataSubmissionService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private DpFeClient dpFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Override
    public Map<String, PremisesDto> getDpCenterPremises(String licenseeId) {
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
        dpSuperDataSubmissionDto = saveBeArSuperDataSubmissionDto(dpSuperDataSubmissionDto);

        DataSubmissionDto dataSubmission = dpSuperDataSubmissionDto.getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
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
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType) || StringUtil.isEmpty(hciCode)) {
            return;
        }
        dpFeClient.deleteDpSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
    }


}
