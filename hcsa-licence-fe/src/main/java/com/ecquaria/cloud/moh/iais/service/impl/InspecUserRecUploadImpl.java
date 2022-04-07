package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicFeInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private LicFeInboxClient licFeInboxClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private InspectionFeClient inspectionFeClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Override
    public List<ChecklistItemDto> getQuesAndClause(String appNo) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        if(!(StringUtil.isEmpty(appNo))){
            List<String> itemIds = applicationFeClient.getItemIdsByAppNo(appNo).getEntity();
            checklistItemDtos = getCheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    @Override
    public void submitRecByUser(LoginContext loginContext, InspecUserRecUploadDto inspecUserRecUploadDto) {
        //remove file and ncDoc, by delete file
        List<AppPremPreInspectionNcDocDto> appNcDocDtoList = removeNcDocByDelFileId(inspecUserRecUploadDto);
        for(AppPremPreInspectionNcDocDto appNcDocDto : appNcDocDtoList) {
            appNcDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appNcDocDto.setSubmitBy(loginContext.getUserId());
            appNcDocDto.setSubmitDt(new Date());
        }
        appNcDocDtoList = applicationFeClient.saveAppNcDoc(appNcDocDtoList).getEntity();
        inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appNcDocDtoList);

        //update nc item
        AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
        appPremisesPreInspectionNcItemDto.setFeRemarks(inspecUserRecUploadDto.getUploadRemarks());
        appPremisesPreInspectionNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesPreInspectionNcItemDto = applicationFeClient.updateAppPreItemNc(appPremisesPreInspectionNcItemDto).getEntity();

        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
        inspecUserRecUploadDto.setFileDelCelIds(null);
        inspecUserRecUploadDto.setFileRepoDelIds(null);
        inspecUserRecUploadDto.setRectifyFlag(AppConsts.SUCCESS);
    }

    private List<AppPremPreInspectionNcDocDto> removeNcDocByDelFileId(InspecUserRecUploadDto inspecUserRecUploadDto) {
        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos();
        List<String> removeIds = inspecUserRecUploadDto.getFileDelCelIds();
        if(!IaisCommonUtils.isEmpty(removeIds)){
            for(int r = 0; r < removeIds.size(); r++){
                for(int i= 0; i < appPremPreInspectionNcDocDtos.size(); i++){
                    if(removeIds.get(r).equals(appPremPreInspectionNcDocDtos.get(i).getFileRepoId())){
                        AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto = appPremPreInspectionNcDocDtos.get(i);
                        if(!StringUtil.isEmpty(appPremPreInspectionNcDocDto.getId())){
                            List<String> ids = IaisCommonUtils.genNewArrayList();
                            ids.add(appPremPreInspectionNcDocDto.getId());
                            inspectionFeClient.deleteNcDocByIds(ids);
                        }
                        appPremPreInspectionNcDocDtos.remove(i);
                        i--;
                    }
                }
                List<String> deleteIds = IaisCommonUtils.genNewArrayList();
                deleteIds.add(removeIds.get(r));
                List<FileRepoDto> fileRepoDtos = fileRepoClient.getFilesByIds(deleteIds).getEntity();
                if(!IaisCommonUtils.isEmpty(fileRepoDtos)){
                    fileRepoClient.removeFileById(fileRepoDtos.get(0));
                }
            }
        }
        return appPremPreInspectionNcDocDtos;
    }

    @Override
    public void submitAllRecNc(List<InspecUserRecUploadDto> inspecUserRecUploadDtos, LoginContext loginContext) {
        String appPremCorrId = inspecUserRecUploadDtos.get(0).getAppPremPreInspectionNcDto().getAppPremCorrId();
        //update application
        ApplicationDto applicationDto = applicationFeClient.getApplicationByCorreId(appPremCorrId).getEntity();
        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationFeClient.updateApplication(applicationDto).getEntity();

        for(InspecUserRecUploadDto inspecUserRecUploadDto : inspecUserRecUploadDtos){
            inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
            //update nc item
            AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
            appPremisesPreInspectionNcItemDto.setFeRectifiedFlag(1);
            appPremisesPreInspectionNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesPreInspectionNcItemDto = applicationFeClient.updateAppPreItemNc(appPremisesPreInspectionNcItemDto).getEntity();

            //set premises nc
            List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = IaisCommonUtils.genNewArrayList();
            appPremPreInspectionNcDtos.add(inspecUserRecUploadDto.getAppPremPreInspectionNcDto());

            //eic synchronization
            InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();
            inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
            inspRectificationSaveDto.setAppPremPreInspectionNcDocDtos(inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos());
            inspRectificationSaveDto.setAppPremPreInspectionNcDtos(appPremPreInspectionNcDtos);
            inspRectificationSaveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            feEicGatewayClient.callEicWithTrack(inspRectificationSaveDto, feEicGatewayClient::feCreateAndUpdateItemDoc, "feCreateAndUpdateItemDoc");
        }
    }

    @Override
    public ApplicationDto getApplicationByCorrId(String appPremCorrId) {
        return applicationFeClient.getApplicationByCorreId(appPremCorrId).getEntity();
    }

    @Override
    public InspecUserRecUploadDto saveFileReportGetFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String auditTrailStr, CommonsMultipartFile file) {
        String fileReportId = fileRepoClient.saveFiles(file, auditTrailStr).getEntity();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        ids.add(fileReportId);
        List<String> delIds = inspecUserRecUploadDto.getFileRepoDelIds();
        if(IaisCommonUtils.isEmpty(delIds)) {
            inspecUserRecUploadDto.setFileRepoDelIds(ids);
        } else {
            delIds.add(fileReportId);
            inspecUserRecUploadDto.setFileRepoDelIds(delIds);
        }
        FileRepoDto fileRepoDto = fileRepoClient.getFilesByIds(ids).getEntity().get(0);
        if(fileRepoDto != null){
            fileRepoDto.setRealFileName(inspecUserRecUploadDto.getFileName());
        }
        if(inspecUserRecUploadDto.getFileRepoDtos() == null){
            List<FileRepoDto> fileRepoDtos = IaisCommonUtils.genNewArrayList();
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
            List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = IaisCommonUtils.genNewArrayList();
            appPremPreInspectionNcDocDtos.add(appPremPreInspectionNcDocDto);
            inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
        } else {
            inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos().add(appPremPreInspectionNcDocDto);
        }
        return inspecUserRecUploadDto;
    }

    @Override
    public List<InspecUserRecUploadDto> getNcItemData(int version, String appPremCorrId, List<ChecklistItemDto> checklistItemDtos) {
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = IaisCommonUtils.genNewArrayList();
        //get vehicle list
        List<AppSvcVehicleDto> appSvcVehicleDtos = inspectionFeClient.getAppSvcVehicleDtoListByCorrId(appPremCorrId).getEntity();
        //get old data
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = applicationFeClient.getAppPremPreInsNcDtoByAppCorrId(appPremCorrId).getEntity();
        String oldNcId = appPremPreInspectionNcDto.getId();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = inspectionFeClient.getNcItemDtoListByAppPremNcId(oldNcId).getEntity();
        String curVersionStr = appPremPreInspectionNcDto.getVersion();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        int curVersion = Integer.parseInt(curVersionStr);
        //request for information
        if(curVersion < version){
            //create new AppPremPreInspectionNcDto
            appPremPreInspectionNcDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            appPremPreInspectionNcDto.setAuditTrailDto(auditTrailDto);
            appPremPreInspectionNcDto = applicationFeClient.updateAppPremPreNc(appPremPreInspectionNcDto).getEntity();

            appPremPreInspectionNcDto.setId(null);
            appPremPreInspectionNcDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremPreInspectionNcDto.setVersion(version + "");
            appPremPreInspectionNcDto.setAuditTrailDto(auditTrailDto);
            appPremPreInspectionNcDto = applicationFeClient.saveAppPremPreNc(appPremPreInspectionNcDto).getEntity();

            //create new AppPremisesPreInspectionNcItemDto
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
                for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                    appPremisesPreInspectionNcItemDto.setPreNcId(appPremPreInspectionNcDto.getId());
                    appPremisesPreInspectionNcItemDto.setId(null);
                    appPremisesPreInspectionNcItemDto.setFeRectifiedFlag(0);
                    appPremisesPreInspectionNcItemDto.setAuditTrailDto(auditTrailDto);
                    appPremisesPreInspectionNcItemDto = applicationFeClient.createAppNcItemDto(appPremisesPreInspectionNcItemDto).getEntity();
                    appPremisesPreInspectionNcItemDtoList.add(appPremisesPreInspectionNcItemDto);
                }
            }

            // set all item data
            if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtoList)){
                for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtoList){
                    //Filter for NC(s) that have been rectified
                    int rec = appPremisesPreInspectionNcItemDto.getIsRecitfied();
                    if (0 == rec) {
                        InspecUserRecUploadDto inspecUserRecUploadDto = new InspecUserRecUploadDto();
                        inspecUserRecUploadDto.setId(appPremisesPreInspectionNcItemDto.getId());
                        inspecUserRecUploadDto.setItemId(appPremisesPreInspectionNcItemDto.getItemId());
                        //set Vehicle No. To Show
                        inspecUserRecUploadDto = setVehicleNoToShow(inspecUserRecUploadDto, appSvcVehicleDtos, appPremisesPreInspectionNcItemDto.getVehicleName());
                        inspecUserRecUploadDto.setVehicleNo(appPremisesPreInspectionNcItemDto.getVehicleName());
                        int feRec = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                        if (1 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
                        } else if (0 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.FAIL);
                        }
                        //set item Clause and Question
                        inspecUserRecUploadDto = setItemClauseQues(checklistItemDtos, inspecUserRecUploadDto);

                        inspecUserRecUploadDto.setAppPremPreInspectionNcDto(appPremPreInspectionNcDto);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtoList);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        inspecUserRecUploadDto.setRectifyFlag(AppConsts.FAIL);

                        inspecUserRecUploadDtos.add(inspecUserRecUploadDto);
                    }
                }
            }
        } else {
            //first rectification
            if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)) {
                for (AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos) {
                    //Filter for NC(s) that have been rectified
                    int rec = appPremisesPreInspectionNcItemDto.getIsRecitfied();
                    if (0 == rec) {
                        InspecUserRecUploadDto inspecUserRecUploadDto = new InspecUserRecUploadDto();
                        inspecUserRecUploadDto.setId(appPremisesPreInspectionNcItemDto.getId());
                        inspecUserRecUploadDto.setItemId(appPremisesPreInspectionNcItemDto.getItemId());
                        //set Vehicle No. To Show
                        inspecUserRecUploadDto = setVehicleNoToShow(inspecUserRecUploadDto, appSvcVehicleDtos, appPremisesPreInspectionNcItemDto.getVehicleName());
                        int feRec = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                        if (1 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
                        } else if (0 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.FAIL);
                        }
                        //set item Clause and Question
                        inspecUserRecUploadDto = setItemClauseQues(checklistItemDtos, inspecUserRecUploadDto);

                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        inspecUserRecUploadDto.setAppPremPreInspectionNcDto(appPremPreInspectionNcDto);
                        //get fileReport
                        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos =
                                applicationFeClient.getNcDocListByItemId(appPremisesPreInspectionNcItemDto.getId()).getEntity();
                        if(!IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos)){
                            List<String> fileReportIds = IaisCommonUtils.genNewArrayList();
                            for(AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto : appPremPreInspectionNcDocDtos){
                                fileReportIds.add(appPremPreInspectionNcDocDto.getFileRepoId());
                            }
                            List<FileRepoDto> fileRepoDtos = fileRepoClient.getFilesByIds(fileReportIds).getEntity();
                            inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
                            if(!IaisCommonUtils.isEmpty(fileRepoDtos)) {
                                inspecUserRecUploadDto.setRectifyFlag(AppConsts.SUCCESS);
                                inspecUserRecUploadDto = setRealFileName(inspecUserRecUploadDto, appPremPreInspectionNcDocDtos);
                            }
                            inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
                        } else {
                            inspecUserRecUploadDto.setRectifyFlag(AppConsts.FAIL);
                        }
                        inspecUserRecUploadDtos.add(inspecUserRecUploadDto);
                    }
                }
            }
        }
        return inspecUserRecUploadDtos;
    }

    private InspecUserRecUploadDto setVehicleNoToShow(InspecUserRecUploadDto inspecUserRecUploadDto, List<AppSvcVehicleDto> appSvcVehicleDtos, String vehicleName) {
        if(inspecUserRecUploadDto != null && !IaisCommonUtils.isEmpty(appSvcVehicleDtos) && !StringUtil.isEmpty(vehicleName)) {
            for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                if(appSvcVehicleDto != null) {
                    String veName = appSvcVehicleDto.getVehicleName();
                    if(vehicleName.equals(veName)) {
                        inspecUserRecUploadDto.setVehicleNo(appSvcVehicleDto.getDisplayName());
                    }
                }
            }
        }
        return inspecUserRecUploadDto;
    }

    private InspecUserRecUploadDto setRealFileName(InspecUserRecUploadDto inspecUserRecUploadDto, List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos) {
        List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
        for(FileRepoDto fileRepoDto : fileRepoDtos) {
            if(fileRepoDto != null) {
                for (AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto : appPremPreInspectionNcDocDtos) {
                    if (appPremPreInspectionNcDocDto != null && fileRepoDto.getId().equals(appPremPreInspectionNcDocDto.getFileRepoId())) {
                        fileRepoDto.setRealFileName(appPremPreInspectionNcDocDto.getDocName());
                    }
                }
            }
        }
        return inspecUserRecUploadDto;
    }

    private InspecUserRecUploadDto setItemClauseQues(List<ChecklistItemDto> checklistItemDtos, InspecUserRecUploadDto inspecUserRecUploadDto) {
        int index = -1;
        if(!IaisCommonUtils.isEmpty(checklistItemDtos)) {
            for (ChecklistItemDto cDto : checklistItemDtos) {
                if(inspecUserRecUploadDto.getItemId().equals(cDto.getItemId())) {
                    inspecUserRecUploadDto.setCheckClause(cDto.getRegulationClause());
                    inspecUserRecUploadDto.setCheckQuestion(cDto.getChecklistItem());
                    inspecUserRecUploadDto.setIndex(index++);
                    //Direct return data
                    return inspecUserRecUploadDto;
                }
            }
        }
        //There are no matching items, search item from adhoc table
        String adhocItemId = inspecUserRecUploadDto.getItemId();
        AdhocChecklistItemDto adhocChecklistItemDto = inspectionFeClient.getAdhocChecklistItemById(adhocItemId).getEntity();
        if(adhocChecklistItemDto != null){
            String itemId = adhocChecklistItemDto.getItemId();
            if(!StringUtil.isEmpty(itemId)){
                ChecklistItemDto checklistItemDto = appConfigClient.getChklItemById(itemId).getEntity();
                inspecUserRecUploadDto.setCheckClause("-");
                inspecUserRecUploadDto.setCheckQuestion(checklistItemDto.getChecklistItem());
            } else {
                inspecUserRecUploadDto.setCheckClause("-");
                inspecUserRecUploadDto.setCheckQuestion(adhocChecklistItemDto.getQuestion());
            }
        }
        return inspecUserRecUploadDto;
    }

    @Override
    public InspecUserRecUploadDto removeFileByFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String removeId) {
        List<String> removeIds = inspecUserRecUploadDto.getFileDelCelIds();
        if(!IaisCommonUtils.isEmpty(removeIds)){
            removeIds.add(removeId);
            inspecUserRecUploadDto.setFileDelCelIds(removeIds);
        } else {
            removeIds = IaisCommonUtils.genNewArrayList();
            removeIds.add(removeId);
            inspecUserRecUploadDto.setFileDelCelIds(removeIds);
        }
        List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
        for(int i= 0; i < fileRepoDtos.size(); i++){
            if(removeId.equals(fileRepoDtos.get(i).getId())){
                fileRepoDtos.remove(i);
            }
        }
        inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
        return inspecUserRecUploadDto;
    }

    @Override
    public InspecUserRecUploadDto removeFileAndNcDocs(InspecUserRecUploadDto inspecUserRecUploadDto) {
        List<String> delIds = inspecUserRecUploadDto.getFileRepoDelIds();
        if(!IaisCommonUtils.isEmpty(delIds)){
            for(String id : delIds){
                List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
                List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos();
                fileRepoDtos = deleteFileById(id, fileRepoDtos);
                appPremPreInspectionNcDocDtos = deleteNcDoc(id, appPremPreInspectionNcDocDtos);
                inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
                inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
            }
        }
        return inspecUserRecUploadDto;
    }

    @Override
    public InspecUserRecUploadDto recoverFile(InspecUserRecUploadDto inspecUserRecUploadDto) {
        List<String> removeIds = inspecUserRecUploadDto.getFileDelCelIds();
        List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
        if(IaisCommonUtils.isEmpty(fileRepoDtos)){
            fileRepoDtos = IaisCommonUtils.genNewArrayList();
        }
        if(!IaisCommonUtils.isEmpty(removeIds)){
            for(String fileId : removeIds){
                List<String> ids = IaisCommonUtils.genNewArrayList();
                ids.add(fileId);
                FileRepoDto fileRepoDto = fileRepoClient.getFilesByIds(ids).getEntity().get(0);
                fileRepoDtos.add(fileRepoDto);
            }
        }
        return inspecUserRecUploadDto;
    }

    @Override
    public void updateMessageStatus(String messageId, String messageStatus) {
        if(!StringUtil.isEmpty(messageId) && !StringUtil.isEmpty(messageStatus)){
            licFeInboxClient.updateMsgStatusTo(messageId, messageStatus);
        }
    }

    private List<AppPremPreInspectionNcDocDto> deleteNcDoc(String id, List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos) {
        if(!IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos)){
            for(int i = 0; i< appPremPreInspectionNcDocDtos.size(); i++){
                if(id.equals(appPremPreInspectionNcDocDtos.get(i).getFileRepoId())){
                    appPremPreInspectionNcDocDtos.remove(i);
                    return appPremPreInspectionNcDocDtos;
                }
            }
        }
        return appPremPreInspectionNcDocDtos;
    }

    private List<FileRepoDto> deleteFileById(String id, List<FileRepoDto> fileRepoDtos) {
        if(!IaisCommonUtils.isEmpty(fileRepoDtos)){
            for(int i = 0; i< fileRepoDtos.size(); i++){
                if(id.equals(fileRepoDtos.get(i).getId())){
                    fileRepoClient.removeFileById(fileRepoDtos.get(i));
                    fileRepoDtos.remove(i);
                    return fileRepoDtos;
                }
            }
        }
        return fileRepoDtos;
    }

    private List<ChecklistItemDto> getCheckDtosByItemIds(List<String> itemIds) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        if(itemIds != null && !(itemIds.isEmpty())) {
            for (String itemId:itemIds) {
                ChecklistItemDto checklistItemDto = appConfigClient.getChklItemById(itemId).getEntity();
                checklistItemDtos.add(checklistItemDto);
            }
        }
        return checklistItemDtos;
    }
}
