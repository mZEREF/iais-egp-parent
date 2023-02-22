package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpSiErrRowsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDefectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.IUICoFundingStageExcelDto;
import com.ecquaria.cloud.moh.iais.dto.IUICycleStageExcelDto;
import com.ecquaria.cloud.moh.iais.dto.OutcomeOfIUICycleStageExcelDto;
import com.ecquaria.cloud.moh.iais.dto.OutcomeOfPregnancyExcelDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IUICycleBatchUploadImpl {
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;
    @Autowired
    private ArBatchUploadCommonServiceImpl arBatchUploadCommonService;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    public Map<String, String> getErrorMap(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = arBatchUploadCommonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = arBatchUploadCommonService.getPageShowFileDto(fileEntry);
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
            List<IUICycleStageExcelDto> iuiCycleStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, IUICycleStageExcelDto.class);
            List<OutcomeOfIUICycleStageExcelDto> outcomeOfIUICycleStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,OutcomeOfIUICycleStageExcelDto.class);
            List<IUICoFundingStageExcelDto> iuiCoFundingStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,IUICoFundingStageExcelDto.class);
            List<OutcomeOfPregnancyExcelDto> outcomeOfPregnancyExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,OutcomeOfPregnancyExcelDto.class);
            fileItemSize = iuiCycleStageExcelDtoList.size();
            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> iuiCycleStageFieldCellMap = ExcelValidatorHelper.getFieldCellMap(IUICycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> outcomeOfIUIFieldCellMap = ExcelValidatorHelper.getFieldCellMap(OutcomeOfIUICycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> iuiCoFundingFieldCellMap = ExcelValidatorHelper.getFieldCellMap(IUICoFundingStageExcelDto.class);
                Map<String, ExcelPropertyDto> outcomeOfPregnancyFieldCellMap = ExcelValidatorHelper.getFieldCellMap(OutcomeOfPregnancyExcelDto.class);

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

                arBatchUploadCommonService.getErrorRowInfo(errorMap,request,errorMsgs);
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, "isIUICycleFile", Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request, "isIUICycleFile", Boolean.TRUE);
            request.getSession().setAttribute(DataSubmissionConsts.NON_PATIENT_DONORSAMPLE_LIST, null);
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
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,Boolean.FALSE);
            dto.setOwnPremises(getBoolen(excelDto.getInOwn()));
            dto.setOtherPremises(excelDto.getNameOfPremise());
            try {
                dto.setStartDate(Formatter.parseDate(excelDto.getDateStarted()));
                Map<Integer,Date> startDate = (Map<Integer, Date>) ParamUtil.getSessionAttr(request,"cycleStartDate");
                startDate.put(count,Formatter.parseDate(excelDto.getDateStarted()));
                ParamUtil.setSessionAttr(request,"cycleStartDate",(Serializable) startDate);
            } catch (ParseException e) {
                errorMsgs.add(new FileErrorMsg(count, fieldCellMap.get("dateStarted"), MessageUtil.getMessageDesc("GENERAL_ERR0033")));
            }
            dto.setCurMarrChildNum(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfMarriageChildren_current"));
            dto.setPrevMarrChildNum(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfMarriageChildren_previous"));
            dto.setIuiDeliverChildNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfMarriageChildren_current(),"noOfIUIChildren"));
            if(StringUtil.isNotEmpty(excelDto.getSourceOfSemen())){
                List<String> semenSources = IaisCommonUtils.genNewArrayList();
                semenSources.add(excelDto.getSourceOfSemen());
                dto.setSemenSources(semenSources);
            }
            dto.setExtractVialsOfSperm(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfVialsSpermExtracted(),"noOfVialsSpermExtracted"));
            dto.setUsedVialsOfSperm(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoOfVialsSpermExtracted(),"noOfVialsSpermUsedInCycle"));
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
            donor.setFreshSpermAge(excelDto.getDonorAge());
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
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,Boolean.FALSE);
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
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,Boolean.FALSE);
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
    private List<PregnancyOutcomeBabyDto> getBabyDtos(OutcomeOfPregnancyExcelDto excelDto, String no){
        List<PregnancyOutcomeBabyDto> babyDtoList = IaisCommonUtils.genNewArrayList();
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
        babyDtoList.add(babyDto);
        return babyDtoList;
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
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,false);
            dto.setFirstUltrasoundOrderShow(excelDto.getOrderShown());
            dto.setWasSelFoeReduCarryOut(getBoolen(excelDto.getIsFoetalReduction()) ? 1 : 0);
            String outcome = excelDto.getOutcomeOfPregnancy();
            dto.setPregnancyOutcome(arBatchUploadCommonService.getMstrKeyByValue(outcome,"OUTOPRE"));
            dto.setMaleLiveBirthNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoLiveBirthMale(),"noLiveBirthMale"));
            dto.setFemaleLiveBirthNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoLiveBirthFemale(),"noLiveBirthFemale"));
            dto.setStillBirthNum(excelDto.getNoStillBirth());
            dto.setSpontAbortNum(excelDto.getNoOfSpontaneousAbortion());
            dto.setIntraUterDeathNum(excelDto.getNoOfIntraUterineDeath());
            dto.setDeliveryMode(arBatchUploadCommonService.getMstrKeyByValue(excelDto.getModeOfDelivery(),"MODEODE"));
            try {
                dto.setDeliveryDate(Formatter.parseDate(excelDto.getDateOfDelivery()));
            } catch (ParseException e) {
                arBatchUploadCommonService.validateParseDate(errorMsgs,excelDto.getDateOfDelivery(),fieldCellMap,count,"dateOfDelivery",false);
            }
            dto.setDeliveryDateType("Yes".equals(excelDto.getDateOfDeliveryIsUnknown()) ? "Unknown" : "Known");
            dto.setBirthPlace(excelDto.getPlaceOfBirth());
            dto.setLocalBirthPlace(excelDto.getPlaceOfLocalBirth());
            dto.setBabyDetailsUnknown(getBoolen(excelDto.getBabyDetailsUnknown()));
            dto.setNicuCareBabyNum(Integer.parseInt(excelDto.getBaby1HeartAnomalies()));
            dto.setL2CareBabyNum(Integer.parseInt(excelDto.getNoOfBabyToL2Care()));
            dto.setL2CareBabyDays(excelDto.getNoDaysBabyInL2());
            dto.setL3CareBabyNum(Integer.parseInt(excelDto.getNoOfBabyToL3Care()));
            dto.setL3CareBabyDays(excelDto.getNoDaysBabyInL3());
            if("No Live Birth".equals(outcome) || "Unknown".equals(outcome)){
                dto.setStillBirthNum(excelDto.getNoStillBirthNoLiveBirth());
                dto.setSpontAbortNum(excelDto.getNoOfSpontaneousAbortionNoLiveBirth());
                dto.setIntraUterDeathNum(excelDto.getNoOfIntraUterineDeathNoLiveBirth());
            }
            dto.setOtherPregnancyOutcome(excelDto.getFreetextOutcomeOfPregnancy());
            for (int i = 1; i <= 4; i ++){
                String no = String.valueOf(i);
                dto.setPregnancyOutcomeBabyDtos(getBabyDtos(excelDto,no));
            }
            result.add(dto);
        }
        return result;
    }
    private <T> void doValid(List<FileErrorMsg> errorMsgs,List<T> tDtos,Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        int count = 0;
        for (T item : tDtos){
            count++;
            if(item instanceof IuiCycleStageDto){
                validIUICycleStage(errorMsgs,(IuiCycleStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof OutcomeStageDto){
                validOutcomeOfIUI(errorMsgs,(OutcomeStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof IuiTreatmentSubsidiesDto){
                validCofundingIUI(errorMsgs,(IuiTreatmentSubsidiesDto) item,fieldCellMap,count,request);
            }
            if(item instanceof PregnancyOutcomeStageDto){
                Map<Integer,Date> cycleStartDateMap = (Map<Integer,Date>)ParamUtil.getSessionAttr(request,"cycleStartDate");
                Date cycleStartDate = cycleStartDateMap.get(count);
                arBatchUploadCommonService.validOutcomeOfPregnancy(errorMsgs,(PregnancyOutcomeStageDto) item,fieldCellMap,count,cycleStartDate);
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
                if("NRIC".equals(idType) || "FIN".equals(idType) || "Passport".equals(idType)){
                    arBatchUploadCommonService.validatePatientIdTypeAndNumber(idType,idNum,fieldCellMap,errorMsgs,i,"donorIdType","donorIdNoSampleCode",request,Boolean.FALSE);
                }else {
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
        if(StringUtil.isNotEmpty(donor.getFreshSpermAge())){
            if(donor.getFreshSpermAge().length() > 2){
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
        if(StringUtil.isEmpty(tsDto.getArtCoFunding())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isCoFunded"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }

        //todo valid (Is there an Approved Appeal?)

        if(StringUtil.isEmpty(tsDto.getAppealNumber())){
            if(tsDto.getAppealNumber().length() > 10){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","10");
                repMap.put("fieldNo","(5) Please indicate appeal reference number (if applicable)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("appealReferenceNum"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("appealReferenceNum"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
}
