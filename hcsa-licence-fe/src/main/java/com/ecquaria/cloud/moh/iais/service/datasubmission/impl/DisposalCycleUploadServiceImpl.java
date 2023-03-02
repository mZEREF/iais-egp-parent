package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.DisposalCycleExcelDto;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
                errorMap = doValidateDisposalCycleStageDto(errorMap, fileItemSize,
                        disposalExcelDtoList,fileEntry,request);
            }
        }
        return errorMap;
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
        DataSubmissionDto dataSubmissionDto = uploadCommonService.setCommonDataSubmissionDtoField(request, declaration, newDto,
                DataSubmissionConsts.DS_CYCLE_STAGE,Boolean.FALSE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_STAGE_DISPOSAL);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setArCurrentInventoryDto(setArCurrentInventoryDto(newDto,dto,request));
        newDto.setArChangeInventoryDto(setArChangeInventoryDto(newDto,dto));
        newDto.setDisposalStageDto(dto);
        return newDto;
    }

    // inventory
    private ArCurrentInventoryDto setArCurrentInventoryDto(ArSuperDataSubmissionDto newDto, DisposalStageDto dto,HttpServletRequest request){
        ArCurrentInventoryDto arCurrentInventoryDto = newDto.getArCurrentInventoryDto();
        if (arCurrentInventoryDto == null){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        ArChangeInventoryDto arChangeInventoryDto = (ArChangeInventoryDto) request.getSession().getAttribute("DisposalArChangeInventory");
        arCurrentInventoryDto.setHciCode(newDto.getHciCode());
        arCurrentInventoryDto.setSvcName(newDto.getSvcName());
        arCurrentInventoryDto.setLicenseeId(newDto.getLicenseeId());
        if (newDto.getPatientInfoDto() != null && newDto.getPatientInfoDto().getPatient() != null
                && newDto.getPatientInfoDto().getPatient().getPatientCode() != null){
            arCurrentInventoryDto.setPatientCode(newDto.getPatientInfoDto().getPatient().getPatientCode());
        }
        ArCurrentInventoryDto oldArCurrentInventoryDto = getOldArCurrentInventoryDto(arCurrentInventoryDto);
        if (oldArCurrentInventoryDto != null){
            arCurrentInventoryDto.setId(oldArCurrentInventoryDto.getId());
        }
        arCurrentInventoryDto = setInitArCurrentInventoryDto(dto, arCurrentInventoryDto, arChangeInventoryDto);
        return arCurrentInventoryDto;
    }

    private ArCurrentInventoryDto setInitArCurrentInventoryDto(DisposalStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto,ArChangeInventoryDto arChangeInventoryDto) {
        if (arChangeInventoryDto == null || dto == null || arChangeInventoryDto == null){
            return arCurrentInventoryDto;
        }
        switch (dto.getDisposedType()){
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                arCurrentInventoryDto.setFreshOocyteNum(arChangeInventoryDto.getFreshOocyteNum() -  dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                arCurrentInventoryDto.setFrozenOocyteNum(arChangeInventoryDto.getFrozenOocyteNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                arCurrentInventoryDto.setThawedOocyteNum(arChangeInventoryDto.getThawedOocyteNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                arCurrentInventoryDto.setFreshEmbryoNum(arChangeInventoryDto.getFreshEmbryoNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                arCurrentInventoryDto.setFrozenEmbryoNum(arChangeInventoryDto.getFrozenEmbryoNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                arCurrentInventoryDto.setThawedEmbryoNum(arChangeInventoryDto.getThawedEmbryoNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                arCurrentInventoryDto.setFrozenSpermNum(arChangeInventoryDto.getFrozenSpermNum() - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_SPERM:
                arCurrentInventoryDto.setFreshSpermNum(arChangeInventoryDto.getFreshSpermNum() - dto.getTotalNum());
                break;
            default:
        }
        return arCurrentInventoryDto;
    }
    private ArChangeInventoryDto setArChangeInventoryDto(ArSuperDataSubmissionDto newDto, DisposalStageDto dto){
        ArChangeInventoryDto arChangeInventoryDto = newDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        switch (dto.getDisposedType()){
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                arChangeInventoryDto.setFreshOocyteNum(0 -dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                arChangeInventoryDto.setFrozenOocyteNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                arChangeInventoryDto.setThawedOocyteNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                arChangeInventoryDto.setFreshEmbryoNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                arChangeInventoryDto.setFrozenEmbryoNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                arChangeInventoryDto.setThawedEmbryoNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                arChangeInventoryDto.setFrozenSpermNum(0 - dto.getTotalNum());
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_SPERM:
                arChangeInventoryDto.setFreshSpermNum(0 - dto.getTotalNum());
                break;
            default:
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

    // set disposalStage dto
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

    // validate disposalStageDto
    private Map<String, String> doValidateDisposalCycleStageDto(Map<String, String> errorMap, int fileItemSize,
                                                                  List<DisposalCycleExcelDto> disposalExcelDtoList,
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
            List<DisposalStageDto> disposalStageDtos = getDisposalStageDtoList(disposalExcelDtoList);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(DisposalCycleExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(disposalStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                DisposalStageDto disposalStageDto = disposalStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(disposalExcelDtoList, fieldCellMap, errorMsgs, i,request,errorMap);
                validateTDisposalCycleStageDto(errorMsgs, disposalStageDto, fieldCellMap, i,request);
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
                                                List<FileErrorMsg> errorMsgs, int i,HttpServletRequest request,Map<String, String> errorMap) {
        String patientId = disposalExcelDtoList.get(i-1).getPatientIdType();
        String patientNumber = disposalExcelDtoList.get(i-1).getPatientIdNo();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,
                "patientIdType","patientIdNo",request,Boolean.FALSE);
        Boolean isThoughValidate = (Boolean) request.getSession().getAttribute("correct");
        if (Boolean.TRUE.equals(isThoughValidate)){
            PatientInfoDto patientInfoDto = uploadCommonService.setPatientInfo(patientId,patientNumber,request);
            if (patientInfoDto != null){
                amountInventory(patientInfoDto,errorMap,request);
            }
        }
    }

    private void amountInventory(PatientInfoDto patientInfoDto, Map<String, String> errorMap,HttpServletRequest request) {
        PatientDto patientDto = patientInfoDto.getPatient();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (patientDto == null || arSuperDataSubmissionDto == null){
            return;
        }
        String patientCode = patientDto.getPatientCode();
        String hciCode = arSuperDataSubmissionDto.getHciCode();
        String licenseeId = arSuperDataSubmissionDto.getLicenseeId();
        String svcName = arSuperDataSubmissionDto.getSvcName();
        if (StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(licenseeId) || StringUtil.isEmpty(svcName)){
            return;
        }
        ArCurrentInventoryDto arCurrentInventoryDto = arFeClient.getArCurrentInventoryDtoByConds(hciCode,licenseeId,patientCode,svcName).getEntity();
        if (arCurrentInventoryDto == null){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("field","disposal");
            String errDs002 = MessageUtil.getMessageDesc("DS_ERR002",repMap);
            errorMap.put("uploadFileError",errDs002);
            return;
        } else {
            request.getSession().setAttribute("DisposalArCurrentInventory", arCurrentInventoryDto);
        }
        CycleDto cycleDto = new CycleDto();
        cycleDto.setPatientCode(patientCode);
        cycleDto.setHciCode(hciCode);
        cycleDto.setSvcName(svcName);
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_AR);
        List<String> status = IaisCommonUtils.genNewArrayList();
        status.add(DataSubmissionConsts.DS_STATUS_ONGOING);
        cycleDto.setStatuses(status);
        setSessionArChangeInventoryDto(request, cycleDto);
    }

    private void setSessionArChangeInventoryDto(HttpServletRequest request, CycleDto cycleDto) {
        List<CycleDto> cycleDtos = arFeClient.getCycleIdByCycleDto(cycleDto).getEntity();
        if (IaisCommonUtils.isEmpty(cycleDtos)){
            return;
        }
        List<String> cycIdList = cycleDtos.stream().map(CycleDto::getId).collect(Collectors.toList());
        if (IaisCommonUtils.isEmpty(cycIdList)){
            return;
        }

        DataSubmissionDto dataSubmissionDto = arFeClient.getSubmissionIdByCycleIdAndCycleType(cycIdList).getEntity();
        if (dataSubmissionDto == null){
            return;
        }
        ArChangeInventoryDto arChangeInventoryDto = arFeClient.getChangeInventoryDtoBySubmissionId(dataSubmissionDto.getId()).getEntity();
        if (arChangeInventoryDto != null){
            request.getSession().setAttribute("DisposalArChangeInventory", arChangeInventoryDto);
        }
    }

    private void validateTDisposalCycleStageDto(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto,
                                                    Map<String, ExcelPropertyDto> fieldCellMap, int i,HttpServletRequest request) {
        if (disposalStageDto == null){
            return;
        }
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (StringUtil.isEmpty(disposalStageDto.getDisposedType())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("disposedItem"), errMsgErr006));
        }
        int maxSamplesNum = 100;
        ArCurrentInventoryDto arCurrentInventoryDto = (ArCurrentInventoryDto) request.getSession()
                .getAttribute("DisposalArCurrentInventory");
        if (arCurrentInventoryDto == null){
            return;
        }
        if (disposalStageDto.getDisposedTypeDisplay() != null){
            maxSamplesNum = getMaxSamplesNum(disposalStageDto, maxSamplesNum, arCurrentInventoryDto);
            if (disposalStageDto.getTotalNum() == null
                    || disposalStageDto.getTotalNum() == 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("disposedItem"), "One data item in the list must be entered"));
            } else {
                doValidateNum(errorMsgs, disposalStageDto, fieldCellMap, i, maxSamplesNum);
            }
        }

        if (disposalStageDto.getOtherDiscardedNumString() != null){
            doValidateOtherNum(errorMsgs, disposalStageDto, fieldCellMap, i,maxSamplesNum);
        } else {
            if (StringUtil.isEmpty(disposalStageDto.getOtherDiscardedNumString())
                    && disposalStageDto.getDisposedType().equals(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM)){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr006));
            }
        }
    }

    private int getMaxSamplesNum(DisposalStageDto disposalStageDto, int maxSamplesNum, ArCurrentInventoryDto arCurrentInventoryDto) {
        switch (disposalStageDto.getDisposedType()){
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE:
                maxSamplesNum = arCurrentInventoryDto.getFreshOocyteNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE:
                maxSamplesNum = arCurrentInventoryDto.getFrozenOocyteNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE:
                maxSamplesNum = arCurrentInventoryDto.getThawedOocyteNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO:
                maxSamplesNum = arCurrentInventoryDto.getFreshEmbryoNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO:
                maxSamplesNum = arCurrentInventoryDto.getFrozenEmbryoNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO:
                maxSamplesNum = arCurrentInventoryDto.getThawedEmbryoNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM:
                maxSamplesNum = arCurrentInventoryDto.getFrozenSpermNum();
                break;
            case DataSubmissionConsts.DISPOSAL_TYPE_FRESH_SPERM:
                maxSamplesNum = arCurrentInventoryDto.getFreshSpermNum();
                break;
            default:
        }
        return maxSamplesNum;
    }

    private void doValidateOtherNum(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i,int maxSamplesNum) {
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
        if (!StringUtil.isDigit(disposalStageDto.getOtherDiscardedNumString())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), errMsgErr002));
        } else {
            doValidateMaxAndMinNum(errorMsgs,"discardedForOtherReasons", disposalStageDto.getImmature(), fieldCellMap, i,maxSamplesNum);
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
                               Map<String, ExcelPropertyDto> fieldCellMap, int i,int maxSamplesNum) {
        if(disposalStageDto.getDisposedTypeDisplay() == 1){
            if (disposalStageDto.getImmatureString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noImmatureDisposed",disposalStageDto.getImmature(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getImmatureString(),"noImmatureDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getAbnormallyFertilisedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noAbnormallyFertilisedDisposed",
                        disposalStageDto.getAbnormallyFertilised(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getAbnormallyFertilisedString(),"noAbnormallyFertilisedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getUnfertilisedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noUnfertilisedDisposed",disposalStageDto.getUnfertilised(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getUnfertilisedString(),"noUnfertilisedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getAtreticString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noAtreticDisposed",disposalStageDto.getAtretic(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getAtreticString(),"noAtreticDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getDamagedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noDamagedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getDamagedString(),"noDamagedDisposed",errorMsgs,fieldCellMap,i);
            }

            if (disposalStageDto.getLysedOrDegeneratedString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noLysedDegeneratedDisposed",disposalStageDto.getDamaged(),fieldCellMap,i,maxSamplesNum);
            } else {
                doValidateIsDigit(disposalStageDto.getLysedOrDegeneratedString(),"noLysedDegeneratedDisposed",errorMsgs,fieldCellMap,i);
            }
        }

        if (disposalStageDto.getDisposedTypeDisplay() == 2){
            if (disposalStageDto.getUnhealthyNumString() != null){
                doValidateMaxAndMinNum(errorMsgs,"noPoorQualityUnhealthyAbnormalDisposed",disposalStageDto.getDamaged(),fieldCellMap,i,maxSamplesNum);
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

    private void doValidateMaxAndMinNum(List<FileErrorMsg> errorMsgs,String filed,int value,
                                        Map<String, ExcelPropertyDto> fieldCellMap, int i,int maxSamplesNum){
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
