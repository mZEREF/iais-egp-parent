package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArCycleBatchUploadService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArCycleBatchUploadServiceImpl implements ArCycleBatchUploadService {
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    @Autowired
    private ArBatchUploadCommonService arBatchUploadCommonService;
    private static final String FILE_ITEM_SIZE = "fileItemSize";

    private static final String AR_CYCLE_STAGE_LIST = "AR_CYCLE_STAGE_LIST";
    private static final String THAWING_STAGE_LIST = "THAWING_STAGE_LIST";
    private static final String AR_OOCYTE_RETRIEVAL_STAGE_LIST = "AR_OOCYTE_RETRIEVAL_STAGE_LIST";
    private static final String FERTILISATION_STAGE_LIST = "FERTILISATION_STAGE_LIST";
    private static final String EMBRYO_CREATE_STAGE_LIST = "EMBRYO_CREATE_STAGE_LIST";
    private static final String EMBRYO_TRANSFER_STAGE_LIST = "EMBRYO_TRANSFER_STAGE_LIST";
    private static final String OUTCOME_TRANSFER_STAGE_LIST = "OUTCOME_TRANSFER_STAGE_LIST";
    private static final String AR_OUTCOME_PREGNANCY_STAGE_LIST = "AR_OUTCOME_PREGNANCY_STAGE_LIST";
    private static final String END_CYCLE_STAGE_LIST = "END_CYCLE_STAGE_LIST";
    private static final String PGT_STAGE_LIST = "PGT_STAGE_LIST";
    private static final String AR_CO_FUNDING_STAGE_LIST = "AR_CO_FUNDING_STAGE_LIST";
    private static final String AR_DONATION_STAGE_LIST = "AR_DONATION_STAGE_LIST";
    private static final String AR_TRANSFER_INOUT_STAGE_LIST = "AR_TRANSFER_INOUT_STAGE_LIST";
    private static final String AR_DISPOSAL_STAGE_LIST = "AR_DISPOSAL_STAGE_LIST";





    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;




    @Override
    public Map<String, String> preBatchUpload(BaseProcessClass bpc, Map<String, String> errorMap) {

        List<ArCycleStageDto> arCycleStageDtoDtos = (List<ArCycleStageDto>) bpc.request.getSession().getAttribute(AR_CYCLE_STAGE_LIST);
        List<ThawingStageDto> thawingStageDtos = (List<ThawingStageDto>) bpc.request.getSession().getAttribute(THAWING_STAGE_LIST);
        List<OocyteRetrievalStageDto> oocyteRetrievalStageDtos = (List<OocyteRetrievalStageDto>) bpc.request.getSession().getAttribute(AR_OOCYTE_RETRIEVAL_STAGE_LIST);
        List<FertilisationDto> fertilisationDtos = (List<FertilisationDto>) bpc.request.getSession().getAttribute(FERTILISATION_STAGE_LIST);
        List<EmbryoCreatedStageDto> embryoCreatedStageDtos = (List<EmbryoCreatedStageDto>) bpc.request.getSession().getAttribute(EMBRYO_CREATE_STAGE_LIST);
        List<EmbryoTransferStageDto> embryoTransferStageDtos = (List<EmbryoTransferStageDto>) bpc.request.getSession().getAttribute(EMBRYO_TRANSFER_STAGE_LIST);
        List<EmbryoTransferredOutcomeStageDto> embryoTransferredOutcomeStageDtos = (List<EmbryoTransferredOutcomeStageDto>) bpc.request.getSession().getAttribute(OUTCOME_TRANSFER_STAGE_LIST);
        List<PregnancyOutcomeStageDto> pregnancyOutcomeStageDtos = (List<PregnancyOutcomeStageDto>) bpc.request.getSession().getAttribute(AR_OUTCOME_PREGNANCY_STAGE_LIST);
        List<EndCycleStageDto> endCycleStageDtos = (List<EndCycleStageDto>) bpc.request.getSession().getAttribute(END_CYCLE_STAGE_LIST);
        List<PgtStageDto> pgtStageDtos = (List<PgtStageDto>) bpc.request.getSession().getAttribute(PGT_STAGE_LIST);
        List<ArTreatmentSubsidiesStageDto> arTreatmentSubsidiesStageDtos = (List<ArTreatmentSubsidiesStageDto>) bpc.request.getSession().getAttribute(AR_CO_FUNDING_STAGE_LIST);
        List<DonationStageDto> donationStageDtos = (List<DonationStageDto>) bpc.request.getSession().getAttribute(AR_DONATION_STAGE_LIST);
        List<TransferInOutStageDto> transferInOutStageDtos = (List<TransferInOutStageDto>) bpc.request.getSession().getAttribute(AR_TRANSFER_INOUT_STAGE_LIST);
        List<DisposalStageDto> disposalStageDtos = (List<DisposalStageDto>) bpc.request.getSession().getAttribute(AR_DISPOSAL_STAGE_LIST);

        if (arCycleStageDtoDtos == null){
            Map.Entry<String, File> fileEntry = arBatchUploadCommonService.getFileEntry(bpc.request);
            PageShowFileDto pageShowFileDto = arBatchUploadCommonService.getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(bpc.request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, bpc.request);
            if (errorMap.isEmpty()) {
                //1. excelDto validation

                //2. dto validation
                List<ArCycleStageExcelDto> arCycleStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArCycleStageExcelDto.class);
                List<ThawingStageExcelDto> thawingStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ThawingStageExcelDto.class);
                List<ArOocyteRetrievalExcelDto> oocyteRetrievalExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArOocyteRetrievalExcelDto.class);
                List<FertilisationStageExcelDto> fertilisationStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, FertilisationStageExcelDto.class);
                List<EmbryoCreatedExcelDto> embryoCreatedExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, EmbryoCreatedExcelDto.class);
                List<EmbryoTransferExcelDto> embryoTransferExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, EmbryoTransferExcelDto.class);
                List<OutcomeTransferExcelDto> outcomeTransferExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, OutcomeTransferExcelDto.class);
                List<ArOutcomePregnancyExcelDto> outcomeOfPregnancyExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArOutcomePregnancyExcelDto.class);
                List<EndCycleStageExcelDto> endCycleStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry,EndCycleStageExcelDto.class);
                List<PgtStageExcelDto> pgtStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, PgtStageExcelDto.class);
                List<ArCoFundingExcelDto> arCoFundingExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArCoFundingExcelDto.class);
                List<DonationStageExcelDto> donationStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, DonationStageExcelDto.class);
                List<ArTransferInOutExcelDto> arTransferInOutExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArTransferInOutExcelDto.class);
                List<ArFreezingStageExcelDto> arFreezingStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArFreezingStageExcelDto.class);
                List<ArDisposalStageExcelDto> arDisposalStageExcelDtos = arBatchUploadCommonService.getExcelDtoList(fileEntry, ArDisposalStageExcelDto.class);

                int arFileItemSize = arCycleStageExcelDtos.size();
                int thawingFileItemSize = thawingStageExcelDtos.size();

                int fertilisationFileItemSize = fertilisationStageExcelDtos.size();
                int embryoCreatedFileItemSize = embryoCreatedExcelDtos.size();
                int embryoTransferFileItemSize = embryoTransferExcelDtos.size();
                if (arFileItemSize == 0) {
                    errorMap.put("uploadFileError", "PRF_ERR006");
                } else if (arFileItemSize > 200) {
                    errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                            Formatter.formatNumber(200, "#,##0"), "maxCount"));
                } else {
                    if (!validateArExcelDto(arCycleStageExcelDtos, errorMap).isEmpty()) {
                        return errorMap;
                    }
                    List<ArCycleStageDto> arCycleStageDtos = setArCycleStageDto(arCycleStageExcelDtos, bpc.request);
                    thawingStageDtos = getThawingStageDto(thawingStageExcelDtos);
                    // OocyteRetrieval
                    fertilisationDtos = getFertilisationDto(fertilisationStageExcelDtos);
                    embryoCreatedStageDtos = getEmbryoCreatedStageDto(embryoCreatedExcelDtos);
                    getEmbryoTransferStageDto(embryoTransferExcelDtos);
                    getTransferOutcomeDtos(outcomeTransferExcelDtos);
                    // outcome pregenancy
                    getEndCycleStageDto(endCycleStageExcelDtos);
                    // pgt getPgtStageDto(pgtStageExcelDtos);
                    getArCoFundingDto(arCoFundingExcelDtos);
                    Map<String, ExcelPropertyDto> arFieldCellMap = ExcelValidatorHelper.getFieldCellMap(ArCycleStageExcelDto.class);
                    List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(arCycleStageDtoDtos, "file", arFieldCellMap);
                    for (int i = 1; i <= arFileItemSize; i++) {
                        ArCycleStageDto arDto=arCycleStageDtoDtos.get(i-1);
                        validateArStage(errorMsgs, arDto, arFieldCellMap, i);
                    }
                    for (int i = 1;i <= thawingFileItemSize; i++) {
                        ThawingStageDto thawingStageDto = thawingStageDtos.get(i-1);
                        validateThawingStage(errorMsgs, thawingStageDto ,arFieldCellMap,i);
                    }
                    if (!errorMsgs.isEmpty()) {

                    }
                }
            }
        }
        return errorMap;
    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {

    }

    private Map<String, String> validateArExcelDto(List<ArCycleStageExcelDto> arCycleStageExcelDtos, Map<String, String> errorMap) {
        for (ArCycleStageExcelDto excelDto: arCycleStageExcelDtos) {
            if (StringUtil.isEmpty(excelDto.getPatientIdType())) {
                errorMap.put("itemError","Please key in ID Type");
                return errorMap;
            }
            if (StringUtil.isEmpty(excelDto.getPatientIdNo())) {
                errorMap.put("itemError","Please key in ID No.");
                return errorMap;
            }
        }
        return errorMap;
    }

    private void validateArStage(List<FileErrorMsg> errorMsgs,ArCycleStageDto arCycleStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        arBatchUploadCommonService.validateParseDate(errorMsgs, arCycleStageDto.getStartDate(),fieldCellMap,i, "startDate");

        if (StringUtil.isEmpty(arCycleStageDto.getMainIndication())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("mainIndication"), errMsgErr006));
        }
        if (DataSubmissionConsts.AR_MAIN_INDICATION_OTHERS.equals(arCycleStageDto.getMainIndication())) {
            if(StringUtil.isEmpty(arCycleStageDto.getMainIndicationOthers())) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("mainIndicationOthers"), errMsgErr006));
            } else if (arCycleStageDto.getMainIndicationOthers().length()>100){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Main Indication (Others)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("mainIndicationOthers"), errMsg));
            }
        }
        if (StringUtil.isEmpty(arCycleStageDto.getOtherIndication()) && StringUtil.stringContain(arCycleStageDto.getOtherIndication(),DataSubmissionConsts.AR_OTHER_INDICATION_OTHERS )
                && StringUtil.isEmpty(arCycleStageDto.getOtherIndicationOthers())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherIndicationText"), errMsgErr006));
        }
        if (StringUtil.isEmpty(arCycleStageDto.getCurrentMarriageChildren())) { // need to change dto int to string

        }
        if (StringUtil.isEmpty(arCycleStageDto.getDeliveredThroughChildren())) {

        }
        if (StringUtil.isEmpty(arCycleStageDto.getTotalPreviouslyPreviously())) {

        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getCyclesUndergoneOverseas())) {
            try {
                Integer.parseInt(arCycleStageDto.getCyclesUndergoneOverseas());
                if (arCycleStageDto.getCyclesUndergoneOverseas().length() > 2) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","No. of AR Cycles undergone Overseas");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cyclesUndergoneOverseas"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cyclesUndergoneOverseas"), errMsgErr002));
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cyclesUndergoneOverseas"), errMsgErr006));
        }
        // todo caculate age
        // Date startDate = DateUtil.parseDate(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
        if (((arCycleStageDto.getCycleAgeYear() > 45 || arCycleStageDto.getCycleAgeYear() == 45 && arCycleStageDto.getCycleAgeMonth() > 0)
                || arCycleStageDto.getCountForEnhancedCounselling() >10) && AppConsts.FALSE.equals(arCycleStageDto.getEnhancedCounselling())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("enhancedCounselling"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getPractitioner()) && arCycleStageDto.getPractitioner().length()>10) {
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","10");
            repMap.put("fieldNo","AR Practitioner");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("practitioner"), errMsg));
        }else if(StringUtil.isEmpty(arCycleStageDto.getPractitioner())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("practitioner"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getEmbryologist()) && (!arCycleStageDto.getEmbryologist().equals("et01") || !arCycleStageDto.getEmbryologist().equals("Not-Applicable"))) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("practitioner"), "Please key in valid message"));
        }
        if (arCycleStageDto.isUsedDonorOocyte()) {
            // todo validate donorDtos
        }
    }

    private void validateThawingStage(List<FileErrorMsg> errorMsgs,ThawingStageDto thawingStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory

        if (thawingStageDto.getHasOocyte() == null) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("hasOocyte"), errMsgErr006));
        }
        if (thawingStageDto.getHasEmbryo() == null) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("hasEmbryo"), errMsgErr006));
        }


    }
    
//    private boolean validateExcelDto(List<ArCycleStageExcelDto> arCycleStageExcelDtos) {
//        if(arCycleStageExcelDtos == null) {
//            return false;
//        }
//        for (ArCycleStageExcelDto excelDto: arCycleStageExcelDtos) {
//            return excelDto.filledMandatory() && ;
//        }
//        return true;
//    }



    private List<ArCycleStageDto> setArCycleStageDto(List<ArCycleStageExcelDto> arCycleStageExcelDtos,HttpServletRequest request) {
        if(arCycleStageExcelDtos == null) {
            return null;
        }
        List<ArCycleStageDto> result = IaisCommonUtils.genNewArrayList(arCycleStageExcelDtos.size());
        List<SelectOption> options = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.AR_MAIN_INDICATION);
        for (ArCycleStageExcelDto excelDto: arCycleStageExcelDtos) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDto = new ArSuperDataSubmissionDto();
            PatientInfoDto patientInfoDto = arBatchUploadCommonService.setPatientInfo(excelDto.getPatientIdType(), excelDto.getPatientIdNo(), request);
            ArCycleStageDto arCycleStageDto = new ArCycleStageDto();
            DonorDto donorDto1 = new DonorDto();
            DonorDto donorDto2 = new DonorDto();
            arCycleStageDto.setStartDate(excelDto.getStartDate());
            for (SelectOption opt: options) {
                if (opt.getText().equals(excelDto.getMainIndication())) {
                    arCycleStageDto.setMainIndication(opt.getValue());
                }
            }
            arCycleStageDto.setMainIndicationOthers(excelDto.getMainIndicationOthers());
            List<String> otherIndications = IaisCommonUtils.genNewArrayList();
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationAdv())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_ADVANCE_MATERNAL_AGE);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationEnd())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_ENDOMETRIOSIS);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationFail())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_FAILED_REPEATED_IUIS);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationInd())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_IMMUNE_FACTOR);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationLow())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_LOW_OVARIAN_RESERVE);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationMale())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_MALE_FACTOR);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationPol())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_POLYCYSTIC_OVARIAN_DISEASE);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationPrei())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_PREIMPLANTATION_GENETIC_TESTING);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationPrem())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_PREMATURE_OVARIAN_FAILURE);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationPre())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_PREVIOUS_TUBAL_LIGATION);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationTubal())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_TUBAL_DISEASE_ANDOR_OBSTRUCTION);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationUnexplained())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_UNEXPLAINED_SUBFERTILITY);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getOtherIndicationOthers())) {
                otherIndications.add(DataSubmissionConsts.AR_OTHER_INDICATION_OTHERS);
                arCycleStageDto.setOtherIndicationOthers(excelDto.getOtherIndicationText());
            }
            arCycleStageDto.setOtherIndication(otherIndications.toString());
            arCycleStageDto.setInVitroMaturation(excelDto.getBooleanValue(excelDto.getInVitroMaturation()));
            arCycleStageDto.setTreatmentFreshNatural(excelDto.getBooleanValue(excelDto.getTreatmentFreshNatural()));
            arCycleStageDto.setTreatmentFreshStimulated(excelDto.getBooleanValue(excelDto.getTreatmentFreshStimulated()));
            arCycleStageDto.setTreatmentFrozenOocyte(excelDto.getBooleanValue(excelDto.getTreatmentFrozenOocyte()));
            arCycleStageDto.setTreatmentFrozenEmbryo(excelDto.getBooleanValue(excelDto.getTreatmentFrozenEmbryo()));
            String currentMarriageChildren = StringUtils.substringBefore(excelDto.getCurrentMarriageChildren(),".");
            String previousMarriageChildren = StringUtils.substringBefore(excelDto.getPreviousMarriageChildren(),".");
            String deliveredThroughChildren = StringUtils.substringBefore(excelDto.getDeliveredThroughChildren(),".");
            String totalPreviouslyPreviously = StringUtils.substringBefore(excelDto.getTotalPreviouslyPreviously(),".");
            arCycleStageDto.setCurrentMarriageChildren(Integer.parseInt(currentMarriageChildren));
            arCycleStageDto.setPreviousMarriageChildren(Integer.parseInt(previousMarriageChildren));
            arCycleStageDto.setDeliveredThroughChildren(Integer.parseInt(deliveredThroughChildren));
            arCycleStageDto.setTotalPreviouslyPreviously(Integer.parseInt(totalPreviouslyPreviously));
            arCycleStageDto.setCyclesUndergoneOverseas(StringUtils.substringBefore(excelDto.getCyclesUndergoneOverseas(),"."));

            if ("Yes".equals(excelDto.getEnhancedCounselling())) {
                arCycleStageDto.setEnhancedCounselling(true);
            } else if ("No".equals(excelDto.getEnhancedCounselling())) {
                arCycleStageDto.setEnhancedCounselling(false);
            }

            arCycleStageDto.setPractitioner(excelDto.getPractitioner());

            arCycleStageDto.setEmbryologist(excelDto.getEmbryologist());
            if ("Yes".equals(excelDto.getUsedDonorOocyte())) {
                arCycleStageDto.setUsedDonorOocyte(true);
                setDonorDto(excelDto, donorDto1, donorDto2);
            } else if ("No".equals(excelDto.getUsedDonorOocyte())) {
                arCycleStageDto.setUsedDonorOocyte(false);
            }
        }
        return result;
    }

    private List<ThawingStageDto> getThawingStageDto(List<ThawingStageExcelDto> thawingStageExcelDtos) {
        if (thawingStageExcelDtos == null) {
            return null;
        }
        List<ThawingStageDto> result = IaisCommonUtils.genNewArrayList(thawingStageExcelDtos.size());
        for (ThawingStageExcelDto excelDto: thawingStageExcelDtos) {
            ThawingStageDto thawingStageDto = new ThawingStageDto();
            if (StringUtil.isNotEmpty(excelDto.getThawedOocytes())) {
                thawingStageDto.setHasOocyte(arBatchUploadCommonService.getBooleanValue(excelDto.getThawedOocytes()));
            }
            if (StringUtil.isNotEmpty(excelDto.getThawingEmbryos())) {
                thawingStageDto.setHasEmbryo(arBatchUploadCommonService.getBooleanValue(excelDto.getThawingEmbryos()));
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getThawedOocytes()) == true) {
                thawingStageDto.setThawedOocytesNum(StringUtils.substringBefore(excelDto.getThawedOocytesNum(),"."));
                thawingStageDto.setThawedOocytesSurvivedMatureNum(StringUtils.substringBefore(excelDto.getThawedOocytesSurvivedMatureNum(),"."));
                thawingStageDto.setThawedOocytesSurvivedImmatureNum(StringUtils.substringBefore(excelDto.getThawedOocytesSurvivedImmatureNum(),"."));
                thawingStageDto.setThawedOocytesSurvivedOtherNum(StringUtils.substringBefore(excelDto.getThawedOocytesSurvivedOtherNum(),"."));
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getThawingEmbryos()) == true) {
                thawingStageDto.setThawedEmbryosNum(StringUtils.substringBefore(excelDto.getThawedEmbryosNum(),"."));
                thawingStageDto.setThawedEmbryosSurvivedNum(StringUtils.substringBefore(excelDto.getThawedEmbryosSurvivedNum(),"."));
            }
            result.add(thawingStageDto);
        }
        return result;
    }

    private List<FertilisationDto> getFertilisationDto(List<FertilisationStageExcelDto> fertilisationStageExcelDtos) {
        if (fertilisationStageExcelDtos == null) {
            return null;
        }
        List<FertilisationDto> result = IaisCommonUtils.genNewArrayList(fertilisationStageExcelDtos.size());
        for (FertilisationStageExcelDto excelDto: fertilisationStageExcelDtos) {
            FertilisationDto fertilisationDto = new FertilisationDto();
            fertilisationDto.setSourceOfOocyte(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocyte()));
            fertilisationDto.setSourceOfOocytePatient(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocytePatient()));
            fertilisationDto.setSourceOfOocytePot(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocytePot()));
            fertilisationDto.setOocyteUsed(excelDto.getOocyteUsed());
            fertilisationDto.setUsedOocytesNum(StringUtils.substringBefore(excelDto.getUsedOocytesNum(),"."));

            fertilisationDto.setFromHusband(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenHus()));
            fertilisationDto.setFromHusbandTissue(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenHusTes()));
            fertilisationDto.setFromDonor(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenDon()));
            fertilisationDto.setFromDonorTissue(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenDonTes()));
            fertilisationDto.setOocyteUsed(excelDto.getFreshOrFrozen());
            fertilisationDto.setExtractedSpermVialsNum(StringUtils.substringBefore(excelDto.getExtractedSpermVialsNum(),"."));
            fertilisationDto.setUsedSpermVialsNum(StringUtils.substringBefore(excelDto.getUsedSpermVialsNum(),"."));

            fertilisationDto.setIvfUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getIfvUsed()));
            fertilisationDto.setIcsiUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getIcsiUsed()));
            fertilisationDto.setGiftUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getGiftUsed()));
            fertilisationDto.setZiftUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getZiftUsed()));
            fertilisationDto.setFreshOocytesInseminatedNum(StringUtils.substringBefore(excelDto.getFreshOocytesInseminatedNum(),"."));
            fertilisationDto.setThawedOocytesInseminatedNum(StringUtils.substringBefore(excelDto.getThawedOocytesInseminatedNum(),"."));
            fertilisationDto.setFreshOocytesMicroInjectedNum(StringUtils.substringBefore(excelDto.getFreshOocytesMicroInjectedNum(),"."));
            fertilisationDto.setThawedOocytesMicroinjectedNum(StringUtils.substringBefore(excelDto.getThawedOocytesMicroInjectedNum(),"."));
            fertilisationDto.setFreshOocytesGiftNum(StringUtils.substringBefore(excelDto.getFreshOocytesGiftNum(),"."));
            fertilisationDto.setThawedOocytesGiftNum(StringUtils.substringBefore(excelDto.getThawedOocytesGiftNum(),"."));
            fertilisationDto.setFreshOocytesZiftNum(StringUtils.substringBefore(excelDto.getFreshOocytesZiftNum(),"."));
            fertilisationDto.setThawedOocytesZiftNum(StringUtils.substringBefore(excelDto.getThawedOocytesZiftNum(),"."));
            result.add(fertilisationDto);
        }
        return result;
    }

    private List<EmbryoCreatedStageDto> getEmbryoCreatedStageDto(List<EmbryoCreatedExcelDto> embryoCreatedExcelDtos) {
        if (embryoCreatedExcelDtos == null) {
            return null;
        }
        List<EmbryoCreatedStageDto> result = IaisCommonUtils.genNewArrayList(embryoCreatedExcelDtos.size());
        for (EmbryoCreatedExcelDto excelDto: embryoCreatedExcelDtos) {
            EmbryoCreatedStageDto embryoCreatedStageDto = new EmbryoCreatedStageDto();
            embryoCreatedStageDto.setTransEmbrFreshOccNum(Integer.parseInt(StringUtils.substringBefore(excelDto.getTransEmbrFreshOccNum(),".")));
            embryoCreatedStageDto.setPoorDevFreshOccNum(Integer.parseInt(StringUtils.substringBefore(excelDto.getPoorDevFreshOccNum(),".")));
            embryoCreatedStageDto.setTransEmbrThawOccNum(Integer.parseInt(StringUtils.substringBefore(excelDto.getTransEmbrThawOccNum(),".")));
            embryoCreatedStageDto.setPoorDevThawOccNum(Integer.parseInt(StringUtils.substringBefore(excelDto.getPoorDevThawOccNum(),".")));
            result.add(embryoCreatedStageDto);
        }
        return result;
    }

    private List<EmbryoTransferStageDto> getEmbryoTransferStageDto(List<EmbryoTransferExcelDto> embryoTransferExcelDtos){
        if (embryoTransferExcelDtos == null) {
            return null;
        }
        List<EmbryoTransferStageDto> result = IaisCommonUtils.genNewArrayList(embryoTransferExcelDtos.size());
        for (EmbryoTransferExcelDto excelDto: embryoTransferExcelDtos) {
            EmbryoTransferStageDto embryoTransferStageDto = new EmbryoTransferStageDto();
            int transferNum = Integer.parseInt(StringUtils.substringBefore(excelDto.getTransferNum(),"."));
            embryoTransferStageDto.setTransferNum(transferNum);
            List<EmbryoTransferDetailDto> detailDtos = IaisCommonUtils.genNewArrayList(transferNum);
            for (int i = 1; i<=10; i++) {
                EmbryoTransferDetailDto detailDto = new EmbryoTransferDetailDto();
                detailDto.setSeqNumber(i);
                // todo type and age
                detailDtos.add(detailDto);
            }

            embryoTransferStageDto.setEmbryoTransferDetailDtos(detailDtos);
            result.add(embryoTransferStageDto);
        }
        return result;
    }

    private List<EmbryoTransferredOutcomeStageDto> getTransferOutcomeDtos(List<OutcomeTransferExcelDto> outcomeTransferExcelDtos) {
        if (outcomeTransferExcelDtos == null) {
            return null;
        }
        List<SelectOption> options = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.OUTCOME_OF_EMBRYO_TRANSFERRED);
        List<EmbryoTransferredOutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomeTransferExcelDtos.size());
        for (OutcomeTransferExcelDto excelDto: outcomeTransferExcelDtos) {
            EmbryoTransferredOutcomeStageDto embryoTransferredOutcomeStageDto = new EmbryoTransferredOutcomeStageDto();
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getTransferedOutcome())){
                    embryoTransferredOutcomeStageDto.setTransferedOutcome(opt.getValue());
                }
            }
            result.add(embryoTransferredOutcomeStageDto);
        }
        return result;
    }

    private List<EndCycleStageDto> getEndCycleStageDto(List<EndCycleStageExcelDto> endCycleStageExcelDtos) {
        if (endCycleStageExcelDtos == null) {
            return null;
        }
        List<EndCycleStageDto> result = IaisCommonUtils.genNewArrayList(endCycleStageExcelDtos.size());
        List<SelectOption> reasons = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.END_CYCLE_REASON_FOR_ABANDONMENT);
        for (EndCycleStageExcelDto excelDto: endCycleStageExcelDtos) {
            EndCycleStageDto endCycleStageDto = new EndCycleStageDto();
            if ("Yes cycle has ended".equals(excelDto.getCycleAbandoned())) {
                endCycleStageDto.setCycleAbandoned(true);
            } else if ("No".equals(endCycleStageDto.getCycleAbandoned())) {
                endCycleStageDto.setCycleAbandoned(false);
            }
            for (SelectOption reason: reasons) {
                if (reason.getText().equals(excelDto.getAbandonReason())) {
                    endCycleStageDto.setAbandonReason(reason.getValue());
                }
            }
            endCycleStageDto.setOtherAbandonReason(excelDto.getOtherAbandonReason());
            result.add(endCycleStageDto);
        }
        return result;
    }

    private List<ArTreatmentSubsidiesStageDto> getArCoFundingDto(List<ArCoFundingExcelDto> arCoFundingExcelDtos) {
        if (arCoFundingExcelDtos == null) {
            return null;
        }
        List<ArTreatmentSubsidiesStageDto> result = IaisCommonUtils.genNewArrayList(arCoFundingExcelDtos.size());
        List<SelectOption> coFundings = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ATS_ART_CO_FUNDING);
        for (ArCoFundingExcelDto excelDto: arCoFundingExcelDtos) {
            ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = new ArTreatmentSubsidiesStageDto();
            for (SelectOption option: coFundings) {
                if (option.getText().equals(excelDto.getCoFunding())) {
                    arTreatmentSubsidiesStageDto.setCoFunding(option.getValue());
                }
            }
            arTreatmentSubsidiesStageDto.setIsThereAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getIsThereAppeal()));
            result.add(arTreatmentSubsidiesStageDto);
        }
        return result;
    }

    private void setDonorDto(ArCycleStageExcelDto excelDto, DonorDto donorDto1, DonorDto donorDto2) {
        donorDto1.setDirectedDonation(excelDto.getBooleanValue(excelDto.getDonorDirectedDonation1()));
        donorDto1.setIdType(excelDto.getDonorIdType1());
        donorDto1.setIdNumber(excelDto.getDonorIdNo1());
        donorDto1.setRelation(excelDto.getDonorRelation1());
        donorDto1.setDonorIndicateFresh(excelDto.getBooleanValue(excelDto.getDonorFreshOocyte1()));
        donorDto1.setDonorIndicateFrozen(excelDto.getBooleanValue(excelDto.getDonorFrozenOocyte1()));
        donorDto1.setDonorIndicateEmbryo(excelDto.getBooleanValue(excelDto.getDonorEmbryo1()));
        donorDto1.setDonorIndicateFreshSperm(excelDto.getBooleanValue(excelDto.getDonorFreshSperm1()));
        donorDto1.setDonorIndicateFrozenSperm(excelDto.getBooleanValue(excelDto.getDonorFrozenSperm1()));

        donorDto2.setDirectedDonation(excelDto.getBooleanValue(excelDto.getDonorDirectedDonation2()));
        donorDto2.setIdType(excelDto.getDonorIdType2());
        donorDto2.setIdNumber(excelDto.getDonorIdNo2());
        donorDto2.setRelation(excelDto.getDonorRelation2());
        donorDto2.setDonorIndicateFresh(excelDto.getBooleanValue(excelDto.getDonorFreshOocyte2()));
        donorDto2.setDonorIndicateFrozen(excelDto.getBooleanValue(excelDto.getDonorFrozenOocyte2()));
        donorDto2.setDonorIndicateEmbryo(excelDto.getBooleanValue(excelDto.getDonorEmbryo2()));
        donorDto2.setDonorIndicateFreshSperm(excelDto.getBooleanValue(excelDto.getDonorFreshSperm2()));
        donorDto2.setDonorIndicateFrozenSperm(excelDto.getBooleanValue(excelDto.getDonorFrozenSperm2()));
    }
}
