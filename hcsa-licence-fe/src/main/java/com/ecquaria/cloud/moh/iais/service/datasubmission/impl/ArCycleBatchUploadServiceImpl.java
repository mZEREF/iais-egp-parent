package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.*;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArCycleBatchUploadService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
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
            Map.Entry<String, File> fileEntry = getFileEntry(bpc.request);
            PageShowFileDto pageShowFileDto = getPageShowFileDto(fileEntry);
            ParamUtil.setSessionAttr(bpc.request, PAGE_SHOW_FILE, pageShowFileDto);
            errorMap = DataSubmissionHelper.validateFile(SEESION_FILES_MAP_AJAX, bpc.request);
            if (errorMap.isEmpty()) {
                //1. excelDto validation

                //2. dto validation
                List<ArCycleStageExcelDto> arCycleStageExcelDtos = getExcelDtoList(fileEntry, ArCycleStageExcelDto.class);
                List<ThawingStageExcelDto> thawingStageExcelDtos = getExcelDtoList(fileEntry, ThawingStageExcelDto.class);
                List<ArOocyteRetrievalExcelDto> oocyteRetrievalExcelDtos = getExcelDtoList(fileEntry, ArOocyteRetrievalExcelDto.class);
                List<FertilisationStageExcelDto> fertilisationStageExcelDtos = getExcelDtoList(fileEntry, FertilisationStageExcelDto.class);
                List<EmbryoCreatedExcelDto> embryoCreatedExcelDtos = getExcelDtoList(fileEntry, EmbryoCreatedExcelDto.class);
                List<EmbryoTransferExcelDto> embryoTransferExcelDtos = getExcelDtoList(fileEntry, EmbryoTransferExcelDto.class);
                List<OutcomeTransferExcelDto> outcomeTransferExcelDtos = getExcelDtoList(fileEntry, OutcomeTransferExcelDto.class);
                List<ArOutcomePregnancyExcelDto> outcomeOfPregnancyExcelDtos = getExcelDtoList(fileEntry, ArOutcomePregnancyExcelDto.class);
                List<EndCycleStageExcelDto> endCycleStageExcelDtos = getExcelDtoList(fileEntry,EndCycleStageExcelDto.class);
                List<PgtStageExcelDto> pgtStageExcelDtos = getExcelDtoList(fileEntry, PgtStageExcelDto.class);
                List<ArCoFundingExcelDto> arCoFundingExcelDtos = getExcelDtoList(fileEntry, ArCoFundingExcelDto.class);
                List<DonationStageExcelDto> donationStageExcelDtos = getExcelDtoList(fileEntry, DonationStageExcelDto.class);
                List<ArTransferInOutExcelDto> arTransferInOutExcelDtos = getExcelDtoList(fileEntry, ArTransferInOutExcelDto.class);
                List<ArFreezingStageExcelDto> arFreezingStageExcelDtos = getExcelDtoList(fileEntry, ArFreezingStageExcelDto.class);
                List<ArDisposalStageExcelDto> arDisposalStageExcelDtos = getExcelDtoList(fileEntry, ArDisposalStageExcelDto.class);

                int arFileItemSize = arCycleStageExcelDtos.size();
                int thawingFileItemSize = thawingStageExcelDtos.size();
                if (arFileItemSize == 0) {
                    errorMap.put("uploadFileError", "PRF_ERR006");
                } else if (arFileItemSize > 200) {
                    errorMap.put("uploadFileError", MessageUtil.replaceMessage("GENERAL_ERR0052",
                            Formatter.formatNumber(200, "#,##0"), "maxCount"));
                } else {
                    arCycleStageDtoDtos = getArCycleStageDto(arCycleStageExcelDtos, bpc.request);
                    thawingStageDtos = getThawingStageDto(thawingStageExcelDtos);

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

    private void validateArStage(List<FileErrorMsg> errorMsgs,ArCycleStageDto arCycleStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){

    }

    private void validateThawingStage(List<FileErrorMsg> errorMsgs,ThawingStageDto thawingStageDto,Map<String, ExcelPropertyDto> fieldCellMap,int i){

    }

    private Map.Entry<String, File> getFileEntry(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, SEESION_FILES_MAP_AJAX);
        if (fileMap == null || fileMap.isEmpty()) {
            return null;
        }
        // only one
        Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Map.Entry<String, File> next = iterator.next();
        File file = next.getValue();
        long length = file.length();
        if (length == 0) {
            return null;
        }
        return next;
    }

    private PageShowFileDto getPageShowFileDto(Map.Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return null;
        }
        File file = fileEntry.getValue();
        PageShowFileDto pageShowFileDto = new PageShowFileDto();
        String index = fileEntry.getKey().substring(FILE_APPEND.length());
        String fileMd5 = FileUtils.getFileMd5(file);
        pageShowFileDto.setIndex(index);
        pageShowFileDto.setFileName(file.getName());
        pageShowFileDto.setFileMapId(FILE_APPEND + "Div" + index);
        pageShowFileDto.setSize((int) (file.length() / 1024));
        pageShowFileDto.setMd5Code(fileMd5);
        List<String> list = arDataSubmissionService.saveFileRepo(Collections.singletonList(file));
        if (!list.isEmpty()) {
            pageShowFileDto.setFileUploadUrl(list.get(0));
        }
        return pageShowFileDto;
    }

    private <T> List<T> getExcelDtoList(Map.Entry<String, File> fileEntry,Class<T> tClass) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), tClass, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), tClass, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
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



    private List<ArCycleStageDto> getArCycleStageDto(List<ArCycleStageExcelDto> arCycleStageExcelDtos,HttpServletRequest request) {
        if(arCycleStageExcelDtos == null) {
            return null;
        }
        List<ArCycleStageDto> result = IaisCommonUtils.genNewArrayList(arCycleStageExcelDtos.size());
        for (ArCycleStageExcelDto excelDto: arCycleStageExcelDtos) {
            PatientInfoDto patientInfoDto = arBatchUploadCommonService.setPatientInfo(excelDto.getPatientIdType(), excelDto.getPatientIdNo(), request);
            ArCycleStageDto arCycleStageDto = new ArCycleStageDto();
            DonorDto donorDto1 = new DonorDto();
            DonorDto donorDto2 = new DonorDto();
            arCycleStageDto.setStartDate(excelDto.getStartDate());
            arCycleStageDto.setMainIndication(excelDto.getMainIndication());
            arCycleStageDto.setMainIndicationOthers(excelDto.getMainIndicationOthers());
            // todo otherIndication
            arCycleStageDto.setInVitroMaturation(excelDto.getBooleanValue(excelDto.getInVitroMaturation()));
            arCycleStageDto.setTreatmentFreshNatural(excelDto.getBooleanValue(excelDto.getTreatmentFreshNatural()));
            arCycleStageDto.setTreatmentFreshStimulated(excelDto.getBooleanValue(excelDto.getTreatmentFreshStimulated()));
            arCycleStageDto.setTreatmentFrozenOocyte(excelDto.getBooleanValue(excelDto.getTreatmentFrozenOocyte()));
            arCycleStageDto.setTreatmentFrozenEmbryo(excelDto.getBooleanValue(excelDto.getTreatmentFrozenEmbryo()));
            Double currentMarriageChildren = Double.valueOf(excelDto.getCurrentMarriageChildren());
            Double previousMarriageChildren = Double.valueOf(excelDto.getPreviousMarriageChildren());
            Double deliveredThroughChildren = Double.valueOf(excelDto.getDeliveredThroughChildren());
            Double totalPreviouslyPreviously = Double.valueOf(excelDto.getTotalPreviouslyPreviously());
            arCycleStageDto.setCurrentMarriageChildren((int) Math.ceil(currentMarriageChildren));
            arCycleStageDto.setPreviousMarriageChildren((int) Math.ceil(previousMarriageChildren));
            arCycleStageDto.setDeliveredThroughChildren((int) Math.ceil(deliveredThroughChildren));
            arCycleStageDto.setTotalPreviouslyPreviously((int) Math.ceil(totalPreviouslyPreviously));
            arCycleStageDto.setCyclesUndergoneOverseas(excelDto.getCyclesUndergoneOverseas());

            arCycleStageDto.setEnhancedCounselling(excelDto.getBooleanValue(excelDto.getEnhancedCounselling()));

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
            thawingStageDto.setHasOocyte(arBatchUploadCommonService.getBooleanValue(excelDto.getThawedOocytes()));
            thawingStageDto.setHasEmbryo(arBatchUploadCommonService.getBooleanValue(excelDto.getThawingEmbryos()));

            thawingStageDto.setThawedOocytesNum(excelDto.getThawedOocytesNum());
            thawingStageDto.setThawedOocytesSurvivedMatureNum(excelDto.getThawedOocytesSurvivedMatureNum());
            thawingStageDto.setThawedOocytesSurvivedImmatureNum(excelDto.getThawedOocytesSurvivedImmatureNum());
            thawingStageDto.setThawedOocytesSurvivedOtherNum(excelDto.getThawedOocytesSurvivedOtherNum());
            thawingStageDto.setThawedEmbryosNum(excelDto.getThawedEmbryosNum());
            thawingStageDto.setThawedEmbryosSurvivedNum(excelDto.getThawedEmbryosSurvivedNum());
            result.add(thawingStageDto);
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
