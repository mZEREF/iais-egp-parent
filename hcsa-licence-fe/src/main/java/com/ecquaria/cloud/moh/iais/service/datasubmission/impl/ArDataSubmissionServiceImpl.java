package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
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
    public Map<String, AppGrpPremisesDto> getArCenterPremises(String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewHashMap();
        }
        List<String> svcNames = new ArrayList<>();
        //svcNames.add(serviceName);
        List<AppGrpPremisesDto> appGrpPremisesDtos = licenceClient.getLatestPremisesByConds(licenseeId, svcNames, false).getEntity();
        Map<String, AppGrpPremisesDto> appGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
        if (appGrpPremisesDtos == null || appGrpPremisesDtos.isEmpty()) {
            return appGrpPremisesDtoMap;
        }
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
            if (!StringUtil.isEmpty(appGrpPremisesDto.getPremisesSelect())) {
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                appGrpPremisesDto.setExistingData(AppConsts.YES);
                appGrpPremisesDtoMap.put(appGrpPremisesDto.getHciCode(), appGrpPremisesDto);
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
    public String getSubmissionNo(String submissionType, String cycleType,
            DataSubmissionDto lastDataSubmissionDto) {
        String submissionNo = null;
        if (!StringUtil.isIn(cycleType, new String[]{DataSubmissionConsts.AR_CYCLE_NON,
                DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT})) {
            if (lastDataSubmissionDto != null
                    && submissionType.equals(lastDataSubmissionDto.getSubmissionType())
                    && !statuses.contains(lastDataSubmissionDto.getStatus())
                    && lastDataSubmissionDto.getSubmissionNo() != null) {
                submissionNo = lastDataSubmissionDto.getSubmissionNo();
            }
        }
        if (StringUtil.isEmpty(submissionNo)) {
            submissionNo = systemAdminClient.submissionID(submissionType).getEntity();
        }
        submissionNo = IaisCommonUtils.getNextSubmissionNo(submissionNo);
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public String getDraftNo(String submissionType) {
        return systemAdminClient.draftNumber(ApplicationConsts.DATA_SUBMISSION).getEntity();
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
            } catch (Exception e) {
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

    @Override
    public Date getLastCompletedCycleStartDate(String patientCode, String hciCode) {
        log.info(StringUtil.changeForLog("PatientCode: " + patientCode + " - hciCode: " + hciCode));
        if (StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        return arFeClient.getLastCompletedCycleStartDate(patientCode, hciCode).getEntity();
    }

    @Override
    public PatientInventoryDto setFreezingPatientChange(PatientInventoryDto patientInventoryDto, ArSubFreezingStageDto arSubFreezingStageDto) {
        if(patientInventoryDto != null && arSubFreezingStageDto != null) {
            String cryopreservedType = arSubFreezingStageDto.getCryopreservedType();
            if(DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_OOCYTE.equals(cryopreservedType)) {
                patientInventoryDto.setChangeFreshOocytes(arSubFreezingStageDto.getCryopreservedNum());
            } else if(DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_EMBRYO.equals(cryopreservedType)) {
                patientInventoryDto.setChangeFreshEmbryos(arSubFreezingStageDto.getCryopreservedNum());
            } else if(DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_OOCYTE.equals(cryopreservedType)) {
                patientInventoryDto.setChangeThawedOocytes(arSubFreezingStageDto.getCryopreservedNum());
            } else if(DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_EMBRYO.equals(cryopreservedType)) {
                patientInventoryDto.setChangeThawedEmbryos(arSubFreezingStageDto.getCryopreservedNum());
            }
        }
        return patientInventoryDto;
    }

    @Override
    public DonorSampleDto getDonorSampleDto(String idType, String idNumber, String donorSampleCode,String sampleFromHciCode,String sampleFromOthers) {
        return ((StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)) &&
                (StringUtil.isEmpty(donorSampleCode) || StringUtil.isEmpty(sampleFromHciCode))) ? null : arFeClient.getDonorSampleDto(idType,idNumber,donorSampleCode,sampleFromHciCode,sampleFromOthers).getEntity();
    }

    @Override
    public List<SelectOption> getSourceOfSemenOption() {
        List<SelectOption> sourceOfSemenOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.SOURCE_OF_SEMEN);
        if(!IaisCommonUtils.isEmpty(sourceOfSemenOption)) {
            for(int i = 0; i < sourceOfSemenOption.size(); i++) {
                SelectOption selectOption = sourceOfSemenOption.get(i);
                if("AR_SOS_004".equals(selectOption.getValue())) {
                    sourceOfSemenOption.remove(i);
                    break;
                }
            }
        }
        return sourceOfSemenOption;
    }

    @Override
    public List<SelectOption> getChildNumOption() {
        List<SelectOption> childNumOption = IaisCommonUtils.genNewArrayList();
        for(int i = 0; i <= 10; i++) {
            SelectOption selectOption = new SelectOption();
            selectOption.setValue(i + "");
            selectOption.setText(i + "");
            childNumOption.add(selectOption);
        }
        return childNumOption;
    }

    @Override
    public ArSuperDataSubmissionDto setIuiCycleStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission) {
        if(arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if (iuiCycleStageDto == null) {
                iuiCycleStageDto = new IuiCycleStageDto();
            }

            arSuperDataSubmission.setIuiCycleStageDto(iuiCycleStageDto);
        }
        return arSuperDataSubmission;
    }

}
