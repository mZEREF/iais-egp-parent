package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ArBatchUploadCommonService
 *
 * @Author dongchi
 * @Date 2023/2/14 15:04
 **/
public interface ArBatchUploadCommonService {
    PatientInfoDto setPatientInfo(String idType, String idNo, HttpServletRequest request);

    Map.Entry<String, File> getFileEntry(HttpServletRequest request);

    PageShowFileDto getPageShowFileDto(Map.Entry<String, File> fileEntry);

    <T> List<T> getExcelDtoList(Map.Entry<String, File> fileEntry, Class<T> tClass);

    void validatePatientIdTypeAndNumber(String patientIdType, String patientIdNumber,
                                        Map<String, ExcelPropertyDto> fieldCellMap, List<FileErrorMsg> errorMsgs,
                                        int i, String filedType,String filedNumber,HttpServletRequest request);

    boolean getBooleanValue(Object obj);

    int getErrorRowInfo(Map<String, String> errorMap, HttpServletRequest request, List<FileErrorMsg> errorMsgs);

    String convertIdType(String idType);

    CycleDto setCycleDtoPatientCodeAndCycleType(PatientInfoDto patientInfoDto, CycleDto cycleDto, String cycleType);

    /**
     *  set common dataSubmissionDto field
     * @param request
     * @param declaration
     * @param newDto
     * @return
     */
    DataSubmissionDto setCommonDataSubmissionDtoField(HttpServletRequest request, String declaration, ArSuperDataSubmissionDto newDto, String cycleType);

    void validateParseDate(List<FileErrorMsg> errorMsgs, String date, Map<String, ExcelPropertyDto> fieldCellMap, int i, String filed);

    Integer excelStrToIntNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed);
    String excelStrToStrNum(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, int i, String value, String filed);
    String getMstrKeyByValue(String value,String keyPrefix);
    void validOutcomeOfPregnancy(List<FileErrorMsg> errorMsgs, PregnancyOutcomeStageDto ocDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, Date cycleStartDate);
    void validDateNoFuture(Date date, List<FileErrorMsg> errorMsgs, String fieldName, String excelFieldName, Map<String, ExcelPropertyDto> fieldCellMap, int i);
    void validFieldLength(int fieldLength, int lengthRequired, List<FileErrorMsg> errorMsgs, String fieldName, String excelFieldName, Map<String, ExcelPropertyDto> fieldCellMap, int i);
}
