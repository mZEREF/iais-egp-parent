package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.CheckListDraftAllDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckListAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSpecServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.RimRiskCountDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:48
 */
@Slf4j
@Service
public class InsepctionNcCheckListImpl implements InsepctionNcCheckListService {
    @Autowired
    private TaskService taskService;

    @Autowired
    private FillupChklistService fillupChklistService;
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    FileRepoClient fileRepoClient;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Override
    public void saveLicPremisesAuditDtoByApplicationViewDto(ApplicationViewDto appViewDto) {
        if( appViewDto != null){
            LicPremisesAuditDto licPremisesAuditDto =  appViewDto.getLicPremisesAuditDto();
            if(licPremisesAuditDto != null){
                boolean isFirstHaveIncRiskType = saveAuditIncRiskType(appViewDto.getAppPremisesCorrelationId(),licPremisesAuditDto.getLgrRemarks(),licPremisesAuditDto.getIncludeRiskType());
                hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto);
                ApplicationGroupDto applicationGroupDto = appViewDto.getApplicationGroupDto();
                if(isFirstHaveIncRiskType){
                    if(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY.equalsIgnoreCase(licPremisesAuditDto.getIncludeRiskType())){
                        if(applicationGroupDto != null ){
                            saveRimRiskCountByLicenseeId(applicationGroupDto.getLicenseeId(),true);
                        }
                    }
                }else {
                    if(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY.equalsIgnoreCase(licPremisesAuditDto.getIncludeRiskTypeOld())){
                        //Bonus points were awarded last time.
                        if( !ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY.equalsIgnoreCase(licPremisesAuditDto.getIncludeRiskType())){
                            //need - 1
                            if(applicationGroupDto != null ) {
                                saveRimRiskCountByLicenseeId(applicationGroupDto.getLicenseeId(), false);
                            }
                        }
                    }else {
                        if(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY.equalsIgnoreCase(licPremisesAuditDto.getIncludeRiskType())){
                            if(applicationGroupDto != null ){
                                saveRimRiskCountByLicenseeId(applicationGroupDto.getLicenseeId(),true);
                            }
                        }
                    }
                }
                licPremisesAuditDto.setIncludeRiskTypeOld(licPremisesAuditDto.getIncludeRiskType());
            }
        }
    }

    private boolean saveAuditIncRiskType(String corrId,String remark,String incRiskType){
        boolean isFirstHaveIncRiskType = false;
        AppPremisesRecommendationDto appPremisesRecommendationDto =  fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(corrId,InspectionConstants.RECOM_TYPE_INSP_RISK_TYPE).getEntity();
        if(appPremisesRecommendationDto != null){
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            fillUpCheckListGetAppClient.updateAppRecom(appPremisesRecommendationDto);
            appPremisesRecommendationDto.setVersion( appPremisesRecommendationDto.getVersion()+1);
        }else {
            if(StringUtil.isEmpty(incRiskType)){
              return isFirstHaveIncRiskType;
            }
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setVersion(1);
            isFirstHaveIncRiskType = true;
        }
        appPremisesRecommendationDto.setId(null);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRecomDecision(incRiskType);
        appPremisesRecommendationDto.setRemarks(remark);
        appPremisesRecommendationDto.setAppPremCorreId(corrId);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_RISK_TYPE);
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        return isFirstHaveIncRiskType;
    }
    @Override
    public void saveRimRiskCountByLicenseeId(String licenseeId,boolean isAdd) {
        RimRiskCountDto rimRiskCountDto = organizationClient.getUenRimRiskCountDtoByLicenseeId(licenseeId).getEntity();
        if( rimRiskCountDto == null){
         log.info(StringUtil.changeForLog("----------getUenRimRiskCountDtoByLicenseeId : " + licenseeId + " have no licensee."));
        }else {
            if(isAdd){
                rimRiskCountDto.setLeadshipGovAuditCount(rimRiskCountDto.getLeadshipGovAuditCount()+1);
            }else {
                rimRiskCountDto.setLeadshipGovAuditCount(rimRiskCountDto.getLeadshipGovAuditCount()-1);
            }
            rimRiskCountDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            organizationClient.doRimRiskCountSave(rimRiskCountDto);
        }
    }
    @Override
    public InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto, AppPremisesPreInspectChklDto appPremDto, List<AppPremisesPreInspectionNcItemDto> itemDtoList, AppPremisesRecommendationDto appPremisesRecommendationDto) {
        List<InspectionCheckQuestionDto> fillCheckList = infillDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = IaisCommonUtils.genNewArrayList();
        InspectionCheckQuestionDto ncCheck = null;
        String answer = appPremDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answer, InspectionCheckListAnswerDto.class);
        if (fillCheckList != null && !fillCheckList.isEmpty()) {
            for (InspectionCheckQuestionDto temp : fillCheckList) {
                for (int i = 0; i < answerDtoList.size(); i++) {
                    if (temp.getItemId().equals(answerDtoList.get(i).getItemId()) &&
                            temp.getSectionName().equals(answerDtoList.get(i).getSectionName())) {
                        //&&"No".equals(answerDtoList.get(i).getAnswer())){
                        temp.setChkanswer(answerDtoList.get(i).getAnswer());
                        temp.setRemark(answerDtoList.get(i).getRemark());
                        if("1".equals(answerDtoList.get(i).getIsRec())&&"No".equals(temp.getChkanswer())){
                            temp.setRectified(true);
                        }else{
                            temp.setRectified(false);
                        }
                        ncCheckList.add(temp);
                    }
                }
            }
        }
        infillDto.setBestPractice(appPremisesRecommendationDto.getBestPractice());
        infillDto.setTuc(Formatter.formatDate(appPremisesRecommendationDto.getRecomInDate()));
        infillDto.setTcuRemark(appPremisesRecommendationDto.getRemarks());
        infillDto.setCheckList(ncCheckList);
        fillInspectionFillCheckListDto(infillDto);
        return infillDto;
    }


    public InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto) {
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        List<SectionDto> sectionDtoList = IaisCommonUtils.genNewArrayList();
        for (InspectionCheckQuestionDto temp : iqdDtolist) {
            SectionDto sectionDto = new SectionDto();
            sectionDto.setSectionName(temp.getSectionName());
            if (isHaveSameSection(temp.getSectionName(), sectionDtoList)) {
                sectionDtoList.add(sectionDto);
            }
        }
        infillCheckListDto.setSectionDtoList(sectionDtoList);
        itemDto(infillCheckListDto);
        return infillCheckListDto;
    }

    public boolean isHaveSameSection(String sectionName, List<SectionDto> sectionDtoList) {
        if (sectionDtoList != null && !sectionDtoList.isEmpty()) {
            for (SectionDto temp : sectionDtoList) {
                if (temp.getSectionName().equals(sectionName)) {
                    return false;
                }
            }
        }
        return true;
    }

    public InspectionFillCheckListDto itemDto(InspectionFillCheckListDto infillCheckListDto) {
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for (SectionDto temp : sectionDtoList) {
            List<ItemDto> itemDtoList = IaisCommonUtils.genNewArrayList();
            for (InspectionCheckQuestionDto iq : iqdDtolist) {
                ItemDto itemDto = new ItemDto();
                if (temp.getSectionName().equals(iq.getSectionName())) {
                    itemDto.setItemId(iq.getItemId());
                    itemDtoList.add(itemDto);
                }
            }
            temp.setItemDtoList(itemDtoList);
        }
        getItemCheckListDto(infillCheckListDto);
        return infillCheckListDto;
    }

    public InspectionFillCheckListDto getItemCheckListDto(InspectionFillCheckListDto infillCheckListDto) {
        List<SectionDto> sectionDtoList = infillCheckListDto.getSectionDtoList();
        List<InspectionCheckQuestionDto> iqdDtolist = infillCheckListDto.getCheckList();
        for (InspectionCheckQuestionDto temp : iqdDtolist) {
            for (SectionDto section : sectionDtoList) {
                if (temp.getSectionName().equals(section.getSectionName())) {
                    List<ItemDto> itemDtoList = section.getItemDtoList();
                    for (ItemDto itemDto : itemDtoList) {
                        if (itemDto.getItemId().equals(temp.getItemId())) {
                            itemDto.setIncqDto(temp);
                        }
                    }
                }
            }
        }
        return infillCheckListDto;
    }

    @Override
    public AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId, String configId) {
        if (StringUtil.isEmpty(taskId)) {
             log.info("-----------getAppPremChklDtoByTaskId TASKID is null -------------------------- ");
             return null;
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = null;
        if (taskDto != null) {
            appPremCorrId = taskDto.getRefNo();
        }
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto = fillUpCheckListGetAppClient.getAppPremInspeChlkByAppCorrIdAndConfigId(appPremCorrId,configId).getEntity();
        return appPremisesPreInspectChklDto;
    }

    @Override
    public List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId) {
        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appCorrId).getEntity();
        String ncId = ncDto.getId();
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList = fillUpCheckListGetAppClient.getAppNcItemByNcId(ncId).getEntity();
        return ncItemDtoList;
    }

    @Override
    public AppPremisesRecommendationDto getAppRecomDtoByAppCorrId(String appCorrId, String recomType) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appCorrId,recomType).getEntity();
        return appPremisesRecommendationDto;
    }

    @Override
    public void submit(InspectionFillCheckListDto commDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId) {
        //save other data
        saveOtherDataForPengingIns(serListDto,appPremId);
        //save checklist data
        saveBeforeSubmitCheckList(commDto, showDto, serListDto, appPremId);
    }

    @Override
    public void submitSpecService(InspectionFillCheckListDto commDto, List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFDtosDto serListDto, AdCheckListShowDto adCheckListShowDto,String appPremId) {
        //save other data
        saveOtherDataForPengingIns(serListDto,appPremId);
        //save spec checklist data
        saveBeforeSubmitSpecCheckList(commDto, inspectionSpecServiceDtos, serListDto,adCheckListShowDto,appPremId);
    }

    @Override
    public void saveBeforeSubmitSpecCheckList(InspectionFillCheckListDto commDto, List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFDtosDto serListDto,AdCheckListShowDto adCheckListShowDto,String appPremId) {
        List<InspectionFillCheckListDto> fillcheckDtoList = getFillcheckDtoListAndSaveCheckListNoVehicle(commDto,serListDto,appPremId);
        AdCheckListShowDto adchklDto =(AdCheckListShowDto) com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(adCheckListShowDto);
        if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : inspectionSpecServiceDtos){

                if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getFdtoList())){
                    saveSerListDto(getInspectionFDtosDtoByInspectionSpecServiceDto(inspectionSpecServiceDto),appPremId);
                    fillcheckDtoList.addAll(inspectionSpecServiceDto.getFdtoList());
                }

                if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
                    if(adchklDto  == null){
                        List<AdhocNcCheckItemDto> adItemList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
                        setIdentifyForAdItemList(adItemList,inspectionSpecServiceDto.getIdentify());
                        adchklDto = (AdCheckListShowDto)com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(inspectionSpecServiceDto.getAdchklDto());
                    }else {
                        List<AdhocNcCheckItemDto> adItemList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
                        setIdentifyForAdItemList(adItemList,inspectionSpecServiceDto.getIdentify());
                        List<AdhocNcCheckItemDto> adItemListAll =  adchklDto.getAdItemList();
                        adItemListAll.addAll(((AdCheckListShowDto) com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(inspectionSpecServiceDto.getAdchklDto())).getAdItemList());
                    }
                }
            }
        }
        if(adchklDto != null) {
            saveAdhocDto(adchklDto, appPremId);
        }

        if (IaisCommonUtils.isNotEmpty(fillcheckDtoList) || (adchklDto != null && !IaisCommonUtils.isEmpty(adchklDto.getAdItemList()))) {
            saveNcItem(fillcheckDtoList, appPremId, adchklDto);
        }

    }

    private void setIdentifyForAdItemList( List<AdhocNcCheckItemDto> adItemList,String identify){
        for(AdhocNcCheckItemDto adhocNcCheckItemDto : adItemList){
            adhocNcCheckItemDto.setIdentify(identify);
        }
    }


    private InspectionFDtosDto getInspectionFDtosDtoByInspectionSpecServiceDto(InspectionSpecServiceDto inspectionSpecServiceDto){
        InspectionFDtosDto inspectionFDtosDto = MiscUtil.transferEntityDto(inspectionSpecServiceDto,InspectionFDtosDto.class);
        inspectionFDtosDto.setFdtoList(inspectionSpecServiceDto.getFdtoList());
        return inspectionFDtosDto;
    }
    public void saveLitterFile(InspectionFDtosDto serListDto){
        if(serListDto.getAppPremisesSpecialDocDto() != null ){
            AppPremisesSpecialDocDto appPremisesSpecialDocDto = serListDto.getAppPremisesSpecialDocDto();
            if( StringUtil.isEmpty(appPremisesSpecialDocDto.getId())){
                if(serListDto.getCopyAppPremisesSpecialDocDto() != null){
                    fillUpCheckListGetAppClient.deleteAppIntranetDocsById(serListDto.getCopyAppPremisesSpecialDocDto().getId());
                }
                String oldFileGuid = serListDto.getOldFileGuid();
                if(!StringUtil.isEmpty(oldFileGuid)){
                    removeFiles(oldFileGuid);
                    serListDto.setOldFileGuid(null);
                }
                String guid = serListDto.getAppPremisesSpecialDocDto().getFileRepoId();
                serListDto.setOldFileGuid(guid);
                appPremisesSpecialDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appPremisesSpecialDocDto.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
                appPremisesSpecialDocDto.setSubmitDt(new Date());
                appPremisesSpecialDocDto.setId(saveAppPremisesSpecialDoc(serListDto.getAppPremisesSpecialDocDto()));
                serListDto.setCopyAppPremisesSpecialDocDto(fillupChklistService.getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(appPremisesSpecialDocDto));
            }
        }else {
            String oldFileGuid = serListDto.getOldFileGuid();
            if(serListDto.getCopyAppPremisesSpecialDocDto() != null){
                fillUpCheckListGetAppClient.deleteAppIntranetDocsById(serListDto.getCopyAppPremisesSpecialDocDto().getId());
            }
            if(!StringUtil.isEmpty(oldFileGuid)){
                removeFiles(oldFileGuid);
                serListDto.setOldFileGuid(null);
                serListDto.setCopyAppPremisesSpecialDocDto(null);
            }

        }
    }

    public String saveAppPremisesSpecialDoc(AppPremisesSpecialDocDto appPremisesSpecialDocDto){
        AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
        appIntranetDocDto.setAuditTrailDto(appPremisesSpecialDocDto.getAuditTrailDto());
        appIntranetDocDto.setAppPremCorrId(appPremisesSpecialDocDto.getAppPremCorreId());
        appIntranetDocDto.setSubmitBy( appPremisesSpecialDocDto.getSubmitBy());
        appIntranetDocDto .setSubmitDt(appPremisesSpecialDocDto.getSubmitDt());
        appIntranetDocDto.setDocSize(String.valueOf(appPremisesSpecialDocDto.getDocSize()));
        appIntranetDocDto.setFileRepoId(appPremisesSpecialDocDto.getFileRepoId());
        String[] fileSplit = appPremisesSpecialDocDto.getDocName() .split("\\.");
        String fileType = fileSplit[fileSplit.length - 1];
        //name
        String fileName = IaisCommonUtils.getDocNameByStrings(fileSplit);
        appIntranetDocDto.setDocType(fileType);
        appIntranetDocDto.setDocName(fileName);
        appIntranetDocDto.setDocDesc(fileName);
        appIntranetDocDto.setAppDocType(ApplicationConsts.APP_DOC_TYPE_CHECK_LIST);
        appIntranetDocDto.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return  fillUpCheckListGetAppClient.saveAppIntranetDocByAppIntranetDoc(appIntranetDocDto).getEntity();
    }
    @Override
    public  String saveFiles( MultipartFile multipartFile){
        FileRepoDto fileRepoDto = new FileRepoDto();
        fileRepoDto.setFileName(multipartFile.getOriginalFilename());
        fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fileRepoDto.setRelativePath(AppConsts.FALSE);
        return fileRepoClient.saveFiles(multipartFile,JsonUtil.parseToJson(fileRepoDto)).getEntity();
    }

    @Override
    public void removeFiles(String id){
        log.info(StringUtil.changeForLog("----- remove id : "+ id+"-------"));
        try {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fileRepoClient.removeFileById(fileRepoDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public  void deleteInvalidFile( InspectionFDtosDto serListDto){
        AppPremisesSpecialDocDto appIntranetDocDto = serListDto.getAppPremisesSpecialDocDto();
        if(appIntranetDocDto != null && !StringUtil.isEmpty(appIntranetDocDto.getFileRepoId())
                && !appIntranetDocDto.getFileRepoId().equalsIgnoreCase(serListDto.getOldFileGuid())){
                 removeFiles(appIntranetDocDto.getFileRepoId());
        }
    }
    public void saveInspectionDate(InspectionFDtosDto serListDto, String appPremId) {
        String inspectionDate = null;
        if(serListDto!=null){
            inspectionDate = serListDto.getInspectionDate();
            if(!StringUtil.isEmpty(inspectionDate)){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setId(null);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                appPreRecommentdationDto.setRecomDecision(inspectionDate);
                Date insDate = null;
                try {
                    insDate = Formatter.parseDate(inspectionDate);
                }catch (Exception e){
                    log.debug(e.toString());

                }
                appPreRecommentdationDto.setRecomInDate(insDate);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveStartTime(InspectionFDtosDto serListDto, String appPremId) {
        String startTime = null;
        if(serListDto!=null){
            startTime = serListDto.getStartTime();
            if(!StringUtil.isEmpty(serListDto.getStartHour())&&!StringUtil.isEmpty(serListDto.getStartMin())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_START_TIME);
                appPreRecommentdationDto.setRecomDecision(startTime);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }
    public void saveEndTime(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            String endTime = serListDto.getEndTime();
            if(!StringUtil.isEmpty(serListDto.getEndHour())&&!StringUtil.isEmpty(serListDto.getEndMin())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);
                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_END_TIME);
                appPreRecommentdationDto.setRecomDecision(endTime);
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveOtherInspection(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto!=null){
            if(!StringUtil.isEmpty(serListDto.getOtherinspectionofficer())){
                AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS).getEntity();
                if(appPreRecommentdationDto!=null){
                    appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
                    appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
                    appPreRecommentdationDto.setId(null);
                }else{
                    appPreRecommentdationDto = new AppPremisesRecommendationDto();
                    appPreRecommentdationDto.setVersion(1);

                }
                appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPreRecommentdationDto.setAppPremCorreId(appPremId);
                appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_OTHER_INSPECTIORS);
                appPreRecommentdationDto.setRemarks(serListDto.getOtherinspectionofficer());
                fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
            }
        }

    }

    private void saveNcItem(List<InspectionFillCheckListDto> fillcheckDtoList, String appPremId,AdCheckListShowDto showDto) {
        int ncflagNum = 0;
        for(InspectionFillCheckListDto temp:fillcheckDtoList){
            if(isHaveNc(temp)){
                ncflagNum++;
                break;
            }
        }
        if(ncflagNum == 0 && showDto != null && !IaisCommonUtils.isEmpty(showDto.getAdItemList())){
            for(AdhocNcCheckItemDto adhocNcCheckItemDto : showDto.getAdItemList()){
               if("No".equalsIgnoreCase(adhocNcCheckItemDto.getAdAnswer())){
                   ncflagNum++;
                   break;
               }
            }
        }
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList;
        if(ncflagNum>0){
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDto(appPremId);
            appPremPreInspectionNcDto = fillUpCheckListGetAppClient.saveAppPreNc(appPremPreInspectionNcDto).getEntity();
            appPremisesPreInspectionNcItemDtoList = getAppPremisesPreInspectionNcItemDto(fillcheckDtoList, appPremPreInspectionNcDto,showDto);
            fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtoList);
        }
    }


    private void saveRecommend(InspectionFDtosDto serListDto,String appPremId) {
        AppPremisesRecommendationDto  appPreRecommentdationDto = getTcuRec(appPremId,serListDto.getTuc());
        appPreRecommentdationDto.setId(null);
        appPreRecommentdationDto.setAppPremCorreId(appPremId);
        appPreRecommentdationDto.setBestPractice(serListDto.getBestPractice());
        appPreRecommentdationDto.setRemarks(serListDto.getTcuRemark());
        appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
        appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
    }

    private  AppPremisesRecommendationDto getTcuRec(String appPremId,String tcu){
        AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_TCU).getEntity();
        if(appPreRecommentdationDto!=null){
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            fillUpCheckListGetAppClient.updateAppRecom(appPreRecommentdationDto);
            appPreRecommentdationDto.setVersion(appPreRecommentdationDto.getVersion()+1);
        }else{
            appPreRecommentdationDto = new AppPremisesRecommendationDto();
            appPreRecommentdationDto.setVersion(1);
        }
        appPreRecommentdationDto.setAppPremCorreId(appPremId);

        Date tcuDate = null;
        try {
            tcuDate = Formatter.parseDate(tcu);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if ( !StringUtil.isEmpty(tcu) && tcuDate != null ) {
            appPreRecommentdationDto.setRecomInDate(tcuDate);
        }else {
            appPreRecommentdationDto.setRecomInDate(null);
        }
        return appPreRecommentdationDto;
    }

    @Override
    public void saveTcuDate(String appPremId,String tcu){
            AppPremisesRecommendationDto appPreRecommentdationDto = getTcuRec(appPremId,tcu);
            appPreRecommentdationDto.setId(null);
            appPreRecommentdationDto.setAppPremCorreId(appPremId);
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
            appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
    }
    private void saveSerListDto(InspectionFDtosDto serListDto,String appPremId) {
        List<InspectionFillCheckListDto> dtoList = serListDto.getFdtoList();
        if(dtoList!=null &&! dtoList.isEmpty()){
            for(InspectionFillCheckListDto temp:dtoList){
                saveInspectionCheckListDto(temp,appPremId);
            }
        }
    }

    private void saveAdhocDto(AdCheckListShowDto showDto,String appPremId){
        List<AdhocNcCheckItemDto>  itemDtoList = showDto.getAdItemList();
        List<AdhocChecklistItemDto> saveItemDtoList = IaisCommonUtils.genNewArrayList();
        AdhocCheckListConifgDto dto = applicationClient.getAdhocConfigByAppPremCorrId(appPremId).getEntity();
        if(dto!=null){
            dto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                applicationClient.updateAppAdhocConfig(dto);
                dto.setVersion(dto.getVersion()+1);
                dto.setOldId(dto.getId());
                dto.setId(null);
                dto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                dto = applicationClient.saveAppAdhocConfig(dto).getEntity();
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    temp.setAdhocConfId(dto.getId());
                    temp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    temp.setId(null);
                    AdhocAnswerDto adhocAnswerDto = new AdhocAnswerDto();
                    adhocAnswerDto.setRemark(temp.getRemark());
                    adhocAnswerDto.setAnswer(temp.getAdAnswer());
                    adhocAnswerDto.setIsRec((temp.getRectified() != null && temp.getRectified()) ? "1" :"0");
                    adhocAnswerDto.setNcs(temp.getNcs());
                    adhocAnswerDto.setIdentify(temp.getIdentify());
                    String saveAnswer = JsonUtil.parseToJson(adhocAnswerDto);
                    temp.setAnswer(saveAnswer);
                    saveItemDtoList.add(temp);
                }
                saveItemDtoList = applicationClient.saveAdhocItems(filterAdhocItemsNoAnswer(saveItemDtoList)).getEntity();
                int index = 0;
                for(AdhocNcCheckItemDto adhocNcCheckItemDto :  itemDtoList){
                    adhocNcCheckItemDto.setId(saveItemDtoList.get(index).getId());
                    index++;
                }
                dto.setAllAdhocItem(saveItemDtoList);
                saveAdhocChecklist(dto);
            }
        }
    }

    private List<AdhocChecklistItemDto> filterAdhocItemsNoAnswer(List<AdhocChecklistItemDto> saveItemDtoList){
        if(IaisCommonUtils.isNotEmpty(saveItemDtoList)){
            Iterator<AdhocChecklistItemDto>  adhocChecklistItemDtoIterator = saveItemDtoList.listIterator();
            while(adhocChecklistItemDtoIterator.hasNext()){
                AdhocChecklistItemDto adhocChecklistItemDto = adhocChecklistItemDtoIterator.next();
                 if(StringUtil.isEmpty(adhocChecklistItemDto.getAnswer())){
                    adhocChecklistItemDtoIterator.remove();
                  }
             }
        }
        return saveItemDtoList;
    }

    public void saveAdhocChecklist(AdhocCheckListConifgDto adhocConfig) {
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT,
                    InsepctionNcCheckListImpl.class.getName(),
                    "callEicGatewaySaveItem", currentApp + "-" + currentDomain,
                    AdhocCheckListConifgDto.class.getName(), JsonUtil.parseToJson(adhocConfig));
            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()){
                    EicRequestTrackingDto preEicRequest = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(preEicRequest.getStatus())){
                        callEicGatewaySaveItem(adhocConfig);
                        preEicRequest.setProcessNum(1);
                        Date now = new Date();
                        preEicRequest.setFirstActionAt(now);
                        preEicRequest.setLastActionAt(now);
                        preEicRequest.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        eicRequestTrackingHelper.getAppEicClient().saveEicTrack(preEicRequest);
                    }
                }
            }catch (Exception e){
                log.debug(StringUtil.changeForLog("encounter failure when sync adhoc item to fe" + e.getMessage()));
            }
    }

    public void callEicGatewaySaveItem(AdhocCheckListConifgDto data) {
        //route to fe
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        gatewayClient.syncAdhocItemData(data, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    public void saveInspectionCheckListDto(InspectionFillCheckListDto dto,String appPremId) {
        boolean ncflag = isHaveNc(dto);
        if (dto.getCheckList()!=null && !dto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> icqDtoList = dto.getCheckList();
            String appPremCorrId = appPremId;
            String configId = icqDtoList.get(0).getConfigId();
            AppPremisesPreInspectChklDto appDto = fillupChklistService.getAppPremChklDtoByCorrIdAndVehicleName(appPremCorrId,configId,dto.getVehicleName());
            if(appDto!=null){
                if(StringUtil.isNotEmpty(appDto.getId())){
                    appDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    fillUpCheckListGetAppClient.updateAppPreInspChkl(appDto);
                }
                appDto.setVersion(String.valueOf((1 + Integer.parseInt(appDto.getVersion()))));
            }else{
                appDto= new AppPremisesPreInspectChklDto();
                appDto.setVersion(String.valueOf(1));
            }
            //update
            appDto.setId(null);
            appDto.setAppPremCorrId(dto.getCheckList().get(0).getAppPreCorreId());
            appDto.setAppPremCorrId(appPremCorrId);
            appDto.setChkLstConfId(configId);
            appDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            try {
                List<InspectionCheckListAnswerDto> answerDtoList = IaisCommonUtils.genNewArrayList();
                for (InspectionCheckQuestionDto inspectionCheckQuestionDto : icqDtoList) {
                    InspectionCheckListAnswerDto  answerDto = new InspectionCheckListAnswerDto();
                    answerDto.setAnswer(inspectionCheckQuestionDto.getChkanswer());
                    answerDto.setRemark(inspectionCheckQuestionDto.getRemark());
                    answerDto.setItemId(inspectionCheckQuestionDto.getItemId());
                    answerDto.setSectionId(inspectionCheckQuestionDto.getSectionId());
                    answerDto.setNcs(inspectionCheckQuestionDto.getNcs());
                    answerDto.setIdentify(dto.getVehicleName());
                    if ( ncflag && inspectionCheckQuestionDto.isRectified() &&"No".equals(inspectionCheckQuestionDto.getChkanswer())  ) {
                        answerDto.setIsRec("1");
                    } else {
                        answerDto.setIsRec("0");
                    }
                    answerDto.setSectionName(inspectionCheckQuestionDto.getSectionName());
                    answerDtoList.add(answerDto);
                }
                String answerJson = JsonUtil.parseToJson(answerDtoList);
                appDto.setAnswer(answerJson);
                fillUpCheckListGetAppClient.saveAppPreInspChkl(appDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    private List<AppPremisesPreInspectionNcItemDto> getAppPremisesPreInspectionNcItemDto(List<InspectionFillCheckListDto> fillcheckDtoList, AppPremPreInspectionNcDto ncDto,AdCheckListShowDto showDto) {
        List<AppPremisesPreInspectionNcItemDto> ncItemDtoList =  IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(fillcheckDtoList)){
            for(InspectionFillCheckListDto dto:fillcheckDtoList){
                List<InspectionCheckQuestionDto> insqDtoList = dto.getCheckList();
                for (InspectionCheckQuestionDto temp : insqDtoList) {
                    getAppNcByTemp(temp,ncDto,ncItemDtoList, dto.getVehicleName(),dto.getConfigId());
                }
            }
        }

        if(showDto != null && !IaisCommonUtils.isEmpty(showDto.getAdItemList())){
            for(AdhocNcCheckItemDto adhocNcCheckItemDto : showDto.getAdItemList()){
                if("No".equals(adhocNcCheckItemDto.getAdAnswer())){
                    AppPremisesPreInspectionNcItemDto ncItemDto = new AppPremisesPreInspectionNcItemDto();
                    ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    ncItemDto.setItemId(adhocNcCheckItemDto.getId());
                    ncItemDto.setPreNcId(ncDto.getId());
                    if( !StringUtil.isEmpty(adhocNcCheckItemDto.getRemark()) )
                        ncItemDto.setBeRemarks(adhocNcCheckItemDto.getRemark());
                    ncItemDto.setFeRectifiedFlag(0);
                    if (adhocNcCheckItemDto.getRectified()) {
                        ncItemDto.setIsRecitfied(1);
                    } else {
                        ncItemDto.setIsRecitfied(0);
                    }
                    ncItemDto.setNcs(adhocNcCheckItemDto.getNcs());
                    ncItemDto.setVehicleName(adhocNcCheckItemDto.getIdentify());
                    ncItemDtoList.add(ncItemDto);
                }
            }
        }
        return ncItemDtoList;
    }

    private void getAppNcByTemp(InspectionCheckQuestionDto temp, AppPremPreInspectionNcDto ncDto,List<AppPremisesPreInspectionNcItemDto> ncItemDtoList,String vehicleName,String configId){
        if("No".equals(temp.getChkanswer())){
            AppPremisesPreInspectionNcItemDto ncItemDto = new AppPremisesPreInspectionNcItemDto();
            ncItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ncItemDto.setItemId(temp.getItemId());
            ncItemDto.setPreNcId(ncDto.getId());
            if( !StringUtil.isEmpty(temp.getRemark()) )
                ncItemDto.setBeRemarks(temp.getRemark());
            ncItemDto.setFeRectifiedFlag(0);
            if (temp.isRectified()) {
                ncItemDto.setIsRecitfied(1);
            } else {
                ncItemDto.setIsRecitfied(0);
            }
            ncItemDto.setNcs(temp.getNcs());
            ncItemDto.setVehicleName(vehicleName);
            ncItemDto.setCheckListConfigId(configId);
            ncItemDtoList.add(ncItemDto);
        }
    }

    private AppPremPreInspectionNcDto getAppPremPreInspectionNcDto(String appPremCorrId) {
        AppPremPreInspectionNcDto ncDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        if(ncDto!=null){
            ncDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            fillUpCheckListGetAppClient.updateAppPreNc(ncDto);
            ncDto.setVersion(1 + Integer.parseInt(ncDto.getVersion())+"");
        }else{
            ncDto =  new AppPremPreInspectionNcDto();
            ncDto.setVersion(1+"");
        }
        ncDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        ncDto.setId(null);
        ncDto.setAppPremCorrId(appPremCorrId);
        ncDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return ncDto;
    }
    @Override
    public List<NcAnswerDto> getNcAnswerDtoList(String appPremCorrId){
        log.info(StringUtil.changeForLog("==>>" +appPremCorrId));

        List<NcAnswerDto> serviceNcDtoList = getServiceNcDtoList(appPremCorrId);
        List<NcAnswerDto> commonNcDtoList = getCommonNcDtoList(appPremCorrId);
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        if(serviceNcDtoList!=null && !serviceNcDtoList.isEmpty()){
            for(NcAnswerDto temp:serviceNcDtoList){
                ncAnswerDtoList.add(temp);
            }
        }
        if(commonNcDtoList!=null&&!commonNcDtoList.isEmpty()){
            for(NcAnswerDto temp:commonNcDtoList){
                ncAnswerDtoList.add(temp);
            }
        }
        //adhoc
        getAdhocNcItem(appPremCorrId,ncAnswerDtoList);
        log.info("=======>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(StringUtil.changeForLog("ncAnswerDtoList config" + JsonUtil.parseToJson(ncAnswerDtoList)));
        return ncAnswerDtoList;

    }

    private List<NcAnswerDto> getCommonNcDtoList(String appPremCorrId) {
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        log.info(StringUtil.changeForLog("AppPremisesPreInspectChklDto config" + JsonUtil.parseToJson(chkList)));

        List<AppPremisesPreInspectChklDto> commonChklList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(chkList)){
            for(AppPremisesPreInspectChklDto temp:chkList){
                ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(temp.getChkLstConfId()).getEntity();
                log.info(StringUtil.changeForLog("ChecklistConfigDto config" + JsonUtil.parseToJson(dto)));

                if(dto!=null&&dto.isCommon()){
                    commonChklList.add(temp);
                }
            }
        }
        AppPremisesPreInspectChklDto appPremisesPreInspectChklDto =null;
        if(!IaisCommonUtils.isEmpty(commonChklList)){
            appPremisesPreInspectChklDto = commonChklList.get(0);
        }
        if(appPremisesPreInspectChklDto!=null){
            String answerStr = appPremisesPreInspectChklDto.getAnswer();
            List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answerStr, InspectionCheckListAnswerDto.class);
            getCommNcDto(answerDtoList,ncAnswerDtoList);
        }
        return ncAnswerDtoList;
    }

    public void getCommNcDto( List<InspectionCheckListAnswerDto> answerDtoList,List<NcAnswerDto> ncAnswerDtoList){
        NcAnswerDto ncAnswerDto = null;
        for(InspectionCheckListAnswerDto temp:answerDtoList){
            if("No".equals(temp.getAnswer())){
                ncAnswerDto = new NcAnswerDto();
                ncAnswerDto.setItemId(temp.getItemId());
                ncAnswerDto.setRemark(temp.getRemark());
                ncAnswerDto.setRef("1".equalsIgnoreCase(temp.getIsRec()) ? "1" :"0");
                ncAnswerDto.setType(temp.getSectionName());
                ncAnswerDto.setNcs(temp.getNcs());
                ncAnswerDto.setVehicleName(temp.getIdentify());
                ncAnswerDtoList.add(ncAnswerDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(ncAnswerDtoList)){
            for(NcAnswerDto temp:ncAnswerDtoList){
                getFillNcAnswerDto(temp);
            }
        }
    }



    private List<NcAnswerDto> getServiceNcDtoList(String appPremCorrId) {
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        log.info(StringUtil.changeForLog("AppPremisesPreInspectChklDto config" + JsonUtil.parseToJson(chkList)));
        List<AppPremisesPreInspectChklDto> serviceChklList = IaisCommonUtils.genNewArrayList();
        if(chkList!=null&&!chkList.isEmpty()){
            for(AppPremisesPreInspectChklDto temp:chkList){
                ChecklistConfigDto dto = hcsaChklClient.getChecklistConfigById(temp.getChkLstConfId()).getEntity();
                log.info(StringUtil.changeForLog("checklist config" + JsonUtil.parseToJson(dto)));
                if(dto!=null&&!dto.isCommon()){
                    serviceChklList.add(temp);
                }
            }
        }
        List<NcAnswerDto> ncList = null;
        if(!serviceChklList.isEmpty()){
            ncList = getNcList(serviceChklList);
        }
        return ncList;

    }

    private List<NcAnswerDto> getNcList(List<AppPremisesPreInspectChklDto> serviceChklList) {
        List<NcAnswerDto> ncAnswerDtoList = IaisCommonUtils.genNewArrayList();
        for(AppPremisesPreInspectChklDto achk:serviceChklList){
            if(achk!=null){
                String answerStr = achk.getAnswer();
                if(StringUtil.isEmpty(answerStr)){
                    log.info(StringUtil.changeForLog("------------ AppPremisesPreInspectChklDto corrId " + StringUtil.getNonNull(achk.getAppPremCorrId()) + " is error data." ));
                    continue;
                }
                List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answerStr, InspectionCheckListAnswerDto.class);
                if(!IaisCommonUtils.isEmpty(answerDtoList)){
                    getServiceNc(answerDtoList,ncAnswerDtoList);
                }
            }
        }
        return ncAnswerDtoList;
    }

    public void getServiceNc(List<InspectionCheckListAnswerDto> answerDtoList,List<NcAnswerDto> ncAnswerDtoList){
        NcAnswerDto ncAnswerDto = null;
        for(InspectionCheckListAnswerDto temp:answerDtoList){
            if("No".equals(temp.getAnswer())){
                ncAnswerDto = new NcAnswerDto();
                ncAnswerDto.setItemId(temp.getItemId());
                ncAnswerDto.setRemark(temp.getRemark());
                ncAnswerDto.setRef("1".equalsIgnoreCase(temp.getIsRec()) ? "1" :"0");
                ncAnswerDto.setType(temp.getSectionName());
                ncAnswerDto.setNcs(temp.getNcs());
                ncAnswerDto.setVehicleName(temp.getIdentify());
                ncAnswerDtoList.add(ncAnswerDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(ncAnswerDtoList)){
            for(NcAnswerDto temp:ncAnswerDtoList){
                getFillNcAnswerDto(temp);
            }
        }
    }

    public void getAdhocNcItem(String appPremId ,List<NcAnswerDto> ncAnswerDtoList){
        AdCheckListShowDto adchklDto =getAdhocCheckListDto(appPremId);
        NcAnswerDto ncDto = null;
        List<AdhocNcCheckItemDto> adItemList = adchklDto.getAdItemList();
        if(adItemList!=null && !adItemList.isEmpty()){
            for(AdhocNcCheckItemDto temp:adItemList){
                if("No".equals(temp.getAdAnswer())){
                    ncDto = new NcAnswerDto();
                    ncDto.setItemQuestion(temp.getQuestion());
                    ncDto.setRemark(temp.getRemark());
                    ncDto.setRef((temp.getRectified()!= null && temp.getRectified())? "1" : "0");
                    ncDto.setNcs(temp.getNcs());
                    ncDto.setVehicleName(temp.getIdentify());
                    ncDto.setType("Adhoc");
                    ncAnswerDtoList.add(ncDto);
                }
            }
        }
    }
    public void getFillNcAnswerDto(NcAnswerDto ncAnswerDto){
        String itemId = ncAnswerDto.getItemId();
        ChecklistItemDto cDto = hcsaChklClient.getChklItemById(itemId).getEntity();
        ncAnswerDto.setItemQuestion(cDto.getChecklistItem());
        HcsaChklSvcRegulationDto regDto = hcsaChklClient.getRegulationDtoById(cDto.getRegulationId()).getEntity();
        ncAnswerDto.setCaluseNo(regDto.getClauseNo());
        ncAnswerDto.setClause(regDto.getClause());
    }

    @Override
    public void updateTaskStatus(ApplicationDto applicationDto,String appPremCorrId) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrId).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    @Override
    public void getCommonDto(InspectionFillCheckListDto commonDto, AppPremisesPreInspectChklDto appPremDto) {
        List<InspectionCheckQuestionDto> fillCheckList = commonDto.getCheckList();
        List<InspectionCheckQuestionDto> ncCheckList = IaisCommonUtils.genNewArrayList();
        String answer = appPremDto.getAnswer();
        List<InspectionCheckListAnswerDto> answerDtoList = JsonUtil.parseToList(answer, InspectionCheckListAnswerDto.class);
        if (!IaisCommonUtils.isEmpty(fillCheckList)) {
            for (InspectionCheckQuestionDto temp : fillCheckList) {
                for (int i = 0; i < answerDtoList.size(); i++) {
                    getComAnswer(temp,answerDtoList,ncCheckList, i);
                }
            }
        }
        commonDto.setCheckList(ncCheckList);
        fillInspectionFillCheckListDto(commonDto);
    }
    public void getComAnswer(InspectionCheckQuestionDto temp,List<InspectionCheckListAnswerDto> answerDtoList,List<InspectionCheckQuestionDto> ncCheckList,int i){
        if (temp.getItemId().equals(answerDtoList.get(i).getItemId()) &&
                temp.getSectionName().equals(answerDtoList.get(i).getSectionName())) {
            temp.setChkanswer(answerDtoList.get(i).getAnswer());
            temp.setRemark(answerDtoList.get(i).getRemark());
            if(getComAnswerLogic(answerDtoList.get(i).getIsRec(),answerDtoList.get(i).getAnswer())){
                temp.setRectified(true);
            }else{
                temp.setRectified(false);
            }
            temp.setNcs(answerDtoList.get(i).getNcs());
            ncCheckList.add(temp);
        }
    }

    public boolean getComAnswerLogic(String recNum,String answer){
        if("1".equals(recNum)&&"No".equals(answer)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public AdCheckListShowDto getAdhocCheckListDto(String appPremCorrId) {
        AdCheckListShowDto adCheckListShowDto = fillupChklistService.getAdhoc(appPremCorrId);
        if(adCheckListShowDto!=null){
            List<AdhocNcCheckItemDto> adItemList = adCheckListShowDto.getAdItemList();
            if(adItemList!=null && !adItemList.isEmpty()){
                for(AdhocNcCheckItemDto temp:adItemList){
                    String answerStr = temp.getAnswer();
                    if(!StringUtil.isEmpty(answerStr)){
                        AdhocAnswerDto answerDto = JsonUtil.parseToObject(answerStr,AdhocAnswerDto.class);
                        if(answerDto != null){
                            temp.setAdAnswer(answerDto.getAnswer());
                            temp.setRemark(answerDto.getRemark());
                            temp.setNcs(answerDto.getNcs());
                            temp.setIdentify(answerDto.getIdentify());
                        }
                    }

                }
            }
        }
        return adCheckListShowDto;
    }

    public boolean isHaveNc(InspectionFillCheckListDto dto){
        if(dto!=null){
            List<InspectionCheckQuestionDto> dtoList = dto.getCheckList();
            if(!IaisCommonUtils.isEmpty(dtoList)){
                for(InspectionCheckQuestionDto temp:dtoList){
                    if("No".equals(temp.getChkanswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isHaveNcOrBestPractice(InspectionFDtosDto serListDto, InspectionFillCheckListDto comDto, AdCheckListShowDto showDto) {
        if(serListDto!=null){
            String bestPractice = serListDto.getBestPractice();
            if(!StringUtil.isEmpty(bestPractice)||serListDto.getTotalNcNum()>0){
                return true;
            }
        }
        if(haveServiceNc(serListDto)||haveComNc(comDto)||haveAdhocDto(showDto)){
            return true;
        }
        return false;
    }

    private boolean haveAdhocDto(AdCheckListShowDto showDto) {
        if(showDto!=null){
            if(!IaisCommonUtils.isEmpty(showDto.getAdItemList())){
                for(AdhocNcCheckItemDto temp:showDto.getAdItemList()){
                    if("No".equals(temp.getAdAnswer())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean haveComNc(InspectionFillCheckListDto comDto) {
        return isHaveNc(comDto);
    }

    private boolean haveServiceNc(InspectionFDtosDto serListDto) {
        int ncflagNum = 0;
        if(serListDto!=null){
            if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
                for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                    if(isHaveNc(temp)){
                        ncflagNum++;
                    }
                }
            }
        }
        if(ncflagNum>0){
            return true;
        }
        return false;
    }



    // only for show check list
    @Override
    public  void  getInspectionFillCheckListDtoForShow(InspectionFillCheckListDto inspectionFillCheckListDto){
        if(inspectionFillCheckListDto != null){
            inspectionFillCheckListDto.setSvcNameShow( StringUtil.getFilterSpecialCharacter( inspectionFillCheckListDto.getSubName()));
            List<SectionDto> sectionDtos = inspectionFillCheckListDto.getSectionDtoList();
            if(sectionDtos != null && sectionDtos.size() > 0){
                for(SectionDto sectionDto : sectionDtos){
                    if(sectionDto != null && sectionDto.getItemDtoList() != null && sectionDto.getItemDtoList().size() > 0){
                        List<ItemDto> itemDtoList = sectionDto.getItemDtoList();
                        for(ItemDto itemDto : itemDtoList){
                            if(itemDto.getIncqDto() != null)
                                itemDto.getIncqDto().setSectionNameShow(StringUtil.getFilterSpecialCharacter(itemDto.getIncqDto().getSectionNameSub()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveDraftChecklist(InspectionFillCheckListDto infillDto, AdCheckListShowDto showDto, InspectionFDtosDto serListDto, String appPremId) {
        CheckListDraftAllDto checkListDraftAllDto = new CheckListDraftAllDto();
        checkListDraftAllDto.setCommonDto(infillDto);
        checkListDraftAllDto.setFdtoList(serListDto.getFdtoList());
        checkListDraftAllDto.setAdCheckListShowDto(showDto);
        checkListDraftAllDto.setRefNo(appPremId);
        checkListDraftAllDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fillUpCheckListGetAppClient.saveDraftAnswerForCheckList(checkListDraftAllDto);
    }

    @Override
    public void saveDraftSpecChecklist(InspectionFillCheckListDto infillDto, String appPremId, List<InspectionSpecServiceDto> inspectionSpecServiceDtos,AdCheckListShowDto showDto, InspectionFDtosDto serListDto) {
        CheckListDraftAllDto checkListDraftAllDto = new CheckListDraftAllDto();
        checkListDraftAllDto.setCommonDto(infillDto);
        List<InspectionFillCheckListDto> fdtoList = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(serListDto.getFdtoList())){
            fdtoList.addAll(serListDto.getFdtoList());
        }
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(showDto);
        if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : inspectionSpecServiceDtos){
                if(IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getFdtoList())){
                    fdtoList .addAll(inspectionSpecServiceDto.getFdtoList());
                }
                if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
                    if(adchklDto  == null){
                        adchklDto = inspectionSpecServiceDto.getAdchklDto();
                        List<AdhocNcCheckItemDto> adItemList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
                        setIdentifyForAdItemList(adItemList,inspectionSpecServiceDto.getIdentify());
                    }else {
                        List<AdhocNcCheckItemDto> adItemList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
                        setIdentifyForAdItemList(adItemList,inspectionSpecServiceDto.getIdentify());
                        List<AdhocNcCheckItemDto> adItemListAll =  adchklDto.getAdItemList();
                        adItemListAll.addAll(adItemList);
                    }
                }
            }
        }
        checkListDraftAllDto.setFdtoList(fdtoList);
        checkListDraftAllDto.setAdCheckListShowDto(adchklDto);
        checkListDraftAllDto.setRefNo(appPremId);
        checkListDraftAllDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        checkListDraftAllDto.setVehicleService(true);
        fillUpCheckListGetAppClient.saveDraftAnswerForCheckList(checkListDraftAllDto);
    }

    @Override
    public void saveBeforeSubmitCheckList(InspectionFillCheckListDto commDto, AdCheckListShowDto showDto, InspectionFDtosDto serListDto, String appPremId) {
        List<InspectionFillCheckListDto> fillcheckDtoList = getFillcheckDtoListAndSaveCheckListNoVehicle(commDto,serListDto,appPremId);
        if (showDto != null) {
            saveAdhocDto(showDto, appPremId);
        }
        if(!fillcheckDtoList.isEmpty() || (showDto != null && !IaisCommonUtils.isEmpty(showDto.getAdItemList()))){
            saveNcItem(fillcheckDtoList,appPremId,showDto);
        }
    }

    private List<InspectionFillCheckListDto> getFillcheckDtoListAndSaveCheckListNoVehicle(InspectionFillCheckListDto commDto, InspectionFDtosDto serListDto,String appPremId){
        List<InspectionFillCheckListDto> fillcheckDtoList = IaisCommonUtils.genNewArrayList();
        if(commDto!=null){
            saveInspectionCheckListDto(commDto,appPremId);
        }
        saveSerListDto(serListDto,appPremId);
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto temp:serListDto.getFdtoList()){
                fillcheckDtoList.add(temp);
            }
        }
        if(commDto!=null&&commDto.getCheckList()!=null&&!commDto.getCheckList().isEmpty()){
            fillcheckDtoList.add(commDto);
        }
        return fillcheckDtoList;
    }


    @Override
    public void saveOtherDataForPengingIns(InspectionFDtosDto serListDto, String appPremId) {
        //saveInspectionDate(serListDto,appPremId);
        saveStartTime(serListDto,appPremId);
        saveEndTime(serListDto,appPremId);
        saveOtherInspection(serListDto,appPremId);
        saveRecommend(serListDto,appPremId);
        //save observation
        saveObservation(serListDto, appPremId);
        saveLitterFile(serListDto);
        if(serListDto.isSpecServiceVehicle()){
            saveSpecServiceVerdict(appPremId);
        }
    }

    private void saveObservation(InspectionFDtosDto serListDto, String appPremId) {
        if(serListDto != null) {
            String observation = serListDto.getObservation();
            if(!StringUtil.isEmpty(observation)) {
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType
                        (appPremId, InspectionConstants.RECOM_TYPE_INSP_CHECKLIST_OBSERVATION).getEntity();
                if(appPremisesRecommendationDto != null) {
                    appPremisesRecommendationDto.setRemarks(observation);
                } else {
                    appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                    appPremisesRecommendationDto.setVersion(1);
                    appPremisesRecommendationDto.setId(null);
                    appPremisesRecommendationDto.setAppPremCorreId(appPremId);
                    appPremisesRecommendationDto.setRemarks(observation);
                    appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_CHECKLIST_OBSERVATION);
                    appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                }
                fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
            }
        }
    }

    private void saveSpecServiceVerdict(String appPremId){
        if(fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.PROCESS_DECI_IS_SPEC_SERVICE_VEHICLE).getEntity() == null){
            AppPremisesRecommendationDto appPreRecommentdationDto = new AppPremisesRecommendationDto();
            appPreRecommentdationDto.setVersion(1);
            appPreRecommentdationDto.setId(null);
            appPreRecommentdationDto.setAppPremCorreId(appPremId);
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPreRecommentdationDto.setRecomType(InspectionConstants.PROCESS_DECI_IS_SPEC_SERVICE_VEHICLE);
            appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
        }
    }


    @Override
    public void saveBeforeFinishCheckListRec(String appPremId,String mobileRemarks) {
        AppPremisesRecommendationDto appPreRecommentdationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremId,InspectionConstants.RECOM_TYPE_INSP_FINISH_CHECKLIST_BEFORE).getEntity();
        if( appPreRecommentdationDto != null){
            log.info(StringUtil.changeForLog("-------------saveBeforeFinishCheckListRec appPremId : " + appPremId +" have exist ------------"));
        }else {
            appPreRecommentdationDto = new AppPremisesRecommendationDto();
            appPreRecommentdationDto.setVersion(1);
            appPreRecommentdationDto.setId(null);
            appPreRecommentdationDto.setAppPremCorreId(appPremId);
            appPreRecommentdationDto.setRemarks(mobileRemarks);
            appPreRecommentdationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPreRecommentdationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_FINISH_CHECKLIST_BEFORE);
            appPreRecommentdationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            fillUpCheckListGetAppClient.saveAppRecom(appPreRecommentdationDto);
        }

    }
}
