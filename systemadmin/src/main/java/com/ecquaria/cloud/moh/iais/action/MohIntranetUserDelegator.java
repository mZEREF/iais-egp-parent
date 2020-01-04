package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.pwd.util.PasswordConfig;
import com.ecquaria.cloud.pwd.util.PasswordConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.xhtmlrenderer.util.XMLUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.crypto.Cipher;
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
    private IntranetUserService intranetUserService;
    @Autowired
    private UserClient userClient;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Create User", "Create User");
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_PARAM,null);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_RESULT,null);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,null);
    }

    public void prepareData (BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(bpc.request,"orgUserDto",null);
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

    public void prepareCreate(BaseProcessClass bpc){
        OrgUserDto orgUserDto = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
    }

    public void doCreate (BaseProcessClass bpc) throws ParseException{
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if(!IntranetUserConstant.SAVE_ACTION.equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
            return;
        }
        Map<String,String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto,IntranetUserConstant.SAVE_ACTION);
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
            return;
        }
        intranetUserService.createIntranetUser(orgUserDto);
        saveEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
        }

    public void prepareEdit(BaseProcessClass bpc){
        String id = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_VALUE);
        if(id!=null){
            OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR, intranetUserById);
        }
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if(!IntranetUserConstant.SAVE_ACTION.equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
            return;
        }
        Map<String,String> errorMap = new HashMap<>(34);
        OrgUserDto OrgUserDtoById = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        String id = OrgUserDtoById.getId();
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc);
        orgUserDto.setId(id);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto,IntranetUserConstant.SAVE_ACTION);
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            return;
        }
        OrgUserDto orgUserDtoUpdate = intranetUserService.updateOrgUser(orgUserDto);
        editEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    public void doDelete(BaseProcessClass bpc){
        String crud_action_deactivate = ParamUtil.getString(bpc.request, "crud_action_deactivate");
        String id = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_VALUE);
        if("doDeactivate".equals(crud_action_deactivate)){
            OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_IACTIVE);
            intranetUserService.updateOrgUser(intranetUserById);
            return;
        }else if ("doReactivate".equals(crud_action_deactivate)){
            OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
            intranetUserService.updateOrgUser(intranetUserById);
            return;
        }
        OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
        intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_DELETED);
        intranetUserService.updateOrgUser(intranetUserById);
    }

    public void doSearch(BaseProcessClass bpc){
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String userId = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_USERID);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        Map<String,Object> intranetUserMap = new HashMap<>();
        if(displayName!=null){
            intranetUserMap.put("displayName",displayName);
        }if(userId!=null){
            intranetUserMap.put("userId",userId);
        }if(email!=null){
            intranetUserMap.put("email",email);
        }
        filterParameter.setFilters(intranetUserMap);
    }

    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    public void doPaging (BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        int pageNo = ParamUtil.getInt(bpc.request,SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        filterParameter.setPageNo(pageNo);
    }

    private OrgUserDto prepareOrgUserDto (BaseProcessClass bpc) throws ParseException {
        OrgUserDto orgUserDto = new OrgUserDto() ;
        String userId = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_STARTDATE);
        Date startDate = DateUtil.parseDate(startDateStr, "dd/MM/yyyy");
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
        String salutation = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        String firstName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);
        String status = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);

        orgUserDto.setUserId(userId);
        orgUserDto.setFirstName(firstName);
        orgUserDto.setLastName(lastName);
        orgUserDto.setDisplayName(displayName);
        orgUserDto.setAccountActivateDatetime(startDate);
        orgUserDto.setAccountDeactivateDatetime(endDate);
        orgUserDto.setSalutation(salutation);
        orgUserDto.setOrgId(IntranetUserConstant.ORGANIZATION);
        orgUserDto.setDivision(division);
        orgUserDto.setBranchUnit(branch);
        orgUserDto.setEmail(email);
        orgUserDto.setMobileNo(mobileNo);
        orgUserDto.setOfficeTelNo(officeNo);
        orgUserDto.setRemarks(remarks);
        orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
        orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
        return orgUserDto ;
    }

    private void saveEgpUser(OrgUserDto orgUserDto){
        ClientUser clientUser = MiscUtil.transferEntityDto(orgUserDto, ClientUser.class);
        clientUser.setUserDomain(orgUserDto.getUserDomain());
        clientUser.setId(orgUserDto.getUserId());
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        clientUser.setEmail("caijing@ecquaria.com");
        clientUser.setPassword("$2a$12$9BoiUcgPb67FlFnxktVbVOLpTCaDtF30gSpo8sYEI5UBFuUNkb5HO");
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");
        intranetUserService.saveEgpUser(clientUser);
    }

    private void editEgpUser(OrgUserDto orgUserDto){
        ClientUser clientUser = MiscUtil.transferEntityDto(orgUserDto, ClientUser.class);
        clientUser.setAccountStatus(ClientUser.STATUS_ACTIVE);
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");
        clientUser.setAccountActivateDatetime(new Date());
        /*
        {
              "userDomain": "intranet",
              "accountStatus": "A",
              "accountActivateDatetime": "2019-12-31T07:55:51.090Z",
              "displayName": "test3",
              "email": "35518@qq.com",
              "id": "test3",
              "passwordChallengeAnswer": "s",
              "passwordChallengeQuestion": "s"
         }
         */
        intranetUserService.updateEgpUser(clientUser);
    }
}
