package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.*;

/**
 * @author weilu
 * @date 2020/2/16 13:12
 */
@Delegator("ReCessationApplication")
@Slf4j
public class ReCessationApplicationDelegator {

    @Autowired
    private CessationService cessationService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private TaskService taskService;

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
//        List<AppCessationDto> cessationByIds = cessationService.getCessationByIds(licIds);
//        AppCessationDto appCessationDto = cessationByIds.get(0);
        AppCessationDto appCessationDto = new AppCessationDto();
        appCessationDto.setEffectiveDate(new Date());
        appCessationDto.setPatientSelect("hciName");
        appCessationDto.setReason("Low");
        appCessationDto.setPatHciName("sss");
        AppCessationDto appCessationDto1 = new AppCessationDto();
        appCessationDto1.setEffectiveDate(new Date());
        appCessationDto1.setPatientSelect("hciName");
        appCessationDto1.setReason("Low");
        appCessationDto1.setPatHciName("sss");
        List<AppCessationDto> cessationByIds= new ArrayList<>();
        cessationByIds.add(appCessationDto);
        cessationByIds.add(appCessationDto1);


        int size = appCessDtosByLicIds.size();
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        String text1 = "(1). The licensee must notify the Director of Medical Services in writing at least 30 days before the cessation of operation," +
                " letting, sale or disposal of his private hospital, medical clinic or clinical laboratory.";
        String text2 = "(2). Any licensee of a licensed healthcare institution (For e.g a medical clinic) who intends to cease operating the medical clinic" +
                " shall take all measures as are reasonable and necessary to ensure that the medical records of every patient are " +
                "properly transferred to the medical clinic or other healthcare institution to which such patient is to be transferred.";
        ParamUtil.setSessionAttr(bpc.request, "appCessDtosByLicIds", (Serializable)appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable)cessationByIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "text1", text1);
        ParamUtil.setSessionAttr(bpc.request, "text2", text2);
        ParamUtil.setSessionAttr(bpc.request, "size", size);

    }

    public void prepareData(BaseProcessClass bpc){

    }

    public void valiant(BaseProcessClass bpc){
        int size = (int)ParamUtil.getSessionAttr(bpc.request, "size");
        List<AppCessationDto> appCessationDtos = prepareDataForValiant(bpc,size);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable)appCessationDtos);
        int i = 0;
        if(appCessationDtos.size()==0){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
        List<String> listLicIds = new ArrayList<>();
        for(AppCessationDto appCessationDto :appCessationDtos){
            String licId = appCessationDto.getWhichTodo();
            if(!StringUtil.isEmpty(licId)){
                i++;
                listLicIds.add(licId);
                Map<String, String> errorMap = new HashMap<>(34);
                ValidationResult validationResult = WebValidationHelper.validateProperty(appCessationDto, "save");
                if (validationResult.isHasErrors()) {
                    errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                    return;
                }
            }
        }
        List<AppCessLicDto> appCessConDtos = cessationService.getAppCessDtosByLicIds(listLicIds);
        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable)appCessConDtos);
        List<AppCessationDto> appCessationDtosConfirm = prepareDataForConfirm(bpc,i);
        ParamUtil.setSessionAttr(bpc.request, "appCessationConfirmDto", (Serializable)appCessationDtosConfirm);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void action(BaseProcessClass bpc){
        String action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if("submit".equals(action_type)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        }else if("back".equals(action_type)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }

    }

    public void saveData(BaseProcessClass bpc){
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>)ParamUtil.getSessionAttr(bpc.request, "appCessationConfirmDto");
        cessationService.saveCessations(appCessationDtos);
        List<AppCessatonConfirmDto> appCessationDtosConfirms = new ArrayList<>();
        for(AppCessationDto appCessationDto :appCessationDtos){
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
        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable)appCessationDtosConfirms);

    }


    /*

     */

    private List<AppCessationDto> prepareDataForValiant(BaseProcessClass bpc,int size){
        List<AppCessationDto> appCessationDtos = new ArrayList<>();
        for (int i = 1; i <=size ; i++) {
            String whichTodo = ParamUtil.getRequestString(bpc.request, i+"whichTodo");
            if(!StringUtil.isEmpty(whichTodo)){
                AppCessationDto appCessationDto = new AppCessationDto();
                String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i+"effectiveDate");
                Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
                String cessationReason = ParamUtil.getRequestString(bpc.request, i+"cessationReason");
                String otherReason = ParamUtil.getRequestString(bpc.request, i+"otherReason");
                String patRadio = ParamUtil.getRequestString(bpc.request, i+"patRadio");
                Boolean patNeedTrans = false;
                if("yes".equals(patRadio)){
                    patNeedTrans = true;
                }
                String patientSelect = ParamUtil.getRequestString(bpc.request, i+"patientSelect");
                String patNoRemarks = ParamUtil.getRequestString(bpc.request, i+"patNoRemarks");
                String patHciName = ParamUtil.getRequestString(bpc.request, i+"patHciName");
                String patRegNo = ParamUtil.getRequestString(bpc.request, i+"patRegNo");
                String patOthers = ParamUtil.getRequestString(bpc.request, i+"patOthers");
                String readInfo = ParamUtil.getRequestString(bpc.request, "readInfo");

                appCessationDto.setEffectiveDate(effectiveDate);
                appCessationDto.setReason(cessationReason);
                appCessationDto.setOtherReason(otherReason);
                appCessationDto.setPatNeedTrans(patNeedTrans);
                appCessationDto.setPatientSelect(patientSelect);
                appCessationDto.setPatNoRemarks(patNoRemarks);
                appCessationDto.setPatHciName(patHciName);
                appCessationDto.setPatRegNo(patRegNo);
                appCessationDto.setPatOthers(patOthers);
                appCessationDto.setWhichTodo(whichTodo);
                appCessationDto.setReadInfo(readInfo);
                appCessationDtos.add(appCessationDto);
            }
        }
        return appCessationDtos;
    }

    private List<AppCessationDto> prepareDataForConfirm(BaseProcessClass bpc,int j){
        List<AppCessationDto> appCessationDtos = new ArrayList<>();
        for (int i = 1; i <=j ; i++) {
            AppCessationDto appCessationDto = new AppCessationDto();
            String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i+"effectiveDate");
            Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
            String cessationReason = ParamUtil.getRequestString(bpc.request, i+"cessationReason");
            String otherReason = ParamUtil.getRequestString(bpc.request, i+"otherReason");
            String patRadio = ParamUtil.getRequestString(bpc.request, i+"patRadio");
            Boolean patNeedTrans = false;
            if("yes".equals(patRadio)){
                patNeedTrans = true;
            }
            String patientSelect = ParamUtil.getRequestString(bpc.request, i+"patientSelect");
            String patNoRemarks = ParamUtil.getRequestString(bpc.request, i+"patNoRemarks");
            String patHciName = ParamUtil.getRequestString(bpc.request, i+"patHciName");
            String patRegNo = ParamUtil.getRequestString(bpc.request, i+"patRegNo");
            String patOthers = ParamUtil.getRequestString(bpc.request, i+"patOthers");
            String whichTodo = ParamUtil.getRequestString(bpc.request, i+"whichTodo");
            String readInfo = ParamUtil.getRequestString(bpc.request, "readInfo");

            appCessationDto.setEffectiveDate(effectiveDate);
            appCessationDto.setReason(cessationReason);
            appCessationDto.setOtherReason(otherReason);
            appCessationDto.setPatNeedTrans(patNeedTrans);
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