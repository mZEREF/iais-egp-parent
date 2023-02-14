package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpSiErrRowsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.SfoExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.SfoCycleUploadService;
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
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * SfoCycleUploadServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/8 14:40
 **/
@Slf4j
@Service
public class SfoCycleUploadServiceImpl implements SfoCycleUploadService {

    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String FILE_ITEM_SIZE = "fileItemSize";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private ArBatchUploadCommonService uploadCommonService;

    @Override
    public Map<String, String> getSfoCycleUploadFile(HttpServletRequest request, Map<String, String> errorMap,
                                                     int fileItemSize) {
        List<EfoCycleStageDto> sfoCycleStageDtos = (List<EfoCycleStageDto>)request.getSession().getAttribute(DataSubmissionConsts.SFO_CYCLE_STAGE_LIST);
        if (sfoCycleStageDtos == null){
            Map.Entry<String, File> fileEntry = uploadCommonService.getFileEntry(request);
            PageShowFileDto pageShowFileDto = uploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
            if (errorMap.isEmpty()) {
                List<SfoExcelDto> sfoExcelDtoList = uploadCommonService.getExcelDtoList(fileEntry,SfoExcelDto.class);
                fileItemSize = sfoExcelDtoList.size();
                errorMap = doValidateUploadFile(errorMap, fileItemSize,sfoCycleStageDtos,sfoExcelDtoList,fileEntry,request);
            }
        }
        if (!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(request,"isSfoCycleFile",Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request,"isSfoCycleFile",Boolean.TRUE);
        }
        return errorMap;
    }

    @Override
    public void saveSfoCycleUploadFile(HttpServletRequest request,ArSuperDataSubmissionDto arSuperDto) {
        log.info(StringUtil.changeForLog("----- sfo cycle upload file is saving -----"));
        List<EfoCycleStageDto> sfoCycleDtoList = (List<EfoCycleStageDto>) request.getSession().getAttribute(DataSubmissionConsts.SFO_CYCLE_STAGE_LIST);
        if (sfoCycleDtoList == null || sfoCycleDtoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = sfoCycleDtoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(sfoCycleDtoList.spliterator(), useParallel)
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

    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, String declaration, EfoCycleStageDto dto) {
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
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_CYCLE_SFO);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setEfoCycleStageDto(dto);
        return newDto;
    }

    private Map<String, String> doValidateUploadFile(Map<String, String> errorMap, int fileItemSize,
                                      List<EfoCycleStageDto> sfoCycleStageDtos,List<SfoExcelDto> sfoExcelDtoList,
                                                     Map.Entry<String, File> fileEntry, HttpServletRequest request) {
        String fileName=fileEntry.getValue().getName();
        if(!fileName.equals("SFO File Upload.xlsx")&&!fileName.equals("SFO File Upload.csv")){
            errorMap.put("uploadFileError", "Please change the file name.");
        }
        if (fileItemSize == 0){
            errorMap.put("uploadFileError", "PRF_ERR006");
        } else if (fileItemSize > 10000){
            errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                    Formatter.formatNumber(10000, "#,##0"), "maxCount"));
        } else {
            sfoCycleStageDtos = getEfoCycleStageDtoList(sfoExcelDtoList);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(SfoExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(sfoCycleStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                EfoCycleStageDto efoCycleStageDto = sfoCycleStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(sfoExcelDtoList, fieldCellMap, errorMsgs, i);
                validateEfoCycleStageDto(errorMsgs, efoCycleStageDto, fieldCellMap, i);
            }
            if (!errorMsgs.isEmpty()) {
                List<DsDrpSiErrRowsDto> errRowsDtos = IaisCommonUtils.genNewArrayList();
                for (FileErrorMsg fileErrorMsg:errorMsgs) {
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
            } else {
                if (sfoCycleStageDtos != null) {
                    fileItemSize = sfoCycleStageDtos.size();
                }
                request.getSession().setAttribute(DataSubmissionConsts.SFO_CYCLE_STAGE_LIST, sfoCycleStageDtos);
            }
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
        return errorMap;
    }

    private void validatePatientIdTypeAndNumber(List<SfoExcelDto> sfoExcelDtoList, Map<String, ExcelPropertyDto> fieldCellMap,
                                                List<FileErrorMsg> errorMsgs, int i) {
        String patientId = sfoExcelDtoList.get(i-1).getIdType();
        String patientNumber = sfoExcelDtoList.get(i-1).getIdNumber();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,"idType","idNumber");
    }

    /**
     * Transfer to patient info dto from sfo cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param sfoExcelDtoList
     * @return
     */
    private List<EfoCycleStageDto> getEfoCycleStageDtoList(List<SfoExcelDto> sfoExcelDtoList) {
        if (sfoExcelDtoList == null){
            return null;
        }
        List<EfoCycleStageDto> result = IaisCommonUtils.genNewArrayList(sfoExcelDtoList.size());
        for (SfoExcelDto sfoExcelDto : sfoExcelDtoList) {
            EfoCycleStageDto sfoCycleStageDto = new EfoCycleStageDto();
            try {
                sfoCycleStageDto.setStartDate(Formatter.parseDate(sfoExcelDto.getDateFreezing()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sfoCycleStageDto.setIsMedicallyIndicated("Yes".equals(sfoExcelDto.getIsMedicallyIndicated()) ? 1 : 0);
            if (StringUtil.isNotEmpty(sfoExcelDto.getReason())){
                if ("Undergoing chemotherapy and/or radiotherapy".equals(sfoExcelDto.getReason())){
                    sfoCycleStageDto.setReason(DataSubmissionConsts.EFO_REASON_UNDERGOING);
                }else if ("others".equals(sfoExcelDto.getReason())){
                    sfoCycleStageDto.setReason(DataSubmissionConsts.EFO_REASON_OTHERS);
                }
            }
            sfoCycleStageDto.setOtherReason(sfoExcelDto.getOthersReason());
            sfoCycleStageDto.setCryopresNum(sfoExcelDto.getCryopreserved() == null ? 0 : Double.valueOf(sfoExcelDto.getCryopreserved()).intValue());
            result.add(sfoCycleStageDto);
        }
        return result;
    }

    private void validateEfoCycleStageDto(List<FileErrorMsg> errorMsgs, EfoCycleStageDto efoCycleStageDto,
                                         Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        if (efoCycleStageDto == null){
            return;
        }
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (StringUtil.isEmpty(efoCycleStageDto.getStartDate())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("startDate"), errMsgErr006));
        } else {
            doValidateStartDate(errorMsgs, efoCycleStageDto.getStartDate(), fieldCellMap, i);
        }

        if (efoCycleStageDto.getCryopresNum() == null){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cryopreserved"), errMsgErr006));
        } else if (StringUtil.isDigit(efoCycleStageDto.getCryopresNum())){
            doValidateCryopresNum(errorMsgs, efoCycleStageDto.getCryopresNum(), fieldCellMap, i);
        }

        int isMedicallyIndicated = efoCycleStageDto.getIsMedicallyIndicated();
        String reason = efoCycleStageDto.getReason();
        String othersReason = efoCycleStageDto.getOtherReason();
        if (StringUtil.isEmpty(isMedicallyIndicated)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isMedicallyIndicated"), errMsgErr006));
        } else {
            doValidateMedicallyIndicated(errorMsgs, fieldCellMap, i, isMedicallyIndicated, reason, othersReason);
        }
        if (StringUtil.isEmpty(reason)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("reason"), errMsgErr006));
        }
    }

    /**
     * validate MedicallyIndicated
     * @param errorMsgs
     * @param fieldCellMap
     * @param i
     * @param isMedicallyIndicated
     * @param reason
     * @param othersReason
     */
    private void doValidateMedicallyIndicated(List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap,
                                              int i, int isMedicallyIndicated, String reason, String othersReason) {
        if(isMedicallyIndicated == 1){
            if (!StringUtil.isEmpty(reason) && "others".toLowerCase(Locale.ROOT).equals(reason)) {
                if (StringUtil.isEmpty(othersReason)) {
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Reason", "field");
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("othersReason"), errMsg));
                }else if(othersReason.length() > 100){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","100");
                    repMap.put("fieldNo","Others Reason");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("othersReason"), errMsg));
                }
            }
        }

        if(isMedicallyIndicated == 0){
            if(!StringUtil.isEmpty(reason) && reason.length() > 100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Reason");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("reason"), errMsg));
            }
        }
    }

    /**
     * validate isDigit
     * @param errorMsgs
     * @param cryopresNum
     * @param fieldCellMap
     * @param i
     */
    private void doValidateCryopresNum(List<FileErrorMsg> errorMsgs, int cryopresNum,
                                       Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isDigit(cryopresNum)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cryopreserved"), errMsgErr002));
        } else if (cryopresNum < 1 || cryopresNum > 99){
            repMap.put("minNum","1");
            repMap.put("maxNum","99");
            repMap.put("field","No.Cryopreserved");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cryopreserved"), errMsg));
        }
    }

    /**
     *  validate date whether after future date
     * @param errorMsgs
     * @param startDate
     * @param fieldCellMap
     * @param i
     */
    private void doValidateStartDate(List<FileErrorMsg> errorMsgs, Date startDate,
                                     Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
        Date today = new Date();
        if(startDate.after(today)) {
            repMap.put("filed","Date Started");
            String errMsgErr001 = MessageUtil.getMessageDesc("DS_ERR001",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("startDate"), errMsgErr001));
        }
    }
}
