package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2019/12/5 15:51
 */
@Controller
@Slf4j
public class NewApplicationAjaxController {

    @Autowired
    private ServiceConfigService serviceConfigService;


    //=============================================================================
    //ajax method
    //=============================================================================
    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/retrieve-address")
    public @ResponseBody
    PostCodeDto retrieveYourAddress(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String postalCode = ParamUtil.getDate(request, "postalCode");
        if(StringUtil.isEmpty(postalCode)){
            log.debug(StringUtil.changeForLog("postCode is null"));
            return null;
        }
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = serviceConfigService.getPremisesByPostalCode(postalCode);
        }catch (Exception e){
            log.debug(StringUtil.changeForLog("api exception"));
        }

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }



    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/premises-html", method = RequestMethod.GET)
    public @ResponseBody String addPremisesHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add premises html start ...."));
        String currentLength = ParamUtil.getRequestString(request, "currentLength");
        log.debug(StringUtil.changeForLog("currentLength : "+currentLength));

        String sql = SqlMap.INSTANCE.getSql("premises", "premisesHtml").getSqlStr();
        Set<String> premType = (Set<String>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PREMISESTYPE);
        StringBuffer premTypeBuffer = new StringBuffer();

        for(String type:premType){
            String className = "";
            String width = "col-md-3";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)){
                className = "onSite";
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)){
                className = "conveyance";
                width = "col-md-4";
            }

            premTypeBuffer.append("<div class=\"col-xs-5 "+width+"\">")
                    .append("<div class=\"form-check\">")
                    .append("<input class=\"form-check-input premTypeRadio "+className+"\"  type=\"radio\" name=\"premType"+currentLength+"\" value = "+type+" aria-invalid=\"false\">");
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>On-site<br/><span>(at a fixed address)</span></label>");
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Conveyance<br/><span>(in a mobile clinic / ambulance)</span></label>");
            }
            premTypeBuffer.append("</div>")
                    .append("</div>");
        }

        //premiseSelect -- on-site
        List<SelectOption> premisesOnSite= (List) ParamUtil.getSessionAttr(request, "premisesSelect");
        Map<String,String> premisesOnSiteAttr = IaisCommonUtils.genNewHashMap();
        premisesOnSiteAttr.put("class", "premSelect");
        premisesOnSiteAttr.put("id", "onSiteSel");
        premisesOnSiteAttr.put("name", "onSiteSelect");
        premisesOnSiteAttr.put("style", "display: none;");
        String premOnSiteSelectStr = NewApplicationHelper.generateDropDownHtml(premisesOnSiteAttr, premisesOnSite, null);

        //premiseSelect -- conveyance
        List<SelectOption> premisesConv= (List) ParamUtil.getSessionAttr(request, "conveyancePremSel");
        Map<String,String> premisesConvAttr = IaisCommonUtils.genNewHashMap();
        premisesConvAttr.put("class", "premSelect");
        premisesConvAttr.put("id", "conveyanceSel");
        premisesConvAttr.put("name", "conveyanceSelect");
        premisesConvAttr.put("style", "display: none;");
        String premConvSelectStr = NewApplicationHelper.generateDropDownHtml(premisesConvAttr, premisesConv, null);



        List<SelectOption> addrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        //Address Type on-site
        Map<String,String> addrTypesAttr = IaisCommonUtils.genNewHashMap();
        addrTypesAttr.put("class", "siteAddressType");
        addrTypesAttr.put("id", "siteAddressType");
        addrTypesAttr.put("name", "onSiteAddressType");
        addrTypesAttr.put("style", "display: none;");
        String addrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(addrTypesAttr, addrTypes,NewApplicationDelegator.FIRESTOPTION);

        //Address Type conveyance
        Map<String,String> conAddrTypesAttr = IaisCommonUtils.genNewHashMap();
        conAddrTypesAttr.put("class", "conveyanceAddressType");
        conAddrTypesAttr.put("id", "siteAddressType");
        conAddrTypesAttr.put("name", "conveyanceAddrType");
        conAddrTypesAttr.put("style", "display: none;");
        String conAddrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(conAddrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(0)", currentLength);
        sql = sql.replace("(1)", premTypeBuffer.toString());
        sql = sql.replace("(2)", premOnSiteSelectStr);
        sql = sql.replace("(3)", premConvSelectStr);
        sql = sql.replace("(4)", addrTypeSelectStr);
        sql = sql.replace("(5)", conAddrTypeSelectStr);

        log.debug(StringUtil.changeForLog("the add premises html end ...."));
        return sql;
    }


    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/file-repo", method = RequestMethod.GET)
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =serviceConfigService.downloadFile(fileRepoId);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }


    @RequestMapping(value = "/file-repo-popup", method = RequestMethod.GET)
    public @ResponseBody void filePopUpDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("filePopUpDownload start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtil.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = serviceConfigService.downloadFile(fileRepoId);
        response.setContentType("application/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("filePopUpDownload end ...."));
    }


    @RequestMapping(value = "/governance-officer-html", method = RequestMethod.GET)
    public @ResponseBody String genGovernanceOfficerHtmlList(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        String sql = SqlMap.INSTANCE.getSql("governanceOfficer", "generateGovernanceOfficerHtml").getSqlStr();

        //assign cgo select
        List<SelectOption> cgoSelectList= (List) ParamUtil.getSessionAttr(request, "CgoSelectList");
        Map<String,String> cgoSelectAttr = IaisCommonUtils.genNewHashMap();
        cgoSelectAttr.put("class", "assignSel");
        cgoSelectAttr.put("name", "assignSelect");
        cgoSelectAttr.put("style", "display: none;");
        String cgoSelectStr = NewApplicationHelper.generateDropDownHtml(cgoSelectAttr, cgoSelectList, null);

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "salutationSel");
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
        Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "idTypeSel");
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("class", "designationSel");
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //Professional Regn Type
        List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);
        Map<String,String> proRegnTypeAttr = IaisCommonUtils.genNewHashMap();
        proRegnTypeAttr.put("class", "professionTypeSel");
        proRegnTypeAttr.put("name", "professionType");
        proRegnTypeAttr.put("style", "display: none;");
        String proRegnTypeSelectStr = NewApplicationHelper.generateDropDownHtml(proRegnTypeAttr, proRegnTypeList, NewApplicationDelegator.FIRESTOPTION);

        //Specialty
        List<SelectOption> specialtyList = (List<SelectOption>) ParamUtil.getSessionAttr(request, "SpecialtySelectList");
        Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialtyList, null);



        sql = sql.replace("(1)", cgoSelectStr);
        sql = sql.replace("(2)", salutationSelectStr);
        sql = sql.replace("(3)", idTypeSelectStr);
        sql = sql.replace("(4)", designationSelectStr);
        sql = sql.replace("(5)", proRegnTypeSelectStr);
        sql = sql.replace("(6)", specialtySelectStr);



        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return sql;
    }

    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/psn-info", method = RequestMethod.GET)
    public @ResponseBody
    AppSvcCgoDto getPsnInfoByIdNo (HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo start ...."));
        String idNo = ParamUtil.getRequestString(request, "idNo");
        AppSvcCgoDto appSvcCgoDto = null;
        if(StringUtil.isEmpty(idNo)){
            return appSvcCgoDto;
        }
        AppSubmissionDto appSubmissionDto = ClinicalLaboratoryDelegator.getAppSubmissionDto(request);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList)){
                    appSvcCgoDto = isExistIdNo(appSvcCgoDtoList, idNo);
                    if(appSvcCgoDto != null){
                        break;
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo end ...."));
        return  appSvcCgoDto;
    }

    @RequestMapping(value = "/nuclear-medicine-imaging-html", method = RequestMethod.GET)
    public @ResponseBody String addNuclearMedicineImagingHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html start ...."));
        String sql = SqlMap.INSTANCE.getSql("servicePersonnel", "NuclearMedicineImaging").getSqlStr();
        String currentSvcCod = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        List<SelectOption> personnel = ClinicalLaboratoryDelegator.genPersonnelTypeSel(currentSvcCod);
        Map<String,String> personnelAttr = IaisCommonUtils.genNewHashMap();
        personnelAttr.put("name", "personnelSel");
        personnelAttr.put("class", "personnelSel");
        personnelAttr.put("style", "display: none;");
        String personnelSelectStr = NewApplicationHelper.generateDropDownHtml(personnelAttr, personnel, NewApplicationDelegator.FIRESTOPTION);

        List<SelectOption> designation = (List) ParamUtil.getSessionAttr(request, "NuclearMedicineImagingDesignation");
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designation, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", personnelSelectStr);
        sql = sql.replace("(2)", designationSelectStr);

        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html end ...."));
        return sql;
    }



    @RequestMapping(value = "/principal-officer-html", method = RequestMethod.GET)
    public @ResponseBody String addPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html start ...."));
        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generatePrincipalOfficersHtml").getSqlStr();

        //assign select
        List<SelectOption> assignPrincipalOfficerSel = ClinicalLaboratoryDelegator.getAssignPrincipalOfficerSel(svcId, false);
        Map<String,String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
        assignPrincipalOfficerAttr.put("name", "assignSelect");
        assignPrincipalOfficerAttr.put("class", "poSelect");
        assignPrincipalOfficerAttr.put("style", "display: none;");
        String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION);

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "salutation");
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
        Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "idType");
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("class", "designation");
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", principalOfficerSelStr);
        sql = sql.replace("(2)", salutationSelectStr);
        sql = sql.replace("(3)", idTypeSelectStr);
        sql = sql.replace("(4)", designationSelectStr);

        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html end ...."));
        return sql;
    }


    @RequestMapping(value = "/deputy-principal-officer-html", method = RequestMethod.GET)
    public @ResponseBody String addDeputyPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html start ...."));
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generateDeputyPrincipalOfficersHtml").getSqlStr();

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "deputySalutation");
        salutationAttr.put("name", "deputySalutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
        Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "deputyIdType");
        idTypeAttr.put("name", "deputyIdType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("class", "deputyDesignation");
        designationAttr.put("name", "deputyDesignation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //MedAlert
        List<SelectOption> medAlertSelectList = ClinicalLaboratoryDelegator.getMedAlertSelectList(false);
        Map<String,String> medAlertSelectAttr = IaisCommonUtils.genNewHashMap();
        medAlertSelectAttr.put("class", "modeOfMedAlert");
        medAlertSelectAttr.put("name", "modeOfMedAlert");
        medAlertSelectAttr.put("style", "display: none;");
        String medAlertSelectStr = NewApplicationHelper.generateDropDownHtml(medAlertSelectAttr, medAlertSelectList, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", salutationSelectStr);
        sql = sql.replace("(2)", idTypeSelectStr);
        sql = sql.replace("(3)", designationSelectStr);
        sql = sql.replace("(4)", medAlertSelectStr);

        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html end ...."));
        return sql;
    }


    @RequestMapping(value = "/lic-premises", method = RequestMethod.GET)
    public @ResponseBody AppGrpPremisesDto getLicPremisesInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo start ...."));
        String premIndexNo = ParamUtil.getString(request,"premIndexNo");
        if(StringUtil.isEmpty(premIndexNo)){
            return null;
        }
        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request,NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP);
        AppGrpPremisesDto appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premIndexNo);
        appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);

        licAppGrpPremisesDtoMap.put(premIndexNo,appGrpPremisesDto);
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo end ...."));
        return appGrpPremisesDto;
    }




    //=============================================================================
    //private method
    //=============================================================================
    private AppSvcCgoDto isExistIdNo(List<AppSvcCgoDto> appSvcCgoDtoList, String idNo){
        for (AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
            if(idNo.equals(appSvcCgoDto.getIdNo())){
                log.info(StringUtil.changeForLog("had matching dto"));
                return appSvcCgoDto;
            }
        }
        return  null;
    }


}
