package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/23 15:25
 **/
@Service
@Slf4j
public class InspecUserRecUploadImpl implements InspecUserRecUploadService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Override
    public List<ChecklistItemDto> getQuesAndClause(String appNo) {
        List<ChecklistItemDto> checklistItemDtos = new ArrayList<>();
        if(!(StringUtil.isEmpty(appNo))){
            List<String> itemIds = applicationClient.getItemIdsByAppNo(appNo).getEntity();
            checklistItemDtos = getcheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    @Override
    public void submitRecByUser(LoginContext loginContext, String auditTrailStr, List<InspecUserRecUploadDto> inspecUserRecUploadDtos) {
        String appNo = inspecUserRecUploadDtos.get(0).getAppNo();
        ApplicationViewDto applicationViewDto = applicationClient.searchAppByNo(appNo).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.listAppPremisesCorrelation(applicationDto.getId()).getEntity().get(0);
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = applicationClient.getAppPremPreInsNcDtoByAppCorrId(appPremisesCorrelationDto.getId()).getEntity();
        AppPremPreInspectionNcDto aDto = createAndUpdateAppPreDto(appPremPreInspectionNcDto);
        List<AppPremPreInspectionNcDocDto> appNcDocDtoList = new ArrayList<>();
        for(InspecUserRecUploadDto iDto : inspecUserRecUploadDtos){
            AppPremisesPreInspectionNcItemDto appNcItemDto = new AppPremisesPreInspectionNcItemDto();
            appNcItemDto.setId(null);
            appNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appNcItemDto.setIsRecitfied(1);
            appNcItemDto.setItemId(iDto.getItemId());
            appNcItemDto.setPreNcId(aDto.getId());
            appNcItemDto = applicationClient.createAppNcItemDto(appNcItemDto).getEntity();
            String fileReportId = fileRepoClient.saveFiles(iDto.getRecFile(), auditTrailStr).getEntity();
            AppPremPreInspectionNcDocDto appNcDocDto = new AppPremPreInspectionNcDocDto();
            appNcDocDto.setId(null);
            appNcDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appNcDocDto.setDocName(iDto.getFileName());
            appNcDocDto.setDocSize(iDto.getFileSize());
            appNcDocDto.setFileRepoId(fileReportId);
            appNcDocDto.setSubmitBy(loginContext.getUserId());
            appNcDocDto.setSubmitDt(new Date());
            appNcDocDto.setInternalRemarks(iDto.getUploadRemarks());
            appNcDocDto.setNcItemId(appNcItemDto.getId());
            appNcDocDtoList.add(appNcDocDto);
        }
        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationClient.updateApplication(applicationDto);
        applicationClient.saveAppNcDoc(appNcDocDtoList).getEntity();
    }

    private AppPremPreInspectionNcDto createAndUpdateAppPreDto(AppPremPreInspectionNcDto appPremPreInspectionNcDto) {
        appPremPreInspectionNcDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        appPremPreInspectionNcDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremPreInspectionNcDto = applicationClient.updateAppPremPreNc(appPremPreInspectionNcDto).getEntity();
        AppPremPreInspectionNcDto aDto = new AppPremPreInspectionNcDto();
        aDto.setId(null);
        aDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        aDto.setVersion(appPremPreInspectionNcDto.getVersion() + 1);
        aDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        aDto.setAppPremCorrId(appPremPreInspectionNcDto.getAppPremCorrId());
        aDto = applicationClient.saveAppPremPreNc(aDto).getEntity();
        return aDto;
    }

    private List<ChecklistItemDto> getcheckDtosByItemIds(List<String> itemIds) {
        List<ChecklistItemDto> checklistItemDtos = new ArrayList<>();
        if(itemIds != null && !(itemIds.isEmpty())) {
            for (String itemId:itemIds) {
                ChecklistItemDto checklistItemDto = appConfigClient.getChklItemById(itemId).getEntity();
                checklistItemDtos.add(checklistItemDto);
            }
        }
        return checklistItemDtos;
    }
}
