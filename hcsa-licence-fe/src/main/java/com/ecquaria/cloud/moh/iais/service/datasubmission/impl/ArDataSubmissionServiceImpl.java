package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ARCycleStageDto;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.ACTION_TYPE;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.JUMP_ACTION_TYPE;

/**
 * @Description ArDataSubmissionServiceImpl
 * @Auther chenlei on 10/22/2021.
 */
@Service
@Slf4j
public class ArDataSubmissionServiceImpl implements ArDataSubmissionService {

    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;

    @Autowired
    NotificationHelper notificationHelper;

    @Autowired
    CessationFeService cessationFeService;

    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    FeMessageClient feMessageClient;

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

    @Autowired
    private DsLicenceService dsLicenceService;
    @Autowired
    private LicenseeService licenseeService;

    @Override
    public Map<String, PremisesDto> getArCenterPremises(String licenseeId) {
        return dsLicenceService.getArCenterPremises(licenseeId);
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
    public CycleStageSelectionDto getCycleStageSelectionDtoByConds(String patientCode, String hciCode, String cycleId) {
        log.info(StringUtil.changeForLog("CycleStageSelectionDto - " + patientCode + " : " + hciCode + " : " + cycleId));
        if (StringUtil.isEmpty(patientCode)) {
            return null;
        }
        return arFeClient.getCycleStageSelectionDtoByConds(patientCode, hciCode, cycleId).getEntity();
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
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoBySubmissionId(String submissionId) {
        log.info(StringUtil.changeForLog("----- Submission Id: " + submissionId + " -----"));
        if (StringUtil.isEmpty(submissionId)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoByDsId(submissionId).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCode, String cycleId) {
        if (StringUtil.isEmpty(patientCode)) {
            log.warn("----- No Patient Code -----");
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDto(patientCode, hciCode, cycleId).getEntity();
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
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoByDraftNo(String draftNo) {
        log.info(StringUtil.changeForLog("----- Draft No: " + draftNo + " -----"));
        if (StringUtil.isEmpty(draftNo)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoByDraftNo(draftNo).getEntity();
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
        arSuperList.forEach(dto -> dto.setFe(true));
        return arFeClient.saveArSuperDataSubmissionDtoList(arSuperList).getEntity();
    }

    @Override
    public List<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDtoListToBE(List<ArSuperDataSubmissionDto> arSuperList) {
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE start ..."));
        if (IaisCommonUtils.isEmpty(arSuperList)) {
            log.warn(StringUtil.changeForLog("---No data to be saved---"));
            return arSuperList;
        }
        DataSubmissionDto dataSubmission = arSuperList.get(0).getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        arSuperList.forEach(dto -> dto.setFe(false));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
        try {
            arSuperList = saveBeArSuperDataSubmissionDtoList(arSuperList);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
        } catch (Throwable e) {
            licEicClient.saveEicTrack(eicRequestTrackingDto);
        }
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE refNo is -->:" + refNo));
        log.info(StringUtil.changeForLog(" the saveArSuperDataSubmissionDtoListToBE end ..."));
        return arSuperList;
    }

    @Override
    public void saveBeArSuperDataSubmissionDtoForEic(EicArSuperDataSubmissionDto eicArSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog(" the saveBeArSuperDataSubmissionDtoForEic start ..."));
        if(eicArSuperDataSubmissionDto != null ){
           List<ArSuperDataSubmissionDto> arSuperList =  eicArSuperDataSubmissionDto.getArSuperDataSubmissionDtos();
           if(IaisCommonUtils.isNotEmpty(arSuperList)){
               saveArSuperDataSubmissionDtoListToBE(arSuperList);
           }else{
               log.error(StringUtil.changeForLog(" the saveBeArSuperDataSubmissionDtoForEic arSuperList is null"));
           }
        }else{
            log.error(StringUtil.changeForLog(" the saveBeArSuperDataSubmissionDtoForEic eicArSuperDataSubmissionDto is null"));
        }
        log.info(StringUtil.changeForLog(" the saveBeArSuperDataSubmissionDtoForEic end ..."));
    }

    private List<ArSuperDataSubmissionDto> saveBeArSuperDataSubmissionDtoList(List<ArSuperDataSubmissionDto> arSuperList) {
        return feEicGatewayClient.saveBeArSuperDataSubmissionDtoList(arSuperList).getEntity();
    }

    @Override
    public List<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
                                                                                  String orgId, String hciCode, boolean onlyStage, String userId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + hciCode + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)
                || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(hciCode)) {
            return null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftByConds(idType, idNumber, nationality, orgId, hciCode, onlyStage,userId).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode, String userId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + submissionType + " : " + hciCode + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return null;
        }
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            hciCode = null;
        }
        return arFeClient.getArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode,userId).getEntity();
    }

    @Override
    public void deleteArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
            String orgId, String hciCode) {
        log.info(StringUtil.changeForLog("----- Delete Param: " + orgId + " : " + hciCode + " : " + idType + " : " + idNumber + " : "
                + nationality + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber)
                || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(hciCode)) {
            return;
        }
        arFeClient.deleteArSuperDataSubmissionDtoDraftByConds(idType, idNumber, nationality, orgId, hciCode);
    }

    @Override
    public void deleteArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        log.info(StringUtil.changeForLog("----- Delete Param: " + orgId + " : " + submissionType + " -----"));
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
            islinkableCycle = DsHelper.isNormalCycle(selectionDto.getCycle());
            isNonCycle = DataSubmissionConsts.DS_CYCLE_NON.equals(selectionDto.getCycle());
            if (islinkableCycle && selectionDto.getLastCycleDto() != null
                    && !DsHelper.isCycleFinalStatus(selectionDto.getLastCycleDto().getStatus())
                    && !DsHelper.isStartStage(selectionDto.getStage())
                    && selectionDto.getLastDataSubmission() != null) {
                submissionNo = selectionDto.getLastDataSubmission().getSubmissionNo();
            } else if (isNonCycle && selectionDto.getLastCycleDto() != null
                    && DataSubmissionConsts.DS_CYCLE_NON.equals(selectionDto.getLatestCycleDto().getCycleType())
                    && selectionDto.getLatestDataSubmission() != null) {
                // 79177
                // submissionNo = selectionDto.getLatestDataSubmission().getSubmissionNo();
            }
        }
        synchronized (this) {
            if (StringUtil.isEmpty(submissionNo)) {
                submissionNo = systemAdminClient.submissionID(dsType).getEntity();
            }
            if (islinkableCycle || isNonCycle) {
                submissionNo = IaisCommonUtils.getNextSubmissionNo(submissionNo);
            }
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
    public List<CycleDto> getCyclesByConds(String patientCode, String hciCode, String svcName) {
        log.info(StringUtil.changeForLog("patientCode: " + patientCode + " - hciCode: " + hciCode + " - Svc Name: " + svcName));
        if (StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(hciCode)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getCyclesByConds(patientCode, hciCode, svcName).getEntity();
    }

    @Override
    public List<CycleDto> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(String patientCode, String hciCode, String cycleType,
            String... status) {
        CycleDto cycleDto = new CycleDto();
        cycleDto.setPatientCode(patientCode);
        cycleDto.setHciCode(hciCode);
        cycleDto.setCycleType(cycleType);
        cycleDto.setStatuses(IaisCommonUtils.isEmpty(status) ? DsHelper.getDsCycleFinalStatus() : Arrays.asList(status));
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
    public ArSuperDataSubmissionDto setIuiCycleStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission) {
        if (arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if (iuiCycleStageDto == null) {
                iuiCycleStageDto = new IuiCycleStageDto();
                iuiCycleStageDto.setOwnPremises(true);
                iuiCycleStageDto.setDonorDtos(IaisCommonUtils.genNewArrayList());
            }
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
    public List<DonorSampleAgeDto> getDonorSampleAgeDtoBySampleKey(String sampleKey) {
        if (StringUtil.isEmpty(sampleKey)){
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getDonorSampleAgeDtoBySampleKey(sampleKey).getEntity();
    }

    @Override
    public List<DonorSampleDto> getDonorSampleDtoBySampleKey(String sampleKey) {
        if (StringUtil.isEmpty(sampleKey)){
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getDonorSampleDtoBySampleKey(sampleKey).getEntity();
    }

    @Override
    public List<DonorSampleAgeDto> getMaleDonorSampleDtoByIdTypeAndIdNo(String idType, String idNo) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getMaleDonorSampleDtoByIdTypeAndIdNo(idType, idNo).getEntity();
    }

    @Override
    public List<DonorSampleAgeDto> getFemaleDonorSampleDtoByIdTypeAndIdNo(String idType, String idNo) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getFemaleDonorSampleDtoByIdTypeAndIdNo(idType, idNo).getEntity();
    }

    @Override
    public List<DonorSampleAgeDto> getDonorSampleAgeDtos(String idType, String idNo) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return arFeClient.getDonorSampleAges(idType, idNo).getEntity();
    }

    @Override
    public String getDonorSampleKey(String idType, String idNo) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo)) {
            return null;
        }
        return arFeClient.getDonorSampleKey(idType, idNo).getEntity();
    }

    @Override
    public List<String> getDonorSampleTypeKey(String idType, String idNo, String donorSampleType) {
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo) || StringUtil.isEmpty(donorSampleType)) {
            return null;
        }
        return arFeClient.getDonorSampleTypeKey(idType, idNo, donorSampleType).getEntity();
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
    public List<DonorDto> getAllDonorDtoByCycleId(String cycleId) {
        if (StringUtil.isEmpty(cycleId)) {
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
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();
        if (arCurrentInventoryDto != null) {
            embryoTransferCount += arCurrentInventoryDto.getFreshEmbryoNum();
            embryoTransferCount += arCurrentInventoryDto.getThawedEmbryoNum();
            embryoTransferCount += arCurrentInventoryDto.getFrozenEmbryoNum();
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
        int transferNum = embryoTransferStageDto.getTransferNum();
        boolean hasGreaterFourDay = false;
        if (embryoTransferStageDto.getEmbryoTransferDetailDtos() == null) {
            List<EmbryoTransferDetailDto> embryoTransferDetailDtos1 = arFeClient.getEmbryoTransferDetail(embryoTransferStageDto.getId()).getEntity();
            embryoTransferStageDto.setEmbryoTransferDetailDtos(embryoTransferDetailDtos1);
        }
        for (int i = 0; i < transferNum; i++) {
            hasGreaterFourDay = hasGreaterFourDay || greaterFourDay(embryoTransferStageDto.getEmbryoTransferDetailDtos().get(i).getEmbryoAge());
        }
        return hasGreaterFourDay;

    }

    private boolean greaterFourDay(String code) {
        return "AOFET005".equals(code) || "AOFET006".equals(code);
    }

    @Override
    public String getTransferConfirmationDsNoByBaseDsId(String patientCode, String hciCode, String svcName, String submissionId) {
        return arFeClient.getTransferConfirmationDsNo(patientCode, hciCode, svcName, submissionId).getEntity();
    }

    private List<String> getLicenseeList(List<CycleDto> overDayCycleDtos) {
        List<String> overDayLicenseeId = IaisCommonUtils.genNewArrayList();
        for (CycleDto overDayCycleDto : overDayCycleDtos) {
            String licenseeId = getLicenseeId(overDayCycleDto);
            overDayLicenseeId.add(licenseeId);
        }
        return overDayLicenseeId;
    }

    public String getLicenseeId(CycleDto cycleDto) {
        String result = null;
        if (cycleDto != null) {
            String hciCode = cycleDto.getHciCode();
            PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(hciCode);
            if (premisesDto != null) {
                String organizationId = premisesDto.getOrganizationId();
                LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(organizationId);
                if (licenseeDto != null) {
                    result = licenseeDto.getId();
                }
            }
        }
        return result;
    }

    @Override
    public int getArCycleStageCountByIdTypeAndIdNoAndNationality(PatientDto patientDto) {
        return StringUtil.allStringIsNull(patientDto.getIdType(), patientDto.getIdNumber(), patientDto.getNationality()) ?
                0 : arFeClient.getArCycleStageCountByIdTypeAndIdNoAndNationality(patientDto.getIdType(), patientDto.getIdNumber(),
                patientDto.getNationality()).getEntity();
    }

    @Override
    public ArCurrentInventoryDto getArCurrentInventoryDtoByConds(String hciCode, String licenseeId, String patientCode, String svcName) {
        return arFeClient.getArCurrentInventoryDtoByConds(hciCode, licenseeId, patientCode, svcName).getEntity();
    }

    @Override
    public ArCurrentInventoryDto getArCurrentInventoryDtoBySubmissionNo(String submissionNo, boolean hasAfter) {
        return arFeClient.getArCurrentInventoryDtoBySubmissionNo(submissionNo, hasAfter).getEntity();
    }

    @Override
    public void remindAndDeleteDraftSubJob() {
        int overDueDays = Integer.parseInt(MasterCodeUtil.getCodeDesc("DSPC_002"));
        sendRemindEmailForDraftOverDueDayNear(overDueDays,Integer.parseInt(MasterCodeUtil.getCodeDesc("DSPC_003")));
        arFeClient.doUpdateDraftStatusMoreThanDays(DataSubmissionConsts.DS_STATUS_INACTIVE,DataSubmissionConsts.DS_STATUS_DRAFT, overDueDays);
    }

    private void sendRemindEmailForDraftOverDueDayNear(int overDueDays,int distanceExpirationDays){
        List<DataSubmissionDraftDto> dataSubmissionDraftDtos = arFeClient.getRemindDraftsByRemindDays(DataSubmissionConsts.DS_STATUS_DRAFT,overDueDays+1-distanceExpirationDays).getEntity();
        if(IaisCommonUtils.isNotEmpty(dataSubmissionDraftDtos)){
            List<String> draftNos = IaisCommonUtils.genNewArrayList(dataSubmissionDraftDtos.size());
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date());
            calendarStart.add(Calendar.DATE,distanceExpirationDays);
            String expDateString = Formatter.formatDate(calendarStart.getTime());
            MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DRAFT_REMIND_MSG).getEntity();
            dataSubmissionDraftDtos.forEach( dataSubmissionDraftDto -> {
                 if( StringUtil.isNotEmpty( dataSubmissionDraftDto.getLicenseeId())){
                     Map<String,Object> map = MasterCodeUtil.listKeyAndValueMap(Arrays.asList("ApplicantName","draftNumber","date","MOH_AGENCY_NAME"),Arrays.asList(licenseeService.getLicenseeDtoById(dataSubmissionDraftDto.getLicenseeId()).getName(),dataSubmissionDraftDto.getDraftNo(),expDateString,AppConsts.MOH_AGENCY_NAME));
                     String serviceType = dsType2ServiceType(dataSubmissionDraftDto.getDsType());
                     String emailTemplateId = dsType2EmailTempId(dataSubmissionDraftDto.getDsType());
                     try {
                         notificationHelper.sendNotification(new EmailParam(MsgTemplateConstants.MSG_TEMPLATE_DRAFT_REMIND_MSG,map,dataSubmissionDraftDto.getDraftNo(),dataSubmissionDraftDto.getDraftNo(),
                                 NotificationHelper.MESSAGE_TYPE_NOTIFICATION,dataSubmissionDraftDto.getLicenseeId(),MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map),serviceType));
                         log.info(StringUtil.changeForLog("---------------------sub draft no :"+ dataSubmissionDraftDto.getDraftNo() +"  send msg end ----------"));
                         notificationHelper.sendNotification(new EmailParam(emailTemplateId,map,dataSubmissionDraftDto.getDraftNo(),dataSubmissionDraftDto.getDraftNo(),
                                 NotificationHelper.RECEIPT_TYPE_LICENSEE_ID ,dataSubmissionDraftDto.getLicenseeId(),MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map),serviceType));
                         draftNos.add(dataSubmissionDraftDto.getDraftNo());
                         log.info(StringUtil.changeForLog("---------------------sub draft no :"+ dataSubmissionDraftDto.getDraftNo() +"  send email end ----------"));
                     } catch (IOException | TemplateException e) {
                         log.error(e.getMessage(),e);
                     }
                 }else {
                     log.info(StringUtil.changeForLog("---------------------sub draft no :"+ dataSubmissionDraftDto.getDraftNo() +"  no licenseeId ----------"));
                 }
            });

            arFeClient.doUpdateDraftRemindEmailStatus(draftNos);
        }
    }

    private String dsType2ServiceType(String dsType){
        switch (dsType){
            case DataSubmissionConsts.DS_AR:
                return DataSubmissionConsts.DS_AR_NEW;
            case DataSubmissionConsts.DS_LDT:
                return  DataSubmissionConsts.DS_LDT_NEW;
            case DataSubmissionConsts.DS_TOP:
                return DataSubmissionConsts.DS_TOP_NEW;
            case DataSubmissionConsts.DS_DRP:
                return  DataSubmissionConsts.DS_DRP_NEW;
            default:
                return dsType;
        }
    }

    private String dsType2EmailTempId(String dsType){
        switch (dsType){
            case DataSubmissionConsts.DS_AR:
                return MsgTemplateConstants.MSG_TEMPLATE_DS_DRAFT_REMIND_EMAIL_AR;
            case DataSubmissionConsts.DS_LDT:
                return  MsgTemplateConstants.MSG_TEMPLATE_DS_DRAFT_REMIND_EMAIL_LDT;
            case DataSubmissionConsts.DS_TOP:
                return MsgTemplateConstants.MSG_TEMPLATE_DS_DRAFT_REMIND_EMAIL_TOP;
            case DataSubmissionConsts.DS_DRP:
                return  MsgTemplateConstants.MSG_TEMPLATE_DS_DRAFT_REMIND_EMAIL_DP;
            default:
                return MsgTemplateConstants.MSG_TEMPLATE_DS_DRAFT_REMIND_EMAIL_VSS;
        }
    }

    private Map<String, String> getCycleStageSubmitByMap(String cycleId) {
        if (StringUtils.hasLength(cycleId)) {
            List<DataSubmissionDto> dataSubmissionDtoList = arFeClient.getAllDataSubmissionByCycleId(cycleId).getEntity();
            if (!CollectionUtils.isEmpty(dataSubmissionDtoList)) {
                Map<String, String> cycleStageMap=IaisCommonUtils.genNewHashMap();
                for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
                     ) {
                    cycleStageMap.put(dataSubmissionDto.getCycleStage(),dataSubmissionDto.getSubmitBy());

                }
                return cycleStageMap;
            }
        }
        return Collections.emptyMap();
    }

    /**
     * Get all the data displayed in the AR Cycle navigation bar
     */
    @Override
    public List<ARCycleStageDto> genAvailableStageList(HttpServletRequest request, boolean missOnGoing) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleStageSelectionDto selectionDto = currentArDataSubmission.getSelectionDto();
        // get from cycleStageSelectionSection.jsp, user select next stage
        String stage = ParamUtil.getString(request, "stage");
        //get from headStepNavTab.jsp , user click to change stage
        String actionValue = ParamUtil.getString(request, "action_value");
        String currentStage;
        List<String> submittedStageList = new ArrayList<>();
        List<String> notSubmittedStageList = new ArrayList<>();
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        //get all can action stages
        List<String> nextStageList = DataSubmissionHelper.getNextStageForAR(selectionDto);
        if (selectionDto != null) {
            currentStage = StringUtils.hasLength(actionValue) ? actionValue : selectionDto.getStage();
            String cycleId = selectionDto.getCycleId();
            map = getCycleStageSubmitByMap(cycleId);
            submittedStageList = new ArrayList<>(map.keySet());
            //remove all submitted stage, result is not-submitted stage
            nextStageList.removeAll(submittedStageList);
            notSubmittedStageList = nextStageList;
        } else {
            currentStage = StringUtils.hasLength(actionValue) ? actionValue : stage;
        }
        List<ARCycleStageDto> arCycleStageDtos = new ArrayList<>();
        List<String> options = new ArrayList<>();
        if(selectionDto != null) {
            String cycle = selectionDto.getNavCurrentCycle();
            if (DataSubmissionConsts.DS_CYCLE_AR.equals(cycle)) {
                options = DataSubmissionHelper.getAllARCycleStages();
            } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(cycle)) {
                options = DataSubmissionHelper.getAllIUICycleStages();
            } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(cycle)) {
                options = DataSubmissionHelper.getAllOFOCycleStages();
            } else if (DataSubmissionConsts.DS_CYCLE_SFO.equals(cycle)) {
                options = DataSubmissionHelper.getAllSFOCycleStages();
            }
        }
        for (String option : options) {
            String codeDesc;
            // the fields displayed by WireFrame are not the same as the fields stored in the database, fix this
            if (DataSubmissionConsts.AR_STAGE_THAWING.equals(option)) {
                codeDesc = "Thawing";
            } else {
                codeDesc = MasterCodeUtil.getCodeDesc(option);
            }
            String permissions = determinePermissions(request, map.get(option));
            if (option.equals(currentStage)) {
                if (missOnGoing) {
                    arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, null, permissions));
                } else {
                    arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, DataSubmissionConstant.AR_CYCLE_STAGE_STATUS_ONGOING, permissions));
                }
            } else if (submittedStageList.contains(option)) {
                arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, DataSubmissionConstant.AR_CYCLE_STAGE_STATUS_SUBMITTED, permissions));
            } else if (notSubmittedStageList.contains(option)) {
                //if only can do rfc, notSubmittedStage -> invalidStage, can't click
                if (DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_RFC.equals(permissions)) {
                    arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, DataSubmissionConstant.AR_CYCLE_STAGE_STATUS_INVALID, null));
                } else {
                    arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, null, permissions));
                }
            } else {
                arCycleStageDtos.add(new ARCycleStageDto(option, codeDesc, DataSubmissionConstant.AR_CYCLE_STAGE_STATUS_INVALID, null));
            }

        }
        return arCycleStageDtos;
    }

    @Override
    public ArSuperDataSubmissionDto prepareArRfcData(ArSuperDataSubmissionDto arSuper, String submissionNo, HttpServletRequest request) {
        if (arSuper == null) {
            arSuper = getArSuperDataSubmissionDtoBySubmissionNo(submissionNo);
        }
        arSuper.setArCurrentInventoryDto(getArCurrentInventoryDtoBySubmissionNo(submissionNo, true));
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_OLD_DATA_SUBMISSION,
                CopyUtil.copyMutableObject(arSuper));
        arSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        arSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        if (arSuper.getDataSubmissionDto() != null) {
            DataSubmissionDto dataSubmissionDto = arSuper.getDataSubmissionDto();
            dataSubmissionDto.setDeclaration(null);
            dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            dataSubmissionDto.setAmendReason(null);
            dataSubmissionDto.setAmendReasonOther(null);
            if (arSuper.getSelectionDto() != null) {
                CycleStageSelectionDto selectionDto = arSuper.getSelectionDto();
                if (StringUtil.isEmpty(selectionDto.getStage()) || StringUtil.isEmpty(selectionDto.getCycle()) || StringUtil.isEmpty(selectionDto.getNavCurrentCycle())) {
                    selectionDto.setStage(dataSubmissionDto.getCycleStage());
                    selectionDto.setCycle(arSuper.getCycleDto().getCycleType());
                    selectionDto.setNavCurrentCycle(arSuper.getCycleDto().getCycleType());
                }
            }
        }
        return arSuper;
    }

    @Override
    public void jumpJudgement(HttpServletRequest request) {
        String actionType = ParamUtil.getString(request, ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, "action_value");
        //ArCycleStagesManualDelegator#doPrepareStage set value
        String haveJump = (String) ParamUtil.getRequestAttr(request, "haveJump");
        String jumpToSubmittedStage = ParamUtil.getString(request, DataSubmissionConstant.JUMP_TO_SUBMITTED_STAGE);
        String targetStageUserPermissions = ParamUtil.getString(request, DataSubmissionConstant.TARGET_STAGE_USER_PERMISSIONS);
        if ("jumpStage".equals(actionType)) {
            //set crud_action_type_ct, decide which process to enter
            ParamUtil.setRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, actionValue);
            if ("Y".equals(haveJump)) {
                // 2. second step, come in from step 1's parent process, judge what user can do
                if ("true".equals(jumpToSubmittedStage) && (DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_VIEW.equals(targetStageUserPermissions) || DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_NEW.equals(targetStageUserPermissions))) {
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");
                } else {
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                }
                prepareTargetStageRfcData(request, actionValue, jumpToSubmittedStage);
            } else {
                // 1. first step, return to parent process
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "return");
                ParamUtil.setRequestAttr(request, JUMP_ACTION_TYPE, "jump");
            }
        }
    }

    private void prepareTargetStageRfcData(HttpServletRequest request, String actionValue, String jumpToSubmittedStage) {
        String targetStageUserPermissions = ParamUtil.getString(request, DataSubmissionConstant.TARGET_STAGE_USER_PERMISSIONS);
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleStageSelectionDto selectionDto = currentArDataSubmission.getSelectionDto();
        DataSubmissionDto dataSubmissionDto1 = currentArDataSubmission.getDataSubmissionDto();
        String cycleId = "";
        if (selectionDto != null) {
            cycleId = selectionDto.getCycleId();
        }
        if (StringUtils.hasLength(cycleId) && "true".equals(jumpToSubmittedStage)) {
            List<DataSubmissionDto> dataSubmissionDtoList = arFeClient.getAllDataSubmissionByCycleId(cycleId).getEntity();
            if (!CollectionUtils.isEmpty(dataSubmissionDtoList)) {
                for (DataSubmissionDto dataSubmissionDto : dataSubmissionDtoList) {
                    if (dataSubmissionDto.getCycleStage().equals(actionValue)) {
                        currentArDataSubmission = prepareArRfcData(null, dataSubmissionDto.getSubmissionNo(), request);
                        break;
                    }
                }
            }
        }
        if (!"true".equals(jumpToSubmittedStage) || DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_VIEW.equals(targetStageUserPermissions)) {
            currentArDataSubmission.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        }
        if (selectionDto != null) {
            selectionDto.setStage(actionValue);
        }
        if (dataSubmissionDto1 != null) {
            dataSubmissionDto1.setCycleStage(actionValue);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
    }

    private String determinePermissions(HttpServletRequest request, String currentSubmissionUserId) {
        String permission = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext == null) {
            loginContext = new LoginContext();
        }
        ArrayList<String> roleIds = loginContext.getRoleIds();
        if (StringUtils.hasLength(currentSubmissionUserId) && !currentSubmissionUserId.equals(loginContext.getUserId())) {
            permission = DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_VIEW;
        } else if (roleIds.contains(RoleConsts.USER_ROLE_DS_AR) && roleIds.contains(RoleConsts.USER_ROLE_DS_AR_SUPERVISOR)) {
            permission = DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_NEW_AND_RFC;
        } else if (roleIds.contains(RoleConsts.USER_ROLE_DS_AR)) {
            permission = DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_NEW;
        } else if (roleIds.contains(RoleConsts.USER_ROLE_DS_AR_SUPERVISOR)) {
            permission = DataSubmissionConstant.AR_CYCLE_USER_PERMISSIONS_RFC;
        }
        return permission;
    }

    @Override
    public ArSuperDataSubmissionDto getDraftArSuperDataSubmissionDtoByConds(String orgId, String hciCode, String submissionStage, String userId) {
        return arFeClient.getDraftArSuperDataSubmissionDtoByConds(orgId, hciCode, submissionStage, userId).getEntity();
    }
}
