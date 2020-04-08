package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaRegulationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(bpc.request, "isUpdate", null);
    }


    /**
     * @AutoStep: doQuery
     * @param:
     * @return:
     * @author: yichen
     */
    public void doQuery(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        RegulationQueryDto regulation = new RegulationQueryDto();

        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);


        regulation.setClauseNo(clause);
        regulation.setClause(desc);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, desc);

        ValidationResult validationResult = WebValidationHelper.validateProperty(regulation, "search");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
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

    }

    /**
     * @AutoStep: doPage
     * @param:
     * @return:
     * @author: yichen
     */
    public void doPage(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: doSort
     * @param:
     * @return:
     * @author: yichen
     */
    public void doSort(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preUpload
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpload(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preUpdate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpdate(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", "Y");
    }

    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preCreate(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, "isUpdate", "N");
    }

    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void doCreateOrUpdate(BaseProcessClass bpc){
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);

        String clauseNo = ParamUtil.getString(bpc.request, "clauseNo");
        String clause = ParamUtil.getString(bpc.request, "clause");
        HcsaChklSvcRegulationDto dto = (HcsaChklSvcRegulationDto) ParamUtil.getSessionAttr(bpc.request, "regulationAttr");
        if (dto == null){
            dto = new HcsaChklSvcRegulationDto();
        }

        dto.setClauseNo(clauseNo);
        dto.setClause(clause);

        ValidationResult validationResult = WebValidationHelper.validateProperty(dto, action);
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }else {
            switch (action){
                case "Update":
                    break;
                case "Create":
                    IaisApiResult<HcsaChklSvcRegulationDto> apiResult =  regulationService.createRegulation(dto);
                    if (apiResult.isHasError()){
                        if (apiResult.getErrorCode().equals(IaisApiStatusCode.DUPLICATION_RECORD.getStatusCode())){
                            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customErrorMessage", "CHKL_ERR007"));
                        }
                        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                    }

                    break;
                default:
            }

        }


    }

    @GetMapping(value = "regulation-result-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));

        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH);
        SearchResult searchResult =  regulationService.searchRegulation(searchParam);
        if (searchResult != null){
            List<RegulationQueryDto> regulationResult = searchResult.getRows();
            file = ExcelWriter.exportExcel(regulationResult, RegulationQueryDto.class, "Checklist_Regulations_Upload_Template");
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
