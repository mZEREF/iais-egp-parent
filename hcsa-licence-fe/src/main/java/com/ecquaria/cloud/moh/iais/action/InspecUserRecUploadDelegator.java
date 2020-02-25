package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
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
import java.util.ArrayList;
import java.util.HashMap;
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
            List<ChecklistItemDto> checklistItemDtos = inspecUserRecUploadService.getQuesAndClause(appPremCorrId);
            ApplicationDto applicationDto = inspecUserRecUploadService.getApplicationByCorrId(appPremCorrId);
            inspecUserRecUploadDtos = new ArrayList<>();
            int index = -1;
            if(checklistItemDtos != null && !(checklistItemDtos.isEmpty())) {
                for (ChecklistItemDto cDto : checklistItemDtos) {
                    InspecUserRecUploadDto iDto = new InspecUserRecUploadDto();
                    iDto.setCheckClause(cDto.getRegulationClause());
                    iDto.setCheckQuestion(cDto.getChecklistItem());
                    iDto.setIndex(index++);
                    iDto.setAppNo(applicationDto.getApplicationNo());
                    iDto.setItemId(cDto.getItemId());
                    inspecUserRecUploadDtos.add(iDto);
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
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        Map<String,String> errorMap = new HashMap<>();
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            errorMap = doValidateByRecFile(inspecUserRecUploadDtos, mulReq, errorMap);
            if(errorMap != null && !(errorMap.isEmpty())){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    private Map<String, String> doValidateByRecFile(List<InspecUserRecUploadDto> inspecUserRecUploadDtos,
                                                    MultipartHttpServletRequest mulReq, Map<String, String> errorMap) {
        for(InspecUserRecUploadDto iDto: inspecUserRecUploadDtos){
            String delFlagName = InspectionConstants.INSEPCTION_FILE_NAME_DELFLAG + iDto.getIndex();
            String selectedFileName = InspectionConstants.INSEPCTION_FILE_NAME_SELECTEDFILE + iDto.getIndex();
            String uploadRemarksName = InspectionConstants.INSEPCTION_FILE_NAME_UPLOADREMARKS + iDto.getIndex();
            String delFlagValue =  mulReq.getParameter(delFlagName);
            String uploadRemarks = mulReq.getParameter(uploadRemarksName);
            String errorKey = "recFile" + iDto.getIndex();
            CommonsMultipartFile file = (CommonsMultipartFile) mulReq.getFile(selectedFileName);
            if(file == null || file.getSize() == 0){
                if(SystemParameterConstants.STATUS_DEACTIVATED.equals(delFlagValue)) {
                    file = iDto.getRecFile();
                }
            }
            if(file.isEmpty() || file.getSize() == 0) {
                errorMap.put(errorKey, "ERR0009");
                continue;
            }
            Boolean flag = false;
            String fileName = file.getOriginalFilename();
            String substring = fileName.substring(fileName.lastIndexOf(".")+1);
            if(file.getSize() > 4*1024*1024) {
                errorMap.put(errorKey, "The file has exceeded the maximum upload size of 4MB.");
                continue;
            }
            FileType[] values = FileType.values();
            for(FileType f:values){
                if(f.name().equalsIgnoreCase(substring)){
                    flag = true;
                }
            }

            if(!flag){
                errorMap.put(errorKey,"The file type is incorrect.");
                continue;
            }
            iDto.setRecFile(file);
            if(file.getSize() / 1024 > 0){
                if(file.getSize() / 1024 < 1){
                    iDto.setFileSize(1);
                } else {
                    iDto.setFileSize((int)file.getSize() / 1024);
                }
            }
            iDto.setFileName(fileName);
            iDto.setUploadRemarks(uploadRemarks);
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
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    /**
     * StartStep: inspecUserRectifiUploadQuery
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadQuery start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        String auditTrailStr = JsonUtil.parseToJson(auditTrailDto);
        inspecUserRecUploadService.submitRecByUser(loginContext, auditTrailStr, inspecUserRecUploadDtos);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
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
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }
}
