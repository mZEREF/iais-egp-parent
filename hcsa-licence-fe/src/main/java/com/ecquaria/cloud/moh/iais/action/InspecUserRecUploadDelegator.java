package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Process MohInspecUserRectifiUpload
 *
 * @author Shicheng
 * @date 2019/12/23 15:20
 **/
@Delegator("inspecUserRecUploadDelegator")
@Slf4j
public class InspecUserRecUploadDelegator {

    @Autowired
    private InspecUserRecUploadService inspecUserRecUploadService;

    /**
     * StartStep: inspecUserRectifiUploadStart
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadStart start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("User Rectification Upload", "Upload Doc Rectification");
    }

    /**
     * StartStep: inspecUserRectifiUploadInit
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", null);
        ParamUtil.setSessionAttr(bpc.request, "buttonFlag", null);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", null);
    }

    /**
     * StartStep: inspecUserRectifiUploadPre
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadPre start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        if(inspecUserRecUploadDtos == null){
            String appPremCorrId = ParamUtil.getRequestString(bpc.request, "appPremCorrId");
            String versionStr = ParamUtil.getRequestString(bpc.request, "recVersion");
            int version = 0;
            if(!StringUtil.isEmpty(versionStr)){
                version = Integer.parseInt(versionStr);
            }
            List<ChecklistItemDto> checklistItemDtos = inspecUserRecUploadService.getQuesAndClause(appPremCorrId);
            ApplicationDto applicationDto = inspecUserRecUploadService.getApplicationByCorrId(appPremCorrId);
            inspecUserRecUploadDtos = IaisCommonUtils.genNewArrayList();
            int index = -1;
            if(checklistItemDtos != null && !(checklistItemDtos.isEmpty())) {
                for (ChecklistItemDto cDto : checklistItemDtos) {
                    InspecUserRecUploadDto iDto = new InspecUserRecUploadDto();
                    iDto.setCheckClause(cDto.getRegulationClause());
                    iDto.setCheckQuestion(cDto.getChecklistItem());
                    iDto.setIndex(index++);
                    iDto.setAppNo(applicationDto.getApplicationNo());
                    iDto.setItemId(cDto.getItemId());
                    iDto = inspecUserRecUploadService.getNcItemData(iDto, version, appPremCorrId);
                    AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = iDto.getAppPremisesPreInspectionNcItemDto();
                    if(appPremisesPreInspectionNcItemDto != null) {
                        int feRec = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                        if (1 == feRec) {
                            iDto.setButtonFlag(AppConsts.SUCCESS);
                        } else if (0 == feRec) {
                            iDto.setButtonFlag(AppConsts.FAIL);
                        }
                        int rec = iDto.getAppPremisesPreInspectionNcItemDto().getIsRecitfied();
                        if (0 == rec) {
                            inspecUserRecUploadDtos.add(iDto);
                        }
                    }
                }
            }
        }

        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    /**
     * StartStep: inspecUserRectifiUploadVali
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadVali start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        InspecUserRecUploadDto inspecUserRecUploadDto = (InspecUserRecUploadDto)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDto");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionValue = mulReq.getParameter("actionValue");
        ParamUtil.setRequestAttr(bpc.request, "inspecUserRecUploadType", actionValue);
        //get file from page
        CommonsMultipartFile file = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        //do validate
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue) || InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)){
            log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
            errorMap = doValidateByRecFile(inspecUserRecUploadDto, mulReq, errorMap, actionValue, file);
            if(errorMap != null && !(errorMap.isEmpty())){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue)) {
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setFileName(file.getOriginalFilename());
                    fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fileRepoDto.setRelativePath(AppConsts.FALSE);
                    String auditTrailStr = JsonUtil.parseToJson(fileRepoDto);
                    inspecUserRecUploadDto = inspecUserRecUploadService.saveFileReportGetFileId(inspecUserRecUploadDto, auditTrailStr, file);
                }
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
        log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    private Map<String, String> doValidateByRecFile(InspecUserRecUploadDto inspecUserRecUploadDto, MultipartHttpServletRequest mulReq,
                                                    Map<String, String> errorMap, String actionValue, CommonsMultipartFile file) {
        String uploadRemarks = mulReq.getParameter("uploadRemarks");
        inspecUserRecUploadDto.setUploadRemarks(uploadRemarks);
        int uploadRemarksLen = uploadRemarks.length();
        if(300 < uploadRemarksLen){
            errorMap.put("remarks", "The Remarks should not be more than 300 characters.");
        }
        String errorKey = "recFile";

        if(InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)) {
            if (IaisCommonUtils.isEmpty(inspecUserRecUploadDto.getFileRepoDtos())) {
                errorMap.put(errorKey, "ERR0009");
                return errorMap;
            }
        } else {
            Boolean flag = false;
            String fileName = file.getOriginalFilename();
            String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (file.getSize() > 4 * 1024 * 1024) {
                errorMap.put(errorKey, "The file has exceeded the maximum upload size of 4MB.");
                return errorMap;
            }
            FileType[] values = FileType.values();
            for (FileType f : values) {
                if (f.name().equalsIgnoreCase(substring)) {
                    flag = true;
                }
            }

            if (!flag) {
                errorMap.put(errorKey, "The file type is incorrect.");
                return errorMap;
            }
            if (file.getSize() / 1024 > 0) {
                if (file.getSize() / 1024 < 1) {
                    inspecUserRecUploadDto.setFileSize(1);
                } else {
                    inspecUserRecUploadDto.setFileSize((int) file.getSize() / 1024);
                }
            }
            inspecUserRecUploadDto.setFileName(fileName);
        }
        return errorMap;
    }

    /**
     * StartStep: inspecUserRectifiUploadConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadConfirm start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        String itemId = ParamUtil.getMaskedString(bpc.request, "itemId");
        log.info(StringUtil.changeForLog("The item id is ===>" + itemId));
        if(!StringUtil.isEmpty(itemId)) {
            InspecUserRecUploadDto inspecUserRecUploadDto = null;
            for (InspecUserRecUploadDto iuruDto : inspecUserRecUploadDtos) {
                if (!StringUtil.isEmpty(itemId)) {
                    if (itemId.equals(iuruDto.getItemId())) {
                        inspecUserRecUploadDto = iuruDto;
                    }
                }
            }
            log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
            ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    /**
     * StartStep: step1
     *
     * @param bpc
     * @throws
     */
    public void step1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the step1 start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    /**
     * StartStep: inspecUserRectifiUploadAdd
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadAdd(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadAdd start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        InspecUserRecUploadDto inspecUserRecUploadDto = (InspecUserRecUploadDto)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDto");
        ParamUtil.setRequestAttr(bpc.request, "inspecUserRecUploadType", "confirm");
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
        log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
    }

    /**
     * StartStep: inspecUserRectifiUploadDel
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadDel(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadDel start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        InspecUserRecUploadDto inspecUserRecUploadDto = (InspecUserRecUploadDto)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDto");
        String removeId = ParamUtil.getMaskedString(bpc.request, "fileId");
        if(!StringUtil.isEmpty(removeId)) {
            inspecUserRecUploadDto = inspecUserRecUploadService.removeFileByFileId(inspecUserRecUploadDto, removeId);
        }
        ParamUtil.setRequestAttr(bpc.request, "inspecUserRecUploadType", "confirm");
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
        log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
    }

    /**
     * StartStep: inspecUserRectifiUploadSuccess
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadSuccess start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        InspecUserRecUploadDto inspecUserRecUploadDto = (InspecUserRecUploadDto)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        inspecUserRecUploadService.submitRecByUser(loginContext, inspecUserRecUploadDto);
        for(InspecUserRecUploadDto iDto : inspecUserRecUploadDtos){
            if(iDto.getItemId().equals(inspecUserRecUploadDto.getItemId())){
                iDto.setButtonFlag(AppConsts.SUCCESS);
            }
        }
        inspecUserRecUploadDto.setButtonFlag(AppConsts.SUCCESS);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
        log.info("The dto we checked is null ===>" + (inspecUserRecUploadDto == null));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
    }
}
