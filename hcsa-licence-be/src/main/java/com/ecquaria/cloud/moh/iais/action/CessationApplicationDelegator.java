package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplicationBe")
@Slf4j
public class CessationApplicationDelegator {

    @Autowired
    private CessationService cessationService;
    @Autowired
    private ApplicationClient applicationClient;

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


    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        AuditTrailHelper.auditFunction("Cessation Application", "Cessation Application");
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, null);
    }

    public void init(BaseProcessClass bpc){
        List<String> licIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "licIds");
        if(licIds==null||licIds.size()==0){
            licIds = IaisCommonUtils.genNewArrayList();
            licIds.add("ACB51822-A656-EA11-BE7F-000C29F371DC");
            licIds.add("4083B3AD-B04D-EA11-BE7F-000C29F371DC");
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
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTOS, (Serializable) appCessDtosByLicIds);
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, "text1", text1);
        ParamUtil.setSessionAttr(bpc.request, "text2", text2);
        ParamUtil.setSessionAttr(bpc.request, "size", size);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
    }

    public void prepareData(BaseProcessClass bpc){
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>CessationApplicationDelegator");
    }

    public void valiant(BaseProcessClass bpc) throws IOException {
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("back".equals(actionType)) {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries");
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
        Boolean choose = false;
        for (int i = 1; i <=size ; i++) {
            for (int j = 1; j <= size; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
                if(!StringUtil.isEmpty(whichTodo)){
                    choose = true;
                }
            }
        }
        if(!choose){
            errorMap.put("choose", "Please select at least one licence");
        }
        if (confirmDtos.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                String whichTodo = ParamUtil.getRequestString(bpc.request, i + WHICHTODO + j);
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

        List<AppCessationDto> appCessationDtos = transformDto(cloneAppCessHciDtos);
        ParamUtil.setSessionAttr(bpc.request, "confirmDtos", (Serializable)confirmDtos);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtosSave", (Serializable)appCessationDtos);
    }

    public void action(BaseProcessClass bpc){
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if("submit".equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }else if("back".equals(actionType)){
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        }

    }

    public void saveData(BaseProcessClass bpc){
        List<AppCessationDto> appCessationDtos = (List<AppCessationDto>) ParamUtil.getSessionAttr(bpc.request, "appCessationDtosSave");
        cessationService.saveCessations(appCessationDtos);
        List<AppCessatonConfirmDto> appCessationDtosConfirms = IaisCommonUtils.genNewArrayList();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            String licId = appCessationDto.getWhichTodo();
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.add(licId);
            ApplicationDto applicationDto = applicationClient.getApplicationByLicId(licId).getEntity();
            List<AppCessLicDto> appCessDtosByLicIds = cessationService.getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            String licenceNo = appCessLicDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            String hciName = appCessLicDto.getAppCessHciDtos().get(0).getHciName();
            String hciAddress = appCessLicDto.getAppCessHciDtos().get(0).getHciAddress();
            String applicationNo = applicationDto.getApplicationNo();
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
        List<String> licNos = IaisCommonUtils.genNewArrayList();
        for(AppCessatonConfirmDto appCessatonConfirmDto :appCessationDtosConfirms){
            String licenceNo = appCessatonConfirmDto.getLicenceNo();
            Date effectiveDate = appCessatonConfirmDto.getEffectiveDate();
            if(effectiveDate.before(new Date())){
                licNos.add(licenceNo);
            }
        }
        if(!licNos.isEmpty()){
            cessationService.updateLicence(licNos);
        }

        ParamUtil.setSessionAttr(bpc.request, "appCessConDtos", (Serializable) appCessationDtosConfirms);
    }

    public void response(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /*

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
                if (!StringUtil.isEmpty(whichTodo)) {
                    String effectiveDateStr = ParamUtil.getRequestString(bpc.request, i + EFFECTIVEDATE + j);
                    Date effectiveDate = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
                    String reason = ParamUtil.getRequestString(bpc.request, i + REASON + j);
                    String otherReason = ParamUtil.getRequestString(bpc.request, i + OTHERREASON + j);
                    String patRadio = ParamUtil.getRequestString(bpc.request, i + PATRADIO + j);
                    Boolean patNeedTrans = null;
                    if ("yes".equals(patRadio)) {
                        patNeedTrans = true;
                    }else if("no".equals(patRadio)) {
                        patNeedTrans = false;
                    }
                    String patientSelect = ParamUtil.getRequestString(bpc.request, i + PATIENTSELECT + j);
                    String patNoRemarks = ParamUtil.getRequestString(bpc.request, i + PATNOREMARKS + j);
                    String patHciName = ParamUtil.getRequestString(bpc.request, i +PATHCINAME + j);
                    String patRegNo = ParamUtil.getRequestString(bpc.request, i + PATREGNO + j);
                    String patOthers = ParamUtil.getRequestString(bpc.request, i + PATOTHERS+ j);
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
                    appCessHciDto.setWhichTodo(whichTodo);
                    appCessHciDto.setReadInfo(readInfo);
                }else{
                    appCessHciDto.setWhichTodo(null);
                }
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
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if(appCessHciDtos!=null&&!appCessHciDtos.isEmpty()){
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String whichTodo = appCessHciDto.getWhichTodo();
                    if(!StringUtil.isEmpty(whichTodo)) {
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

        }
        return appCessationDtos;
    }

    private List<AppCessLicDto> getConfirmDtos(List<AppCessLicDto> appCessLicDtos){
        for(AppCessLicDto appCessLicDto :appCessLicDtos){
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if(appCessHciDtos!=null&&!appCessHciDtos.isEmpty()){
                List<AppCessHciDto> list = IaisCommonUtils.genNewArrayList();
                for (AppCessHciDto appCessHciDto :appCessHciDtos){
                    String whichTodo = appCessHciDto.getWhichTodo();
                    if(StringUtil.isEmpty(whichTodo)){
                        list.add(appCessHciDto);
                    }
                }
                appCessHciDtos.removeAll(list);
            }
        }
        for (int i = 0; i <appCessLicDtos.size() ; i++) {
            List<AppCessHciDto> appCessHciDtos = appCessLicDtos.get(i).getAppCessHciDtos();
            if(appCessHciDtos.size()==0){
                appCessLicDtos.remove(i);
                i = i - 1 ;
            }

        }
        return appCessLicDtos;
    }

    private Map<String, String> validate(BaseProcessClass bpc,int i,int j) {
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
        if(StringUtil.isEmpty(patRadio)){
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
        String patHciName = ParamUtil.getRequestString(httpServletRequest, i + PATHCINAME+ j);
        String patRegNo = ParamUtil.getRequestString(httpServletRequest, i + PATREGNO + j);
        String patOthers = ParamUtil.getRequestString(httpServletRequest, i + PATOTHERS + j);
        if (ApplicationConsts.CESSATION_REASON_OTHER.equals(cessationReason)) {
            if (StringUtil.isEmpty(otherReason)) {
                errorMap.put(i + OTHERREASON + j, ERROR);
            }
        }
        if ("yes".equals(patRadio)&&StringUtil.isEmpty(patientSelect)) {
                errorMap.put(i + PATIENTSELECT + j, ERROR);
            }
        if ("yes".equals(patRadio)&&!StringUtil.isEmpty(patientSelect)) {
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect)&&StringUtil.isEmpty(patHciName)) {
                errorMap.put(i + PATHCINAME + j, ERROR);
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect)&&StringUtil.isEmpty(patRegNo)) {
                errorMap.put(i + PATREGNO + j, ERROR);
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patientSelect)&&StringUtil.isEmpty(patOthers)) {
                errorMap.put(i + PATOTHERS + j, ERROR);
            }
        }
        if ("no".equals(patRadio)&&StringUtil.isEmpty(patNoRemarks)) {
                errorMap.put(i + PATNOREMARKS + j,ERROR);
        }
        return errorMap;
    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, "Not Profitable");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, "Reduce Workloa");
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
