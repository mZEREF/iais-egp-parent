package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description ArDataSubmissionServiceImpl
 * @Auther chenlei on 10/22/2021.
 */
@Service
@Slf4j
public class ArDataSubmissionServiceImpl implements ArDataSubmissionService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private ArFeClient arFeClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    private static final List<String> statuses = IaisCommonUtils.getDsCycleFinalStatus();
    @Override
    public Map<String, AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName) {
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        List<String> svcNames = new ArrayList<>();
        if (!StringUtil.isEmpty(serviceName)) {
            svcNames.add(serviceName);
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos = licenceClient.getLatestPremisesByConds(licenseeId, svcNames, false).getEntity();
        Map<String, AppGrpPremisesDto> appGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (appGrpPremisesDtos == null || appGrpPremisesDtos.isEmpty()) {
            return appGrpPremisesDtoMap;
        }
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
            if (!StringUtil.isEmpty(appGrpPremisesDto.getPremisesSelect())) {
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                appGrpPremisesDto.setExistingData(AppConsts.YES);
                appGrpPremisesDtoMap.put(appGrpPremisesDto.getPremisesSelect(), appGrpPremisesDto);
            }
        }
        return appGrpPremisesDtoMap;
    }

    @Override
    public CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return arFeClient.getCycleStageSelectionDtoByConds(idType, idNumber, nationality, orgId, hciCode).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCOde) {
        if (StringUtil.isEmpty(patientCode)) {
            log.warn("----- No Patient Code -----");
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDto(patientCode, hciCOde).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDto ..."));
        return arFeClient.saveArSuperDataSubmissionDto(arSuperDataSubmission).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        return arFeClient.doUpdateDataSubmissionDraft(arSuperDataSubmissionDto).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftById(String id) {
        log.info(StringUtil.changeForLog("----- Param: " + id + " -----"));
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftById(id).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDtoToBE(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoToBE start ..."));
        arSuperDataSubmission.setFe(false);
        arSuperDataSubmission = saveBeArSuperDataSubmissionDto(arSuperDataSubmission);

        DataSubmissionDto dataSubmission = arSuperDataSubmission.getCurrentDataSubmissionDto();
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
        return arSuperDataSubmission;
    }

    public ArSuperDataSubmissionDto saveBeArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        return feEicGatewayClient.saveBeArSuperDataSubmissionDto(arSuperDataSubmission).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
            String orgId, String hciCode) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + hciCode + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)
                || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftByConds(idType, idNumber, nationality, orgId, hciCode).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return null;
        }
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            hciCode = null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode).getEntity();
    }

    @Override
    public void deleteArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
            String orgId, String hciCode) {
        log.info("----- Delete Param: " + orgId + " : " + hciCode + " : " + idType + " : " + idNumber + " : "
                + nationality + " -----");
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)
                || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(hciCode)) {
            return;
        }
        arFeClient.deleteArSuperDataSubmissionDtoDraftByConds(idType, idNumber, nationality, orgId, hciCode);
    }

    @Override
    public void deleteArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        log.info("----- Delete Param: " + orgId + " : " + submissionType + " -----");
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return;
        }
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            hciCode = null;
        }
        arFeClient.deleteArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
    }

    @Override
    public String getSubmissionNo(String submisisonType, String cycleStage, DataSubmissionDto lastDataSubmissionDto) {
        String submissionNo = null;
        int serialNo = 0;
        if (!DataSubmissionConsts.DATA_SUBMISSION_CYCLE_STAGE_PATIENT.equals(cycleStage)) {
            if (lastDataSubmissionDto != null
                    && submisisonType.equals(lastDataSubmissionDto.getSubmissionType())
                    && !statuses.contains(lastDataSubmissionDto.getStatus())
                    && lastDataSubmissionDto.getSubmissionNo() != null) {
                String[] previous = lastDataSubmissionDto.getSubmissionNo().split("-");
                submissionNo = previous[0];
                serialNo = Integer.parseInt(previous[1]);
            }
            serialNo++;
        }
        if (StringUtil.isEmpty(submissionNo)) {
            submissionNo = systemAdminClient.submissionID(submisisonType).getEntity();
        }
        if (serialNo != 0) {
            submissionNo += "-" + Formatter.formatNumber(serialNo, "00");
        }
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public ArSubFreezingStageDto setFreeCryoNumAndDate(ArSubFreezingStageDto arSubFreezingStageDto, String cryopreservedNum,
            String cryopreservationDate) {
        if (!StringUtil.isEmpty(cryopreservedNum)) {
            try {
                int cryopreservedNo = Integer.parseInt(cryopreservedNum);
                arSubFreezingStageDto.setCryopreservedNum(cryopreservedNo);
            } catch (Exception e) {
                log.info("Freezing invalid cryopreservedNum");
            }
        }
        if (!StringUtil.isEmpty(cryopreservationDate)) {
            try {
                Date date = Formatter.parseDate(cryopreservationDate);
                arSubFreezingStageDto.setCryopreservedDate(date);
            } catch (ParseException e) {
                log.info("Freezing invalid cryopreservationDate");
            }
        }
        return arSubFreezingStageDto;
    }

    @Override
    public ArSubFreezingStageDto checkValueIsDirtyData(String freeCryoRadio, ArSubFreezingStageDto arSubFreezingStageDto, List<SelectOption> freeCryoOptions) {
        if(!IaisCommonUtils.isEmpty(freeCryoOptions)) {
            boolean codeValueFlag = false;
            for(SelectOption selectOption : freeCryoOptions) {
                if(selectOption != null) {
                    String codeValue = selectOption.getValue();
                    if(!StringUtil.isEmpty(codeValue) && codeValue.equals(freeCryoRadio)) {
                        codeValueFlag = true;
                        break;
                    }
                }
            }
            if(codeValueFlag) {
                arSubFreezingStageDto.setCryopreservedType(freeCryoRadio);
            } else {
                arSubFreezingStageDto.setCryopreservedType(null);
            }
        }
        return arSubFreezingStageDto;
    }

    @Override
    public List<CycleDto> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(String patientCode, String hciCode, String cycleType, String ... status) {
        CycleDto cycleDto = new CycleDto();
        cycleDto.setPatientCode(patientCode);
        cycleDto.setHciCode(hciCode);
        cycleDto.setCycleType(cycleType);
        cycleDto.setStatuses(IaisCommonUtils.isEmpty(status) ? statuses : Arrays.asList(status));
        return arFeClient.getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(cycleDto).getEntity();
    }

    @Override
    public void updateDataSubmissionDraftStatus(String draftId, String status) {
        log.info(StringUtil.changeForLog("------Draft ID: " + draftId + " - Status: " + status + "------"));
        if (StringUtil.isEmpty(draftId) || StringUtil.isEmpty(status)) {
            return;
        }
        arFeClient.updateDataSubmissionDraftStatus(draftId, status);
    }

}
