package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator(value = "masterCodeDelegator")
@Slf4j
public class MasterCodeDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(MasterCodeQueryDto.class)
            .searchAttr(MasterCodeConstants.SEARCH_PARAM)
            .resultAttr(MasterCodeConstants.SEARCH_RESULT)
            .sortField(MasterCodeConstants.MASTERCODE_SORT_COLUM).sortType(SearchParam.ASCENDING).build();


    private final MasterCodeService masterCodeService;

    @Autowired
    private MasterCodeDelegator(MasterCodeService masterCodeService){
        this.masterCodeService = masterCodeService;
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        logAboutStart("doStart");
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("MasterCode",
                "MasterCode Function");
        ParamUtil.setSessionAttr(request, MasterCodeConstants.SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, MasterCodeConstants.SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, null);
    }

    /**
     * AutoStep: PrepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        logAboutStart("prepareData");
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = SearchResultHelper.getSearchParam(request,true, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "masterCodeQuery",searchParam);
        SearchResult searchResult = masterCodeService.doQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,MasterCodeConstants.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,MasterCodeConstants.SEARCH_RESULT, searchResult);

            ParamUtil.setRequestAttr(request,"pageCount", searchResult.getPageCount(searchParam.getPageSize()));
        }
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc){
        logAboutStart("prepareSwitch");
        String type = ParamUtil.getString(bpc.request, MasterCodeConstants.CRUD_ACTION_TYPE);
        logAboutStart(type);
    }

    /**
     * AutoStep: PrepareCreate
     *
     * @param bpc
     * @throws
     */
    public void prepareCreate(BaseProcessClass bpc){
        logAboutStart("prepareCreate");
        HttpServletRequest request = bpc.request;
    }



    /**
     * AutoStep: doSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doSearch");
        HttpServletRequest request = bpc.request;
        String categoryDescription = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY);
        String codeDescription = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION);
        String codeStatus = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS);
        String codeStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM)),
                "yyyy-MM-dd");
        String codeEndDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO)),
                "yyyy-MM-dd");
        Map<String,Object> masterCodeMap = new HashMap<>();

        if (!StringUtil.isEmpty(categoryDescription)){
            String codeCategory = masterCodeService.findCodeCategoryByDescription(categoryDescription);
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_CATEGORY,codeCategory);
        }
        if(!StringUtil.isEmpty(codeStatus)){
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_STATUS,codeStatus);
        }
        if(!StringUtil.isEmpty(codeDescription)){
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_DESCRIPTION,codeDescription);
        }
        if (!StringUtil.isEmpty(codeStartDate)){
            masterCodeMap.put( MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM,codeStartDate);
        }
        if (!StringUtil.isEmpty(codeEndDate)){
            masterCodeMap.put( MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO,codeEndDate);
        }
        filterParameter.setFilters(masterCodeMap);
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     * @throws
     */
    public void doSorting(BaseProcessClass bpc){
        logAboutStart("doSorting");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     * @throws
     */
    public void doPaging(BaseProcessClass bpc){
        logAboutStart("doPaging");
        HttpServletRequest request = bpc.request;
        int pageNo = ParamUtil.getInt(bpc.request,MasterCodeConstants.CRUD_ACTION_VALUE);
        filterParameter.setPageNo(pageNo);
    }

    /**
     * AutoStep: doDelete
     *
     * @param bpc
     * @throws
     */
    public void doDelete(BaseProcessClass bpc){
        logAboutStart("doDelete");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, MasterCodeConstants.CRUD_ACTION_TYPE);
        String action = ParamUtil.getString(request, "crud_action_deactivate");

        if ("doDelete".equals(type)){
            String masterCodeId = ParamUtil.getString(bpc.request,MasterCodeConstants.CRUD_ACTION_VALUE);
            if("doDeactivate".equals(action)){
                MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
                String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
                masterCodeDto.setCodeCategory(codeCategory);
                masterCodeDto.setStatus("CMSTAT003");
                masterCodeService.updateMasterCode(masterCodeDto);
            }else{
                masterCodeService.deleteMasterCodeById(masterCodeId);
            }

        }
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreate(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, MasterCodeConstants.CRUD_ACTION_TYPE);
        if (!MasterCodeConstants.SAVE_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        getValueFromPage(masterCodeDto,request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto,MasterCodeConstants.SAVE_ACTION);
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.NO);
            return;
        }
        masterCodeService.saveMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.YES);

    }


    /**
     * AutoStep: prepareEdit
     *
     * @param bpc
     * @throws
     */
    public void prepareEdit(BaseProcessClass bpc){
        logAboutStart("prepareEdit");
        HttpServletRequest request = bpc.request;
        String masterCodeId = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);
        if (!masterCodeId.isEmpty()){
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            ParamUtil.setSessionAttr(request,MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
        }
    }

    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_TYPE);
        if (!MasterCodeConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
        getValueFromPage(masterCodeDto, request);
        ValidationResult validationResult =WebValidationHelper.validateProperty(masterCodeDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()) {
            logAboutStart("Edit validation");
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_CATEGORY));
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeService.updateMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);

    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setMasterCodeKey(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_KEY));
        masterCodeDto.setCodeValue(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_VALUE));
        masterCodeDto.setCodeCategory(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_CATEGORY));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_DESCRIPTION));
        masterCodeDto.setFilterValue(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_FILTER_VALUE));
        masterCodeDto.setStatus(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_STATUS));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_SEQUENCE))? null : ParamUtil.getInt(request,MasterCodeConstants.MASTER_CODE_SEQUENCE));
        masterCodeDto.setVersion(StringUtil.isEmpty(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_VERSION)) ? null : ParamUtil.getInt(request,MasterCodeConstants.MASTER_CODE_VERSION));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO)));
        masterCodeDto.setIsEditable(0);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void logAboutStart(String methodName){
        log.debug("**** The  "+methodName+"  Start ****");

    }
}
