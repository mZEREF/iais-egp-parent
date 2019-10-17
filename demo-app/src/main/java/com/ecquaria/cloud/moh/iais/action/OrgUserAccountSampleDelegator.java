/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.DemoQueryDto;
import com.ecquaria.cloud.moh.iais.dto.OrgUserAccountSampleDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.OrgUserAccountSampleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Process: OrgUserAccount
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Delegator("orgUserAccountDelegator")
@Slf4j
public class OrgUserAccountSampleDelegator {
    public static final String SEARCH_PARAM                        = "demoSearchParam";
    public static final String SEARCH_RESULT                       = "demoSearchResult";
    public static final String ORG_USER_ACCOUNT_TILE               = "orgUserAccountEditTile";
    public static final String ORG_USER_DTO_ATTR                   = "orgUserAccountDto";
    public static final String CRUD_ACTION_TYPE                    = "crud_action_type";
    public static final String STATUS                              = "status";
    public static final String CRUD_ACTION_VALUE                   = "crud_action_value";
    public static final String ISVALID                             = "isValid";
    public static final String ERRORMAP                            = "errorMap";

    @Autowired
    private OrgUserAccountSampleService orgUserAccountService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doStart start ..."));
        AuditTrailHelper.auditFunction("iais-demo", "org user management");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, null);
        log.debug(StringUtil.changeForLog("The doStart end ..."));
    }
    /**
     * AutoStep: PrepareData
     *
     * @param bpc
     * @throws
     */
    public  void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The prepareData start ..."));
        HttpServletRequest request = bpc.request;
        SearchParam param = getSearchParam(bpc);
        param.addFilter("ORGANIZATION_ID","1",true);
        ParamUtil.setRequestAttr(request,"ORGANIZATION_ID", 1);
        QueryHelp.setMainSql("demo", "searchDemo",param);
        SearchResult searchResult = orgUserAccountService.doQuery(param);
        ParamUtil.setSessionAttr(request, SEARCH_PARAM, param);
        ParamUtil.setRequestAttr(request, SEARCH_RESULT, searchResult);
        log.debug(StringUtil.changeForLog("The prepareData end ..."));
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public  void prepareSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The prepareSwitch start ..."));
        String  action = ParamUtil.getString(bpc.request,CRUD_ACTION_TYPE);
        log.debug(StringUtil.changeForLog("*******************action-->:"+action));
        log.debug(StringUtil.changeForLog("The prepareSwitch end ..."));
    }
    /**
     * AutoStep: doSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doSearch start ..."));
        HttpServletRequest request = bpc.request;
        SearchParam param = getSearchParam(bpc,true);
        String nricNo = ParamUtil.getString(request, "nric_no");
        String uenNo = ParamUtil.getString(request,"uen_no");
        String[] status = ParamUtil.getStrings(request,STATUS);
        if(!StringUtil.isEmpty(nricNo)){
            param.addFilter("nric_no",nricNo,true);
        }
        if(!StringUtil.isEmpty(uenNo)){
            param.addFilter("uen_no",uenNo,true);
        }
        if(status != null && status.length>0){
            String statusStr = SqlHelper.constructInCondition("account.STATUS",status.length);
            param.addParam(STATUS,statusStr);
            for (int i = 0 ; i<status.length; i++ ) {
                param.addFilter("account.STATUS"+i,status[i]);
            }
        }
        log.debug(StringUtil.changeForLog("The doSearch end ..."));
    }
    /**
     * AutoStep: doSorting
     *
     * @param bpc
     * @throws
     */
    public void doSorting(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doSorting start ..."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam,  bpc.request);
        log.debug(StringUtil.changeForLog("The doSorting end ..."));
    }
    /**
     * AutoStep: doPaging
     *
     * @param bpc
     * @throws
     */
    public void doPaging(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doPaging start ..."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
        log.debug(StringUtil.changeForLog("The doPaging end ..."));
    }
    /**
     * AutoStep: doDelete
     *
     * @param bpc
     * @throws
     */
    public void doDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doDelete start ..."));
        String id = ParamUtil.getString(bpc.request,CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(id)){
            orgUserAccountService.deleteOrgUserAccountsById(id);
        }
        log.debug(StringUtil.changeForLog("The doDelete end ..."));
    }
    /**
     * AutoStep: PrepareCreate
     *
     * @param bpc
     * @throws
     */
    public void prepareCreateData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The prepareCreateData start ..."));
        HttpServletRequest request = bpc.request;
        String orgId = ParamUtil.getString(request,CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request, "orgId", orgId);
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request,"statusSelect",statusSelect);
        ParamUtil.setRequestAttr(request, ORG_USER_ACCOUNT_TILE,"Org Account Create");
        Map<String,String> errorMap = (Map<String, String>) ParamUtil.getRequestAttr(request,ERRORMAP);
        if(MapUtils.isEmpty(errorMap)){
            ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, null);
        }
        log.debug(StringUtil.changeForLog("******************-->:"+orgId));
        log.debug(StringUtil.changeForLog("The prepareCreateData end ..."));
    }
    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doCreate start ..."));
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, CRUD_ACTION_TYPE);
        if("save".equals(type)){
            String orgId = ParamUtil.getString(request,CRUD_ACTION_VALUE);
            OrgUserAccountSampleDto accountDto = new OrgUserAccountSampleDto();
            getValueFromPage(accountDto, request);
            accountDto.setOrgId(orgId);
            ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, accountDto);
            ValidationResult validationResult =WebValidationHelper.validateProperty(accountDto,"create");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,ISVALID,"N");
            }else{
                orgUserAccountService.saveOrgUserAccounts(accountDto);
                ParamUtil.setRequestAttr(request,ISVALID,"Y");
            }
        }else{
            ParamUtil.setRequestAttr(request,ISVALID,"Y");
        }

        log.debug(StringUtil.changeForLog("The doCreate end ..."));
    }
    /**
     * AutoStep: PrepareEdit
     *
     * @param bpc
     * @throws
     */
    public void prepareEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The prepareEdit start ..."));
        HttpServletRequest request = bpc.request;
        String rowguid = ParamUtil.getMaskedString(request,CRUD_ACTION_VALUE);
        OrgUserAccountSampleDto dto;
        if(StringUtil.isEmpty(rowguid)){
            dto = (OrgUserAccountSampleDto)ParamUtil.getSessionAttr(request,ORG_USER_DTO_ATTR);
        }else{
            dto = orgUserAccountService.getOrgUserAccountByRowguId(rowguid);
            dto.setEditFlag(true);
            dto.setOldNricNo(dto.getNircNo());
        }
        ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, dto);
        ParamUtil.setRequestAttr(request, ORG_USER_ACCOUNT_TILE,"Org Account Edit");
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request, "statusSelect",statusSelect);
        log.debug(StringUtil.changeForLog("The prepareEdit end ..."));
    }
    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The doEdit start ..."));
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,CRUD_ACTION_TYPE);
        if("edit".equals(type)){
            OrgUserAccountSampleDto accountDto =
                    (OrgUserAccountSampleDto) ParamUtil.getSessionAttr(request, ORG_USER_DTO_ATTR);
            getValueFromPage(accountDto, request);
            ValidationResult validationResult =WebValidationHelper.validateProperty(accountDto, "edit");
            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,ISVALID,"N");
            }else{
                Map<String,String> successMap = new HashMap<>();
                successMap.put("test","suceess");
                orgUserAccountService.saveOrgUserAccounts(accountDto);
                ParamUtil.setRequestAttr(request,ISVALID,"Y");
                ParamUtil.setRequestAttr(request,"successMap",successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,ISVALID,"Y");
        }
        log.debug(StringUtil.changeForLog("The doEdit end ..."));
    }

    /******************************************************************************************************************
     Private methods
     ******************************************************************************************************************/
    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        HttpServletRequest request = bpc.request;
        SearchParam param = (SearchParam) ParamUtil.getSessionAttr(request, SEARCH_PARAM);
        if(param == null || isNew){
            param = new SearchParam(DemoQueryDto.class.getName());
            param.setPageSize(10);
            param.setPageNo(1);
            param.setSort("user_id", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(request, SEARCH_PARAM, param);
        }
        return param;
    }

    private void getValueFromPage(OrgUserAccountSampleDto accountDto, HttpServletRequest request) {
        String name = ParamUtil.getString(request,"name");
        String nircNo = ParamUtil.getString(request,"nircNo");
        String corpPassId = ParamUtil.getString(request,"corpPassId");
        String status = ParamUtil.getString(request,STATUS);
        accountDto.setName(name);
        accountDto.setNircNo(nircNo);
        accountDto.setCorpPassId(corpPassId);
        accountDto.setStatus(status);
    }
}
