package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaRegulationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import ecq.commons.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

@Delegator(value = "hcsaRegulationDelegator")
@Slf4j
public class RegulationDelegator {


    @Autowired
    private RegulationService regulationService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(RegulationQueryDto.class)
            .searchAttr(HcsaRegulationConstants.PARAM_SEARCH)
            .resultAttr(HcsaRegulationConstants.PARAM_RESULT)
            .sortField("id").build();

    /**
     * @AutoStep: startStep
     * @param:
     * @return:
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("Checklist Regulation", "Regulation");
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", null);

        ParamUtil.setSessionAttr(bpc.request, HcsaRegulationConstants.PARAM_SEARCH, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaChecklistConstants.PARAM_REGULATION_DESC, null);
    }


    /**
     * @AutoStep: doQuery
     * @param:
     * @return:
     * @author: yichen
     */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        RegulationQueryDto regulation = new RegulationQueryDto();

        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);

        regulation.setClauseNo(clause);
        regulation.setClause(desc);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, desc);

        ValidationResult validationResult = WebValidationHelper.validateProperty(regulation, "search");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if(!StringUtil.isEmpty(clause)){
            searchParam.addFilter("clauseNo", clause, true);
        }

        if(!StringUtil.isEmpty(desc)){
            searchParam.addFilter("clause", desc, true);
        }
    }


    /**
     * @AutoStep: preLoad
     * @param:
     * @return:
     * @author: yichen
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

        QueryHelp.setMainSql("hcsaconfig", "regulationQuery", searchParam);

        SearchResult searchResult =  regulationService.searchRegulation(searchParam);

        ParamUtil.setSessionAttr(bpc.request, "regulationAttr", null);
        ParamUtil.setSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, HcsaRegulationConstants.PARAM_RESULT, searchResult);
    }

    /**
     * @AutoStep: doDelete
     * @param:
     * @return:
     * @author: yichen
     */
    public void doDelete(BaseProcessClass bpc){
        String regulationId = ParamUtil.getMaskedString(bpc.request, "regulationId");
        if (!StringUtils.isEmpty(regulationId)){
            boolean deleteSuccess = regulationService.deleteRegulation(regulationId);
            if (!deleteSuccess){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customValidation",
                        "CHKL_ERR025"));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }

    }

    /**
     * @AutoStep: doPage
     * @param:
     * @return:
     * @author: yichen
     */
    public void doPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, request);
        ParamUtil.setSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH, searchParam);
    }

    /**
     * @AutoStep: doSort
     * @param:
     * @return:
     * @author: yichen
     */
    public void doSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
        ParamUtil.setSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH, searchParam);
    }

    /**
     * @AutoStep: preUpload
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpload(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, "switchUploadPage", "regulation");
    }

    /**
     * @AutoStep: preUpload
     * @param:
     * @return:
     * @author: yichen
     */
    public void uploadValidate(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: step2
     * @param:
     * @return:
     * @author: yichen
     */
    public void step2(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String currentAction = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
    }

    /**
     * @AutoStep: preUpload
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitUpload(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");

        Map<String, String> errorMap = validationFile(request, file);
        if (errorMap != null && !errorMap.isEmpty()){
            return;
        }

        File toFile = FileUtils.multipartFileToFile(file);
        try {
            List<HcsaChklSvcRegulationDto> regulationDtoList = FileUtils.transformToJavaBean(toFile, HcsaChklSvcRegulationDto.class);
            regulationDtoList.forEach(i -> i.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto()));
            List<ErrorMsgContent> errorMsgContentList = regulationService.submitUploadRegulation(regulationDtoList);

            for (ErrorMsgContent errorMsgContent : errorMsgContentList){
                int idx = 0;
                for(String error : errorMsgContent.getErrorMsgList()){
                    String msg = MessageUtil.getMessageDesc(error);
                    errorMsgContent.getErrorMsgList().set(idx++, msg);
                }
            }

            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(request, "messageContent", errorMsgContentList);
            FileUtils.deleteTempFile(toFile);

        }catch (IaisRuntimeException e){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage());
        }catch (BaseRuntimeException e){
            //may be will occur error when upload empty file
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage());
        }
    }

    private Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (file == null){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0005);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        if (FileUtils.outFileSize(file.getSize())){
            errorMap.put(ChecklistConstant.FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        return errorMap;
    }

    /**
     * @AutoStep: preUpdate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpdate(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", "Y");
        String regulationId = ParamUtil.getMaskedString(bpc.request, "regulationId");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(bpc.request, filterParameter);
        SearchResult searchResult =  regulationService.searchRegulation(searchParam);
        if (searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())){
            List<RegulationQueryDto> allRegulation = searchResult.getRows();
            Optional<RegulationQueryDto> regulation = allRegulation.stream().filter(i -> i.getId().equals(regulationId)).findFirst();
            if (regulation.isPresent()){
                RegulationQueryDto queryDto = regulation.get();
                HcsaChklSvcRegulationDto dto = new HcsaChklSvcRegulationDto();
                dto.setId(queryDto.getId());
                dto.setClauseNo(queryDto.getClauseNo());
                dto.setClause(queryDto.getClause());
                dto.setStatus(queryDto.getStatus());
                ParamUtil.setSessionAttr(bpc.request, "regulationAttr", dto);
            }
        }
    }

    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preCreate(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", "N");
        ParamUtil.setSessionAttr(bpc.request, "regulationAttr", null);

    }


    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void doCreateOrUpdate(BaseProcessClass bpc){
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);

        String clauseNo = ParamUtil.getString(bpc.request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String clause = ParamUtil.getString(bpc.request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        HcsaChklSvcRegulationDto dto = (HcsaChklSvcRegulationDto) ParamUtil.getSessionAttr(bpc.request, "regulationAttr");
        if (dto == null){
            dto = new HcsaChklSvcRegulationDto();
        }

        dto.setClauseNo(clauseNo);
        dto.setClause(clause);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ParamUtil.setRequestAttr(bpc.request, "regulationAttr", dto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(dto, action);
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }else {
            IaisApiResult<HcsaChklSvcRegulationDto> apiResult = null;
            switch (action){
                case "update":
                    apiResult =  regulationService.updateRegulation(dto);
                    ParamUtil.setRequestAttr(bpc.request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ERR023", AppConsts.DEFAULT_DATE_FORMAT));
                    break;
                case "create":
                    apiResult =  regulationService.createRegulation(dto);
                    ParamUtil.setRequestAttr(bpc.request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ERR024", AppConsts.DEFAULT_DATE_FORMAT));
                    break;
                default:
            }

            if (apiResult != null && apiResult.isHasError()){
                if (apiResult.getErrorCode().equals(IaisApiStatusCode.DUPLICATION_RECORD.getStatusCode())){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customErrorMessage", "CHKL_ERR007"));
                }
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);

                return;
            }
        }

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    @GetMapping(value = "regulation-result-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));

        File file = null;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        SearchResult searchResult =  regulationService.searchRegulation(searchParam);
        if (searchResult != null){
            List<RegulationQueryDto> regulationResult = searchResult.getRows();

            ExcelWriter excelWriter = new ExcelWriter();
            excelWriter.setFileName("Checklist_Regulations_Upload_Template");
            excelWriter.setClz(RegulationQueryDto.class);
            try {
                file = excelWriter.writerToExcel(regulationResult);
            } catch (Exception  e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        if(file != null){
            try {
                FileUtils.writeFileResponseContent(response, file);
                FileUtils.deleteTempFile(file);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));

    }

}
