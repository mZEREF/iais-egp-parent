package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
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
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
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

    @Autowired
    private ArFeClient arFeClient;

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
        List<ArSuperDataSubmissionDto> arSuperList = StreamSupport.stream(sfoCycleDtoList.spliterator(), useParallel)
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

    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto
            , EfoCycleStageDto dto) {
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        ArSuperDataSubmissionDto newDto = DataSubmissionHelper.reNew(arSuperDto);
        DataSubmissionDto dataSubmissionDto = uploadCommonService.setCommonDataSubmissionDtoField(request, declaration, newDto,
                DataSubmissionConsts.DS_CYCLE_SFO,Boolean.FALSE);
        dataSubmissionDto.setCycleStage(DataSubmissionConsts.AR_CYCLE_SFO);
        newDto.setArCurrentInventoryDto(setArCurrentInventoryDto(newDto,dto));
        newDto.setArChangeInventoryDto(setArChangeInventoryDto(newDto,dto));
        newDto.setEfoCycleStageDto(dto);
        return newDto;
    }

    private ArCurrentInventoryDto setArCurrentInventoryDto(ArSuperDataSubmissionDto newDto, EfoCycleStageDto dto){
        ArCurrentInventoryDto arCurrentInventoryDto = newDto.getArCurrentInventoryDto();
        if (arCurrentInventoryDto == null){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        if (arCurrentInventoryDto.getFrozenSpermNum() == 0){
            arCurrentInventoryDto.setFrozenSpermNum(dto.getCryopresNum());
        }
        arCurrentInventoryDto.setHciCode(newDto.getHciCode());
        arCurrentInventoryDto.setSvcName(newDto.getSvcName());
        arCurrentInventoryDto.setLicenseeId(newDto.getLicenseeId());
        if (newDto.getPatientInfoDto() != null && newDto.getPatientInfoDto().getPatient() != null
                && newDto.getPatientInfoDto().getPatient().getPatientCode() != null){
            arCurrentInventoryDto.setPatientCode(newDto.getPatientInfoDto().getPatient().getPatientCode());
        }
        isVerifyCurrency(arCurrentInventoryDto);
        return arCurrentInventoryDto;
    }

    private ArChangeInventoryDto setArChangeInventoryDto(ArSuperDataSubmissionDto newDto, EfoCycleStageDto dto){
        ArChangeInventoryDto arChangeInventoryDto = newDto.getArChangeInventoryDto();
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        if (arChangeInventoryDto.getFrozenSpermNum() == 0){
            arChangeInventoryDto.setFrozenSpermNum(dto.getCryopresNum());
        }
        return arChangeInventoryDto;
    }

    private void isVerifyCurrency(ArCurrentInventoryDto arCurrentInventoryDto){
        String hciCode = arCurrentInventoryDto.getHciCode();
        String patientCode = arCurrentInventoryDto.getPatientCode();
        String licenseeId = arCurrentInventoryDto.getLicenseeId();
        String svcName = arCurrentInventoryDto.getSvcName();
        if (StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(patientCode) || StringUtil.isEmpty(licenseeId) || StringUtil.isEmpty(svcName)){
            return;
        }
        ArCurrentInventoryDto oldArCurrentInventoryDto = arFeClient.getArCurrentInventoryDtoByConds(hciCode,licenseeId,patientCode,svcName).getEntity();
        if (oldArCurrentInventoryDto == null){
            return;
        }
        arCurrentInventoryDto.setId(oldArCurrentInventoryDto.getId());
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
            sfoCycleStageDtos = getEfoCycleStageDtoList(sfoExcelDtoList,errorMap);
            Map<String, ExcelPropertyDto> fieldCellMap = ExcelValidatorHelper.getFieldCellMap(SfoExcelDto.class);
            List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(sfoCycleStageDtos, "file", fieldCellMap);
            for (int i = 1; i <= fileItemSize; i++) {
                EfoCycleStageDto efoCycleStageDto = sfoCycleStageDtos.get(i-1);
                validatePatientIdTypeAndNumber(sfoExcelDtoList, fieldCellMap, errorMsgs, i,request);
                validateEfoCycleStageDto(errorMsgs, efoCycleStageDto, fieldCellMap, i);
            }
            if (!errorMsgs.isEmpty()) {
                fileItemSize = uploadCommonService.getErrorRowInfo(errorMap, request, errorMsgs);
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
                                                List<FileErrorMsg> errorMsgs, int i,HttpServletRequest request) {
        String patientId = sfoExcelDtoList.get(i-1).getIdType();
        String patientNumber = sfoExcelDtoList.get(i-1).getIdNumber();
        uploadCommonService.validatePatientIdTypeAndNumber(patientId,patientNumber,fieldCellMap,errorMsgs,i,"idType","idNumber",request,Boolean.FALSE);
    }

    /**
     * Transfer to patient info dto from sfo cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param sfoExcelDtoList
     * @return
     */
    private List<EfoCycleStageDto> getEfoCycleStageDtoList(List<SfoExcelDto> sfoExcelDtoList,Map<String, String> errorMap) {
        if (sfoExcelDtoList == null){
            return null;
        }
        List<EfoCycleStageDto> result = IaisCommonUtils.genNewArrayList(sfoExcelDtoList.size());
        for (SfoExcelDto sfoExcelDto : sfoExcelDtoList) {
            EfoCycleStageDto sfoCycleStageDto = new EfoCycleStageDto();
            try {
                sfoCycleStageDto.setStartDate(Formatter.parseDate(sfoExcelDto.getDateFreezing()));
            } catch (ParseException e) {
                errorMap.put("uploadFileError", "GENERAL_ERR0033");
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
