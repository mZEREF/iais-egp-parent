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
public class DonationStageUploadServiceImpl {
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;
    private static final String DONATION_STAGE_LIST = "DONATION_STAGE_LIST";
    @Autowired
    private ArBatchUploadCommonServiceImpl commonService;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    public Map<String,String> getErrorMap(HttpServletRequest request){
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = commonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = commonService.getPageShowFileDto(fileEntry);
        ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
        errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
        List<DonationStageDto> donationStageDtos = null;
        if(errorMap.isEmpty()){
            String fileName = fileEntry.getValue().getName();
            if (!fileName.equals("(For Registered Patients Only) Donation File Upload.xlsx") && !fileName.equals("(For Registered Patients Only) Donation File Upload.csv")) {
                errorMap.put("uploadFileError", "Please change the file name.");
            }
            List<RegisteredPatientDonorSampleExcelDto> donationExcelDtoList = commonService.getExcelDtoList(fileEntry, RegisteredPatientDonorSampleExcelDto.class);
            fileItemSize = donationExcelDtoList.size();
            if(fileItemSize == 0){
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            }else {
                Map<String, ExcelPropertyDto> donationStageFieldCellMap = ExcelValidatorHelper.getFieldCellMap(RegisteredPatientDonorSampleExcelDto.class);
                List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
                donationStageDtos = getDonationStageDtoList(donationExcelDtoList,errorMsgs,donationStageFieldCellMap,request);
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(donationStageDtos, "file", donationStageFieldCellMap));
                int count = 0;
                for(DonationStageDto dto : donationStageDtos){
                    count ++;
                    Map<Integer,Boolean> rowIdRes = (Map<Integer,Boolean>) request.getSession().getAttribute("rowIdRes");
                    if(rowIdRes.get(count) != null && rowIdRes.get(count)){
                        validDonorSample(errorMsgs, dto, donationStageFieldCellMap, count, request);
                    }
                }
                commonService.clearRowIdSession(request);
                if(!errorMsgs.isEmpty()){
                    commonService.getErrorRowInfo(errorMap,request,errorMsgs);
                }
            }
        }
        if (errorMap.isEmpty()){
            request.getSession().setAttribute(DONATION_STAGE_LIST, donationStageDtos);
        }
        return errorMap;
    }
    private Integer getIntBoolen(String value){
        if(value == null){
            return null;
        }
        return "Yes".equals(value) ? 1 : 0;
    }
    public  List<DonationStageDto> getDonationStageDtoList(List<RegisteredPatientDonorSampleExcelDto> donationStageExcelDtos, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        if (donationStageExcelDtos == null) {
            return null;
        }
        List<DonationStageDto> result = IaisCommonUtils.genNewArrayList();
        int count = 0;
        for (RegisteredPatientDonorSampleExcelDto excelDto : donationStageExcelDtos) {
            count ++;
            DonationStageDto dto = new DonationStageDto();
            commonService.saveRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo());
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setLocalOrOversea(getIntBoolen(excelDto.getLocalOrOverseas()));
            dto.setDonatedType(commonService.getMstrKeyByValue(excelDto.getTypeOfSample(),"DONTY"));
            dto.setIsOocyteDonorPatient(getIntBoolen(excelDto.getIsOocyteDonorPatient()));
            dto.setIsFemaleIdentityKnown(getIntBoolen(excelDto.getFemaleIdentityKnown()));
            dto.setFemaleIdType(getIntBoolen(excelDto.getFemaleIdType()));
            dto.setFemaleDonorSampleCode(excelDto.getFemaleSampleCode());
            dto.setFemaleDonorAgeStr(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getFemaleAge(),"femaleAge"));
            dto.setIsSpermDonorPatient(getIntBoolen(excelDto.getIsSpermDonorPatientsHus()));
            dto.setIsMaleIdentityKnown(getIntBoolen(excelDto.getMaleIdentityKnown()));
            dto.setMaleIdType(getIntBoolen(excelDto.getMaleIdType()));
            dto.setMaleIdNumber(excelDto.getMaleIdNo());
            dto.setMaleDonorAgeStr(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getMaleAge(),"maleAge"));
            dto.setDonatedCentre(excelDto.getInstitutionFrom());
            dto.setDonationReason(excelDto.getReasonsForDonation());
            dto.setOtherDonationReason(excelDto.getOtherReasonsForDonation());
            dto.setDonatedForTreatment(getIntBoolen(excelDto.getPurposeOfDonation_treatment()));
            dto.setDonatedForResearch(getIntBoolen(excelDto.getPurposeOfDonation_research()));
            dto.setDonatedForTraining(getIntBoolen(excelDto.getPurposeOfDonation_training()));
            dto.setIsDirectedDonation(getIntBoolen(excelDto.getIsDirectedDonation()));
            dto.setTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForTreatment(),"noDonatedForTreatment"));
            dto.setTrainingNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoUsedForTraining(),"noUsedForTraining"));
            dto.setDonResForTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForResearch_useTreatment(),"noDonatedForResearch_useTreatment"));
            dto.setDonResForCurCenNotTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForResearch_unUseTreatment(),"nnoDonatedForResearch_unUseTreatment"));
            dto.setDonatedForResearchHescr(getIntBoolen(excelDto.getDonatedForHESCResearch()));
            dto.setDonatedForResearchRrar(getIntBoolen(excelDto.getDonatedForResearchRelatedToAR()));
            dto.setDonatedForResearchOtherType(excelDto.getOtherTypeOfResearch());
            result.add(dto);
        }
        return result;
    }

    public void validDonorSample(List<FileErrorMsg> errorMsgs, DonationStageDto dsDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, HttpServletRequest request){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");

        if(StringUtil.isEmpty(dsDto.getLocalOrOversea())){//localOrOverseas is empty
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("localOrOverseas"), errMsgErr006));
        }else {
            if(dsDto.getLocalOrOversea() == 1){//local
                ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
                String hciCode = arSuperDataSubmissionDto.getHciCode();
                dsDto.setDonatedCentre(hciCode);
            }else {//oversea
                if(StringUtil.isNotEmpty(dsDto.getDonatedCentre())){
                    if(dsDto.getDonatedCentre().length()>256){
                        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                        repMap.put("number","256");
                        repMap.put("fieldNo","(15) Which Institution was the Sample Donated From?");
                        String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("institutionFrom"), errMsg));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("institutionFrom"), errMsgErr006));
                }
            }
        }

        if(StringUtil.isEmpty(dsDto.getDonatedType())){
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
                        repMap.put("fieldNo","(17) Other Reason for Donation");
                        String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDonation"), errMsg));
                    }
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDonation"), errMsgErr006));
                }
            }
        }

        if(dsDto.getDonatedForResearch() != null && dsDto.getDonatedForResearch() == 1){//Research is selected
            validPurposeResearch(errorMsgs,dsDto,fieldCellMap,i);
        }
        if(dsDto.getDonatedForResearch() != null && dsDto.getDonatedForTraining() == 1){//Training is selected
            if(StringUtil.isNotEmpty(dsDto.getTrainingNum())){
                if(String.valueOf(dsDto.getTrainingNum()).length() > 2){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","(28) No. Used for Training");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUsedForTraining"), errMsg));
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUsedForTraining"), errMsgErr006));
            }
        }
        if(dsDto.getDonatedForResearch() != null && dsDto.getDonatedForTreatment() == 1){//Treatment is selected
            if(StringUtil.isEmpty(dsDto.getIsDirectedDonation())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isDirectedDonation"), errMsgErr006));
            }
            if(StringUtil.isNotEmpty(dsDto.getTreatNum())){
                if(String.valueOf(dsDto.getTreatNum()).length() > 2){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","(22) No. Donated For Treatment");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForTreatment"), errMsg));
                }
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForTreatment"), errMsgErr006));
            }
        }
        if((dsDto.getDonatedForTraining() == null || dsDto.getDonatedForTraining() != 1)
                &&(dsDto.getDonatedForResearch() == null || dsDto.getDonatedForResearch() != 1)
                && (dsDto.getDonatedForTreatment() == null || dsDto.getDonatedForTreatment() !=1)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_treatment"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_research"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("purposeOfDonation_training"), errMsgErr006));
        }
    }
    private void validDonorIdentity(List<FileErrorMsg> errorMsgs,DonationStageDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        switch (dsDto.getDonatedType()){
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
    private void validFemaleIdentity(List<FileErrorMsg> errorMsgs,DonationStageDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){

        if(StringUtil.isNotEmpty(dsDto.getIsOocyteDonorPatient())){
            if(dsDto.getIsOocyteDonorPatient() != 1){
                if(StringUtil.isNotEmpty(dsDto.getIsFemaleIdentityKnown())){
                    if(dsDto.getIsFemaleIdentityKnown() == 1){
                        if(StringUtil.isNotEmpty(dsDto.getFemaleIdType())){
//                    if(StringUtil.isNotEmpty(dsDto.getFemaleIdNumber())){
//                        if(dsDto.getFemaleIdNumber().length() > 20){
//                            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
//                            repMap.put("number","20");
//                            repMap.put("fieldNo","(5) Female Donor ID No.");
//                            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
//                            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdNo"), errMsg));
//                        }
//                    }else {
//                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdNo"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
//                    }
                        }else {
                            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdType"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                        }
                    }
                } else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleIdentityKnown"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                }
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteDonorPatient"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }

        if(StringUtil.isNotEmpty(dsDto.getFemaleDonorSampleCode())){
            if(dsDto.getFemaleDonorSampleCode().length() > 20){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","20");
                repMap.put("fieldNo","(8) Female Donor Sample Code");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleSampleCode"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleSampleCode"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getFemaleDonorAgeStr())){
            if(dsDto.getFemaleDonorAgeStr().length() > 2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(9) Age of Female Donor at the Point of Donation");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleAge"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("femaleAge"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validMaleIdentity(List<FileErrorMsg> errorMsgs,DonationStageDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){

        if(StringUtil.isNotEmpty(dsDto.getIsOocyteDonorPatient())){
            if(dsDto.getIsOocyteDonorPatient() != 1){
                if(StringUtil.isNotEmpty(dsDto.getIsMaleIdentityKnown())){
                    if(dsDto.getIsMaleIdentityKnown() == 1){
                        if(StringUtil.isNotEmpty(dsDto.getMaleIdNumber())){
                            if(StringUtil.isNotEmpty(dsDto.getMaleIdNumber())){
                                if(dsDto.getMaleIdNumber().length() > 20){
                                    Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                                    repMap.put("number","20");
                                    repMap.put("fieldNo","(13) Male Donor ID No.");
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
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isSpermDonorPatientsHus"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }

        if(StringUtil.isNotEmpty(dsDto.getMaleDonorAgeStr())){
            if(dsDto.getMaleDonorAgeStr().length() > 2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(14) Age of Male Donor at the Point of Donation");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleAge"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("maleAge"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validPurposeResearch(List<FileErrorMsg> errorMsgs,DonationStageDto dsDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        if(StringUtil.isNotEmpty(dsDto.getDonResForTreatNum())){
            if(String.valueOf(dsDto.getDonResForTreatNum()).length() > 2){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(23) No. Donated for Research (Usable for Treatment)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_useTreatment"), errMsg));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDonatedForResearch_useTreatment"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(dsDto.getDonResForCurCenNotTreatNum())){
            if(String.valueOf(dsDto.getDonResForCurCenNotTreatNum()).length() > 2){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","2");
                repMap.put("fieldNo","(24) No. Donated for Research (Unusable for Treatment)");
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
                repMap.put("fieldNo","(27) Please indicate other type of research");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherTypeOfResearch"), errMsg));
            }
        }
        if((dsDto.getDonatedForResearchHescr() == null || dsDto.getDonatedForResearchHescr() == 1)
                && (dsDto.getDonatedForResearchRrar() == null || dsDto.getDonatedForResearchRrar() == 1)
                && StringUtil.isEmpty(dsDto.getDonatedForResearchOtherType())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donatedForHESCResearch"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("donatedForResearchRelatedToAR"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherTypeOfResearch"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }

    public void saveDonationStage(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto){
        log.info(StringUtil.changeForLog("----- donation stage upload file is saving -----"));
        List<DonationStageDto> donationStageDtoList = (List<DonationStageDto>) request.getSession().getAttribute(DONATION_STAGE_LIST);
        if (donationStageDtoList == null || donationStageDtoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = donationStageDtoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(donationStageDtoList.spliterator(), useParallel)
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

    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, String declaration,DonationStageDto dto) {
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
        dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_STAGE_DONATION);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setDonationStageDto(dto);
        return newDto;
    }


}
