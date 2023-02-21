package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.SfoExcelDto;
import com.ecquaria.cloud.moh.iais.dto.TransferInOutExcelDto;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TransferInOutCycleUploadService;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private ArFeClient arFeClient;

    @Autowired
    DsLicenceService dsLicenceService;

    @Autowired
    RequestForChangeService requestForChangeService;

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
        DataSubmissionDto dataSubmissionDto = uploadCommonService.setCommonDataSubmissionDtoField(request, declaration, newDto,
                DataSubmissionConsts.DS_CYCLE_STAGE,Boolean.FALSE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        newDto.setArCurrentInventoryDto(setArCurrentInventoryDto(newDto,dto));
        newDto.setArChangeInventoryDto(setArChangeInventoryDto(newDto,dto));
        newDto.setTransferInOutStageDto(dto);
        return newDto;
    }

    private ArCurrentInventoryDto setArCurrentInventoryDto(ArSuperDataSubmissionDto newDto, TransferInOutStageDto dto){
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
        arCurrentInventoryDto = getOldArCurrentInventoryDto(arCurrentInventoryDto);
        
        if (arCurrentInventoryDto != null){
            
        } else {
            setInitArCurrentInventoryDto(dto, arCurrentInventoryDto);
        }
        return arCurrentInventoryDto;
    }

    private void setExistArCurrentInventoryDto(TransferInOutStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto, ArCurrentInventoryDto oldArCurrentInventoryDto) {
        arCurrentInventoryDto.setId(oldArCurrentInventoryDto.getId());
        if (dto.getTransferType().equals(DataSubmissionConsts.TRANSFER_TYPE_IN)){
            if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)){
                arCurrentInventoryDto.setFrozenOocyteNum(oldArCurrentInventoryDto.getFrozenOocyteNum()+Double.valueOf(dto.getOocyteNum()).intValue());
            } else if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)){
                arCurrentInventoryDto.setFrozenEmbryoNum(oldArCurrentInventoryDto.getFrozenEmbryoNum()+Double.valueOf(dto.getEmbryoNum()).intValue());
            } else if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)){
                arCurrentInventoryDto.setFrozenSpermNum(oldArCurrentInventoryDto.getFrozenSpermNum()+Double.valueOf(dto.getSpermVialsNum()).intValue());
            }
        } else if (dto.getTransferType().equals(DataSubmissionConsts.TRANSFER_TYPE_OUT)){
            reduceArCurrentInventoryDto(dto, arCurrentInventoryDto,oldArCurrentInventoryDto);
        }
    }

    private void reduceArCurrentInventoryDto(TransferInOutStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto, ArCurrentInventoryDto oldArCurrentInventoryDto) {
        if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)){
            arCurrentInventoryDto.setFrozenOocyteNum(oldArCurrentInventoryDto.getFrozenOocyteNum()-Double.valueOf(dto.getOocyteNum()).intValue());
        } else if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)){
            arCurrentInventoryDto.setFrozenEmbryoNum(oldArCurrentInventoryDto.getFrozenEmbryoNum()-Double.valueOf(dto.getEmbryoNum()).intValue());
        } else if (dto.getTransferredList().contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)){
            arCurrentInventoryDto.setFrozenSpermNum(oldArCurrentInventoryDto.getFrozenSpermNum()-Double.valueOf(dto.getSpermVialsNum()).intValue());
        }
    }

    private void setInitArCurrentInventoryDto(TransferInOutStageDto dto, ArCurrentInventoryDto arCurrentInventoryDto) {
        if (StringUtil.isNotEmpty(dto.getSpermVialsNum())){
            arCurrentInventoryDto.setFrozenSpermNum(Double.valueOf(dto.getSpermVialsNum()).intValue());
        }
        if (StringUtil.isNotEmpty(dto.getOocyteNum())){
            arCurrentInventoryDto.setFrozenOocyteNum(Double.valueOf(dto.getOocyteNum()).intValue());
        }
        if (StringUtil.isNotEmpty(dto.getEmbryoNum())){
            arCurrentInventoryDto.setFrozenEmbryoNum(Double.valueOf(dto.getEmbryoNum()).intValue());
        }
    }

    private ArChangeInventoryDto setArChangeInventoryDto(ArSuperDataSubmissionDto newDto, TransferInOutStageDto dto){
        ArChangeInventoryDto arChangeInventoryDto = newDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        if (StringUtil.isNotEmpty(dto.getSpermVialsNum())){
            arChangeInventoryDto.setFrozenSpermNum(Double.valueOf(dto.getSpermVialsNum()).intValue());
        }
        if (StringUtil.isNotEmpty(dto.getOocyteNum())){
            arChangeInventoryDto.setFrozenOocyteNum(Double.valueOf(dto.getOocyteNum()).intValue());
        }
        if (StringUtil.isNotEmpty(dto.getEmbryoNum())){
            arChangeInventoryDto.setFrozenEmbryoNum(Double.valueOf(dto.getEmbryoNum()).intValue());
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
            transferInOutStageDtos = getTransferInOutStageDtoList(transferInOutExcelDtoList,request);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(TransferInOutExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(transferInOutStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                TransferInOutStageDto transferInOutStageDto = transferInOutStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(transferInOutExcelDtoList, fieldCellMap, errorMsgs, i,request);
                validateTransferInOutCycleStageDto(errorMsgs, transferInOutStageDto, fieldCellMap, i);
            }
            if (!errorMsgs.isEmpty()) {
                fileItemSize = uploadCommonService.getErrorRowInfo(errorMap, request, errorMsgs);
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
    private List<TransferInOutStageDto> getTransferInOutStageDtoList(List<TransferInOutExcelDto> transferInOutExcelDtoList,HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(transferInOutExcelDtoList)){
            return null;
        }
        List<TransferInOutStageDto> result = IaisCommonUtils.genNewArrayList();
        transferInOutExcelDtoList.stream().forEach(dto ->{
            TransferInOutStageDto transferInOutStageDto = setTransferInOutStageDto(dto,request);
            result.add(transferInOutStageDto);
        });
        return result;
    }

    private TransferInOutStageDto setTransferInOutStageDto(TransferInOutExcelDto dto,HttpServletRequest request) {
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

        transferInOutStageDto.setFromDonor(StringUtil.isNotEmpty(dto.getIsDonor()) && "Yes".equals(dto.getIsDonor()));
        setTransferInOutName(dto, request, transferInOutStageDto);
        transferInOutStageDto.setOocyteNum(dto.getOocyteNum());
        transferInOutStageDto.setEmbryoNum(dto.getEmbryoNum());
        transferInOutStageDto.setSpermVialsNum(dto.getSpermVialsNum());
        transferInOutStageDto.setTransferDate(dto.getDateTransfer());
        return transferInOutStageDto;
    }

    private void setTransferInOutName(TransferInOutExcelDto dto, HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
        if (DataSubmissionHelper.getCurrentArDataSubmission(request) == null){
            return;
        }
        PremisesDto currentPremisesDto = DataSubmissionHelper.getCurrentArDataSubmission(request).getPremisesDto();
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremiseList(orgId);
        premisesDtos = premisesDtos.stream().filter(premisesDto ->
                !premisesDto.getHciCode().equals(currentPremisesDto.getHciCode()) || !premisesDto.getOrganizationId().equals(currentPremisesDto.getOrganizationId())
        ).collect(Collectors.toList());
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            String licenseeId = getLicenseeId(premisesDto.getOrganizationId());
            premisesSel.add(new SelectOption(licenseeId + "/" + premisesDto.getHciCode(), premisesDto.getPremiseLabel()));
        }
        premisesSel.add(new SelectOption("Others", "Others"));
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && "Transfer In".equals(dto.getTransferType())){
            setTransFromLicenseeIdAndHicCode(dto, transferInOutStageDto, premisesSel,DataSubmissionConsts.TRANSFER_TYPE_IN);
        }
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && "Transfer Out".equals(dto.getTransferType())){
            setTransFromLicenseeIdAndHicCode(dto, transferInOutStageDto, premisesSel,DataSubmissionConsts.TRANSFER_TYPE_OUT);
        }
    }

    private void setTransFromLicenseeIdAndHicCode(TransferInOutExcelDto dto, TransferInOutStageDto transferInOutStageDto, List<SelectOption> premisesSel,String type) {
        String inOut = String.valueOf(Double.valueOf(dto.getTransferInOut()).intValue());
        Pattern pattern = Pattern.compile(inOut);
        for (int i = 0; i < premisesSel.size(); i++) {
            Matcher matcher = pattern.matcher(premisesSel.get(i).getText());
            if (matcher.find()){
                String str = premisesSel.get(i).getValue();
                String licenseeId = str.substring(0,str.indexOf("/"));
                String hicCode = str.substring(str.indexOf("/")+1);
                setTransFromLicenseeIdAndHicCode(transferInOutStageDto, type, licenseeId, hicCode);
                break;
            }
        }
    }

    private void setTransFromLicenseeIdAndHicCode(TransferInOutStageDto transferInOutStageDto, String type, String licenseeId, String hicCode) {
        if (DataSubmissionConsts.TRANSFER_TYPE_IN.equals(type)){
            transferInOutStageDto.setTransInFromLicenseeId(licenseeId);
            transferInOutStageDto.setTransInFromHciCode(hicCode);
        }
        if (DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(type)){
            transferInOutStageDto.setTransOutToLicenseeId(licenseeId);
            transferInOutStageDto.setTransOutToHciCode(hicCode);
        }
    }

    private String getLicenseeId(String orgId) {
        LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(orgId);
        return licenseeDto.getId();
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

        uploadCommonService.validateParseDate(errorMsgs, transferInOutStageDto.getTransferDate(), fieldCellMap, i,"transferInOut",Boolean.FALSE);
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
                                                List<FileErrorMsg> errorMsgs, int i,HttpServletRequest request) {
        String patientId = transferInOutExcelDtoList.get(i-1).getIdType();
        String patientNumber = transferInOutExcelDtoList.get(i-1).getIdNumber();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,"idType","idNumber",request,Boolean.FALSE);
    }
}
