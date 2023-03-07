package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ArCycleBatchUploadServiceImpl implements ArCycleBatchUploadService {
    @Autowired
    private TransferInOutCycleUploadService transferInOutCycleUploadService;
    @Autowired
    DsLicenceService dsLicenceService;
    @Autowired
    private ArBatchUploadCommonService arBatchUploadCommonService;
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    ArDataSubmissionService arDataSubmissionService;
    @Autowired
    SystemAdminClient systemAdminClient;
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
    private static final String AR_SURPER_DTO_LIST = "AR_SURPER_DTO_LIST";





    private static final String PAGE_SHOW_FILE = "showPatientFile";
    private static final String FILE_APPEND = "uploadFile";
    private static final String SEESION_FILES_MAP_AJAX = HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + FILE_APPEND;




    @Override
    public Map<String, String> preBatchUpload(BaseProcessClass bpc, Map<String, String> errorMap) {

//        List<ArCycleStageDto> arCycleStageDtos = (List<ArCycleStageDto>) bpc.request.getSession().getAttribute(AR_CYCLE_STAGE_LIST);
//        List<ThawingStageDto> thawingStageDtos = (List<ThawingStageDto>) bpc.request.getSession().getAttribute(THAWING_STAGE_LIST);
//        List<OocyteRetrievalStageDto> oocyteRetrievalStageDtos = (List<OocyteRetrievalStageDto>) bpc.request.getSession().getAttribute(AR_OOCYTE_RETRIEVAL_STAGE_LIST);
//        List<FertilisationDto> fertilisationDtos = (List<FertilisationDto>) bpc.request.getSession().getAttribute(FERTILISATION_STAGE_LIST);
//        List<EmbryoCreatedStageDto> embryoCreatedStageDtos = (List<EmbryoCreatedStageDto>) bpc.request.getSession().getAttribute(EMBRYO_CREATE_STAGE_LIST);
//        List<EmbryoTransferStageDto> embryoTransferStageDtos = (List<EmbryoTransferStageDto>) bpc.request.getSession().getAttribute(EMBRYO_TRANSFER_STAGE_LIST);
//        List<EmbryoTransferredOutcomeStageDto> embryoTransferredOutcomeStageDtos = (List<EmbryoTransferredOutcomeStageDto>) bpc.request.getSession().getAttribute(OUTCOME_TRANSFER_STAGE_LIST);
//        List<PregnancyOutcomeStageDto> pregnancyOutcomeStageDtos = (List<PregnancyOutcomeStageDto>) bpc.request.getSession().getAttribute(AR_OUTCOME_PREGNANCY_STAGE_LIST);
//        List<EndCycleStageDto> endCycleStageDtos = (List<EndCycleStageDto>) bpc.request.getSession().getAttribute(END_CYCLE_STAGE_LIST);
//        List<PgtStageDto> pgtStageDtos = (List<PgtStageDto>) bpc.request.getSession().getAttribute(PGT_STAGE_LIST);
//        List<ArTreatmentSubsidiesStageDto> arTreatmentSubsidiesStageDtos = (List<ArTreatmentSubsidiesStageDto>) bpc.request.getSession().getAttribute(AR_CO_FUNDING_STAGE_LIST);
//        List<DonationStageDto> donationStageDtos = (List<DonationStageDto>) bpc.request.getSession().getAttribute(AR_DONATION_STAGE_LIST);
//        List<TransferInOutStageDto> transferInOutStageDtos = (List<TransferInOutStageDto>) bpc.request.getSession().getAttribute(AR_TRANSFER_INOUT_STAGE_LIST);
//        List<DisposalStageDto> disposalStageDtos = (List<DisposalStageDto>) bpc.request.getSession().getAttribute(AR_DISPOSAL_STAGE_LIST);
        List<ArSuperDataSubmissionDto> arSuperDataSubmissionDtos = (List<ArSuperDataSubmissionDto>) bpc.request.getSession().getAttribute(AR_SURPER_DTO_LIST);

        if (true){
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
                int oocyteRetrievalSize = oocyteRetrievalExcelDtos.size();
                int fertilisationFileItemSize = fertilisationStageExcelDtos.size();
                int embryoCreatedFileItemSize = embryoCreatedExcelDtos.size();
                int embryoTransferFileItemSize = embryoTransferExcelDtos.size();
                int transferOutcomeFileItemSize = outcomeTransferExcelDtos.size();
                int endFileItemSize = endCycleStageExcelDtos.size();
                int pgtFileItemSize = pgtStageExcelDtos.size();
                int arCoFundingFileItemSize = arCoFundingExcelDtos.size();
                if (arFileItemSize == 0) {
                    errorMap.put("uploadFileError", "PRF_ERR006");
                } else if (arFileItemSize > 200) {
                    errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                            Formatter.formatNumber(200, "#,##0"), "maxCount"));
                } else {
                    Map<String, String> arCyclePatientsMap = getArCyclePatientsMap(arCycleStageExcelDtos);
                    arSuperDataSubmissionDtos = initArSupDtos(arCycleStageExcelDtos, bpc.request);

                    // set dto
                    List<ArCycleStageDto> arCycleStageDtos = getArCycleStageDto(arCycleStageExcelDtos);
                    List<ThawingStageDto> thawingStageDtos = getThawingStageDto(thawingStageExcelDtos);
                    List<OocyteRetrievalStageDto> oocyteRetrievalStageDtos = getOocyteRetrievalStageDto(oocyteRetrievalExcelDtos);
                    List<FertilisationDto> fertilisationDtos = getFertilisationDto(fertilisationStageExcelDtos);
                    List<EmbryoCreatedStageDto> embryoCreatedStageDtos = getEmbryoCreatedStageDto(embryoCreatedExcelDtos);
                    List<EmbryoTransferStageDto>  embryoTransferStageDtos = getEmbryoTransferStageDto(embryoTransferExcelDtos);
                    List<EmbryoTransferredOutcomeStageDto> embryoTransferredOutcomeStageDtos = getTransferOutcomeDtos(outcomeTransferExcelDtos);
                    List<PregnancyOutcomeStageDto> pregnancyOutcomeStageDtos = getPregnancyOutcomeStageDto(outcomeOfPregnancyExcelDtos);
                    List<EndCycleStageDto> endCycleStageDtos = getEndCycleStageDto(endCycleStageExcelDtos);
                    List<PgtStageDto> pgtStageDtos = getPgtStageDto(pgtStageExcelDtos);
                    List<ArTreatmentSubsidiesStageDto> arTreatmentSubsidiesStageDtos = getArCoFundingDto(arCoFundingExcelDtos);
                    List<DonationStageDto> donationStageDtos = getDonationStageDto(donationStageExcelDtos); //todo
                    List<TransferInOutStageDto> transferInOutStageDtos = getTransferInOutDto(arTransferInOutExcelDtos, bpc.request);
                    List<ArSubFreezingStageDto> freezingStageDtos = getFreezingDto(arFreezingStageExcelDtos); //todo
                    List<DisposalStageDto> disposalStageDtos = getDisposalStageDto(arDisposalStageExcelDtos); //todo

                    Map<String, ExcelPropertyDto> arFieldCellMap = getFieldCellMap(ArCycleStageExcelDto.class);
                    List<FileErrorMsg> errorMsgs = DataSubmissionHelper.validateExcelList(arCycleStageDtos, "file", arFieldCellMap);

                    Map<String, ExcelPropertyDto> thawingFieldCellMap = getFieldCellMap(ThawingStageExcelDto.class);
                    List<FileErrorMsg> thawingErrorMsgs = DataSubmissionHelper.validateExcelList(thawingStageDtos, "file", thawingFieldCellMap);

                    Map<String, ExcelPropertyDto> oocyteFieldCellMap = getFieldCellMap(ArOocyteRetrievalExcelDto.class);
                    List<FileErrorMsg> oocyteRetrievalErrorMsgs = DataSubmissionHelper.validateExcelList(oocyteRetrievalStageDtos, "file", oocyteFieldCellMap);

                    Map<String, ExcelPropertyDto> fertilisationFieldCellMap = getFieldCellMap(FertilisationStageExcelDto.class);
                    List<FileErrorMsg> fertilisationErrorMsgs = DataSubmissionHelper.validateExcelList(fertilisationDtos, "file", fertilisationFieldCellMap);

                    Map<String, ExcelPropertyDto> embryoCreateCellMap = getFieldCellMap(EmbryoCreatedExcelDto.class);
                    List<FileErrorMsg> embryoCreateErrorMsgs = DataSubmissionHelper.validateExcelList(embryoCreatedStageDtos, "file", embryoCreateCellMap);

                    Map<String, ExcelPropertyDto> embryoTransferCellMap = getFieldCellMap(EmbryoTransferExcelDto.class);
                    List<FileErrorMsg> embryoTransferErrorMsgs = DataSubmissionHelper.validateExcelList(embryoTransferStageDtos, "file", embryoTransferCellMap);

                    Map<String, ExcelPropertyDto> outcomeTransferFieldCellMap = getFieldCellMap(OutcomeTransferExcelDto.class);
                    List<FileErrorMsg> outcomeTransferOutcomeErrorMsgs = DataSubmissionHelper.validateExcelList(embryoTransferredOutcomeStageDtos, "file", outcomeTransferFieldCellMap);

                    Map<String, ExcelPropertyDto> endCycleFieldCellMap = getFieldCellMap(EndCycleStageExcelDto.class);
                    List<FileErrorMsg> endCycleErrorMsgs = DataSubmissionHelper.validateExcelList(endCycleStageDtos, "file", endCycleFieldCellMap);

                    Map<String, ExcelPropertyDto> pgtFieldCellMap = getFieldCellMap(PgtStageExcelDto.class);
                    List<FileErrorMsg> pgtErrorMsgs = DataSubmissionHelper.validateExcelList(pgtStageDtos, "file", pgtFieldCellMap);

                    Map<String, ExcelPropertyDto> arCoFundingFieldCellMap = getFieldCellMap(ArCoFundingExcelDto.class);
                    List<FileErrorMsg> arCoFundingErrorMsgs = DataSubmissionHelper.validateExcelList(arTreatmentSubsidiesStageDtos, "file", arCoFundingFieldCellMap);

                    for (int i = 1; i <= arFileItemSize; i++) {
                        ArCycleStageDto arDto=arCycleStageDtos.get(i-1);
                        validateArStage(errorMsgs, arDto, arFieldCellMap, i,bpc.request);
                    }
                    for (int i = 1;i <= thawingFileItemSize; i++) {
                        ThawingStageDto thawingStageDto = thawingStageDtos.get(i-1);
                        validateThawingStage(thawingErrorMsgs, thawingStageDto, thawingFieldCellMap, i, arCyclePatientsMap);
                    }
                    for (int i = 1; i<= oocyteRetrievalSize; i++) {
                        OocyteRetrievalStageDto oocyteRetrievalStageDto = oocyteRetrievalStageDtos.get(i - 1);
                        validateOocyteRetrievalStage(oocyteRetrievalErrorMsgs, oocyteRetrievalStageDto, oocyteFieldCellMap, i, arCyclePatientsMap);
                    }
                    for (int i = 1;i <= fertilisationFileItemSize; i++) {
                        FertilisationDto fertilisationDto = fertilisationDtos.get(i-1);
                        validateFertilisationStage(fertilisationErrorMsgs, fertilisationDto ,fertilisationFieldCellMap,i, arCyclePatientsMap);
                    }
                    for (int i = 1;i <= embryoCreatedFileItemSize; i++) {
                        EmbryoCreatedStageDto embryoCreatedStageDto = embryoCreatedStageDtos.get(i-1);
                        validateEmbryoCreateStage(embryoCreateErrorMsgs, embryoCreatedStageDto ,embryoCreateCellMap,i, arCyclePatientsMap);
                    }
                    for (int i = 1;i <= embryoTransferFileItemSize; i++) {
                        EmbryoTransferStageDto embryoTransferStageDto = embryoTransferStageDtos.get(i-1);
                        validateEmbryoTransferStage(embryoTransferErrorMsgs, embryoTransferStageDto ,embryoTransferCellMap,i, arCyclePatientsMap);
                    }
                    for (int i = 1; i<=transferOutcomeFileItemSize; i++) {
                        EmbryoTransferredOutcomeStageDto embryoTransferredOutcomeStageDto = embryoTransferredOutcomeStageDtos.get(i-1);
                        validateEmbryoTransferredOutcomeStage(outcomeTransferOutcomeErrorMsgs, embryoTransferredOutcomeStageDto, outcomeTransferFieldCellMap, i, arCyclePatientsMap);
                    }
                    for (int i = 1; i <= endFileItemSize; i++) {
                        EndCycleStageDto endCycleStageDto = endCycleStageDtos.get(i-1);
                        validateEndCycleStage(endCycleErrorMsgs, endCycleStageDto, endCycleFieldCellMap, i, arCyclePatientsMap);
                    }
                    for (int i = 1; i <= pgtFileItemSize; i++) {
                        PgtStageDto pgtStageDto = pgtStageDtos.get(i-1);
                        validatePgtStage(pgtErrorMsgs, pgtStageDto,pgtFieldCellMap, i, arCyclePatientsMap);
                    }
                    for (int i = 1; i<= arCoFundingFileItemSize; i++) {
                        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arTreatmentSubsidiesStageDtos.get(i-1);
                        validateArCofundingStage(arCoFundingErrorMsgs, arTreatmentSubsidiesStageDto, arCoFundingFieldCellMap, i, arCyclePatientsMap);
                    }
                    List<FileErrorMsg> mergedErrorMsgs = Stream.of(errorMsgs,thawingErrorMsgs,oocyteRetrievalErrorMsgs, fertilisationErrorMsgs,embryoCreateErrorMsgs,embryoTransferErrorMsgs,outcomeTransferOutcomeErrorMsgs
                            ,endCycleErrorMsgs,pgtErrorMsgs,arCoFundingErrorMsgs).flatMap(Collection::stream).collect(Collectors.toList());
                    if (!mergedErrorMsgs.isEmpty()) {
                        arBatchUploadCommonService.getErrorRowInfo(errorMap, bpc.request, mergedErrorMsgs);
                    } else {
                        ParamUtil.setSessionAttr(bpc.request, AR_CYCLE_STAGE_LIST, (Serializable) arCycleStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, THAWING_STAGE_LIST, (Serializable)thawingStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, AR_OOCYTE_RETRIEVAL_STAGE_LIST, (Serializable)oocyteRetrievalStageDtos); //todo
                        ParamUtil.setSessionAttr(bpc.request, FERTILISATION_STAGE_LIST, (Serializable)fertilisationDtos);
                        ParamUtil.setSessionAttr(bpc.request, EMBRYO_CREATE_STAGE_LIST, (Serializable)embryoCreatedStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, EMBRYO_TRANSFER_STAGE_LIST, (Serializable)embryoTransferStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, OUTCOME_TRANSFER_STAGE_LIST, (Serializable)embryoTransferredOutcomeStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, AR_OUTCOME_PREGNANCY_STAGE_LIST, (Serializable)pregnancyOutcomeStageDtos);//todo
                        ParamUtil.setSessionAttr(bpc.request, END_CYCLE_STAGE_LIST, (Serializable)endCycleStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, PGT_STAGE_LIST, (Serializable)pgtStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, AR_CO_FUNDING_STAGE_LIST, (Serializable)arTreatmentSubsidiesStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, AR_DONATION_STAGE_LIST, (Serializable)donationStageDtos);//todo
                        ParamUtil.setSessionAttr(bpc.request, AR_TRANSFER_INOUT_STAGE_LIST, (Serializable)transferInOutStageDtos);
                        ParamUtil.setSessionAttr(bpc.request, AR_DISPOSAL_STAGE_LIST, (Serializable)disposalStageDtos);//todo

                        ParamUtil.setSessionAttr(bpc.request, AR_SURPER_DTO_LIST, (Serializable) arSuperDataSubmissionDtos);
                    }
                }
            }
        }
        return errorMap;
    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {

    }

    private void validateArStage(List<FileErrorMsg> errorMsgs,ArCycleStageDto arCycleStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i, HttpServletRequest request){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        if (StringUtil.isEmpty(arCycleStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idType"), "GENERAL_ERR0006"));
        }
        if (StringUtil.isEmpty(arCycleStageDto.getIdNo())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "GENERAL_ERR0006"));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getIdType()) && StringUtil.isNotEmpty(arCycleStageDto.getIdNo()) &&
            arBatchUploadCommonService.setPatientInfo(arCycleStageDto.getIdType(),arCycleStageDto.getIdNo(),request) == null) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not find this patient"));
        }
        if (StringUtil.isEmpty( arCycleStageDto.getStartDate())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("startDate"), "GENERAL_ERR0006"));
        } else {
            try {
                Formatter.parseDate(arCycleStageDto.getStartDate());
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("startDate"), "GENERAL_ERR0033"));
            }
        }

        if (StringUtil.isEmpty(arCycleStageDto.getMainIndication())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("mainIndication"), errMsgErr006));
        } else if (arCycleStageDto.getMainIndication().length() > 10) {
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","10");
            repMap.put("fieldNo","Main Indication");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("mainIndication"), errMsg));
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
        int currentMarriageChildren = 0;
        int previousMarriageChildren = 0;
        int deliveredThroughChildren = 0;
        if (StringUtil.isNotEmpty(arCycleStageDto.getCurrentMarriageChildrenStr())) {
            try {
                Double currentMarriageChildrenDouble = Double.parseDouble(arCycleStageDto.getCurrentMarriageChildrenStr());
                currentMarriageChildren = currentMarriageChildrenDouble.intValue();
                arCycleStageDto.setCurrentMarriageChildren(currentMarriageChildren);
                if (currentMarriageChildren > 10 || currentMarriageChildren < 0) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","10");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("currentMarriageChildren"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("currentMarriageChildren"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("currentMarriageChildren"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getPreviousMarriageChildrenStr())) {
            try {
                Double currentMarriageChildrenDouble = Double.parseDouble(arCycleStageDto.getPreviousMarriageChildrenStr());
                previousMarriageChildren = currentMarriageChildrenDouble.intValue();
                arCycleStageDto.setCurrentMarriageChildren(currentMarriageChildren);
                if (currentMarriageChildren > 10 || currentMarriageChildren < 0) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","10");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("previousMarriageChildren"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("previousMarriageChildren"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("currentMarriageChildren"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getDeliveredThroughChildrenStr())) {
            try {
                Double strDouble = Double.parseDouble(arCycleStageDto.getDeliveredThroughChildrenStr());
                deliveredThroughChildren = strDouble.intValue();
                arCycleStageDto.setDeliveredThroughChildren(deliveredThroughChildren);
                if (deliveredThroughChildren > 10 || deliveredThroughChildren < 0) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","10");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveredThroughChildren"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveredThroughChildren"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveredThroughChildren"), errMsgErr006));
        }
        if (currentMarriageChildren + previousMarriageChildren < deliveredThroughChildren) {
            Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(3);
            stringStringMap.put("field1","");
            stringStringMap.put("field2","No. of Children from Current Marriage");
            stringStringMap.put("field3","No. of Children from Previous Marriage");
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("deliveredThroughChildren"), MessageUtil.getMessageDesc("DS_ERR011",stringStringMap).trim()));
        }
        if (StringUtil.isNotEmpty(arCycleStageDto.getTotalPreviouslyPreviouslyStr())) {
            try {
                Double strDouble = Double.parseDouble(arCycleStageDto.getTotalPreviouslyPreviouslyStr());
                int totalPreviouslyPreviously = strDouble.intValue();
                arCycleStageDto.setTotalPreviouslyPreviously(totalPreviouslyPreviously);
                if (totalPreviouslyPreviously > 10 || totalPreviouslyPreviously < 0) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("minNum","0");
                    repMap.put("maxNum","10");
                    repMap.put("field","This field");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("totalPreviouslyPreviously"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("totalPreviouslyPreviously"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("totalPreviouslyPreviously"), errMsgErr006));
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

    private void validateThawingStage(List<FileErrorMsg> errorMsgs,ThawingStageDto thawingStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        String dsErr064 = MessageUtil.getMessageDesc("DS_ERR064");

        if (!patients.containsKey(thawingStageDto.getIdNo()) || !patients.get(thawingStageDto.getIdNo()).equals(thawingStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }

        if (Boolean.TRUE.equals(thawingStageDto.getHasOocyte())) {
            int thawedOocyteNum = 0;
            int thawedOocytesSurvivedMatureNum = 0;
            int thawedOocytesSurvivedImmatureNum = 0;
            int thawedOocytesSurvivedOtherNum = 0;

            if (StringUtil.isNotEmpty(thawingStageDto.getThawedOocytesNum())) {
                try {
                    thawedOocyteNum = Integer.parseInt(thawingStageDto.getThawedOocytesNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesNum"), errMsgErr006));
            }
            if (StringUtil.isNotEmpty(thawingStageDto.getThawedOocytesSurvivedMatureNum())) {
                try {
                    thawedOocytesSurvivedMatureNum = Integer.parseInt(thawingStageDto.getThawedOocytesSurvivedMatureNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedMatureNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedMatureNum"), errMsgErr006));
            }
            if (StringUtil.isNotEmpty(thawingStageDto.getThawedOocytesSurvivedImmatureNum())) {
                try {
                    thawedOocytesSurvivedImmatureNum = Integer.parseInt(thawingStageDto.getThawedOocytesSurvivedImmatureNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedImmatureNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedImmatureNum"), errMsgErr006));
            }
            if (StringUtil.isNotEmpty(thawingStageDto.getThawedOocytesSurvivedOtherNum())) {
                try {
                    thawedOocytesSurvivedOtherNum = Integer.parseInt(thawingStageDto.getThawedOocytesSurvivedOtherNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedOtherNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesSurvivedOtherNum"), errMsgErr006));
            }
            if (thawedOocytesSurvivedMatureNum + thawedOocytesSurvivedImmatureNum + thawedOocytesSurvivedOtherNum + thawedOocyteNum > thawedOocyteNum) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesNum"), dsErr064));
            }
        } else if (thawingStageDto.getHasOocyte() == null) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytes"), errMsgErr006));
        }

        if (Boolean.TRUE.equals(thawingStageDto.getHasEmbryo())) {
            int thawedEmbryosNum = 0;
            int thawedEmbryosSurvivedNum = 0;
            if (StringUtil.isNotEmpty(thawingStageDto.getThawedEmbryosNum())) {
                try {
                    thawedEmbryosNum = Integer.parseInt(thawingStageDto.getThawedEmbryosNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedEmbryosNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedEmbryosNum"), errMsgErr006));
            }
            if (StringUtil.isNotEmpty(thawingStageDto.getThawedEmbryosSurvivedNum())) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedEmbryosSurvivedNum"), errMsgErr006));
                try {
                    thawedEmbryosSurvivedNum = Integer.parseInt(thawingStageDto.getThawedEmbryosSurvivedNum());
                } catch (Exception e) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedEmbryosSurvivedNum"), errMsgErr002));
                }
            } else {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedEmbryosSurvivedNum"), errMsgErr006));
            }
            if (thawedEmbryosNum < thawedEmbryosSurvivedNum) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesNum"), dsErr064));
            }
        } else if (thawingStageDto.getHasEmbryo() == null) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawingEmbryos"), errMsgErr006));
        }

        // todo validate inventory
    }

    private void validateOocyteRetrievalStage(List<FileErrorMsg> errorMsgs,OocyteRetrievalStageDto oocyteRetrievalStageDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients) {
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //only number
        String errMsgErr027 = MessageUtil.getMessageDesc("GENERAL_ERR0027"); //invalid number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        String errMsgErr0051 = MessageUtil.getMessageDesc("GENERAL_ERR0051"); // invalid data
        if (!patients.containsKey(oocyteRetrievalStageDto.getIdNo()) || !patients.get(oocyteRetrievalStageDto.getIdNo()).equals(oocyteRetrievalStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        if (StringUtil.isEmpty(oocyteRetrievalStageDto.getIsOvarianSyndrome())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isOvarianSyndrome"), errMsgErr006));
        }
        if (Boolean.TRUE.equals(oocyteRetrievalStageDto.getIsFromPatient()) && Boolean.TRUE.equals(oocyteRetrievalStageDto.getIsFromPatientTissue())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isFromPatient"), "Only can choose one source"));
        }
        if (Boolean.FALSE.equals(oocyteRetrievalStageDto.getIsFromPatient()) && Boolean.FALSE.equals(oocyteRetrievalStageDto.getIsFromPatientTissue())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isFromPatient"), "Must choose one source"));
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getMatureRetrievedNum())) {
            try {
                Double matureRetrievedNumDouble = Double.parseDouble(oocyteRetrievalStageDto.getMatureRetrievedNum());
                int matureRetrievedNum = matureRetrievedNumDouble.intValue();
                if(matureRetrievedNum > 100 || matureRetrievedNum < 0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field","No. Retrieved (Mature)");
                    repMap.put("minNum","1");
                    repMap.put("maxNum","99");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("matureRetrievedNum"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("matureRetrievedNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("matureRetrievedNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getImmatureRetrievedNum())) {
            try {
                Double immatureRetrievedNumDouble = Double.parseDouble(oocyteRetrievalStageDto.getImmatureRetrievedNum());
                int immatureRetrievedNum = immatureRetrievedNumDouble.intValue();
                if(immatureRetrievedNum > 100 || immatureRetrievedNum < 0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field","No. Retrieved (Immature)");
                    repMap.put("minNum","1");
                    repMap.put("maxNum","99");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("immatureRetrievedNum"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("immatureRetrievedNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("immatureRetrievedNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getOtherRetrievedNum())) {
            try {
                Double otherRetrievedNumDouble = Double.parseDouble(oocyteRetrievalStageDto.getOtherRetrievedNum());
                int otherRetrievedNum = otherRetrievedNumDouble.intValue();
                if(otherRetrievedNum > 100 || otherRetrievedNum < 0){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field","No. Retrieved (Others)");
                    repMap.put("minNum","1");
                    repMap.put("maxNum","99");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherRetrievedNum"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherRetrievedNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherRetrievedNum"), errMsgErr006));
        }
    }

    private void validateFertilisationStage(List<FileErrorMsg> errorMsgs,FertilisationDto fertilisationDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //only number
        String errMsgErr027 = MessageUtil.getMessageDesc("GENERAL_ERR0027"); //invalid number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        String errMsgErr0051 = MessageUtil.getMessageDesc("GENERAL_ERR0051"); // invalid data

        if (!patients.containsKey(fertilisationDto.getIdNo()) || !patients.get(fertilisationDto.getIdNo()).equals(fertilisationDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }

        if (StringUtil.isEmpty(fertilisationDto.getSourceOfOocyte()) && StringUtil.isEmpty(fertilisationDto.getSourceOfOocytePot())
                && StringUtil.isEmpty(fertilisationDto.getSourceOfOocytePatient())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("sourceOfOocyte"), errMsgErr006));
        }
        if (!"Fresh".equals(fertilisationDto.getOocyteUsed())
                && !"Frozen".equals(fertilisationDto.getOocyteUsed())
                && !"Both".equals(fertilisationDto.getOocyteUsed())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("oocyteUsed"), errMsgErr0051));
        } else if (StringUtil.isEmpty(fertilisationDto.getOocyteUsed())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("oocyteUsed"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(fertilisationDto.getUsedOocytesNum())) {
            try {
                int usedOocyteNum = Integer.parseInt(fertilisationDto.getUsedOocytesNum());
                if (usedOocyteNum <= 0 || usedOocyteNum>99) {
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field","How many oocyte(s) were used in this cycle");
                    repMap.put("minNum","1");
                    repMap.put("maxNum","99");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedOocytesNum"), errMsg));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedOocytesNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedOocytesNum"), errMsgErr006));
        }

        if (Boolean.FALSE.equals(fertilisationDto.isFromHusband()) && Boolean.FALSE.equals(fertilisationDto.isFromHusbandTissue())
                && Boolean.FALSE.equals(fertilisationDto.isFromDonor()) && Boolean.FALSE.equals(fertilisationDto.isFromDonorTissue())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("sourceOfSemenHus"), "Must have an option as 'Yes' from (8)~(11)"));
        }
        if (!"Fresh".equals(fertilisationDto.getSpermUsed())
                && !"Frozen".equals(fertilisationDto.getSpermUsed())
                && !"Both".equals(fertilisationDto.getSpermUsed())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("spermUsed"), errMsgErr0051));
        } else if (StringUtil.isEmpty(fertilisationDto.getSpermUsed())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("spermUsed"), errMsgErr006));
        }
        int extractedSpermVialsNum = 0;
        int usedSpermVialsNum = 0;

        if (StringUtil.isNotEmpty(fertilisationDto.getExtractedSpermVialsNum())) {
            try {
                extractedSpermVialsNum = Integer.parseInt(fertilisationDto.getExtractedSpermVialsNum());
                if (extractedSpermVialsNum <= 0) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("extractedSpermVialsNum"),  errMsgErr027));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("extractedSpermVialsNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("extractedSpermVialsNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(fertilisationDto.getUsedSpermVialsNum())) {
            try {
                usedSpermVialsNum = Integer.parseInt(fertilisationDto.getUsedSpermVialsNum());
                if (usedSpermVialsNum <= 0) {
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedSpermVialsNum"),  errMsgErr027));
                }
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedSpermVialsNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedSpermVialsNum"), errMsgErr006));
        }
        if (extractedSpermVialsNum < usedSpermVialsNum) {
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("field1","How many vials of sperm were used in this cycle?");
            repMap.put("field2","'How many vials of sperm were extracted?'");
            repMap.put("field3","frozen sperm tagged to patient");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR011",repMap);
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("usedSpermVialsNum"), errMsg));
        }

        int totalThawedSum = 0;
        int totalFreshSum = 0;
        if(Boolean.TRUE.equals(fertilisationDto.isZiftUsed()) || Boolean.TRUE.equals(fertilisationDto.isIvfUsed()) || Boolean.TRUE.equals(fertilisationDto.isIcsiUsed())|| Boolean.TRUE.equals(fertilisationDto.isGiftUsed())){
            if (Boolean.TRUE.equals(fertilisationDto.isIvfUsed())){
                if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesInseminatedNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesInseminatedNum())&& StringUtil.isNumber(fertilisationDto.getFreshOocytesInseminatedNum())){
                    try {
                        totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesInseminatedNum());
                    } catch (Exception e) {
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesInseminatedNum"), errMsgErr002));
                    }
                }
                if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesInseminatedNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesInseminatedNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesInseminatedNum())){
                    try {
                        totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesInseminatedNum());
                    } catch (Exception e) {
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesInseminatedNum"), errMsgErr002));
                    }
                }
            }
            if (Boolean.TRUE.equals(fertilisationDto.isIcsiUsed())){
                if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesMicroInjectedNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesMicroInjectedNum())&& StringUtil.isNumber(fertilisationDto.getFreshOocytesMicroInjectedNum())){
                    try {
                        totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesMicroInjectedNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesMicroInjectedNum"), errMsgErr002));
                    }

                }
                if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesMicroinjectedNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesMicroinjectedNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesMicroinjectedNum())){
                    try {
                        totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesMicroinjectedNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesMicroinjectedNum"), errMsgErr002));
                    }
                }
            }
            if (Boolean.TRUE.equals(fertilisationDto.isGiftUsed())){
                if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesGiftNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesGiftNum()) && StringUtil.isNumber(fertilisationDto.getFreshOocytesGiftNum())){
                    try {
                        totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesGiftNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesGiftNum"), errMsgErr002));
                    }
                }
                if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesGiftNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesGiftNum()) && StringUtil.isNumber(fertilisationDto.getThawedOocytesGiftNum())){
                    try {
                        totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesGiftNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesGiftNum"), errMsgErr002));
                    }
                }
            }
            if (Boolean.TRUE.equals(fertilisationDto.isZiftUsed())){
                if (StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesZiftNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getFreshOocytesZiftNum())&&StringUtil.isNumber(fertilisationDto.getFreshOocytesZiftNum())){
                    try {
                        totalFreshSum+=Integer.parseInt(fertilisationDto.getFreshOocytesZiftNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesZiftNum"), errMsgErr002));
                    }
                }
                if (StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())){
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesZiftNum"), errMsgErr006));
                }else if(!StringUtil.isEmpty(fertilisationDto.getThawedOocytesZiftNum())&&StringUtil.isNumber(fertilisationDto.getThawedOocytesZiftNum())){
                    try {
                        totalThawedSum+=Integer.parseInt(fertilisationDto.getThawedOocytesZiftNum());
                    } catch (Exception e){
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("thawedOocytesZiftNum"), errMsgErr002));
                    }
                }
            }
        }else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("freshOocytesInseminatedNum"), "Must have an option as 'Yes' from (15)~(18)"));
        }
        // todo validate Inventory
    }

    private void validateEmbryoCreateStage(List<FileErrorMsg> errorMsgs,EmbryoCreatedStageDto embryoCreatedStageDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        int transEmbrFreshOccNum = 0;
        int poorDevFreshOccNum = 0;
        int transEmbrThawOccNum = 0;
        int poorDevThawOccNum = 0;

        if (!patients.containsKey(embryoCreatedStageDto.getIdNo()) || !patients.get(embryoCreatedStageDto.getIdNo()).equals(embryoCreatedStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        if (StringUtil.isNotEmpty(embryoCreatedStageDto.getTransEmbrFreshOccNumStr())) {
            try {
                Double transEmbrFreshOccNumDouble = Double.parseDouble(embryoCreatedStageDto.getTransEmbrFreshOccNumStr());
                transEmbrFreshOccNum = transEmbrFreshOccNumDouble.intValue();
                if(embryoCreatedStageDto.getTransEmbrFreshOccNumStr().length()>50){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","2");
                    repMap.put("fieldNo","No. of Transferrable embryos created from fresh oocyte(s)");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transEmbrFreshOccNum"), errMsg));
                }
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transEmbrFreshOccNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transEmbrFreshOccNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(embryoCreatedStageDto.getPoorDevFreshOccNumStr())) {
            try {
                Double poorDevFreshOccNumDouble = Double.parseDouble(embryoCreatedStageDto.getPoorDevFreshOccNumStr());
                poorDevFreshOccNum = poorDevFreshOccNumDouble.intValue();
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("poorDevFreshOccNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("poorDevFreshOccNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(embryoCreatedStageDto.getTransEmbrThawOccNumStr())) {
            try {
                Double transEmbrThawOccNumDouble = Double.parseDouble(embryoCreatedStageDto.getTransEmbrThawOccNumStr());
                transEmbrThawOccNum = transEmbrThawOccNumDouble.intValue();
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transEmbrThawOccNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transEmbrThawOccNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(embryoCreatedStageDto.getPoorDevThawOccNumStr())) {
            try {
                Double poorDevThawOccNumDouble = Double.parseDouble(embryoCreatedStageDto.getPoorDevThawOccNumStr());
                poorDevThawOccNum = poorDevThawOccNumDouble.intValue();
            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("poorDevThawOccNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("poorDevThawOccNum"), errMsgErr006));
        }
        if (errorMsgs.isEmpty()) {
            int fresh = transEmbrFreshOccNum + poorDevFreshOccNum;
            int thawed = transEmbrThawOccNum + poorDevThawOccNum;
            // todo validate patient inventory
        }
    }

    private void validateEmbryoTransferStage(List<FileErrorMsg> errorMsgs,EmbryoTransferStageDto embryoTransferStageDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr002 = MessageUtil.getMessageDesc("GENERAL_ERR0002"); //number
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory

        if (!patients.containsKey(embryoTransferStageDto.getIdNo()) || !patients.get(embryoTransferStageDto.getIdNo()).equals(embryoTransferStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        if (StringUtil.isNotEmpty(embryoTransferStageDto.getTransferNumStr())) {
            try {
                Double transferNumDouble = Double.parseDouble(embryoTransferStageDto.getTransferNumStr());
                int transferNum = transferNumDouble.intValue();
                if(transferNum<1 || transferNum>10){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("field","No. Transferred");
                    repMap.put("minNum","1");
                    repMap.put("maxNum","10");
                    String errMsg = MessageUtil.getMessageDesc("DS_ERR003",repMap);
                    errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferNum"), errMsg));
                }

                List<EmbryoTransferDetailDto> detailDtos = embryoTransferStageDto.getEmbryoTransferDetailDtos();
                for (int j = 0; j < transferNum; j++) {
                    if (StringUtil.isEmpty(detailDtos.get(j).getEmbryoType())) {
                        int num = j + 1;
                        String type ="embryoType" + num;
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(type), errMsgErr006));
                    }
                    if (StringUtil.isEmpty(detailDtos.get(j).getEmbryoAge())) {
                        int num = j + 1;
                        String age = "embryo" + num;
                        errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get(age), errMsgErr006));
                    }
                }


            }catch (Exception e){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferNum"), errMsgErr002));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferNum"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(embryoTransferStageDto.getFirstTransferDateStr())) {
            try {
                Formatter.parseDate(embryoTransferStageDto.getFirstTransferDateStr());
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("firstTransferDate"), MessageUtil.getMessageDesc("GENERAL_ERR0033")));
            }
        } else {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("firstTransferDate"), errMsgErr006));
        }
        if (StringUtil.isNotEmpty(embryoTransferStageDto.getSecondTransferDateStr())) {
            try {
                Formatter.parseDate(embryoTransferStageDto.getSecondTransferDateStr());
            } catch (Exception e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("secondTransferDate"), MessageUtil.getMessageDesc("GENERAL_ERR0033")));
            }
        }

    }

    private void validateEmbryoTransferredOutcomeStage(List<FileErrorMsg> errorMsgs,EmbryoTransferredOutcomeStageDto dto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        if (!patients.containsKey(dto.getIdNo()) || !patients.get(dto.getIdNo()).equals(dto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        if (StringUtil.isEmpty(dto.getTransferedOutcome())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferedOutcome"), errMsgErr006));
        }
    }


    private void validateEndCycleStage(List<FileErrorMsg> errorMsgs,EndCycleStageDto endCycleStageDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006"); //mandatory
        if (!patients.containsKey(endCycleStageDto.getIdNo()) || !patients.get(endCycleStageDto.getIdNo()).equals(endCycleStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        if (StringUtil.isEmpty(endCycleStageDto.getCycleAbandoned())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("cycleAbandoned"), errMsgErr006));
        }
        if (Boolean.TRUE.equals(endCycleStageDto.getCycleAbandoned()) &&StringUtil.isEmpty(endCycleStageDto.getAbandonReason())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("abandonReason"), errMsgErr006));
        }
        if (Boolean.TRUE.equals(endCycleStageDto.getCycleAbandoned()) && DataSubmissionConsts.ABANDON_REASON_OTHER.equals(endCycleStageDto.getAbandonReason())) {
            if (endCycleStageDto.getOtherAbandonReason().length() > 100) {
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","100");
                repMap.put("fieldNo","Reason for Abandonment (Others)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherAbandonReason"), errMsg));
            }
            if (StringUtil.isEmpty(endCycleStageDto.getOtherAbandonReason())){
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("otherAbandonReason"), errMsgErr006));
            }
        }

    }

    private void validatePgtStage(List<FileErrorMsg> errorMsgs,PgtStageDto pgtStageDto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        if (!patients.containsKey(pgtStageDto.getIdNo()) || !patients.get(pgtStageDto.getIdNo()).equals(pgtStageDto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("patientIdNo"), "Can not submit patients not in ar stage"));
        }
        if ("0".equals(pgtStageDto.getIsPgtMCom()) && "0".equals(pgtStageDto.getIsPgtMRare()) && "0".equals(pgtStageDto.getIsPgtSr()) &&
                "0".equals(pgtStageDto.getIsPgtA()) && "0".equals(pgtStageDto.getIsPtt()) && "0".equals(pgtStageDto.getIsOtherPgt())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isPgtMCom"), "Must select at least one as 'Yes'"));
        }
        if ("1".equals(pgtStageDto.getIsPgtMCom())) {

        }
        if ("1".equals(pgtStageDto.getIsPgtMRare())) {

        }
        if ("1".equals(pgtStageDto.getIsPgtMCom()) || "1".equals(pgtStageDto.getIsPgtMRare())) {

        }
//        pgtStageDto.setWorkUpCom
//        pgtStageDto.setEbtCom
//        pgtStageDto.setWorkUpRare
//        pgtStageDto.setEbtRare
//
//        pgtStageDto.setPgtMDateStr(excelDto.getPgtMDate());
//        try {
//            pgtStageDto.setPgtMDate(Formatter.parseDate(excelDto.getPgtMDate()));
//        } catch (ParseException e) {
//
//        }
//        pgtStageDto.setIsPgtMDsld
//        pgtStageDto.setIsPgtMWithHla
//        pgtStageDto.setIsPgtMNon
//        pgtStageDto.setPgtMRefNo
//        pgtStageDto.setPgtMCondition
//        pgtStageDto.setIsPgtCoFunding
//        pgtStageDto.setIsPgtMRareCoFunding
//        pgtStageDto.setPgtMAppeal

    }

    private void validateArCofundingStage(List<FileErrorMsg> errorMsgs,ArTreatmentSubsidiesStageDto dto, Map<String, ExcelPropertyDto> fieldCellMap,int i, Map<String, String> patients){
        String errMsgErr006 = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (!patients.containsKey(dto.getIdNo()) || !patients.get(dto.getIdNo()).equals(dto.getIdType())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("idNo"), "Can not submit patients not in ar stage"));
        }
        int totalCoFundingnum = 0;
        if (StringUtil.isNotEmpty(dto.getCoFunding()) && !DataSubmissionConsts.ART_APPLE_NO.equals(dto.getCoFunding())
                && !DataSubmissionConsts.ART_APPLE_FRESH_THREE.equals(dto.getCoFunding()) && !DataSubmissionConsts.ART_APPLE_FROZEN_THREE.equals(dto.getCoFunding())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("coFunding"), "Please enter the correct option"));
        }
        if (StringUtil.isEmpty(dto.getCoFunding())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("coFunding"), errMsgErr006));
        }
        // todo calculate ar co-funding
        if (totalCoFundingnum >= 3 && StringUtil.isEmpty(dto.getIsThereAppeal())) {
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("isThereAppeal"), errMsgErr006));
        }
    }

    private List<ArSuperDataSubmissionDto> initArSupDtos(List<ArCycleStageExcelDto> excelDtos, HttpServletRequest request) {
        List<ArSuperDataSubmissionDto> surDtos = IaisCommonUtils.genNewArrayList();
        ArSuperDataSubmissionDto superDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        for (ArCycleStageExcelDto excelDto: excelDtos) {
            ArSuperDataSubmissionDto superDataSubmissionDto = new ArSuperDataSubmissionDto();
            BeanUtils.copyProperties(superDto, superDataSubmissionDto);
            superDataSubmissionDto.setPatientInfoDto(arBatchUploadCommonService.setPatientInfo(excelDto.getIdType(),excelDto.getIdNo(),request));
            superDataSubmissionDto.setIdType(excelDto.getIdType());
            superDataSubmissionDto.setIdNo(excelDto.getIdNo());
            surDtos.add(superDataSubmissionDto);
        }
        return surDtos;
    }

    private Map<String, String> getArCyclePatientsMap(List<ArCycleStageExcelDto> excelDtos) {
        Map<String,String> result = IaisCommonUtils.genNewHashMap(excelDtos.size());
        for (ArCycleStageExcelDto excelDto: excelDtos) {
            if (StringUtil.isNotEmpty(excelDto.getIdNo())) {
                result.put(excelDto.getIdNo(), excelDto.getIdType());
            }
        }
        return result;
    }



    private List<ArCycleStageDto> getArCycleStageDto(List<ArCycleStageExcelDto> arCycleStageExcelDtos) {
        if(arCycleStageExcelDtos == null) {
            return null;
        }
        List<ArCycleStageDto> result = IaisCommonUtils.genNewArrayList(arCycleStageExcelDtos.size());
        List<SelectOption> options = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.AR_MAIN_INDICATION);
        for (ArCycleStageExcelDto excelDto: arCycleStageExcelDtos) {
            ArCycleStageDto arCycleStageDto = new ArCycleStageDto();
            arCycleStageDto.setIdType(excelDto.getIdType());
            arCycleStageDto.setIdNo(excelDto.getIdNo());
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
            arCycleStageDto.setOtherIndication(String.join("#", otherIndications));
            arCycleStageDto.setInVitroMaturation(excelDto.getBooleanValue(excelDto.getInVitroMaturation()));
            arCycleStageDto.setTreatmentFreshNatural(excelDto.getBooleanValue(excelDto.getTreatmentFreshNatural()));
            arCycleStageDto.setTreatmentFreshStimulated(excelDto.getBooleanValue(excelDto.getTreatmentFreshStimulated()));
            arCycleStageDto.setTreatmentFrozenOocyte(excelDto.getBooleanValue(excelDto.getTreatmentFrozenOocyte()));
            arCycleStageDto.setTreatmentFrozenEmbryo(excelDto.getBooleanValue(excelDto.getTreatmentFrozenEmbryo()));
            String currentMarriageChildren = StringUtils.substringBefore(excelDto.getCurrentMarriageChildren(),".");
            String previousMarriageChildren = StringUtils.substringBefore(excelDto.getPreviousMarriageChildren(),".");
            String deliveredThroughChildren = StringUtils.substringBefore(excelDto.getDeliveredThroughChildren(),".");
            String totalPreviouslyPreviously = StringUtils.substringBefore(excelDto.getTotalPreviouslyPreviously(),".");
            arCycleStageDto.setCurrentMarriageChildrenStr(currentMarriageChildren);
            arCycleStageDto.setPreviousMarriageChildrenStr(previousMarriageChildren);
            arCycleStageDto.setDeliveredThroughChildrenStr(deliveredThroughChildren);
            arCycleStageDto.setTotalPreviouslyPreviouslyStr(totalPreviouslyPreviously);
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

            result.add(arCycleStageDto);
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
            thawingStageDto.setIdType(excelDto.getIdType());
            thawingStageDto.setIdNo(excelDto.getIdNo());
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

    private List<OocyteRetrievalStageDto> getOocyteRetrievalStageDto(List<ArOocyteRetrievalExcelDto> oocyteRetrievalExcelDtos) {
        if (oocyteRetrievalExcelDtos == null) {
            return null;
        }
        List<OocyteRetrievalStageDto> result = IaisCommonUtils.genNewArrayList(oocyteRetrievalExcelDtos.size());
        for (ArOocyteRetrievalExcelDto excelDto: oocyteRetrievalExcelDtos) {
            OocyteRetrievalStageDto oocyteRetrievalStageDto = new OocyteRetrievalStageDto();
            oocyteRetrievalStageDto.setIdType(excelDto.getPatientIdType());
            oocyteRetrievalStageDto.setIdNo(excelDto.getPatientIdNo());
            oocyteRetrievalStageDto.setIsOvarianSyndrome(arBatchUploadCommonService.getBooleanValue(excelDto.getIsOvarianSyndrome()));
            oocyteRetrievalStageDto.setIsFromPatient(arBatchUploadCommonService.getBooleanValue(excelDto.getIsFromPatient()));
            oocyteRetrievalStageDto.setIsFromPatientTissue(arBatchUploadCommonService.getBooleanValue(excelDto.getIsFromPatientTissue()));
            oocyteRetrievalStageDto.setMatureRetrievedNum(excelDto.getMatureRetrievedNum());
            oocyteRetrievalStageDto.setImmatureRetrievedNum(excelDto.getImmatureRetrievedNum());
            oocyteRetrievalStageDto.setOtherRetrievedNum(excelDto.getOtherRetrievedNum());
            result.add(oocyteRetrievalStageDto);
        }
        return result;
    }

    private List<FertilisationDto> getFertilisationDto(List<FertilisationStageExcelDto> fertilisationStageExcelDtos) {
        if (fertilisationStageExcelDtos == null) {
            return null;
        }
        List<FertilisationDto> result = IaisCommonUtils.genNewArrayList(fertilisationStageExcelDtos.size());
        List<SelectOption> freshOrFrozen = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_FRESH_OR_FROZEN);
        for (FertilisationStageExcelDto excelDto: fertilisationStageExcelDtos) {
            FertilisationDto fertilisationDto = new FertilisationDto();
            fertilisationDto.setIdType(excelDto.getIdType());
            fertilisationDto.setIdNo(excelDto.getIdNo());
            fertilisationDto.setSourceOfOocyte(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocyte()));
            fertilisationDto.setSourceOfOocytePatient(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocytePatient()));
            fertilisationDto.setSourceOfOocytePot(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfOocytePot()));
            for (SelectOption opt: freshOrFrozen) {
                if (opt.getText().equals(excelDto.getOocyteUsed())) {
                    fertilisationDto.setOocyteUsed(opt.getText());
                }
            }
            if (StringUtil.isEmpty(fertilisationDto.getOocyteUsed())) {
                fertilisationDto.setOocyteUsed(excelDto.getOocyteUsed());
            }
            fertilisationDto.setUsedOocytesNum(StringUtils.substringBefore(excelDto.getUsedOocytesNum(),"."));

            fertilisationDto.setFromHusband(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenHus()));
            fertilisationDto.setFromHusbandTissue(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenHusTes()));
            fertilisationDto.setFromDonor(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenDon()));
            fertilisationDto.setFromDonorTissue(arBatchUploadCommonService.getBooleanValue(excelDto.getSourceOfSemenDonTes()));
            for (SelectOption opt: freshOrFrozen) {
                if (opt.getText().equals(excelDto.getSpermUsed())) {
                    fertilisationDto.setSpermUsed(opt.getText());
                }
            }
            if (StringUtil.isEmpty(fertilisationDto.getSpermUsed())) {
                fertilisationDto.setSpermUsed(excelDto.getOocyteUsed());
            }
            fertilisationDto.setExtractedSpermVialsNum(StringUtils.substringBefore(excelDto.getExtractedSpermVialsNum(),"."));
            fertilisationDto.setUsedSpermVialsNum(StringUtils.substringBefore(excelDto.getUsedSpermVialsNum(),"."));

            List<String> atuList = IaisCommonUtils.genNewArrayList(4);
            fertilisationDto.setIvfUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getIfvUsed()));
            fertilisationDto.setIcsiUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getIcsiUsed()));
            fertilisationDto.setGiftUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getGiftUsed()));
            fertilisationDto.setZiftUsed(arBatchUploadCommonService.getBooleanValue(excelDto.getZiftUsed()));
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getIfvUsed())) {
                atuList.add(DataSubmissionConsts.AR_TECHNIQUES_USED_IVF);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getIcsiUsed())) {
                atuList.add(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getGiftUsed())) {
                atuList.add(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT);
            }
            if (arBatchUploadCommonService.getBooleanValue(excelDto.getZiftUsed())) {
                atuList.add(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT);
            }
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
            embryoCreatedStageDto.setIdType(excelDto.getIdType());
            embryoCreatedStageDto.setIdNo(excelDto.getIdNo());
            embryoCreatedStageDto.setTransEmbrFreshOccNumStr(excelDto.getTransEmbrFreshOccNum());
            embryoCreatedStageDto.setPoorDevFreshOccNumStr(excelDto.getPoorDevFreshOccNum());
            embryoCreatedStageDto.setTransEmbrThawOccNumStr(excelDto.getTransEmbrThawOccNum());
            embryoCreatedStageDto.setPoorDevThawOccNumStr(excelDto.getPoorDevThawOccNum());
            result.add(embryoCreatedStageDto);
        }
        return result;
    }

    private List<EmbryoTransferStageDto> getEmbryoTransferStageDto(List<EmbryoTransferExcelDto> embryoTransferExcelDtos){
        if (embryoTransferExcelDtos == null) {
            return null;
        }
        List<EmbryoTransferStageDto> result = IaisCommonUtils.genNewArrayList(embryoTransferExcelDtos.size());
        List<SelectOption> options = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AGE_OF_EMBRYO_TRANSFER);
        for (EmbryoTransferExcelDto excelDto: embryoTransferExcelDtos) {
            EmbryoTransferStageDto embryoTransferStageDto = new EmbryoTransferStageDto();
            embryoTransferStageDto.setIdType(excelDto.getIdType());
            embryoTransferStageDto.setIdNo(excelDto.getIdNo());
            embryoTransferStageDto.setTransferNumStr(excelDto.getTransferNum());
            if (StringUtil.isNumber(excelDto.getTransferNum())) {
                Double transferNumDouble = Double.parseDouble(excelDto.getTransferNum());
                embryoTransferStageDto.setTransferNum(transferNumDouble.intValue());
            }
            List<EmbryoTransferDetailDto> detailDtos = IaisCommonUtils.genNewArrayList(10);
            for (int i = 1; i<=10; i++) {
                EmbryoTransferDetailDto detailDto = new EmbryoTransferDetailDto();
                detailDto.setSeqNumber(i);
                detailDtos.add(detailDto);
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo1())){
                    detailDtos.get(0).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType1())) {
                detailDtos.get(0).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType1())) {
                detailDtos.get(0).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo2())){
                    detailDtos.get(1).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType2())) {
                detailDtos.get(1).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType2())) {
                detailDtos.get(1).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo3())){
                    detailDtos.get(2).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType3())) {
                detailDtos.get(2).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType3())) {
                detailDtos.get(2).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo4())){
                    detailDtos.get(3).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType4())) {
                detailDtos.get(3).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType4())) {
                detailDtos.get(3).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo5())){
                    detailDtos.get(4).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType5())) {
                detailDtos.get(4).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType5())) {
                detailDtos.get(4).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo6())){
                    detailDtos.get(5).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType6())) {
                detailDtos.get(5).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType6())) {
                detailDtos.get(5).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo7())){
                    detailDtos.get(6).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType7())) {
                detailDtos.get(6).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType7())) {
                detailDtos.get(6).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo8())){
                    detailDtos.get(7).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType8())) {
                detailDtos.get(7).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType8())) {
                detailDtos.get(7).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo9())){
                    detailDtos.get(8).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType9())) {
                detailDtos.get(8).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType9())) {
                detailDtos.get(8).setEmbryoType("thawed");
            }
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getEmbryo10())){
                    detailDtos.get(9).setEmbryoAge(opt.getValue());
                }
            }
            if ("Fresh Embryo".equals(excelDto.getEmbryoType10())) {
                detailDtos.get(9).setEmbryoType("fresh");
            } else if ("Thawed Embryo".equals(excelDto.getEmbryoType10())) {
                detailDtos.get(9).setEmbryoType("thawed");
            }
            embryoTransferStageDto.setEmbryoTransferDetailDtos(detailDtos);

            embryoTransferStageDto.setFirstTransferDateStr(excelDto.getFirstTransferDate());
            embryoTransferStageDto.setSecondTransferDateStr(excelDto.getSecondTransferDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date firstDate = sdf.parse(excelDto.getFirstTransferDate());
                embryoTransferStageDto.setFirstTransferDate(firstDate);
            } catch (Exception e) {

            }
            try {
                Date secondDate = sdf.parse(excelDto.getSecondTransferDate());
                embryoTransferStageDto.setSecondTransferDate(secondDate);
            } catch (Exception e) {

            }

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
            embryoTransferredOutcomeStageDto.setIdType(embryoTransferredOutcomeStageDto.getIdType());
            embryoTransferredOutcomeStageDto.setIdNo(embryoTransferredOutcomeStageDto.getIdNo());
            for (SelectOption opt: options) {
                if(opt.getText().equals(excelDto.getTransferedOutcome())){
                    embryoTransferredOutcomeStageDto.setTransferedOutcome(opt.getValue());
                }
            }
            result.add(embryoTransferredOutcomeStageDto);
        }
        return result;
    }

    private List<PregnancyOutcomeStageDto> getPregnancyOutcomeStageDto(List<ArOutcomePregnancyExcelDto> outcomePregnancyExcelDtos) {
        if (outcomePregnancyExcelDtos == null) {
            return null;
        }
        List<PregnancyOutcomeStageDto> result = IaisCommonUtils.genNewArrayList(outcomePregnancyExcelDtos.size());
        List<SelectOption> firstUltrasoundOrderShowOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ORDER_IN_ULTRASOUND);
        List<SelectOption> pregnancyOutcomeOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_OUTCOME_OFPREGNANCY);
        List<SelectOption> deliveryModeOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MODE_OF_DELIVERY);
        for (ArOutcomePregnancyExcelDto excelDto: outcomePregnancyExcelDtos) {
            PregnancyOutcomeStageDto dto = new PregnancyOutcomeStageDto();
            for (SelectOption opt: firstUltrasoundOrderShowOptions) {
                if (excelDto.getFirstUltrasoundOrderShow().equals(opt.getText())) {
                    dto.setFirstUltrasoundOrderShow(opt.getValue());
                    break;
                }
            }
            dto.setWasSelFoeReduCarryOut(arBatchUploadCommonService.getBooleanValue(excelDto.getWasSelFoeReduCarryOut()) ? 1 : 0);
            for (SelectOption opt: pregnancyOutcomeOptions) {
                if (excelDto.getPregnancyOutcome().equals(opt.getText())) {
                    dto.setPregnancyOutcome(opt.getValue());
                    break;
                }
            }
            dto.setMaleLiveBirthNum(excelDto.getMaleLiveBirthNum());
            dto.setFemaleLiveBirthNum(excelDto.getFemaleLiveBirthNum());
            dto.setStillBirthNum(excelDto.getStillBirthNum());
            dto.setSpontAbortNum(excelDto.getSpontAbortNum());
            dto.setIntraUterDeathNum(excelDto.getIntraUterDeathNum());
            for (SelectOption opt:deliveryModeOptions) {
                if (excelDto.getDeliveryMode().equals(opt.getText())) {
                    dto.setDeliveryMode(opt.getValue());
                    break;
                }
            }
            try {
                dto.setDeliveryDate(Formatter.parseDate(excelDto.getDeliveryDate()));
            } catch (ParseException e) {

            }
            dto.setDeliveryDateType("Yes".equals(excelDto.getDeliveryDateType()) ? "Unknown" : "Known");
            dto.setBirthPlace(excelDto.getBirthPlace());
            dto.setLocalBirthPlace(excelDto.getLocalBirthPlace());
            dto.setBabyDetailsUnknown(arBatchUploadCommonService.getBooleanValue(excelDto.getBabyDetailsUnknown()));

            // todo babys


            if ("OUTOPRE002".equals(excelDto.getPregnancyOutcome()) || "OUTOPRE003".equals(excelDto.getPregnancyOutcome())) {
                dto.setStillBirthNum(excelDto.getStillBirthNumUn());
                dto.setSpontAbortNum(excelDto.getSpontAbortNumUn());
                dto.setIntraUterDeathNum(excelDto.getIntraUterDeathNumUn());
            }

            dto.setOtherPregnancyOutcome(excelDto.getOtherPregnancyOutcome());

            result.add(dto);
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
            endCycleStageDto.setIdType(excelDto.getIdType());
            endCycleStageDto.setIdNo(excelDto.getIdNo());
            if ("Yes cycle has ended".equals(excelDto.getCycleAbandoned())) {
                endCycleStageDto.setCycleAbandoned(false);
            } else if ("No".equals(excelDto.getCycleAbandoned())) {
                endCycleStageDto.setCycleAbandoned(true);
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

    private List<PgtStageDto> getPgtStageDto(List<PgtStageExcelDto> pgtStageExcelDtos) {
        if (pgtStageExcelDtos == null) {
            return null;
        }
        List<PgtStageDto> result = IaisCommonUtils.genNewArrayList(pgtStageExcelDtos.size());
        for (PgtStageExcelDto excelDto: pgtStageExcelDtos) {
            PgtStageDto pgtStageDto = new PgtStageDto();
            pgtStageDto.setIdType(excelDto.getPatientIdType());
            pgtStageDto.setIdNo(excelDto.getPatientIdNo());
            // todo set pgt value

            pgtStageDto.setIsPgtMCom(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtMCom())?1:0);
            pgtStageDto.setIsPgtMRare(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtMRare())?1:0);
            pgtStageDto.setIsPgtSr(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtSr())?1:0);
            pgtStageDto.setIsPgtA(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtA())?1:0);
            pgtStageDto.setIsPtt(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPtt())?1:0);
            pgtStageDto.setIsOtherPgt(arBatchUploadCommonService.getBooleanValue(excelDto.getIsOtherPgt())?1:0);

            // PGT-M (Common/Rare)
            pgtStageDto.setWorkUpCom(arBatchUploadCommonService.getBooleanValue(excelDto.getWorkUpCom())?1:0);
            pgtStageDto.setEbtCom(arBatchUploadCommonService.getBooleanValue(excelDto.getEbtCom())?1:0);
            pgtStageDto.setWorkUpRare(arBatchUploadCommonService.getBooleanValue(excelDto.getWorkUpRare())?1:0);
            pgtStageDto.setEbtRare(arBatchUploadCommonService.getBooleanValue(excelDto.getEbtRare())?1:0);

            pgtStageDto.setPgtMDateStr(excelDto.getPgtMDate());
            try {
                pgtStageDto.setPgtMDate(Formatter.parseDate(excelDto.getPgtMDate()));
            } catch (ParseException e) {

            }
            pgtStageDto.setIsPgtMDsld(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtMDsld())?1:0);
            pgtStageDto.setIsPgtMWithHla(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtMWithHla())?1:0);
            pgtStageDto.setIsPgtMNon(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtMNon())?1:0);
            pgtStageDto.setPgtMRefNo(excelDto.getPgtMRefNo());
            pgtStageDto.setPgtMCondition(excelDto.getPgtMCondition());
            pgtStageDto.setIsPgtCoFunding(getWhetherCoFunding(excelDto.getIsPgtCoFunding()));
            pgtStageDto.setIsPgtMRareCoFunding(getWhetherCoFunding(excelDto.getIsPgtMRareCoFunding()));
            pgtStageDto.setPgtMAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getPgtMAppeal())?1:0);

            // pgtA
            pgtStageDto.setIsPgtAAma(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtAAma())?1:0);
            pgtStageDto.setIsPgtATomrif(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtATomrif())?1:0);
            pgtStageDto.setIsPgtATomrpl(arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtATomrpl())?1:0);
            pgtStageDto.setPgtAResult(excelDto.getPgtAResult());
            pgtStageDto.setIsPgtACoFunding(String.valueOf((arBatchUploadCommonService.getBooleanValue(excelDto.getIsPgtACoFunding())?1:0)));
            pgtStageDto.setPgtAAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getPgtAAppeal())?1:0);

            // Pgt sr
            pgtStageDto.setPgtSrDateStr(pgtStageDto.getPgtSrDate());
            try {
                pgtStageDto.setPgtSrDate(Formatter.parseDate(excelDto.getPgtSrDate()));
            } catch (ParseException e) {

            }
            pgtStageDto.setPgtSrRefNo(excelDto.getPgtSrRefNo());
            pgtStageDto.setPgtSrCondition(excelDto.getPgtSrCondition());
            pgtStageDto.setIsPgtSrCoFunding(getWhetherCoFunding(excelDto.getIsPgtSrCoFunding()));
            pgtStageDto.setPgtSrAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getPgtSrAppeal())?1:0);

            // ptt
            pgtStageDto.setPttCondition(excelDto.getPttCondition());
            pgtStageDto.setIsPttCoFunding(getWhetherCoFunding(excelDto.getIsPttCoFunding()));
            pgtStageDto.setPgtPttAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getPgtPttAppeal())?1:0);

            // other
            pgtStageDto.setIsEmbryosBiopsiedLocal(excelDto.getIsEmbryosBiopsiedLocal()); // premise map
            pgtStageDto.setOtherEmbryosBiopsiedAddr(excelDto.getOtherEmbryosBiopsiedAddr());
            pgtStageDto.setIsBiopsyLocal(excelDto.getIsBiopsyLocal()); // premise
            pgtStageDto.setOtherBiopsyAddr(excelDto.getOtherBiopsyAddr());

            result.add(pgtStageDto);
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
            arTreatmentSubsidiesStageDto.setIdType(excelDto.getIdType());
            arTreatmentSubsidiesStageDto.setIdNo(excelDto.getIdNo());
            for (SelectOption option: coFundings) {
                if (option.getText().equals(excelDto.getCoFunding())) {
                    arTreatmentSubsidiesStageDto.setCoFunding(option.getValue());
                }
            }
            if (StringUtil.isEmpty(arTreatmentSubsidiesStageDto.getCoFunding())) {
                arTreatmentSubsidiesStageDto.setCoFunding(excelDto.getCoFunding());
            }
            arTreatmentSubsidiesStageDto.setIsThereAppeal(arBatchUploadCommonService.getBooleanValue(excelDto.getIsThereAppeal()));
            result.add(arTreatmentSubsidiesStageDto);
        }
        return result;
    }

    private List<DonationStageDto> getDonationStageDto(List<DonationStageExcelDto> donationStageExcelDtos) {
        if (donationStageExcelDtos == null) {
            return null;
        }
        List<DonationStageDto> result = IaisCommonUtils.genNewArrayList(donationStageExcelDtos.size());
        for (DonationStageExcelDto excelDto: donationStageExcelDtos) {
            DonationStageDto donationStageDto = new DonationStageDto();


            result.add(donationStageDto);
        }
        return result;
    }

    private List<TransferInOutStageDto> getTransferInOutDto(List<ArTransferInOutExcelDto> arTransferInOutExcelDtos, HttpServletRequest request) {
        if (arTransferInOutExcelDtos == null) {
            return null;
        }
        List<TransferInOutStageDto> result = IaisCommonUtils.genNewArrayList(arTransferInOutExcelDtos.size());
        for (ArTransferInOutExcelDto excelDto: arTransferInOutExcelDtos) {
            TransferInOutStageDto transferInOutStageDto = new TransferInOutStageDto();
            transferInOutStageDto.setIdType(excelDto.getIdType());
            transferInOutStageDto.setIdNo(excelDto.getIdNumber());
            if (StringUtil.isNotEmpty(excelDto.getTransferType())){
                if ("Transfer In".equals(excelDto.getTransferType())){
                    transferInOutStageDto.setTransferType(DataSubmissionConsts.TRANSFER_TYPE_IN);
                } else {
                    transferInOutStageDto.setTransferType(DataSubmissionConsts.TRANSFER_TYPE_OUT);
                }
            }
            List<String> transferredList = IaisCommonUtils.genNewArrayList();
            if (StringUtil.isNotEmpty(excelDto.getIsOocyteTransfer()) && "Yes".equals(excelDto.getIsOocyteTransfer())){
                transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES);
            }
            if (StringUtil.isNotEmpty(excelDto.getIsEmbryoTransfer()) && "Yes".equals(excelDto.getIsEmbryoTransfer())){
                transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS);
            }
            if (StringUtil.isNotEmpty(excelDto.getIsSpermTransfer()) && "Yes".equals(excelDto.getIsSpermTransfer())){
                transferredList.add(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM);
            }
            transferInOutStageDto.setTransferredList(transferredList);

            transferInOutStageDto.setFromDonor(StringUtil.isNotEmpty(excelDto.getIsDonor()) && "Yes".equals(excelDto.getIsDonor()));
            setTransferInOutName(excelDto, request, transferInOutStageDto);
            transferInOutStageDto.setOocyteNum(excelDto.getOocyteNum());
            transferInOutStageDto.setEmbryoNum(excelDto.getEmbryoNum());
            transferInOutStageDto.setSpermVialsNum(excelDto.getSpermVialsNum());
            transferInOutStageDto.setTransferDate(excelDto.getDateTransfer());
            result.add(transferInOutStageDto);
        }
        return result;
    }

    private List<ArSubFreezingStageDto> getFreezingDto(List<ArFreezingStageExcelDto> freezingExcelDtos) {
        if (freezingExcelDtos == null) {
            return null;
        }
        List<ArSubFreezingStageDto> result = IaisCommonUtils.genNewArrayList(freezingExcelDtos.size());
        for (ArFreezingStageExcelDto excelDto: freezingExcelDtos) {
            ArSubFreezingStageDto freezingStageDto = new ArSubFreezingStageDto();

            result.add(freezingStageDto);
        }
        return result;
    }

    private List<DisposalStageDto> getDisposalStageDto(List<ArDisposalStageExcelDto> disposalStageExcelDtos) {
        if (disposalStageExcelDtos == null) {
            return null;
        }
        List<DisposalStageDto> result = IaisCommonUtils.genNewArrayList(disposalStageExcelDtos.size());
        for (ArDisposalStageExcelDto excelDto: disposalStageExcelDtos) {
            DisposalStageDto disposalStageDto = new DisposalStageDto();

            result.add(disposalStageDto);
        }
        return result;
    }

    private void setTransferInOutName(ArTransferInOutExcelDto dto, HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
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
    private String getLicenseeId(String orgId) {
        LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(orgId);
        return licenseeDto.getId();
    }

    private void setTransFromLicenseeIdAndHicCode(ArTransferInOutExcelDto dto, TransferInOutStageDto transferInOutStageDto, List<SelectOption> premisesSel,String type) {
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

    public static Map<String, ExcelPropertyDto> getFieldCellMap(Class<?> clazz) {
        Map<String, ExcelPropertyDto> map = IaisCommonUtils.genNewHashMap();
        if (clazz == null) {
            return map;
        }
        ExcelSheetProperty annotation = clazz.getAnnotation(ExcelSheetProperty.class);
        int sheetAt = annotation.sheetAt() + 1;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                map.put(field.getName(), new ExcelPropertyDto(sheetAt,excelProperty.cellIndex(), excelProperty.cellName(), field.getName()));
            }
        }
        return map;
    }

    private String getWhetherCoFunding(String coFunding) {
        if ("Yes".equals(coFunding)) {
            return "Y";
        } else if ("No".equals(coFunding)) {
            return "N";
        } else if ("N/A".equals(coFunding)) {
            return "NA";
        }
        return coFunding;
    }
}
