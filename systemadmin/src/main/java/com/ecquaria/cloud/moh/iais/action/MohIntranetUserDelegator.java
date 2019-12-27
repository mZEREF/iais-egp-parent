package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/24 15:15
 */
@Delegator(value = "IntranetUser")
@Slf4j
public class MohIntranetUserDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(OrgUserQueryDto.class)
            .searchAttr(IntranetUserConstant.SEARCH_PARAM)
            .resultAttr(IntranetUserConstant.SEARCH_RESULT)
            .sortField(IntranetUserConstant.INTRANET_USER_SORT_COLUMN).sortType(SearchParam.ASCENDING).build();



    @Autowired
    private IntranetUserService intranetUserService ;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Create User", "Create User");
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_PARAM,null);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_RESULT,null);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,null);

    }

    public void prepareData (BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = SearchResultHelper.getSearchParam(request,true, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery",searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,IntranetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,IntranetUserConstant.SEARCH_RESULT, searchResult);
            ParamUtil.setRequestAttr(request,"pageCount", searchResult.getPageCount(searchParam.getPageSize()));
        }

    }
    public void prepareSwitch(BaseProcessClass bpc){

    }
    public void prepareCreate(BaseProcessClass bpc) throws ParseException {



    }
    public void doCreate (BaseProcessClass bpc) throws ParseException{
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if(!"save".equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,"true");
            return;
        }
        Map<String,String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto,"save");
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,"errorMsg",WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,"false");
            return;
        }
        intranetUserService.createIntranetUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,"true");
        }


    public void prepareEdit(BaseProcessClass bpc){
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        String id = ParamUtil.getString(bpc.request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
        ParamUtil.setSessionAttr(bpc.request,"intranetUserById",intranetUserById);
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc);
        OrgUserDto OrgUserDto1 = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, "intranetUserById");
        String id = OrgUserDto1.getId();
        orgUserDto.setId(id);
        OrgUserDto orgUserDto1 = intranetUserService.updateOrgUser(orgUserDto);
        ParamUtil.setSessionAttr(bpc.request,"orgUserDto",orgUserDto);
    }


    public void doDelete(BaseProcessClass bpc){

    }



    public void doSearch(BaseProcessClass bpc){

    }

    public void doSorting(BaseProcessClass bpc){

    }

    private OrgUserDto prepareOrgUserDto (BaseProcessClass bpc) throws ParseException {
        OrgUserDto orgUserDto = new OrgUserDto() ;
        String userId = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_STARTDATE);
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);
        String salutation = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        String firstName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
        String organization = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ORGID);
        String division = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);

        orgUserDto.setUserId(userId);
        orgUserDto.setFirstName(firstName);
        orgUserDto.setLastName(lastName);
        orgUserDto.setDisplayName(displayName);
        orgUserDto.setEffectiveFrom(startDate);
        orgUserDto.setEffectiveTo(endDate);
        orgUserDto.setSalutation(salutation);
        orgUserDto.setOrgId("29ABCF6D-770B-EA11-BE7D-000C29F371DC");
        orgUserDto.setDivision(division);
        orgUserDto.setBranchUnit(branch);
        orgUserDto.setEmailAddr(email);
        orgUserDto.setMobileNo(mobileNo);
        orgUserDto.setOfficeTelNo(officeNo);
        orgUserDto.setRemarks(remarks);
        orgUserDto.setStatus("CMSTAT001");
        orgUserDto.setUserDomain("intranet");
        return orgUserDto ;

    }

}
