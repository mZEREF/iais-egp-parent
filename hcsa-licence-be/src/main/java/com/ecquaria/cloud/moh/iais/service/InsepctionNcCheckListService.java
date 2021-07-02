package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:46
 */
public interface InsepctionNcCheckListService {

    InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto,AppPremisesPreInspectChklDto appPremDto,List<AppPremisesPreInspectionNcItemDto> itemDtoList, AppPremisesRecommendationDto appPremisesRecommendationDto);
    AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId,String configId);
    List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId);
    AppPremisesRecommendationDto getAppRecomDtoByAppCorrId(String appCorrId,String type);
    void submit(InspectionFillCheckListDto infillDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId);
    List<NcAnswerDto> getNcAnswerDtoList(String appPremCorrId);
    void updateTaskStatus(ApplicationDto applicationDto,String appPremCorrId);
    void getCommonDto(InspectionFillCheckListDto commonDto, AppPremisesPreInspectChklDto appPremPreCklDto);
    AdCheckListShowDto getAdhocCheckListDto(String appPremCorrId);
    boolean isHaveNcOrBestPractice(InspectionFDtosDto serListDto, InspectionFillCheckListDto comDto, AdCheckListShowDto showDto);
    void  getInspectionFillCheckListDtoForShow(InspectionFillCheckListDto inspectionFillCheckListDto);
    String saveFiles( MultipartFile multipartFile);
    void removeFiles(String id);
    void deleteInvalidFile( InspectionFDtosDto serListDto);
    void saveLicPremisesAuditDtoByApplicationViewDto(ApplicationViewDto applicationViewDto);

    void saveRimRiskCountByLicenseeId(String licenseeId,boolean isAdd);
    void saveDraftChecklist(InspectionFillCheckListDto infillDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId);

    void saveBeforeSubmitCheckList(InspectionFillCheckListDto infillDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId);
    void saveOtherDataForPengingIns(InspectionFDtosDto serListDto,String appPremId);
    void saveBeforeFinishCheckListRec(String appPremId,String mobileRemarks);

    void submitSpecService(InspectionFillCheckListDto infillDto, List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFDtosDto serListDto,AdCheckListShowDto adCheckListShowDto, String appPremId);

    void saveBeforeSubmitSpecCheckList(InspectionFillCheckListDto infillDto, List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFDtosDto serListDto, AdCheckListShowDto adCheckListShowDto,String appPremId);

    void saveDraftSpecChecklist(InspectionFillCheckListDto infillDto, String appPremId, List<InspectionSpecServiceDto> inspectionSpecServiceDtos,AdCheckListShowDto showDto, InspectionFDtosDto serListDto);

    void saveTcuDate(String appPremId,String tcu);
}
