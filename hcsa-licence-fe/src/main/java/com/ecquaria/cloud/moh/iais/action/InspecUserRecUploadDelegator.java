package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
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
        String appPremCorrId = ParamUtil.getMaskedString(bpc.request, "appPremCorrId");
        String versionStr = ParamUtil.getRequestString(bpc.request, "recVersion");
        InspSetMaskValueDto inspSetMaskValueDto = new InspSetMaskValueDto();
        inspSetMaskValueDto.setAppPremCorrId(appPremCorrId);
        inspSetMaskValueDto.setVersion(versionStr);
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", inspSetMaskValueDto);
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
        ParamUtil.setSessionAttr(bpc.request, "submitButtonFlag", null);
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
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, "inspSetMaskValueDto");
        if(inspecUserRecUploadDtos == null){
            String appPremCorrId = inspSetMaskValueDto.getAppPremCorrId();
            String versionStr = inspSetMaskValueDto.getVersion();
            int version = 0;
            if(!StringUtil.isEmpty(versionStr)){
                version = Integer.parseInt(versionStr);
            }
            List<ChecklistItemDto> checklistItemDtos = inspecUserRecUploadService.getQuesAndClause(appPremCorrId);
            inspecUserRecUploadDtos = inspecUserRecUploadService.getNcItemData(version, appPremCorrId, checklistItemDtos);
        } else {
            for(InspecUserRecUploadDto inspecUserRecUploadDto : inspecUserRecUploadDtos){
                List<FileRepoDto> fileRepoDtos = inspecUserRecUploadDto.getFileRepoDtos();
                if(!IaisCommonUtils.isEmpty(fileRepoDtos)){
                    inspecUserRecUploadDto.setRectifyFlag(AppConsts.SUCCESS);
                } else {
                    inspecUserRecUploadDto.setRectifyFlag(AppConsts.FAIL);
                }
            }
        }
        String submitButtonFlag = ncIsAllRectified(inspecUserRecUploadDtos);

        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
        ParamUtil.setSessionAttr(bpc.request, "submitButtonFlag", submitButtonFlag);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"List of Rectifications");
    }

    private String ncIsAllRectified(List<InspecUserRecUploadDto> inspecUserRecUploadDtos) {
        String submitButtonFlag = InspectionConstants.SWITCH_ACTION_SUBMIT;
        if(!IaisCommonUtils.isEmpty(inspecUserRecUploadDtos)){
            for(InspecUserRecUploadDto inspecUserRecUploadDto : inspecUserRecUploadDtos){
                String buttonFlag = inspecUserRecUploadDto.getButtonFlag();
                if(!StringUtil.isEmpty(buttonFlag) && AppConsts.SUCCESS.equals(buttonFlag)){
                    return InspectionConstants.SWITCH_ACTION_REJECT;
                }
            }
        }
        return submitButtonFlag;
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
        //get file from page
        CommonsMultipartFile file = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        //do validate
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        if(InspectionConstants.SWITCH_ACTION_ADD.equals(actionValue) || InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)){
            log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
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
        } else if(InspectionConstants.SWITCH_ACTION_CANCEL.equals(actionValue)){
            //remove new add file
            inspecUserRecUploadDto = inspecUserRecUploadService.removeFileAndNcDocs(inspecUserRecUploadDto);
            //recover delete file
            inspecUserRecUploadDto = inspecUserRecUploadService.recoverFile(inspecUserRecUploadDto);
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
            actionValue = "review";
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
        log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
        ParamUtil.setRequestAttr(bpc.request, "inspecUserRecUploadType", actionValue);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
    }

    private Map<String, String> doValidateByRecFile(InspecUserRecUploadDto inspecUserRecUploadDto, MultipartHttpServletRequest mulReq,
                                                    Map<String, String> errorMap, String actionValue, CommonsMultipartFile file) {
        String uploadRemarks = mulReq.getParameter("uploadRemarks");
        inspecUserRecUploadDto.setUploadRemarks(uploadRemarks);
        int uploadRemarksLen = uploadRemarks.length();
        String errorKey = "recFile";

        if(InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)) {
            if (IaisCommonUtils.isEmpty(inspecUserRecUploadDto.getFileRepoDtos())) {
                errorMap.put(errorKey, "ERR0009");
                return errorMap;
            }
            if(300 < uploadRemarksLen){
                errorMap.put("remarks", "The Remarks should not be more than 300 characters.");
            }
        } else {
            Boolean flag = Boolean.FALSE;
            String fileName = file.getOriginalFilename();
            String substring = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (file.getSize() > 4 * 1024 * 1024) {
                errorMap.put(errorKey, "The file has exceeded the maximum upload size of 4MB.");
                return errorMap;
            }
            FileType[] values = FileType.values();
            for (FileType f : values) {
                if (f.name().equalsIgnoreCase(substring)) {
                    flag = Boolean.TRUE;
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
        String ncItemId = ParamUtil.getMaskedString(bpc.request, "ncItemId");
        log.info(StringUtil.changeForLog("The item id is ===>" + ncItemId));
        if(!StringUtil.isEmpty(ncItemId)) {
            InspecUserRecUploadDto inspecUserRecUploadDto = null;
            for (InspecUserRecUploadDto iuruDto : inspecUserRecUploadDtos) {
                if (!StringUtil.isEmpty(ncItemId)) {
                    if (ncItemId.equals(iuruDto.getId())) {
                        inspecUserRecUploadDto = iuruDto;
                        break;
                    }
                }
            }
            log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
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
        log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
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
        log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
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
        List<InspecUserRecUploadDto> inspecUserRecUploadDtoList = IaisCommonUtils.genNewArrayList();
        for(InspecUserRecUploadDto iDto : inspecUserRecUploadDtos){
            if(iDto.getId().equals(inspecUserRecUploadDto.getId())){
                inspecUserRecUploadDtoList.add(inspecUserRecUploadDto);
            } else {
                inspecUserRecUploadDtoList.add(iDto);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtoList);
        log.info(StringUtil.changeForLog("The dto we checked is null ===>" + (inspecUserRecUploadDto == null)));
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDto", inspecUserRecUploadDto);
    }

    /**
     * StartStep: inspecUserRectifiUploadSubmit
     *
     * @param bpc
     * @throws
     */
    public void inspecUserRectifiUploadSubmit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspecUserRectifiUploadSubmit start ...."));
        List<InspecUserRecUploadDto> inspecUserRecUploadDtos = (List<InspecUserRecUploadDto>)ParamUtil.getSessionAttr(bpc.request, "inspecUserRecUploadDtos");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!IaisCommonUtils.isEmpty(inspecUserRecUploadDtos)) {
            //do validate
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            for (InspecUserRecUploadDto inspecUserRecUploadDto : inspecUserRecUploadDtos) {
                if(!AppConsts.SUCCESS.equals((inspecUserRecUploadDto.getRectifyFlag()))){
                    errorMap.put("subFlag", "UC_INSP_ERR0008");
                }
            }
            if(errorMap != null){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "subflag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request, "subflag", AppConsts.TRUE);
                inspecUserRecUploadService.submitAllRecNc(inspecUserRecUploadDtos, loginContext);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecUserRecUploadDtos", (Serializable) inspecUserRecUploadDtos);
        ParamUtil.setRequestAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"Acknowledgement");
    }
}
