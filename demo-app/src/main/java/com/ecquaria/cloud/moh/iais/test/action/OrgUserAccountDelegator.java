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

package com.ecquaria.cloud.moh.iais.test.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import com.ecquaria.cloud.moh.iais.test.dto.OrgUserAccountDto;
import com.ecquaria.cloud.moh.iais.test.entity.DemoQuery;
import com.ecquaria.cloud.moh.iais.test.entity.OrgUserAccount;
import com.ecquaria.cloud.moh.iais.test.service.OrgUserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.ValidationUtils;
import sg.gov.moh.iais.common.validation.dto.ValidationResult;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OrgUserAccountController
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Delegator
@Slf4j
public class OrgUserAccountDelegator {

    private static final String SEARCHPARAMSESSION = "OrgUserAccountDelegator_searchParam";
    private static final String SEARCHPARAM = "searchParam";
    private static final String SEARCHRESULT = "searchResult";
    private static final String ORGUSERACCOUNT = "orgUserAccount";
    private static final String ORGUSERACCOUNTTILE = "orgUserAccountEditTile";
    @Autowired
    private OrgUserAccountService orgUserAccountService;

    @Autowired
    private QueryDao<DemoQuery> demoQueryDao;

    public void doStart(BaseProcessClass bpc){
        log.debug("The doStart start ...");
        bpc.request.getSession().setAttribute(SEARCHPARAMSESSION,null);
        bpc.request.getSession().setAttribute(SEARCHRESULT,null);
        log.debug("The doStart end ...");
    }
    public  void prepareData(BaseProcessClass bpc){
        log.debug("The prepareData start ...");
        SearchParam param = getSearchParam(bpc);
        param.addFilter("ORGANIZATION_ID","0",true);
        bpc.request.setAttribute("ORGANIZATION_ID",0);
        SearchResult searchResult = demoQueryDao.doQuery(param, "demo", "searchDemo");
        bpc.request.setAttribute(SEARCHPARAM,param);
        bpc.request.setAttribute(SEARCHRESULT,searchResult);
        log.debug("The prepareData end ...");
    }
    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }
    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam param = (SearchParam)bpc.request.getSession().getAttribute(SEARCHPARAMSESSION);
        if(param == null || isNew){
            param = new SearchParam(DemoQuery.class);
            param.setPageSize(10);
            param.setPageNo(1);
            param.setSort("user_id", SearchParam.ASCENDING);
            bpc.request.getSession().setAttribute(SEARCHPARAMSESSION,param);
        }
        return param;
    }
    public  void prepareSwitch(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
          String  action = bpc.request.getParameter("crud_action_type");
        log.debug("*******************action-->:"+action);
        log.debug("The prepareSwitch end ...");
    }
    public  void doSearch(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        SearchParam param = getSearchParam(bpc,true);
        String nric_no = bpc.request.getParameter("nric_no");
        String uen_no = bpc.request.getParameter("uen_no");
        String[] status = bpc.request.getParameterValues("status");
        if(!StringUtil.isEmpty(nric_no)){
            param.addFilter("nric_no",nric_no,true);
        }
        if(!StringUtil.isEmpty(uen_no)){
            param.addFilter("uen_no",uen_no,true);
        }
        if(status != null && status.length>0){
            String statusStr = SqlHelper.constructInCondition("account.STATUS",status.length);
            param.addParam("status",statusStr);
            for (int i = 0 ; i<status.length; i++ ) {
                param.addFilter("account.STATUS"+i,status[i]);
            }
        }
        log.debug("The prepareSwitch end ...");
    }
    public  void doSorting(BaseProcessClass bpc){
        log.debug("The doSorting start ...");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam,  bpc.request);
        log.debug("The doSorting end ...");
    }
    public  void doPaging(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
        log.debug("The prepareSwitch end ...");
    }
    public  void doDelete(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        String id = bpc.request.getParameter("crud_action_value");
        if(!StringUtil.isEmpty(id)){
            orgUserAccountService.deleteOrgUserAccountsById(id);
        }
        log.debug("The prepareSwitch end ...");
    }

    public void prepareCreateData(BaseProcessClass bpc){
        log.debug("The doCreateStart start ...");
        String orgId = bpc.request.getParameter("crud_action_value");
        bpc.request.setAttribute("orgId",orgId);
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        bpc.request.setAttribute("statusSelect",statusSelect);
        bpc.request.setAttribute(ORGUSERACCOUNTTILE,"Org Account Create");
        log.debug("******************-->:"+orgId);
        log.debug("The doCreateStart end ...");
    }
    public void doCreate(BaseProcessClass bpc){
        log.debug("The doCreateStart start ...");
        String type = bpc.request.getParameter("crud_action_type");
        if("save".equals(type)){
            String orgId = bpc.request.getParameter("crud_action_value");
            OrgUserAccountDto accountDto = MiscUtil.generateDtoFromParam(bpc.request,OrgUserAccountDto.class);
            accountDto.setOrgId(orgId);
            ValidationResult validationResult =ValidationUtils.validateEntity(accountDto);
            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                bpc.request.setAttribute("errorMap",errorMap);
                bpc.request.setAttribute("isValid","N");
            }else{
                OrgUserAccount orgUserAccount = MiscUtil.transferEntityDto(accountDto,OrgUserAccount.class);
                orgUserAccountService.saveOrgUserAccounts(orgUserAccount);
                bpc.request.setAttribute("isValid","Y");
            }
        }else{
            bpc.request.setAttribute("isValid","Y");
        }

        log.debug("The doCreateStart end ...");
    }
    public void prepareEdit(BaseProcessClass bpc){
        log.debug("The prepareEdit start ...");
        String rowguid = bpc.request.getParameter("crud_action_value");
        OrgUserAccount orgUserAccount = orgUserAccountService.getOrgUserAccountByRowguId(rowguid);
        bpc.request.setAttribute(ORGUSERACCOUNT,orgUserAccount);
        bpc.request.setAttribute(ORGUSERACCOUNTTILE,"Org Account Edit");
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending","Pending");
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing","Procing");
        statusSelect.add(sp2);
        bpc.request.setAttribute("statusSelect",statusSelect);
        log.debug("The prepareEdit end ...");
    }
    public void doEdit(BaseProcessClass bpc){
        log.debug("The doEdit start ...");
        String type = bpc.request.getParameter("crud_action_type");
        if("edit".equals(type)){
            String rowguid = bpc.request.getParameter("crud_action_value");
            OrgUserAccount orgUserAccount = orgUserAccountService.getOrgUserAccountByRowguId(rowguid);
            String name = bpc.request.getParameter("name");
            String nircNo = bpc.request.getParameter("nircNo");
            String corpPassId = bpc.request.getParameter("corpPassId");
            String status = bpc.request.getParameter("status");
            orgUserAccount.setName(name);
            orgUserAccount.setNircNo(nircNo);
            orgUserAccount.setCorpPassId(corpPassId);
            orgUserAccount.setStatus(status);
            OrgUserAccountDto accountDto =  MiscUtil.transferEntityDto(orgUserAccount,OrgUserAccountDto.class);
            ValidationResult validationResult =ValidationUtils.validateEntity(accountDto);
            if (validationResult.isHasErrors()){
                log.error("****************Error");
                Map<String,String> errorMap = validationResult.retrieveAll();
                bpc.request.setAttribute("errorMap",errorMap);
                bpc.request.setAttribute("isValid","N");
            }else{
                orgUserAccountService.saveOrgUserAccounts(orgUserAccount);
                bpc.request.setAttribute("isValid","Y");
            }
        }else{
            bpc.request.setAttribute("isValid","Y");
        }
        log.debug("The doEdit end ...");
    }
}
