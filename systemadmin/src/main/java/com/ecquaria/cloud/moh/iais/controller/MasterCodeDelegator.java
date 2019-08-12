package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.ValidationUtils;
import sg.gov.moh.iais.common.validation.dto.ValidationResult;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator
@Slf4j
public class MasterCodeDelegator {

    public static final String SEARCH_PARAM                        = "MasterCodeSearchParam";
    public static final String SEARCH_RESULT                       = "MasterCodeSearchResult";
    public static final String MASTERCODE_USER_ACCOUNT_TILE        = "MasterCodeEditTile";
    public static final String MASTERCODE_USER_DTO_ATTR            = "MasterCodeDto";


    @Autowired
    private MasterCodeService masterCodeService;

    public void doStart(BaseProcessClass bpc){
        logAboutStart("doStart");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, MASTERCODE_USER_DTO_ATTR, null);
    }

    public void prepareData(BaseProcessClass bpc){
        logAboutStart("prepareData");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        SearchParam param = getSearchParam(bpc);
        ParamUtil.setRequestAttr(request,"master_code_id", 1);
        SearchResult searchResult = masterCodeService.doQuery(param, "systemAdmin", "masterCodeQuery");
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, param);
        ParamUtil.setRequestAttr(request, SEARCH_RESULT, searchResult);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        HttpServletRequest request = bpc.request;
        SearchParam param = (SearchParam) ParamUtil.getSessionAttr(request, SEARCH_PARAM);
        if(param == null || isNew){
            param = new SearchParam(MasterCodeQuery.class);
            param.setPageSize(10);
            param.setPageNo(1);
            param.setSort("master_code_id", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(request, SEARCH_PARAM, param);
        }
        return param;
    }

    public void prepareSwitch(BaseProcessClass bpc){
        logAboutStart("prepareSwitch");
        String  action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
    }

    public void prepareCreate(BaseProcessClass bpc){
        logAboutStart("prepareCreate");
        HttpServletRequest request = bpc.request;
        String masterCodeId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request, "masterCodeId", masterCodeId);
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request,"statusSelect",statusSelect);
        ParamUtil.setRequestAttr(request, MASTERCODE_USER_ACCOUNT_TILE,"Master Code Create");
    }

    public void prepareEdit(BaseProcessClass bpc){
        logAboutStart("prepareEdit");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        String rowguid = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        MasterCode masterCode = masterCodeService.findMasterCodeByRowguid(rowguid);
        MasterCodeDto masterCodeDto = MiscUtil.transferEntityDto(masterCode, MasterCodeDto.class);
        ParamUtil.setSessionAttr(request, MASTERCODE_USER_DTO_ATTR, masterCodeDto);
        ParamUtil.setRequestAttr(request, MASTERCODE_USER_ACCOUNT_TILE,"MasterCode Edit");
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request, "statusSelect",statusSelect);
    }

    public void doSearch(BaseProcessClass bpc){
        logAboutStart("doSearch");
        HttpServletRequest request = bpc.request;
        SearchParam param = getSearchParam(bpc,true);
        String master_code_key = ParamUtil.getString(request, "master_code_key");
        String code_value = ParamUtil.getString(request,"code_value");
        String[] status = ParamUtil.getStrings(request,"status");
        if(!StringUtil.isEmpty(master_code_key)){
            param.addFilter("master_code_key",master_code_key,true);
        }
        if(!StringUtil.isEmpty(code_value)){
            param.addFilter("code_value",code_value,true);
        }
        if(status != null && status.length>0){
            String statusStr = SqlHelper.constructInCondition("mc.STATUS",status.length);
            param.addParam("status",statusStr);
            for (int i = 0 ; i<status.length; i++ ) {
                param.addFilter("mc.STATUS"+i,status[i]);
            }
        }
    }

    public void doSorting(BaseProcessClass bpc){
        logAboutStart("doSorting");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    public void doPaging(BaseProcessClass bpc){
        logAboutStart("doPaging");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    public void doDelete(BaseProcessClass bpc){
        logAboutStart("doDelete");
        String id = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(id)){
            masterCodeService.deleteMasterCodeById(Long.valueOf(id));
        }
    }

    public void doCreate(BaseProcessClass bpc){
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("save".equals(type)){
            int masterCodeId = ParamUtil.getInt(request,IaisEGPConstant.CRUD_ACTION_VALUE);
            MasterCodeDto masterCodeDto = new MasterCodeDto();
            getValueFromPage(masterCodeDto, request);
            masterCodeDto.setMasterCodeId(masterCodeId);
            ParamUtil.setSessionAttr(request, MASTERCODE_USER_DTO_ATTR, masterCodeDto);
            ValidationResult validationResult = ValidationUtils.validateProperty(masterCodeDto,"create");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }else{
                MasterCode masterCode = MiscUtil.transferEntityDto(masterCodeDto,MasterCode.class);
                masterCodeService.saveMasterCode(masterCode);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            }
        }else{
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }
    }

    public void doEdit(BaseProcessClass bpc){
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("edit".equals(type)){
            MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MASTERCODE_USER_DTO_ATTR);
            getValueFromPage(masterCodeDto, request);
            ValidationResult validationResult =ValidationUtils.validateProperty(masterCodeDto, "edit");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }else{
                Map<String,String> successMap = new HashMap<>();
                successMap.put("test","suceess");
                MasterCode masterCode = MiscUtil.transferEntityDto(masterCodeDto,MasterCode.class);
                masterCodeService.saveMasterCode(masterCode);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.SUCCESSMAP,successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }
    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request){
        String masterCodeKey = ParamUtil.getString(request,"master_code_key");
        String rowguid = ParamUtil.getString(request,"rowguid");
        int codeCategory = ParamUtil.getInt(request,"code_category");
        String codeValue = ParamUtil.getString(request,"code_value");
        int status = ParamUtil.getInt(request,"status");
        masterCodeDto.setMasterCodeKey(masterCodeKey);
        masterCodeDto.setMasterCodeKey(rowguid);
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeDto.setCodeValue(codeValue);
        masterCodeDto.setStatus(status);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> statusOptionList = new ArrayList<>();
        statusOptionList.add(new SelectOption("Procing", "Procing"));
        statusOptionList.add(new SelectOption("Pending", "Pending"));

        List<SelectOption> codeDescriptionOptionList = new ArrayList<>();
        codeDescriptionOptionList.add(new SelectOption("import", "import"));
        codeDescriptionOptionList.add(new SelectOption("ordinary", "ordinary"));

        ParamUtil.setRequestAttr(request, "StatusOption", statusOptionList);
        ParamUtil.setRequestAttr(request, "CodeDesOption", codeDescriptionOptionList);
    }

    private void logAboutStart(String methodName){
        log.debug("**** The"+methodName+"Start ****");

    }
}
