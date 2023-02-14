package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.dto.DisposalExcelDto;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DisposalCycleUploadService {

    List<DisposalExcelDto> getDisposalExcelDtoList(Map.Entry<String, File> fileEntry);

    /**
     * Transfer to patient info dto from transfer in out cycle excel dto
     * And map value to code for some fields (drowndrop)
     * @param disposalExcelDtoList
     * @return
     */
    List<DisposalStageDto> getDisposalStageDtoList(List<DisposalExcelDto> disposalExcelDtoList);

    void validateEfoCycleStageDto(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto, Map<String, ExcelPropertyDto> fieldCellMap, int i);
}
