package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaRegulationConstants;
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
import com.ecquaria.cloud.moh.iais.helper.ChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import ecq.commons.exception.BaseRuntimeException;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

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
            .sortFieldToMap("status", SearchParam.ASCENDING)
            .sortFieldToMap("id", SearchParam.ASCENDING).build();

    private static final String REGULATION_CHECK_BOX_REDISPLAY = "regulation_item_CheckboxReDisplay";

    /**
     * @AutoStep: startStep
     * @param:
     * @return:
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CHECKLIST_MANAGEMENT,  AuditTrailConsts.FUNCTION_CHECKLIST_REGULATION);
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", null);

        ParamUtil.setSessionAttr(bpc.request, REGULATION_CHECK_BOX_REDISPLAY, null);
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

        ValidationResult validationResult = WebValidationHelper.validateProperty(regulation, "search");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if(StringUtil.isNotEmpty(clause)){
            searchParam.addFilter("clauseNo", clause, true);
        }

        if(StringUtil.isNotEmpty(desc)){
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
        if (StringUtils.isNotEmpty(regulationId)){
            boolean deleteSuccess = regulationService.deleteRegulation(regulationId);
            if (deleteSuccess){
                AuditTrailHelper.callSaveAuditTrailByOperation(AuditTrailConsts.OPERATION_INACTIVE_RECORD);
            }else {
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
        ParamUtil.setSessionAttr(bpc.request, "switchUploadPage", "Checklist Regulation Upload");
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
        MultipartFile mulReqFile = mulReq.getFile("selectedFile");
        boolean fileHasError = ChecklistHelper.validateFile(request, mulReqFile);
        if (fileHasError){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        File file = FileUtils.multipartFileToFile(mulReqFile, request.getSession().getId());
        try {
            List<HcsaChklSvcRegulationDto> reglList = FileUtils.transformToJavaBean(file, HcsaChklSvcRegulationDto.class);
            reglList.forEach(i -> i.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto()));
            List<ErrorMsgContent> errorMsgContentList = regulationService.submitUploadRegulation(reglList);
            FileUtils.deleteTempFile(file);
            ChecklistHelper.replaceErrorMsgContentMasterCode(errorMsgContentList);
            ParamUtil.setRequestAttr(request, "messageContent", errorMsgContentList);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }catch (IaisRuntimeException | BaseRuntimeException e){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage());
        }//may be will occur error when upload empty file

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
        if (searchResult != null && IaisCommonUtils.isNotEmpty(searchResult.getRows())){
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
        String clauseNo = ParamUtil.getString(bpc.request, "edit_regulationClauseNo");
        String clause = ParamUtil.getString(bpc.request, "edit_regulationClause");
        HcsaChklSvcRegulationDto regulation = (HcsaChklSvcRegulationDto) ParamUtil.getSessionAttr(bpc.request, "regulationAttr");
        regulation = Optional.ofNullable(regulation).orElseGet(() -> new HcsaChklSvcRegulationDto());
        regulation.setClauseNo(clauseNo);
        regulation.setClause(clause);
        regulation.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ParamUtil.setRequestAttr(bpc.request, "regulationAttr", regulation);
        ValidationResult validationResult = WebValidationHelper.validateProperty(regulation, action);
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }else {
            IaisApiResult<HcsaChklSvcRegulationDto> apiResult = null;
            switch (action){
                case "update":
                    apiResult =  regulationService.updateRegulation(regulation);
                    ParamUtil.setRequestAttr(bpc.request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ERR023"));
                    break;
                case "create":
                    apiResult =  regulationService.createRegulation(regulation);
                    ParamUtil.setRequestAttr(bpc.request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ERR024"));
                    break;
                default:
            }

            if (apiResult != null && apiResult.isHasError()){
                if (IaisApiStatusCode.DUPLICATE_RECORD.getStatusCode().equals(apiResult.getErrorCode())){
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

        List<RegulationQueryDto> list = IaisCommonUtils.genNewArrayList();
        LinkedHashSet<String> set = (LinkedHashSet<String>) ParamUtil.getSessionAttr(request, REGULATION_CHECK_BOX_REDISPLAY);
        if (Optional.ofNullable(set).isPresent()){
            SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            String idStr = SqlHelper.constructInCondition("ID", set.size());
            searchParam.addParam("regulationId", idStr);
            int indx = 0;
            for (String checked : set){
                searchParam.addFilter("ID"+indx, checked);
                indx++;
            }

            QueryHelp.setMainSql("hcsaconfig", "regulationQuery", searchParam);
            SearchResult searchResult =  regulationService.searchRegulation(searchParam);

            if (Optional.ofNullable(searchResult).isPresent() && Optional.ofNullable(searchResult.getRows()).isPresent()){
                list = searchResult.getRows();
            }
        }

        File file = null;
        try {
            file = ExcelWriter.writerToExcel(list, RegulationQueryDto.class, "Checklist_Regulations_Upload_Template");
            FileUtils.writeFileResponseContent(response, file);
        } catch (Exception  e) {
            log.error("=======>fileHandler error >>>>>", e);
        }finally {
            if (file != null){
                FileUtils.deleteTempFile(file);
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

}
