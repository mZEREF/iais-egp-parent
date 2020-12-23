package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplicationBe")
@Slf4j
public class CessationApplicationBeDelegator {

    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
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
    private static final String APPCESSATIONDTOS = "appCessationDtos";
    private static final String READINFO = "readInfo";
    private static final String WHICHTODO = "whichTodo";
    private static final String EFFECTIVEDATE = "effectiveDate";
    private static final String REASON = "reason";
    private static final String OTHERREASON = "otherReason";
    private static final String PATRADIO = "patRadio";
    private static final String PATIENTSELECT = "patientSelect";
    private static final String PATNOREMARKS = "patNoRemarks";
    private static final String PATHCINAME = "patHciName";
    private static final String PATREGNO = "patRegNo";
    private static final String PATOTHERS = "patOthers";
    private static final String ERROR = "GENERAL_ERR0006";


    public void start(BaseProcessClass bpc) {
        log.debug("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CESSATION, AuditTrailConsts.FUNCTION_CESSATION);
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfo", null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag",null);
        ParamUtil.setSessionAttr(bpc.request, "isGrpLic",null);
    }

    public void init(BaseProcessClass bpc) {
        List<String> licIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licIds");
        log.debug(StringUtil.changeForLog("cessation licenceIds ===>" + JsonUtil.parseToJson(licIds)));
        boolean isGrpLicence = cessationBeService.isGrpLicence(licIds);
        List<String> specLicIds = cessationBeService.filtrateSpecLicIds(licIds);
        List<AppSpecifiedLicDto> specLicInfo = cessationBeService.getSpecLicInfo(licIds);
        if(specLicInfo.size()>0) {
            Map<String,List<AppSpecifiedLicDto>> map = IaisCommonUtils.genNewHashMap();
            for(AppSpecifiedLicDto appSpecifiedLicDto : specLicInfo){
                String specLicId = appSpecifiedLicDto.getSpecLicId();
                String baseLicNo = appSpecifiedLicDto.getBaseLicNo();
                if(specLicIds.contains(specLicId)){
                    licIds.remove(specLicId);
                    List<AppSpecifiedLicDto> specLicInfoConfirmExist = map.get(baseLicNo);
                    if(!IaisCommonUtils.isEmpty(specLicInfoConfirmExist)){
                        specLicInfoConfirmExist.add(appSpecifiedLicDto);
                    }else {
                        List<AppSpecifiedLicDto> specLicInfoConfirm = IaisCommonUtils.genNewArrayList();
                        specLicInfoConfirm.add(appSpecifiedLicDto);
                        map.put(baseLicNo,specLicInfoConfirm);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request, "specLicInfo", (Serializable) map);
            ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag","exist");
        }
        List<AppCessLicDto> appCessDtosByLicIds = cessationBeService.getAppCessDtosByLicIds(licIds);
        int size = appCessDtosByLicIds.size();
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "size", size);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
        ParamUtil.setSessionAttr(bpc.request, "isGrpLic",isGrpLicence);
    }

    public void prepareData(BaseProcessClass bpc) {
        log.debug("=======>>>>>prepareData>>>>>>>>>>>>>>>>CessationApplicationDelegator");
    }

    public void valiant(BaseProcessClass bpc) throws IOException {
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("back".equals(actionType)) {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTRANET/MohLicenceManagement");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
        List<String> licIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "licIds");
        List<AppCessLicDto> appCessDtosByLicIds = (List<AppCessLicDto>) ParamUtil.getSessionAttr(bpc.request, APPCESSATIONDTOS);
        int size = (int) ParamUtil.getSessionAttr(bpc.request, "size");
        List<AppCessLicDto> appCessHciDtos = prepareDataForValiant(bpc, size, appCessDtosByLicIds);
        List<AppCessLicDto> cloneAppCessHciDtos = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(appCessHciDtos, cloneAppCessHciDtos);
        List<AppCessLicDto> confirmDtos = getConfirmDtos(cloneAppCessHciDtos);
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessHciDtos);
        String readInfo = ParamUtil.getRequestString(bpc.request, READINFO);
        ParamUtil.setSessionAttr(bpc.request, READINFO, readInfo);
        Map<String, String> errorMap = new HashMap<>(34);
        boolean choose = false;
        for (int i = 1; i <= size; i++) {
            int size1 = appCessHciDtos.get(i - 1).getAppCessHciDtos().size();
            for (int j = 1; j <= size1; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    choose = true;
                }
            }
        }
        if (!choose) {
            errorMap.put("choose",MessageUtil.getMessageDesc("CESS_ERR003"));
        }
        if (confirmDtos.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }

        for (int i = 1; i <= size; i++) {
            int size1 = appCessHciDtos.get(i - 1).getAppCessHciDtos().size();
            for (int j = 1; j <= size1; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    Map<String, String> validate = validate(bpc, i, j);
                    errorMap.putAll(validate);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            if(!IaisCommonUtils.isEmpty(licIds)){
                if(licIds.size()==1){
                    LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licIds.get(0)).getEntity();
                    WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
                }
            }
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
        List<AppCessationDto> appCessationDtos = transformDto(cloneAppCessHciDtos);
        ParamUtil.setSessionAttr(bpc.request, "confirmDtos", (Serializable) confirmDtos);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtosSave", (Serializable) appCessationDtos);
    }

    public void action(BaseProcessClass bpc) {
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("submit".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        } else if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        }
    }

    public void saveData(BaseProcessClass bpc) throws Exception {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtosSave");
        Map<String, List<String>> appIdPremisesMap = cessationBeService.saveCessations(appCessationDtos);
        List<AppCessatonConfirmDto> confirmDto = cessationBeService.getConfirmDto(appCessationDtos, appIdPremisesMap, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable) confirmDto);
    }

    /*
        utils
     */

    private List<AppCessLicDto> prepareDataForValiant(BaseProcessClass bpc, int size, List<AppCessLicDto> appCessDtosByLicIds) {
        List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 1; i <= size; i++) {
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(i - 1);
            List<AppCessHciDto> appCessHciDtoso = appCessLicDto.getAppCessHciDtos();
            int size1 = appCessHciDtoso.size();
            List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
            for (int j = 1; j <= size1; j++) {
                AppCessHciDto appCessHciDto = appCessHciDtoso.get(j - 1);
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i + EFFECTIVEDATE + j);
                Date effectiveDate = DateUtil.parseDate(effectiveDateStr, AppConsts.DEFAULT_DATE_FORMAT);
                String reason = ParamUtil.getRequestString(bpc.request, i + REASON + j);
                String otherReason = ParamUtil.getRequestString(bpc.request, i + OTHERREASON + j);
                String patRadio = ParamUtil.getRequestString(bpc.request, i + PATRADIO + j);
                Boolean patNeedTrans = null;
                if(!StringUtil.isEmpty(patRadio)){
                    patNeedTrans = "yes".equals(patRadio);
                }
                String patientSelect = ParamUtil.getRequestString(bpc.request, i + PATIENTSELECT + j);
                String patNoRemarks = ParamUtil.getRequestString(bpc.request, i + PATNOREMARKS + j);
                String patHciName = ParamUtil.getRequestString(bpc.request, i + PATHCINAME + j);
                String patRegNo = ParamUtil.getRequestString(bpc.request, i + PATREGNO + j);
                String patOthers = ParamUtil.getRequestString(bpc.request, i + PATOTHERS + j);
                String readInfo = ParamUtil.getRequestString(bpc.request, READINFO);
                String hciName = appCessHciDto.getHciName();
                String hciAddress = appCessHciDto.getHciAddress();

                appCessHciDto.setHciAddress(hciAddress);
                appCessHciDto.setHciName(hciName);
                appCessHciDto.setEffectiveDate(effectiveDate);
                appCessHciDto.setReason(reason);
                appCessHciDto.setOtherReason(otherReason);
                appCessHciDto.setPatNeedTrans(patNeedTrans);
                appCessHciDto.setPatientSelect(patientSelect);
                appCessHciDto.setPatNoRemarks(patNoRemarks);
                appCessHciDto.setPatHciName(patHciName);
                appCessHciDto.setPatRegNo(patRegNo);
                appCessHciDto.setPatOthers(patOthers);
                appCessHciDto.setPremiseIdChecked(whichTodo);
                appCessHciDto.setReadInfo(readInfo);
                appCessHciDtos.add(appCessHciDto);
            }
            appCessLicDto.setAppCessHciDtos(appCessHciDtos);
            appCessLicDtos.add(appCessLicDto);
        }
        return appCessLicDtos;
    }

    private List<AppCessationDto> transformDto(List<AppCessLicDto> appCessLicDtos) {
        List<AppCessationDto> appCessationDtos = IaisCommonUtils.genNewArrayList();
        for (AppCessLicDto appCessLicDto : appCessLicDtos) {
            String licenceId = appCessLicDto.getLicenceId();
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if (appCessHciDtos != null && !appCessHciDtos.isEmpty()) {
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String whichTodo = appCessHciDto.getPremiseIdChecked();
                    if (!StringUtil.isEmpty(whichTodo)) {
                        Date effectiveDate = appCessHciDto.getEffectiveDate();
                        String reason = appCessHciDto.getReason();
                        String otherReason = appCessHciDto.getOtherReason();
                        Boolean patNeedTrans = appCessHciDto.getPatNeedTrans();
                        String patientSelect = appCessHciDto.getPatientSelect();
                        String patTransType = appCessHciDto.getPatTransType();
                        String patTransTo = appCessHciDto.getPatTransTo();
                        String patRegNo = appCessHciDto.getPatRegNo();
                        String patNoRemarks = appCessHciDto.getPatNoRemarks();
                        String patOthers = appCessHciDto.getPatOthers();
                        String readInfo = appCessHciDto.getReadInfo();

                        AppCessationDto appCessationDto = new AppCessationDto();
                        appCessationDto.setEffectiveDate(effectiveDate);
                        appCessationDto.setReason(reason);
                        appCessationDto.setOtherReason(otherReason);
                        appCessationDto.setPatNeedTrans(patNeedTrans);
                        appCessationDto.setPatientSelect(patientSelect);
                        appCessationDto.setPatTransType(patTransType);
                        appCessationDto.setPatTransTo(patTransTo);
                        appCessationDto.setPatRegNo(patRegNo);
                        appCessationDto.setPatOthers(patOthers);
                        appCessationDto.setPatNoRemarks(patNoRemarks);
                        appCessationDto.setPremiseId(whichTodo);
                        appCessationDto.setReadInfo(readInfo);
                        appCessationDto.setLicId(licenceId);
                        appCessationDtos.add(appCessationDto);
                    }
                }
            }

        }
        return appCessationDtos;
    }

    private List<AppCessLicDto> getConfirmDtos(List<AppCessLicDto> appCessLicDtos) {
        for (AppCessLicDto appCessLicDto : appCessLicDtos) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if (appCessHciDtos != null && !appCessHciDtos.isEmpty()) {
                List<AppCessHciDto> list = IaisCommonUtils.genNewArrayList();
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String premiseIdChecked = appCessHciDto.getPremiseIdChecked();
                    if (StringUtil.isEmpty(premiseIdChecked)) {
                        list.add(appCessHciDto);
                    }
                }
                appCessHciDtos.removeAll(list);
            }
        }
        for (int i = 0; i < appCessLicDtos.size(); i++) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDtos.get(i).getAppCessHciDtos();
            if (appCessHciDtos.size() == 0) {
                appCessLicDtos.remove(i);
                i = i - 1;
            }
        }
        return appCessLicDtos;
    }

    private Map<String, String> validate(BaseProcessClass bpc, int i, int j) {
        HttpServletRequest httpServletRequest = bpc.request;
        Map<String, String> errorMap = new HashMap<>(34);
        String effectiveDateStr = ParamUtil.getRequestString(httpServletRequest, i + EFFECTIVEDATE + j);
        if (StringUtil.isEmpty(effectiveDateStr)) {
            errorMap.put(i + EFFECTIVEDATE + j, MessageUtil.replaceMessage(ERROR, "Effective Date", "field"));
        }
        String reason = ParamUtil.getRequestString(httpServletRequest, i + "reason" + j);
        if (StringUtil.isEmpty(reason)) {
            errorMap.put(i + REASON + j, MessageUtil.replaceMessage(ERROR, "Cessation Reasons", "field"));
        }
        String patRadio = ParamUtil.getRequestString(httpServletRequest, i + PATRADIO + j);
        if (StringUtil.isEmpty(patRadio)) {
            errorMap.put(i + PATRADIO + j, MessageUtil.replaceMessage(ERROR, "Patients' Record will be transferred", "field"));
        }
        String readInfo = ParamUtil.getRequestString(httpServletRequest, READINFO);
        if (StringUtil.isEmpty(readInfo)) {
            errorMap.put(i + READINFO + j, ERROR);
        }
        String cessationReason = ParamUtil.getRequestString(httpServletRequest, i + REASON + j);
        String otherReason = ParamUtil.getRequestString(httpServletRequest, i + OTHERREASON + j);
        String patientSelect = ParamUtil.getRequestString(httpServletRequest, i + PATIENTSELECT + j);
        String patNoRemarks = ParamUtil.getRequestString(httpServletRequest, i + PATNOREMARKS + j);
        String patHciName = ParamUtil.getRequestString(httpServletRequest, i + PATHCINAME + j);
        String patRegNo = ParamUtil.getRequestString(httpServletRequest, i + PATREGNO + j);
        String patOthers = ParamUtil.getRequestString(httpServletRequest, i + PATOTHERS + j);
        if (ApplicationConsts.CESSATION_REASON_OTHER.equals(cessationReason)) {
            if (StringUtil.isEmpty(otherReason)) {
                errorMap.put(i + OTHERREASON + j, MessageUtil.replaceMessage(ERROR, "Others", "field"));
            }
        }
        if ("yes".equals(patRadio) && StringUtil.isEmpty(patientSelect)) {
            errorMap.put(i + PATIENTSELECT + j, MessageUtil.replaceMessage(ERROR, "Who will take over your patients' case records", "field"));
        }
        if ("yes".equals(patRadio) && !StringUtil.isEmpty(patientSelect)) {
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && StringUtil.isEmpty(patHciName)) {
                errorMap.put(i + PATHCINAME + j, MessageUtil.replaceMessage(ERROR, "Healthcare Institution Name", "field"));
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && !StringUtil.isEmpty(patHciName)) {
                List<String> hciName = cessationBeService.listHciName();
                if (!hciName.contains(patHciName)) {
                    errorMap.put(i + "patHciName" + j, "Healthcare Institution Name cannot be found.");
                }
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect) && StringUtil.isEmpty(patRegNo)) {
                errorMap.put(i + PATREGNO + j, MessageUtil.replaceMessage(ERROR, "Professional Regn. No.", "field"));
            }else if(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect) && !StringUtil.isEmpty(patRegNo)){
                if("Y".equals(prsFlag)){
                    ProfessionalParameterDto professionalParameterDto = new ProfessionalParameterDto();
                    List<String> prgNos = IaisCommonUtils.genNewArrayList();
                    prgNos.add(patRegNo);
                    professionalParameterDto.setRegNo(prgNos);
                    professionalParameterDto.setClientId("22222");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    String format = simpleDateFormat.format(new Date());
                    professionalParameterDto.setTimestamp(format);
                    professionalParameterDto.setSignature("2222");
                    HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                    HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                    try {
                        List<ProfessionalResponseDto> professionalResponseDtos = beEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(), signature.authorization(),
                                signature2.date(), signature2.authorization()).getEntity();
                        if(!IaisCommonUtils.isEmpty(professionalResponseDtos)){
                            List<String> specialty = professionalResponseDtos.get(0).getSpecialty();
                            if(IaisCommonUtils.isEmpty(specialty)){
                                errorMap.put(i + PATREGNO + j, "GENERAL_ERR0042");
                            }
                        }
                    }catch (Throwable e){
                        bpc.request.setAttribute("PRS_SERVICE_DOWN","PRS_SERVICE_DOWN");

                    }
                }

            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patientSelect) && StringUtil.isEmpty(patOthers)) {
                errorMap.put(i + PATOTHERS + j, MessageUtil.replaceMessage(ERROR, "Others", "field"));
            }
        }
        if ("no".equals(patRadio) && StringUtil.isEmpty(patNoRemarks)) {
            errorMap.put(i + PATNOREMARKS + j, MessageUtil.replaceMessage(ERROR, "Reason for no patients' records transfer", "field"));
        }
        //max length
        return errorMap;
    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, "Not Profitable");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, "Reduce Workload");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_REASON_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    private List<SelectOption> getPatientsOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, "Healthcare Institution");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, "Professional Regn. No.");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }
}
