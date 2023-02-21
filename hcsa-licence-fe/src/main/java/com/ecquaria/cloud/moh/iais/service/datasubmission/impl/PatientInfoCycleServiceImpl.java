package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
import java.util.List;
import java.util.Map;

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
        List<PatientInfoDto> patientInfoDtos = (List<PatientInfoDto>)request.getSession().getAttribute(DataSubmissionConsts.PATIENT_INFO__CYCLE_STAGE_LIST);
        if (patientInfoDtos == null){
            Map.Entry<String, File> fileEntry = uploadCommonService.getFileEntry(request);
            PageShowFileDto pageShowFileDto = uploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
            if (errorMap.isEmpty()) {
                List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList = uploadCommonService.getExcelDtoList(fileEntry,PatientInfoCycleExcelDto.class);
                fileItemSize = patientInfoCycleExcelDtoList.size();
                errorMap = doValidateUploadFile(errorMap, fileItemSize,patientInfoDtos,patientInfoCycleExcelDtoList,fileEntry,request);
            }
        }
        if (!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(request,"isPatientCycleFile",Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request,"isPatientCycleFile",Boolean.TRUE);
        }
        return errorMap;
    }

    @Override
    public void savePatientInfoCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto) {

    }

    private Map<String, String> doValidateUploadFile(Map<String, String> errorMap, int fileItemSize,
                                                     List<PatientInfoDto> patientInfoDtos,List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList,
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
            patientInfoDtos = getPatientInfoList(patientInfoCycleExcelDtoList,orgId);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(PatientInfoCycleExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(patientInfoDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize - 1; i++) {
                PatientInfoDto patientInfoDto = patientInfoDtos.get(i-1);
                validatePatientInfoCycleStageDto(errorMsgs, patientInfoDto, fieldCellMap, i,request);
            }
            if (!errorMsgs.isEmpty()) {
                fileItemSize = uploadCommonService.getErrorRowInfo(errorMap, request, errorMsgs);
            } else {
                if (patientInfoDtos != null) {
                    fileItemSize = patientInfoDtos.size();
                }
                request.getSession().setAttribute(DataSubmissionConsts.PATIENT_INFO__CYCLE_STAGE_LIST, patientInfoDtos);
            }
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
        return errorMap;
    }

    private void validatePatientInfoCycleStageDto(List<FileErrorMsg> errorMsgs, PatientInfoDto patientInfoDto, Map<String, ExcelPropertyDto> fieldCellMap, int i,HttpServletRequest request) {
        PatientDto patientDto = patientInfoDto.getPatient();
        if (patientDto == null){
            return;
        }
        uploadCommonService.validateIsNull(errorMsgs,patientDto.getName(),fieldCellMap,i,"name");
        uploadCommonService.validatePatientIdTypeAndNumber(patientDto.getIdType(), patientDto.getIdNumber(),
                fieldCellMap, errorMsgs,i,"idType","idNumber",request);
        uploadCommonService.validateParseDate(errorMsgs,patientDto.getBirthDate(),fieldCellMap,i,"birthDate");
        uploadCommonService.validateIsNull(errorMsgs,patientDto.getNationality(),fieldCellMap,i,"nationality");
        uploadCommonService.validateIsNull(errorMsgs,patientDto.getEthnicGroup(),fieldCellMap,i,"ethnicGroup");
        uploadCommonService.validateIsNull(errorMsgs,patientDto.getEthnicGroupOther(),fieldCellMap,i,"ethnicGroupOther");

        HusbandDto husbandDto = patientInfoDto.getHusband();
        if (husbandDto == null){
            return;
        }
        uploadCommonService.validateIsNull(errorMsgs,husbandDto.getName(),fieldCellMap,i,"nameHbd");
        uploadCommonService.validatePatientIdTypeAndNumber(husbandDto.getIdType(), husbandDto.getIdNumber(),
                fieldCellMap, errorMsgs,i,"idTypeHbd","idNumberHbd",request);
        uploadCommonService.validateParseDate(errorMsgs, husbandDto.getBirthDate(), fieldCellMap,i,"birthDateHbd");
        uploadCommonService.validateIsNull(errorMsgs, husbandDto.getNationality(), fieldCellMap,i,"nationalityHbd");
        uploadCommonService.validateIsNull(errorMsgs, husbandDto.getEthnicGroup(), fieldCellMap,i,"ethnicGroupHbd");
        uploadCommonService.validateIsNull(errorMsgs, husbandDto.getEthnicGroupOther(), fieldCellMap,i,"ethnicGroupOtherHbd");
    }

    private List<PatientInfoDto> getPatientInfoList(List<PatientInfoCycleExcelDto> patientInfoCycleExcelDtoList, String orgId) {
        if (patientInfoCycleExcelDtoList == null) {
            return null;
        }
        List<MasterCodeView> idTypes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE);
        List<MasterCodeView> nationalities = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<MasterCodeView> groups = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_ETHNIC_GROUP);
        List<PatientInfoDto> result = IaisCommonUtils.genNewArrayList(patientInfoCycleExcelDtoList.size());
        for (int i = 1; i <= patientInfoCycleExcelDtoList.size() - 1; i++) {
            PatientInfoCycleExcelDto patientInfoCycleExcelDto = patientInfoCycleExcelDtoList.get(i);
            PatientInfoDto dto = new PatientInfoDto();
            PatientDto patient = MiscUtil.transferEntityDto(patientInfoCycleExcelDto, PatientDto.class);
            patient.setBirthDate(IaisCommonUtils.handleDate(patient.getBirthDate()));
            patient.setIdType(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getIdType(), idTypes));
            patient.setNationality(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getNationality(), nationalities));
            patient.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getEthnicGroup(), groups));
            patient.setPreviousIdentification("YES".equals(patientInfoCycleExcelDto.getIsPreviousIdentification()));
            patient.setOrgId(orgId);
            DsRfcHelper.prepare(patient);
            dto.setPatient(patient);
            dto.setIsPreviousIdentification(patientInfoCycleExcelDto.getIsPreviousIdentification());
            if (Boolean.TRUE.equals(patient.getPreviousIdentification())) {
                String preIdNumber = patientInfoCycleExcelDto.getPreIdNumber();
                String preNationality = DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getPreNationality(), nationalities);
                PatientDto previous = new PatientDto();
                previous.setIdType(patientInfoCycleExcelDto.getPreName());
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
            husbandDto.setIdType(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getIdTypeHbd(), idTypes));
            husbandDto.setIdNumber(patientInfoCycleExcelDto.getIdNumberHbd());
            husbandDto.setNationality(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getNationalityHbd(), nationalities));
            husbandDto.setBirthDate(IaisCommonUtils.handleDate(patientInfoCycleExcelDto.getBirthDateHbd()));
            husbandDto.setEthnicGroup(DataSubmissionHelper.getCode(patientInfoCycleExcelDto.getEthnicGroupHbd(), groups));
            husbandDto.setEthnicGroupOther(patientInfoCycleExcelDto.getEthnicGroupOtherHbd());
            DsRfcHelper.prepare(husbandDto);
            dto.setHusband(husbandDto);
            result.add(dto);
        }
        return result;
    }
}
