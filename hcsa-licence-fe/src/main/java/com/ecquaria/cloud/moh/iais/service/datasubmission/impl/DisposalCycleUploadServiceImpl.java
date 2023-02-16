package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.DisposalCycleExcelDto;
import com.ecquaria.cloud.moh.iais.dto.DisposalExcelDto;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DisposalCycleUploadService;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * DisposalCycleUploadServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/8 15:25
 **/
@Slf4j
@Service
public class DisposalCycleUploadServiceImpl implements DisposalCycleUploadService {

    @Override
    public Map<String, String> getDisposalCycleUploadFile(HttpServletRequest request, Map<String, String> errorMap, int fileItemSize) {
        List<DisposalStageDto> disposalStageDtos = (List<DisposalStageDto>)request.getSession()
                .getAttribute(DataSubmissionConsts.DISPOSAL_CYCLE_STAGE_LIST);
        if (disposalStageDtos == null){
            Map.Entry<String, File> fileEntry = uploadCommonService.getFileEntry(request);
            PageShowFileDto pageShowFileDto = uploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);
            if (errorMap.isEmpty()) {
                List<DisposalCycleExcelDto> disposalExcelDtoList = uploadCommonService.getExcelDtoList(fileEntry, DisposalCycleExcelDto.class);
                fileItemSize = disposalExcelDtoList.size();
                errorMap = doValidateDisposalCycleStageDto(errorMap, fileItemSize,disposalStageDtos,
                        disposalExcelDtoList,fileEntry,request);
            }
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), DisposalExcelDto.class, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), DisposalExcelDto.class, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
    }

    @Override
    public void saveDisposalCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto) {
        log.info(StringUtil.changeForLog("----- transfer in out cycle upload file is saving -----"));
        List<DisposalStageDto> disposalStageDtoList = (List<DisposalStageDto>) request.getSession()
                .getAttribute(DataSubmissionConsts.DISPOSAL_CYCLE_STAGE_LIST);
        if (disposalStageDtoList == null || disposalStageDtoList.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
        boolean useParallel = disposalStageDtoList.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(disposalStageDtoList.spliterator(), useParallel)
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
                                                                 String declaration, DisposalStageDto dto) {
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
        newDto.setDisposalStageDto(dto);
        return newDto;
    }

    public List<DisposalStageDto> getDisposalStageDtoList(List<DisposalCycleExcelDto> disposalExcelDtoList) {
        if (IaisCommonUtils.isEmpty(disposalExcelDtoList)){
            return null;
        }
        List<DisposalStageDto> result = IaisCommonUtils.genNewArrayList();
        disposalExcelDtoList.stream().forEach(dto ->{
            DisposalStageDto disposalStageDto = new DisposalStageDto();
            disposalStageDto.setDisposedType(dto.getDisposedItem());
            disposalStageDto.setImmatureString(dto.getNoImmatureDisposed());
            disposalStageDto.setAbnormallyFertilisedString(dto.getNoAbnormallyFertilisedDisposed());
            disposalStageDto.setUnfertilisedString(dto.getNoUnfertilisedDisposed());
            disposalStageDto.setAtreticString(dto.getNoAtreticDisposed());
            disposalStageDto.setDamagedString(dto.getNoDamagedDisposed());
            disposalStageDto.setLysedOrDegeneratedString(dto.getNoLysedDegeneratedDisposed());
            disposalStageDto.setUnhealthyNumString(dto.getNoPoorQualityUnhealthyAbnormalDisposed());
            disposalStageDto.setOtherDiscardedNumString(dto.getDiscardedForOtherReasons());
            disposalStageDto.setOtherDiscardedReason(dto.getOtherReasonsForDiscarding());
        });
        return result;
    }

    private void setDisposalType(DisposalCycleExcelDto dto, DisposalStageDto disposalStageDto) {
        switch (dto.getDisposedItem()){
            case "Fresh Oocyte(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE);
                disposalStageDto.setDisposedTypeDisplay(1);
                break;
            case "Frozen Oocyte(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE);
                disposalStageDto.setDisposedTypeDisplay(1);
                break;
            case "Thawed Oocyte(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE);
                disposalStageDto.setDisposedTypeDisplay(1);
                break;
            case "Fresh Embryo(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO);
                disposalStageDto.setDisposedTypeDisplay(2);
                break;
            case "Frozen Embryo(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO);
                disposalStageDto.setDisposedTypeDisplay(2);
                break;
            case "Thawed Embryo(s)":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO);
                disposalStageDto.setDisposedTypeDisplay(2);
                break;
            case "Frozen Sperm":
                disposalStageDto.setDisposedType(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM);
                disposalStageDto.setDisposedTypeDisplay(3);
                break;
        }
    }


    private Map<String, String> doValidateDisposalCycleStageDto(Map<String, String> errorMap, int fileItemSize,
                                                                  List<DisposalStageDto> disposalStageDtos,List<DisposalCycleExcelDto> disposalExcelDtoList,
                                                                  Map.Entry<String, File> fileEntry, HttpServletRequest request) {
        String fileName=fileEntry.getValue().getName();
        if(!fileName.equals("Disposal File Upload.xlsx")&&!fileName.equals("Disposal File Upload.csv")){
            errorMap.put("uploadFileError", "Please change the file name.");
        }
        if (fileItemSize == 0){
            errorMap.put("uploadFileError", "PRF_ERR006");
        } else if (fileItemSize > 10000){
            errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                    Formatter.formatNumber(10000, "#,##0"), "maxCount"));
        } else {
            disposalStageDtos = getDisposalStageDtoList(disposalExcelDtoList);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(SfoExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(disposalStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                DisposalStageDto disposalStageDto = disposalStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(disposalExcelDtoList, fieldCellMap, errorMsgs, i,request);
                validateTDisposalCycleStageDto(errorMsgs, disposalStageDto, fieldCellMap, i);
            }
            if (!errorMsgs.isEmpty()) {
                fileItemSize = uploadCommonService.getErrorRowInfo(errorMap, request, errorMsgs);
            } else {
                if (disposalStageDtos != null) {
                    fileItemSize = disposalStageDtos.size();
                }
                request.getSession().setAttribute(DataSubmissionConsts.DISPOSAL_CYCLE_STAGE_LIST, disposalStageDtos);
            }
            ParamUtil.setRequestAttr(request, FILE_ITEM_SIZE, fileItemSize);
        }
        return errorMap;
    }



    private void validatePatientIdTypeAndNumber(List<DisposalCycleExcelDto> disposalExcelDtoList, Map<String, ExcelPropertyDto> fieldCellMap,
                                                List<FileErrorMsg> errorMsgs, int i,HttpServletRequest request) {
        String patientId = disposalExcelDtoList.get(i-1).getPatientIdType();
        String patientNumber = disposalExcelDtoList.get(i-1).getPatientIdNo();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,"patientIdType","patientIdNo",request);
    }

    private void validateTDisposalCycleStageDto(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto,
                                                    Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        if (disposalStageDto == null){
            return;
        }
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (StringUtil.isEmpty(disposalStageDto.getDisposedType())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("disposedItem"), errMsgErr006));
        }
        if (disposalStageDto.getDisposedTypeDisplay() != null){
            doValidateNum(errorMsgs, disposalStageDto, fieldCellMap, i, errMsgErr006);
        }
        if (StringUtil.isEmpty(disposalStageDto.getOtherDiscardedNumString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr006));
        } else {
            doValidateOtherNum(errorMsgs, disposalStageDto, fieldCellMap, i);
        }
    }

    private void doValidateOtherNum(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        if (!StringUtil.isDigit(disposalStageDto.getOtherDiscardedNumString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr002));
        } else {
            disposalStageDto.setOtherDiscardedNum(
                    StringUtil.isEmpty(disposalStageDto.getOtherDiscardedNumString()) ? 0 : Double.valueOf(disposalStageDto.getOtherDiscardedNumString()).intValue());
            doValidateMaxAndMinNum(errorMsgs,"discardedForOtherReasons", disposalStageDto.getImmature(), fieldCellMap, i);
            if(disposalStageDto.getOtherDiscardedNum()>0){
                if(StringUtil.isEmpty(disposalStageDto.getOtherDiscardedReason())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDiscarding"), errMsgErr006));
                }else if(disposalStageDto.getOtherDiscardedReason().length() > 20){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","20");
                    repMap.put("fieldNo","This field");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherReasonsForDiscarding"), errMsg));
                }
            }
        }
    }

    private void doValidateNum(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto,
                               Map<String, ExcelPropertyDto> fieldCellMap, int i, String errMsgErr006) {
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        if(disposalStageDto.getDisposedTypeDisplay() == 1){
            doValidateDisposedTypeOne(errorMsgs, disposalStageDto, fieldCellMap, i, errMsgErr006);
            if (!StringUtil.isDigit(disposalStageDto.getImmatureString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noImmatureDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setImmature(
                        StringUtil.isEmpty(disposalStageDto.getImmatureString()) ? 0 : Double.valueOf(disposalStageDto.getImmatureString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noImmatureDisposed",disposalStageDto.getImmature(),fieldCellMap,i);
            }

            if (!StringUtil.isDigit(disposalStageDto.getAbnormallyFertilisedString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAbnormallyFertilisedDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setAbnormallyFertilised(
                        StringUtil.isEmpty(disposalStageDto.getAbnormallyFertilisedString()) ? 0 : Double.valueOf(disposalStageDto.getAbnormallyFertilisedString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noAbnormallyFertilisedDisposed",disposalStageDto.getAbnormallyFertilised(),fieldCellMap,i);
            }

            if (!StringUtil.isDigit(disposalStageDto.getUnfertilisedString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUnfertilisedDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setUnfertilised(
                        StringUtil.isEmpty(disposalStageDto.getUnfertilisedString()) ? 0 : Double.valueOf(disposalStageDto.getUnfertilisedString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noUnfertilisedDisposed",disposalStageDto.getUnfertilised(),fieldCellMap,i);
            }

            if (!StringUtil.isDigit(disposalStageDto.getAtreticString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAtreticDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setAtretic(
                        StringUtil.isEmpty(disposalStageDto.getAtreticString()) ? 0 : Double.valueOf(disposalStageDto.getAtreticString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noAtreticDisposed",disposalStageDto.getAtretic(),fieldCellMap,i);
            }

            if (!StringUtil.isDigit(disposalStageDto.getDamagedString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDamagedDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setDamaged(
                        StringUtil.isEmpty(disposalStageDto.getDamagedString()) ? 0 : Double.valueOf(disposalStageDto.getDamagedString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noDamagedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            }

            if (!StringUtil.isDigit(disposalStageDto.getLysedOrDegeneratedString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLysedDegeneratedDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setLysedOrDegenerated(
                        StringUtil.isEmpty(disposalStageDto.getLysedOrDegeneratedString()) ? 0 : Double.valueOf(disposalStageDto.getLysedOrDegeneratedString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noLysedDegeneratedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            }
        }

        if (disposalStageDto.getDisposedTypeDisplay() == 2){
            if (StringUtil.isEmpty(disposalStageDto.getUnhealthyNumString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noPoorQualityUnhealthyAbnormalDisposed"), errMsgErr006));
            }
            if (!StringUtil.isDigit(disposalStageDto.getUnhealthyNumString())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noPoorQualityUnhealthyAbnormalDisposed"), errMsgErr002));
            } else {
                disposalStageDto.setUnhealthyNum(
                        StringUtil.isEmpty(disposalStageDto.getUnhealthyNumString()) ? 0 : Double.valueOf(disposalStageDto.getUnhealthyNumString()).intValue());
                doValidateMaxAndMinNum(errorMsgs,"noPoorQualityUnhealthyAbnormalDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            }
        }
    }

    private void doValidateDisposedTypeOne(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i, String errMsgErr006) {
        if (StringUtil.isEmpty(disposalStageDto.getImmatureString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noImmatureDisposed"), errMsgErr006));
        }
        if (StringUtil.isEmpty(disposalStageDto.getAbnormallyFertilisedString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAbnormallyFertilisedDisposed"), errMsgErr006));
        }
        if (StringUtil.isEmpty(disposalStageDto.getUnfertilisedString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUnfertilisedDisposed"), errMsgErr006));
        }
        if (StringUtil.isEmpty(disposalStageDto.getAtreticString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAtreticDisposed"), errMsgErr006));
        }
        if (StringUtil.isEmpty(disposalStageDto.getDamagedString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDamagedDisposed"), errMsgErr006));
        }
        if (StringUtil.isEmpty(disposalStageDto.getLysedOrDegeneratedString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLysedDegeneratedDisposed"), errMsgErr006));
        }
    }

    private void doValidateMaxAndMinNum(List<FileErrorMsg> errorMsgs,String filed,int value, Map<String, ExcelPropertyDto> fieldCellMap, int i){
        int maxSamplesNum = 100;
        Map<String, String> eMsg = IaisCommonUtils.genNewHashMap();
        eMsg.put("field","disposal");
        String errMsg023 = MessageUtil.getMessageDesc("DS_ERR002",eMsg);
        if(value > 99 || value < 0){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("minNum","0");
            repMap.put("maxNum","99");
            repMap.put("field","This field");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), errMsg));
        }
        if(value > maxSamplesNum){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), errMsg023));
        }
    }
}
