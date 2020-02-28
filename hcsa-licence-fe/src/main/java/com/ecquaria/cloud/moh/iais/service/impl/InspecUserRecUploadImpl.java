package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
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
            checklistItemDtos = getCheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    @Override
    public void submitRecByUser(LoginContext loginContext, InspecUserRecUploadDto inspecUserRecUploadDto) {
        String appNo = inspecUserRecUploadDto.getAppNo();
        ApplicationViewDto applicationViewDto = applicationClient.searchAppByNo(appNo).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<AppPremPreInspectionNcDocDto> appNcDocDtoList = inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos();
        for(AppPremPreInspectionNcDocDto appNcDocDto : appNcDocDtoList) {
            appNcDocDto.setId(null);
            appNcDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appNcDocDto.setSubmitBy(loginContext.getUserId());
            appNcDocDto.setSubmitDt(new Date());
        }
        AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
        appPremisesPreInspectionNcItemDto.setRemarks(inspecUserRecUploadDto.getUploadRemarks());
        appPremisesPreInspectionNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
        inspecUserRecUploadDto.setApplicationDto(applicationDto);
        inspecUserRecUploadDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationDto = applicationClient.updateApplication(applicationDto).getEntity();
        appPremisesPreInspectionNcItemDto = applicationClient.updateAppPreItemNc(appPremisesPreInspectionNcItemDto).getEntity();
        appNcDocDtoList = applicationClient.saveAppNcDoc(appNcDocDtoList).getEntity();
        inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appNcDocDtoList);

        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();
        inspRectificationSaveDto.setApplicationDto(applicationDto);
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
        inspRectificationSaveDto.setAppPremPreInspectionNcDocDtos(appNcDocDtoList);
        inspRectificationSaveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());


    }

    @Override
    public ApplicationDto getApplicationByCorrId(String appPremCorrId) {
        return applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
    }

    @Override
    public InspecUserRecUploadDto saveFileReportGetFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String auditTrailStr) {
        String fileReportId = fileRepoClient.saveFiles(inspecUserRecUploadDto.getRecFile(), auditTrailStr).getEntity();
        List<String> ids = new ArrayList<>();
        ids.add(fileReportId);
        FileRepoDto fileRepoDto = fileRepoClient.getFilesByIds(ids).getEntity().get(0);
        if(inspecUserRecUploadDto.getFileRepoDtos() == null){
            List<FileRepoDto> fileRepoDtos = new ArrayList<>();
            fileRepoDtos.add(fileRepoDto);
            inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
        } else {
            inspecUserRecUploadDto.getFileRepoDtos().add(fileRepoDto);
        }
        AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto = new AppPremPreInspectionNcDocDto();
        appPremPreInspectionNcDocDto.setId(null);
        appPremPreInspectionNcDocDto.setDocName(inspecUserRecUploadDto.getFileName());
        appPremPreInspectionNcDocDto.setDocSize(inspecUserRecUploadDto.getFileSize());
        appPremPreInspectionNcDocDto.setFileRepoId(fileReportId);
        appPremPreInspectionNcDocDto.setNcItemId(inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto().getId());

        if(inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos() == null){
            List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = new ArrayList<>();
            appPremPreInspectionNcDocDtos.add(appPremPreInspectionNcDocDto);
            inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
        } else {
            inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos().add(appPremPreInspectionNcDocDto);
        }
        return inspecUserRecUploadDto;
    }

    @Override
    public InspecUserRecUploadDto getNcItemData(InspecUserRecUploadDto inspecUserRecUploadDto) {
        AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = applicationClient.getNcItemByItemId(inspecUserRecUploadDto.getItemId()).getEntity();
        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
        return inspecUserRecUploadDto;
    }

    @Override
    public InspecUserRecUploadDto removeFileByFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String removeId) {
        fileRepoClient.removeFileById(removeId);
        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos();
        List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
        for(int i= 0; i < appPremPreInspectionNcDocDtos.size(); i++){
            if(removeId.equals(appPremPreInspectionNcDocDtos.get(i).getFileRepoId())){
                appPremPreInspectionNcDocDtos.remove(i);
            }
        }
        for(int i= 0; i < fileRepoDtos.size(); i++){
            if(removeId.equals(fileRepoDtos.get(i).getId())){
                fileRepoDtos.remove(i);
            }
        }
        inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
        inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
        return inspecUserRecUploadDto;
    }

    private List<ChecklistItemDto> getCheckDtosByItemIds(List<String> itemIds) {
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
