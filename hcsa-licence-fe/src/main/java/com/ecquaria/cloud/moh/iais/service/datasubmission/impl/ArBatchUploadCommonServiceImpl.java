package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ArBatchUploadCommonServiceImpl
 *
 * @Author dongchi
 * @Date 2023/2/14 14:57
 **/
@Service
@Slf4j
public class ArBatchUploadCommonServiceImpl implements ArBatchUploadCommonService {

    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private ArFeClient arFeClient;

    @Override
    public PatientInfoDto setPatientInfo(String idType, String idNo, HttpServletRequest request) {
        if(idType == null || idNo == null) {
            return null;
        }
        PatientInfoDto patientInfoDto ;
        if ("FIN".equals(idType)) {
            boolean finValidation = SgNoValidator.validateFin(idNo);
            if(!finValidation) return null;

        } else if("NRIC".equals(idType)) {
            boolean nricValidation = SgNoValidator.validateNric(idNo);
            if (!nricValidation) return null;
        }
        String orgId = null;
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
        }

        if ("FIN".equals(idType) || "NRIC".equals(idType)) {
            // birthday now not in template
            // patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(idType,idNo, Formatter.formatDateTime(birthDate, AppConsts.DEFAULT_DATE_BIRTHDATE_FORMAT), orgId);
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(convertIdType(idType),idNo, orgId);

        }else {
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(convertIdType(idType),idNo, orgId);
        }
        return patientInfoDto;
    }

    @Override
    public Map.Entry<String, File> getFileEntry(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, SEESION_FILES_MAP_AJAX);
        if (fileMap == null || fileMap.isEmpty()) {
            return null;
        }
        // only one
        Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Map.Entry<String, File> next = iterator.next();
        File file = next.getValue();
        long length = file.length();
        if (length == 0) {
            return null;
        }
        return next;
    }

    @Override
    public PageShowFileDto getPageShowFileDto(Map.Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return null;
        }
        File file = fileEntry.getValue();
        PageShowFileDto pageShowFileDto = new PageShowFileDto();
        String index = fileEntry.getKey().substring(FILE_APPEND.length());
        String fileMd5 = FileUtils.getFileMd5(file);
        pageShowFileDto.setIndex(index);
        pageShowFileDto.setFileName(file.getName());
        pageShowFileDto.setFileMapId(FILE_APPEND + "Div" + index);
        pageShowFileDto.setSize((int) (file.length() / 1024));
        pageShowFileDto.setMd5Code(fileMd5);
        List<String> list = arDataSubmissionService.saveFileRepo(Collections.singletonList(file));
        if (!list.isEmpty()) {
            pageShowFileDto.setFileUploadUrl(list.get(0));
        }
        return pageShowFileDto;
    }

    @Override
    public <T> List<T> getExcelDtoList(Map.Entry<String, File> fileEntry, Class<T> tClass) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), tClass, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), tClass, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
    }

    @Override
    public boolean validPatientId(String patientIdType, String patientIdNumber,
                                  Map<String, ExcelPropertyDto> fieldCellMap, List<FileErrorMsg> errorMsgs, int i,
                                  String filedType,String filedNumber,HttpServletRequest request){
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");

        if (StringUtil.isEmpty(patientIdType)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedType), errMsgErr006));
            return false;
        }

        int maxLength = 9;
        if (StringUtil.isNotEmpty(patientIdType) && "Passport".equals(patientIdType)) {
            maxLength = 20;
        }
        if (StringUtil.isEmpty(patientIdNumber)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), errMsgErr006));
            return false;
        } else if (patientIdNumber.length() > maxLength) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field", "The field");
            params.put("maxlength", String.valueOf(maxLength));
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041",params);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), errMsg));
            return false;
        } else if ("NRIC".equals(patientIdType)){
            boolean b = SgNoValidator.validateFin(patientIdNumber);
            boolean b1 = SgNoValidator.validateNric(patientIdNumber);
            if (!(b || b1)) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), "Please key in a valid NRIC/FIN"));
                return false;
            }
        }
        request.getSession().setAttribute("correct", Boolean.TRUE);
        request.getSession().setAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_TYPE, patientIdType);
        request.getSession().setAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_NUMBER, patientIdNumber);
        return true;
    }

    @Override
    public boolean getBooleanValue(Object obj) {
        return "Yes".equals(obj);
    }

    @Override
    public int getErrorRowInfo(Map<String, String> errorMap, HttpServletRequest request, List<FileErrorMsg> errorMsgs) {
        int fileItemSize;
        List<DsArErrDto> errRowsDtos = IaisCommonUtils.genNewArrayList();
        for (FileErrorMsg fileErrorMsg: errorMsgs) {
            DsArErrDto rowsDto=new DsArErrDto();
            rowsDto.setSheetAt(fileErrorMsg.getSheetAt());
            rowsDto.setRow(fileErrorMsg.getRow()+"");
            rowsDto.setFieldName(fileErrorMsg.getCellName()+"("+fileErrorMsg.getColHeader()+")");
            rowsDto.setErrorMessage(fileErrorMsg.getMessage());
            errRowsDtos.add(rowsDto);
        }
        Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getSheetAt).thenComparing(FileErrorMsg::getRow));
        ParamUtil.setSessionAttr(request, "errRowsDtos", (Serializable) errRowsDtos);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.FILE_ITEM_ERROR_MSGS, errorMsgs);
        errorMap.put("itemError", "itemError");
        errorMap.put("uploadFileError68", "DS_ERR068");
        ParamUtil.setRequestAttr(request, "DS_ERR068", true);
        fileItemSize = 0;
        return fileItemSize;
    }

    /**
     * Convert patient IdNo a in excelDto to the correct format for data storage
     * @param idType
     * @return
     */
    @Override
    public String convertIdType(String idType) {
        String result = "";
        if ("NRIC".equals(idType)){
            result = DataSubmissionConsts.DTV_ID_TYPE_NRIC;
        } else if ("FIN".equals(idType)){
            result = DataSubmissionConsts.DTV_ID_TYPE_FIN;
        } else if("Passport".equals(idType)){
            result = DataSubmissionConsts.DTV_ID_TYPE_PASSPORT;
        }
        return result;
    }

    @Override
    public CycleDto setCycleDtoPatientCodeAndCycleType(PatientInfoDto patientInfoDto, CycleDto cycleDto, String cycleType) {
        if (patientInfoDto == null || cycleDto == null){
            return cycleDto;
        }
        cycleDto.setCycleType(cycleType);
        PatientDto patient = patientInfoDto.getPatient();
        if (patient != null){
            String patientCode = patient.getPatientCode();
            if (patientCode != null){
                cycleDto.setPatientCode(patientCode);
            }
        }
        return cycleDto;
    }

    @Override
    public DataSubmissionDto setCommonDataSubmissionDtoField(HttpServletRequest request, String declaration,
                                                             ArSuperDataSubmissionDto newDto, String cycleType, boolean isPatient) {
        if (request == null){
            return new DataSubmissionDto();
        }
        newDto.setFe(Boolean.TRUE);
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
        if (isPatient){
            dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
            newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
        } else {
            dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
            newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        }
        newDto.setDataSubmissionDto(dataSubmissionDto);
        if (Boolean.FALSE.equals(isPatient)){
            CycleDto cycleDto = newDto.getCycleDto();
            String patientIdType = (String) request.getSession().getAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_TYPE);
            String patientIdNumber = (String) request.getSession().getAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_NUMBER);
            if (StringUtil.isNotEmpty(patientIdType) && StringUtil.isNotEmpty(patientIdNumber)){
                PatientInfoDto patientInfoDto = setPatientInfo(patientIdType, patientIdNumber, request);
                newDto.setPatientInfoDto(patientInfoDto);
                cycleDto = setCycleDtoPatientCodeAndCycleType(patientInfoDto,cycleDto,cycleType);
                newDto.setCycleDto(cycleDto);
            }
        }
        return dataSubmissionDto;
    }

    @Override
    public void validateParseDate(List<FileErrorMsg> errorMsgs, String date, Map<String, ExcelPropertyDto> fieldCellMap, int i, String filed,Boolean isPatient) {
        if (StringUtil.isEmpty(date) && !isPatient){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateTransfer"), "GENERAL_ERR0006"));
        } else {
            try {
                Formatter.parseDate(date);
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), "GENERAL_ERR0033"));
            }
        }
    }

    @Override
    public Integer excelStrToIntNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed) {
        if(StringUtil.isNotEmpty(value)){
            try{
                float temp = Float.parseFloat(value);
                return (int)temp;
            }catch (NumberFormatException e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), MessageUtil.getMessageDesc("GENERAL_ERR0027")));
            }
        }
        return null;
    }

    @Override
    public String excelStrToStrNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed) {
        Integer res = excelStrToIntNum(errorMsgs,fieldCellMap,i,value,filed);
        if(res != null){
            return String.valueOf(res);
        }
        return null;
    }

    @Override
    public String getMstrKeyByValue(String value, String keyPrefix) {
        if(value == null){
            return null;
        }
        List<String> keys = MasterCodeUtil.getCodeKeyByCodeValue(value);
        for (String item : keys){
            if (item.startsWith(keyPrefix)){
                return item;
            }
        }
        return value;
    }

    @Override
    public void validOutcomeOfPregnancy(List<FileErrorMsg> errorMsgs, PregnancyOutcomeStageDto ocDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, Date cycleStartDate){
        // PregnancyOutcome use masterCode
        if(StringUtil.isNotEmpty(ocDto.getPregnancyOutcome())){
            String outCome = ocDto.getPregnancyOutcome();
            if("OUTOPRE001".equals(outCome)){
                validLiveBirth(errorMsgs,ocDto,fieldCellMap,i,cycleStartDate);
            }
            if("OUTOPRE002".equals(outCome) || "OUTOPRE003".equals(outCome)){
                validNoLiveBirth(errorMsgs,ocDto,fieldCellMap,i);
            }
            if("OUTOPRE004".equals(outCome)){
                if(StringUtil.isNotEmpty(ocDto.getOtherPregnancyOutcome())){
                    validFieldLength(ocDto.getOtherPregnancyOutcome().length(),100,errorMsgs,
                            "freetextOutcomeOfPregnancy","(74) Outcome of Pregnancy (Others)",fieldCellMap,i);
                }else {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freetextOutcomeOfPregnancy"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
                }
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("outcomeOfPregnancy"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validLiveBirth(List<FileErrorMsg> errorMsgs, PregnancyOutcomeStageDto ocDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, Date cycleStartDate){
        if(StringUtil.isNotEmpty(ocDto.getMaleLiveBirthNum())){
            validFieldLength(ocDto.getMaleLiveBirthNum().length(),2,errorMsgs,
                    "noLiveBirthMale","(6) No. Live Birth (Male)",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLiveBirthMale"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getFemaleLiveBirthNum())){
            validFieldLength(ocDto.getMaleLiveBirthNum().length(),2,errorMsgs,
                    "noLiveBirthFemale","(7) No. Live Birth (female)",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLiveBirthMale"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getStillBirthNum())){
            validFieldLength(ocDto.getStillBirthNum().length(),2,errorMsgs,
                    "noStillBirth","(8) No. Still Birth",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noStillBirth"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getSpontAbortNum())){
            validFieldLength(ocDto.getSpontAbortNum().length(),2,errorMsgs,
                    "noOfSpontaneousAbortion","(9) No. of Spontaneous Abortion",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfSpontaneousAbortion"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getIntraUterDeathNum())){
            validFieldLength(ocDto.getIntraUterDeathNum().length(),2,errorMsgs,
                    "noOfIntraUterineDeath","(10) No. of Intra-uterine Death",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfIntraUterineDeath"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if("Known".equals(ocDto.getDeliveryDateType())){
            if(StringUtil.isNotEmpty(ocDto.getDeliveryDate())){
                if(cycleStartDate.getTime() > ocDto.getDeliveryDate().getTime()){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field1","(13) Date of Delivery");
                    repMap.put("field2","Cycle Start Date");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR069",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateOfDelivery"), errMsg));
                }
                validDateNoFuture(ocDto.getDeliveryDate(),errorMsgs,"dateOfDelivery","(13) Date of Delivery",fieldCellMap,i);
            }else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateOfDelivery"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            }
        }
        if(StringUtil.isNotEmpty(ocDto.getBabyDetailsUnknown())){
            Integer maleBaby = ocDto.getMaleLiveBirthNum() == null ? 0 : Integer.parseInt(ocDto.getMaleLiveBirthNum());
            Integer femaleBaby = ocDto.getFemaleLiveBirthNum() == null ? 0 : Integer.parseInt(ocDto.getFemaleLiveBirthNum());
            Integer totalBaby =  maleBaby + femaleBaby;
            if(totalBaby > 0){
                for(int babyCount = 1; babyCount <= 4; babyCount ++){
                    validBaby(errorMsgs,ocDto,fieldCellMap,i,babyCount,totalBaby);
                }
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateOfDeliveryIsUnknown"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    private void validBaby(List<FileErrorMsg> errorMsgs, PregnancyOutcomeStageDto ocDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, int babyCount, int totalBaby){
        PregnancyOutcomeBabyDto babyDto = ocDto.getPregnancyOutcomeBabyDtos().get(babyCount - 1);
        List<PregnancyOutcomeBabyDefectDto> defectDtos = babyDto.getPregnancyOutcomeBabyDefectDtos();
        if(defectDtos.size() != 0 && "POSBDT008".equals(defectDtos.get(defectDtos.size() - 1).getDefectType())){
            validFieldLength(defectDtos.get(defectDtos.size() - 1).getOtherDefectType().length(),20,
                    errorMsgs,"baby" + babyCount + "FreetextDefectTypeOthers","(" + (babyCount * 12 + 17) + ") Baby " + babyCount + " Defect Type (Others)",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(ocDto.getNicuCareBabyNum()) && ocDto.getNicuCareBabyNum() > totalBaby){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateOfDelivery"), "Cannot be greater than No. of Live Births (Total)"));
        }
        if(StringUtil.isNotEmpty(ocDto.getL3CareBabyDays())){
            validFieldLength(ocDto.getL3CareBabyDays().length(),2,errorMsgs,"noDaysBabyInL2","(68) No. Days Baby Stay in L2",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(ocDto.getL3CareBabyDays())){
            validFieldLength(ocDto.getL3CareBabyDays().length(),2,errorMsgs,"noDaysBabyInL3","(70) No. Days Baby Stay in L3",fieldCellMap,i);
        }
    }
    private void validNoLiveBirth(List<FileErrorMsg> errorMsgs, PregnancyOutcomeStageDto ocDto, Map<String, ExcelPropertyDto> fieldCellMap, int i){
        if(StringUtil.isNotEmpty(ocDto.getStillBirthNum())){
            validFieldLength(ocDto.getStillBirthNum().length(),2,errorMsgs,
                    "noStillBirthNoLiveBirth","(71) No. of Still Birth",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noStillBirthNoLiveBirth"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getSpontAbortNum())){
            validFieldLength(ocDto.getSpontAbortNum().length(),2,errorMsgs,
                    "noOfSpontaneousAbortionNoLiveBirth","(72) No. of Spontaneous Abortion",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfSpontaneousAbortionNoLiveBirth"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(ocDto.getIntraUterDeathNum())){
            validFieldLength(ocDto.getIntraUterDeathNum().length(),2,errorMsgs,
                    "noOfIntraUterineDeathNoLiveBirth","(73) No. of Intra-uterine Death",fieldCellMap,i);
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noOfIntraUterineDeathNoLiveBirth"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
    }
    @Override
    public void validDateNoFuture(Date date, List<FileErrorMsg> errorMsgs, String fieldName, String excelFieldName, Map<String, ExcelPropertyDto> fieldCellMap, int i){
        long now = new Date().getTime();
        if(date.getTime() > now){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("field",excelFieldName);
            String errMsg = MessageUtil.getMessageDesc("DS_ERR001",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(fieldName), errMsg));
        }
    }
    @Override
    public void validFieldLength(int fieldLength, int lengthRequired, List<FileErrorMsg> errorMsgs, String fieldName, String excelFieldName, Map<String, ExcelPropertyDto> fieldCellMap, int i){
        if(fieldLength > lengthRequired){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number",String.valueOf(lengthRequired));
            repMap.put("fieldNo",excelFieldName);
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(fieldName), errMsg));
        }
    }
    @Override
    public boolean validateIsNull(List<FileErrorMsg> errorMsgs, Object value, Map<String, ExcelPropertyDto> fieldCellMap, int i, String filed) {
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (StringUtil.isEmpty(value)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), errMsgErr006));
            return false;
        }
        return true;
    }
    @Override
    public void saveRowId(HttpServletRequest request, int row, String idType, String idNo){
        Map<Integer, PatientIdDto> idMap = (Map<Integer, PatientIdDto>) request.getSession().getAttribute("idMap");
        if(idMap == null){
            idMap = IaisCommonUtils.genNewHashMap();
            idMap.put(row,new PatientIdDto(idType,idNo));
            request.getSession().setAttribute("idMap",idMap);
        }else {
            idMap.put(row,new PatientIdDto(idType,idNo));
            request.getSession().setAttribute("idMap",idMap);
        }
    }
    @Override
    public void validRowId(HttpServletRequest request, int row, String idType, String idNo, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap){
        Map<Integer,PatientIdDto> idMap = (Map<Integer,PatientIdDto>) request.getSession().getAttribute("idMap");
        Map<Integer,Boolean> rowIdRes = (Map<Integer,Boolean>) request.getSession().getAttribute("rowIdRes");
        if(StringUtil.isEmpty(rowIdRes)){
            rowIdRes = IaisCommonUtils.genNewHashMap();
        }
        if(!validPatientId(idType,idNo,fieldCellMap,errorMsgs,row,"patientIdType","patientIdNo",request)){
            rowIdRes.put(row,Boolean.FALSE);
            request.getSession().setAttribute("rowIdRes",rowIdRes);
            return;
        }
        if(StringUtil.isNotEmpty(idMap)){
            String currentRowIdType = idMap.get(row).getIdType();
            String currentRowIdNo = idMap.get(row).getIdNo();
            if(currentRowIdType != null && currentRowIdNo != null){
                if(currentRowIdType.equals(idType) && currentRowIdNo.equals(idNo)){
                    rowIdRes.put(row,Boolean.TRUE);
                    request.getSession().setAttribute("rowIdRes",rowIdRes);
                    return;
                }else {
                    errorMsgs.add(new FileErrorMsg(row, fieldCellMap.get("patientIdNo"), "patient id not match"));
                    rowIdRes.put(row,Boolean.FALSE);
                    request.getSession().setAttribute("rowIdRes",rowIdRes);
                    return;
                }
            }
        }
        errorMsgs.add(new FileErrorMsg(row, fieldCellMap.get("patientIdNo"), "id validation error"));
        rowIdRes.put(row,Boolean.FALSE);
        request.getSession().setAttribute("rowIdRes",rowIdRes);
    }
    public PatientIdDto getRowId(HttpServletRequest request, Integer row){
        Map<Integer,PatientIdDto> idMap = (Map<Integer,PatientIdDto>) request.getSession().getAttribute("idMap");
        if(idMap == null){
            return null;
        }
        return idMap.get(row);
    }
    @Override
    public void clearRowIdSession(HttpServletRequest request){
        request.getSession().removeAttribute("idMap");
        request.getSession().removeAttribute("rowIdRes");
    }

}
