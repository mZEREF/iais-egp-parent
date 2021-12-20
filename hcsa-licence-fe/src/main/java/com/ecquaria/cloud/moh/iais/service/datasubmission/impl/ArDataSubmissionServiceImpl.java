package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Autowired
    private ComFileRepoClient comFileRepoClient;

    private static final List<String> statuses = IaisCommonUtils.getDsCycleFinalStatus();

    @Override
    public Map<String, PremisesDto> getArCenterPremises(String licenseeId) {
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
            premisesDtoMap.put(DataSubmissionHelper.getPremisesMapKey(premisesDto, DataSubmissionConsts.DS_AR),
                    premisesDto);
        }
        return premisesDtoMap;
    }

    @Override
    public CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(
                orgId)) {
            return null;
        }
        return arFeClient.getCycleStageSelectionDtoByConds(idType, idNumber, nationality, orgId, hciCode).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoBySubmissionNo(String submissionNo) {
        log.info(StringUtil.changeForLog("----- Submission No: " + submissionNo + " -----"));
        if (StringUtil.isEmpty(submissionNo)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDto(submissionNo).getEntity();
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
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Draft No: " + draftNo + " -----"));
        if (StringUtil.isEmpty(draftNo)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDto ..."));
        if (arSuperDataSubmission == null) {
            log.warn(StringUtil.changeForLog("---No data to be saved---"));
            return arSuperDataSubmission;
        }
        List<ArSuperDataSubmissionDto> dtos = saveArSuperDataSubmissionDtoList(
                Collections.singletonList(arSuperDataSubmission));
        if (dtos == null || dtos.isEmpty()) {
            return null;
        }
        return dtos.get(0);
    }

    @Override
    public ArSuperDataSubmissionDto saveArSuperDataSubmissionDtoToBE(ArSuperDataSubmissionDto arSuperDataSubmission) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDtoToBE ..."));
        if (arSuperDataSubmission == null) {
            log.warn(StringUtil.changeForLog("---No data to be saved---"));
            return arSuperDataSubmission;
        }
        List<ArSuperDataSubmissionDto> dtos = saveArSuperDataSubmissionDtoListToBE(
                Collections.singletonList(arSuperDataSubmission));
        if (dtos == null || dtos.isEmpty()) {
            return null;
        }
        return dtos.get(0);
    }

    @Override
    public List<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDtoList(List<ArSuperDataSubmissionDto> arSuperList) {
        log.info(StringUtil.changeForLog("do the saveArSuperDataSubmissionDtos ..."));
        if (IaisCommonUtils.isEmpty(arSuperList)) {
            log.warn(StringUtil.changeForLog("---No data to be saved---"));
            return arSuperList;
        }
        return arFeClient.saveArSuperDataSubmissionDtoList(arSuperList).getEntity();
    }

    @Override
    public List<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDtoListToBE(List<ArSuperDataSubmissionDto> arSuperList) {
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE start ..."));
        if (IaisCommonUtils.isEmpty(arSuperList)) {
            log.warn(StringUtil.changeForLog("---No data to be saved---"));
            return arSuperList;
        }
        arSuperList.forEach(dto -> dto.setFe(false));
        arSuperList = saveBeArSuperDataSubmissionDtoList(arSuperList);

        DataSubmissionDto dataSubmission = arSuperList.get(0).getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
        } else {
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:" + refNo));
        }
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE end ..."));
        return arSuperList;
    }

    private List<ArSuperDataSubmissionDto> saveBeArSuperDataSubmissionDtoList(List<ArSuperDataSubmissionDto> arSuperList) {
        return feEicGatewayClient.saveBeArSuperDataSubmissionDtoList(arSuperList).getEntity();
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
    public String getSubmissionNo(CycleStageSelectionDto selectionDto, String dsType) {
        String submissionNo = null;
        boolean islinkableCycle = false;
        boolean isNonCycle = false;
        if (selectionDto != null) {
            islinkableCycle = StringUtil.isIn(selectionDto.getCycle(), new String[]{
                    DataSubmissionConsts.DS_CYCLE_AR,
                    DataSubmissionConsts.DS_CYCLE_IUI,
                    DataSubmissionConsts.DS_CYCLE_EFO});
            isNonCycle = DataSubmissionConsts.DS_CYCLE_NON.equals(selectionDto.getCycle());
            if (islinkableCycle && selectionDto.getLastCycleDto() != null
                    && !statuses.contains(selectionDto.getLastCycleDto().getStatus())
                    && selectionDto.getLastDataSubmission() != null) {
                submissionNo = selectionDto.getLastDataSubmission().getSubmissionNo();
            } else if (isNonCycle && selectionDto.getLastCycleDto() != null
                    && DataSubmissionConsts.DS_CYCLE_NON.equals(selectionDto.getLatestCycleDto().getCycleType())
                    && selectionDto.getLatestDataSubmission() != null) {
                submissionNo = selectionDto.getLatestDataSubmission().getSubmissionNo();
            }
        }
        if (StringUtil.isEmpty(submissionNo)) {
            submissionNo = systemAdminClient.submissionID(dsType).getEntity();
        }
        if (islinkableCycle || isNonCycle) {
            submissionNo = IaisCommonUtils.getNextSubmissionNo(submissionNo);
        }
        log.info(StringUtil.changeForLog("The submissionNo: " + submissionNo));
        return submissionNo;
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
    public ArSubFreezingStageDto setFreeCryoNumAndDate(ArSubFreezingStageDto arSubFreezingStageDto, String cryopreservedNum,
            String cryopreservationDate) {
        if (!StringUtil.isEmpty(cryopreservedNum)) {
            try {
                arSubFreezingStageDto.setCryopreservedNum(cryopreservedNum);
            } catch (Exception e) {
                arSubFreezingStageDto.setCryopreservedNum(null);
                log.info("Freezing invalid cryopreservedNum");
            }
        }else {
            arSubFreezingStageDto.setCryopreservedNum(null);
        }
        if (!StringUtil.isEmpty(cryopreservationDate)) {
            try {
                Date date = Formatter.parseDate(cryopreservationDate);
                arSubFreezingStageDto.setCryopreservedDate(date);
            } catch (Exception e) {
                arSubFreezingStageDto.setCryopreservedDate(null);
                log.info("Freezing invalid cryopreservationDate");
            }
        }else {
            arSubFreezingStageDto.setCryopreservedDate(null);
        }
        return arSubFreezingStageDto;
    }

    @Override
    public ArSubFreezingStageDto checkValueIsDirtyData(String freeCryoRadio, ArSubFreezingStageDto arSubFreezingStageDto,
            List<SelectOption> freeCryoOptions) {
        if (!IaisCommonUtils.isEmpty(freeCryoOptions)) {
            boolean codeValueFlag = false;
            for (SelectOption selectOption : freeCryoOptions) {
                if (selectOption != null) {
                    String codeValue = selectOption.getValue();
                    if (!StringUtil.isEmpty(codeValue) && codeValue.equals(freeCryoRadio)) {
                        codeValueFlag = true;
                        break;
                    }
                }
            }
            if (codeValueFlag) {
                arSubFreezingStageDto.setCryopreservedType(freeCryoRadio);
            } else {
                arSubFreezingStageDto.setCryopreservedType(null);
            }
        }
        return arSubFreezingStageDto;
    }

    @Override
    public List<CycleDto> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(String patientCode, String hciCode, String cycleType,
            String... status) {
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
    public PatientInventoryDto setFreezingPatientChange(PatientInventoryDto patientInventoryDto,
            ArSubFreezingStageDto arSubFreezingStageDto) {
        if (patientInventoryDto != null && arSubFreezingStageDto != null) {
            String cryopreservedType = arSubFreezingStageDto.getCryopreservedType();
            int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getCryopreservedNum());
            if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_OOCYTE.equals(cryopreservedType)) {
                patientInventoryDto.setChangeFreshOocytes(-1 * cryopreservedNum);
                patientInventoryDto.setChangeFrozenOocytes(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_EMBRYO.equals(cryopreservedType)) {
                patientInventoryDto.setChangeFreshEmbryos(-1 * cryopreservedNum);
                patientInventoryDto.setChangeFrozenEmbryos(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_OOCYTE.equals(cryopreservedType)) {
                patientInventoryDto.setChangeThawedOocytes(-1 * cryopreservedNum);
                patientInventoryDto.setChangeFrozenOocytes(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_EMBRYO.equals(cryopreservedType)) {
                patientInventoryDto.setChangeThawedEmbryos(-1 * cryopreservedNum);
                patientInventoryDto.setChangeFrozenEmbryos(cryopreservedNum);
            }
        }
        return patientInventoryDto;
    }

    @Override
    public DonorSampleDto getDonorSampleDto(String idType, String idNumber,String sampleType,String donorSampleCode, String sampleFromHciCode,
            String sampleFromOthers) {
        return ((StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)) &&
                (StringUtil.isEmpty(sampleType) || StringUtil.isEmpty(donorSampleCode) || StringUtil.isEmpty(sampleFromHciCode))) ? null : arFeClient.getDonorSampleDto(
                idType, idNumber,sampleType, donorSampleCode, sampleFromHciCode, sampleFromOthers).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto setIuiCycleStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission) {
        if (arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if (iuiCycleStageDto == null) {
                iuiCycleStageDto = new IuiCycleStageDto();
                iuiCycleStageDto.setOwnPremises(true);
                //set patient age show
                PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
                if (patientInfoDto != null) {
                    PatientDto patientDto = patientInfoDto.getPatient();
                    if (patientDto != null) {
                        List<Integer> integers = Formatter.getYearsAndDays(patientDto.getBirthDate());
                        if (IaisCommonUtils.isNotEmpty(integers)) {
                            int year = integers.get(0);
                            int month = integers.get(integers.size() - 1);
                            iuiCycleStageDto.setUserAgeShow(IaisCommonUtils.getYearsAndMonths(year, month));
                        }
                    }
                }
              iuiCycleStageDto.setDonorDtos(IaisCommonUtils.genNewArrayList());
            }
            arSuperDataSubmission.setIuiCycleStageDto(iuiCycleStageDto);
        }
        return arSuperDataSubmission;
    }


    @Override
    public List<String> checkBoxIsDirtyData(String[] stringArr, List<SelectOption> selectOptionList) {
        if (!IaisCommonUtils.isEmpty(selectOptionList)) {
            if (stringArr != null && stringArr.length > 0) {
                List<String> stringList = IaisCommonUtils.genNewArrayList();
                List<String> stringArrList = Arrays.asList(stringArr);
                for (SelectOption selectOption : selectOptionList) {
                    String value = selectOption.getValue();
                    if (!StringUtil.isEmpty(value) && stringArrList.contains(value)) {
                        stringList.add(value);
                    }
                }
                return stringList;
            }
        }
        return null;
    }

    @Override
    public ArSuperDataSubmissionDto setFreeStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission) {
        if (arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if(arSubFreezingStageDto == null) {
                arSubFreezingStageDto = new ArSubFreezingStageDto();
                arSubFreezingStageDto.setCryopreservedType(DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_OOCYTE);
                arSuperDataSubmission.setArSubFreezingStageDto(arSubFreezingStageDto);
            }
        }
        return arSuperDataSubmission;
    }

    @Override
    public List<DonorSampleAgeDto> getDonorSampleAgeDtoBySampleKey(String sampleKey) {
        return arFeClient.getDonorSampleAgeDtoBySampleKey(sampleKey).getEntity();
    }

    @Override
    public List<DonorSampleDto> getDonorSampleDtoBySampleKey(String sampleKey) {
        return arFeClient.getDonorSampleDtoBySampleKey(sampleKey).getEntity();
    }

    @Override
    public List<String> saveFileRepo(List<File> files) {
        if (IaisCommonUtils.isEmpty(files)) {
            log.info(StringUtil.changeForLog("------ No file to be saved to file report server -----"));
            return IaisCommonUtils.genNewArrayList(0);
        }
        return comFileRepoClient.saveFileRepo(files);
    }

    @Override
    public PatientInventoryDto getPatientInventory(String patientCode, String HciCode) {
        if (StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(HciCode)){
            log.info(StringUtil.changeForLog("------ No patient code or hci code -----"));
            return new PatientInventoryDto();
        }
        return arFeClient.patientInventoryByCode(patientCode, HciCode).getEntity();
    }

    @Override
    public List<DonorDto> getAllDonorDtoByCycleId(String cycleId) {
        if (StringUtil.isEmpty(cycleId)){
            log.info(StringUtil.changeForLog("------ No cycle Id -----"));
            return IaisCommonUtils.genNewArrayList(0);
        }
        return arFeClient.getAllDonorDtoByCycleId(cycleId).getEntity();
    }

    @Override
    public Date getCycleStartDate(String cycleId) {
        if (StringUtil.isEmpty(cycleId)) {
            log.info(StringUtil.changeForLog("------ No cycle Id -----"));
            return null;
        }
        return arFeClient.getCycleStartDate(cycleId).getEntity();
    }

    @Override
    public boolean flagOutEnhancedCounselling(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        String patientCode = cycleDto.getPatientCode();
        if (haveEnhancedCounsellingIncludeCurrentStage(arSuperDataSubmissionDto)) {
            int age = getPatientAge(arSuperDataSubmissionDto);
            if (age > 45) {
                return true;
            }
            return arFeClient.treatmentCycleCount(patientCode).getEntity() > 10;
        }
        return false;
    }

    @Override
    public boolean flagOutEmbryoTransferAgeAndCount(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        int embryoTransferCount = 0;
//        CycleDto cycleDto=arSuperDataSubmissionDto.getCycleDto();
//        if (cycleDto != null){
//            embryoTransferCount = embryoTransferCount(cycleDto.getId());
//        }
        PatientInventoryDto patientInventoryDto = arSuperDataSubmissionDto.getPatientInventoryDto();
        if (patientInventoryDto != null) {
            embryoTransferCount += patientInventoryDto.getCurrentFreshEmbryos();
            embryoTransferCount += patientInventoryDto.getCurrentThawedEmbryos();
            embryoTransferCount += patientInventoryDto.getCurrentFrozenEmbryos();
        }
        return haveEmbryoTransferGreaterFiveDayIncludeCurrentStage(arSuperDataSubmissionDto)
                && embryoTransferCount >= 3;
    }

    @Override
    public boolean flagOutEmbryoTransferCountAndPatAge(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        if (embryoTransferCountIncludeCurrentStage(arSuperDataSubmissionDto) >= 3) {
            int age = getPatientAge(arSuperDataSubmissionDto);
            if (age < 37) {
                return true;
            }
            return !haveStimulationCyclesIncludeCurrentStage(arSuperDataSubmissionDto);
        }
        return false;
    }

    private boolean haveEnhancedCounsellingIncludeCurrentStage(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        if (arSuperDataSubmissionDto.getArCycleStageDto() != null) {
            ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
            return arCycleStageDto.getEnhancedCounselling() != null && arCycleStageDto.getEnhancedCounselling();
        }
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        String patientCode = cycleDto.getPatientCode();
        return arFeClient.haveEnhancedCounselling(patientCode, cycleDto.getHciCode()).getEntity();
    }

    private boolean haveStimulationCyclesIncludeCurrentStage(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        if (arSuperDataSubmissionDto == null) {
            return false;
        }
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        if (arCycleStageDto != null) {
            List<String> currentArTreatmentValues = arCycleStageDto.getCurrentArTreatmentValues();
            if (currentArTreatmentValues != null && currentArTreatmentValues.contains("AR_CAT_002")) {
                return true;
            }
        }
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        if (cycleDto != null) {
            String patientCode = cycleDto.getPatientCode();
            return haveStimulationCycles(patientCode);
        }
        return false;
    }

    @Override
    public boolean haveStimulationCycles(String patientCode) {
        if (StringUtil.isNotEmpty(patientCode)) {
            return arFeClient.haveStimulationCycles(patientCode).getEntity();
        }
        return false;
    }

    private boolean haveEmbryoTransferGreaterFiveDayIncludeCurrentStage(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        String cycleDtoId = arSuperDataSubmissionDto.getCycleDto().getId();
        if (arSuperDataSubmissionDto.getEmbryoTransferStageDto() != null) {
            EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
            if (embryoHasGreaterFourDay(embryoTransferStageDto)) {
                return true;
            }
        }
        return haveEmbryoTransferGreaterFiveDay(cycleDtoId);
    }

    @Override
    public boolean haveEmbryoTransferGreaterFiveDay(String cycleId) {
        if (StringUtil.isNotEmpty(cycleId)) {
            return arFeClient.haveEmbryoTransferGreaterFiveDay(cycleId).getEntity();
        }
        return false;
    }

    private int getPatientAge(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        List<Integer> integers = Formatter.getYearsAndDays(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate());
        int age = 0;
        if (IaisCommonUtils.isNotEmpty(integers)) {
            age = integers.get(0);
        }
        return age;
    }

    private int embryoTransferCountIncludeCurrentStage(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        int result = 0;
        String cycleDtoId = arSuperDataSubmissionDto.getCycleDto().getId();
        if (arSuperDataSubmissionDto.getEmbryoTransferStageDto() != null) {
            EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
            if (embryoTransferStageDto != null) {
                result += embryoTransferStageDto.getTransferNum();
            }
        }
        result += embryoTransferCount((cycleDtoId));
        return result;
    }

    @Override
    public int embryoTransferCount(String cycleDtoId) {
        int result = 0;
        if (StringUtil.isNotEmpty(cycleDtoId)) {
            Integer cycleEmbryoTransferCount = arFeClient.embryoTransferCount(cycleDtoId).getEntity();
            if (cycleEmbryoTransferCount != null) {
                result += cycleEmbryoTransferCount;
            }
        }
        return result;
    }

    private boolean embryoHasGreaterFourDay(EmbryoTransferStageDto embryoTransferStageDto) {
        if (embryoTransferStageDto == null) {
            return false;
        }
        return greaterFourDay(embryoTransferStageDto.getFirstEmbryoAge()) || greaterFourDay(embryoTransferStageDto.getSecondEmbryoAge())
                || greaterFourDay(embryoTransferStageDto.getThirdEmbryoAge());
    }

    private boolean greaterFourDay(String code) {
        return "AOFET005".equals(code) || "AOFET006".equals(code);
    }
}
