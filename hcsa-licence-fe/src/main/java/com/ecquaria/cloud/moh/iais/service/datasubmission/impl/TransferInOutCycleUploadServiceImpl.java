package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.TransferInOutExcelDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TransferInOutCycleUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * TransferInOutCycleUploadServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/8 15:22
 **/
@Slf4j
@Service
public class TransferInOutCycleUploadServiceImpl implements TransferInOutCycleUploadService {

    @Override
    public List<TransferInOutExcelDto> getTransferInOutExcelDtoList(Map.Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), TransferInOutExcelDto.class, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), TransferInOutExcelDto.class, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
    }

    /**
     * Transfer to patient info dto from transfer in out cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param transferInOutExcelDtoList
     * @return
     */
    @Override
    public List<TransferInOutStageDto> getTransferInOutStageDtoList(List<TransferInOutExcelDto> transferInOutExcelDtoList) {
        if (IaisCommonUtils.isEmpty(transferInOutExcelDtoList)){
            return null;
        }
        List<TransferInOutStageDto> result = IaisCommonUtils.genNewArrayList();
        transferInOutExcelDtoList.stream().forEach(dto ->{
            TransferInOutStageDto transferInOutStageDto = setTransferInOutStageDto(dto);
            result.add(transferInOutStageDto);
        });
        return result;
    }

    private TransferInOutStageDto setTransferInOutStageDto(TransferInOutExcelDto dto) {
        TransferInOutStageDto transferInOutStageDto = new TransferInOutStageDto();
        transferInOutStageDto.setTransferType(dto.getTransferType());
        List<String> transferredList = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isNotEmpty(dto.getIsOocyteTransfer())){
            transferredList.add(dto.getIsOocyteTransfer());
        }
        if (StringUtil.isNotEmpty(dto.getIsEmbryoTransfer())){
            transferredList.add(dto.getIsEmbryoTransfer());
        }
        if (StringUtil.isNotEmpty(dto.getIsSpermTransfer())){
            transferredList.add(dto.getIsSpermTransfer());
        }
        transferInOutStageDto.setTransferredList(transferredList);
        transferInOutStageDto.setOocyteNum(String.valueOf(dto.getOocyteNum()));
        transferInOutStageDto.setEmbryoNum(String.valueOf(dto.getEmbryoNum()));
        transferInOutStageDto.setSpermVialsNum(String.valueOf(dto.getSpermVialsNum()));
        transferInOutStageDto.setFromDonor(AppConsts.YES.equals(dto.getIsDonor()));
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_IN.equals(dto.getTransferType())){
            transferInOutStageDto.setTransInFromHciCode(dto.getTransferInOut());
        }
        if (StringUtil.isNotEmpty(dto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(dto.getTransferType())){
            transferInOutStageDto.setTransOutToHciCode(dto.getTransferInOut());
        }
        transferInOutStageDto.setTransferDate(dto.getDateTransfer());
        return transferInOutStageDto;
    }

    @Override
    public void validateEfoCycleStageDto(List<FileErrorMsg> errorMsgs, TransferInOutStageDto transferInOutStageDto,
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
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferredList"), errMsgErr006));
        } else if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)
                && StringUtil.isEmpty(transferInOutStageDto.getOocyteNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("oocyteNum"), errMsgErr006));
        } else if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)
                && StringUtil.isEmpty(transferInOutStageDto.getEmbryoNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("embryoNum"), errMsgErr006));
        } else if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)
                && StringUtil.isEmpty(transferInOutStageDto.getSpermVialsNum())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("spermVialsNum"), errMsgErr006));
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
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transInFromHciCode"), errMsgErr006));
        }

        if (StringUtil.isNotEmpty(transferInOutStageDto.getTransferType())
                && DataSubmissionConsts.TRANSFER_TYPE_OUT.equals(transferInOutStageDto.getTransferType())
                && StringUtil.isEmpty(transferInOutStageDto.getTransOutToHciCode())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transOutToHciCode"), errMsgErr006));
        }

        if (StringUtil.isEmpty(transferInOutStageDto.getTransferDate())){
            errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferDate"), errMsgErr006));
        } else {
            try {
                Formatter.parseDate(transferInOutStageDto.getTransferDate());
            } catch (ParseException e) {
                errorMsgs.add(new FileErrorMsg(i, fieldCellMap.get("transferDate"), "GENERAL_ERR0033"));
            }
        }
    }
}
