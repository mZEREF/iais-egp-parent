package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OFOCycleUploadServiceImpl {
    @Autowired
    private ArBatchUploadCommonServiceImpl commonService;
    @Autowired
    private DonationStageUploadServiceImpl donationStageUploadService;
    @Autowired
    private ArFeClient arFeClient;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;
    private static final String OFO_CYCLE_STAGE_FILELIST = "OFO_CYCLE_STAGE_FILELIST";
    private static final String OOCYTE_RETRIEVAL_FILELIST = "OOCYTE_RETRIEVAL_FILELIST";
    private static final String FREZING_FILELIST = "FREZING_FILELIST";
    private static final String DISPOSAL_FILELIST = "DISPOSAL_FILELIST";
    private static final String DONATION_FILELIST = "DONATION_FILELIST";

    public Map<String, String> getErrorMap(HttpServletRequest request){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        int fileItemSize = 0;
        Map.Entry<String, File> fileEntry = commonService.getFileEntry(request);
        PageShowFileDto pageShowFileDto = commonService.getPageShowFileDto(fileEntry);
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
            List<OFOCycleStageExcelDto> ofoCycleStageExcelDtoList = commonService.getExcelDtoList(fileEntry, OFOCycleStageExcelDto.class);
            List<OocyteRetrievalExcelDto> oocyteRetrievalExcelDtoList = commonService.getExcelDtoList(fileEntry,OocyteRetrievalExcelDto.class);
            List<FreezingExcelDto> freezingExcelDtoList = commonService.getExcelDtoList(fileEntry, FreezingExcelDto.class);
            List<DisposalExcelDto> disposalExcelDtoList = commonService.getExcelDtoList(fileEntry, DisposalExcelDto.class);
            List<OFODonationStageExcelDto> ofoDonationStageExcelDtoList = commonService.getExcelDtoList(fileEntry, OFODonationStageExcelDto.class);
            fileItemSize = ofoCycleStageExcelDtoList.size();
            if (fileItemSize == 0) {
                errorMap.put("uploadFileError", "PRF_ERR006");
            } else if (fileItemSize > 200) {
                errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                        Formatter.formatNumber(200, "#,##0"), "maxCount"));
            } else {
                Map<String, ExcelPropertyDto> ofoCycleStageFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(OFOCycleStageExcelDto.class);
                Map<String, ExcelPropertyDto> oocyteRetrievalFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(OocyteRetrievalExcelDto.class);
                Map<String, ExcelPropertyDto> freezingFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(FreezingExcelDto.class);
                Map<String, ExcelPropertyDto> disposalFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(DisposalExcelDto.class);
                Map<String, ExcelPropertyDto> ofoDonationStageFieldCellMap = ExcelValidatorHelper.getFieldCellMapWithSheetAt(OFODonationStageExcelDto.class);

                List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
                efoCycleStageDtoList = getOfoCycleStageDtoList(ofoCycleStageExcelDtoList,errorMsgs,ofoCycleStageFieldCellMap,request);
                oocyteRetrievalStageDtoList = getOocyteRetrievalStageList(oocyteRetrievalExcelDtoList,errorMsgs,oocyteRetrievalFieldCellMap,request);
                arSubFreezingStageDtoList = getFreezingStageList(freezingExcelDtoList,errorMsgs,freezingFieldCellMap,request);
                disposalStageDtoList = getDisposalStageList(disposalExcelDtoList,errorMsgs,disposalFieldCellMap,request);
                donationStageDtoList = getDonationStageDtoList(ofoDonationStageExcelDtoList,errorMsgs,ofoDonationStageFieldCellMap,request);

                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(efoCycleStageDtoList, "file", ofoCycleStageFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(oocyteRetrievalStageDtoList, "file", oocyteRetrievalFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(arSubFreezingStageDtoList, "file", freezingFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(disposalStageDtoList, "file", disposalFieldCellMap));
                errorMsgs.addAll(DataSubmissionHelper.validateExcelList(donationStageDtoList, "file", ofoDonationStageFieldCellMap));

                doValid(errorMsgs,efoCycleStageDtoList,ofoCycleStageFieldCellMap,request);
                doValid(errorMsgs,oocyteRetrievalStageDtoList,oocyteRetrievalFieldCellMap,request);
                doValid(errorMsgs,arSubFreezingStageDtoList,freezingFieldCellMap,request);
                doValid(errorMsgs,disposalStageDtoList,disposalFieldCellMap,request);
                doValid(errorMsgs,donationStageDtoList,ofoDonationStageFieldCellMap,request);

                commonService.clearRowIdSession(request);
                if(!errorMsgs.isEmpty()){
                    commonService.getErrorRowInfo(errorMap,request,errorMsgs);
                }
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
        Map<Integer,Boolean> rowIdRes = (Map<Integer,Boolean>) request.getSession().getAttribute("rowIdRes");
        int count = 0;
        for (T item : tDtos){
            count++;
            if(item instanceof EfoCycleStageDto && rowIdRes.get(count)){
                validOFOCycleStage(errorMsgs,(EfoCycleStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof OocyteRetrievalStageDto && rowIdRes.get(count)){
                validOocyteRetrieval(errorMsgs,(OocyteRetrievalStageDto)item,fieldCellMap,count,request);
            }
            if(item instanceof ArSubFreezingStageDto && rowIdRes.get(count)){
                validFreezing(errorMsgs,(ArSubFreezingStageDto) item,fieldCellMap,count,request);
            }
            if(item instanceof DisposalStageDto && rowIdRes.get(count)){
                validDisposal(errorMsgs,(DisposalStageDto) item,fieldCellMap,count,request);
            }
            if(item instanceof DonationStageDto && rowIdRes.get(count)){
                donationStageUploadService.validDonorSample(errorMsgs , (DonationStageDto) item, fieldCellMap, count, request);
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
            commonService.saveRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo());
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            try {
                dto.setStartDate(Formatter.parseDate(excelDto.getDateOfFreezing()));
            }catch (Exception e){
                commonService.validateParseDate(errorMsgs,excelDto.getDateOfFreezing(),fieldCellMap,count,"dateOfFreezing",false);
            }
            dto.setIsMedicallyIndicated(getIntBoolen(excelDto.getIsMedicallyIndicated()));
            dto.setReason(commonService.getMstrKeyByValue(excelDto.getReason(),"EFOR"));
            dto.setOtherReason(excelDto.getFreetextOtherReason());
            dto.setCryopresNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreserved(),"noCryopreserved"));
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
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setIsOvarianSyndrome(getBoolen(excelDto.getSevereOHS()));
            dto.setIsFromPatient(getBoolen(excelDto.getIsOocyteFromPatient()));
            dto.setIsFromPatientTissue(getBoolen(excelDto.getIsOocyteFromPatientsOT()));
            dto.setMatureRetrievedNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedMature(),"noRetrievedMature"));
            dto.setImmatureRetrievedNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedImmature(),"noRetrievedImmature"));
            dto.setOtherRetrievedNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoRetrievedOthers(),"noRetrievedOthers"));
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
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setFreshOocyteCryopNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedFreshOocyte(),"noCryopreservedFreshOocyte"));
            dto.setThawedOocyteCryopNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedThawedOocyte(),"noCryopreservedThawedOocyte"));
            dto.setFreshEmbryoCryopNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedFreshEmbryo(),"noCryopreservedFreshEmbryo"));
            dto.setThawedEmbryoCryopNum(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getNoCryopreservedThawedEmbryo(),"noCryopreservedThawedEmbryo"));
            try {
                dto.setCryopreservedDate(Formatter.parseDate(excelDto.getCryopreservationDate()));
            }catch (Exception e){
                commonService.validateParseDate(errorMsgs,excelDto.getCryopreservationDate(),fieldCellMap,count,"cryopreservationDate",false);
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
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setDisposedType(commonService.getMstrKeyByValue(excelDto.getDisposedItem(),"DISPTY"));
            dto.setImmature(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoImmatureDisposed(),"noImmatureDisposed"));
            dto.setAbnormallyFertilised(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoAbnormallyFertilisedDisposed(),"noAbnormallyFertilisedDisposed"));
            dto.setUnfertilised(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoUnfertilisedDisposed(),"noUnfertilisedDisposed"));
            dto.setAtretic(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoAtreticDisposed(),"noAtreticDisposed"));
            dto.setDamaged(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDamagedDisposed(),"noDamagedDisposed"));
            dto.setLysedOrDegenerated(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoLysedDegeneratedDisposed(),"noLysedDegeneratedDisposed"));
            dto.setUnhealthyNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoPoorQualityUnhealthyAbnormalDisposed(),"noPoorQualityUnhealthyAbnormalDisposed"));
            dto.setOtherDiscardedNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getDiscardedForOtherReasons(),"discardedForOtherReasons"));
            dto.setOtherDiscardedReason(excelDto.getOtherReasonsForDiscarding());
            result.add(dto);
        }
        return result;
    }
    public  List<DonationStageDto> getDonationStageDtoList(List<OFODonationStageExcelDto> donationStageExcelDtos, List<FileErrorMsg> errorMsgs, Map<String, ExcelPropertyDto> fieldCellMap, HttpServletRequest request) {
        if (donationStageExcelDtos == null) {
            return null;
        }
        List<DonationStageDto> result = IaisCommonUtils.genNewArrayList();
        int count = 0;
        for (OFODonationStageExcelDto excelDto : donationStageExcelDtos) {
            count ++;
            DonationStageDto dto = new DonationStageDto();
            commonService.validRowId(request,count,excelDto.getPatientIdType(),excelDto.getPatientIdNo(),errorMsgs,fieldCellMap);
            dto.setLocalOrOversea(getIntBoolen(excelDto.getLocalOrOverseas()));
            dto.setDonatedType(commonService.getMstrKeyByValue(excelDto.getTypeOfSample(),"DONTY"));
            dto.setIsOocyteDonorPatient(getIntBoolen(excelDto.getIsOocyteDonorPatient()));
            dto.setIsFemaleIdentityKnown(getIntBoolen(excelDto.getFemaleIdentityKnown()));
            dto.setFemaleIdType(getIntBoolen(excelDto.getFemaleIdType()));
            dto.setFemaleDonorSampleCode(excelDto.getFemaleSampleCode());
            dto.setFemaleDonorAgeStr(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getFemaleAge(),"femaleAge"));
            dto.setIsSpermDonorPatient(getIntBoolen(excelDto.getIsSpermDonorPatientsHus()));
            dto.setIsMaleIdentityKnown(getIntBoolen(excelDto.getMaleIdentityKnown()));
            dto.setMaleIdType(getIntBoolen(excelDto.getMaleIdType()));
            dto.setMaleIdNumber(excelDto.getMaleIdNo());
            dto.setMaleDonorAgeStr(commonService.excelStrToStrNum(errorMsgs,fieldCellMap,count,excelDto.getMaleAge(),"maleAge"));
            dto.setDonatedCentre(excelDto.getInstitutionFrom());
            dto.setDonationReason(excelDto.getReasonsForDonation());
            dto.setOtherDonationReason(excelDto.getOtherReasonsForDonation());
            dto.setDonatedForTreatment(getIntBoolen(excelDto.getPurposeOfDonation_treatment()));
            dto.setDonatedForResearch(getIntBoolen(excelDto.getPurposeOfDonation_research()));
            dto.setDonatedForTraining(getIntBoolen(excelDto.getPurposeOfDonation_training()));
            dto.setIsDirectedDonation(getIntBoolen(excelDto.getIsDirectedDonation()));
            dto.setTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForTreatment(),"noDonatedForTreatment"));
            dto.setTrainingNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoUsedForTraining(),"noUsedForTraining"));
            dto.setDonResForTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForResearch_useTreatment(),"noDonatedForResearch_useTreatment"));
            dto.setDonResForCurCenNotTreatNum(commonService.excelStrToIntNum(errorMsgs,fieldCellMap,count,excelDto.getNoDonatedForResearch_unUseTreatment(),"nnoDonatedForResearch_unUseTreatment"));
            dto.setDonatedForResearchHescr(getIntBoolen(excelDto.getDonatedForHESCResearch()));
            dto.setDonatedForResearchRrar(getIntBoolen(excelDto.getDonatedForResearchRelatedToAR()));
            dto.setDonatedForResearchOtherType(excelDto.getOtherTypeOfResearch());
            result.add(dto);
        }
        return result;
    }
    private void validOFOCycleStage(List<FileErrorMsg> errorMsgs,EfoCycleStageDto ecDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){
        if(commonService.validateIsNull(errorMsgs,ecDto.getStartDate(),fieldCellMap,i,"dateOfFreezing")){
            commonService.validDateNoFuture(ecDto.getStartDate(),errorMsgs,"dateOfFreezing","(3) Date of Freezing",fieldCellMap,i);
        }
        if(commonService.validateIsNull(errorMsgs,ecDto.getIsMedicallyIndicated(),fieldCellMap,i,"isMedicallyIndicated")){
            if(commonService.validateIsNull(errorMsgs,ecDto.getReason(),fieldCellMap,i,"reason")){
                if(ecDto.getIsMedicallyIndicated() != 1){
                    commonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"reason","(5) Reason",fieldCellMap,i);
                }else {
                    if("EFOR004".equals(ecDto.getReason())){
                        if(commonService.validateIsNull(errorMsgs,ecDto.getOtherReason(),fieldCellMap,i,"freetextOtherReason")){
                            commonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"freetextOtherReason","(6) Reason(Others)",fieldCellMap,i);
                        }
                    }
                }
            }
        }
        if(commonService.validateIsNull(errorMsgs,ecDto.getCryopresNum(),fieldCellMap,i,"noCryopreserved")){
            if(ecDto.getCryopresNum() == 0){
                if(commonService.validateIsNull(errorMsgs,ecDto.getOthers(),fieldCellMap,i,"others")){
                    commonService.validFieldLength(ecDto.getReason().length(),100,errorMsgs,"others","(8) Others",fieldCellMap,i);
                }
            }else {
                if(ecDto.getCryopresNum() < 0){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreserved"), "Can not be negative number"));
                }
            }
        }
    }
    public void validOocyteRetrieval(List<FileErrorMsg> errorMsgs,OocyteRetrievalStageDto orDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){
        commonService.validateIsNull(errorMsgs,orDto.getIsOvarianSyndrome(),fieldCellMap,i,"severeOHS");
        if(orDto.getIsFromPatient() == null && orDto.getIsFromPatientTissue() == null){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteFromPatient"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOocyteFromPatientsOT"), MessageUtil.getMessageDesc("GENERAL_ERR0006")));
        }
        if(StringUtil.isNotEmpty(orDto.getMatureRetrievedNum())){
            if(Integer.parseInt(orDto.getMatureRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedMature"), "Can not be negative number"));
            }
            commonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedMature","(6) No. Retrieved (Mature)",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(orDto.getImmatureRetrievedNum())){
            if(Integer.parseInt(orDto.getImmatureRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedImmature"), "Can not be negative number"));
            }
            commonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedImmature","(7) No. Retrieved (Immature)",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(orDto.getOtherRetrievedNum())){
            if(Integer.parseInt(orDto.getOtherRetrievedNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noRetrievedOthers"), "Can not be negative number"));
            }
            commonService.validFieldLength(orDto.getMatureRetrievedNum().length(),2,
                    errorMsgs,"noRetrievedOthers","(8) No. Retrieved (Others)",fieldCellMap,i);
        }
    }
    public void validFreezing(List<FileErrorMsg> errorMsgs,ArSubFreezingStageDto fzDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){

        PatientIdDto patientId = commonService.getRowId(request,i);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String idType = commonService.convertIdType(patientId.getIdType());
        String idNo = patientId.getIdNo();
        String hciCode = arSuperDataSubmissionDto.getHciCode();
        ArCurrentInventoryDto arCurrentInventoryDto = null;
        try{
            arCurrentInventoryDto = arFeClient.getArCurrentInventoryDtoByPatientIdTypeAndNo(idType,idNo,hciCode).getEntity();
        }catch (Exception e){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        if(arCurrentInventoryDto == null){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        int freshOocyteNum = arCurrentInventoryDto.getFreshOocyteNum();
        int thawedOocyteNum = arCurrentInventoryDto.getThawedOocyteNum();
        int freshEmbryoNum = arCurrentInventoryDto.getFreshEmbryoNum();
        int thawedEmbryoNum = arCurrentInventoryDto.getThawedEmbryoNum();

        if(commonService.validateIsNull(errorMsgs,fzDto.getFreshOocyteCryopNum(),fieldCellMap,i,"noCryopreservedFreshOocyte")){

            if(Integer.parseInt(fzDto.getFreshOocyteCryopNum()) > freshOocyteNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshOocyte"), "Cannot be greater than number of fresh oocytes under patient's inventory currently"));
            }
            if(Integer.parseInt(fzDto.getFreshOocyteCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshOocyte"), "Can not be negative number"));
            }
        }
        if(commonService.validateIsNull(errorMsgs,fzDto.getThawedOocyteCryopNum(),fieldCellMap,i,"noCryopreservedThawedOocyte")){

            if(Integer.parseInt(fzDto.getThawedOocyteCryopNum()) > thawedOocyteNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedOocyte"), "Cannot be greater than number of thawed oocytes under patient's inventory currently"));
            }
            if(Integer.parseInt(fzDto.getThawedOocyteCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedOocyte"), "Can not be negative number"));
            }
        }
        if(commonService.validateIsNull(errorMsgs,fzDto.getFreshEmbryoCryopNum(),fieldCellMap,i,"noCryopreservedFreshEmbryo")){

            if(Integer.parseInt(fzDto.getFreshEmbryoCryopNum()) > freshEmbryoNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshEmbryo"), "Cannot be greater than number of fresh embryo under patient's inventory currently"));
            }
            if(Integer.parseInt(fzDto.getFreshEmbryoCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedFreshEmbryo"), "Can not be negative number"));
            }
        }
        if(commonService.validateIsNull(errorMsgs,fzDto.getThawedEmbryoCryopNum(),fieldCellMap,i,"noCryopreservedThawedEmbryo")){

            if(Integer.parseInt(fzDto.getThawedEmbryoCryopNum()) > thawedEmbryoNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedEmbryo"), "Cannot be greater than number of thawed embryo under patient's inventory currently"));
            }
            if(Integer.parseInt(fzDto.getThawedEmbryoCryopNum()) < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noCryopreservedThawedEmbryo"), "Can not be negative number"));
            }
        }
        if(commonService.validateIsNull(errorMsgs,fzDto.getCryopreservedDate(),fieldCellMap,i,"cryopreservationDate")){
            commonService.validDateNoFuture(fzDto.getCryopreservedDate(),errorMsgs,"cryopreservationDate","(7) Cryopreservation Date",fieldCellMap,i);
        }
    }
    private int getInventoryNum(String type, Map<String, Integer> inventoryMap){
        switch (type){
            case "DISPTY001":
                return inventoryMap.get("freshOocyteNum");
            case "DISPTY002":
                return inventoryMap.get("frozenOocyteNum");
            case "DISPTY003":
                return inventoryMap.get("thawedOocyteNum");
            case "DISPTY004":
                return inventoryMap.get("freshEmbryoNum");
            case "DISPTY005":
                return inventoryMap.get("frozenEmbryoNum");
            case "DISPTY006":
                return inventoryMap.get("thawedEmbryoNum");
            case "DISPTY007":
                return inventoryMap.get("frozenSpermNum");
            case "DISPTY008":
                return inventoryMap.get("freshSpermNum");
            default:
                return 0;
        }
    }
    public void validDisposal(List<FileErrorMsg> errorMsgs,DisposalStageDto disDto,Map<String, ExcelPropertyDto> fieldCellMap,int i,HttpServletRequest request){

        commonService.validateIsNull(errorMsgs,disDto.getDisposedType(),fieldCellMap,i,"disposedItem");

        PatientIdDto patientId = commonService.getRowId(request,i);
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String idType = commonService.convertIdType(patientId.getIdType());
        String idNo = patientId.getIdNo();
        String hciCode = arSuperDataSubmissionDto.getHciCode();
        ArCurrentInventoryDto arCurrentInventoryDto = null;
        try{
            arCurrentInventoryDto = arFeClient.getArCurrentInventoryDtoByPatientIdTypeAndNo(idType,idNo,hciCode).getEntity();
        }catch (Exception e){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        if(arCurrentInventoryDto == null){
            arCurrentInventoryDto = new ArCurrentInventoryDto();
        }
        Map<String, Integer> inventoryMap = IaisCommonUtils.genNewHashMap();

        inventoryMap.put("freshOocyteNum", arCurrentInventoryDto.getFreshOocyteNum());
        inventoryMap.put("frozenOocyteNum", arCurrentInventoryDto.getFrozenOocyteNum());
        inventoryMap.put("thawedOocyteNum", arCurrentInventoryDto.getThawedOocyteNum());
        inventoryMap.put("freshEmbryoNum", arCurrentInventoryDto.getFreshEmbryoNum());
        inventoryMap.put("frozenEmbryoNum", arCurrentInventoryDto.getFrozenEmbryoNum());
        inventoryMap.put("thawedEmbryoNum", arCurrentInventoryDto.getThawedEmbryoNum());
        inventoryMap.put("freshSpermNum", arCurrentInventoryDto.getFreshSpermNum());
        inventoryMap.put("frozenSpermNum", arCurrentInventoryDto.getFrozenSpermNum());

        String type = disDto.getDisposedType();
        String[] strs = {"DISPTY001","DISPTY002","DISPTY003"};
        if(StringUtil.isIn(type,strs)){
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noImmatureDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noAbnormallyFertilisedDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noUnfertilisedDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noAtreticDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noDamagedDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"noLysedDegeneratedDisposed");
            commonService.validateIsNull(errorMsgs,disDto.getImmature(),fieldCellMap,i,"discardedForOtherReasons");
        }

        if(StringUtil.isNotEmpty(disDto.getImmature())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getImmature() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noImmatureDisposed"), msg));
            }
            if(disDto.getImmature() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noImmatureDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getImmature()).length(),2,errorMsgs,"noImmatureDisposed","(4) No. of Immature Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getAbnormallyFertilised())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getAbnormallyFertilised() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAbnormallyFertilisedDisposed"), msg));
            }
            if(disDto.getAbnormallyFertilised() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAbnormallyFertilisedDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getAbnormallyFertilised()).length(),2,errorMsgs,"noAbnormallyFertilisedDisposed","(5) No. of Abnormally Fertilised Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getUnfertilised())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getUnfertilised() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUnfertilisedDisposed"), msg));
            }
            if(disDto.getUnfertilised() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noUnfertilisedDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getUnfertilised()).length(),2,errorMsgs,"noUnfertilisedDisposed","(6) No. of Unfertilised Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getAtretic())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getAtretic() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAtreticDisposed"), msg));
            }
            if(disDto.getAtretic() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noAtreticDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getAtretic()).length(),2,errorMsgs,"noAtreticDisposed","(7) No. of Atretic Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getDamaged())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getDamaged() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDamagedDisposed"), msg));
            }
            if(disDto.getDamaged() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noDamagedDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getDamaged()).length(),2,errorMsgs,"noDamagedDisposed","(8) No. of Damaged Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getLysedOrDegenerated())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getLysedOrDegenerated() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLysedDegeneratedDisposed"), msg));
            }
            if(disDto.getLysedOrDegenerated() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noLysedDegeneratedDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getLysedOrDegenerated()).length(),2,errorMsgs,"noLysedDegeneratedDisposed","(9) No. of Lysed/ Degenerated Disposed",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getUnhealthyNum())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getUnhealthyNum() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noPoorQualityUnhealthyAbnormalDisposed"), msg));
            }
            if(disDto.getUnhealthyNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("noPoorQualityUnhealthyAbnormalDisposed"), "Can not be negative number"));
            }
            commonService.validFieldLength(String.valueOf(disDto.getUnhealthyNum()).length(),2,errorMsgs,"noPoorQualityUnhealthyAbnormalDisposed","(10) No. of Poor Quality/Unhealthy/Abnormal Discarded",fieldCellMap,i);
        }
        if(StringUtil.isNotEmpty(disDto.getOtherDiscardedNum())){
            int inventoryNum = getInventoryNum(disDto.getDisposedType(),inventoryMap);
            String msg = "Cannot be greater than number of " + commonService.getMstrKeyByValue(disDto.getDisposedType(), "DISPTY") + " under patient's inventory currently";
            if(disDto.getOtherDiscardedNum() > inventoryNum){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), msg));
            }
            if(disDto.getOtherDiscardedNum() < 0){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("discardedForOtherReasons"), "Can not be negative number"));
            }
            if(disDto.getOtherDiscardedNum() > 0){
                if(commonService.validateIsNull(errorMsgs,disDto.getOtherDiscardedReason(),fieldCellMap,i,"otherReasonsForDiscarding")){
                    commonService.validFieldLength(disDto.getOtherDiscardedReason().length(),100,errorMsgs,"otherReasonsForDiscarding","(13) Other reasons for Discarding",fieldCellMap,i);
                }
            }
            commonService.validFieldLength(String.valueOf(disDto.getUnhealthyNum()).length(),2,errorMsgs,"discardedForOtherReasons","(12) Discarded for Other Reasons",fieldCellMap,i);
        }

    }
    public void saveOFOCycleFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto){
        log.info(StringUtil.changeForLog("----- ofo cycle file is saving -----"));
        List<EfoCycleStageDto> efoCycleStageDtos = (List<EfoCycleStageDto>) request.getSession().getAttribute(OFO_CYCLE_STAGE_FILELIST);
        List<OocyteRetrievalStageDto> oocyteRetrievalStageDtos = (List<OocyteRetrievalStageDto>) request.getSession().getAttribute(OOCYTE_RETRIEVAL_FILELIST);
        List<ArSubFreezingStageDto> freezingStageDtos = (List<ArSubFreezingStageDto>) request.getSession().getAttribute(FREZING_FILELIST);
        List<DisposalStageDto> disposalStageDtos = (List<DisposalStageDto>) request.getSession().getAttribute(DISPOSAL_FILELIST);
        List<DonationStageDto> donationStageDtos = (List<DonationStageDto>) request.getSession().getAttribute(DONATION_FILELIST);
        if (efoCycleStageDtos == null || efoCycleStageDtos.isEmpty()) {
            log.warn(StringUtil.changeForLog("----- No Data to be submitted -----"));
            return;
        }
//        boolean useParallel = iuiCycleStageDtos.size() >= AppConsts.DFT_MIN_PARALLEL_SIZE;
        String declaration = arSuperDto.getDataSubmissionDto().getDeclaration();
        List<ArSuperDataSubmissionDto> arSuperList = IaisCommonUtils.genNewArrayList();
        for(int i = 0; i < efoCycleStageDtos.size(); i ++){
            arSuperList.add(getArSuperDataSubmissionDto(request, arSuperDto, declaration,
                    efoCycleStageDtos.get(i),
                    oocyteRetrievalStageDtos.get(i),
                    freezingStageDtos.get(i),
                    disposalStageDtos.get(i),
                    donationStageDtos.get(i)));
        }
        arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoList(arSuperList);
        try {
            arSuperList = arDataSubmissionService.saveArSuperDataSubmissionDtoListToBE(arSuperList);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_LIST, (Serializable) arSuperList);
        clearFlieListSession(request);
    }
    private ArSuperDataSubmissionDto getArSuperDataSubmissionDto(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto, String declaration,
                                                                 EfoCycleStageDto efoCycleStageDto,
                                                                 OocyteRetrievalStageDto oocyteRetrievalStageDto,
                                                                 ArSubFreezingStageDto freezingStageDto,
                                                                 DisposalStageDto disposalStageDto,
                                                                 DonationStageDto donationStageDto) {
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
//        dataSubmissionDto.setCycleStage(DataSubmissionConsts.ALL);
        newDto.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setEfoCycleStageDto(efoCycleStageDto);
        newDto.setOocyteRetrievalStageDto(oocyteRetrievalStageDto);
        newDto.setArSubFreezingStageDto(freezingStageDto);
        newDto.setDisposalStageDto(disposalStageDto);
        newDto.setDonationStageDto(donationStageDto);
        return newDto;
    }
    private void clearFlieListSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(OFO_CYCLE_STAGE_FILELIST);
        session.removeAttribute(OOCYTE_RETRIEVAL_FILELIST);
        session.removeAttribute(FREZING_FILELIST);
        session.removeAttribute(DISPOSAL_FILELIST);
        session.removeAttribute(DONATION_FILELIST);
    }
}
