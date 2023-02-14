package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DisposalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.DisposalExcelDto;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DisposalCycleUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * DisposalCycleUploadServiceImpl
 *
 * @Author Shufei
 * @Date 2023/2/8 15:25
 **/
@Slf4j
@Service
public class DisposalCycleUploadServiceImpl implements DisposalCycleUploadService {

    @Override
    public List<DisposalExcelDto> getDisposalExcelDtoList(Map.Entry<String, File> fileEntry) {
        if (fileEntry == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        try {
            File file = fileEntry.getValue();
            if (FileUtils.isExcel(file.getName())) {
                return FileUtils.transformToJavaBean(fileEntry.getValue(), DisposalExcelDto.class, true);
            } else if (FileUtils.isCsv(file.getName())) {
                return FileUtils.transformCsvToJavaBean(fileEntry.getValue(), DisposalExcelDto.class, true);
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return IaisCommonUtils.genNewArrayList(0);
    }

    @Override
    public List<DisposalStageDto> getDisposalStageDtoList(List<DisposalExcelDto> disposalExcelDtoList) {
        if (IaisCommonUtils.isEmpty(disposalExcelDtoList)){
            return null;
        }
        List<DisposalStageDto> result = IaisCommonUtils.genNewArrayList();
        disposalExcelDtoList.stream().forEach(dto ->{
            DisposalStageDto disposalStageDto = new DisposalStageDto();
            disposalStageDto.setDisposedType(dto.getDisposedItem());
            disposalStageDto.setImmatureString(dto.getNoImmatureDisposed());
            disposalStageDto.setAbnormallyFertilisedString(dto.getNoAbnormallyFertilisedDisposed());
            disposalStageDto.setUnfertilisedString(dto.getNoUnfertilisedDisposed());
            disposalStageDto.setAtreticString(dto.getNoAtreticDisposed());
            disposalStageDto.setDamagedString(dto.getNoDamagedDisposed());
            disposalStageDto.setLysedOrDegeneratedString(dto.getNoLysedDegeneratedDisposed());
            disposalStageDto.setUnhealthyNumString(dto.getNoPoorQualityUnhealthyAbnormalDisposed());
            disposalStageDto.setOtherDiscardedNumString(dto.getDiscardedForOtherReasons());
            disposalStageDto.setOtherDiscardedReason(dto.getOtherReasonsForDiscarding());
        });
        return result;
    }

    @Override
    public void validateEfoCycleStageDto(List<FileErrorMsg> errorMsgs, DisposalStageDto disposalStageDto,
                                         Map<String, ExcelPropertyDto> fieldCellMap, int i) {

    }
}
