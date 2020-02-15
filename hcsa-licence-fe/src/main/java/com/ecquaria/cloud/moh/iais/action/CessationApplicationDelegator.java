package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

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

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        AuditTrailHelper.auditFunction("Cessation Application", "Cessation Application");
        ParamUtil.setSessionAttr(bpc.request, "appCessationDto", null);
        ParamUtil.setSessionAttr(bpc.request, "appCessationConfirmDto", null);
    }

    public void init(BaseProcessClass bpc){
        List<String> licIds = new ArrayList<>();
        licIds.add("7ECAE165-534A-EA11-BE7F-000C29F371DC");
        licIds.add("CFCAC193-6F4D-EA11-BE7F-000C29F371DC");
        List<AppCessLicDto> appCessDtosByLicIds = cessationService.getAppCessDtosByLicIds(licIds);
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        String text1 = "(1). The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation, letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.";
        String text2 = "(2). Any licensee of a licensed healthcare institution (For e.g a medical clinic) who intends to cease operating the medical clinic shall take all measures as are reasonable and necessary to ensure that the medical records of every patient are properly transferred to the medical clinic or other healthcare institution to which such patient is to be transferred.";

        ParamUtil.setSessionAttr(bpc.request, "appCessDtosByLicIds", (Serializable)appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "text1", text1);
        ParamUtil.setSessionAttr(bpc.request, "text2", text2);

    }

    public void prepareData(BaseProcessClass bpc){

    }

    public void valiant(BaseProcessClass bpc){
        List<AppCessationDto> appCessationDtos = prepareDataForValiant(bpc);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable)appCessationDtos);
        for(AppCessationDto appCessationDto :appCessationDtos){
            if("on".equals(appCessationDto.getWhichTodo())){
                Map<String, String> errorMap = new HashMap<>(34);
                ValidationResult validationResult = WebValidationHelper.validateProperty(appCessationDto, "save");
                if (validationResult.isHasErrors()) {
                    errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    return;
                }
            }else{
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                return;
            }
        }
        AppCessationDto appCessationConfirmDto = prepareDataForConfirm(bpc);
        ParamUtil.setSessionAttr(bpc.request, "appCessationConfirmDto", appCessationConfirmDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);


    }

    public void action(BaseProcessClass bpc){
        String action_type = (String) ParamUtil.getSessionAttr(bpc.request, "action_type");
        if("submit".equals(action_type)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        }else if("back".equals(action_type)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }

    }

    public void saveData(BaseProcessClass bpc){
        AppCessationDto appCessationDto = (AppCessationDto)ParamUtil.getSessionAttr(bpc.request, "appCessationConfirmDto");
    }


    /*

     */

    private List<AppCessationDto> prepareDataForValiant(BaseProcessClass bpc){
        List<AppCessationDto> appCessationDtos = new ArrayList<>();
        for (int i = 1; i <3 ; i++) {
                AppCessationDto appCessationDto = new AppCessationDto();
                String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i+"effectiveDate");
                Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
                String cessationReason = ParamUtil.getRequestString(bpc.request, i+"cessationReason");
                String otherReason = ParamUtil.getRequestString(bpc.request, i+"otherReason");
                String patRadio = ParamUtil.getRequestString(bpc.request, i+"patRadio");
                String patientSelect = ParamUtil.getRequestString(bpc.request, i+"patientSelect");
                String patNoRemarks = ParamUtil.getRequestString(bpc.request, i+"patNoRemarks");
                String patHciName = ParamUtil.getRequestString(bpc.request, i+"patHciName");
                String patRegNo = ParamUtil.getRequestString(bpc.request, i+"patRegNo");
                String patOthers = ParamUtil.getRequestString(bpc.request, i+"patOthers");
                String whichTodo = ParamUtil.getRequestString(bpc.request, i+"whichTodo");
                String readInfo = ParamUtil.getRequestString(bpc.request, i+"readInfo");

                appCessationDto.setEffectiveDate(effectiveDate);
                appCessationDto.setCessationReason(cessationReason);
                appCessationDto.setOtherReason(otherReason);
                appCessationDto.setPatRadio(patRadio);
                appCessationDto.setPatientSelect(patientSelect);
                appCessationDto.setPatNoRemarks(patNoRemarks);
                appCessationDto.setPatHciName(patHciName);
                appCessationDto.setPatRegNo(patRegNo);
                appCessationDto.setPatOthers(patOthers);
                appCessationDto.setWhichTodo(whichTodo);
                appCessationDto.setReadInfo(readInfo);
                appCessationDtos.add(appCessationDto);
            }
        return appCessationDtos;
    }

    private AppCessationDto prepareDataForConfirm(BaseProcessClass bpc){
        AppCessationDto appCessationDto = new AppCessationDto();
        String effectiveDateStr = ParamUtil.getRequestString(bpc.request, "effectiveDate");
        Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
        String cessationReason = ParamUtil.getRequestString(bpc.request, "cessationReason");
        String otherReason = ParamUtil.getRequestString(bpc.request, "otherReason");
        String patRadio = ParamUtil.getRequestString(bpc.request, "patRadio");
        String patientSelect = ParamUtil.getRequestString(bpc.request, "patientSelect");
        String patNoRemarks = ParamUtil.getRequestString(bpc.request, "patNoRemarks");
        String patHciName = ParamUtil.getRequestString(bpc.request, "patHciName");
        String patRegNo = ParamUtil.getRequestString(bpc.request, "patRegNo");
        String patOthers = ParamUtil.getRequestString(bpc.request, "patOthers");
        String whichTodo = ParamUtil.getRequestString(bpc.request, "whichTodo");

        appCessationDto.setEffectiveDate(effectiveDate);
        appCessationDto.setCessationReason(cessationReason);
        appCessationDto.setOtherReason(otherReason);
        appCessationDto.setPatRadio(patRadio);
        appCessationDto.setPatientSelect(patientSelect);
        appCessationDto.setPatNoRemarks(patNoRemarks);
        appCessationDto.setPatHciName(patHciName);
        appCessationDto.setPatRegNo(patRegNo);
        appCessationDto.setPatOthers(patOthers);
        appCessationDto.setWhichTodo(whichTodo);
        return appCessationDto;

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
