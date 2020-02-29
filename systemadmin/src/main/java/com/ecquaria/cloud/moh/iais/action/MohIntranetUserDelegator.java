package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserClient;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
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
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.rbac.user.UserIdentifier;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Create User", "Create User");
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, null);
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter,true);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery",searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.SEARCH_RESULT, searchResult);
        }
    }

    public void prepareData(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        prepareOption(bpc);
        List<SelectOption> statusOption = getStatusOption();
        ParamUtil.setSessionAttr(bpc.request, "statusOption", (Serializable) statusOption);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, null);
        Object requestAttr = ParamUtil.getRequestAttr(bpc.request, IntranetUserConstant.SEARCH_RESULT);
        if(requestAttr!=null){
            return;
        }
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter,true);
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery",searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(bpc.request,IntranetUserConstant.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.SEARCH_RESULT, searchResult);
        }
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>user");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_type = request.getParameter("crud_action_type");
        ParamUtil.setSessionAttr(bpc.request,"crud_action_type", crud_action_type);
        return;
    }

    public void prepareCreate(BaseProcessClass bpc) {
        List<SelectOption> salutation = new ArrayList<>();
        SelectOption so1 = new SelectOption("Mr", "Mr");
        SelectOption so2 = new SelectOption("Ms", "Ms");
        SelectOption so3 = new SelectOption("Mrs", "Mrs");
        SelectOption so4 = new SelectOption("Mdm", "Mdm");
        SelectOption so5 = new SelectOption("Dr", "Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        ParamUtil.setSessionAttr(bpc.request, "salutation", (Serializable) salutation);
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
    }

    public void doCreate(BaseProcessClass bpc) {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if (!IntranetUserConstant.SAVE_ACTION.equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        OrgUserDto orgUserDto = prepareOrgUserDto(bpc.request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
            return;
        }
        intranetUserService.createIntranetUser(orgUserDto);
        //saveEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void prepareEdit(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String id = ParamUtil.getString(request, IntranetUserConstant.CRUD_ACTION_VALUE);
        if (id != null) {
            OrgUserDto intranetUserById = intranetUserService.findIntranetUserById(id);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, intranetUserById);
        }
    }

    public void doEdit(BaseProcessClass bpc) {
        String actionType = ParamUtil.getString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if (!IntranetUserConstant.SAVE_ACTION.equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        Map<String, String> errorMap = new HashMap<>(34);
        OrgUserDto orgUserDto = prepareEditOrgUserDto(bpc);
        //errorMap = doValidatePo(orgUserDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(orgUserDto, "edit");
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            Map<String, String> validationResultMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            ParamUtil.setSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
            return;
        }
        intranetUserService.updateOrgUser(orgUserDto);
        //editEgpUser(orgUserDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void doDelete(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String id = ParamUtil.getString(request, IntranetUserConstant.CRUD_ACTION_VALUE);
        intranetUserService.delOrgUser(id);
        return;



//        ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
//        if(userByIdentifier.isFirstTimeLoginNo()){
//            intranetUserById.setStatus(IntranetUserConstant.COMMON_STATUS_DELETED);
//            intranetUserService.updateOrgUser(intranetUserById);
//            deleteEgpUser(userDomain, userId);
//            return;
//        }
    }

    public void doImport(BaseProcessClass bpc) throws IOException, DocumentException {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile sessionFile =(CommonsMultipartFile)request.getFile("xmlFile");
        File file = File.createTempFile("temp","xml");
        File file1 = inputStreamToFile(sessionFile.getInputStream(), file);
        importXML(file1);
        return;
    }

    public void doExport(BaseProcessClass bpc) throws IOException {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String[] ids = ParamUtil.getStrings(request, "userUid");
        if (ids == null || ids.length == 0) {
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
//        byte[] xml = createXML(bpc, ids);
        ParamUtil.setSessionAttr(bpc.request, "ids", ids);
        String s = "https://egp.sit.intra.iais.com/system-admin-web/eservice/INTRANET/IntranetUserDownload";
        String url = RedirectUtil.changeUrlToCsrfGuardUrlUrl(s, request);
        bpc.response.sendRedirect(url);
        return;
    }


    public void doSearch(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String displayName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String userId = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_USERID);
        String email = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_EMAILADDR);
        String status = ParamUtil.getRequestString(request, "accountStatus");
        ParamUtil.setRequestAttr(request, "displayName", displayName);
        ParamUtil.setRequestAttr(request, "userId", userId);
        ParamUtil.setRequestAttr(request, "email", email);
        ParamUtil.setRequestAttr(request, "status", status);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (!StringUtil.isEmpty(userId)) {
            searchParam.addFilter("userId", userId, true);
        }
        if (!StringUtil.isEmpty(displayName)) {
            searchParam.addFilter("displayName", displayName, true);
        }
        if (!StringUtil.isEmpty(email)) {
            searchParam.addFilter("email", email, true);
        }
        if (!StringUtil.isEmpty(status)) {
            searchParam.addFilter("status", status, true);
        }
        QueryHelp.setMainSql("systemAdmin", "IntranetUserQuery", searchParam);
        SearchResult searchResult = intranetUserService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request, IntranetUserConstant.SEARCH_PARAM, searchParam);
        ParamUtil.setRequestAttr(request, IntranetUserConstant.SEARCH_RESULT, searchResult);

    }

    public void doSorting(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchResultHelper.doSort(request, filterParameter);
    }

    public void doPaging(BaseProcessClass bpc) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        SearchResultHelper.doPage(request, filterParameter);
    }


    public void changeStatus(BaseProcessClass bpc) {

    }

    public void saveStatus(BaseProcessClass bpc) {
        String userId = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_USERID);
        String password = ParamUtil.getRequestString(bpc.request, "password");
        String actionType = ParamUtil.getString(bpc.request, "crud_action_type");
        if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        String userDomain = "intranet";
        ClientUser intranetUser = null;
        OrgUserDto orgUserDto = null;
        Map<String, String> errorMap = new HashMap<>(34);
        if (!StringUtil.isEmpty(userId)) {
            orgUserDto = intranetUserService.findIntranetUserByUserId(userId);
            if(orgUserDto!=null){
                if (IntranetUserConstant.DEACTIVATE.equals(actionType)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_IACTIVE);
                    intranetUserService.updateOrgUser(orgUserDto);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.REDEACTIVATE.equals(actionType)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                    intranetUserService.updateOrgUser(orgUserDto);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.TERMINATE.equals(actionType)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_TERMINATED);
                    intranetUserService.updateOrgUser(orgUserDto);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                } else if (IntranetUserConstant.UNLOCK.equals(actionType)) {
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                    intranetUserService.updateOrgUser(orgUserDto);
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                    return;
                }
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                return;
            }else {
                errorMap.put("userId", "Please input correct userId");
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                return;
            }
        } else {
            errorMap.put("userId", "ERR0009");
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
//        Boolean validatepassword = false ;
//        if(intranetUser!=null) {
//            UserIdentifier userIdentifier = new UserIdentifier();
//            userIdentifier.setId(userId);
//            userIdentifier.setUserDomain("intranet");
//            validatepassword = intranetUserService.validatepassword(password, userIdentifier);
//        }
//        if(validatepassword){


//        if(validatepassword){
//           // ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
//            if(IntranetUserConstant.DEACTIVATE.equals(actionType)){
//                userByIdentifier.setAccountStatus(ClientUser.STATUS_INACTIVE);
//                intranetUserService.updateEgpUser(userByIdentifier);
//                ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//                return;
//            }else if(IntranetUserConstant.REDEACTIVATE.equals(actionType)){
//                userByIdentifier.setAccountStatus(ClientUser.STATUS_ACTIVE);
//                intranetUserService.updateEgpUser(userByIdentifier);
//                ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//                return;
//            }else if(IntranetUserConstant.TERMINATE.equals(actionType)){
//                userByIdentifier.setAccountStatus(ClientUser.STATUS_TERMINATED);
//                intranetUserService.updateEgpUser(userByIdentifier);
//                ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//                return;
//            }else {
//                userByIdentifier.setAccountStatus(ClientUser.STATUS_ACTIVE);
//                intranetUserService.updateEgpUser(userByIdentifier);
//                ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//                return;
//            }
//        }
    }


    private OrgUserDto prepareOrgUserDto(HttpServletRequest request) {
        OrgUserDto orgUserDto = new OrgUserDto();
        String userId = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_USERID);
        String displayName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String startDateStr = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_STARTDATE);
        Date startDate = DateUtil.parseDate(startDateStr, "dd/MM/yyyy");
        String endDateStr = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
        String[] salutation = ParamUtil.getStrings(request, IntranetUserConstant.INTRANET_SALUTATION);
        if (!"".equals(salutation[0])) {
            orgUserDto.setSalutation(salutation[0]);
        }
        String firstName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_FIRSTNAME);
        String lastName = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_LASTNAME);
        String division = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_DIVISION);
        String branch = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_BRANCH);
        String email = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_EMAILADDR);
        String mobileNo = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_OFFICETELNO);
        String remarks = ParamUtil.getRequestString(request, IntranetUserConstant.INTRANET_REMARKS);

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
        return orgUserDto;
    }


    private OrgUserDto prepareEditOrgUserDto(BaseProcessClass bpc) {
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(bpc.request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        String displayName = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_DISPLAYNAME);
        String endDateStr = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.INTRANET_ENDDATE);
        Date endDate = DateUtil.parseDate(endDateStr, "dd/MM/yyyy");
        String[] salutation = ParamUtil.getStrings(bpc.request, IntranetUserConstant.INTRANET_SALUTATION);
        if (!"".equals(salutation[0])) {
            orgUserDto.setSalutation(salutation[0]);
        }
//        String [] status = ParamUtil.getStrings(bpc.request,"status");
//        if(!"".equals(status[0])){
//            orgUserDto.setStatus(status[0]);
//        }
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
        return orgUserDto;
    }


    private void saveEgpUser(OrgUserDto orgUserDto) {
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


    private void editEgpUser(OrgUserDto orgUserDto) {
        String userId = orgUserDto.getUserId();
        String userDomain = orgUserDto.getUserDomain();
        ClientUser clientUser = intranetUserService.getUserByIdentifier(userId, userDomain);
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


    private Boolean deleteEgpUser(String userDomian, String userId) {
        return intranetUserService.deleteEgpUser(userDomian, userId);
    }


//    private Map<String,  String> doValidatePo(OrgUserDto orgUserDto) {
//        Map<String,String> errorMap = new HashMap<>(34);
//        String userDomain = orgUserDto.getUserDomain();
//        String userId = orgUserDto.getUserId();
//        String displayName = orgUserDto.getDisplayName();
////        String email = orgUserDto.getEmail();
//        Date accountActivateDatetime = orgUserDto.getAccountActivateDatetime();
//        Date accountDeactivateDatetime = orgUserDto.getAccountDeactivateDatetime();
//        //userId
//        if(!StringUtil.isEmpty(userId)){
//            if(!userId.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(.{1,64})$")){
//                errorMap.put("userId","USER_ERR002");
//            }else{
//                ClientUser userByIdentifier = intranetUserService.getUserByIdentifier(userId,userDomain);
//                if(userByIdentifier!=null){
//                    String userid = userByIdentifier.getUserIdentifier().getId();
//                    if(userId.equals(userid)){
//                        errorMap.put("userId","USER_ERR003");
//                    }
//                }
//            }
//        }
//        //date
//        if(accountActivateDatetime!=null) {
//            int i = accountActivateDatetime.compareTo(new Date());
//            if(i<0){
//                errorMap.put("accountActivateDatetime", "USER_ERR007");
//            }
//        }
//
//        if(accountDeactivateDatetime!=null&&accountActivateDatetime!=null) {
//            int i = accountDeactivateDatetime.compareTo(accountActivateDatetime);
//            if(i<0){
//                errorMap.put("accountDeactivateDatetime", "USER_ERR006");
//            }
//        }
//        return errorMap;
//    }


    private void prepareOption(BaseProcessClass bpc) {
        HttpServletRequest request =bpc.request;
        List<SelectOption> salutation = new ArrayList<>();
        SelectOption so1 = new SelectOption("Mr", "Mr");
        SelectOption so2 = new SelectOption("Ms", "Ms");
        SelectOption so3 = new SelectOption("Mrs", "Mrs");
        SelectOption so4 = new SelectOption("Mdm", "Mdm");
        SelectOption so5 = new SelectOption("Dr", "Dr");
        salutation.add(so1);
        salutation.add(so2);
        salutation.add(so3);
        salutation.add(so4);
        salutation.add(so5);
        List<SelectOption> statusOption = new ArrayList<>();
        SelectOption status1 = new SelectOption("I", "I");
        SelectOption status2 = new SelectOption("A", "A");
        SelectOption status3 = new SelectOption("E", "E");
        SelectOption status4 = new SelectOption("L", "L");
        SelectOption status5 = new SelectOption("S", "S");
        SelectOption status6 = new SelectOption("T", "T");
        statusOption.add(status1);
        statusOption.add(status2);
        statusOption.add(status3);
        statusOption.add(status4);
        statusOption.add(status5);
        statusOption.add(status6);
        ParamUtil.setSessionAttr(request, "salutation", (Serializable) salutation);
        ParamUtil.setSessionAttr(request, "statusOption", (Serializable) statusOption);
        OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(request, IntranetUserConstant.INTRANET_USER_DTO_ATTR);
        ParamUtil.setSessionAttr(request, IntranetUserConstant.INTRANET_USER_DTO_ATTR, orgUserDto);
    }


    private void importXML(File file) throws DocumentException {
        SAXReader saxReader=new SAXReader();
        CommonsMultipartFile sessionFile;
        Document document=saxReader.read(file);
        //root
        Element root=document.getRootElement();
        //ele
        List list=root.elements();
        for (int i = 0; i < list.size(); i++) {
            Element element=(Element)list.get(i);
            String userId=element.element("userId").getText();
            String userDomain=element.element("userDomain").getText();
            String displayName=element.element("displayName").getText();
            String startDateStr=element.element("startDate").getText();
            Date startDate = DateUtil.parseDate(startDateStr, "yyyy-MM-dd");
            String endDateStr=element.element("endDate").getText();
            Date endDate = DateUtil.parseDate(endDateStr, "yyyy-MM-dd");
            String salutation=element.element("salutation").getText();
            String firstName=element.element("firstName").getText();
            String lastName=element.element("lastName").getText();
            String mobileNo=element.element("mobileNo").getText();
            String officeNo=element.element("officeNo").getText();
            String email=element.element("email").getText();
            String organization=element.element("organization").getText();
            String branchUnit=element.element("branchUnit").getText();
            String status=element.element("status").getText();
            OrgUserDto orgUserDto = new OrgUserDto();
            orgUserDto.setUserId(userId);
            orgUserDto.setFirstName(firstName);
            orgUserDto.setLastName(lastName);
            orgUserDto.setDisplayName(displayName);
            orgUserDto.setAccountActivateDatetime(startDate);
            orgUserDto.setAccountDeactivateDatetime(endDate);
            orgUserDto.setOrgId(IntranetUserConstant.ORGANIZATION);
            orgUserDto.setBranchUnit(branchUnit);
            orgUserDto.setEmail(email);
            orgUserDto.setMobileNo(mobileNo);
            orgUserDto.setOfficeTelNo(officeNo);
            orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
            orgUserDto.setUserDomain(IntranetUserConstant.DOMAIN_INTRANET);
            intranetUserService.createIntranetUser(orgUserDto);
        }
    }


    private List<SelectOption> getStatusOption() {
        List<SelectOption> result = new ArrayList<>();
        SelectOption so1 = new SelectOption("CMSTAT001", "Active");
        SelectOption so2 = new SelectOption("CMSTAT003", "Inactive");
        SelectOption so3 = new SelectOption("OUSTAT004", "Terminated");
        SelectOption so4 = new SelectOption("OUSTAT001", "Expired");
        SelectOption so5 = new SelectOption("OUSTAT002", "Locked");
        SelectOption so6 = new SelectOption("OUSTAT003", "Suspended");
        result.add(so1);
        result.add(so2);
        result.add(so3);
        result.add(so4);
        result.add(so5);
        result.add(so6);
        return result;
    }

    private static File inputStreamToFile(InputStream ins,File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return file;
    }
}
