package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntrenetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.IntranetUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author weilu
 * @date 2019/12/24 15:15
 */
@Delegator(value = "createUser")
@Slf4j
public class MohIntranetUserDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(OrgUserQueryDto.class)
            .searchAttr(IntrenetUserConstant.SEARCH_PARAM)
            .resultAttr(IntrenetUserConstant.SEARCH_RESULT)
            .sortField(IntrenetUserConstant.INTRANET_USER_SORT_COLUMN).sortType(SearchParam.ASCENDING).build();



    @Autowired
    private IntranetUserService intranetUserService ;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Create User", "Create User");
        ParamUtil.setSessionAttr(bpc.request,IntrenetUserConstant.SEARCH_PARAM,null);
        ParamUtil.setSessionAttr(bpc.request,IntrenetUserConstant.SEARCH_RESULT,null);
        ParamUtil.setSessionAttr(bpc.request,IntrenetUserConstant.INTRANET_USER_DTO_ATTR,null);

    }

    public void prepareData (BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = SearchResultHelper.getSearchParam(request,true, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery",searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,IntrenetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,IntrenetUserConstant.SEARCH_RESULT, searchResult);
            ParamUtil.setRequestAttr(request,"pageCount", searchResult.getPageCount(searchParam.getPageSize()));
        }

    }
    public void prepareSwitch(BaseProcessClass bpc){

    }
    public void prepareCreate(BaseProcessClass bpc) throws ParseException {

    }
    public void doCreate (BaseProcessClass bpc) throws ParseException{
        String actionType = ParamUtil.getString(bpc.request, IntrenetUserConstant.CRUD_ACTION_TYPE);

        if(!"save".equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntrenetUserConstant.ISVALID,"true");
        }
        IntranetUserDto intranetUserDto = prepareOrgUserDto(bpc);
        intranetUserService.createIntranetUser(intranetUserDto);
        ParamUtil.setRequestAttr(bpc.request,IntrenetUserConstant.ISVALID,"true");
        }


    public void prepareEdit(BaseProcessClass bpc){

    }

    public void doEdit(BaseProcessClass bpc){

    }


    public void doDelete(BaseProcessClass bpc){

    }



    public void doSearch(BaseProcessClass bpc){

    }

    public void doSorting(BaseProcessClass bpc){

    }

    private IntranetUserDto prepareOrgUserDto (BaseProcessClass bpc) throws ParseException {
        IntranetUserDto intranetUserDto = new IntranetUserDto() ;
        String userId = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_STARTDATE);
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_ENDDATE);
        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);
        String salutation = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_SALUTATION);
        String firstName = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_LASTNAME);
        String organization = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_ORGID);
        String division = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntrenetUserConstant.INTRANET_REMARKS);

        intranetUserDto.setUserId(userId);
        intranetUserDto.setDisplayName(displayName);
        intranetUserDto.setEffectiveFrom(startDate);
        intranetUserDto.setEffectiveTo(endDate);
        intranetUserDto.setSalutation(salutation);
        intranetUserDto.setOrgId("29ABCF6D-770B-EA11-BE7D-000C29F371DC");
        intranetUserDto.setDivision(division);
        intranetUserDto.setBranchUnit(branch);
        intranetUserDto.setEmailAddr(email);
        intranetUserDto.setMobileNo(mobileNo);
        intranetUserDto.setOfficeTelNo(officeNo);
        intranetUserDto.setRemarks(remarks);
        intranetUserDto.setStatus("CMSTAT001");
        intranetUserDto.setUserDomain("intranet");
        return intranetUserDto ;

    }

}
