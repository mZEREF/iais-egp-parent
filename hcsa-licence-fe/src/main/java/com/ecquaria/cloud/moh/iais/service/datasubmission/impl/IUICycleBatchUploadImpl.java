package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.action.datasubmission.OutcomePregnancyDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
import java.util.Collections;
import java.util.Comparator;
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
        List<DonorSampleDto> donorSampleDtos = null;
        if (errorMap.isEmpty()) {
            String fileName = fileEntry.getValue().getName();
            if (!fileName.equals("IUI Cycle File Upload.xlsx") && !fileName.equals("IUI Cycle File Upload.csv")) {
                errorMap.put("uploadFileError", "Please change the file name.");
            }
            List<IUICycleStageExcelDto> iuiCycleStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, IUICycleStageExcelDto.class);
            List<OutcomeOfIUICycleStageExcelDto> outcomeOfIUICycleStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,OutcomeOfIUICycleStageExcelDto.class);
            List<IUICoFundingStageExcelDto> iuiCoFundingStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,IUICoFundingStageExcelDto.class);
            List<OutcomeOfPregnancyExcelDto> outcomeOfPregnancyExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,OutcomeOfPregnancyExcelDto.class);

            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(NonPatinetDonorSampleExcelDto.class);
                List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(donorSampleDtos, "file", fieldCellMap);
                for (int i = 1; i <= fileItemSize; i++) {
                    DonorSampleDto dsDto = donorSampleDtos.get(i - 1);
                    //validDonorSample(errorMsgs, dsDto, fieldCellMap, i);
                }
                if (!errorMsgs.isEmpty()) {
                    Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getRow).thenComparing(FileErrorMsg::getCol));
                    List<DsDrpSiErrRowsDto> errRowsDtos = IaisCommonUtils.genNewArrayList();
                    for (FileErrorMsg fileErrorMsg : errorMsgs
                    ) {
                        DsDrpSiErrRowsDto rowsDto = new DsDrpSiErrRowsDto();
                        rowsDto.setRow(fileErrorMsg.getRow() + "");
                        rowsDto.setFieldName(fileErrorMsg.getCellName() + "(" + fileErrorMsg.getColHeader() + ")");
                        rowsDto.setErrorMessage(fileErrorMsg.getMessage());
                        errRowsDtos.add(rowsDto);
                    }
                    ParamUtil.setSessionAttr(request, "errRowsDtos", (Serializable) errRowsDtos);

                    errorMap.put("itemError", "itemError");
                    errorMap.put("uploadFileError68", "DS_ERR068");
                    ParamUtil.setRequestAttr(request, "DS_ERR068", true);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, "isDonorSampleFile", Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request, "isDonorSampleFile", Boolean.TRUE);
            request.getSession().setAttribute(DataSubmissionConsts.NON_PATIENT_DONORSAMPLE_LIST, donorSampleDtos);
        }
        return errorMap;
    }
    public List<IuiCycleStageDto> getIUICycleStageList(List<IUICycleStageExcelDto> iuiCycleStageExcelDtoList) {
        if (iuiCycleStageExcelDtoList == null) {
            return null;
        }
        boolean jumpFlag = true;
        List<IuiCycleStageDto> result = IaisCommonUtils.genNewArrayList(iuiCycleStageExcelDtoList.size());
        for (IUICycleStageExcelDto excelDto : iuiCycleStageExcelDtoList) {
            if(jumpFlag) {
                jumpFlag = false;
                continue;
            }
            IuiCycleStageDto dto = new IuiCycleStageDto();

        }
        return result;
    }

    public List<OutcomeStageDto> getOutcomeOfIUICycleStageList(List<OutcomeOfIUICycleStageExcelDto> outcomeStageDtoList) {
        if (outcomeStageDtoList == null) {
            return null;
        }
        boolean jumpFlag = true;
        List<OutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomeStageDtoList.size());
        for (OutcomeOfIUICycleStageExcelDto excelDto : outcomeStageDtoList) {
            if(jumpFlag) {
                jumpFlag = false;
                continue;
            }
            OutcomeStageDto dto = new OutcomeStageDto();

        }
        return result;
    }

    public List<IuiTreatmentSubsidiesDto> getIUICoFundingStageList(List<IUICoFundingStageExcelDto> iuiCoFundingStageExcelDtoList) {
        if (iuiCoFundingStageExcelDtoList == null) {
            return null;
        }
        boolean jumpFlag = true;
        List<IuiTreatmentSubsidiesDto> result = IaisCommonUtils.genNewArrayList(iuiCoFundingStageExcelDtoList.size());
        for (IUICoFundingStageExcelDto excelDto : iuiCoFundingStageExcelDtoList) {
            if(jumpFlag) {
                jumpFlag = false;
                continue;
            }
            IuiTreatmentSubsidiesDto dto = new IuiTreatmentSubsidiesDto();

        }
        return result;
    }

    public List<PregnancyOutcomeStageDto> getPregnancyOutcomeList(List<OutcomeOfPregnancyExcelDto> outcomeOfPregnancyExcelDtoList) {
        if (outcomeOfPregnancyExcelDtoList == null) {
            return null;
        }
        boolean jumpFlag = true;
        List<PregnancyOutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomeOfPregnancyExcelDtoList.size());
        for (OutcomeOfPregnancyExcelDto excelDto : outcomeOfPregnancyExcelDtoList) {
            if(jumpFlag) {
                jumpFlag = false;
                continue;
            }
            PregnancyOutcomeStageDto dto = new PregnancyOutcomeStageDto();

        }
        return result;
    }
}
