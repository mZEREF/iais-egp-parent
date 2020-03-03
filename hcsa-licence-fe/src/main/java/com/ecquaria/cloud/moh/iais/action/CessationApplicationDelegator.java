package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.*;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplication")
@Slf4j
public class CessationApplicationDelegator {

    @Autowired
    private CessationService cessationService;
    @Autowired
    private ApplicationClient applicationClient;


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        AuditTrailHelper.auditFunction("Cessation Application", "Cessation Application");
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", null);
    }

    public void init(BaseProcessClass bpc) {
        List<String> licIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "licIds");
        if(licIds==null){
            licIds = new ArrayList<>();
            licIds.add("7ECAE165-534A-EA11-BE7F-000C29F371DC");
            licIds.add("CFCAC193-6F4D-EA11-BE7F-000C29F371DC");
        }
        List<AppCessLicDto> appCessDtosByLicIds = cessationService.getAppCessDtosByLicIds(licIds);
        int size = appCessDtosByLicIds.size();
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        String text1 = "(1). The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation," +
                " letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.";
        String text2 = "(2). Any licensee of a licensed healthcare institution (For e.g a medical clinic) who intends to cease operating the medical clinic" +
                " shall take all measures as are reasonable and necessary to ensure that the medical records of every patient are " +
                "properly transferred to the medical clinic or other healthcare institution to which such patient is to be transferred.";
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable) appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "text1", text1);
        ParamUtil.setSessionAttr(bpc.request, "text2", text2);
        ParamUtil.setSessionAttr(bpc.request, "size", size);

    }

    public void prepareData(BaseProcessClass bpc) {

    }

    public void valiant(BaseProcessClass bpc) {
        List<AppCessLicDto> appCessDtosByLicIds = (List<AppCessLicDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtos");
        int size = (int) ParamUtil.getSessionAttr(bpc.request, "size");
        List<AppCessLicDto> appCessHciDtos = prepareDataForValiant(bpc, size, appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable) appCessHciDtos);
        if (appCessHciDtos.size() == 0) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
        Map<String, String> errorMap = new HashMap<>(34);
        for (int i = 1; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + "whichTodo" + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    Map<String, String> validate = validate(bpc,i,j);
                    errorMap.putAll(validate);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }


        List<AppCessationDto> appCessationDtos = transformDto(appCessHciDtos);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtosSave", (Serializable)appCessationDtos);
    }

    public void action(BaseProcessClass bpc) {
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("submit".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        } else if ("back".equals(action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }

    }

    public void saveData(BaseProcessClass bpc) {
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtosSave");
        cessationService.saveCessations(appCessationDtos);
        List<AppCessatonConfirmDto> appCessationDtosConfirms = new ArrayList<>();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            String licId = appCessationDto.getWhichTodo();
            List<String> licIds = new ArrayList<>();
            licIds.add(licId);
            List<ApplicationDto> entity = applicationClient.getApplicationByLicId(licId).getEntity();
            List<AppCessLicDto> appCessDtosByLicIds = cessationService.getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            String licenceNo = appCessLicDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            String hciName = appCessLicDto.getAppCessHciDtos().get(0).getHciName();
            String hciAddress = appCessLicDto.getAppCessHciDtos().get(0).getHciAddress();
            String applicationNo = entity.get(0).getApplicationNo();
            Date effectiveDate = appCessationDto.getEffectiveDate();
            AppCessatonConfirmDto appCessatonConfirmDto = new AppCessatonConfirmDto();
            appCessatonConfirmDto.setAppNo(applicationNo);
            appCessatonConfirmDto.setEffectiveDate(effectiveDate);
            appCessatonConfirmDto.setHciAddress(hciAddress);
            appCessatonConfirmDto.setSvcName(svcName);
            appCessatonConfirmDto.setLicenceNo(licenceNo);
            appCessatonConfirmDto.setHciName(hciName);
            appCessationDtosConfirms.add(appCessatonConfirmDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable) appCessationDtosConfirms);
    }


    /*

     */

    private List<AppCessLicDto> prepareDataForValiant(BaseProcessClass bpc, int size, List<AppCessLicDto> appCessDtosByLicIds) {
        List<AppCessLicDto> appCessLicDtos = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(i - 1);
            List<AppCessHciDto> appCessHciDtoso = appCessLicDto.getAppCessHciDtos();
            int size1 = appCessHciDtoso.size();
            List<AppCessHciDto> appCessHciDtos = new ArrayList<>();
            for (int j = 1; j <= size1; j++) {
                AppCessHciDto appCessHciDto = appCessHciDtoso.get(j - 1);
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + "whichTodo" + j);
                if (!StringUtil.isEmpty(whichTodo)) {
                    String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i + "effectiveDate" + j);
                    Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
                    String reason = ParamUtil.getRequestString(bpc.request, i + "reason" + j);
                    String otherReason = ParamUtil.getRequestString(bpc.request, i + "otherReason" + j);
                    String patRadio = ParamUtil.getRequestString(bpc.request, i + "patRadio" + j);
                    Boolean patNeedTrans = false;
                    if ("yes".equals(patRadio)) {
                        patNeedTrans = true;
                    }
                    String patientSelect = ParamUtil.getRequestString(bpc.request, i + "patientSelect" + j);
                    String patNoRemarks = ParamUtil.getRequestString(bpc.request, i + "patNoRemarks" + j);
                    String patHciName = ParamUtil.getRequestString(bpc.request, i + "patHciName" + j);
                    String patRegNo = ParamUtil.getRequestString(bpc.request, i + "patRegNo" + j);
                    String patOthers = ParamUtil.getRequestString(bpc.request, i + "patOthers" + j);
                    String readInfo = ParamUtil.getRequestString(bpc.request, "readInfo");
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
                    appCessHciDto.setWhichTodo(whichTodo);
                    appCessHciDto.setReadInfo(readInfo);
                }
                appCessHciDtos.add(appCessHciDto);
            }
            appCessLicDto.setAppCessHciDtos(appCessHciDtos);
            appCessLicDtos.add(appCessLicDto);
        }
        return appCessLicDtos;
    }

    private List<AppCessationDto> transformDto(List<AppCessLicDto> appCessLicDtos) {
        List<AppCessationDto> appCessationDtos = new ArrayList<>();
        for (AppCessLicDto appCessLicDto : appCessLicDtos) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                String whichTodo = appCessHciDto.getWhichTodo();
                if(!StringUtil.isEmpty(whichTodo)){
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
                    appCessationDto.setWhichTodo(whichTodo);
                    appCessationDto.setReadInfo(readInfo);
                    appCessationDtos.add(appCessationDto);
                }
            }
        }
        return appCessationDtos;
    }

    private Map<String, String> validate(BaseProcessClass bpc,int i,int j) {
        HttpServletRequest httpServletRequest = bpc.request;
        Map<String, String> errorMap = new HashMap<>(34);

        String effectiveDateStr = ParamUtil.getRequestString(httpServletRequest, i + "effectiveDate" + j);
        if (StringUtil.isEmpty(effectiveDateStr)) {
            errorMap.put(i + "effectiveDate" + j, "ERR0009");
        }
        String reason = ParamUtil.getRequestString(httpServletRequest, i + "reason" + j);
        if (StringUtil.isEmpty(reason)) {
            errorMap.put(i + "reason" + j, "ERR0009");
        }
        String patRadio = ParamUtil.getRequestString(httpServletRequest, i + "patRadio" + j);
        if(StringUtil.isEmpty(patRadio)){
            errorMap.put(i + "patRadio" + j, "ERR0009");
        }
        String readInfo = ParamUtil.getRequestString(httpServletRequest, "readInfo");
        if (StringUtil.isEmpty(readInfo)) {
            errorMap.put(i + "readInfo" + j, "ERR0009");
        }

        String cessationReason = ParamUtil.getRequestString(httpServletRequest, i + "cessationReason" + j);
        String otherReason = ParamUtil.getRequestString(httpServletRequest, i + "otherReason" + j);
        String patientSelect = ParamUtil.getRequestString(httpServletRequest, i + "patientSelect" + j);
        String patNoRemarks = ParamUtil.getRequestString(httpServletRequest, i + "patNoRemarks" + j);
        String patHciName = ParamUtil.getRequestString(httpServletRequest, i + "patHciName" + j);
        String patRegNo = ParamUtil.getRequestString(httpServletRequest, i + "patRegNo" + j);
        String patOthers = ParamUtil.getRequestString(httpServletRequest, i + "patOthers" + j);
        if ("OtherReasons".equals(cessationReason)) {
            if (StringUtil.isEmpty(otherReason)) {
                errorMap.put(i + "otherReason" + j, "ERR0009");
            }
        }
        if ("yes".equals(patRadio)) {
            if (StringUtil.isEmpty(patientSelect)) {
                errorMap.put(i + "patientSelect" + j, "ERR0009");
            } else {
                if ("hciName".equals(patientSelect)) {
                    if (StringUtil.isEmpty(patHciName)) {
                        errorMap.put(i + "patHciName" + j, "ERR0009");
                    }
                } else if ("regNo".equals(patientSelect)) {
                    if (StringUtil.isEmpty(patRegNo)) {
                        errorMap.put(i + "patRegNo" + j, "ERR0009");
                    }
                } else if ("Others".equals(patientSelect)) {
                    if (StringUtil.isEmpty(patOthers)) {
                        errorMap.put(i + "patOthers" + j, "ERR0009");
                    }
                }
            }
        } else if ("no".equals(patRadio)) {
            if (StringUtil.isEmpty(patNoRemarks)) {
                errorMap.put(i + "patNoRemarks" + j, "ERR0009");
            }
        }
        return errorMap;
    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Low", "Not Profitable");
        SelectOption so2 = new SelectOption("Moderate", "Reduce Workloa");
        SelectOption so3 = new SelectOption("OtherReasons", "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    private List<SelectOption> getPatientsOption() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("hciName", "HCI Name");
        SelectOption so2 = new SelectOption("regNo", "Professional Regn No.");
        SelectOption so3 = new SelectOption("Others", "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

}
