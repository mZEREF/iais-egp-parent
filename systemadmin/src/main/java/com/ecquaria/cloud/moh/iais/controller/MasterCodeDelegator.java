package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
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
        log.debug("The doStart start ...");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, MASTERCODE_USER_DTO_ATTR, null);
        log.debug("The doStart end ...");
    }

    public void prepareData(BaseProcessClass bpc){
        log.debug("The prepareData start ...");
        HttpServletRequest request = bpc.request;
        SearchParam param = getSearchParam(bpc);
        ParamUtil.setRequestAttr(request,"master_code_id", 1);
        SearchResult searchResult = masterCodeService.doQuery(param, "systemAdmin", "masterCodeQuery");
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, param);
        ParamUtil.setRequestAttr(request, SEARCH_RESULT, searchResult);
        log.debug("The prepareData end ...");
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
        log.debug("The prepareSwitch start ...");
        String  action = ParamUtil.getString(bpc.request,"crud_action_type");
        log.debug("**** The Next Step is -->:"+action);
        log.debug("The prepareSwitch end ...");
    }

    public void prepareCreate(BaseProcessClass bpc){
        log.debug("The prepareCreateData start ...");
        HttpServletRequest request = bpc.request;
        String masterCodeId = ParamUtil.getString(request,"crud_action_value");
        log.info("The crud_action_value is ---->"+masterCodeId);
        ParamUtil.setRequestAttr(request, "masterCodeId", masterCodeId);
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request,"statusSelect",statusSelect);
        ParamUtil.setRequestAttr(request, MASTERCODE_USER_ACCOUNT_TILE,"Master Code Create");
        log.debug("The prepareCreateData end ...");
    }

    public void prepareEdit(BaseProcessClass bpc){
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String rowguid = ParamUtil.getString(request,"crud_action_value");
        log.info("The crud_action_value is ---->"+rowguid);
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
        log.debug("The prepareEdit end ...");
    }

    public void doSearch(BaseProcessClass bpc){
        log.debug("The doSearch start ...");
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
        log.debug("The doSearch end ...");

    }

    public void doSorting(BaseProcessClass bpc){
        log.debug("The doSorting start ...");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam,  bpc.request);
        log.debug("The doSorting end ...");
    }

    public void doPaging(BaseProcessClass bpc){
        log.debug("The doPaging start ...");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
        log.debug("The doPaging end ...");
    }

    public void doDelete(BaseProcessClass bpc){
        log.debug("The doDelete start ...");
        String id = ParamUtil.getString(bpc.request,"crud_action_value");
        if(!StringUtil.isEmpty(id)){
            masterCodeService.deleteMasterCodeById(Long.valueOf(id));
        }
        log.debug("The doDelete end ...");
    }

    public void doCreate(BaseProcessClass bpc){
        log.debug("The doCreate start ...");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, "crud_action_type");
        if("save".equals(type)){
            int masterCodeId = ParamUtil.getInt(request,"crud_action_value");
            MasterCodeDto masterCodeDto = new MasterCodeDto();
            getValueFromPage(masterCodeDto, request);
            masterCodeDto.setMasterCodeId(masterCodeId);
            ParamUtil.setSessionAttr(request, MASTERCODE_USER_DTO_ATTR, masterCodeDto);
            ValidationResult validationResult = ValidationUtils.validateProperty(masterCodeDto,"create");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,"errorMap",errorMap);
                ParamUtil.setRequestAttr(request,"isValid","N");
            }else{
                MasterCode masterCode = MiscUtil.transferEntityDto(masterCodeDto,MasterCode.class);
                masterCodeService.saveMasterCode(masterCode);
                ParamUtil.setRequestAttr(request,"isValid","Y");
            }
        }else{
            ParamUtil.setRequestAttr(request,"isValid","Y");
        }

        log.debug("The doCreate end ...");
    }

    public void doEdit(BaseProcessClass bpc){
        log.debug("The doEdit start ...");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,"crud_action_type");
        if("edit".equals(type)){
            MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MASTERCODE_USER_DTO_ATTR);
            getValueFromPage(masterCodeDto, request);
            ValidationResult validationResult =ValidationUtils.validateProperty(masterCodeDto, "edit");
            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,"errorMap",errorMap);
                ParamUtil.setRequestAttr(request,"isValid","N");
            }else{
                Map<String,String> successMap = new HashMap<>();
                successMap.put("test","suceess");
                MasterCode masterCode = MiscUtil.transferEntityDto(masterCodeDto,MasterCode.class);
                masterCodeService.saveMasterCode(masterCode);
                ParamUtil.setRequestAttr(request,"isValid","Y");
                ParamUtil.setRequestAttr(request,"successMap",successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,"isValid","Y");
        }
        log.debug("The doEdit end ...");
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
}
