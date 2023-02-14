package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.TransferInOutExcelDto;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TransferInOutCycleUploadService {

    List<TransferInOutExcelDto> getTransferInOutExcelDtoList(Map.Entry<String, File> fileEntry);

    /**
     * Transfer to patient info dto from transfer in out cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param transferInOutExcelDtoList
     * @return
     */
    List<TransferInOutStageDto> getTransferInOutStageDtoList(List<TransferInOutExcelDto> transferInOutExcelDtoList);

    void validateEfoCycleStageDto(List<FileErrorMsg> errorMsgs, TransferInOutStageDto transferInOutStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i);
}
