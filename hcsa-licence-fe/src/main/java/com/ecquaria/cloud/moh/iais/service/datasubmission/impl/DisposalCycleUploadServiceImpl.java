package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
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
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DisposalCycleUploadService;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
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

    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String FILE_ITEM_SIZE = "fileItemSize";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    @Autowired
    private ArBatchUploadCommonService uploadCommonService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private ArFeClient arFeClient;

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
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(disposalStageDtoList.spliterator(), useParallel)
                .map(dto -> {
                    return getArSuperDataSubmissionDto(request, arSuperDto, dto);
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
    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, DisposalStageDto dto) {
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        ArSuperDataSubmissionDto newDto = DataSubmissionHelper.reNew(arSuperDto);
        DataSubmissionDto dataSubmissionDto = uploadCommonService.setCommonDataSubmissionDtoField(request, declaration, newDto,DataSubmissionConsts.DS_CYCLE_STAGE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_STAGE_DISPOSAL);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setArCurrentInventoryDto(setArCurrentInventoryDto(newDto,dto));
        newDto.setArChangeInventoryDto(setArChangeInventoryDto(newDto,dto));
        newDto.setDisposalStageDto(dto);
        return newDto;
    }
    private ArCurrentInventoryDto setArCurrentInventoryDto(ArSuperDataSubmissionDto newDto, DisposalStageDto dto){
        ArCurrentInventoryDto arCurrentInventoryDto = newDto.getArCurrentInventoryDto();
        if (arCurrentInventoryDto == null){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        arCurrentInventoryDto.setHciCode(newDto.getHciCode());
        arCurrentInventoryDto.setSvcName(newDto.getSvcName());
        arCurrentInventoryDto.setLicenseeId(newDto.getLicenseeId());
        if (newDto.getPatientInfoDto() != null && newDto.getPatientInfoDto().getPatient() != null
                && newDto.getPatientInfoDto().getPatient().getPatientCode() != null){
            arCurrentInventoryDto.setPatientCode(newDto.getPatientInfoDto().getPatient().getPatientCode());
        }
        ArCurrentInventoryDto oldArCurrentInventoryDto = getOldArCurrentInventoryDto(arCurrentInventoryDto);

        if (oldArCurrentInventoryDto != null){
            setExistArCurrentInventoryDto(dto, arCurrentInventoryDto, oldArCurrentInventoryDto);
        } else {
            setInitArCurrentInventoryDto(dto, arCurrentInventoryDto);
        }
        return arCurrentInventoryDto;
    }

    private void setExistArCurrentInventoryDto(DisposalStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto, ArCurrentInventoryDto oldArCurrentInventoryDto) {
        arCurrentInventoryDto.setId(oldArCurrentInventoryDto.getId());
    }

    private void reduceArCurrentInventoryDto(DisposalStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto, ArCurrentInventoryDto oldArCurrentInventoryDto) {
    }

    private void setInitArCurrentInventoryDto(DisposalStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto) {
    }

    private ArChangeInventoryDto setArChangeInventoryDto(ArSuperDataSubmissionDto newDto, DisposalStageDto dto){
        ArChangeInventoryDto arChangeInventoryDto = newDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        if (DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE.equals(dto.getDisposedType())){
//            arChangeInventoryDto.setFreshOocyteNum();
        }
        return arChangeInventoryDto;
    }

    private ArCurrentInventoryDto getOldArCurrentInventoryDto(ArCurrentInventoryDto arCurrentInventoryDto){
        String hciCode = arCurrentInventoryDto.getHciCode();
        String patientCode = arCurrentInventoryDto.getPatientCode();
        String licenseeId = arCurrentInventoryDto.getLicenseeId();
        String svcName = arCurrentInventoryDto.getSvcName();
        if (StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(licenseeId) || StringUtil.isEmpty(svcName)){
            return null;
        }
        ArCurrentInventoryDto oldArCurrentInventoryDto = arFeClient.getArCurrentInventoryDtoByConds(hciCode,licenseeId,patientCode,svcName).getEntity();
        if (oldArCurrentInventoryDto == null){
            return null;
        }
        return oldArCurrentInventoryDto;
    }

    public List<DisposalStageDto> getDisposalStageDtoList(List<DisposalCycleExcelDto> disposalExcelDtoList) {
        if (IaisCommonUtils.isEmpty(disposalExcelDtoList)){
            return null;
        }
        List<DisposalStageDto> result = IaisCommonUtils.genNewArrayList();
        disposalExcelDtoList.stream().forEach(dto ->{
            DisposalStageDto disposalStageDto = new DisposalStageDto();
            if (StringUtil.isNotEmpty(dto.getDisposedItem())){
                setDisposalType(dto, disposalStageDto);
            }
            disposalStageDto = setNum(dto, disposalStageDto);
            result.add(disposalStageDto);
        });
        return result;
    }

    private DisposalStageDto setNum(DisposalCycleExcelDto dto, DisposalStageDto disposalStageDto) {
        disposalStageDto.setImmatureString(getNumString(dto.getNoImmatureDisposed()));
        disposalStageDto.setImmature(Integer.valueOf(getNumString(dto.getNoImmatureDisposed())));

        disposalStageDto.setAbnormallyFertilisedString(getNumString(dto.getNoAbnormallyFertilisedDisposed()));
        disposalStageDto.setAbnormallyFertilised(Integer.valueOf(getNumString(dto.getNoAbnormallyFertilisedDisposed())));

        disposalStageDto.setUnfertilisedString(getNumString(dto.getNoUnfertilisedDisposed()));
        disposalStageDto.setUnfertilised(Integer.valueOf(getNumString(dto.getNoUnfertilisedDisposed())));

        disposalStageDto.setAtreticString(getNumString(dto.getNoAtreticDisposed()));
        disposalStageDto.setAtretic(Integer.valueOf(getNumString(dto.getNoAtreticDisposed())));

        disposalStageDto.setDamagedString(getNumString(dto.getNoDamagedDisposed()));
        disposalStageDto.setDamaged(Integer.valueOf(getNumString(dto.getNoDamagedDisposed())));

        disposalStageDto.setLysedOrDegeneratedString(getNumString(dto.getNoLysedDegeneratedDisposed()));
        disposalStageDto.setLysedOrDegenerated(Integer.valueOf(getNumString(dto.getNoLysedDegeneratedDisposed())));

        disposalStageDto.setUnhealthyNumString(getNumString(dto.getNoPoorQualityUnhealthyAbnormalDisposed()));
        disposalStageDto.setUnhealthyNum(Integer.valueOf(getNumString(dto.getNoPoorQualityUnhealthyAbnormalDisposed())));

        disposalStageDto.setOtherDiscardedNumString(getNumString(dto.getDiscardedForOtherReasons()));
        disposalStageDto.setOtherDiscardedNum(Integer.valueOf(getNumString(dto.getDiscardedForOtherReasons())));
        disposalStageDto.setOtherDiscardedReason(dto.getOtherReasonsForDiscarding());

        disposalStageDto.setTotalNum(disposalStageDto.getTotalNum());
        return disposalStageDto;
    }

    private String getNumString(String str){
        if (str == null){
            return "0";
        }
        return str.substring(0,str.indexOf("."));
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
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(DisposalCycleExcelDto.class);
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
            if (disposalStageDto.getTotalNum() == null
                    || disposalStageDto.getTotalNum() == 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("disposedItem"), "One data item in the list must be entered"));
            } else {
                doValidateNum(errorMsgs, disposalStageDto, fieldCellMap, i);
            }
        }

        if (disposalStageDto.getOtherDiscardedNumString() != null){
            doValidateOtherNum(errorMsgs, disposalStageDto, fieldCellMap, i);
        } else {
            if (StringUtil.isEmpty(disposalStageDto.getOtherDiscardedNumString())
                    && disposalStageDto.getDisposedType().equals(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM)){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr006));
            }
        }
    }

    private void doValidateOtherNum(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        if (!StringUtil.isDigit(disposalStageDto.getOtherDiscardedNumString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr002));
        } else {
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
                               Map<String, ExcelPropertyDto> fieldCellMap, int i) {
        if(disposalStageDto.getDisposedTypeDisplay() == 1){
            if (disposalStageDto.getImmatureString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noImmatureDisposed",disposalStageDto.getImmature(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getImmatureString(),"noImmatureDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getAbnormallyFertilisedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noAbnormallyFertilisedDisposed",disposalStageDto.getAbnormallyFertilised(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getAbnormallyFertilisedString(),"noAbnormallyFertilisedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getUnfertilisedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noUnfertilisedDisposed",disposalStageDto.getUnfertilised(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getUnfertilisedString(),"noUnfertilisedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getAtreticString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noAtreticDisposed",disposalStageDto.getAtretic(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getAtreticString(),"noAtreticDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getDamagedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noDamagedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getDamagedString(),"noDamagedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getLysedOrDegeneratedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noLysedDegeneratedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getLysedOrDegeneratedString(),"noLysedDegeneratedDisposed",errorMsgs,fieldCellMap,i);
            }
        }

        if (disposalStageDto.getDisposedTypeDisplay() == 2){
            if (disposalStageDto.getUnhealthyNumString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noPoorQualityUnhealthyAbnormalDisposed",disposalStageDto.getDamaged(),fieldCellMap,i);
            } else {
                doValidateIsDigit(disposalStageDto.getUnhealthyNumString(),"noPoorQualityUnhealthyAbnormalDisposed",errorMsgs,fieldCellMap,i);
            }
        }
    }

    private void doValidateIsDigit(String value, String filed ,List<FileErrorMsg> errorMsgs,Map<String, ExcelPropertyDto> fieldCellMap, int i){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        if (!StringUtil.isDigit(value)){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(filed), errMsgErr002));
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
