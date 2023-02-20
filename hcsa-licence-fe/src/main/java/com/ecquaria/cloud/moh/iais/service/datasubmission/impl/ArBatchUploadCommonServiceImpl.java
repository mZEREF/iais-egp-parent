package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpSiErrRowsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
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
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNo, orgId);

        }else {
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNo, orgId);
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
    public void validatePatientIdTypeAndNumber(String patientIdType, String patientIdNumber,
                                               Map<String, ExcelPropertyDto> fieldCellMap, List<FileErrorMsg> errorMsgs, int i,
                                               String filedType,String filedNumber,HttpServletRequest request) {
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        Boolean idTypeFlag = Boolean.TRUE;
        Boolean idNumberFlag = Boolean.TRUE;
        if (StringUtil.isEmpty(patientIdType)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedType), errMsgErr006));
            idTypeFlag = Boolean.FALSE;
        }

        int maxLength = 9;
        if (StringUtil.isNotEmpty(patientIdType) && "Passport".equals(patientIdType)) {
            maxLength = 20;
        }
        if (StringUtil.isEmpty(patientIdNumber)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), errMsgErr006));
            idNumberFlag = Boolean.FALSE;
        } else if (patientIdNumber.length() > maxLength) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field", "The field");
            params.put("maxlength", String.valueOf(maxLength));
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041",params);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), errMsg));
            idNumberFlag = Boolean.FALSE;
        } else if ("NRIC".equals(patientIdType)){
            boolean b = SgNoValidator.validateFin(patientIdNumber);
            boolean b1 = SgNoValidator.validateNric(patientIdNumber);
            if (!(b || b1)) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filedNumber), "Please key in a valid NRIC/FIN"));
                idNumberFlag = Boolean.FALSE;
            }
        }
        if (idTypeFlag && idNumberFlag){
            request.getSession().setAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_TYPE, patientIdType);
            request.getSession().setAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_NUMBER, patientIdNumber);
        }
    }

    @Override
    public boolean getBooleanValue(Object obj) {
        return "Yes".equals(obj);
    }

    @Override
    public int getErrorRowInfo(Map<String, String> errorMap, HttpServletRequest request, List<FileErrorMsg> errorMsgs) {
        int fileItemSize;
        List<DsDrpSiErrRowsDto> errRowsDtos = IaisCommonUtils.genNewArrayList();
        for (FileErrorMsg fileErrorMsg: errorMsgs) {
            DsDrpSiErrRowsDto rowsDto=new DsDrpSiErrRowsDto();
            rowsDto.setRow(fileErrorMsg.getRow()+"");
            rowsDto.setFieldName(fileErrorMsg.getCellName()+"("+fileErrorMsg.getColHeader()+")");
            rowsDto.setErrorMessage(fileErrorMsg.getMessage());
            errRowsDtos.add(rowsDto);
        }
        Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getRow).thenComparing(FileErrorMsg::getCol));
        ParamUtil.setSessionAttr(request, "errRowsDtos", (Serializable) errRowsDtos);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.FILE_ITEM_ERROR_MSGS, errorMsgs);
        errorMap.put("itemError", "itemError");
        errorMap.put("uploadFileError68", "DS_ERR068");
        ParamUtil.setRequestAttr(request, "DS_ERR068", true);
        fileItemSize = 0;
        return fileItemSize;
    }

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
                                                             ArSuperDataSubmissionDto newDto, String cycleType) {
        if (request == null){
            return new DataSubmissionDto();
        }
        String patientIdType = (String) request.getSession().getAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_TYPE);
        String patientIdNumber = (String) request.getSession().getAttribute(DataSubmissionConsts.UPLOAD_PATIENT_ID_NUMBER);
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
        // apart from patientInfo cycle outside
        dataSubmissionDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        CycleDto cycleDto = newDto.getCycleDto();
        newDto.setDataSubmissionDto(dataSubmissionDto);
        if (StringUtil.isNotEmpty(patientIdType) && StringUtil.isNotEmpty(patientIdNumber)){
            PatientInfoDto patientInfoDto = setPatientInfo(convertIdType(patientIdType), patientIdNumber, request);
            newDto.setPatientInfoDto(patientInfoDto);
            cycleDto = setCycleDtoPatientCodeAndCycleType(patientInfoDto,cycleDto,cycleType);
            newDto.setCycleDto(cycleDto);
        }
        return dataSubmissionDto;
    }

    @Override
    public void validateParseDate(List<FileErrorMsg> errorMsgs, String date, Map<String, ExcelPropertyDto> fieldCellMap, int i, String filed) {
        if (StringUtil.isEmpty(fieldCellMap)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateTransfer"), "GENERAL_ERR0006"));
        } else {
            try {
                Formatter.parseDate(date);
            } catch (ParseException e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), "GENERAL_ERR0033"));
            }
        }
    }

    /**
     * get Integer number from excelDto's filed with validation
     * @param errorMsgs
     * @param fieldCellMap
     * @param i
     * @param value
     * @param filed
     * @return if input is null then return null
     */
    @Override
    public Integer excelStrToIntNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed) {
        if(StringUtil.isNotEmpty(value)){
            try{
                return Integer.parseInt(value);
            }catch (NumberFormatException e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), MessageUtil.getMessageDesc("GENERAL_ERR0027")));
            }
        }
        return null;
    }

    /**
     * get String number from excelDto's filed with validation
     * @param errorMsgs
     * @param fieldCellMap
     * @param i
     * @param value
     * @param filed
     * @return if input is null then return null
     */
    @Override
    public String excelStrToStrNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed) {
        Integer res = excelStrToIntNum(errorMsgs,fieldCellMap,i,value,filed);
        if(res != null){
            return String.valueOf(res);
        }
        return null;
    }

}
