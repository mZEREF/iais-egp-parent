package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JarFileUtil;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.utils.PDFGenerator;
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2019/12/5 15:51
 */
@Controller
@Slf4j
public class NewApplicationAjaxController {

    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${moh.halp.prs.enable}")
    private String prsFlag;

    //=============================================================================
    //ajax method
    //=============================================================================

    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @GetMapping(value = "/retrieve-address")
    public @ResponseBody
    PostCodeDto retrieveYourAddress(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String postalCode = ParamUtil.getDate(request, "postalCode");
        if (StringUtil.isEmpty(postalCode)) {
            log.debug(StringUtil.changeForLog("postCode is null"));
            return null;
        }
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = serviceConfigService.getPremisesByPostalCode(postalCode);
        } catch (Exception e) {
            log.debug(StringUtil.changeForLog("api exception"));
        }

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }


    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @RequestMapping(value = "/premises-html", method = RequestMethod.GET)
    public @ResponseBody
    String addPremisesHtml(HttpServletRequest request) {
//        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
//        for (int i = 0; i < 24; i++) {
//            timeHourList.add(new SelectOption(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
//        }
//        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
//        for (int i = 0; i < 60; i++) {
//            timeMinList.add(new SelectOption(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
//        }

        log.debug(StringUtil.changeForLog("the add premises html start ...."));
        String currentLength = ParamUtil.getRequestString(request, "currentLength");
        log.debug(StringUtil.changeForLog("currentLength : " + currentLength));

        String sql = SqlMap.INSTANCE.getSql("premises", "premisesHtml").getSqlStr();
        Set<String> premType = (Set<String>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PREMISESTYPE);
        StringBuilder premTypeBuffer = new StringBuilder();

        for (String type : premType) {
            String className = "";
            String width = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)) {
                className = "onSite";
                if (premType.size() > 2) {
                    width = "col-md-2";
                } else {
                    width = "col-md-3";
                }
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)) {
                className = "conveyance";
                width = "col-md-3";
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(type)) {
                className = "offSite";
                width = "col-md-3";
            }
            premTypeBuffer.append("<div class=\"col-xs-12 ").append(width).append("\">");
            String premTypeTooltip = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)) {
                premTypeTooltip = MessageUtil.getMessageDesc("NEW_ACK019");
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)) {
                premTypeTooltip = MessageUtil.getMessageDesc("NEW_ACK021");
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(type)) {
                premTypeTooltip = MessageUtil.getMessageDesc("NEW_ACK020");
            }
            premTypeBuffer.append("<a class=\"btn-tooltip styleguide-tooltip\" style=\"z-index: 999;position: absolute; right: 30px; top: 12px;\" href=\"javascript:void(0);\" data-placement=\"top\"  data-toggle=\"tooltip\" data-html=\"true\" title=\"&lt;p&gt;")
                    .append(premTypeTooltip)
                    .append("&lt;/p&gt;\">i</a>")
                    .append("<div class=\"form-check\">").append("<input class=\"form-check-input premTypeRadio ").append(className).append("\"  type=\"radio\" name=\"premType").append(currentLength).append("\" value = ").append(type).append(" aria-invalid=\"false\">");
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)) {
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Premises<br/><span>(at fixed address)</span></label>");
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)) {
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Conveyance<br/><span>(registered vehicle, aircraft, vessel or train)</span></label>");
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(type)) {
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Off-site<br/><span>(remotely/non-fixed location)</span></label>");
            }
            premTypeBuffer.append("</div>")
                    .append("</div>");
        }

        //premiseSelect -- on-site
        List<SelectOption> premisesOnSite = (List) ParamUtil.getSessionAttr(request, "premisesSelect");
        Map<String, String> premisesOnSiteAttr = IaisCommonUtils.genNewHashMap();
        premisesOnSiteAttr.put("class", "premSelect");
        premisesOnSiteAttr.put("id", "onSiteSel");
        premisesOnSiteAttr.put("name", "onSiteSelect");
        premisesOnSiteAttr.put("style", "display: none;");
        String premOnSiteSelectStr = NewApplicationHelper.generateDropDownHtml(premisesOnSiteAttr, premisesOnSite, null, null);
        //premiseSelect -- conveyance
        List<SelectOption> premisesConv = (List) ParamUtil.getSessionAttr(request, "conveyancePremSel");
        Map<String, String> premisesConvAttr = IaisCommonUtils.genNewHashMap();
        premisesConvAttr.put("class", "premSelect");
        premisesConvAttr.put("id", "conveyanceSel");
        premisesConvAttr.put("name", "conveyanceSelect");
        premisesConvAttr.put("style", "display: none;");
        String premConvSelectStr = NewApplicationHelper.generateDropDownHtml(premisesConvAttr, premisesConv, null, null);
        //premisesSelect -- offSite
        List<SelectOption> premisesOffSite = (List) ParamUtil.getSessionAttr(request, "offSitePremSel");
        Map<String, String> premisesOffSiteAttr = IaisCommonUtils.genNewHashMap();
        premisesOffSiteAttr.put("class", "premSelect");
        premisesOffSiteAttr.put("id", "offSiteSel");
        premisesOffSiteAttr.put("name", "offSiteSelect");
        premisesOffSiteAttr.put("style", "display: none;");
        String premOffSiteSelectStr = NewApplicationHelper.generateDropDownHtml(premisesOffSiteAttr, premisesOffSite, null, null);

        List<SelectOption> addrTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        //Address Type on-site
        Map<String, String> addrTypesAttr = IaisCommonUtils.genNewHashMap();
        addrTypesAttr.put("class", "onSiteAddressType");
        addrTypesAttr.put("id", "onSiteAddressType");
        addrTypesAttr.put("name", "onSiteAddressType");
        addrTypesAttr.put("style", "display: none;");
        String addrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(addrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION, null);
        //Address Type conveyance
        Map<String, String> conAddrTypesAttr = IaisCommonUtils.genNewHashMap();
        conAddrTypesAttr.put("class", "conveyanceAddressType");
        conAddrTypesAttr.put("id", "conveyanceAddressType");
        conAddrTypesAttr.put("name", "conveyanceAddrType");
        conAddrTypesAttr.put("style", "display: none;");
        String conAddrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(conAddrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION, null);
        //Address Type offSite
        Map<String, String> offSiteAddrTypesAttr = IaisCommonUtils.genNewHashMap();
        offSiteAddrTypesAttr.put("class", "offSiteAddressType");
        offSiteAddrTypesAttr.put("id", "offSiteAddressType");
        offSiteAddrTypesAttr.put("name", "offSiteAddrType");
        offSiteAddrTypesAttr.put("style", "display: none;");
        String offSiteAddrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(offSiteAddrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION, null);



        sql = sql.replace("${premVal}", currentLength);
        sql = sql.replace("${premTypeInfo}", premTypeBuffer.toString());
        sql = sql.replace("${onSiteSelectStr}", premOnSiteSelectStr);
        sql = sql.replace("${conveyanceSelectStr}", premConvSelectStr);
        sql = sql.replace("${offSiteSelectStr}", premOffSiteSelectStr);
        sql = sql.replace("${onSiteAddressTypeStr}", addrTypeSelectStr);
        sql = sql.replace("${conveyanceAddressTypeStr}", conAddrTypeSelectStr);
        sql = sql.replace("${offSiteAddressTypeStr}", offSiteAddrTypeSelectStr);
        String fireTooltip = MessageUtil.getMessageDesc("NEW_ACK006");
        sql = sql.replace("(fireTooltip)", fireTooltip);

        //operation hour
        List<SelectOption> hourList = NewApplicationHelper.getTimeHourList();
        List<SelectOption> minList = NewApplicationHelper.getTimeMinList();
        List<SelectOption> weeklyOpList =  MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
        List<SelectOption> phOpList =  MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);

        sql = setOperationHour(sql,weeklyOpList,phOpList,hourList,minList,"onSite",currentLength);
        sql = setOperationHour(sql,weeklyOpList,phOpList,hourList,minList,"conveyance",currentLength);
        sql = setOperationHour(sql,weeklyOpList,phOpList,hourList,minList,"offSite",currentLength);

        //premises no. val
        Integer premNo = Integer.parseInt(currentLength) + 1;
        sql = sql.replace("${premNo}", String.valueOf(premNo));

        log.debug(StringUtil.changeForLog("the add premises html end ...."));
        return sql;
    }
    /**
     * @param
     * @description: ajax
     * @author: junyu
     */
    @RequestMapping(value = "/file-repo-Authorisation", method = RequestMethod.GET)
    public @ResponseBody
    void fileAuthorisationDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo Authorisation start ...."));

        File inputFile = ResourceUtils.getFile("classpath:docTemplate/Authorisation Letter.doc");

        FileUtils.writeFileResponseContent(response,inputFile);
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: junyu
     */
    @RequestMapping(value = "/file-repo-DCA", method = RequestMethod.GET)
    public @ResponseBody
    void fileDCADownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo Authorisation start ...."));

        File inputFile = ResourceUtils.getFile("classpath:docTemplate/Annex 8-1-9 DCA.doc");

        FileUtils.writeFileResponseContent(response,inputFile);

        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @RequestMapping(value = "/file-repo", method = RequestMethod.GET)
    public @ResponseBody
    void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtil.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = serviceConfigService.downloadFile(fileRepoId);
        if (fileData != null) {
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName+"\"");
            response.addHeader("Content-Length", "" + fileData.length);
            response.setContentType("application/x-octet-stream");
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(fileData);
            ops.close();
            ops.flush();
        }
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }


    @RequestMapping(value = "/file-repo-popup", method = RequestMethod.GET)
    public @ResponseBody
    void filePopUpDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName+"\"");
        response.addHeader("Content-Length", "" + fileData.length);
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("filePopUpDownload end ...."));
    }


    @PostMapping(value = "/governance-officer-html")
    public @ResponseBody
    Map<String, String> genGovernanceOfficerHtmlList(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        int canAddNumber = ParamUtil.getInt(request, "AddNumber");
        String hasNumber = ParamUtil.getRequestString(request, "HasNumber");
        String errMsg = "You are allowed to add up till only " + hasNumber + " CGO";
        if (canAddNumber > 0) {
            String sql = SqlMap.INSTANCE.getSql("governanceOfficer", "generateGovernanceOfficerHtml").getSqlStr();
            //assign cgo select
            List<SelectOption> cgoSelectList = NewApplicationHelper.genAssignPersonSel(request, true);
            Map<String, String> cgoSelectAttr = IaisCommonUtils.genNewHashMap();
            cgoSelectAttr.put("class", "assignSel");
            cgoSelectAttr.put("name", "assignSelect");
            cgoSelectAttr.put("style", "display: none;");
            String cgoSelectStr = NewApplicationHelper.generateDropDownHtml(cgoSelectAttr, cgoSelectList, null, null);

            //salutation
            List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutationSel");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

            //ID Type
            List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
            Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idTypeSel");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);

            //Designation
            List<SelectOption> designationList = NewApplicationHelper.genDesignationOpList(true);
            Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "designationSel");
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            //Professional Regn Type
            List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);

            Map<String, String> proRegnTypeAttr = IaisCommonUtils.genNewHashMap();
            proRegnTypeAttr.put("class", "professionTypeSel");
            proRegnTypeAttr.put("name", "professionType");
            proRegnTypeAttr.put("style", "display: none;");
            String proRegnTypeSelectStr = NewApplicationHelper.generateDropDownHtml(proRegnTypeAttr, proRegnTypeList, NewApplicationDelegator.FIRESTOPTION, null);

            //Specialty
            List<SelectOption> specialtyList = (List<SelectOption>) ParamUtil.getSessionAttr(request, "SpecialtySelectList");

            Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialtyList, null, null);

            sql = sql.replace("(1)", cgoSelectStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", designationSelectStr);
            sql = sql.replace("(5)", proRegnTypeSelectStr);
            sql = sql.replace("(6)", specialtySelectStr);

            log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
            resp.put("sucInfo", sql);
            resp.put("res", "success");
        } else {
            resp.put("errInfo", errMsg);
        }
        return resp;

    }



    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @RequestMapping(value = "/psn-info", method = RequestMethod.GET)
    public @ResponseBody
    AppSvcPrincipalOfficersDto getPsnInfoByIdNo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo start ...."));
        String idNo = ParamUtil.getRequestString(request, "idNo");
        AppSvcPrincipalOfficersDto appSvcCgoDto = null;
        if (StringUtil.isEmpty(idNo)) {
            return appSvcCgoDto;
        }
        AppSubmissionDto appSubmissionDto = ClinicalLaboratoryDelegator.getAppSubmissionDto(request);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcCgoDtoList)) {
                    appSvcCgoDto = isExistIdNo(appSvcCgoDtoList, idNo);
                    if (appSvcCgoDto != null) {
                        break;
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo end ...."));
        return appSvcCgoDto;
    }

    @PostMapping(value = "/nuclear-medicine-imaging-html")
    public @ResponseBody
    Map<String, String> addNuclearMedicineImagingHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html start ...."));
        int spMaxNumber = 0;
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        String errMsg = "You are allowed to add up till only " + hasNumber + " SP";
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);

        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            if ("SVCPSN".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())) {
                spMaxNumber = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }

        if (spMaxNumber - hasNumber > 0) {
            String sql = SqlMap.INSTANCE.getSql("servicePersonnel", "NuclearMedicineImaging").getSqlStr();
            String currentSvcCod = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
            List<SelectOption> personnel = ClinicalLaboratoryDelegator.genPersonnelTypeSel(currentSvcCod);
            Map<String, String> personnelAttr = IaisCommonUtils.genNewHashMap();
            personnelAttr.put("name", "personnelSel");
            personnelAttr.put("class", "personnelSel");
            personnelAttr.put("style", "display: none;");
            String personnelSelectStr = NewApplicationHelper.generateDropDownHtml(personnelAttr, personnel, NewApplicationDelegator.FIRESTOPTION, null);

            List<SelectOption> designation = (List) ParamUtil.getSessionAttr(request, "NuclearMedicineImagingDesignation");
            Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            designationAttr.put("class", "designation");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designation, NewApplicationDelegator.FIRESTOPTION, null);
            sql = sql.replace("(0)", String.valueOf(hasNumber + 1));
            sql = sql.replace("(1)", personnelSelectStr);
            sql = sql.replace("(2)", designationSelectStr);

            log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html end ...."));
            resp.put("sucInfo", sql);
            resp.put("res", "success");
        } else {
            resp.put("errInfo", errMsg);
        }
        return resp;
    }


    @PostMapping(value = "/principal-officer-html")
    public @ResponseBody
    Map<String, String> addPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html start ...."));
        int poMmaximumCount = 0;
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generatePrincipalOfficersHtml").getSqlStr();
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            if ("PO".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())) {
                poMmaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }
        String errMsg = "You are allowed to add up till only " + hasNumber + " PO";
        if (poMmaximumCount - hasNumber > 0) {
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = NewApplicationHelper.genAssignPersonSel(request, true);
            Map<String, String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "poSelect");
            assignPrincipalOfficerAttr.put("class", "poSelect");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION, null);

            //salutation
            List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutation");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

            //ID Type
            List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
            Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idType");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);

            //Designation
            List<SelectOption> designationList = NewApplicationHelper.genDesignationOpList(true);
            Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "designation");
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            sql = sql.replace("(1)", principalOfficerSelStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", designationSelectStr);
            sql = sql.replace("(poOfficerCount)", String.valueOf(hasNumber + 1));
            resp.put("sucInfo", sql);
            resp.put("res", "success");
            log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html end ...."));
        } else {
            resp.put("errInfo", errMsg);
        }
        return resp;
    }


    @PostMapping(value = "/deputy-principal-officer-html")
    public @ResponseBody
    Map<String, String> addDeputyPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html start ...."));
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generateDeputyPrincipalOfficersHtml").getSqlStr();
        int dpoMmaximumCount = 0;
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);

        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            if ("DPO".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())) {
                dpoMmaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }

        String errMsg = "You are allowed to add up till only " + dpoMmaximumCount + " DPO";
        if (dpoMmaximumCount - hasNumber > 0) {
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = NewApplicationHelper.genAssignPersonSel(request, false);
            Map<String, String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "deputyPoSelect");
            assignPrincipalOfficerAttr.put("class", "deputyPoSelect");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION, null);
            //salutation
            List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "deputySalutation");
            salutationAttr.put("name", "deputySalutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);
            //ID Type
            List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
            Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "deputyIdType");
            idTypeAttr.put("name", "deputyIdType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);

            //Designation
            List<SelectOption> designationList = NewApplicationHelper.genDesignationOpList(true);
            Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "deputyDesignation");
            designationAttr.put("name", "deputyDesignation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            sql = sql.replace("(1)", salutationSelectStr);
            sql = sql.replace("(2)", idTypeSelectStr);
            sql = sql.replace("(3)", designationSelectStr);
            sql = sql.replace("(4)", principalOfficerSelStr);
            sql = sql.replace("(dpoOfficerCount)", String.valueOf(hasNumber + 1));
            log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html end ...."));
            resp.put("sucInfo", sql);
            resp.put("res", "success");
        } else {
            resp.put("errInfo", errMsg);
        }
        return resp;
    }


    @RequestMapping(value = "/lic-premises", method = RequestMethod.GET)
    public @ResponseBody
    AppGrpPremisesDto getLicPremisesInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo start ...."));
        String premIndexNo = ParamUtil.getString(request, "premIndexNo");
        String premisesType = ParamUtil.getString(request,"premisesType");
        String premiseIndex = request.getParameter("premiseIndex");
        if (StringUtil.isEmpty(premIndexNo) || StringUtil.isEmpty(premisesType) || StringUtil.isEmpty(premiseIndex)) {
            return null;
        }
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP);
        AppGrpPremisesDto appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premIndexNo);
        appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
        //set dayName
        if (appGrpPremisesDto != null) {
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
            if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
                NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
            }
            // floor and unit
            List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
            if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
                StringBuilder operationHtml = new StringBuilder();
                String sql = SqlMap.INSTANCE.getSql("premises", "premises-operational").getSqlStr();
                sql = sql.replace("${premType}", premisesType);
                sql = sql.replace("${premIndex}", premiseIndex);
                int size = operationalUnitDtos.size();
                for (int i = 0; i < size; i++) {
                    operationHtml.append(sql.replace("${opCount}", Integer.toString(i)));
                }
                appGrpPremisesDto.setOperationHtml(operationHtml.toString());
            }
            String premPrefixName = getPremPrefixName(premisesType);
            //weekly
            List<OperationHoursReloadDto> weeklyDtoList = appGrpPremisesDto.getWeeklyDtoList();
            if(!IaisCommonUtils.isEmpty(weeklyDtoList)){
                StringBuilder weeklyHtml = new StringBuilder();
                for(int i =0;i<weeklyDtoList.size();i++){
                    String sql = genWeeklyCountHtml(premisesType,premiseIndex,String.valueOf(i));
                    String weeklyName =  "Weekly";
                    Map<String, String> weeklyAttr = IaisCommonUtils.genNewHashMap();
                    weeklyAttr.put("class", weeklyName);
                    weeklyAttr.put("id", weeklyName);
                    weeklyAttr.put("name", premiseIndex+premPrefixName + weeklyName + i);
                    weeklyAttr.put("style", "display: none;");
                    List<SelectOption> weeklyOpList =  MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
                    String weeklyDropHtml = NewApplicationHelper.genMutilSelectOpHtml(weeklyAttr,weeklyOpList,null,weeklyDtoList.get(i).getSelectValList(),true);
                    sql = sql.replace("${multipleDropDown}",weeklyDropHtml);
                    weeklyHtml.append(sql);
                }
                appGrpPremisesDto.setWeeklyHtml(weeklyHtml.toString());
            }
            //ph
            List<OperationHoursReloadDto> phDtoList = appGrpPremisesDto.getPhDtoList();
            StringBuilder phHtml = new StringBuilder();
            if(!IaisCommonUtils.isEmpty(phDtoList)){
                for(int i =0;i<phDtoList.size();i++){
                    String sql = genPhCountHtml(premisesType,premiseIndex,String.valueOf(i));
                    String pubHolidayName ="PubHoliday";
                    Map<String, String> pubHolidayAttr = IaisCommonUtils.genNewHashMap();
                    pubHolidayAttr.put("class", pubHolidayName);
                    pubHolidayAttr.put("id", pubHolidayName);
                    pubHolidayAttr.put("name", premiseIndex +premPrefixName+ pubHolidayName + i);
                    pubHolidayAttr.put("style", "display: none;");
                    List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
                    String pubHolidayHtml = NewApplicationHelper.genMutilSelectOpHtml(pubHolidayAttr,phOpList,null,phDtoList.get(i).getSelectValList(),true);
                    sql = sql.replace("${multipleDropDown}", pubHolidayHtml);
                    phHtml.append(sql);
                }
            }else{
                String sql = genPhCountHtml(premisesType,"0","0");
                String pubHolidayName ="PubHoliday";
                Map<String, String> pubHolidayAttr = IaisCommonUtils.genNewHashMap();
                pubHolidayAttr.put("class", pubHolidayName);
                pubHolidayAttr.put("id", pubHolidayName);
                pubHolidayAttr.put("name", premiseIndex +premPrefixName+ pubHolidayName + 0);
                pubHolidayAttr.put("style", "display: none;");
                List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
                String pubHolidayHtml = NewApplicationHelper.genMutilSelectOpHtml(pubHolidayAttr,phOpList,null,null,true);
                sql = sql.replace("${multipleDropDown}", pubHolidayHtml);
                phHtml.append(sql);
            }
            appGrpPremisesDto.setPhHtml(phHtml.toString());
            //event
            List<AppPremEventPeriodDto> eventDtoList = appGrpPremisesDto.getEventDtoList();
            if(!IaisCommonUtils.isEmpty(eventDtoList)){
                StringBuilder eventHtml = new StringBuilder();
                String sql = SqlMap.INSTANCE.getSql("premises", "event").getSqlStr();
                sql = sql.replace("${premIndex}","");
                sql = sql.replace("${premType}",premPrefixName);
                sql = sql.replace("${eventCount}","");
                for(int i =0;i<eventDtoList.size();i++){
                    eventHtml.append(sql);
                }
                appGrpPremisesDto.setEventHtml(eventHtml.toString());
            }

        }

        licAppGrpPremisesDtoMap.put(premIndexNo, appGrpPremisesDto);
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo end ...."));
        //for rfc new  renew choose other address ,if no this cannot choose other address from page
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) request.getSession().getAttribute("oldAppSubmissionDto");
        if(oldAppSubmissionDto!=null){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
            if(appGrpPremisesDtoList!=null&&!appGrpPremisesDtoList.isEmpty()){
                boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appGrpPremisesDto, appGrpPremisesDtoList.get(0));
                appGrpPremisesDto.setEqHciCode(String.valueOf(eqHciCode));
            }
        }

        return appGrpPremisesDto;
    }

    /**
     * @param request
     * @return AppSvcPrincipalOfficersDto
     * @Designation Deprecated
     */
    @GetMapping(value = "/psn-select-info")
    public @ResponseBody
    AppSvcPrincipalOfficersDto getPsnSelectInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getNewPsnInfo start ...."));
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String psnType = ParamUtil.getString(request, "psnType");
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)) {
            return null;
        }
        String psnKey = idType + "," + idNo;
        Map<String, AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        AppSvcPrincipalOfficersDto psn = psnMap.get(psnKey);
        if (psn == null) {
            log.info(StringUtil.changeForLog("can not get data from PersonSelectMap ..."));
            return new AppSvcPrincipalOfficersDto();
        }
        String currentSvcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            List<SelectOption> specialityOpts = NewApplicationHelper.genSpecialtySelectList(currentSvcCode, false);
            List<SelectOption> selectOptionList = psn.getSpcOptList();
            if (!IaisCommonUtils.isEmpty(selectOptionList)) {
                for (SelectOption sp : selectOptionList) {
                    if (!specialityOpts.contains(sp) && !sp.getValue().equals("other")) {
                        specialityOpts.add(sp);
                    }
                }
            }
            //set other
            specialityOpts.add(new SelectOption("other", "Others"));
            psn.setSpcOptList(specialityOpts);
            Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String specialityHtml = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psn.getSpeciality());
            psn.setSpecialityHtml(specialityHtml);
        }
        log.debug(StringUtil.changeForLog("the getNewPsnInfo end ...."));
        return psn;
    }

    @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    ProfessionalResponseDto getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        return appSubmissionService.retrievePrsInfo(professionRegoNo);
    }


    /**
     * @param request
     * @return AppSvcPrincipalOfficersDto
     * @Designation
     */
    @GetMapping(value = "/person-info/svc-code")
    public @ResponseBody AppSvcPrincipalOfficersDto getPsnSelectInfoVersionTwo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getNewPsnInfo start ...."));
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String psnType = ParamUtil.getString(request, "psnType");
        String svcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType) || StringUtil.isEmpty(svcCode)) {
            return null;
        }
        String psnKey = idType + "," + idNo;
        Map<String, AppSvcPersonAndExtDto> psnMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        AppSvcPersonAndExtDto appSvcPersonAndExtDto = psnMap.get(psnKey);
        AppSvcPrincipalOfficersDto person = null;
        //66762
        AppSvcPersonDto appSvcPersonDto = appSvcPersonAndExtDto.getPersonDto();
        if (appSvcPersonDto != null) {
            person = MiscUtil.transferEntityDto(appSvcPersonDto, AppSvcPrincipalOfficersDto.class);
            person.setLicPerson(appSvcPersonAndExtDto.isLicPerson());
        }
        if (person != null && !person.isLicPerson()) {
            person = getAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, person);
        } else {
            person = NewApplicationHelper.genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, svcCode, false);
        }

        if (person == null) {
            log.info(StringUtil.changeForLog("can not get data from person dropdown ..."));
            return new AppSvcPrincipalOfficersDto();
        }
        String currentSvcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            List<SelectOption> specialityOpts = NewApplicationHelper.genSpecialtySelectList(currentSvcCode, false);
            List<SelectOption> selectOptionList = person.getSpcOptList();
            if (!IaisCommonUtils.isEmpty(selectOptionList)) {
                for (SelectOption sp : selectOptionList) {
                    if (!specialityOpts.contains(sp) && !sp.getValue().equals("other")) {
                        specialityOpts.add(sp);
                    }
                }
            }
            String speciality = person.getSpeciality();
            if (!StringUtil.isEmpty(speciality) && selectOptionList != null) {
                int i = 0;
                for (SelectOption sp : selectOptionList) {
                    if (sp.getValue().equals(speciality)) {
                        break;
                    }
                    if (i == selectOptionList.size() - 1) {
                        specialityOpts.add(NewApplicationHelper.getSpecialtyByValue(speciality));
                    }
                    i++;
                }
            }
            //set other
            specialityOpts.add(new SelectOption("other", "Others"));
            person.setSpcOptList(specialityOpts);
            Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String specialityHtml = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, speciality);
            person.setSpecialityHtml(specialityHtml);
        } else if(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)){
            Date specialtyGetDate = person.getSpecialtyGetDate();
            if(specialtyGetDate != null){
                person.setSpecialtyGetDateStr(Formatter.formatDate(specialtyGetDate));
            }
            Date currRegiDate = person.getCurrRegiDate();
            if(currRegiDate != null){
                person.setCurrRegiDateStr(Formatter.formatDate(currRegiDate));
            }
            Date praCerEndDate = person.getPraCerEndDate();
            if(praCerEndDate != null){
                person.setPraCerEndDateStr(Formatter.formatDate(praCerEndDate));
            }
            Date acls = person.getAclsExpiryDate();
            if(acls != null){
                person.setAclsExpiryDateStr(Formatter.formatDate(acls));
            }
            Date bcls = person.getBclsExpiryDate();
            if(bcls != null){
                person.setBclsExpiryDateStr(Formatter.formatDate(bcls));
            }
        }
        log.debug(StringUtil.changeForLog("the getNewPsnInfo end ...."));
        return person;
    }

    @GetMapping(value = "/person-info/company-licesee")
    public @ResponseBody SubLicenseeDto getCompanyLicesee(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext == null) {
            return null;
        }
        return appSubmissionService.getSubLicenseeByLicenseeId(loginContext.getLicenseeId(), loginContext.getUenNo());
    }

    @GetMapping(value = "/person-info/individual-licesee")
    public @ResponseBody SubLicenseeDto getLiceseeDetail(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the getLiceseeDetail start ...."));
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        // String svcCode = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType) /*|| StringUtil.isEmpty(svcCode)*/) {
            return null;
        }
        Map<String, SubLicenseeDto> psnMap = (Map<String, SubLicenseeDto>) ParamUtil.getSessionAttr(request,
                NewApplicationDelegator.LICENSEE_MAP);
        SubLicenseeDto person = Optional.ofNullable(psnMap)
                .map(map -> map.get(NewApplicationHelper.getPersonKey(idType, idNo)))
                .orElseGet(SubLicenseeDto::new);
        log.info(StringUtil.changeForLog("the getLiceseeDetail end .... " + JsonUtil.parseToJson(person)));
        return person;
    }

    @PostMapping(value = "/med-alert-person-html")
    public @ResponseBody
    Map<String, String> genMedAlertPersonHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the genMedAlertPersonHtml start ...."));
        String sql = SqlMap.INSTANCE.getSql("medAlertPerson", "generateMedAlertPersonHtml").getSqlStr();
        int mapMaximumCount = 0;
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);
        for (Map.Entry<String, List<HcsaSvcPersonnelDto>> stringListEntry : svcConfigInfo.entrySet()) {
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = stringListEntry.getValue();
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtoList) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())) {
                    mapMaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                    break;
                }
            }
        }
        String errMsg = "You are allowed to add up till only " + mapMaximumCount + " MedAlert Person";
        //mapMaximumCount = 4;
        if (mapMaximumCount - hasNumber > 0) {
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = NewApplicationHelper.genAssignPersonSel(request, true);
            Map<String, String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "assignSel");
            assignPrincipalOfficerAttr.put("class", "assignSel");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, null, null);
            //salutation
            List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutation");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);
            //ID Type
            List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
            Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idType");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);
            //pre mode
            List<SelectOption> medAlertSelectList = ClinicalLaboratoryDelegator.getMedAlertSelectList();
            Map<String, String> medAlertAttr = IaisCommonUtils.genNewHashMap();
            medAlertAttr.put("class", "description");
            medAlertAttr.put("name", "description");
            medAlertAttr.put("style", "display: none;");
            String medAlertSelectStr = NewApplicationHelper.generateDropDownHtml(medAlertAttr, medAlertSelectList, null, null);

            sql = sql.replace("(0)", String.valueOf(hasNumber));
            sql = sql.replace("(1)", principalOfficerSelStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", medAlertSelectStr);
            resp.put("sucInfo", sql);
            resp.put("res", "success");
        } else {
            resp.put("errInfo", errMsg);
        }

        log.debug(StringUtil.changeForLog("the genMedAlertPersonHtml end ...."));
        return resp;
    }


    @PostMapping(value = "/user-account-info")
    public @ResponseBody
    AjaxResDto getUserAccountInfo(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        List<FeUserDto> feUserDtos = (List<FeUserDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURR_ORG_USER_ACCOUNT);
        String resCode = "404";
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        if (feUserDtos != null && !StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo)) {
            for (FeUserDto feUserDto : feUserDtos) {
                String userIdType = feUserDto.getIdType();
                if (idType.equals(userIdType) && idNo.equals(feUserDto.getIdNumber())) {
                    Map<String, AppSvcPersonAndExtDto> psnMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
                    if (psnMap != null) {
                        AppSvcPersonAndExtDto appSvcPersonAndExtDto = psnMap.get(NewApplicationHelper.getPersonKey(idType, idNo));
//                        if(!ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(appSvcPersonAndExtDto)){
//                            log.info(StringUtil.changeForLog("can not loading this type data"));
//                            continue;
//                        }
                        AppSvcPersonDto appSvcPersonDto = appSvcPersonAndExtDto.getPersonDto();
                        if (appSvcPersonDto != null) {
                            appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcPersonDto, AppSvcPrincipalOfficersDto.class);
                            appSvcPrincipalOfficersDto.setLicPerson(appSvcPersonAndExtDto.isLicPerson());
                        }
                        appSvcPrincipalOfficersDto = getAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, appSvcPrincipalOfficersDto);

                        resCode = "200";
                    }
                }
            }
        }
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode(resCode);
        ajaxResDto.setResultJson(appSvcPrincipalOfficersDto);
        return ajaxResDto;
    }

    @PostMapping(value = "/premises-operational-html")
    public @ResponseBody
    AjaxResDto genPremOperationalHtml(HttpServletRequest request){
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        String premIndex = ParamUtil.getString(request,"premIndex");
        String premType = ParamUtil.getString(request,"premType");
        String opCount = ParamUtil.getString(request,"opCount");

        String premTypeStr = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
            premTypeStr = "onSite";
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
            premTypeStr = "conveyance";
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
            premTypeStr = "offSite";
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premType)){
            premTypeStr = "easMts";
        }
        String sql = SqlMap.INSTANCE.getSql("premises", "premises-operational").getSqlStr();
        sql = sql.replace("${premType}", premTypeStr);
        sql = sql.replace("${premIndex}", premIndex);
        sql = sql.replace("${opCount}", opCount);
        ajaxResDto.setResultJson(sql);
        return ajaxResDto;
    }

    @PostMapping(value = "/operation-weekly-html")
    public @ResponseBody
    AjaxResDto genPremOperationalWeeklyHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the gen weekly start ...."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        String premIndex = ParamUtil.getString(request,"premIndex");
        String premType = ParamUtil.getString(request,"premType");
        String weeklyCount = ParamUtil.getString(request,"weeklyCount");

        String weeklyHtml = genWeeklyCountHtml(premType,premIndex,weeklyCount);

        String premPrefixName = getPremPrefixName(premType);
        String weeklyName = "Weekly";
        Map<String, String> weeklyAttr = IaisCommonUtils.genNewHashMap();
        weeklyAttr.put("class", weeklyName);
        weeklyAttr.put("id", weeklyName);
        weeklyAttr.put("name", premIndex +premPrefixName+ weeklyName + weeklyCount);
        weeklyAttr.put("style", "display: none;");
        List<SelectOption> weeklyOpList =  MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
        String weeklyDropHtml = NewApplicationHelper.genMutilSelectOpHtml(weeklyAttr,weeklyOpList,null,null,true);
        weeklyHtml = weeklyHtml.replace("${multipleDropDown}",weeklyDropHtml);
        ajaxResDto.setResultJson(weeklyHtml);
        log.debug(StringUtil.changeForLog("the gen weekly end ...."));
        return ajaxResDto;
    }

    @PostMapping(value = "/operation-public-holiday-html")
    public @ResponseBody
    AjaxResDto genPublicHolidayHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        String premIndex = ParamUtil.getString(request,"premIndex");
        String premType = ParamUtil.getString(request,"premType");
        String phCount = ParamUtil.getString(request,"phCount");

        String phHtml = genPhCountHtml(premType,premIndex,phCount);

        String premPrefixName = getPremPrefixName(premType);
        String pubHolidayName = "PubHoliday";
        Map<String, String> pubHolidayAttr = IaisCommonUtils.genNewHashMap();
        pubHolidayAttr.put("class", pubHolidayName);
        pubHolidayAttr.put("id", pubHolidayName);
        pubHolidayAttr.put("name", premIndex +premPrefixName+ pubHolidayName + phCount);
        pubHolidayAttr.put("style", "display: none;");
        List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
        String pubHolidayHtml = NewApplicationHelper.genMutilSelectOpHtml(pubHolidayAttr,phOpList,null,null,true);

        phHtml = phHtml.replace("${multipleDropDown}", pubHolidayHtml);

        ajaxResDto.setResultJson(phHtml);

        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml end ...."));
        return ajaxResDto;
    }

    @PostMapping(value = "/operation-event-html")
    public @ResponseBody
    AjaxResDto genEventHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the gen event start ...."));

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        String premIndex = ParamUtil.getString(request,"premIndex");
        String premType = ParamUtil.getString(request,"premType");
        String eventCount = ParamUtil.getString(request,"eventCount");
        String premPrefixName = getPremPrefixName(premType);
        String sql = SqlMap.INSTANCE.getSql("premises", "event").getSqlStr();
        sql = sql.replace("${premIndex}",premIndex);
        sql = sql.replace("${premType}",premPrefixName);
        sql = sql.replace("${eventCount}",eventCount);
        ajaxResDto.setResultJson(sql);

        log.debug(StringUtil.changeForLog("the gen event end ...."));
        return ajaxResDto;
    }



    @GetMapping(value = "/new-app-ack-print")
    public @ResponseBody void generateAckPdf(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String action = ParamUtil.getString(request,"action");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.APPSUBMISSIONDTO);
        String txndt = (String) ParamUtil.getSessionAttr(request, "txnDt");
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        byte[] bytes = doPrint(appSubmissionDto,isRfi,txnRefNo,txndt,action);
        if(bytes != null){
            String fileName = "newAppAck.pdf";
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                fileName = "amendAck.pdf";
            }
            response.setContentType("application/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            response.addHeader("Content-Length", "" + bytes.length);
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(bytes);
            ops.close();
            ops.flush();
        }
    }
    @GetMapping(value = "/rfc-app-ack-print")
    public @ResponseBody void generateAckPdfOfRfc(HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(request, "appSubmissionDtos");
        String dAmount = (String)request.getSession().getAttribute("dAmount");
        String payMethod = (String)request.getSession().getAttribute("payMethod");
        appSubmissionDtos.get(0).setAmountStr(dAmount);
        appSubmissionDtos.get(0).setPaymentMethod(payMethod);
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        byte[] bytes = doPrint(appSubmissionDtos.get(0),isRfi,txnRefNo,new SimpleDateFormat("dd/MM/yyyy").format(new Date()),"");
        if(bytes != null){
            response.setContentType("application/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment;filename=rfcAppAck.pdf");
            response.addHeader("Content-Length", String.valueOf(bytes.length));
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(bytes);
            ops.close();
            ops.flush();
        }
    }

    @GetMapping(value = "/renew-ack-print")
    public @ResponseBody void generateRenewAckPdf(HttpServletRequest request,HttpServletResponse response) throws Exception {
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        String txndt = (String) ParamUtil.getSessionAttr(request, "txnDt");
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        List<String> svcNames = (List<String>) ParamUtil.getSessionAttr(request, "serviceNamesAck");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        String totalString = (String) ParamUtil.getSessionAttr(request, "totalStr");
        byte[] bytes = doRenewPrint(renewDto.getAppSubmissionDtos().get(0), txnRefNo, txndt, svcNames, licenseeId, totalString);



        if(bytes != null){
            String fileName = "renewAppAck.pdf";
            response.setContentType("application/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            response.addHeader("Content-Length", "" + bytes.length);
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(bytes);
            ops.close();
            ops.flush();
        }
    }

    @PostMapping(value = "/vehicle-html")
    public @ResponseBody AjaxResDto generateVehicleHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the generateVehicleHtml start ...."));

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int vehicleLength = ParamUtil.getInt(request,"vehicleLength");

        String vehicleHtml = SqlMap.INSTANCE.getSql("vehicle", "generateVehicleHtml").getSqlStr();
        vehicleHtml = vehicleHtml.replace("${vehicleLength}",String.valueOf(vehicleLength+1));
        vehicleHtml = vehicleHtml.replace("${vehicleSuffix}",String.valueOf(vehicleLength));

        ajaxResDto.setResultJson(vehicleHtml);

        log.debug(StringUtil.changeForLog("the generateVehicleHtml end ...."));
        return ajaxResDto;

    }

    @PostMapping(value = "/section-leader-html")
    public @ResponseBody AjaxResDto generateSectionLeaderHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the generateSectionLeaderHtml start ...."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int slLength = ParamUtil.getInt(request, "slLength");
        log.info(StringUtil.changeForLog("The index: " + slLength));
        String html = SqlMap.INSTANCE.getSql("sectionLeaderHtml", "genSectionLeaderHtml").getSqlStr();
        html = html.replace("${stepName}", HcsaConsts.SECTION_LEADER);
        html = html.replace("${index}", String.valueOf(slLength));
        html = html.replace("${slIndex}", String.valueOf(slLength + 1));
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        html = html.replace("${salutionOptions}", generateDropDownHtml(selectOptions, null));
        ajaxResDto.setResultJson(html);
        log.debug(StringUtil.changeForLog("the generateSectionLeaderHtml end ...."));
        return ajaxResDto;
    }

    private String generateDropDownHtml(List<SelectOption> options, String firstOption) {
        if (options == null || options.isEmpty()) {
            return "";
        }
        StringBuilder html = new StringBuilder();
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<option value=\"\">").append(StringUtil.escapeHtml(firstOption)).append("</option>");
        }
        for (SelectOption option : options) {
            String val = StringUtil.viewNonNullHtml(option.getValue());
            String txt = StringUtil.escapeHtml(option.getText());
            html.append("<option value=\"").append(val).append("\">").append(txt).append("</option>");
        }
        return html.toString();
    }

    @PostMapping(value = "/clinical-director-html")
    public @ResponseBody AjaxResDto generateClinicalDirectorHtml(HttpServletRequest request) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the generateClinicalDirectorHtml start ...."));

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int cdLength = ParamUtil.getInt(request,"cdLength");
        // Assigned person dropdown list
        Map<String, String> assignSelAttr = IaisCommonUtils.genNewHashMap();
        List<SelectOption> personOptions = NewApplicationHelper.genAssignPersonSel(request, true);
        assignSelAttr.put("class", "assignSel");
        assignSelAttr.put("name", "assignSel" + cdLength);
        assignSelAttr.put("style", "display: none;");
        String personalSelect = NewApplicationHelper.generateDropDownHtml(assignSelAttr, personOptions, null, null);
        //proBoardSel
        List<SelectOption> proBoardSel = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSION_BOARD);
        Map<String, String> proBoardAttr = IaisCommonUtils.genNewHashMap();
        proBoardAttr.put("class", "professionBoard");
        proBoardAttr.put("name", "professionBoard"+cdLength);
        proBoardAttr.put("style", "display: none;");
        String proBoardSelectStr = NewApplicationHelper.generateDropDownHtml(proBoardAttr, proBoardSel, NewApplicationDelegator.FIRESTOPTION, null);

        //salutation
        List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "salutation");
        salutationAttr.put("name", "salutation"+cdLength);
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

        //ID Type
        List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "idType");
        idTypeAttr.put("name", "idType"+cdLength);
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);

        //Designation
        List<SelectOption> designationOpList = NewApplicationHelper.genDesignationOpList(true);
        Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
        designationAttr.put("class", "designation");
        designationAttr.put("name", "designation"+cdLength);
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationOpList, NewApplicationDelegator.FIRESTOPTION, null);

        String currSvcCode = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSVCCODE);
        //specialty
        List<SelectOption> easMtsSpecialtySelectList = NewApplicationHelper.genEasMtsSpecialtySelectList(currSvcCode);
        Map<String, String> easMtsSpecialtyAttr = IaisCommonUtils.genNewHashMap();
        easMtsSpecialtyAttr.put("class", "speciality");
        easMtsSpecialtyAttr.put("name", "speciality"+cdLength);
        easMtsSpecialtyAttr.put("style", "display: none;");
        String easMtsSpecialtySelStr = NewApplicationHelper.generateDropDownHtml(easMtsSpecialtyAttr, easMtsSpecialtySelectList, null, null);

        String aclsOrBcls = "";
        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(currSvcCode)){
            aclsOrBcls = "ACLS";
        }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(currSvcCode)){
            aclsOrBcls = "BCLS and AED";
        }
        Map<String,Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("svcCode",currSvcCode);
        paramMap.put("cdLength",cdLength+1);
        paramMap.put("cdSuffix",cdLength);
        paramMap.put("proBoardSel",proBoardSelectStr);
        paramMap.put("salutationSel",salutationSelectStr);
        paramMap.put("idTypeSel",idTypeSelectStr);
        paramMap.put("designationSel",designationSelectStr);
        paramMap.put("aclsOrBcls",aclsOrBcls);
        paramMap.put("specialitySel",easMtsSpecialtySelStr);
        paramMap.put("personalSelect",personalSelect);
        paramMap.put("singleName", HcsaConsts.CLINICAL_DIRECTOR);

        String clinicalDirectorHtml = SqlMap.INSTANCE.getSql("clinicalDirector", "generateClinicalDirectorHtml",paramMap);
        ajaxResDto.setResultJson(clinicalDirectorHtml);

        log.debug(StringUtil.changeForLog("the generateClinicalDirectorHtml end ...."));
        return ajaxResDto;

    }


    @PostMapping(value = "/search-charges-type")
    public @ResponseBody AjaxResDto searchChargesTypeByCategory(HttpServletRequest request){
        AjaxResDto ajaxResDto = new AjaxResDto();
        String category = ParamUtil.getString(request,"category");
        String chargesSuffix = ParamUtil.getString(request,"chargesSuffix");
        List<SelectOption> chargesTypeList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(category)){
            ajaxResDto.setResCode("200");
            switch (category){
                case ApplicationConsts.CHARGES_CATEGORY_AIRWAY_AND_VENTILATION_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AIRWAY_AND_VENTILATION_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_INTRAVENOUS_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INTRAVENOUS_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_CARDIAC_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_CARDIAC_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_IMMOBILISATION_DEVICE:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_IMMOBILISATION_DEVICE_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_TRAUMA_SUPPLIES_OR_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_TRAUMA_SUPPLIES_OR_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_INFECTION_CONTROL_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INFECTION_CONTROL_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_MEDICATIONS:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MEDICATIONS_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_MISCELLANEOUS:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MISCELLANEOUS_CHARGES_TYPE);
                    break;
                default:break;
            }
            Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
            chargesTypeAttr.put("class", "otherChargesType");
            chargesTypeAttr.put("name", "otherChargesType"+chargesSuffix);
            chargesTypeAttr.put("style", "display: none;");
            String chargeTypeSelHtml = NewApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, chargesTypeList, NewApplicationDelegator.FIRESTOPTION, null, false);
            ajaxResDto.setResultJson(chargeTypeSelHtml);
        }

        return ajaxResDto;
    }

    @PostMapping(value = "/general-charges-html")
    public @ResponseBody AjaxResDto generateGeneralChargesHtml(HttpServletRequest request) throws IOException, TemplateException {
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int generalChargeLength = ParamUtil.getInt(request,"generalChargeLength");
        //charges type
        List<SelectOption> chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_GENERAL_CONVEYANCE_CHARGES_TYPE);
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("class", "chargesType");
        chargesTypeAttr.put("name", "chargesType"+generalChargeLength);
        chargesTypeAttr.put("style", "display: none;");
        String chargesTypeSelectStr = NewApplicationHelper.generateDropDownHtml(chargesTypeAttr, chargesTypeList, NewApplicationDelegator.FIRESTOPTION, null);

        Map<String,Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("chargesType",chargesTypeSelectStr);
        paramMap.put("suffix",generalChargeLength);

        String chargesHtml = SqlMap.INSTANCE.getSql("charges", "generalChargesHtml",paramMap);
        ajaxResDto.setResultJson(chargesHtml);

        return ajaxResDto;
    }

    @PostMapping(value = "/other-charges-html")
    public @ResponseBody AjaxResDto generateOtherChargesHtml(HttpServletRequest request) throws IOException, TemplateException {
        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int otherChargeLength = ParamUtil.getInt(request,"otherChargeLength");
        //charges category
        List<SelectOption> chargesCateList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MEDICAL_EQUIPMENT_AND_OTHER_CHARGES_CATEGORY);
        Map<String, String> chargesCateAttr = IaisCommonUtils.genNewHashMap();
        chargesCateAttr.put("class", "otherChargesCategory");
        chargesCateAttr.put("name", "otherChargesCategory"+otherChargeLength);
        chargesCateAttr.put("style", "display: none;");
        String chargesCateSelectStr = NewApplicationHelper.generateDropDownHtml(chargesCateAttr, chargesCateList, NewApplicationDelegator.FIRESTOPTION, null);
        //empty charges type
        List<SelectOption> chargesTypeList = IaisCommonUtils.genNewArrayList();
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("class", "otherChargesType");
        chargesTypeAttr.put("name", "otherChargesType"+otherChargeLength);
        chargesTypeAttr.put("style", "display: none;");
        String chargesTypeSelectStr = NewApplicationHelper.generateDropDownHtml(chargesTypeAttr, chargesTypeList, NewApplicationDelegator.FIRESTOPTION, null);

        Map<String,Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("chargesCategory",chargesCateSelectStr);
        paramMap.put("chargesType",chargesTypeSelectStr);
        paramMap.put("suffix",otherChargeLength);

        String chargesHtml = SqlMap.INSTANCE.getSql("charges", "otherChargesHtml",paramMap);
        ajaxResDto.setResultJson(chargesHtml);

        return ajaxResDto;
    }

    @PostMapping(value = "/keyAppointmentHolder-html")
    public @ResponseBody AjaxResDto generateKeyAppointmentHolderHtml(HttpServletRequest request) throws TemplateException, IOException {

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");
        int keyAppointmentHolderLength = ParamUtil.getInt(request,"keyAppointmentHolderLength");

        // Assigned person dropdown list
        Map<String, String> assignSelAttr = IaisCommonUtils.genNewHashMap();
        List<SelectOption> personOptions = NewApplicationHelper.genAssignPersonSel(request, true);
        assignSelAttr.put("class", "assignSel");
        assignSelAttr.put("name", "assignSel" + keyAppointmentHolderLength);
        assignSelAttr.put("style", "display: none;");
        String personalSelect = NewApplicationHelper.generateDropDownHtml(assignSelAttr, personOptions, null, null);

        //salutation
        List<SelectOption> salutationList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String, String> salutationAttr = IaisCommonUtils.genNewHashMap();
        salutationAttr.put("class", "salutation");
        salutationAttr.put("name", "salutation" + keyAppointmentHolderLength);
        salutationAttr.put("style", "display: none;");
        String salutationSelect = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

        //ID Type
        List<SelectOption> idTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        Map<String, String> idTypeAttr = IaisCommonUtils.genNewHashMap();
        idTypeAttr.put("class", "idType");
        idTypeAttr.put("name", "idType" + keyAppointmentHolderLength);
        idTypeAttr.put("style", "display: none;");
        String idTypeSelect = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, NewApplicationDelegator.FIRESTOPTION, null);

        Map<String,Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("keyAppointmentHolderLength",keyAppointmentHolderLength + 1);
        paramMap.put("keyAppointmentHolderSuffix",keyAppointmentHolderLength);
        paramMap.put("personalSelect",personalSelect);
        paramMap.put("salutationSelect",salutationSelect);
        paramMap.put("idTypeSelect",idTypeSelect);

        String keyAppointmentHolderHtml = SqlMap.INSTANCE.getSql("keyAppointmentHolder", "generateKeyAppointmentHolderHtml", paramMap);

        ajaxResDto.setResultJson(keyAppointmentHolderHtml);

        return ajaxResDto;

    }

    //=============================================================================
    //private method
    //=============================================================================
    private AppSvcPrincipalOfficersDto isExistIdNo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList, String idNo) {
        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
            if (idNo.equals(appSvcCgoDto.getIdNo())) {
                log.info(StringUtil.changeForLog("had matching dto"));
                return appSvcCgoDto;
            }
        }
        return null;
    }

    private AppSvcPrincipalOfficersDto getAppSvcPrincipalOfficersDto(AppSvcPersonAndExtDto appSvcPersonAndExtDto, AppSvcPrincipalOfficersDto person) {
        if (appSvcPersonAndExtDto == null) {
            return person;
        }
        List<AppSvcPersonExtDto> appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
        AppSvcPersonExtDto appSvcPersonExtDto = new AppSvcPersonExtDto();
        if (!IaisCommonUtils.isEmpty(appSvcPersonExtDtos)) {
            appSvcPersonExtDtos.sort((h1, h2) -> h1.getServiceCode().compareTo(h2.getServiceCode()));
            appSvcPersonExtDto = appSvcPersonExtDtos.get(0);
        }
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        person = MiscUtil.transferEntityDto(appSvcPersonExtDto, AppSvcPrincipalOfficersDto.class, fieldMap, person);
        //transfer
        person.setLicPerson(appSvcPersonAndExtDto.isLicPerson());
        AppPsnEditDto appPsnEditDto = NewApplicationHelper.setNeedEditField(person);
        person.setPsnEditDto(appPsnEditDto);
        return person;
    }

    private static byte[] doPrint(AppSubmissionDto appSubmissionDto,boolean isRfi, String txnRefNo, String txnDt, String action) throws Exception {
        byte[] bytes = null;
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtos != null && appSvcRelatedInfoDtos.size()>0){
                Map<String,String> paramMap = IaisCommonUtils.genNewHashMap();

                List<String> svcNameList = IaisCommonUtils.genNewArrayList();
                StringBuilder serviceName = new StringBuilder();
                List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    hcsaServiceDtos.add(HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode()));
                }
                hcsaServiceDtos = NewApplicationHelper.sortHcsaServiceDto(hcsaServiceDtos);
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    svcNameList.add("<strong>"+hcsaServiceDto.getSvcName()+"</strong>");
                    serviceName.append("<div class=\"col-xs-12\"><p class=\"ack-font-20\">- <strong>")
                            .append(hcsaServiceDto.getSvcName())
                            .append("</strong></p></div>");
                }
                String serviceNameTitle = String.join(" | ",svcNameList);
                List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(appSubmissionDto.getLicenseeId());
                String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);

                String newAck005 = MessageUtil.getMessageDesc("NEW_ACK005");
                String emptyStr = "N/A";
                String appType = appSubmissionDto.getAppType();
                String title;
                StringBuilder newExtraTitle = new StringBuilder();
                StringBuilder rfcExtraTitle = new StringBuilder();
                if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                    title = "New Licence Application";
                    newExtraTitle.append("<h3 id=\"newSvc\">You are applying for ")
                            .append(serviceNameTitle)
                            .append("</h3>");
                }else {
                    title = "Amendment" ;
                    String svcName = appSvcRelatedInfoDtos.get(0).getServiceName();
                    rfcExtraTitle.append("<p class=\"center\">You are amending the <strong>")
                            .append(svcName)
                            .append("  licence (Licence No. ")
                            .append(appSubmissionDto.getLicenceNo())
                            .append("</strong>)</p>");
                }
                if(StringUtil.isEmpty(action) || !"noHeader".equals(action)){
                    paramMap.put("title",title);
                    paramMap.put("rfcExtraTitle",rfcExtraTitle.toString());
                    paramMap.put("newExtraTitle",newExtraTitle.toString());
                }
                paramMap.put("serviceName",serviceName.toString());
                paramMap.put("emailAddress",StringUtil.viewHtml(emailAddress));
                paramMap.put("NEW_ACK005",StringUtil.viewHtml(newAck005));
                paramMap.put("appGrpNo",StringUtil.viewHtml(appSubmissionDto.getAppGrpNo()));
                if(StringUtil.isEmpty(txnDt)){
                    paramMap.put("txnDt",StringUtil.viewHtml(emptyStr));
                }else{
                    paramMap.put("txnDt",StringUtil.viewHtml(txnDt));
                }

                paramMap.put("amountStr",StringUtil.viewHtml(appSubmissionDto.getAmountStr()));
                if(StringUtil.isEmpty(appSubmissionDto.getPaymentMethod())){
                    paramMap.put("paymentMethod",StringUtil.viewHtml(emptyStr));
                }else {
                    String pmtName = MasterCodeUtil.getCodeDesc(appSubmissionDto.getPaymentMethod());
                    paramMap.put("paymentMethod",StringUtil.viewHtml(pmtName));
                }

                paramMap.put("dateColumn",StringUtil.viewHtml("Date & Time"));
                //rfi
                if(!isRfi){
                    paramMap.put("txnRefNoColumn","<th>Transactional No.</th>");
                    if(StringUtil.isEmpty(txnRefNo)){
                        paramMap.put("txnRefNo",StringUtil.viewHtml(emptyStr));
                    }else{
                        paramMap.put("txnRefNo",StringUtil.viewHtml(txnRefNo));
                    }
                }else{
                    paramMap.put("txnRefNoColumn",null);
                    paramMap.put("txnRefNo",null);
                }
                File pdfFile = new File("new application report.pdf");
                JarFileUtil.copyFileToDir("pdfTemplate", "newAppAck.ftl");
                File templateDir = MiscUtil.generateFile(JarFileUtil.DEFAULT_TMP_DIR_PATH , "pdfTemplate");
                PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
                bytes = pdfGenerator.convertHtmlToPDF("newAppAck.ftl", paramMap);
            }
        }
        return bytes;
    }

    private static byte[] doRenewPrint(AppSubmissionDto appSubmissionDto, String txnRefNo, String txnDt, List<String> svcNames, String licenseeId, String totalString) throws IOException, TemplateException {
        byte[] bytes = null;
        if(appSubmissionDto != null){
            String emptyStr = "N/A";
            Map<String,String> paramMap = IaisCommonUtils.genNewHashMap();
            StringBuilder serviceName = new StringBuilder();
            if(!IaisCommonUtils.isEmpty(svcNames)){
                for(String svcName:svcNames){
                    serviceName.append("<div class=\"col-xs-12\"><p class=\"ack-font-20\">- <strong>")
                            .append(svcName)
                            .append("</strong></p></div>");
                }
            }
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
            String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
            String newAck005 = MessageUtil.getMessageDesc("NEW_ACK005");

            if(StringUtil.isEmpty(txnRefNo)){
                paramMap.put("txnRefNo",StringUtil.viewHtml(emptyStr));
            }else{
                paramMap.put("txnRefNo",StringUtil.viewHtml(txnRefNo));
            }
            if(StringUtil.isEmpty(txnDt)){
                paramMap.put("txnDt",StringUtil.viewHtml(emptyStr));
            }else{
                paramMap.put("txnDt",StringUtil.viewHtml(txnDt));
            }
            if(StringUtil.isEmpty(appSubmissionDto.getPaymentMethod())){
                paramMap.put("paymentMethod",StringUtil.viewHtml(emptyStr));
            }else {
                String pmtName = MasterCodeUtil.getCodeDesc(appSubmissionDto.getPaymentMethod());
                paramMap.put("paymentMethod",StringUtil.viewHtml(pmtName));
            }
            paramMap.put("amountStr",StringUtil.viewHtml(totalString));
            paramMap.put("emailAddress",StringUtil.viewHtml(emailAddress));
            paramMap.put("serviceName",serviceName.toString());
            paramMap.put("NEW_ACK005",StringUtil.viewHtml(newAck005));
            paramMap.put("dateColumn",StringUtil.viewHtml("Date & Time"));

            File pdfFile = new File("renew application report.pdf");
            JarFileUtil.copyFileToDir("pdfTemplate", "renewAck.ftl");
            File templateDir = MiscUtil.generateFile(JarFileUtil.DEFAULT_TMP_DIR_PATH , "pdfTemplate");
            PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
            bytes = pdfGenerator.convertHtmlToPDF("renewAck.ftl", paramMap);
        }
        return bytes;
    }

    private String getPremPrefixName(String premType){
        String premTypeStr = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
            premTypeStr = "onSite";
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
            premTypeStr = "conveyance";
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
            premTypeStr = "offSite";
        }else{
            premTypeStr = premType;
        }
        return premTypeStr;
    }

    private String genWeeklyCountHtml(String premType,String premIndex,String weeklyCount){
        String premPrefixName = getPremPrefixName(premType);
        List<SelectOption> hourList = NewApplicationHelper.getTimeHourList();
        List<SelectOption> minList = NewApplicationHelper.getTimeMinList();

        String weeklyStartHourName = "WeeklyStartHH";
        Map<String, String> startHourAttr = IaisCommonUtils.genNewHashMap();
        startHourAttr.put("class", weeklyStartHourName);
        startHourAttr.put("id", weeklyStartHourName);
        startHourAttr.put("name", premIndex+premPrefixName+weeklyStartHourName+weeklyCount);
        startHourAttr.put("style", "display: none;");
        String weeklyStartHourHtml = NewApplicationHelper.generateDropDownHtml(startHourAttr,hourList,"--",null);

        String weeklyStartMinName = "WeeklyStartMM";
        Map<String, String> startMinAttr = IaisCommonUtils.genNewHashMap();
        startMinAttr.put("class", weeklyStartMinName);
        startMinAttr.put("id", weeklyStartMinName);
        startMinAttr.put("name", premIndex+premPrefixName+weeklyStartMinName+weeklyCount);
        startMinAttr.put("style", "display: none;");
        String weeklyStartMinHtml = NewApplicationHelper.generateDropDownHtml(startMinAttr,minList,"--",null);


        String weeklyEndHourName = "WeeklyEndHH";
        Map<String, String> endHourAttr = IaisCommonUtils.genNewHashMap();
        endHourAttr.put("class", weeklyEndHourName);
        endHourAttr.put("id", weeklyEndHourName);
        endHourAttr.put("name", premIndex+premPrefixName+weeklyEndHourName+weeklyCount);
        endHourAttr.put("style", "display: none;");
        String weeklyEndHourHtml = NewApplicationHelper.generateDropDownHtml(endHourAttr,hourList,"--",null);

        String weeklyEndMinName ="WeeklyEndMM";
        Map<String, String> endMinAttr = IaisCommonUtils.genNewHashMap();
        endMinAttr.put("class", weeklyEndMinName);
        endMinAttr.put("id", weeklyEndMinName);
        endMinAttr.put("name", premIndex+premPrefixName+weeklyEndMinName+weeklyCount);
        endMinAttr.put("style", "display: none;");
        String weeklyEndMinHtml = NewApplicationHelper.generateDropDownHtml(endMinAttr,minList,"--",null);


        String sql = SqlMap.INSTANCE.getSql("premises", "weekly").getSqlStr();
        sql = sql.replace("${fieldName}","Weekly <span class=\"mandatory\">*</span>");
        sql = sql.replace("${opType}","Weekly");
        sql = sql.replace("${premPrefixName}", premPrefixName);
        sql = sql.replace("${premIndex}", premIndex);
        sql = sql.replace("${opCount}", weeklyCount);
        sql = sql.replace("${startHH}", weeklyStartHourHtml);
        sql = sql.replace("${startMM}", weeklyStartMinHtml);
        sql = sql.replace("${endHH}", weeklyEndHourHtml);
        sql = sql.replace("${endMM}", weeklyEndMinHtml);
        sql = sql.replace("${delClass}","weeklyDel");
        sql = sql.replace("${divClass}","weeklyDiv");
        sql = sql.replace("${allDayName}",premIndex+premPrefixName+"WeeklyAllDay"+weeklyCount);
        return sql;
    }

    private String genPhCountHtml(String premType,String premIndex,String phCount){
        String premPrefixName = getPremPrefixName(premType);
        List<SelectOption> hourList = NewApplicationHelper.getTimeHourList();
        List<SelectOption> minList = NewApplicationHelper.getTimeMinList();

        String pubHolidayStartHourName = "PhStartHH";
        Map<String, String> startHourAttr = IaisCommonUtils.genNewHashMap();
        startHourAttr.put("class", pubHolidayStartHourName);
        startHourAttr.put("id", pubHolidayStartHourName);
        startHourAttr.put("name", premIndex+premPrefixName+pubHolidayStartHourName+phCount);
        startHourAttr.put("style", "display: none;");
        String pubHolidayStartHourHtml = NewApplicationHelper.generateDropDownHtml(startHourAttr,hourList,"--",null);

        String pubHolidayStartMinName = "PhStartMM";
        Map<String, String> startMinAttr = IaisCommonUtils.genNewHashMap();
        startMinAttr.put("class", pubHolidayStartMinName);
        startMinAttr.put("id", pubHolidayStartMinName);
        startMinAttr.put("name", premIndex+premPrefixName+pubHolidayStartMinName+phCount);
        startMinAttr.put("style", "display: none;");
        String pubHolidayStartMinHtml = NewApplicationHelper.generateDropDownHtml(startMinAttr,minList,"--",null);


        String pubHolidayEndHourName = "PhEndHH";
        Map<String, String> endHourAttr = IaisCommonUtils.genNewHashMap();
        endHourAttr.put("class", pubHolidayEndHourName);
        endHourAttr.put("id", pubHolidayEndHourName);
        endHourAttr.put("name", premIndex+premPrefixName+pubHolidayEndHourName+phCount);
        endHourAttr.put("style", "display: none;");
        String pubHolidayEndHourHtml = NewApplicationHelper.generateDropDownHtml(endHourAttr,hourList,"--",null);

        String pubHolidayEndMinName ="PhEndMM";
        Map<String, String> endMinAttr = IaisCommonUtils.genNewHashMap();
        endMinAttr.put("class", pubHolidayEndMinName);
        endMinAttr.put("id", pubHolidayEndMinName);
        endMinAttr.put("name", premIndex+premPrefixName+pubHolidayEndMinName+phCount);
        endMinAttr.put("style", "display: none;");
        String pubHolidayEndMinHtml = NewApplicationHelper.generateDropDownHtml(endMinAttr,minList,"--",null);


        String sql = SqlMap.INSTANCE.getSql("premises", "weekly").getSqlStr();
        sql = sql.replace("${fieldName}","Public Holiday");
        sql = sql.replace("${opType}","PubHoliday");
        sql = sql.replace("${premPrefixName}", premPrefixName);
        sql = sql.replace("${premIndex}", premIndex);
        sql = sql.replace("${opCount}", phCount);
        sql = sql.replace("${startHH}", pubHolidayStartHourHtml);
        sql = sql.replace("${startMM}", pubHolidayStartMinHtml);
        sql = sql.replace("${endHH}", pubHolidayEndHourHtml);
        sql = sql.replace("${endMM}", pubHolidayEndMinHtml);
        sql = sql.replace("${delClass}","pubHolidayDel");
        sql = sql.replace("${divClass}","pubHolidayDiv");
        sql = sql.replace("${allDayName}",premIndex+premPrefixName+"PhAllDay"+phCount);
        return sql;
    }

    private String genMultiDropDown(String premVal,String dropLength,List<SelectOption> dropDownOpList,String premType,String name){
        Map<String, String> attr = IaisCommonUtils.genNewHashMap();
        attr.put("class", name);
        attr.put("id", name);
        attr.put("name", premVal + premType + name + dropLength);
        attr.put("style", "display: none;");
        return NewApplicationHelper.genMutilSelectOpHtml(attr,dropDownOpList,null,null,true);
    }

    private String genOperationHourDropDown(String premVal,String dropLength,List<SelectOption> dropDownOpList,String premType,String name){
        Map<String, String> ohAttr = IaisCommonUtils.genNewHashMap();
        ohAttr.put("class", name);
        ohAttr.put("id", name);
        ohAttr.put("name", premVal+premType+name+dropLength);
        ohAttr.put("style", "display: none;");
        return NewApplicationHelper.generateDropDownHtml(ohAttr, dropDownOpList, "--", null);
    }

    private String setOperationHour(String html,List<SelectOption> weeklyList,List<SelectOption> phList,List<SelectOption> hourList,List<SelectOption> minList,String premType,String premVal){
        //onsite weekly
        String onSiteWeeklyDropHtml = genMultiDropDown(premVal,"0",weeklyList,premType,"Weekly");
        String onSiteWeeklyStartHH = genOperationHourDropDown(premVal,"0",hourList,premType,"WeeklyStartHH");
        String onSiteWeeklyStartMM = genOperationHourDropDown(premVal,"0",minList,premType,"WeeklyStartMM");
        String onSiteWeeklyEndHH = genOperationHourDropDown(premVal,"0",hourList,premType,"WeeklyEndHH");
        String onSiteWeeklyEndMM = genOperationHourDropDown(premVal,"0",minList,premType,"WeeklyEndMM");

        html = html.replace("${"+premType+"WeeklySelect}", onSiteWeeklyDropHtml);
        html = html.replace("${"+premType+"WeeklyStartHH}", onSiteWeeklyStartHH);
        html = html.replace("${"+premType+"WeeklyStartMM}", onSiteWeeklyStartMM);
        html = html.replace("${"+premType+"WeeklyEndHH}", onSiteWeeklyEndHH);
        html = html.replace("${"+premType+"WeeklyEndMM}", onSiteWeeklyEndMM);
        //onsite ph
        String onSitePubHolidayDropHtml = genMultiDropDown(premVal,"0",phList,premType,"PubHoliday");
        String onSitePhStartHH = genOperationHourDropDown(premVal,"0",hourList,premType,"PhStartHH");
        String onSitePhStartMM = genOperationHourDropDown(premVal,"0",minList,premType,"PhStartMM");
        String onSitePhEndHH = genOperationHourDropDown(premVal,"0",hourList,premType,"PhEndHH");
        String onSitePhEndMM = genOperationHourDropDown(premVal,"0",minList,premType,"PhEndMM");

        html = html.replace("${"+premType+"PhSelect}", onSitePubHolidayDropHtml);
        html = html.replace("${"+premType+"PhStartHH}", onSitePhStartHH);
        html = html.replace("${"+premType+"PhStartMM}", onSitePhStartMM);
        html = html.replace("${"+premType+"PhEndHH}", onSitePhEndHH);
        html = html.replace("${"+premType+"PhEndMM}", onSitePhEndMM);

        return html;
    }

}
