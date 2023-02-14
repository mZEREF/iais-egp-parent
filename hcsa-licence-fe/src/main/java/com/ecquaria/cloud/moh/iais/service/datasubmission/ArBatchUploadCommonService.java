package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
                                        int i, String filedType,String filedNumber);

    boolean getBooleanValue(Object obj);
}
