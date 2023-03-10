package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class IUICycleBatchUploadImpl {
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;
    private static final String IUI_CYCLE_STAGE_FILELIST = "IUI_CYCLE_STAGE_FILELIST";
    private static final String IUI_OUTCOME_FILELIST = "IUI_OUTCOME_FILELIST";
    private static final String IUI_COFUNDING_FILELIST = "IUI_COFUNDING_FILELIST";
    private static final String PREGNANCY_OUTCOME_FILELIST = "PREGNANCY_OUTCOME_FILELIST";

    @Autowired
    private ArBatchUploadCommonServiceImpl commonService;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    @Autowired
    private ArFeClient arFeClient;
    public Map<String, String> getErrorMap(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = commonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = commonService.getPageShowFileDto(fileEntry);
        ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
        errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
        List<IuiCycleStageDto> iuiCycleStageDtos = null;
        List<OutcomeStageDto> outcomeStageDtos = null;
        List<IuiTreatmentSubsidiesDto> iuiTreatmentSubsidiesDtos = null;
        List<PregnancyOutcomeStageDto> pregnancyOutcomeStageDtos = null;
        if (errorMap.isEmpty()) {
            String fileName = fileEntry.getValue().getName();
            if (!fileName.equals("IUI Cycle File Upload.xlsx") && !fileName.equals("IUI Cycle File Upload.csv")) {
                errorMap.put("uploadFileError", "Please change the file name.");
            }
            List<IUICycleStageExcelDto> iuiCycleStageExcelDtoList = commonService.getExcelDtoList(fileEntry, IUICycleStageExcelDto.class);
            List<OutcomeOfIUICycleStageExcelDto> outcomeOfIUICycleStageExcelDtoList = commonService.getExcelDtoList(fileEntry,OutcomeOfIUICycleStageExcelDto.class);
            List<IUICoFundingStageExcelDto> iuiCoFundingStageExcelDtoList = commonService.getExcelDtoList(fileEntry,IUICoFundingStageExcelDto.class);
            List<OutcomeOfPregnancyExcelDto> outcomeOfPregnancyExcelDtoList = commonService.getExcelDtoList(fileEntry,OutcomeOfPregnancyExcelDto.class);
            fileItemSize = iuiCycleStageExcelDtoList.size();
            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> iuiCycleStageFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(IUICycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> outcomeOfIUIFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(OutcomeOfIUICycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> iuiCoFundingFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(IUICoFundingStageExcelDto.class);
                Map<String, ExcelPropertyDto> outcomeOfPregnancyFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(OutcomeOfPregnancyExcelDto.class);

                List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
                iuiCycleStageDtos = getIUICycleStageList(iuiCycleStageExcelDtoList,errorMsgs,iuiCycleStageFieldCellMap,request);
                outcomeStageDtos = getOutcomeOfIUICycleStageList(outcomeOfIUICycleStageExcelDtoList,errorMsgs,outcomeOfIUIFieldCellMap,request);
                iuiTreatmentSubsidiesDtos = getIUICoFundingStageList(iuiCoFundingStageExcelDtoList,errorMsgs,iuiCoFundingFieldCellMap,request);
                pregnancyOutcomeStageDtos = getPregnancyOutcomeList(outcomeOfPregnancyExcelDtoList,errorMsgs,outcomeOfPregnancyFieldCellMap,request);

                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(iuiCycleStageDtos, "file", iuiCycleStageFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(outcomeStageDtos, "file", outcomeOfIUIFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(iuiTreatmentSubsidiesDtos, "file", iuiCoFundingFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(pregnancyOutcomeStageDtos, "file", outcomeOfPregnancyFieldCellMap));

                doValid(errorMsgs,iuiCycleStageDtos,iuiCycleStageFieldCellMap,request);
                doValid(errorMsgs,outcomeStageDtos,outcomeOfIUIFieldCellMap,request);
                doValid(errorMsgs,iuiTreatmentSubsidiesDtos,iuiCoFundingFieldCellMap,request);
                doValid(errorMsgs,pregnancyOutcomeStageDtos,outcomeOfPregnancyFieldCellMap,request);

                if(!errorMsgs.isEmpty()){
                    commonService.clearRowIdSession(request);
                    commonService.getErrorRowInfo(errorMap,request,errorMsgs);
                }
            }
        }
        if (errorMap.isEmpty()){
            request.getSession().setAttribute(IUI_CYCLE_STAGE_FILELIST, iuiCycleStageDtos);
            request.getSession().setAttribute(IUI_OUTCOME_FILELIST, outcomeStageDtos);
            request.getSession().setAttribute(IUI_COFUNDING_FILELIST , iuiTreatmentSubsidiesDtos);
            request.getSession().setAttribute(PREGNANCY_OUTCOME_FILELIST, pregnancyOutcomeStageDtos);
        }
        return errorMap;
    }
    private boolean getBoolen(String val){
        return "Yes".equals(val);
    }
    public List<IuiCycleStageDto> getIUICycleStageList(List<IUICycleStageExcelDto> iuiCycleStageExcelDtoList,List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        Map<Integer,Date> cycleStartDate = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(request,"cycleStartDate", (Serializable) cycleStartDate);
        if (iuiCycleStageExcelDtoList == null) {
            return null;
        }
        List<IuiCycleStageDto> result = IaisCommonUtils.genNewArrayList(iuiCycleStageExcelDtoList.size());
        int count = 0;
        for (IUICycleStageExcelDto excelDto : iuiCycleStageExcelDtoList) {
            count ++;
            IuiCycleStageDto dto = new IuiCycleStageDto();
            commonService.saveRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo());
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);dto.setOwnPremises(getBoolen(excelDto.getInOwn()));
            dto.setOtherPremises(excelDto.getNameOfPremise());
            try {
                dto.setStartDate(Formatter.parseDate(excelDto.getDateStarted()));
                Map<Integer,Date> startDate = (Map<Integer, Date>) ParamUtil.getSessionAttr(request,"cycleStartDate");
                startDate.put(count,Formatter.parseDate(excelDto.getDateStarted()));
                ParamUtil.setSessionAttr(request,"cycleStartDate",(Serializable) startDate);
            } catch (ParseException e) {
                errorMsgs.add(new FileErrorMsg(count, fieldCellMap.get("dateStarted"), MessageUtil.getMessageDesc("GENERAL_ERR0033")));
            }
            dto.setCurMarrChildNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfMarriageChildren_current"));
            dto.setPrevMarrChildNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfMarriageChildren_previous"));
            dto.setIuiDeliverChildNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfIUIChildren"));
            if(StringUtil.isNotEmpty(excelDto.getSourceOfSemen())){
                List<String> semenSources = IaisCommonUtils.genNewArrayList();
                semenSources.add(excelDto.getSourceOfSemen());
                dto.setSemenSources(semenSources);
            }
            dto.setExtractVialsOfSperm(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfVialsSpermExtracted(),"noOfVialsSpermExtracted"));
            dto.setUsedVialsOfSperm(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfVialsSpermExtracted(),"noOfVialsSpermUsedInCycle"));
            List<DonorDto> donorDtos = IaisCommonUtils.genNewArrayList();
            DonorDto donor = new DonorDto();
            donorDtos.add(donor);
            donor.setDirectedDonation(getBoolen(excelDto.getIsDirectedDonation()));
            String idType = excelDto.getDonorIdType();
            donor.setIdType(idType);
            String idNum = excelDto.getDonorIdNoSampleCode();
            if("NRIC".equals(idType) || "FIN".equals(idType) || "Passport".equals(idType)){
                donor.setIdNumber(idNum);
            }else {
                donor.setDonorSampleCode(idNum);
            }
            String age = commonService.excelStrToStrNum(errorMsgs, fieldCellMap, count, excelDto.getDonorAge(), "donorAge");
            donor.setAge(age);
            donor.setRelation(excelDto.getDonorRelationToPatient());
            dto.setDonorDtos(donorDtos);
            result.add(dto);
        }
        return result;
    }

    public List<OutcomeStageDto> getOutcomeOfIUICycleStageList(List<OutcomeOfIUICycleStageExcelDto> outcomeStageDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        if (outcomeStageDtoList == null) {
            return null;
        }
        List<OutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomeStageDtoList.size());
        int count = 0;
        for (OutcomeOfIUICycleStageExcelDto excelDto : outcomeStageDtoList) {
            count ++;
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            OutcomeStageDto dto = new OutcomeStageDto();
            dto.setPregnancyDetected(excelDto.getIsClinicalPregnancyDetected());
            result.add(dto);
        }
        return result;
    }

    public List<IuiTreatmentSubsidiesDto> getIUICoFundingStageList(List<IUICoFundingStageExcelDto> iuiCoFundingStageExcelDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        if (iuiCoFundingStageExcelDtoList == null) {
            return null;
        }
        List<IuiTreatmentSubsidiesDto> result = IaisCommonUtils.genNewArrayList(iuiCoFundingStageExcelDtoList.size());
        int count = 0;
        for (IUICoFundingStageExcelDto excelDto : iuiCoFundingStageExcelDtoList) {
            count ++;
            IuiTreatmentSubsidiesDto dto = new IuiTreatmentSubsidiesDto();
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setThereAppeal(excelDto.getIsApprovedAppeal());
            dto.setAppealNumber(excelDto.getAppealReferenceNum());

            if(StringUtil.isNotEmpty(excelDto.getIsCoFunded())){
                dto.setArtCoFunding(getBoolen(excelDto.getIsCoFunded()) ? DataSubmissionConsts.ART_APPLE_FROZEN_THREE : DataSubmissionConsts.ART_APPLE_FRESH_THREE);
            }
            result.add(dto);
        }
        return result;
    }
    private String getExcelValueFromMethod(OutcomeOfPregnancyExcelDto excelDto, String methodName){
        try {
            Method method = OutcomeOfPregnancyExcelDto.class.getMethod(methodName);
            return (String) method.invoke(excelDto);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private PregnancyOutcomeBabyDto getBabyDtos(OutcomeOfPregnancyExcelDto excelDto, String no){

        PregnancyOutcomeBabyDto babyDto = new PregnancyOutcomeBabyDto();
        babyDto.setBirthWeight(getExcelValueFromMethod(excelDto,"getBaby" + no + "BirthWeight"));
        babyDto.setBirthDefect(getExcelValueFromMethod(excelDto,"getBaby" + no + "BirthDefect"));
        List<PregnancyOutcomeBabyDefectDto> defectDtoList = IaisCommonUtils.genNewArrayList();
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "ChromosomalAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT001");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "HeartAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT002");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "MusculoskeletalAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT003");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "NervousSystemAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT004");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "OtherFetalAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT005");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "RespiratorySystemAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT006");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "UrinarySystemAnomalies"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT007");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "DefectTypeOthers"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setDefectType("POSBDT008");
            defectDtoList.add(defectDto);
        }
        if(getBoolen(getExcelValueFromMethod(excelDto,"getBaby" + no + "DefectTypeOthers"))){
            PregnancyOutcomeBabyDefectDto defectDto = new PregnancyOutcomeBabyDefectDto();
            defectDto.setOtherDefectType(getExcelValueFromMethod(excelDto,"getBaby" + no + "FreetextDefectTypeOthers"));
            defectDtoList.add(defectDto);
        }
        babyDto.setPregnancyOutcomeBabyDefectDtos(defectDtoList);
        return babyDto;
    }

    public List<PregnancyOutcomeStageDto> getPregnancyOutcomeList(List<OutcomeOfPregnancyExcelDto> outcomeOfPregnancyExcelDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        if (outcomeOfPregnancyExcelDtoList == null) {
            return null;
        }
        List<PregnancyOutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomeOfPregnancyExcelDtoList.size());
        int count = 0;
        for (OutcomeOfPregnancyExcelDto excelDto : outcomeOfPregnancyExcelDtoList) {
            count ++;
            PregnancyOutcomeStageDto dto = new PregnancyOutcomeStageDto();
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setFirstUltrasoundOrderShow(excelDto.getOrderShown());
            dto.setWasSelFoeReduCarryOut(getBoolen(excelDto.getIsFoetalReduction()) ? 1 : 0);
            String outcome = excelDto.getOutcomeOfPregnancy();
            dto.setPregnancyOutcome(commonService.getMstrKeyByValue(outcome,"OUTOPRE"));
            dto.setMaleLiveBirthNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoLiveBirthMale(),"noLiveBirthMale"));
            dto.setFemaleLiveBirthNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoLiveBirthFemale(),"noLiveBirthFemale"));
            dto.setStillBirthNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoStillBirth(),"noStillBirth"));
            dto.setSpontAbortNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfSpontaneousAbortion(),"noOfSpontaneousAbortion"));
            dto.setIntraUterDeathNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfIntraUterineDeath(),"noOfIntraUterineDeath"));
            dto.setDeliveryMode(commonService.getMstrKeyByValue(excelDto.getModeOfDelivery(),"MODEODE"));
            try {
                dto.setDeliveryDate(Formatter.parseDate(excelDto.getDateOfDelivery()));
            } catch (ParseException e) {
                commonService.validateParseDate(errorMsgs,excelDto.getDateOfDelivery(),fieldCellMap,count,"dateOfDelivery",false);
                commonService.validateParseDate(errorMsgs,excelDto.getDateOfDelivery(),fieldCellMap,count,"dateOfDelivery",Boolean.FALSE);
            }
            dto.setDeliveryDateType("Yes".equals(excelDto.getDateOfDeliveryIsUnknown()) ? "Unknown" : "Known");
            dto.setBirthPlace(excelDto.getPlaceOfBirth());
            dto.setLocalBirthPlace(excelDto.getPlaceOfLocalBirth());
            dto.setBabyDetailsUnknown(getBoolen(excelDto.getBabyDetailsUnknown()));
            dto.setNicuCareBabyNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getBaby1HeartAnomalies(),"baby1HeartAnomalies"));
            dto.setL2CareBabyNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfBabyToL2Care(),"noOfBabyToL2Care"));
            dto.setL2CareBabyDays(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoDaysBabyInL2(),"noDaysBabyInL2"));
            dto.setL3CareBabyNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfBabyToL3Care(),"noOfBabyToL3Care"));
            dto.setL3CareBabyDays(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoDaysBabyInL3(),"noDaysBabyInL3"));
            if("No Live Birth".equals(outcome) || "Unknown".equals(outcome)){
                dto.setStillBirthNum(excelDto.getNoStillBirthNoLiveBirth());
                dto.setSpontAbortNum(excelDto.getNoOfSpontaneousAbortionNoLiveBirth());
                dto.setIntraUterDeathNum(excelDto.getNoOfIntraUterineDeathNoLiveBirth());
            }
            dto.setOtherPregnancyOutcome(excelDto.getFreetextOutcomeOfPregnancy());
            List<PregnancyOutcomeBabyDto> babyDtos = IaisCommonUtils.genNewArrayList();
            for (int i = 1; i <= 4; i ++){
                String no = String.valueOf(i);
                babyDtos.add(getBabyDtos(excelDto,no));
            }
            dto.setPregnancyOutcomeBabyDtos(babyDtos);
            result.add(dto);
        }
        return result;
    }
    private <T> void doValid(List<FileErrorMsg> errorMsgs,List<T> tDtos,Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        Map<Integer,Boolean> rowIdRes = (Map<Integer,Boolean>) request.getSession().getAttribute("rowIdRes");
        int count = 0;
        for (T item : tDtos){
            count++;
            if(item instanceof IuiCycleStageDto && rowIdRes.get(count)){
                validIUICycleStage(errorMsgs,(IuiCycleStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof OutcomeStageDto && rowIdRes.get(count)){
                validOutcomeOfIUI(errorMsgs,(OutcomeStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof IuiTreatmentSubsidiesDto && rowIdRes.get(count)){
                validCofundingIUI(errorMsgs,(IuiTreatmentSubsidiesDto) item,fieldCellMap,count,request);
            }
            if(item instanceof PregnancyOutcomeStageDto && rowIdRes.get(count)){
                Map<Integer,Date> cycleStartDateMap = (Map<Integer,Date>)ParamUtil.getSessionAttr(request,"cycleStartDate");
                Date cycleStartDate = cycleStartDateMap.get(count);
                commonService.validOutcomeOfPregnancy(errorMsgs,(PregnancyOutcomeStageDto) item,fieldCellMap,count,cycleStartDate);
            }
        }

    }
    private void validIUICycleStage(List<FileErrorMsg> errorMsgs,IuiCycleStageDto icDto,Map<String, ExcelPropertyDto> fieldCellMap,int i, HttpServletRequest request){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if(!icDto.isOwnPremises()){
            if(StringUtil.isNotEmpty(icDto.getOtherPremises())){
                if(icDto.getOtherPremises().length() > 66){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","66");
                    repMap.put("fieldNo","(4) Name of Premise Where IUI Treatment Is Performed");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("nameOfPremise"), errMsg));
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("nameOfPremise"), errMsgErr006));
            }
        }
        if(StringUtil.isNotEmpty(icDto.getStartDate())){
            Date now = new Date();
            Date startDate = icDto.getStartDate();
            if(startDate.getTime() > now.getTime()){//DS_ERR001
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("field","(5) Date Started");
                String errMsg = MessageUtil.getMessageDesc("DS_ERR001",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateStarted"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateStarted"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(icDto.getCurMarrChildNum())){
            if(icDto.getCurMarrChildNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfMarriageChildren_current"), "Can not be negative number"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfMarriageChildren_current"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(icDto.getPrevMarrChildNum())){
            if(icDto.getPrevMarrChildNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfMarriageChildren_previous"), "Can not be negative number"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfMarriageChildren_previous"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(icDto.getIuiDeliverChildNum())){
            Integer deliverNum = Integer.parseInt(icDto.getIuiDeliverChildNum());
            Integer curNum = icDto.getCurMarrChildNum();
            Integer preNum = icDto.getPrevMarrChildNum();
            if(deliverNum < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfIUIChildren"), "Can not be negative number"));
            }else if(deliverNum > curNum + preNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfIUIChildren"), "Cannot be greater than No. of Children with Current Marriage + No. of Children with Previous Marriage"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfIUIChildren"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(icDto.getExtractVialsOfSperm())){
            if(Integer.parseInt(icDto.getExtractVialsOfSperm()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfVialsSpermExtracted"), "Can not be negative number"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfVialsSpermExtracted"), errMsgErr006));
        }
        if(StringUtil.isNotEmpty(icDto.getUsedVialsOfSperm())){
            if(Integer.parseInt(icDto.getUsedVialsOfSperm()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfVialsSpermUsedInCycle"), "Can not be negative number"));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfVialsSpermUsedInCycle"), errMsgErr006));
        }
       if(StringUtil.isNotEmpty(icDto.getSemenSources())){
            if("Donor".equals(icDto.getSemenSources().get(0)) || "Donor's Testicular Tissue".equals(icDto.getSemenSources().get(0))){
                validDonor(errorMsgs,icDto,fieldCellMap,i,request);
            }
       }else {
           errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("sourceOfSemen"), errMsgErr006));
       }
    }
    private void validDonor(List<FileErrorMsg> errorMsgs,IuiCycleStageDto icDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){
        DonorDto donor = icDto.getDonorDtos().get(0);
        if(StringUtil.isEmpty(donor.getDirectedDonation())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isDirectedDonation"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(donor.getIdType())){
            if(StringUtil.isNotEmpty(donor.getIdNumber())){
                String idType = donor.getIdType();
                String idNum = donor.getIdNumber();
                String sampleCode = donor.getDonorSampleCode();
                if("NRIC".equals(idType) || "FIN".equals(idType) || "Passport".equals(idType)){
//                    if(!commonService.validDonor(idType, idNum)){
//                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorIdNoSampleCode"), MessageUtil.getMessageDesc("DS_ERR012")));
//                    }else {
//
//                    }
                }else {
//                    if(!commonService.validDonor(idType, sampleCode)){
//                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorIdNoSampleCode"), MessageUtil.getMessageDesc("DS_ERR012")));
//                    }
                    if(donor.getDonorSampleCode().length() > 20){
                        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                        repMap.put("number","20");
                        repMap.put("fieldNo","(15) Donor ID No./ Donor Sample Code");
                        String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorIdNoSampleCode"), errMsg));
                    }
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorIdNoSampleCode"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorIdType"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(donor.getAge())){
            if(donor.getAge().length() > 2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(17) Age of donor when sperm was collected");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorAge"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorAge"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isEmpty(donor.getRelation())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donorRelationToPatient"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validOutcomeOfIUI(List<FileErrorMsg> errorMsgs,OutcomeStageDto ocDto,Map<String, ExcelPropertyDto> fieldCellMap,int i, HttpServletRequest request){
        if(StringUtil.isEmpty(ocDto.getPregnancyDetected())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isClinicalPregnancyDetected"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validCofundingIUI(List<FileErrorMsg> errorMsgs,IuiTreatmentSubsidiesDto tsDto,Map<String, ExcelPropertyDto> fieldCellMap, int i, HttpServletRequest request){
        PatientIdDto patientId = commonService.getRowId(request,i);
        String idType = commonService.convertIdType(patientId.getIdType());
        String idNo = patientId.getIdNo();
        Integer iuiCoFundingCount = 0;
        try{
            iuiCoFundingCount = arFeClient.getIuiTotalCofundingCountByPatientIdTypeAndIdNo(idType,idNo).getEntity();
        }catch (Exception e){
            iuiCoFundingCount = 0;
        }

        if(StringUtil.isEmpty(tsDto.getArtCoFunding())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isCoFunded"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }

        if(iuiCoFundingCount >= 3 && commonService.validateIsNull(errorMsgs, tsDto.getThereAppeal(),fieldCellMap,i,"isApprovedAppeal")){
            if("NO".equals(tsDto.getThereAppeal())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isApprovedAppeal"), "patient's total co-funded IUI cycles entered in the system is ≥ 3,please select \"Yes\""));
            }
        }

        if(StringUtil.isNotEmpty(tsDto.getAppealNumber())){
            if(tsDto.getAppealNumber().length() > 10){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","10");
                repMap.put("fieldNo","(5) Please indicate appeal reference number (if applicable)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("appealReferenceNum"), errMsg));
            }
        }
    }
    public void saveIUICycleFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto){
        log.info(StringUtil.changeForLog("----- iui cycle file is saving -----"));
        List<IuiCycleStageDto> iuiCycleStageDtos = (List<IuiCycleStageDto>) request.getSession().getAttribute(IUI_CYCLE_STAGE_FILELIST);
        List<OutcomeStageDto> outcomeStageDtos = (List<OutcomeStageDto>) request.getSession().getAttribute(IUI_OUTCOME_FILELIST);
        List<IuiTreatmentSubsidiesDto> iuiTreatmentSubsidiesDtos = (List<IuiTreatmentSubsidiesDto>) request.getSession().getAttribute(IUI_COFUNDING_FILELIST);
        List<PregnancyOutcomeStageDto> pregnancyOutcomeStageDtos = (List<PregnancyOutcomeStageDto>) request.getSession().getAttribute(PREGNANCY_OUTCOME_FILELIST);
        if (iuiCycleStageDtos == null || iuiCycleStageDtos.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
//        boolean useParallel = iuiCycleStageDtos.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = IaisCommonUtils.genNewArrayList();
        for(int i = 0; i < iuiCycleStageDtos.size(); i ++){
            arSuperList.add(getArSuperDataSubmissionDto(request, arSuperDto, declaration,
                    iuiCycleStageDtos.get(i),
                    outcomeStageDtos.get(i),
                    iuiTreatmentSubsidiesDtos.get(i),
                    pregnancyOutcomeStageDtos.get(i),
                    i + 1));
        }
        arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(arSuperList);
        try {
            arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(arSuperList);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_LIST, (Serializable) arSuperList);
        clearFlieListSession(request);
        commonService.clearRowIdSession(request);
    }
    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, String declaration,
                                                                 IuiCycleStageDto iuiCycleStageDto,
                                                                 OutcomeStageDto outcomeStageDto,
                                                                 IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto,
                                                                 PregnancyOutcomeStageDto pregnancyOutcomeStageDto,
                                                                 int row) {
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
        CycleDto cycleDto = newDto.getCycleDto();
        PatientIdDto idDto = ((Map<Integer, PatientIdDto>) request.getSession().getAttribute("idMap")).get(row);
        String patientIdType = null;
        String patientIdNo = null;
        if(idDto != null){
            patientIdType = idDto.getIdType();
            patientIdNo = idDto.getIdNo();
        }
        if (StringUtil.isNotEmpty(patientIdType) && StringUtil.isNotEmpty(patientIdNo)){
            PatientInfoDto patientInfoDto = commonService.setPatientInfo(patientIdType, patientIdNo, request);
            newDto.setPatientInfoDto(patientInfoDto);
            cycleDto = commonService.setCycleDtoPatientCodeAndCycleType(patientInfoDto,cycleDto,DataSubmissionConsts.DS_CYCLE_IUI);
            newDto.setCycleDto(cycleDto);
        }
        dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_CYCLE_IUI);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setIuiCycleStageDto(iuiCycleStageDto);
        newDto.setOutcomeStageDto(outcomeStageDto);
        newDto.setIuiTreatmentSubsidiesDto(iuiTreatmentSubsidiesDto);
        newDto.setPregnancyOutcomeStageDto(pregnancyOutcomeStageDto);
        return newDto;
    }
    private void clearFlieListSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(IUI_CYCLE_STAGE_FILELIST);
        session.removeAttribute(IUI_OUTCOME_FILELIST);
        session.removeAttribute(IUI_COFUNDING_FILELIST);
        session.removeAttribute(PREGNANCY_OUTCOME_FILELIST);
    }
}
