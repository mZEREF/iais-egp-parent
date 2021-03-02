package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @date 2020/10/26 13:12
 */
@Delegator("CessationResubmit")
@Slf4j
public class ReCessationApplicationBeDelegator {

    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApplicationViewService applicationViewService;

    @Value("${moh.halp.prs.enable}")
    private String prsFlag;
    private static final String APPCESSATIONDTO = "appCess";
    private static final String READINFO = "readInfo";
    private static final String WHICHTODO = "whichTodo";
    private static final String TRANSFORMNO = "patNoConfirm";
    private static final String EFFECTIVEDATE = "effectiveDate";
    private static final String REASON = "reason";
    private static final String OTHERREASON = "otherReason";
    private static final String PATRADIO = "patRadio";
    private static final String PATIENTSELECT = "patientSelect";
    private static final String PATNOREMARKS = "patNoRemarks";
    private static final String PATHCINAME = "patHciName";
    private static final String PATREGNO = "patRegNo";
    private static final String PATOTHERS = "patOthers";
    private static final String PATOTHERSMOBILENO = "patOthersMobileNo";
    private static final String PATOTHERSEMAILADDRESS = "patOthersEmailAddress";
    private static final String ERROR = "GENERAL_ERR0006";


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfo", null);
        ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag", null);
        ParamUtil.setSessionAttr(bpc.request, "isGrpLic", null);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtoSave", null);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
    }

    public void init(BaseProcessClass bpc) throws Exception {
        String taskId = null;
        try {
            taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        } catch (MaskAttackException e) {
            log.error(e.getMessage(), e);
            bpc.response.sendRedirect("https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
        }
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CESSATION, AuditTrailConsts.FUNCTION_CESSATION);
        TaskDto taskDto = taskService.getTaskById(taskId);
        String refNo = taskDto.getRefNo();
        List<AppCessLicDto> appCessLicDtos = cessationBeService.initData(refNo);
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(refNo);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String originLicenceId = applicationDto.getOriginLicenceId();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        licIds.add(originLicenceId);
        List<String> specLicIds = cessationBeService.filtrateSpecLicIds(licIds);
        List<AppSpecifiedLicDto> specLicInfo = cessationBeService.getSpecLicInfo(licIds);
        if (specLicInfo.size() > 0) {
            Map<String, List<AppSpecifiedLicDto>> map = IaisCommonUtils.genNewHashMap();
            for (AppSpecifiedLicDto appSpecifiedLicDto : specLicInfo) {
                String specLicId = appSpecifiedLicDto.getSpecLicId();
                String baseLicNo = appSpecifiedLicDto.getBaseLicNo();
                if (!specLicIds.contains(specLicId)) {
                    List<AppSpecifiedLicDto> specLicInfoConfirmExist = map.get(baseLicNo);
                    if (!IaisCommonUtils.isEmpty(specLicInfoConfirmExist)) {
                        specLicInfoConfirmExist.add(appSpecifiedLicDto);
                    } else {
                        List<AppSpecifiedLicDto> specLicInfoConfirm = IaisCommonUtils.genNewArrayList();
                        specLicInfoConfirm.add(appSpecifiedLicDto);
                        map.put(baseLicNo, specLicInfoConfirm);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request, "specLicInfo", (Serializable) map);
            ParamUtil.setSessionAttr(bpc.request, "specLicInfoFlag", "exist");
        }
        List<SelectOption> reasonOption = getReasonOption();
        List<SelectOption> patientsOption = getPatientsOption();
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTO, appCessLicDtos.get(0));
        ParamUtil.setSessionAttr(bpc.request, "reasonOption", (Serializable) reasonOption);
        ParamUtil.setSessionAttr(bpc.request, "patientsOption", (Serializable) patientsOption);
        ParamUtil.setSessionAttr(bpc.request, READINFO, null);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void prepare(BaseProcessClass bpc) {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>CessationApplicationDelegator");
    }

    public void valiant(BaseProcessClass bpc) {
        AppCessLicDto appCessationDto = (AppCessLicDto) ParamUtil.getSessionAttr(bpc.request, APPCESSATIONDTO);
        String readInfo = ParamUtil.getRequestString(bpc.request, READINFO);
        ParamUtil.setSessionAttr(bpc.request, READINFO, readInfo);
        Map<String, String> errorMap = validate(bpc, appCessationDto);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public void action(BaseProcessClass bpc) {
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if ("submit".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
        } else if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }
    }

    public void saveData(BaseProcessClass bpc) throws FeignException {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AppCessationDto appCessationDto = (AppCessationDto) ParamUtil.getSessionAttr(bpc.request, "appCessationDtoSave");
        cessationBeService.saveRfiCessation(appCessationDto, taskDto, loginContext);
    }

    public void response(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries/1/check");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /*
        utils
     */

    private Map<String, String> validate(BaseProcessClass bpc, AppCessLicDto appCessLicDto) {
        AppCessHciDto appCessHciDto = appCessLicDto.getAppCessHciDtos().get(0);

        HttpServletRequest httpServletRequest = bpc.request;
        Map<String, String> errorMap = new HashMap<>(34);
        String effectiveDateStr = ParamUtil.getRequestString(httpServletRequest, EFFECTIVEDATE);
        if (StringUtil.isEmpty(effectiveDateStr)) {
            errorMap.put(EFFECTIVEDATE, MessageUtil.replaceMessage(ERROR, "Effective Date", "field"));
        }
        String reason = ParamUtil.getRequestString(httpServletRequest, "reason");
        if (StringUtil.isEmpty(reason)) {
            errorMap.put(REASON, MessageUtil.replaceMessage(ERROR, "Cessation Reasons", "field"));
        }
        String patRadio = ParamUtil.getRequestString(httpServletRequest, PATRADIO);
        if (StringUtil.isEmpty(patRadio)) {
            errorMap.put(PATRADIO, MessageUtil.replaceMessage(ERROR, "Patients' Record will be transferred", "field"));
        }
        String readInfo = ParamUtil.getRequestString(httpServletRequest, READINFO);
        if (StringUtil.isEmpty(readInfo)) {
            errorMap.put(READINFO, ERROR);
        }
        String cessationReason = ParamUtil.getRequestString(httpServletRequest, REASON);
        String otherReason = ParamUtil.getRequestString(httpServletRequest, "otherReason");
        String patientSelect = ParamUtil.getRequestString(httpServletRequest, PATIENTSELECT);
        String patNoRemarks = ParamUtil.getRequestString(httpServletRequest, PATNOREMARKS);
        String patHciName = ParamUtil.getRequestString(httpServletRequest, PATHCINAME);
        String patRegNo = ParamUtil.getRequestString(httpServletRequest, PATREGNO);
        String patOthers = ParamUtil.getRequestString(httpServletRequest, PATOTHERS);
        String patMobile = ParamUtil.getRequestString(httpServletRequest, PATOTHERSMOBILENO);
        String patEmailAddress = ParamUtil.getRequestString(httpServletRequest, PATOTHERSEMAILADDRESS);
        String patNoConfirm = ParamUtil.getRequestString(bpc.request, TRANSFORMNO);
        AppCessationDto appCessationDto = new AppCessationDto();
        Date date = DateUtil.parseDate(effectiveDateStr, "dd/MM/yyyy");
        appCessationDto.setEffectiveDate(date);
        appCessHciDto.setEffectiveDate(date);
        appCessationDto.setReason(reason);
        appCessHciDto.setReason(reason);
        appCessationDto.setOtherReason(otherReason);
        appCessHciDto.setOtherReason(otherReason);
        if ("yes".equals(patRadio)) {
            appCessationDto.setPatNeedTrans(Boolean.TRUE);
            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
        } else {
            appCessationDto.setPatNeedTrans(Boolean.FALSE);
            appCessHciDto.setPatNeedTrans(Boolean.FALSE);
        }
        if (!StringUtil.isEmpty(patHciName)) {
            PremisesDto premisesDto = cessationBeService.getPremiseByHciCodeName(patHciName);
            if (premisesDto != null) {
                String hciAddressPat = premisesDto.getHciAddress();
                String hciNamePat = premisesDto.getHciName();
                appCessHciDto.setHciNamePat(hciNamePat);
                appCessationDto.setHciNamePat(hciNamePat);
                appCessHciDto.setHciAddressPat(hciAddressPat);
                appCessationDto.setHciAddressPat(hciAddressPat);
            }
        }
        appCessationDto.setPatientSelect(patientSelect);
        appCessHciDto.setPatientSelect(patientSelect);
        appCessationDto.setPatHciName(patHciName);
        appCessHciDto.setPatHciName(patHciName);
        appCessationDto.setPatRegNo(patRegNo);
        appCessHciDto.setPatRegNo(patRegNo);
        appCessationDto.setPatOthers(patOthers);
        appCessHciDto.setPatOthers(patOthers);
        appCessationDto.setPatNoRemarks(patNoRemarks);
        appCessHciDto.setPatNoRemarks(patNoRemarks);
        appCessationDto.setMobileNo(patMobile);
        appCessHciDto.setMobileNo(patMobile);
        appCessationDto.setEmailAddress(patEmailAddress);
        appCessHciDto.setEmailAddress(patEmailAddress);
        appCessHciDto.setPatNoConfirm(patNoConfirm);
        ParamUtil.setSessionAttr(bpc.request, "appCessationDtoSave", appCessationDto);
        if (ApplicationConsts.CESSATION_REASON_OTHER.equals(cessationReason)) {
            if (StringUtil.isEmpty(otherReason)) {
                errorMap.put(OTHERREASON, MessageUtil.replaceMessage(ERROR, "Others", "field"));
            }
        }
        if ("yes".equals(patRadio) && StringUtil.isEmpty(patientSelect)) {
            errorMap.put(PATIENTSELECT, MessageUtil.replaceMessage(ERROR, "Who will take over your patients' case records", "field"));
        }
        if ("yes".equals(patRadio) && !StringUtil.isEmpty(patientSelect)) {
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && StringUtil.isEmpty(patHciName)) {
                errorMap.put(PATHCINAME, MessageUtil.replaceMessage(ERROR, "HCI Name", "field"));
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect) && !StringUtil.isEmpty(patHciName)) {
                List<String> hciName = cessationBeService.listHciName();
                if (!hciName.contains(patHciName)) {
                    errorMap.put("patHciName", "HCI Name cannot be found.");
                }
            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect) && StringUtil.isEmpty(patRegNo)) {
                errorMap.put(PATREGNO, MessageUtil.replaceMessage(ERROR, "Professional Regn. No.", "field"));
            } else if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect) && !StringUtil.isEmpty(patRegNo)) {
                if ("Y".equals(prsFlag)) {
                    try {
                        ProfessionalParameterDto professionalParameterDto = new ProfessionalParameterDto();
                        List<String> prgNos = IaisCommonUtils.genNewArrayList();
                        prgNos.add(patRegNo);
                        professionalParameterDto.setRegNo(prgNos);
                        professionalParameterDto.setClientId("22222");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        String format = simpleDateFormat.format(new Date());
                        professionalParameterDto.setTimestamp(format);
                        professionalParameterDto.setSignature("2222");
                        List<ProfessionalResponseDto> professionalResponseDtos = applicationClient.getProfessionalDetail(professionalParameterDto).getEntity();
                        if (!IaisCommonUtils.isEmpty(professionalResponseDtos)) {
                            List<String> specialty = professionalResponseDtos.get(0).getSpecialty();
                            if (IaisCommonUtils.isEmpty(specialty)) {
                                errorMap.put(PATREGNO, "GENERAL_ERR0042");
                            }
                        }
                    } catch (Throwable e) {
                        bpc.request.setAttribute("PRS_SERVICE_DOWN", "PRS_SERVICE_DOWN");
                    }
                }

            }
            if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patientSelect)) {
                if (StringUtil.isEmpty(patOthers)) {
                    errorMap.put(PATOTHERS, MessageUtil.replaceMessage(ERROR, "Others", "field"));
                }
                if (StringUtil.isEmpty(patMobile)) {
                    errorMap.put(PATOTHERSMOBILENO, MessageUtil.replaceMessage(ERROR, PATOTHERSMOBILENO, "field"));
                } else {
                    if (!patMobile.matches("^[8|9][0-9]{7}$")) {
                        errorMap.put(PATOTHERSMOBILENO, "GENERAL_ERR0007");
                    }
                }
                if (StringUtil.isEmpty(patEmailAddress)) {
                    errorMap.put(PATOTHERSEMAILADDRESS, MessageUtil.replaceMessage(ERROR, PATOTHERSEMAILADDRESS, "field"));
                } else {
                    if (!ValidationUtils.isEmail(patEmailAddress)) {
                        errorMap.put(PATOTHERSEMAILADDRESS, "GENERAL_ERR0014");
                    }
                }
            }
        }
        if ("no".equals(patRadio)) {
            if (StringUtil.isEmpty(patNoRemarks)) {
                errorMap.put(PATNOREMARKS, MessageUtil.replaceMessage(ERROR, "Reason for no patients' records transfer", "field"));
            }
            if (StringUtil.isEmpty(patNoConfirm)) {
                errorMap.put("patNoConfirm", MessageUtil.replaceMessage(ERROR, "Reason for no patients' records transfer", "field"));
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPCESSATIONDTO, appCessLicDto);
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
