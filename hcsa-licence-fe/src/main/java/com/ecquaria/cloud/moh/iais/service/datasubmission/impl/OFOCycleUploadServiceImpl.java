package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OFOCycleUploadServiceImpl {
    @Autowired
    private ArBatchUploadCommonServiceImpl arBatchUploadCommonService;
    @Autowired
    private DonationStageUploadServiceImpl donationStageUploadService;
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;

    public Map<String, String> getErrorMap(HttpServletRequest request){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = arBatchUploadCommonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = arBatchUploadCommonService.getPageShowFileDto(fileEntry);
        ParamUtil.setSessionAttr(request, PAGE_SHOW_FILE, pageShowFileDto);
        errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, request);

        List<EfoCycleStageDto> efoCycleStageDtoList = null;
        List<OocyteRetrievalStageDto> oocyteRetrievalStageDtoList = null;
        List<ArSubFreezingStageDto> arSubFreezingStageDtoList = null;
        List<DisposalStageDto> disposalStageDtoList = null;
        List<DonationStageDto> donationStageDtoList = null;
        if(errorMap.isEmpty()){
            String fileName = fileEntry.getValue().getName();
            if (!fileName.equals("OFO File Upload.xlsx") && !fileName.equals("OFO File Upload.csv")) {
                errorMap.put("uploadFileError", "Please change the file name.");
            }
            List<OFOCycleStageExcelDto> ofoCycleStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, OFOCycleStageExcelDto.class);
            List<OocyteRetrievalExcelDto> oocyteRetrievalExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry,OocyteRetrievalExcelDto.class);
            List<FreezingExcelDto> freezingExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, FreezingExcelDto.class);
            List<DisposalExcelDto> disposalExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, DisposalExcelDto.class);
            List<OFODonationStageExcelDto> ofoDonationStageExcelDtoList = arBatchUploadCommonService.getExcelDtoList(fileEntry, OFODonationStageExcelDto.class);
            fileItemSize = ofoCycleStageExcelDtoList.size();
            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> ofoCycleStageFieldCellMap = ExcelValidatorHelper.getFieldCellMap(OFOCycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> oocyteRetrievalFieldCellMap = ExcelValidatorHelper.getFieldCellMap(OocyteRetrievalExcelDto.class);
                Map<String, ExcelPropertyDto> freezingFieldCellMap = ExcelValidatorHelper.getFieldCellMap(FreezingExcelDto.class);
                Map<String, ExcelPropertyDto> disposalFieldCellMap = ExcelValidatorHelper.getFieldCellMap(DisposalExcelDto.class);
                Map<String, ExcelPropertyDto> ofoDonationStageFieldCellMap = ExcelValidatorHelper.getFieldCellMap(OFODonationStageExcelDto.class);

                List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
                efoCycleStageDtoList = getOfoCycleStageDtoList(ofoCycleStageExcelDtoList,errorMsgs,ofoCycleStageFieldCellMap,request);
                oocyteRetrievalStageDtoList = getOocyteRetrievalStageList(oocyteRetrievalExcelDtoList,errorMsgs,oocyteRetrievalFieldCellMap,request);
                arSubFreezingStageDtoList = getFreezingStageList(freezingExcelDtoList,errorMsgs,freezingFieldCellMap,request);
                disposalStageDtoList = getDisposalStageList(disposalExcelDtoList,errorMsgs,disposalFieldCellMap,request);
                donationStageDtoList = donationStageUploadService.getDonationStageDtoList(ofoDonationStageExcelDtoList,errorMsgs,ofoDonationStageFieldCellMap,request);

                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(efoCycleStageDtoList, "file", ofoCycleStageFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(oocyteRetrievalStageDtoList, "file", oocyteRetrievalFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(arSubFreezingStageDtoList, "file", freezingFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(disposalStageDtoList, "file", disposalFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(donationStageDtoList, "file", ofoDonationStageFieldCellMap));

                doValid(errorMsgs,efoCycleStageDtoList,ofoCycleStageFieldCellMap,request);
                doValid(errorMsgs,oocyteRetrievalStageDtoList,ofoCycleStageFieldCellMap,request);
                doValid(errorMsgs,arSubFreezingStageDtoList,ofoCycleStageFieldCellMap,request);
                doValid(errorMsgs,disposalStageDtoList,ofoCycleStageFieldCellMap,request);
                doValid(errorMsgs,donationStageDtoList,ofoCycleStageFieldCellMap,request);

                arBatchUploadCommonService.getErrorRowInfo(errorMap,request,errorMsgs);
            }
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, "isOFOCycleFile", Boolean.FALSE);
            } else {
                ParamUtil.setRequestAttr(request, "isOFOCycleFile", Boolean.TRUE);
//                request.getSession().setAttribute(DataSubmissionConsts.NON_PATIENT_DONORSAMPLE_LIST, null);
            }
        }
        return errorMap;
    }
    private <T> void doValid(List<FileErrorMsg> errorMsgs,List<T> tDtos,Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        int count = 0;
        for (T item : tDtos){
            count++;
            if(item instanceof EfoCycleStageDto){
                validOFOCycleStage(errorMsgs,(EfoCycleStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof OocyteRetrievalStageDto){
                validOocyteRetrieval(errorMsgs,(OocyteRetrievalStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof ArSubFreezingStageDto){
                validFreezing(errorMsgs,(ArSubFreezingStageDto) item,fieldCellMap,count,request);
            }
            if(item instanceof DisposalStageDto){
                validDisposal(errorMsgs,(DisposalStageDto) item,fieldCellMap,count,request);
            }
            if(item instanceof DonationStageDto){
                donationStageUploadService.validDonorSample(errorMsgs,(DonationStageDto) item,fieldCellMap,count);
            }
        }
    }
    private Integer getIntBoolen(String value){
        if(value == null){
            return null;
        }
        return "Yes".equals(value) ? 1 : 0;
    }
    private List<EfoCycleStageDto> getOfoCycleStageDtoList(List<OFOCycleStageExcelDto> ofoCycleStageExcelDtoList,List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        if (ofoCycleStageExcelDtoList == null) {
            return null;
        }
        List<EfoCycleStageDto> result = IaisCommonUtils.genNewArrayList(ofoCycleStageExcelDtoList.size());
        int count = 0;
        for(OFOCycleStageExcelDto excelDto : ofoCycleStageExcelDtoList){
            count ++;
            EfoCycleStageDto dto = new EfoCycleStageDto();
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,false);
            try {
                dto.setStartDate(Formatter.parseDate(excelDto.getDateOfFreezing()));
            }catch (Exception e){
                arBatchUploadCommonService.validateParseDate(errorMsgs,excelDto.getDateOfFreezing(),fieldCellMap,count,"dateOfFreezing",false);
            }
            dto.setIsMedicallyIndicated(getIntBoolen(excelDto.getIsMedicallyIndicated()));
            dto.setReason(arBatchUploadCommonService.getMstrKeyByValue(excelDto.getReason(),"EFOR"));
            dto.setOtherReason(excelDto.getFreetextOtherReason());
            dto.setCryopresNum(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreserved(),"noCryopreserved"));
            dto.setOthers(excelDto.getOthers());
            result.add(dto);
        }
        return result;
    }
    private Boolean getBoolen(String val){
        if(val == null){
            return null;
        }
        return "Yes".equals(val);
    }
    public List<OocyteRetrievalStageDto> getOocyteRetrievalStageList(List<OocyteRetrievalExcelDto> oocyteRetrievalExcelDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        if(oocyteRetrievalExcelDtoList == null){
            return null;
        }
        List<OocyteRetrievalStageDto> result = IaisCommonUtils.genNewArrayList(oocyteRetrievalExcelDtoList.size());
        int count = 0;
        for(OocyteRetrievalExcelDto excelDto : oocyteRetrievalExcelDtoList){
            count ++;
            OocyteRetrievalStageDto dto = new OocyteRetrievalStageDto();
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,false);
            dto.setIsOvarianSyndrome(getBoolen(excelDto.getSevereOHS()));
            dto.setIsFromPatient(getBoolen(excelDto.getIsOocyteFromPatient()));
            dto.setIsFromPatientTissue(getBoolen(excelDto.getIsOocyteFromPatientsOT()));
            dto.setMatureRetrievedNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedMature(),"noRetrievedMature"));
            dto.setImmatureRetrievedNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedImmature(),"noRetrievedImmature"));
            dto.setOtherRetrievedNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedOthers(),"noRetrievedOthers"));
            result.add(dto);
        }
        return result;
    }
    public List<ArSubFreezingStageDto> getFreezingStageList(List<FreezingExcelDto> freezingExcelDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        if(freezingExcelDtoList == null){
            return null;
        }
        List<ArSubFreezingStageDto> result = IaisCommonUtils.genNewArrayList(freezingExcelDtoList.size());
        int count = 0;
        for(FreezingExcelDto excelDto : freezingExcelDtoList){
            count ++;
            ArSubFreezingStageDto dto = new ArSubFreezingStageDto();
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,false);
            dto.setFreshOocyteCryopNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedFreshOocyte(),"noCryopreservedFreshOocyte"));
            dto.setThawedOocyteCryopNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedThawedOocyte(),"noCryopreservedThawedOocyte"));
            dto.setFreshEmbryoCryopNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedFreshEmbryo(),"noCryopreservedFreshEmbryo"));
            dto.setThawedEmbryoCryopNum(arBatchUploadCommonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedThawedEmbryo(),"noCryopreservedThawedEmbryo"));
            try {
                dto.setCryopreservedDate(Formatter.parseDate(excelDto.getCryopreservationDate()));
            }catch (Exception e){
                arBatchUploadCommonService.validateParseDate(errorMsgs,excelDto.getCryopreservationDate(),fieldCellMap,count,"cryopreservationDate",false);
            }
            result.add(dto);
        }
        return result;
    }
    public List<DisposalStageDto> getDisposalStageList(List<DisposalExcelDto> disposalExcelDtoList, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request){
        if(disposalExcelDtoList == null){
            return null;
        }
        List<DisposalStageDto> result = IaisCommonUtils.genNewArrayList(disposalExcelDtoList.size());
        int count = 0;
        for (DisposalExcelDto excelDto : disposalExcelDtoList){
            count ++;
            DisposalStageDto dto = new DisposalStageDto();
            arBatchUploadCommonService.validatePatientIdTypeAndNumber(excelDto.getPatientIdType(),excelDto.getPatientIdNo(),fieldCellMap,errorMsgs,count,"patientIdType","patientIdNo",request,false);
            dto.setDisposedType(arBatchUploadCommonService.getMstrKeyByValue(excelDto.getDisposedItem(),"DISPTY"));
            dto.setImmature(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoImmatureDisposed(),"noImmatureDisposed"));
            dto.setAbnormallyFertilised(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoAbnormallyFertilisedDisposed(),"noAbnormallyFertilisedDisposed"));
            dto.setUnfertilised(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoUnfertilisedDisposed(),"noUnfertilisedDisposed"));
            dto.setAtretic(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoAtreticDisposed(),"noAtreticDisposed"));
            dto.setDamaged(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDamagedDisposed(),"noDamagedDisposed"));
            dto.setLysedOrDegenerated(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoLysedDegeneratedDisposed(),"noLysedDegeneratedDisposed"));
            dto.setUnhealthyNum(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoPoorQualityUnhealthyAbnormalDisposed(),"noPoorQualityUnhealthyAbnormalDisposed"));
            dto.setOtherDiscardedNum(arBatchUploadCommonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getDiscardedForOtherReasons(),"discardedForOtherReasons"));
            dto.setOtherDiscardedReason(excelDto.getOtherReasonsForDiscarding());
            result.add(dto);
        }
        return result;
    }
    private void validOFOCycleStage(List<FileErrorMsg> errorMsgs,EfoCycleStageDto ecDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getStartDate(),fieldCellMap,i,"dateOfFreezing")){
            arBatchUploadCommonService.validDateNoFuture(ecDto.getStartDate(),errorMsgs,"dateOfFreezing","(3) Date of Freezing",fieldCellMap,i);
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getIsMedicallyIndicated(),fieldCellMap,i,"isMedicallyIndicated")){
            if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getReason(),fieldCellMap,i,"reason")){
                if(ecDto.getIsMedicallyIndicated() != 1){
                    arBatchUploadCommonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"reason","(5) Reason",fieldCellMap,i);
                }else {
                    if("EFOR004".equals(ecDto.getReason())){
                        if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getOtherReason(),fieldCellMap,i,"freetextOtherReason")){
                            arBatchUploadCommonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"freetextOtherReason","(6) Reason(Others)",fieldCellMap,i);
                        }
                    }
                }
            }
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getCryopresNum(),fieldCellMap,i,"noCryopreserved")){
            if(ecDto.getCryopresNum() == 0){
                if(arBatchUploadCommonService.validateIsNull(errorMsgs,ecDto.getOthers(),fieldCellMap,i,"others")){
                    arBatchUploadCommonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"others","(8) Others",fieldCellMap,i);
                }
            }else {
                if(ecDto.getCryopresNum() < 0){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreserved"), "Can not be negative number"));
                }
            }
        }
    }
    public void validOocyteRetrieval(List<FileErrorMsg> errorMsgs,OocyteRetrievalStageDto orDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){
        arBatchUploadCommonService.validateIsNull(errorMsgs,orDto.getIsOvarianSyndrome(),fieldCellMap,i,"severeOHS");
        if(orDto.getIsFromPatient() == null && orDto.getIsFromPatientTissue() == null){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteFromPatient"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteFromPatientsOT"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(orDto.getMatureRetrievedNum())){
            if(Integer.parseInt(orDto.getMatureRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedMature"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedMature","(6) No. Retrieved (Mature)",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(orDto.getImmatureRetrievedNum())){
            if(Integer.parseInt(orDto.getImmatureRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedImmature"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedImmature","(7) No. Retrieved (Immature)",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(orDto.getOtherRetrievedNum())){
            if(Integer.parseInt(orDto.getOtherRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedOthers"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedOthers","(8) No. Retrieved (Others)",fieldCellMap,i);
        }
    }
    public void validFreezing(List<FileErrorMsg> errorMsgs,ArSubFreezingStageDto fzDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){

        if(arBatchUploadCommonService.validateIsNull(errorMsgs,fzDto.getFreshOocyteCryopNum(),fieldCellMap,i,"noCryopreservedFreshOocyte")){
            //todo validate greater inventory
            if(Integer.parseInt(fzDto.getFreshOocyteCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshOocyte"), "Can not be negative number"));
            }
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,fzDto.getThawedOocyteCryopNum(),fieldCellMap,i,"noCryopreservedThawedOocyte")){
            //todo validate greater inventory
            if(Integer.parseInt(fzDto.getThawedOocyteCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedOocyte"), "Can not be negative number"));
            }
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,fzDto.getFreshEmbryoCryopNum(),fieldCellMap,i,"noCryopreservedFreshEmbryo")){
            //todo validate greater inventory
            if(Integer.parseInt(fzDto.getFreshEmbryoCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshEmbryo"), "Can not be negative number"));
            }
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,fzDto.getThawedEmbryoCryopNum(),fieldCellMap,i,"noCryopreservedThawedEmbryo")){
            //todo validate greater inventory
            if(Integer.parseInt(fzDto.getThawedEmbryoCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedEmbryo"), "Can not be negative number"));
            }
        }
        if(arBatchUploadCommonService.validateIsNull(errorMsgs,fzDto.getCryopreservedDate(),fieldCellMap,i,"cryopreservationDate")){
            arBatchUploadCommonService.validDateNoFuture(fzDto.getCryopreservedDate(),errorMsgs,"cryopreservationDate","(7) Cryopreservation Date",fieldCellMap,i);
        }
    }

    public void validDisposal(List<FileErrorMsg> errorMsgs,DisposalStageDto disDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){

        arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getDisposedType(),fieldCellMap,i,"disposedItem");

        String type = disDto.getDisposedType();
        String[] strs = {"DISPTY001","DISPTY002","DISPTY003"};
        if(StringUtil.isIn(type,strs)){
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noImmatureDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noAbnormallyFertilisedDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noUnfertilisedDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noAtreticDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noDamagedDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noLysedDegeneratedDisposed");
            arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"discardedForOtherReasons");
        }

        if(StringUtil.isNotEmpty(disDto.getImmature())){
            if(disDto.getImmature() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noImmatureDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getImmature()).length(),2,errorMsgs,"noImmatureDisposed","(4) No. of Immature Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getAbnormallyFertilised())){
            if(disDto.getAbnormallyFertilised() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAbnormallyFertilisedDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getAbnormallyFertilised()).length(),2,errorMsgs,"noAbnormallyFertilisedDisposed","(5) No. of Abnormally Fertilised Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getUnfertilised())){
            if(disDto.getUnfertilised() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUnfertilisedDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getUnfertilised()).length(),2,errorMsgs,"noUnfertilisedDisposed","(6) No. of Unfertilised Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getAtretic())){
            if(disDto.getAtretic() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAtreticDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getAtretic()).length(),2,errorMsgs,"noAtreticDisposed","(7) No. of Atretic Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getDamaged())){
            if(disDto.getDamaged() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDamagedDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getDamaged()).length(),2,errorMsgs,"noDamagedDisposed","(8) No. of Damaged Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getLysedOrDegenerated())){
            if(disDto.getLysedOrDegenerated() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLysedDegeneratedDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getLysedOrDegenerated()).length(),2,errorMsgs,"noLysedDegeneratedDisposed","(9) No. of Lysed/ Degenerated Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getUnhealthyNum())){
            if(disDto.getUnhealthyNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noPoorQualityUnhealthyAbnormalDisposed"), "Can not be negative number"));
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getUnhealthyNum()).length(),2,errorMsgs,"noPoorQualityUnhealthyAbnormalDisposed","(10) No. of Poor Quality/Unhealthy/Abnormal Discarded",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getOtherDiscardedNum())){
            if(disDto.getOtherDiscardedNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), "Can not be negative number"));
            }
            if(disDto.getOtherDiscardedNum() > 0){
                if(arBatchUploadCommonService.validateIsNull(errorMsgs,disDto.getOtherDiscardedReason(),fieldCellMap,i,"otherReasonsForDiscarding")){
                    arBatchUploadCommonService.validFieldLength(disDto.getOtherDiscardedReason().length(),100,errorMsgs,"otherReasonsForDiscarding","(13) Other reasons for Discarding",fieldCellMap,i);
                }
            }
            arBatchUploadCommonService.validFieldLength(String.valueOf(disDto.getUnhealthyNum()).length(),2,errorMsgs,"discardedForOtherReasons","(12) Discarded for Other Reasons",fieldCellMap,i);
        }

    }

}
