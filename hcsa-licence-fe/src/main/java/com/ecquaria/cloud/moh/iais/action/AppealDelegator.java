package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

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
    private SystemParamConfig systemParamConfig;

    public void preparetionData(BaseProcessClass bpc){
        log.info("start**************preparetionData************");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext!=null){
            bpc.request.getSession().setAttribute("loginContext",loginContext);
        }
        String crud_action_type = bpc.request.getParameter("crud_action_type");
        if("inbox".equals(crud_action_type)){
            bpc. request.setAttribute("crud_action_type","inbox");
            return;
        }
        appealService.getMessage(bpc.request);
        log.info("end**************preparetionData************");
        bpc. request.setAttribute("crud_action_type","");
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
                bpc.response.sendRedirect(tokenUrl);
            } catch (IOException e) {
              log.error(e.getMessage(),e);
            }
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

        bpc.request.setAttribute("newApplicationNo",s);
        log.info("end**************submit************");
    }

    public void cancel(BaseProcessClass bpc) throws IOException {
        log.info("start**************cancel************");
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
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
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = getHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = getIdTypeSelOp();
        Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = getHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = getHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //Professional Regn Type
        List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);
        Map<String,String> proRegnTypeAttr = IaisCommonUtils.genNewHashMap();
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


        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return sql;
    }

    public static List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
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
}
