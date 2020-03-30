package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionFeClient inspectionFeClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<ChecklistItemDto> getQuesAndClause(String appNo) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        if(!(StringUtil.isEmpty(appNo))){
            List<String> itemIds = applicationClient.getItemIdsByAppNo(appNo).getEntity();
            checklistItemDtos = getCheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    @Override
    public void submitRecByUser(LoginContext loginContext, InspecUserRecUploadDto inspecUserRecUploadDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String appPremCorrId = inspecUserRecUploadDto.getAppPremPreInspectionNcDto().getAppPremCorrId();
        ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
        List<AppPremPreInspectionNcDocDto> appNcDocDtoList = inspecUserRecUploadDto.getAppPremPreInspectionNcDocDtos();
        for(AppPremPreInspectionNcDocDto appNcDocDto : appNcDocDtoList) {
            appNcDocDto.setId(null);
            appNcDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appNcDocDto.setSubmitBy(loginContext.getUserId());
            appNcDocDto.setSubmitDt(new Date());
        }
        AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
        appPremisesPreInspectionNcItemDto.setRemarks(inspecUserRecUploadDto.getUploadRemarks());
        appPremisesPreInspectionNcItemDto.setFeRectifiedFlag(1);
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

        List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = IaisCommonUtils.genNewArrayList();
        appPremPreInspectionNcDtos.add(inspecUserRecUploadDto.getAppPremPreInspectionNcDto());
        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();
        inspRectificationSaveDto.setApplicationDto(applicationDto);
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
        inspRectificationSaveDto.setAppPremPreInspectionNcDocDtos(appNcDocDtoList);
        inspRectificationSaveDto.setAppPremPreInspectionNcDtos(appPremPreInspectionNcDtos);
        inspRectificationSaveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        feEicGatewayClient.feCreateAndUpdateItemDoc(inspRectificationSaveDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public ApplicationDto getApplicationByCorrId(String appPremCorrId) {
        return applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
    }

    @Override
    public InspecUserRecUploadDto saveFileReportGetFileId(InspecUserRecUploadDto inspecUserRecUploadDto, String auditTrailStr, CommonsMultipartFile file) {
        String fileReportId = fileRepoClient.saveFiles(file, auditTrailStr).getEntity();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        ids.add(fileReportId);
        FileRepoDto fileRepoDto = fileRepoClient.getFilesByIds(ids).getEntity().get(0);
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
    public List<InspecUserRecUploadDto> getNcItemData(int version, String appPremCorrId, List<ChecklistItemDto> checklistItemDtos, String appNo) {
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = IaisCommonUtils.genNewArrayList();
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = applicationClient.getAppPremPreInsNcDtoByAppCorrId(appPremCorrId).getEntity();
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
            appPremPreInspectionNcDto = applicationClient.updateAppPremPreNc(appPremPreInspectionNcDto).getEntity();

            appPremPreInspectionNcDto.setId(null);
            appPremPreInspectionNcDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremPreInspectionNcDto.setVersion(version + "");
            appPremPreInspectionNcDto.setAuditTrailDto(auditTrailDto);
            appPremPreInspectionNcDto = applicationClient.saveAppPremPreNc(appPremPreInspectionNcDto).getEntity();

            //create new AppPremisesPreInspectionNcItemDto
            List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
                for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                    appPremisesPreInspectionNcItemDto.setPreNcId(appPremPreInspectionNcDto.getId());
                    appPremisesPreInspectionNcItemDto.setId(null);
                    appPremisesPreInspectionNcItemDto.setFeRectifiedFlag(0);
                    appPremisesPreInspectionNcItemDto.setAuditTrailDto(auditTrailDto);
                    appPremisesPreInspectionNcItemDto = applicationClient.createAppNcItemDto(appPremisesPreInspectionNcItemDto).getEntity();
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
                        int feRec = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                        if (1 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
                        } else if (0 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.FAIL);
                        }
                        //set item Clause and Question
                        inspecUserRecUploadDto = setItemClauseQues(checklistItemDtos, inspecUserRecUploadDto, appNo);

                        inspecUserRecUploadDto.setAppPremPreInspectionNcDto(appPremPreInspectionNcDto);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtoList);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        //get fileReport
                        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos =
                                applicationClient.getNcDocListByItemId(appPremisesPreInspectionNcItemDto.getId()).getEntity();
                        if(!IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos)){
                            List<String> fileReportIds = IaisCommonUtils.genNewArrayList();
                            for(AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto : appPremPreInspectionNcDocDtos){
                                appPremPreInspectionNcDocDto.setId(null);
                                fileReportIds.add(appPremPreInspectionNcDocDto.getFileRepoId());
                            }
                            List<FileRepoDto> fileRepoDtos = fileRepoClient.getFilesByIds(fileReportIds).getEntity();
                            inspecUserRecUploadDto.setFileRepoDtos(fileRepoDtos);
                            inspecUserRecUploadDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
                        }
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
                        int feRec = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                        if (1 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
                        } else if (0 == feRec) {
                            inspecUserRecUploadDto.setButtonFlag(AppConsts.FAIL);
                        }
                        //set item Clause and Question
                        inspecUserRecUploadDto = setItemClauseQues(checklistItemDtos, inspecUserRecUploadDto, appNo);

                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
                        inspecUserRecUploadDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        inspecUserRecUploadDto.setAppPremPreInspectionNcDto(appPremPreInspectionNcDto);
                        inspecUserRecUploadDtos.add(inspecUserRecUploadDto);
                    }
                }
            }
        }
        return inspecUserRecUploadDtos;
    }

    private InspecUserRecUploadDto setItemClauseQues(List<ChecklistItemDto> checklistItemDtos, InspecUserRecUploadDto inspecUserRecUploadDto, String appNo) {
        int index = -1;
        if(!IaisCommonUtils.isEmpty(checklistItemDtos)) {
            for (ChecklistItemDto cDto : checklistItemDtos) {
                if(inspecUserRecUploadDto.getItemId().equals(cDto.getItemId())) {
                    inspecUserRecUploadDto.setCheckClause(cDto.getRegulationClause());
                    inspecUserRecUploadDto.setCheckQuestion(cDto.getChecklistItem());
                    inspecUserRecUploadDto.setIndex(index++);
                    inspecUserRecUploadDto.setAppNo(appNo);
                    return inspecUserRecUploadDto;
                }
            }
        }
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
