package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.NonPatinetDonorSampleExcelDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class NonPatientDonorSampleUploadServiceImpl {
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;
    @Autowired
    private ArBatchUploadCommonServiceImpl arBatchUploadCommonService;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    public Map<String, String> getErrorMap(HttpServletRequest request){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = arBatchUploadCommonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = arBatchUploadCommonService.getPageShowFileDto(fileEntry);
        ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
        errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
        List<DonorSampleDto> donorSampleDtos = null;
        if (errorMap.isEmpty()) {
            String fileName=fileEntry.getValue().getName();

            if(!fileName.equals("(For Non-Patient or Overseas Donor) Donor Sample File Upload v0.2.xlsx") && !fileName.equals("(For Non-Patient or Overseas Donor) Donor Sample File Upload v0.2.csv")){
                errorMap.put("uploadFileError", "Please change the file name.");
            }
            List<NonPatinetDonorSampleExcelDto> nonPatinetDonorSampleExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, NonPatinetDonorSampleExcelDto.class);
            donorSampleDtos = getDonorSampleList(nonPatinetDonorSampleExcelDtoList);
            fileItemSize = donorSampleDtos.size();
            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(NonPatinetDonorSampleExcelDto.class);
                List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(donorSampleDtos, "file", fieldCellMap);
                for (int i = 1; i <= fileItemSize; i++) {
                    DonorSampleDto dsDto = donorSampleDtos.get(i-1);
                    validDonorSample(errorMsgs, dsDto, fieldCellMap, i);
                }
                arBatchUploadCommonService.getErrorRowInfo(errorMap,request,errorMsgs);
            }
        }
        if (!errorMap.isEmpty()){

        } else {

            request.getSession().setAttribute(DataSubmissionConsts.NON_PATIENT_DONORSAMPLE_LIST, donorSampleDtos);
        }
        return errorMap;
    }
    private boolean getBoolen(String val){
        return "Yes".equals(val);
    }
    private String getKey(String value){
        if (value == null){
            return null;
        }
        return MasterCodeUtil.getCodeKeyByCodeValue(value).get(0);
    }
    private String subNumberStr(String value){
        if (value == null){
            return null;
        }
        int index = value.indexOf('.');
        return value.substring(0,index);
    }
    private String getSampleTypeMasterCodeKey(String value){
        if (value == null){
            return null;
        }
        switch (value){
            case "Fresh Oocyte":
                return "DONTY001";

            case "Frozen Oocyte":
                return "DONTY002";

            case "Frozen Embryo":
                return "DONTY003";

            case "Frozen Sperm":
                return "DONTY004";

            default:
                return "DONTY005";

        }
    }
    public List<DonorSampleDto> getDonorSampleList(List<NonPatinetDonorSampleExcelDto> nonPatinetDonorSampleExcelDtos) {
        if (nonPatinetDonorSampleExcelDtos == null) {
            return null;
        }
        List<DonorSampleDto> result = IaisCommonUtils.genNewArrayList(nonPatinetDonorSampleExcelDtos.size());
        for (NonPatinetDonorSampleExcelDto excelDto : nonPatinetDonorSampleExcelDtos) {
            DonorSampleDto dto = new DonorSampleDto();
            dto.setLocalOrOversea(excelDto.getLocalOrOverseas() == null ? null : "Local".equals(excelDto.getLocalOrOverseas()));
            dto.setSampleType(getSampleTypeMasterCodeKey(excelDto.getTypeOfSample()));
            dto.setDonorIdentityKnown(excelDto.getFemaleIdentityKnown() == null ? null : ("Yes".equals(excelDto.getFemaleIdentityKnown()) ? DataSubmissionConsts.DONOR_IDENTITY_KNOWN : DataSubmissionConsts.DONOR_IDENTITY_ANONYMOUS));
            dto.setIdType(getKey(excelDto.getFemaleIdType()));
            dto.setIdNumber(excelDto.getFemaleIdNo());
            dto.setDonorSampleCode(excelDto.getFemaleSampleCode());
            dto.setDonorSampleAge(subNumberStr(excelDto.getFemaleAge()));
            dto.setMaleDonorIdentityKnow(getBoolen(excelDto.getMaleIdentityKnown()));
            dto.setIdTypeMale(excelDto.getMaleIdType());
            dto.setIdNumberMale(excelDto.getMaleIdNo());
            dto.setMaleDonorSampleAge(subNumberStr(excelDto.getMaleAge()));
            dto.setSampleFromOthers(excelDto.getInstitutionFrom());
            dto.setDonationReason(excelDto.getReasonsForDonation());
            dto.setOtherDonationReason(excelDto.getOtherReasonsForDonation());
            dto.setDonatedForTreatment(getBoolen(excelDto.getPurposeOfDonation_treatment()));
            dto.setDonatedForResearch(getBoolen(excelDto.getPurposeOfDonation_research()));
            dto.setDonatedForTraining(getBoolen(excelDto.getPurposeOfDonation_training()));
            dto.setDirectedDonation(getBoolen(excelDto.getIsDirectedDonation()));
            dto.setTreatNum(subNumberStr(excelDto.getNoDonatedForTreatment()));
            dto.setTrainingNum(subNumberStr(excelDto.getNoUsedForTraining()));
            dto.setDonResForTreatNum(subNumberStr(excelDto.getNoDonatedForResearch_useTreatment()));
            dto.setDonResForCurCenNotTreatNum(subNumberStr(excelDto.getNoDonatedForResearch_unUseTreatment()));
            dto.setDonatedForResearchHescr(getBoolen(excelDto.getDonatedForHESCResearch()));
            dto.setDonatedForResearchRrar(getBoolen(excelDto.getDonatedForResearchRelatedToAR()));
            dto.setDonatedForResearchOtherType(excelDto.getOtherTypeOfResearch());
            result.add(dto);
        }
        return result;
    }

    public void validDonorSample(List<FileErrorMsg> errorMsgs,DonorSampleDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");

        if(StringUtil.isEmpty(dsDto.getLocalOrOversea())){//localOrOverseas is empty
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("localOrOverseas"), errMsgErr006));
        }else {
            if(dsDto.getLocalOrOversea()){//local

            }else {//oversea
                if(StringUtil.isNotEmpty(dsDto.getSampleFromOthers())){
                    if(dsDto.getSampleFromOthers().length()>256){
                        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                        repMap.put("number","256");
                        repMap.put("fieldNo","(12) Which Institution was the Sample Donated From?");
                        String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("institutionFrom"), errMsg));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("institutionFrom"), errMsgErr006));
                }
            }
        }

        if(StringUtil.isEmpty(dsDto.getSampleType())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("typeOfSample"), errMsgErr006));
        }else {
            validDonorIdentity(errorMsgs,dsDto,fieldCellMap,i);
        }

        if(StringUtil.isEmpty(dsDto.getDonationReason())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("reasonsForDonation"), errMsgErr006));
        }else {
            if(DataSubmissionConsts.DONATION_REASON_OTHERS.equals(dsDto.getDonationReason())){
                if(StringUtil.isNotEmpty(dsDto.getOtherDonationReason())){
                    if(dsDto.getOtherDonationReason().length() > 100){
                        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                        repMap.put("number","100");
                        repMap.put("fieldNo","(14) Other Reason for Donation");
                        String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDonation"), errMsg));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDonation"), errMsgErr006));
                }
            }
        }

        if(dsDto.isDonatedForResearch()){//Research is selected
            validPurposeResearch(errorMsgs,dsDto,fieldCellMap,i);
        }
        if(dsDto.isDonatedForTraining()){//Training is selected
            if(StringUtil.isNotEmpty(dsDto.getTrainingNum())){
                if(dsDto.getTrainingNum().length() > 2){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","(25) No. Used for Training");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUsedForTraining"), errMsg));
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUsedForTraining"), errMsgErr006));
            }
        }
        if(dsDto.isDonatedForTreatment()){//Treatment is selected
            if(StringUtil.isEmpty(dsDto.getDirectedDonation())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isDirectedDonation"), errMsgErr006));
            }
            if(StringUtil.isNotEmpty(dsDto.getTreatNum())){
                if(dsDto.getTreatNum().length() > 2){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","(19) No. Donated For Treatment");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForTreatment"), errMsg));
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForTreatment"), errMsgErr006));
            }
        }
        if(!dsDto.isDonatedForTraining() && !dsDto.isDonatedForResearch() && !dsDto.isDonatedForTreatment()){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_treatment"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_research"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_training"), errMsgErr006));
        }
    }
    private void validDonorIdentity(List<FileErrorMsg> errorMsgs,DonorSampleDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        switch (dsDto.getSampleType()){
            case DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE:
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE:
                validFemaleIdentity(errorMsgs,dsDto,fieldCellMap,i);
                break;
            case DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM:
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM:
                validMaleIdentity(errorMsgs,dsDto,fieldCellMap,i);
                break;
            case DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO:
                validFemaleIdentity(errorMsgs,dsDto,fieldCellMap,i);
                validMaleIdentity(errorMsgs,dsDto,fieldCellMap,i);
                break;
        }
    }
    private void validFemaleIdentity(List<FileErrorMsg> errorMsgs,DonorSampleDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        if(StringUtil.isNotEmpty(dsDto.getDonorIdentityKnown())){
            if(DataSubmissionConsts.DONOR_IDENTITY_KNOWN.equals(dsDto.getDonorIdentityKnown())){
                if(StringUtil.isNotEmpty(dsDto.getIdType())){
                    if(StringUtil.isNotEmpty(dsDto.getIdNumber())){
                        if(dsDto.getIdNumber().length() > 20){
                            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                            repMap.put("number","20");
                            repMap.put("fieldNo","(5) Female Donor ID No.");
                            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdNo"), errMsg));
                        }
                    }else {
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdNo"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdType"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                }
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdentityKnown"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getDonorSampleCode())){
            if(dsDto.getDonorSampleCode().length() > 20){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","(6) Female Donor Sample Code");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleSampleCode"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleSampleCode"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getDonorSampleAge())){
            if(dsDto.getDonorSampleAge().length() > 2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(7) Age of Female Donor at the Point of Donation");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleAge"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleAge"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validMaleIdentity(List<FileErrorMsg> errorMsgs,DonorSampleDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        if(StringUtil.isNotEmpty(dsDto.getMaleDonorIdentityKnow())){
            if(dsDto.getMaleDonorIdentityKnow()){
                if(StringUtil.isNotEmpty(dsDto.getIdNumberMale())){
                    if(StringUtil.isNotEmpty(dsDto.getIdNumberMale())){
                        if(dsDto.getIdNumber().length() > 20){
                            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                            repMap.put("number","20");
                            repMap.put("fieldNo","(10) Male Donor ID No.");
                            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleIdNo"), errMsg));
                        }
                    }else {
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleIdNo"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleIdType"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                }
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleIdentityKnown"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getMaleDonorSampleAge())){
            if(dsDto.getMaleDonorSampleAge().length() > 2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(11) Age of Male Donor at the Point of Donation");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleAge"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleAge"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validPurposeResearch(List<FileErrorMsg> errorMsgs,DonorSampleDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        if(StringUtil.isNotEmpty(dsDto.getDonResForTreatNum())){
            if(dsDto.getDonResForTreatNum().length() > 2){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(20) No. Donated for Research (Usable for Treatment)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_useTreatment"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_useTreatment"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getDonResForCurCenNotTreatNum())){
            if(dsDto.getDonResForCurCenNotTreatNum().length() > 2){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(21) No. Donated for Research (Unusable for Treatment)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_unUseTreatment"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_unUseTreatment"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getDonatedForResearchOtherType())){
            if(dsDto.getDonatedForResearchOtherType().length() > 100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(24) Please indicate other type of research");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherTypeOfResearch"), errMsg));
            }
        }
        if(!dsDto.isDonatedForResearchHescr() && !dsDto.isDonatedForResearchRrar() && StringUtil.isEmpty(dsDto.getDonatedForResearchOtherType())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donatedForHESCResearch"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donatedForResearchRelatedToAR"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherTypeOfResearch"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }

    public void saveNonPatientDonorSampleFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto){
        log.info(StringUtil.changeForLog("----- sfo cycle upload file is saving -----"));
        List<DonorSampleDto> donorSampleDtoList = (List<DonorSampleDto>) request.getSession().getAttribute(DataSubmissionConsts.NON_PATIENT_DONORSAMPLE_LIST);
        if (donorSampleDtoList == null || donorSampleDtoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = donorSampleDtoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(donorSampleDtoList.spliterator(), useParallel)
                .map(dto -> {
                    return getArSuperDataSubmissionDto(request, arSuperDto, declaration, dto);
                })
                .collect(Collectors.toList());
        if (useParallel) {
            Collections.sort(arSuperList, Comparator.comparing(dto -> dto.getDataSubmissionDto().getSubmissionNo()));
        }
        arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(arSuperList);
        try {
            arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(arSuperList);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_LIST, (Serializable) arSuperList);
    }

    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, String declaration,DonorSampleDto dto) {
        ArSuperDataSubmissionDto newDto = DataSubmissionHelper.reNew(arSuperDto);
        newDto.setFe(true);
        DataSubmissionDto dataSubmissionDto = newDto.getDataSubmissionDto();
        String submissionNo = arDataSubmissionService.getSubmissionNo(newDto.getSelectionDto(),
                DataSubmissionConsts.DS_AR);
        dataSubmissionDto.setSubmitBy(DataSubmissionHelper.getLoginContext(request).getUserId());
        dataSubmissionDto.setSubmitDt(new Date());
        dataSubmissionDto.setSubmissionNo(submissionNo);
        if (StringUtil.isEmpty(declaration)){
            dataSubmissionDto.setDeclaration("1");
        } else {
            dataSubmissionDto.setDeclaration(declaration);
        }
        dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_CYCLE_All);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setDonorSampleDto(dto);
        return newDto;
    }

}
