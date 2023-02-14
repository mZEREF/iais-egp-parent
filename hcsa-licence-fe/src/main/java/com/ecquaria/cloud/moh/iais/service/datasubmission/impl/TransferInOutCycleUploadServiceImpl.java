package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpSiErrRowsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.SfoExcelDto;
import com.ecquaria.cloud.moh.iais.dto.TransferInOutExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TransferInOutCycleUploadService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TransferInOutCycleUploadServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/8 15:22
 **/
@Slf4j
@Service
public class TransferInOutCycleUploadServiceImpl implements TransferInOutCycleUploadService {

    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String FILE_ITEM_SIZE = "fileItemSize";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private ArBatchUploadCommonService uploadCommonService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Override
    public Map<String, String> getTransferInOutUploadFile(HttpServletRequest request, Map<String, String> errorMap, int fileItemSize) {
        List<TransferInOutStageDto> transferInOutStageDtos = (List<TransferInOutStageDto>)request.getSession()
                .getAttribute(DataSubmissionConsts.TRANSFER_IN_OUT_CYCLE_STAGE_LIST);
        if (transferInOutStageDtos == null){
            Map.Entry<String, File> fileEntry = uploadCommonService.getFileEntry(request);
            PageShowFileDto pageShowFileDto = uploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
            if (errorMap.isEmpty()) {
                List<TransferInOutExcelDto> transferInOutExcelDtoList = uploadCommonService.getExcelDtoList(fileEntry, TransferInOutExcelDto.class);
                fileItemSize = transferInOutExcelDtoList.size();
                errorMap = doValidateTransferInOutUploadFile(errorMap, fileItemSize,transferInOutStageDtos,
                        transferInOutExcelDtoList,fileEntry,request);
            }
        }
        if (!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(request,"isTransferInOutCycleFile",Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request,"isTransferInOutCycleFile",Boolean.TRUE);
        }
        return errorMap;
    }

    @Override
    public void saveTransferInOutCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto) {
        log.info(StringUtil.changeForLog("----- transfer in out cycle upload file is saving -----"));
        List<TransferInOutStageDto> transferInOutStageDtoList = (List<TransferInOutStageDto>) request.getSession()
                .getAttribute(DataSubmissionConsts.TRANSFER_IN_OUT_CYCLE_STAGE_LIST);
        if (transferInOutStageDtoList == null || transferInOutStageDtoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = transferInOutStageDtoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(transferInOutStageDtoList.spliterator(), useParallel)
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
                                                                 String declaration, TransferInOutStageDto dto) {
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
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setTransferInOutStageDto(dto);
        return newDto;
    }

    private Map<String, String> doValidateTransferInOutUploadFile(Map<String, String> errorMap, int fileItemSize,
                                                     List<TransferInOutStageDto> transferInOutStageDtos,List<TransferInOutExcelDto> transferInOutExcelDtoList,
                                                     Map.Entry<String, File> fileEntry, HttpServletRequest request) {
        String fileName=fileEntry.getValue().getName();
        if(!fileName.equals("Transfer In_Out File Upload.xlsx")&&!fileName.equals("Transfer In_Out File Upload.csv")){
            errorMap.put("uploadFileError", "Please change the file name.");
        }
        if (fileItemSize == 0){
            errorMap.put("uploadFileError", "PRF_ERR006");
        } else if (fileItemSize > 10000){
            errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                    Formatter.formatNumber(10000, "#,##0"), "maxCount"));
        } else {
            transferInOutStageDtos = getTransferInOutStageDtoList(transferInOutExcelDtoList);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(SfoExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(transferInOutStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                TransferInOutStageDto transferInOutStageDto = transferInOutStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(transferInOutExcelDtoList, fieldCellMap, errorMsgs, i);
                validateTransferInOutCycleStageDto(errorMsgs, transferInOutStageDto, fieldCellMap, i);
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
                if (transferInOutStageDtos != null) {
                    fileItemSize = transferInOutStageDtos.size();
                }
                request.getSession().setAttribute(DataSubmissionConsts.TRANSFER_IN_OUT_CYCLE_STAGE_LIST, transferInOutStageDtos);
            }
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
        return errorMap;
    }

    /**
     * Transfer to patient info dto from transfer in out cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param transferInOutExcelDtoList
     * @return
     */
    private List<TransferInOutStageDto> getTransferInOutStageDtoList(List<TransferInOutExcelDto> transferInOutExcelDtoList) {
        if (IaisCommonUtils.isEmpty(transferInOutExcelDtoList)){
            return null;
        }
        List<TransferInOutStageDto> result = IaisCommonUtils.genNewArrayList();
        transferInOutExcelDtoList.stream().forEach(dto ->{
            TransferInOutStageDto transferInOutStageDto = setTransferInOutStageDto(dto);
            result.add(transferInOutStageDto);
        });
        return result;
    }

    private TransferInOutStageDto setTransferInOutStageDto(TransferInOutExcelDto dto) {
        TransferInOutStageDto transferInOutStageDto = new TransferInOutStageDto();
        if (StringUtil.isNotEmpty(dto.getTransferType())){
            if ("Transfer In".equals(dto.getTransferType())){
                transferInOutStageDto.setTransferType("in");
            } else {
                transferInOutStageDto.setTransferType("out");
            }
        }
        List<String> transferredList = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isNotEmpty(dto.getIsOocyteTransfer())){
            transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES);
        }
        if (StringUtil.isNotEmpty(dto.getIsEmbryoTransfer())){
            transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS);
        }
        if (StringUtil.isNotEmpty(dto.getIsSpermTransfer())){
            transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM);
        }
        transferInOutStageDto.setTransferredList(transferredList);
        transferInOutStageDto.setOocyteNum(String.valueOf(dto.getOocyteNum()));
        transferInOutStageDto.setEmbryoNum(String.valueOf(dto.getEmbryoNum()));
        transferInOutStageDto.setSpermVialsNum(String.valueOf(dto.getSpermVialsNum()));
        transferInOutStageDto.setFromDonor(StringUtil.isNotEmpty(dto.getIsDonor()));
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_IN.equals(dto.getTransferType())){
            transferInOutStageDto.setTransInFromHciCode(dto.getTransferInOut());
        }
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(dto.getTransferType())){
            transferInOutStageDto.setTransOutToHciCode(dto.getTransferInOut());
        }
        transferInOutStageDto.setTransferDate(dto.getDateTransfer());
        return transferInOutStageDto;
    }

    private void validateTransferInOutCycleStageDto(List<FileErrorMsg> errorMsgs, TransferInOutStageDto transferInOutStageDto,
                                         Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        if (transferInOutStageDto == null){
            return;
        }
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        String transferType = transferInOutStageDto.getTransferType();
        if (StringUtil.isEmpty(transferType)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferType"), errMsgErr006));
        } else if (!DataSubmissionConsts.TRANSFER_TYPE_IN.equals(transferType)
                && !DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(transferType)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferType"), "This a transfer is not in or out."));
        }
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        if (IaisCommonUtils.isEmpty(transferredList)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteTransfer"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isEmbryoTransfer"), errMsgErr006));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isSpermTransfer"), errMsgErr006));
        } else{
            doValidateNum(errorMsgs, transferInOutStageDto, fieldCellMap, i, errMsgErr006, transferredList);
        }

        if (StringUtil.isNotEmpty(transferInOutStageDto.getOocyteNum())
                && !StringUtil.isNumber(transferInOutStageDto.getOocyteNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("oocyteNum"), errMsgErr002));
        }
        if (StringUtil.isNotEmpty(transferInOutStageDto.getEmbryoNum())
                && !StringUtil.isNumber(transferInOutStageDto.getEmbryoNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("embryoNum"), errMsgErr002));
        }
        if (StringUtil.isNotEmpty(transferInOutStageDto.getSpermVialsNum())
                && !StringUtil.isNumber(transferInOutStageDto.getSpermVialsNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("spermVialsNum"), errMsgErr002));
        }

        if (StringUtil.isNotEmpty(transferInOutStageDto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_IN.equals(transferInOutStageDto.getTransferType())
                && StringUtil.isEmpty(transferInOutStageDto.getTransInFromHciCode())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferInOut"), errMsgErr006));
        }

        if (StringUtil.isNotEmpty(transferInOutStageDto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(transferInOutStageDto.getTransferType())
                && StringUtil.isEmpty(transferInOutStageDto.getTransOutToHciCode())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferInOut"), errMsgErr006));
        }

        if (StringUtil.isEmpty(transferInOutStageDto.getTransferDate())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateTransfer"), errMsgErr006));
        } else {
            try {
                Formatter.parseDate(transferInOutStageDto.getTransferDate());
            } catch (ParseException e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("dateTransfer"), "GENERAL_ERR0033"));
            }
        }
    }

    private void doValidateNum(List<FileErrorMsg> errorMsgs, TransferInOutStageDto transferInOutStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, String errMsgErr006, List<String> transferredList) {
        if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)
                && StringUtil.isEmpty(transferInOutStageDto.getOocyteNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("oocyteNum"), errMsgErr006));
        } else if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)
                && StringUtil.isEmpty(transferInOutStageDto.getEmbryoNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("embryoNum"), errMsgErr006));
        } else if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)
                && StringUtil.isEmpty(transferInOutStageDto.getSpermVialsNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("spermVialsNum"), errMsgErr006));
        }
    }
    private void validatePatientIdTypeAndNumber(List<TransferInOutExcelDto> transferInOutExcelDtoList, Map<String, ExcelPropertyDto> fieldCellMap,
                                                List<FileErrorMsg> errorMsgs, int i) {
        String patientId = transferInOutExcelDtoList.get(i-1).getIdType();
        String patientNumber = transferInOutExcelDtoList.get(i-1).getIdNumber();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,"idType","idNumber");
    }
}
