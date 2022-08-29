package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JarFileUtil;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.PDFGenerator;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chenlei on 5/6/2022.
 */
@Slf4j
@RestController
public class ApplicationAjaxController {

    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";

    @Autowired
    private AppCommService appCommService;

    @Autowired
    private ConfigCommService configCommService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @GetMapping(value = "/retrieve-address")
    public PostCodeDto retrieveYourAddress(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String postalCode = ParamUtil.getDate(request, "postalCode");
        if (StringUtil.isEmpty(postalCode)) {
            log.debug(StringUtil.changeForLog("postCode is null"));
            return null;
        }
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = configCommService.getPostCodeByCode(postalCode);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("api exception - " + e.getMessage()), e);
        }

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }

    /**
     * @param
     * @description: ajax
     * @author: junyu
     */
    @RequestMapping(value = "/file-repo-Authorisation", method = RequestMethod.GET)
    public void fileAuthorisationDownload(HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo Authorisation start ...."));

        File inputFile = ResourceUtils.getFile("classpath:docTemplate/Authorisation Letter.doc");

        FileUtils.writeFileResponseContent(response, inputFile);
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: junyu
     */
    @RequestMapping(value = "/file-repo-DCA", method = RequestMethod.GET)
    public void fileDcaDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo Authorisation start ...."));

        File inputFile = ResourceUtils.getFile("classpath:docTemplate/Annex 8-1-9 DCA.doc");

        FileUtils.writeFileResponseContent(response, inputFile);

        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @GetMapping({"/file-repo", "/file-repo-popup"})
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtil.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = configCommService.downloadFileRepo(fileRepoId);
        if (fileData == null || fileData.length == 0) {
            IaisEGPHelper.redirectUrl(response, "https://" + request.getServerName() + "/main-web/404-error.jsp");
        } else {
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName + "\"");
            response.addHeader("Content-Length", "" + fileData.length);
            response.setContentType("application/x-octet-stream");
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(fileData);
            ops.flush();
            ops.close();
        }
        log.info(StringUtil.changeForLog("file-repo end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: zixian
     */
    @RequestMapping(value = "/psn-info", method = RequestMethod.GET)
    public AppSvcPrincipalOfficersDto getPsnInfoByIdNo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo start ...."));
        String idNo = ParamUtil.getRequestString(request, "idNo");
        AppSvcPrincipalOfficersDto appSvcCgoDto = null;
        if (StringUtil.isEmpty(idNo)) {
            return appSvcCgoDto;
        }
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
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
    public Map<String, String> addNuclearMedicineImagingHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html start ...."));
        int spMaxNumber = 0;
        Map<String, String> resp = IaisCommonUtils.genNewHashMap();
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        String errMsg = "You are allowed to add up till only " + hasNumber + " SP";
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(
                request, SERVICEALLPSNCONFIGMAP);

        String svcId = (String) ParamUtil.getSessionAttr(request, HcsaAppConst.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            if ("SVCPSN".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())) {
                spMaxNumber = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }

        if (spMaxNumber - hasNumber > 0) {
            String sql = SqlMap.INSTANCE.getSql("servicePersonnel", "NuclearMedicineImaging");
            String currentSvcCod = (String) ParamUtil.getSessionAttr(request, HcsaAppConst.CURRENTSVCCODE);
            List<SelectOption> personnel = ApplicationHelper.genPersonnelTypeSel(currentSvcCod);
            Map<String, String> personnelAttr = IaisCommonUtils.genNewHashMap();
            personnelAttr.put("name", "personnelSel");
            personnelAttr.put("class", "personnelSel");
            personnelAttr.put("style", "display: none;");
            String personnelSelectStr = ApplicationHelper.generateDropDownHtml(personnelAttr, personnel, HcsaAppConst.FIRESTOPTION,
                    null);

            List<SelectOption> designation = (List) ParamUtil.getSessionAttr(request, "NuclearMedicineImagingDesignation");
            Map<String, String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            designationAttr.put("class", "designation");
            String designationSelectStr = ApplicationHelper.generateDropDownHtml(designationAttr, designation,
                    HcsaAppConst.FIRESTOPTION, null);
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

    @RequestMapping(value = "/lic-premises", method = RequestMethod.GET)
    public AppGrpPremisesDto getLicPremisesInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo start ...."));
        String premIndexNo = ParamUtil.getString(request, "premIndexNo");
        String premSelectVal = ParamUtil.getString(request, "premSelectVal");
        String premisesType = ParamUtil.getString(request, "premisesType");
        if (StringUtil.isEmpty(premSelectVal) || StringUtil.isEmpty(premisesType)) {
            return null;
        }
        AppGrpPremisesDto appGrpPremisesDto = ApplicationHelper.getPremisesFromMap(premSelectVal, request);
        if (appGrpPremisesDto != null) {
            //for rfc new  renew choose other address ,if no this cannot choose other address from page
            if (AppConsts.YES.equals(appGrpPremisesDto.getExistingData())) {
                boolean sameOne = premIndexNo != null && premIndexNo.equals(appGrpPremisesDto.getPremisesIndexNo());
                log.info(StringUtil.changeForLog("--- The current one: " + sameOne));
                appGrpPremisesDto.setExistingData(sameOne ? AppConsts.NO : AppConsts.YES);
            }
            appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
        } else {
            log.warn(StringUtil.changeForLog("The Session Map is null for this premise selected - " + premSelectVal));
        }
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo end ...."));
        return appGrpPremisesDto;
    }

    /**
     * @param request
     * @return AppSvcPrincipalOfficersDto
     * @Designation Deprecated
     */
    @GetMapping(value = "/psn-select-info")
    public AppSvcPrincipalOfficersDto getPsnSelectInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getNewPsnInfo start ...."));
        String nationality = ParamUtil.getString(request, "nationality");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String psnType = ParamUtil.getString(request, "psnType");
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)) {
            return null;
        }
        String psnKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
        log.info(StringUtil.changeForLog("The Person Key: " + psnKey));
        Map<String, AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.PERSONSELECTMAP);
        AppSvcPrincipalOfficersDto psn = psnMap.get(psnKey);
        if (psn == null) {
            log.info(StringUtil.changeForLog("can not get data from PersonSelectMap ..."));
            return new AppSvcPrincipalOfficersDto();
        }
        String currentSvcCode = (String) ParamUtil.getSessionAttr(request, HcsaAppConst.CURRENTSVCCODE);
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            List<SelectOption> specialityOpts = ApplicationHelper.genSpecialtySelectList(currentSvcCode, false);
            List<SelectOption> selectOptionList = psn.getSpcOptList();
            if (!IaisCommonUtils.isEmpty(selectOptionList)) {
                for (SelectOption sp : selectOptionList) {
                    if (!specialityOpts.contains(sp) && !"other".equals(sp.getValue())) {
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
            String specialityHtml = ApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psn.getSpeciality());
            psn.setSpecialityHtml(specialityHtml);
        }
        log.debug(StringUtil.changeForLog("the getNewPsnInfo end ...."));
        return psn;
    }

    @GetMapping(value = "/prg-input-info")
    public ProfessionalResponseDto getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        return appCommService.retrievePrsInfo(professionRegoNo);
    }

    /**
     * @param request
     * @return AppSvcPrincipalOfficersDto
     * @Designation
     */
    @GetMapping(value = "/person-info")
    public AppSvcPrincipalOfficersDto getKeyPersonnel(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getNewPsnInfo start ...."));
        String nationality = ParamUtil.getString(request, "nationality");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String indexNo = ParamUtil.getString(request, "indexNo");
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)) {
            return null;
        }
        String psnKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
        log.info(StringUtil.changeForLog("The Person Key: " + psnKey));
        String svcCode = (String) ParamUtil.getSessionAttr(request, HcsaAppConst.CURRENTSVCCODE);
        AppSvcPrincipalOfficersDto person = ApplicationHelper.getKeyPersonnelDto(psnKey, svcCode, request);
        person.setIndexNo(indexNo);
        log.debug(StringUtil.changeForLog("the getNewPsnInfo end ...."));
        return person;
    }

    @GetMapping(value = "/user-account-info")
    public Map<String, Object> getUserAccountInfo(HttpServletRequest request) {
        String nationality = ParamUtil.getString(request, "nationality");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        List<FeUserDto> feUserDtos = (List<FeUserDto>) ParamUtil.getSessionAttr(request, HcsaAppConst.CURR_ORG_USER_ACCOUNT);
        String resCode = "404";
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        if (feUserDtos != null && !StringUtil.isEmpty(idType) && !StringUtil.isEmpty(idNo)
            /*&& (AppConsts.NATIONALITY_SG.equals(nationality) || StringUtil.isEmpty(nationality))*/) {
            for (FeUserDto feUserDto : feUserDtos) {
                String userIdType = feUserDto.getIdType();
                if (idType.equals(userIdType) && idNo.equals(feUserDto.getIdNumber())) {
                    String psnKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
                    appSvcPrincipalOfficersDto = ApplicationHelper.getKeyPersonnelDto(psnKey, "", request);
                    if (appSvcPrincipalOfficersDto != null) {
                        resCode = "200";
                    }
                }
            }
        }
        Map<String, Object> map = IaisCommonUtils.genNewHashMap(2);
        map.put("resCode", resCode);
        map.put("resultJson", appSvcPrincipalOfficersDto);
        return map;
    }

    @GetMapping(value = "/person-info/company-licesee")
    public SubLicenseeDto getCompanyLicesee(HttpServletRequest request) {
        return organizationService.getSubLicenseeByLicenseeId(ApplicationHelper.getLicenseeId(request));
    }

    @GetMapping(value = "/person-info/individual-licesee")
    public SubLicenseeDto getLiceseeDetail(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the getLiceseeDetail start ...."));
        String nationality = ParamUtil.getString(request, "nationality");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)) {
            return null;
        }
        Map<String, SubLicenseeDto> psnMap = (Map<String, SubLicenseeDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.LICENSEE_MAP);
        SubLicenseeDto person = Optional.ofNullable(psnMap)
                .map(map -> map.get(ApplicationHelper.getPersonKey(nationality, idType, idNo)))
                .orElseGet(SubLicenseeDto::new);
        log.info(StringUtil.changeForLog("the getLiceseeDetail end .... " + JsonUtil.parseToJson(person)));
        return person;
    }

    @GetMapping(value = "/new-app-ack-print")
    public void generateAckPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String action = ParamUtil.getString(request, "action");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO);
        String txndt = (String) ParamUtil.getSessionAttr(request, "txnDt");
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        byte[] bytes = doPrint(appSubmissionDto, isRfi, txnRefNo, txndt, action);
        if (bytes != null) {
            String fileName = "newAppAck.pdf";
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                fileName = "amendAck.pdf";
            }
            response.setContentType("application/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + bytes.length);
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(bytes);
            ops.close();
            ops.flush();
        }
    }

    @GetMapping(value = "/rfc-app-ack-print")
    public void generateAckPdfOfRfc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(request, "appSubmissionDtos");
        String dAmount = (String) request.getSession().getAttribute("dAmount");
        String payMethod = (String) request.getSession().getAttribute("payMethod");
        appSubmissionDtos.get(0).setAmountStr(dAmount);
        appSubmissionDtos.get(0).setPaymentMethod(payMethod);
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        byte[] bytes = doPrint(appSubmissionDtos.get(0), isRfi, txnRefNo, new SimpleDateFormat("dd/MM/yyyy").format(new Date()), "");
        if (bytes != null) {
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
    public void generateRenewAckPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        String txndt = (String) ParamUtil.getSessionAttr(request, "txnDt");
        String txnRefNo = (String) ParamUtil.getSessionAttr(request, "txnRefNo");
        List<String> svcNames = (List<String>) ParamUtil.getSessionAttr(request, "serviceNamesAck");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if (loginContext != null) {
            licenseeId = loginContext.getLicenseeId();
        }
        String totalString = (String) ParamUtil.getSessionAttr(request, "totalStr");
        byte[] bytes = doRenewPrint(renewDto.getAppSubmissionDtos().get(0), txnRefNo, txndt, svcNames, licenseeId, totalString);


        if (bytes != null) {
            String fileName = "renewAppAck.pdf";
            response.setContentType("application/OCTET-STREAM");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + bytes.length);
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(bytes);
            ops.close();
            ops.flush();
        }
    }

    @PostMapping(value = "/search-charges-type")
    public Map<String, Object> searchChargesTypeByCategory(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap(2);
        String category = ParamUtil.getString(request, "category");
        String chargesSuffix = ParamUtil.getString(request, "chargesSuffix");
        List<SelectOption> chargesTypeList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(category)) {
            switch (category) {
                case ApplicationConsts.CHARGES_CATEGORY_AIRWAY_AND_VENTILATION_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(
                            MasterCodeUtil.CATE_ID_AIRWAY_AND_VENTILATION_EQUIPMENT_CHARGES_TYPE);
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
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(
                            MasterCodeUtil.CATE_ID_TRAUMA_SUPPLIES_OR_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_INFECTION_CONTROL_EQUIPMENT:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(
                            MasterCodeUtil.CATE_ID_INFECTION_CONTROL_EQUIPMENT_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_MEDICATIONS:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MEDICATIONS_CHARGES_TYPE);
                    break;
                case ApplicationConsts.CHARGES_CATEGORY_MISCELLANEOUS:
                    chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MISCELLANEOUS_CHARGES_TYPE);
                    break;
                default:
                    break;
            }
            Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
            chargesTypeAttr.put("class", "otherChargesType");
            chargesTypeAttr.put("name", "otherChargesType" + chargesSuffix);
            chargesTypeAttr.put("style", "display: none;");
            String chargeTypeSelHtml = ApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, chargesTypeList,
                    HcsaAppConst.FIRESTOPTION, null, false);
            map.put("resCode", "200");
            map.put("resultJson", chargeTypeSelHtml);
        }else {
            Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
            chargesTypeAttr.put("class", "otherChargesType");
            chargesTypeAttr.put("name", "otherChargesType" + chargesSuffix);
            chargesTypeAttr.put("style", "display: none;");
            String chargeTypeSelHtml = ApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, chargesTypeList,
                    HcsaAppConst.FIRESTOPTION, null, false);
            map.put("resCode", "200");
            map.put("resultJson", chargeTypeSelHtml);
        }
        return map;
    }

    @PostMapping(value = "/general-charges-html")
    public Map<String, Object> generateGeneralChargesHtml(HttpServletRequest request) throws IOException, TemplateException {
        int generalChargeLength = ParamUtil.getInt(request, "generalChargeLength");
        //charges type
        List<SelectOption> chargesTypeList = MasterCodeUtil.retrieveOptionsByCate(
                MasterCodeUtil.CATE_ID_GENERAL_CONVEYANCE_CHARGES_TYPE);
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("class", "chargesType");
        chargesTypeAttr.put("name", "chargesType" + generalChargeLength);
        chargesTypeAttr.put("style", "display: none;");
        String chargesTypeSelectStr = ApplicationHelper.generateDropDownHtml(chargesTypeAttr, chargesTypeList,
                HcsaAppConst.FIRESTOPTION, null);

        Map<String, Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("chargesType", chargesTypeSelectStr);
        paramMap.put("suffix", generalChargeLength);

        String chargesHtml = SqlMap.INSTANCE.getSql("charges", "generalChargesHtml", paramMap);

        Map<String, Object> map = IaisCommonUtils.genNewHashMap(2);
        map.put("resCode", "200");
        map.put("resultJson", chargesHtml);
        return map;
    }

    @PostMapping(value = "/other-charges-html")
    public Map<String, Object> generateOtherChargesHtml(HttpServletRequest request) throws IOException, TemplateException {
        int otherChargeLength = ParamUtil.getInt(request, "otherChargeLength");
        //charges category
        List<SelectOption> chargesCateList = MasterCodeUtil.retrieveOptionsByCate(
                MasterCodeUtil.CATE_ID_MEDICAL_EQUIPMENT_AND_OTHER_CHARGES_CATEGORY);
        Map<String, String> chargesCateAttr = IaisCommonUtils.genNewHashMap();
        chargesCateAttr.put("class", "otherChargesCategory");
        chargesCateAttr.put("name", "otherChargesCategory" + otherChargeLength);
        chargesCateAttr.put("style", "display: none;");
        String chargesCateSelectStr = ApplicationHelper.generateDropDownHtml(chargesCateAttr, chargesCateList,
                HcsaAppConst.FIRESTOPTION, null);
        //empty charges type
        List<SelectOption> chargesTypeList = IaisCommonUtils.genNewArrayList();
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("class", "otherChargesType");
        chargesTypeAttr.put("name", "otherChargesType" + otherChargeLength);
        chargesTypeAttr.put("style", "display: none;");
        String chargesTypeSelectStr = ApplicationHelper.generateDropDownHtml(chargesTypeAttr, chargesTypeList,
                HcsaAppConst.FIRESTOPTION, null);

        Map<String, Object> paramMap = IaisCommonUtils.genNewHashMap();
        paramMap.put("chargesCategory", chargesCateSelectStr);
        paramMap.put("chargesType", chargesTypeSelectStr);
        paramMap.put("suffix", otherChargeLength);

        String chargesHtml = SqlMap.INSTANCE.getSql("charges", "otherChargesHtml", paramMap);

        Map<String, Object> map = IaisCommonUtils.genNewHashMap(2);
        map.put("resCode", "200");
        map.put("resultJson", chargesHtml);
        return map;
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

    private static byte[] doPrint(AppSubmissionDto appSubmissionDto, boolean isRfi, String txnRefNo, String txnDt, String action)
            throws Exception {
        byte[] bytes = null;
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtos != null && appSvcRelatedInfoDtos.size() > 0) {
                Map<String, String> paramMap = IaisCommonUtils.genNewHashMap();

                List<String> svcNameList = IaisCommonUtils.genNewArrayList();
                StringBuilder serviceName = new StringBuilder();
                List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    hcsaServiceDtos.add(HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode()));
                }
                hcsaServiceDtos = ApplicationHelper.sortHcsaServiceDto(hcsaServiceDtos);
                for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                    svcNameList.add("<strong>" + hcsaServiceDto.getSvcName() + "</strong>");
                    serviceName.append("<div class=\"col-xs-12\"><p class=\"ack-font-20\">- <strong>")
                            .append(hcsaServiceDto.getSvcName())
                            .append("</strong></p></div>");
                }
                String serviceNameTitle = String.join(" | ", svcNameList);
                List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(appSubmissionDto.getLicenseeId());
                String emailAddress = ApplicationHelper.emailAddressesToString(licenseeEmailAddrs);

                String newAck005 = MessageUtil.getMessageDesc("NEW_ACK005");
                String emptyStr = "N/A";
                String appType = appSubmissionDto.getAppType();
                String title;
                StringBuilder newExtraTitle = new StringBuilder();
                StringBuilder rfcExtraTitle = new StringBuilder();
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    title = "New Licence Application";
                    newExtraTitle.append("<h3 id=\"newSvc\">You are applying for ")
                            .append(serviceNameTitle)
                            .append("</h3>");
                } else {
                    title = "Amendment";
                    String svcName = appSvcRelatedInfoDtos.get(0).getServiceName();
                    rfcExtraTitle.append("<p class=\"center\">You are amending the <strong>")
                            .append(svcName)
                            .append("  licence (Licence No. ")
                            .append(appSubmissionDto.getLicenceNo())
                            .append("</strong>)</p>");
                }
                if (StringUtil.isEmpty(action) || !"noHeader".equals(action)) {
                    paramMap.put("title", title);
                    paramMap.put("rfcExtraTitle", rfcExtraTitle.toString());
                    paramMap.put("newExtraTitle", newExtraTitle.toString());
                }
                paramMap.put("serviceName", serviceName.toString());
                paramMap.put("emailAddress", StringUtil.viewHtml(emailAddress));
                paramMap.put("NEW_ACK005", StringUtil.viewHtml(newAck005));
                paramMap.put("appGrpNo", StringUtil.viewHtml(appSubmissionDto.getAppGrpNo()));
                if (StringUtil.isEmpty(txnDt)) {
                    paramMap.put("txnDt", StringUtil.viewHtml(emptyStr));
                } else {
                    paramMap.put("txnDt", StringUtil.viewHtml(txnDt));
                }

                paramMap.put("amountStr", StringUtil.viewHtml(appSubmissionDto.getAmountStr()));
                if (StringUtil.isEmpty(appSubmissionDto.getPaymentMethod())) {
                    paramMap.put("paymentMethod", StringUtil.viewHtml(emptyStr));
                } else {
                    String pmtName = MasterCodeUtil.getCodeDesc(appSubmissionDto.getPaymentMethod());
                    paramMap.put("paymentMethod", StringUtil.viewHtml(pmtName));
                }

                paramMap.put("dateColumn", StringUtil.viewHtml("Date & Time"));
                //rfi
                if (!isRfi) {
                    paramMap.put("txnRefNoColumn", "<th>Transactional No.</th>");
                    if (StringUtil.isEmpty(txnRefNo)) {
                        paramMap.put("txnRefNo", StringUtil.viewHtml(emptyStr));
                    } else {
                        paramMap.put("txnRefNo", StringUtil.viewHtml(txnRefNo));
                    }
                } else {
                    paramMap.put("txnRefNoColumn", null);
                    paramMap.put("txnRefNo", null);
                }
                //File pdfFile = new File("new application report.pdf");
                JarFileUtil.copyFileToDir("pdfTemplate", "newAppAck.ftl");
                File templateDir = MiscUtil.generateFile(JarFileUtil.DEFAULT_TMP_DIR_PATH, "pdfTemplate");
                PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
                bytes = pdfGenerator.convertHtmlToPDF("newAppAck.ftl", paramMap);
            }
        }
        return bytes;
    }

    private static byte[] doRenewPrint(AppSubmissionDto appSubmissionDto, String txnRefNo, String txnDt, List<String> svcNames,
            String licenseeId, String totalString) throws IOException, TemplateException {
        byte[] bytes = null;
        if (appSubmissionDto != null) {
            String emptyStr = "N/A";
            Map<String, String> paramMap = IaisCommonUtils.genNewHashMap();
            StringBuilder serviceName = new StringBuilder();
            if (!IaisCommonUtils.isEmpty(svcNames)) {
                for (String svcName : svcNames) {
                    serviceName.append("<div class=\"col-xs-12\"><p class=\"ack-font-20\">- <strong>")
                            .append(svcName)
                            .append("</strong></p></div>");
                }
            }
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
            String emailAddress = ApplicationHelper.emailAddressesToString(licenseeEmailAddrs);
            String newAck005 = MessageUtil.getMessageDesc("NEW_ACK005");

            if (StringUtil.isEmpty(txnRefNo)) {
                paramMap.put("txnRefNo", StringUtil.viewHtml(emptyStr));
            } else {
                paramMap.put("txnRefNo", StringUtil.viewHtml(txnRefNo));
            }
            if (StringUtil.isEmpty(txnDt)) {
                paramMap.put("txnDt", StringUtil.viewHtml(emptyStr));
            } else {
                paramMap.put("txnDt", StringUtil.viewHtml(txnDt));
            }
            if (StringUtil.isEmpty(appSubmissionDto.getPaymentMethod())) {
                paramMap.put("paymentMethod", StringUtil.viewHtml(emptyStr));
            } else {
                String pmtName = MasterCodeUtil.getCodeDesc(appSubmissionDto.getPaymentMethod());
                paramMap.put("paymentMethod", StringUtil.viewHtml(pmtName));
            }
            paramMap.put("amountStr", StringUtil.viewHtml(totalString));
            paramMap.put("emailAddress", StringUtil.viewHtml(emailAddress));
            paramMap.put("serviceName", serviceName.toString());
            paramMap.put("NEW_ACK005", StringUtil.viewHtml(newAck005));
            paramMap.put("dateColumn", StringUtil.viewHtml("Date & Time"));

            File pdfFile = new File("renew application report.pdf");
            JarFileUtil.copyFileToDir("pdfTemplate", "renewAck.ftl");
            File templateDir = MiscUtil.generateFile(JarFileUtil.DEFAULT_TMP_DIR_PATH, "pdfTemplate");
            PDFGenerator pdfGenerator = new PDFGenerator(templateDir);
            bytes = pdfGenerator.convertHtmlToPDF("renewAck.ftl", paramMap);
        }
        return bytes;
    }

    private String getPremPrefixName(String premType) {
        String premTypeStr = "";
        /*if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)){
            premTypeStr = "onSite";
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)){
            premTypeStr = "conveyance";
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premType)){
            premTypeStr = "offSite";
        }else{
            premTypeStr = premType;
        }*/
        return premTypeStr;
    }


    @GetMapping(value = "/co-non-hcsa-template")
    public void downloadCoNonHcsaTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            File inputFile = ResourceUtils.getFile("classpath:template/Co-location_Template.xlsx");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
            }
            FileUtils.writeFileResponseContent(response, inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Export Template has error - " + e.getMessage()), e);
        }
    }

    @GetMapping(value = "/co-non-hcsa-template-top")
    public void downloadTopFromTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            File inputFile = ResourceUtils.getFile("classpath:template/TOP_From_II.pdf");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
            }
            FileUtils.writeFileResponseContent(response, inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Export Template has error - " + e.getMessage()), e);
        }
    }

    @GetMapping(value = "/co-non-hcsa-template2")
    public void downloadNurseTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            File inputFile = ResourceUtils.getFile("classpath:template/Nurse-Upload-Template.xlsx");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
            }
            FileUtils.writeFileResponseContent(response, inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Export Template has error - " + e.getMessage()), e);
        }
    }

}
