package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplication")
@Slf4j
public class CessationApplicationFeDelegator {

    @Autowired
    private CessationFeService cessationFeService;

    private static final String APPCESSATIONDTOS ="appCessationDtos";
    private static final String READINFO ="readInfo";
    private static final String WHICHTODO ="whichTodo";
    private static final String EFFECTIVEDATE ="effectiveDate";
    private static final String REASON ="reason";
    private static final String OTHERREASON ="otherReason";
    private static final String PATRADIO ="patRadio";
    private static final String PATIENTSELECT ="patientSelect";
    private static final String PATNOREMARKS ="patNoRemarks";
    private static final String PATHCINAME ="patHciName";
    private static final String PATREGNO ="patRegNo";
    private static final String PATOTHERS ="patOthers";
    private static final String ERROR ="ERR0009";


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        AuditTrailHelper.auditFunction("Cessation Application", "Cessation Application");
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfo", null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag",null);
    }

    public void init(BaseProcessClass bpc) {
        List<String> licIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "licIds");
        if(licIds==null){
            licIds = IaisCommonUtils.genNewArrayList();
            licIds.add("4176FF73-895E-EA11-BE7F-000C29F371DC");
        }
        List<AppCessLicDto> appCessDtosByLicIds = cessationFeService.getAppCessDtosByLicIds(licIds);
        List<AppSpecifiedLicDto> specLicInfo = cessationFeService.getSpecLicInfo(licIds);
        if(specLicInfo.size()>0) {
            ParamUtil.setSessionAttr(bpc.request, "specLicInfo", (Serializable) specLicInfo);
            ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag","exist");
        }
        int size = appCessDtosByLicIds.size();
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "size", size);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
    }

    public void prepareData(BaseProcessClass bpc) {
    }

    public void valiant(BaseProcessClass bpc) throws IOException {
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("back".equals(action_type)) {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
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
        Boolean choose = Boolean.FALSE;
        for (int i = 1; i <= size; i++) {
            int size1 = appCessDtosByLicIds.get(i-1).getAppCessHciDtos().size();
            for (int j = 1; j <=size1 ; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    choose = Boolean.TRUE;
                    break;
                }
            }
        }
        if (!choose) {
            errorMap.put("choose", "Please select at least one licence");
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
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }

        List<AppCessationDto> appCessationDtos = transformDto(cloneAppCessHciDtos);
        ParamUtil.setSessionAttr(bpc.request, "confirmDtos", (Serializable) confirmDtos);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtosSave", (Serializable) appCessationDtos);
    }

    public void action(BaseProcessClass bpc) throws IOException {
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("submit".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        } else if ("back".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }
    }

    public void saveData(BaseProcessClass bpc) throws Exception {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtosSave");
        List<String> appIds = cessationFeService.saveCessations(appCessationDtos,loginContext);
        List<AppCessatonConfirmDto> confirmDto = cessationFeService.getConfirmDto(appCessationDtos, appIds, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable)confirmDto);
    }

    public void response(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /*
     * utils
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
//                if (!StringUtil.isEmpty(whichTodo)) {
                    String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i + EFFECTIVEDATE + j);
                    Date effectiveDate = DateUtil.parseDate(effectiveDateStr, AppConsts.DEFAULT_DATE_FORMAT);
                    String reason = ParamUtil.getRequestString(bpc.request, i + REASON + j);
                    String otherReason = ParamUtil.getRequestString(bpc.request, i + OTHERREASON + j);
                    String patRadio = ParamUtil.getRequestString(bpc.request, i + PATRADIO + j);
                    Boolean patNeedTrans = null;
                    if ("yes".equals(patRadio)) {
                        patNeedTrans = Boolean.TRUE;
                    } else if ("no".equals(patRadio)) {
                        patNeedTrans = Boolean.FALSE;
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
//                } else {
//                    appCessHciDto.setPremiseIdChecked(null);
//                }
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
            errorMap.put(i + EFFECTIVEDATE + j, ERROR);
        }
        String reason = ParamUtil.getRequestString(httpServletRequest, i + "reason" + j);
        if (StringUtil.isEmpty(reason)) {
            errorMap.put(i + REASON + j, ERROR);
        }
        String patRadio = ParamUtil.getRequestString(httpServletRequest, i + PATRADIO + j);
        if (StringUtil.isEmpty(patRadio)) {
            errorMap.put(i + PATRADIO + j, ERROR);
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
                errorMap.put(i + OTHERREASON + j, ERROR);
            }
        }
        if ("yes".equals(patRadio) && StringUtil.isEmpty(patientSelect)) {
            errorMap.put(i + PATIENTSELECT + j, ERROR);
        }
        if ("yes".equals(patRadio) && !StringUtil.isEmpty(patientSelect)) {
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && StringUtil.isEmpty(patHciName)) {
                errorMap.put(i + PATHCINAME + j, ERROR);
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && !StringUtil.isEmpty(patHciName)) {
                List<String> hciName = cessationFeService.listHciName();
                if (!hciName.contains(patHciName)) {
                    errorMap.put(i + "patHciName" + j, "HCI Name cannot be found.");
                }
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect) && StringUtil.isEmpty(patRegNo)) {
                errorMap.put(i + PATREGNO + j, ERROR);
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patientSelect) && StringUtil.isEmpty(patOthers)) {
                errorMap.put(i + PATOTHERS + j, ERROR);
            }
        }
        if ("no".equals(patRadio) && StringUtil.isEmpty(patNoRemarks)) {
            errorMap.put(i + PATNOREMARKS + j, ERROR);
        }
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
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, "HCI");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, "Professional Regn No.");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

}
