package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        prepareOption(bpc);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,null);
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
        List<SelectOption> salutation = new ArrayList<>();
        SelectOption so1 = new SelectOption("Mr","Mr");
        SelectOption so2 = new SelectOption("Ms","Ms");
        SelectOption so3 = new SelectOption("Mrs","Mrs");
        SelectOption so4 = new SelectOption("Mdm","Mdm");
        SelectOption so5 = new SelectOption("Dr","Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        ParamUtil.setSessionAttr(bpc.request,"salutation",(Serializable)salutation);
        OrgUserDto orgUserDto = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
    }

    public void doCreate (BaseProcessClass bpc){
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if(!IntranetUserConstant.SAVE_ACTION.equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
            return;
        }
        Map<String,String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc);
        errorMap = doValidatePo(orgUserDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto,"save");
        if (!errorMap.isEmpty()||validationResult.isHasErrors()) {
            Map<String, String> validationResultMap = validationResult.retrieveAll();
            errorMap = doValidatePo(orgUserDto);
            errorMap.putAll(validationResultMap);
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
    public void doEdit(BaseProcessClass bpc){
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if(!IntranetUserConstant.SAVE_ACTION.equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
            return;
        }
        Map<String,String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareEditOrgUserDto(bpc);
        //errorMap = doValidatePo(orgUserDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto,"edit");
        if (!errorMap.isEmpty()||validationResult.isHasErrors()) {
            Map<String, String> validationResultMap = validationResult.retrieveAll();
            errorMap = doValidatePo(orgUserDto);
            errorMap.putAll(validationResultMap);
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
            return;
        }
        intranetUserService.updateOrgUser(orgUserDto);
        editEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    public void doDelete(BaseProcessClass bpc){
        String crud_action_deactivate = ParamUtil.getString(bpc.request, "crud_action_deactivate");
        String id = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_VALUE);
        OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
        String userDomain = intranetUserById.getUserDomain();
        String userId = intranetUserById.getUserId();
        if("doDeactivate".equals(crud_action_deactivate)){
           // OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_IACTIVE);
            intranetUserService.updateOrgUser(intranetUserById);
            return;
        }else if ("doReactivate".equals(crud_action_deactivate)){
            //OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
            intranetUserService.updateOrgUser(intranetUserById);
            return;
        }else if("doTerminate".equals(crud_action_deactivate)){
            //OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_IACTIVE);
            intranetUserService.updateOrgUser(intranetUserById);
            ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
            userByIdentifier.setAccountStatus(ClientUser.STATUS_TERMINATED);
        }

        ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
        if(userByIdentifier.isFirstTimeLoginNo()){
            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_DELETED);
            intranetUserService.updateOrgUser(intranetUserById);
            Boolean aBoolean = deleteEgpUser(userDomain, userId);
            return;
        }


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

    private OrgUserDto prepareOrgUserDto (BaseProcessClass bpc){
        OrgUserDto orgUserDto = new OrgUserDto() ;
        String userId = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_STARTDATE);
        Date startDate = DateUtil.parseDate(startDateStr, "dd/MM/yyyy");
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
        String [] salutation = ParamUtil.getStrings(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        if(!"".equals(salutation[0])){
            orgUserDto.setSalutation(salutation[0]);
        }
        String firstName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
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
        orgUserDto.setAccountActivateDatetime(startDate);
        orgUserDto.setAccountDeactivateDatetime(endDate);
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

    private OrgUserDto prepareEditOrgUserDto (BaseProcessClass bpc){
        OrgUserDto orgUserDto = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
        String [] salutation = ParamUtil.getStrings(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        if(!"".equals(salutation[0])){
            orgUserDto.setSalutation(salutation[0]);
        }
        String [] status = ParamUtil.getStrings(bpc.request,"status");
        if(!"".equals(status[0])){
            orgUserDto.setStatus(status[0]);
        }
        String firstName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_REMARKS);

        orgUserDto.setFirstName(firstName);
        orgUserDto.setLastName(lastName);
        orgUserDto.setDisplayName(displayName);
        orgUserDto.setAccountDeactivateDatetime(endDate);
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
        String userId = orgUserDto.getUserId();
        String userDomain = orgUserDto.getUserDomain();
        ClientUser clientUser = intranetUserService.getUserByIdentifier(userId,userDomain);
        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
        String email = orgUserDto.getEmail();
        String displayName = orgUserDto.getDisplayName();
        String status = orgUserDto.getStatus();
        String firstName = orgUserDto.getFirstName();
        String lastName = orgUserDto.getLastName();
        String division = orgUserDto.getDivision();
        String salutation = orgUserDto.getSalutation();
        String mobileNo = orgUserDto.getMobileNo();

        clientUser.setUserDomain(userDomain);
        clientUser.setId(userId);
        clientUser.setDisplayName(displayName);
        clientUser.setAccountStatus(ClientUser.STATUS_TERMINATED);
        clientUser.setPasswordChallengeQuestion("A");
        clientUser.setPasswordChallengeAnswer("A");
        clientUser.setAccountActivateDatetime(accountActivateDatetime);
        clientUser.setAccountDeactivateDatetime(accountDeactivateDatetime);
        clientUser.setFirstName(firstName);
        clientUser.setLastName(lastName);
        clientUser.setEmail(email);
        clientUser.setMobileNo(mobileNo);
        clientUser.setSalutation(salutation);




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

    private Boolean deleteEgpUser(String userDomian,String userId){
        return intranetUserService.deleteEgpUser(userDomian, userId);
    }


    private Map<String,  String> doValidatePo(OrgUserDto orgUserDto) {
        Map<String,String> errorMap = new HashMap<>(34);
        String userDomain = orgUserDto.getUserDomain();
        String userId = orgUserDto.getUserId();
        String displayName = orgUserDto.getDisplayName();
//        String email = orgUserDto.getEmail();
        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
        //userId
        if(!StringUtil.isEmpty(userId)){
            if(!userId.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(.{1,64})$")){
                errorMap.put("userId","USER_ERR002");
            }else{
                ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
                if(userByIdentifier!=null){
                    String userid = userByIdentifier.getUserIdentifier().getId();
                    if(userId.equals(userid)){
                        errorMap.put("userId","USER_ERR003");
                    }
                }
            }
        }
        //date
        if(accountActivateDatetime!=null) {
            int i = accountActivateDatetime.compareTo(new Date());
            if(i<0){
                errorMap.put("accountActivateDatetime", "USER_ERR007");
            }
        }

        if(accountDeactivateDatetime!=null&&accountActivateDatetime!=null) {
            int i = accountDeactivateDatetime.compareTo(accountActivateDatetime);
            if(i<0){
                errorMap.put("accountDeactivateDatetime", "USER_ERR006");
            }
        }
        return errorMap;
    }

    private void prepareOption(BaseProcessClass bpc){
        List<SelectOption> salutation = new ArrayList<>();
        SelectOption so1 = new SelectOption("Mr","Mr");
        SelectOption so2 = new SelectOption("Ms","Ms");
        SelectOption so3 = new SelectOption("Mrs","Mrs");
        SelectOption so4 = new SelectOption("Mdm","Mdm");
        SelectOption so5 = new SelectOption("Dr","Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        List<SelectOption> statusOption = new ArrayList<>();
        SelectOption status1 = new SelectOption("I","I");
        SelectOption status2 = new SelectOption("A","A");
        SelectOption status3 = new SelectOption("E","E");
        SelectOption status4 = new SelectOption("L","L");
        SelectOption status5 = new SelectOption("S","S");
        SelectOption status6 = new SelectOption("T","T");
        statusOption.add(status1);
        statusOption.add(status2);
        statusOption.add(status3);
        statusOption.add(status4);
        statusOption.add(status5);
        statusOption.add(status6);
        ParamUtil.setSessionAttr(bpc.request,"salutation",(Serializable)salutation);
        ParamUtil.setSessionAttr(bpc.request,"statusOption",(Serializable)statusOption);
        OrgUserDto orgUserDto = (OrgUserDto)ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.INTRANET_USER_DTO_ATTR,orgUserDto);
    }
}
