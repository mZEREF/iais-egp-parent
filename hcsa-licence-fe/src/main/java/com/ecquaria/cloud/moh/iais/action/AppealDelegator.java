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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.AppealService;
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
                    subjectMap.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
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

        List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption sp0 = new SelectOption("-1", "Please Select");
        cgoSelectList.add(sp0);
        SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
        cgoSelectList.add(sp1);
        ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);

        List<SelectOption> idTypeSelectList = getIdTypeSelOp();
        ParamUtil.setSessionAttr(request, "IdTypeSelect",(Serializable)  idTypeSelectList);

        String currentSvcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        List<SelectOption> specialtySelectList = genSpecialtySelectList(currentSvcCode);

        //reload

        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        String sql = SqlMap.INSTANCE.getSql("governanceOfficer", "generateGovernanceOfficerHtml").getSqlStr();

        //assign cgo select
        Map<String,String> cgoSelectAttr = IaisCommonUtils.genNewHashMap();
        cgoSelectAttr.put("class", "assignSel");
        cgoSelectAttr.put("name", "assignSelect");
        cgoSelectAttr.put("style", "display: none;");
        String cgoSelectStr =getHtml(cgoSelectAttr, cgoSelectList, null);

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "salutationSel");
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = getHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = getIdTypeSelOp();
        Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "idTypeSel");
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = getHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("class", "designationSel");
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = getHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //Professional Regn Type
        List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);
        Map<String,String> proRegnTypeAttr = IaisCommonUtils.genNewHashMap();
        proRegnTypeAttr.put("class", "professionTypeSel");
        proRegnTypeAttr.put("name", "professionType");
        proRegnTypeAttr.put("style", "display: none;");
        String proRegnTypeSelectStr = getHtml(proRegnTypeAttr, proRegnTypeList, NewApplicationDelegator.FIRESTOPTION);

        //Specialty

        Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        String serviceName =(String)request.getSession().getAttribute("serviceName");
        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        specialtySelectList= genSpecialtySelectList(serviceByServiceName.getSvcCode());
        ParamUtil.setSessionAttr(request, "SpecialtySelectList",(Serializable)  specialtySelectList);

        String specialtySelectStr = getHtml(specialtyAttr, specialtySelectList, null);



        sql = sql.replace("(1)", cgoSelectStr);
        sql = sql.replace("(2)", salutationSelectStr);
        sql = sql.replace("(3)", idTypeSelectStr);
        sql = sql.replace("(4)", designationSelectStr);
        sql = sql.replace("(5)", proRegnTypeSelectStr);
        sql = sql.replace("(6)", specialtySelectStr);
        sql = sql.replace("(7)", generateDropDownHtml(MasterCodeUtil.CATE_ID_NATIONALITY, "nationality"));


        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return sql;
    }

    public static List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", NewApplicationDelegator.FIRESTOPTION);
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


    private String getHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption){
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        sBuffer.append(" >");
        for(SelectOption sp:selectOptionList){
            sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if(StringUtil.isEmpty(firestOption)){
            sBuffer.append("<span class=\"current\">").append(selectOptionList.get(0).getText()).append("</span>");
        }else {
            sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"-1\" class=\"option selected\">").append(firestOption).append("</li>");
        }
        for(SelectOption kv:selectOptionList){
            sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
        }
        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
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
        return NewApplicationHelper.generateDropDownHtml(attrs, list, NewApplicationDelegator.FIRESTOPTION, null,
                !MasterCodeUtil.CATE_ID_NATIONALITY.equals(cateId));
    }
}
