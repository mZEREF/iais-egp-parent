package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.LicFeInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/4 12:46
 */
@Delegator("appealDelegator")
@Slf4j
public class AppealDelegator {
    @Autowired
    private AppealService appealService;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private LicFeInboxClient licFeInboxClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private GenerateIdClient generateIdClient;
    public void preparetionData(BaseProcessClass bpc) throws Exception {
        log.info("start**************preparetionData************");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext!=null){
            bpc.request.getSession().setAttribute("loginContext",loginContext);
        }
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        if(appNo!=null){
            ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(appNo).getEntity();
            if(applicationDto!=null){
                if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus())){
                    bpc.request.setAttribute("appealRfi",ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
                    AppPremisesCorrelationDto entity1 = applicationFeClient.getCorrelationByAppNo(applicationDto.getApplicationNo()).getEntity();
                    AppPremiseMiscDto entity2 = applicationFeClient.getAppPremisesMisc(entity1.getId()).getEntity();
                    bpc.request.setAttribute("applicationId",entity2.getRelateRecId());
                    appealService.getMessage(bpc.request);
                    bpc. request.setAttribute("crud_action_type","appeal");
                    Map<String,Object> subjectMap = IaisCommonUtils.genNewHashMap();
                    subjectMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
                    subjectMap.put("ApplicationNumber",StringUtil.viewHtml(appNo));
                    MsgTemplateDto autoEntity = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_MSG).getEntity();
                    String msgSubject = MsgUtil.getTemplateMessageByContent(autoEntity.getTemplateName(),subjectMap);
                    InterMessageDto interMessageBySubjectLike = appSubmissionService.getInterMessageBySubjectLike(msgSubject.trim(), MessageConstants.MESSAGE_STATUS_RESPONSE);
                    bpc.request.getSession().setAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID,interMessageBySubjectLike.getId());
                    return;
                }
            }
            bpc. request.setAttribute("crud_action_type","inbox");
            return;
        }
        String attribute = (String)bpc.request.getSession().getAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        if(attribute!=null){
            InterMessageDto entity = licFeInboxClient.getInterMessageById(attribute).getEntity();
            if(entity!=null){
                if(MessageConstants.MESSAGE_STATUS_RESPONSE.equals(entity.getStatus())){
                    bpc.request.setAttribute("INBOX_ERR001", MessageUtil.getMessageDesc("INBOX_ERR001"));
                    bpc. request.setAttribute("crud_action_type","ackPage");
                    String appealingFor = ParamUtil.getMaskedString(bpc.request, "appealingFor");
                    String type = bpc.request.getParameter("type");
                    if("application".equals(type)){
                        ApplicationDto applicationDto = applicationFeClient.getApplicationById(appealingFor).getEntity();
                        String serviceId = applicationDto.getServiceId();
                        HcsaServiceDto serviceDtoById = serviceConfigService.getServiceDtoById(serviceId);
                        bpc.request.setAttribute("rfiServiceName",serviceDtoById.getSvcName());
                    }else if("licence".equals(type)){
                        LicenceDto licenceDto = licenceClient.getLicDtoById(appealingFor).getEntity();
                        bpc.request.setAttribute("rfiServiceName",licenceDto.getSvcName());
                    }
                    return;
                }
            }
        }
        appealService.getMessage(bpc.request);
        log.info("end**************preparetionData************");
        bpc. request.setAttribute("crud_action_type","appeal");
    }
    public void ackPage(BaseProcessClass bpc){
    }
    public void appealFrom(BaseProcessClass bpc){
    }
    public void switchProcess(BaseProcessClass bpc ){
        log.info("start**************switchProcess************");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_value =(String) request.getParameter("crud_action_value");
        if ("save".equals(crud_action_value)) {
            bpc. request.setAttribute("crud_action_type","save");
            return;
        }else if("cancel".equals(crud_action_value)){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            try {
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            } catch (IOException e) {
              log.error(e.getMessage(),e);
            }
            return;
        }else if("print".equals(crud_action_value)){
            bpc. request.setAttribute("crud_action_type","print");
            return;
        }
        Map<String, String> validate = appealService.validate(bpc.request);
        if(!validate.isEmpty()){
            bpc. request.setAttribute("crud_action_type","save");
            bpc. request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(validate));
            return;
        }

        bpc. request.setAttribute("crud_action_type","submit");
        log.info("end**************switchProcess************");
    }

    public void inbox(BaseProcessClass bpc) {
        log.info("start**************inbox************");
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        appealService.inbox(bpc.request,appNo);
    }
    public void submit(BaseProcessClass bpc){
        log.info("start**************submit************");
        String s = appealService.submitData(bpc.request);
        //wenkang
        String substring = MessageUtil.replaceMessage("APPEAL_ACK001",s, "Application No");
        log.info(StringUtil.changeForLog("substring : " + substring));
        try {
            if(substring.contains(s)){
                substring = substring.substring(0,substring.lastIndexOf(s));
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        log.info(StringUtil.changeForLog("substring substring : " + substring));
        bpc.request.setAttribute("substring",substring);
        bpc.request.setAttribute("newApplicationNo",s);
        String attribute = (String)bpc.request.getSession().getAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        if(attribute!=null){
            licFeInboxClient.updateMsgStatusTo(attribute, MessageConstants.MESSAGE_STATUS_RESPONSE);
            bpc.request.getSession().removeAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        }
        bpc.getSession().setAttribute("isPopApplicationView",Boolean.FALSE);
        log.info("end**************submit************");
    }

    public void cancel(BaseProcessClass bpc) throws IOException {
        log.info("start**************cancel************");
        bpc.request.getSession().removeAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void start(BaseProcessClass bpc){
        log.info("start**************start************");
        bpc.getSession().removeAttribute("serviceName");
        bpc.getSession().removeAttribute("applicationNo");
        bpc.getSession().removeAttribute("file");
        bpc.getSession().removeAttribute("filename");
        bpc.getSession().removeAttribute("fileReportIdForAppeal");
        bpc.getSession().removeAttribute("appPremisesSpecialDocDto");
        bpc.getSession().removeAttribute("appealingFor");
        bpc.getSession().removeAttribute("type");
        bpc.getSession().removeAttribute("maxCGOnumber");
        bpc.getSession().removeAttribute("appealNo");
        bpc.getSession().removeAttribute("lateFee");
        bpc.getSession().removeAttribute("saveDraftNo");
        bpc.getSession().removeAttribute("CgoMandatoryCount");
        bpc.getSession().removeAttribute("GovernanceOfficersList");
        bpc.getSession().removeAttribute("CgoSelectList");
        bpc.getSession().removeAttribute("SpecialtySelectList");
        bpc.getSession().removeAttribute("IdTypeSelect");
        bpc.getSession().removeAttribute("rfiApplication");
        bpc.getSession().removeAttribute("rfi");
        bpc.getSession().removeAttribute("applicationAPPROVED");
        bpc.getSession().removeAttribute("cgoEqDay");
        bpc.getSession().removeAttribute("nameEqDay");
        bpc.getSession().removeAttribute("otherEqDay");
        bpc.getSession().removeAttribute("feeEqDay");
        bpc.getSession().removeAttribute("rejectEqDay");
        bpc.getSession().removeAttribute("periodEqDay");
        bpc.getSession().removeAttribute("selectOptionList");
        bpc.getSession().removeAttribute("seesion_files_map_ajax_feselectedFile");
        bpc.getSession().removeAttribute("seesion_files_map_ajax_feselectedFile_MaxIndex");
        bpc.getSession().removeAttribute("pageShowFiles");
        bpc.getSession().removeAttribute("appPremiseMiscDto");
        bpc.getSession().setAttribute("isPopApplicationView",Boolean.FALSE);
        ParamUtil.setSessionAttr(bpc.request, HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR, null);
        //set upload file config
        setFileConfig(bpc.request);
        log.info("end**************start************");
    }

    public void save(BaseProcessClass bpc){
        log.info("start**************save************");
        Object errorMsg = bpc.request.getAttribute("errorMsg");

            appealService.saveData(bpc.request);

        log.info("end**************save************");
    }

    public void print(BaseProcessClass bpc) throws IOException {
        log.info("=====start====print");
        appealService.print(bpc.request);
        bpc.request.setAttribute("need_print","need_print");
    }
    @RequestMapping(value = "/regNo-prs",method = RequestMethod.GET)
    @ResponseBody
    public ProfessionalResponseDto prsFlag(@RequestParam("regNo") String regNo){
        ProfessionalResponseDto professionalResponseDto= appealService.prsFlag(regNo);
        return professionalResponseDto;
    }


    @RequestMapping(value = "/governance-officer", method = RequestMethod.GET)
    public @ResponseBody String genGovernanceOfficerHtmlList(HttpServletRequest request){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<FeUserDto> feUserDtos = requestForChangeService.getFeUserDtoByLicenseeId(loginContext.getLicenseeId());
        ParamUtil.setSessionAttr(request, HcsaAppConst.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
        List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
        Map<String, AppSvcPersonAndExtDto> licPersonMap=IaisCommonUtils.genNewHashMap();
        Map<String, AppSvcPersonAndExtDto> personMap = ApplicationHelper.getLicPsnIntoSelMap(feUserDtos,licPersonList,licPersonMap);
        ParamUtil.setSessionAttr(request, HcsaAppConst.PERSONSELECTMAP, (Serializable) personMap);
        List<SelectOption> cgoSelectList = ApplicationHelper.genAssignPersonSel(request, true);
        ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);

        //reload

        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        String sql = SqlMap.INSTANCE.getSql("appealGovernanceOfficer", "appealGenerateGovernanceOfficerHtml").getSqlStr();


        //assign cgo select
        sql = sql.replace("(1)", generateDropDownHtml(cgoSelectList, "assignSelect", "assignSel", null));
        //salutation
        sql = sql.replace("(2)",  generateDropDownHtml(MasterCodeUtil.CATE_ID_SALUTATION, "salutation", "salutationSel"));
        //ID Type
        sql = sql.replace("(3)", generateDropDownHtml(MasterCodeUtil.CATE_ID_ID_TYPE, "idType", "idTypeSel"));
        //Designation
        sql = sql.replace("(4)", generateDropDownHtml(MasterCodeUtil.CATE_ID_DESIGNATION, "designation", "designationSel"));
        //Professional Regn Type
        sql = sql.replace("(5)", generateDropDownHtml(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE, "professionType", "professionTypeSel"));
        //Specialty
        String serviceName =(String)request.getSession().getAttribute("serviceName");
        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        ParamUtil.setSessionAttr(request, HcsaAppConst.CURRENTSVCCODE,serviceByServiceName.getSvcCode());
        List<SelectOption> specialtySelectList= genSpecialtySelectList(serviceByServiceName.getSvcCode());
        ParamUtil.setSessionAttr(request, "SpecialtySelectList", (Serializable) specialtySelectList);
        sql = sql.replace("(6)", generateDropDownHtml(specialtySelectList, "specialty", null));
        // Nationality
        sql = sql.replace("(7)", generateDropDownHtml(MasterCodeUtil.CATE_ID_NATIONALITY, "nationality"));


        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return sql;
    }

    public static List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", HcsaAppConst.FIRESTOPTION);
        idTypeSelectList.add(idType0);

        List<SelectOption> selectOptionList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        idTypeSelectList.addAll(selectOptionList);
        return idTypeSelectList;
    }


    public static   List<SelectOption> genSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }
        }
        return specialtySelectList;
    }



    private void setFileConfig(HttpServletRequest request){
        int configFileSize = systemParamConfig.getUploadFileLimit();

        ParamUtil.setSessionAttr(request,"configFileSize",configFileSize);
    }


    private String generateDropDownHtml(String cateId, String name) {
        return generateDropDownHtml(cateId, name, name);
    }

    private String generateDropDownHtml(String cateId, String name, String className) {
        List<SelectOption> list = MasterCodeUtil.retrieveOptionsByCate(cateId);
        Map<String, String> attrs = IaisCommonUtils.genNewHashMap();
        attrs.put("class", className);
        attrs.put("name", name);
        attrs.put("style", "display: none;");
        return ApplicationHelper.generateDropDownHtml(attrs, list, HcsaAppConst.FIRESTOPTION, null,
                !MasterCodeUtil.CATE_ID_NATIONALITY.equals(cateId));
    }

    private String generateDropDownHtml(List<SelectOption> options, String name, String firstOption) {
        return generateDropDownHtml(options, name, name, firstOption);
    }

    private String generateDropDownHtml(List<SelectOption> options, String name, String className, String firstOption) {
        Map<String, String> attrs = IaisCommonUtils.genNewHashMap();
        attrs.put("class", className);
        attrs.put("name", name);
        attrs.put("style", "display: none;");
        return ApplicationHelper.generateDropDownHtml(attrs, options, firstOption, null);
    }
}
