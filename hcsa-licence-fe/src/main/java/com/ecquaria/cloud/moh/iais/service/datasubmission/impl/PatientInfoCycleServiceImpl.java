package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.PatientInfoCycleExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientInfoCycleUploadService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * PatientInfoCycleServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/15 14:45
 **/
@Slf4j
@Service
public class PatientInfoCycleServiceImpl implements PatientInfoCycleUploadService {

    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String FILE_ITEM_SIZE = "fileItemSize";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private ArBatchUploadCommonService uploadCommonService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private PatientService patientService;

    @Override
    public Map<String, String> getPatientInfoCycleUploadFile(HttpServletRequest request, Map<String, String> errorMap, int fileItemSize) {
        List<PatientInfoDto> patientInfoDtos = (List<PatientInfoDto>)request.getSession().getAttribute(DataSubmissionConsts.PATIENT_INFO_LIST);
        if (patientInfoDtos == null){
            Map.Entry<String, File> fileEntry = uploadCommonService.getFileEntry(request);
            PageShowFileDto pageShowFileDto = uploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
            if (errorMap.isEmpty()) {
                List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList = uploadCommonService.getExcelDtoList(fileEntry,PatientInfoCycleExcelDto.class);
                fileItemSize = patientInfoCycleExcelDtoList.size();
                errorMap = doValidateUploadFile(errorMap, fileItemSize,patientInfoCycleExcelDtoList,fileEntry,request);
            }
        }

        return errorMap;
    }

    @Override
    public void savePatientInfoCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto) {
        List<PatientInfoDto> patientInfoList = (List<PatientInfoDto>) request.getSession().getAttribute(DataSubmissionConsts.PATIENT_INFO_LIST);
        if (patientInfoList == null || patientInfoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = patientInfoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(patientInfoList.spliterator(), useParallel)
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

    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto,
                                                                 String declaration, PatientInfoDto dto) {
        ArSuperDataSubmissionDto newDto = DataSubmissionHelper.reNew(arSuperDto);
        DataSubmissionDto dataSubmissionDto = uploadCommonService.setCommonDataSubmissionDtoField(request, declaration, newDto,
                null,Boolean.TRUE);
        PatientDto patient = dto.getPatient();
        String patientCode = patient.getPatientCode();
        if (Boolean.TRUE.equals(patient.getPreviousIdentification()) && dto.getPrevious() != null) {
            patientCode = dto.getPrevious().getPatientCode();
        }
        patient.setPatientCode(patientService.getPatientCode(patientCode));
        patient.setPatientType(DataSubmissionConsts.DS_PATIENT_ART);
        dto.setPatient(patient);
        CycleDto cycleDto = newDto.getCycleDto();
        // judge Ar or Iui
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_PATIENT_ART);
        cycleDto.setPatientCode(patient.getPatientCode());
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT);
        newDto.setCycleDto(cycleDto);
        newDto.setPatientInfoDto(dto);
        return newDto;
    }

    private Map<String, String> doValidateUploadFile(Map<String, String> errorMap, int fileItemSize, List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList,
                                                     Map.Entry<String, File> fileEntry, HttpServletRequest request) {
        String fileName=fileEntry.getValue().getName();
        if(!fileName.equals("Patient Information File Upload.xlsx")&&!fileName.equals("Patient Information File Upload.csv")){
            errorMap.put("uploadFileError", "Please change the file name.");
        }
        if (fileItemSize == 0){
            errorMap.put("uploadFileError", "PRF_ERR006");
        } else if (fileItemSize > 10000){
            errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                    Formatter.formatNumber(10000, "#,##0"), "maxCount"));
        } else {
            String orgId = DataSubmissionHelper.getLoginContext(request).getOrgId();
            List<PatientInfoDto> patientInfoDtos = getPatientInfoList(patientInfoCycleExcelDtoList,orgId,errorMap);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(PatientInfoCycleExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(patientInfoDtos, "file", fieldCellMap);
            List<PatientDto> patientDtos = patientInfoDtos.stream()
                    .map(PatientInfoDto::getPatient)
                    .collect(Collectors.toList());
            for (int i = 1; i < fileItemSize - 1; i++) {
                if (duplicate(patientDtos.get(i), patientDtos.subList(0, i))) {
                    errorMsgs.add(new FileErrorMsg(DataSubmissionHelper.getRow(i), fieldCellMap.get("idNumber"),
                            "GENERAL_ERR0053"));
                }
            }
            if (!errorMsgs.isEmpty()) {
                fileItemSize = uploadCommonService.getErrorRowInfo(errorMap, request, errorMsgs);
            } else {
                if (patientInfoDtos != null) {
                    fileItemSize = patientInfoDtos.size();
                }
                request.getSession().setAttribute(DataSubmissionConsts.PATIENT_INFO_LIST, patientInfoDtos);
            }
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
        return errorMap;
    }

    private List<PatientInfoDto> getPatientInfoList(List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList, String orgId,Map<String,String> errorMap) {
        if (patientInfoCycleExcelDtoList == null) {
            return null;
        }
        List<MasterCodeView> nationalities = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<MasterCodeView> groups = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_ETHNIC_GROUP);
        List<PatientInfoDto> result = IaisCommonUtils.genNewArrayList(patientInfoCycleExcelDtoList.size());
        for (int i = 1; i <= patientInfoCycleExcelDtoList.size() - 1; i++) {
            PatientInfoCycleExcelDto patientInfoCycleExcelDto = patientInfoCycleExcelDtoList.get(i);
            PatientInfoDto dto = new PatientInfoDto();
            PatientDto patient = new PatientDto();
            patient.setName(patientInfoCycleExcelDto.getName());
            patient.setBirthDate(patientInfoCycleExcelDto.getBirthDate());
            patient.setIdType(uploadCommonService.convertIdType(patientInfoCycleExcelDto.getIdType()));
            patient.setIdNumber(patientInfoCycleExcelDto.getIdNumber());
            patient.setNationality(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getNationality(), nationalities));
            patient.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getEthnicGroup(), groups));
            patient.setEthnicGroupOther(patientInfoCycleExcelDto.getEthnicGroupOther());
            patient.setPreviousIdentification("YES".equals(patientInfoCycleExcelDto.getIsPreviousIdentification()));
            patient.setOrgId(orgId);
            DsRfcHelper.prepare(patient);
            dto.setPatient(patient);
            dto.setIsPreviousIdentification(patientInfoCycleExcelDto.getIsPreviousIdentification());
            //
            validateIsSameAdd(errorMap,orgId,patient);
            if (Boolean.TRUE.equals(patient.getPreviousIdentification())) {
                String preIdNumber = patientInfoCycleExcelDto.getPreIdNumber();
                String preNationality = DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getPreNationality(), nationalities);
                PatientDto previous = new PatientDto();
                previous.setIdType(uploadCommonService.convertIdType(patientInfoCycleExcelDto.getIdType()));
                previous.setIdNumber(preIdNumber);
                previous.setNationality(preNationality);
                PatientDto db = patientService.getActiveArPatientByConds(patientInfoCycleExcelDto.getPreName(), preIdNumber, preNationality, orgId);
                if (db != null) {
                    previous = db;
                }
                dto.setPrevious(previous);
            } else {
                dto.setPrevious(null);
            }
            HusbandDto husbandDto = new HusbandDto();
            husbandDto.setName(patientInfoCycleExcelDto.getNameHbd());
            husbandDto.setIdType(uploadCommonService.convertIdType(patientInfoCycleExcelDto.getIdTypeHbd()));
            husbandDto.setIdNumber(patientInfoCycleExcelDto.getIdNumberHbd());
            husbandDto.setNationality(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getNationalityHbd(), nationalities));
            husbandDto.setBirthDate(patientInfoCycleExcelDto.getBirthDateHbd());
            husbandDto.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getEthnicGroupHbd(), groups));
            husbandDto.setEthnicGroupOther(patientInfoCycleExcelDto.getEthnicGroupOtherHbd());
            DsRfcHelper.prepare(husbandDto);
            dto.setHusband(husbandDto);
            result.add(dto);
        }
        return result;
    }

    //
    private void validateIsSameAdd(Map<String,String> errorMap,String orgId,PatientDto patient){
        if (patient == null){
            return;
        }
        String idType = patient.getIdType();
        String idNumber = patient.getIdNumber();
        String date = patient.getBirthDate();
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(date)){
            return;
        }
        Date birth = new Date();
        try {
            birth = Formatter.parseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String birthDate = Formatter.formatDateTime(birth, AppConsts.DEFAULT_DATE_BIRTHDATE_FORMAT);
        PatientInfoDto oldPatientInfoDto;
        if (DataSubmissionConsts.DTV_ID_TYPE_PASSPORT.equals(idType)){
            oldPatientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(idType,idNumber, birthDate, orgId);
        } else {
            oldPatientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNumber, orgId);
        }
        if (oldPatientInfoDto != null){
            errorMap.put("uploadFileError","The patient in the upload file already exists.");
        }
    }

    /**
     * Check duplication for all records in file
     *
     * @param patient
     * @param patientDtos
     * @return
     */
    private boolean duplicate(PatientDto patient, List<PatientDto> patientDtos) {
        if (StringUtil.isEmpty(patient.getIdType()) || StringUtil.isEmpty(patient.getIdNumber()) || StringUtil.isEmpty(
                patient.getNationality())) {
            return false;
        }
        return patientDtos.stream().anyMatch(dto -> Objects.equals(patient.getIdNumber(), dto.getIdNumber())
                && Objects.equals(patient.getIdType(), dto.getIdType())
                && Objects.equals(patient.getNationality(), dto.getNationality()));
    }
}
