package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator(value = "masterCodeDelegator")
@Slf4j
public class MasterCodeDelegator {

    private final FilterParameter filterParameter;
    private final MasterCodeService masterCodeService;

    @Autowired
    private MasterCodeDelegator(FilterParameter filterParameter, MasterCodeService masterCodeService){
        this.filterParameter = filterParameter;

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
        filterParameter.setClz(MasterCodeQueryDto.class);
        filterParameter.setSearchAttr(MasterCodeConstants.SEARCH_PARAM);
        filterParameter.setResultAttr(MasterCodeConstants.SEARCH_RESULT);
        filterParameter.setSortField(MasterCodeConstants.MASTERCODE_ID);

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
        String masterCodeId = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);

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
            ParamUtil.setRequestAttr(request,MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
        }
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
        String categoryDescription = ParamUtil.getString(request, "codeCategory");
        String codeDescription = ParamUtil.getString(request, "codeDescription");
        String codeStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "esd")),
                "yyyy-MM-dd");
        String codeEndDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eed")),
                "yyyy-MM-dd");
        String codeStatus = ParamUtil.getString(request, "codeStatus");
        Map<String,Object> masterCodeMap = new HashMap<>();

        if (!StringUtil.isEmpty(categoryDescription)){
            String codeCategory = masterCodeService.findMasterCodeByDescription(categoryDescription);
            masterCodeMap.put("codeCategory",codeCategory);
        }
        if(!StringUtil.isEmpty(codeStatus)){
            masterCodeMap.put("codeStatus",codeStatus);
        }
        if(!StringUtil.isEmpty(codeDescription)){
            masterCodeMap.put("codeDescription",codeDescription);
        }
        if (!StringUtil.isEmpty(codeStartDate)){
            masterCodeMap.put("esd",codeStartDate);
        }
        if (!StringUtil.isEmpty(codeEndDate)){
            masterCodeMap.put("eed",codeEndDate);
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
        CrudHelper.doSorting(searchParam,  bpc.request);
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
        String id = ParamUtil.getString(bpc.request,MasterCodeConstants.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(id)){
            masterCodeService.deleteMasterCodeById(id);
        }
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreate(BaseProcessClass bpc){
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, MasterCodeConstants.CRUD_ACTION_TYPE);
        String value = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);
        if("save".equals(type)){
            if (value != null){
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
            }
        }
    }

    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc){
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_TYPE);
        if("edit".equals(type)){
            MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
            getValueFromPage(masterCodeDto, request);
            ValidationResult validationResult =WebValidationHelper.validateProperty(masterCodeDto, "edit");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.NO);
            }else{
                Map<String,String> successMap = new HashMap<>();
                successMap.put("test","success");
                masterCodeService.saveMasterCode(masterCodeDto);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.SUCCESSMAP,successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
        }
    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request){
        String masterCodeKey = ParamUtil.getString(request,MasterCodeConstants.MASTERCODE_KEY);
        String codeCategory = ParamUtil.getString(request,"code_category");
        String codeValue = ParamUtil.getString(request,MasterCodeConstants.CRUD_VALUE);
        String status = ParamUtil.getString(request,MasterCodeConstants.STATUS);
        masterCodeDto.setMasterCodeKey(masterCodeKey);
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeDto.setCodeValue(codeValue);
        masterCodeDto.setStatus(status);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> statusOptionList = new ArrayList<>();
        statusOptionList.add(new SelectOption("Procing", MasterCodeConstants.PROCING));
        statusOptionList.add(new SelectOption("Pending", MasterCodeConstants.PENDING));

        List<SelectOption> codeDescriptionOptionList = new ArrayList<>();
        codeDescriptionOptionList.add(new SelectOption("import", "import"));
        codeDescriptionOptionList.add(new SelectOption("ordinary", "ordinary"));

        ParamUtil.setRequestAttr(request, "StatusOption", statusOptionList);
        ParamUtil.setRequestAttr(request, "CodeDesOption", codeDescriptionOptionList);
    }

    private void logAboutStart(String methodName){
        log.debug("**** The  "+methodName+"  Start ****");

    }
}
