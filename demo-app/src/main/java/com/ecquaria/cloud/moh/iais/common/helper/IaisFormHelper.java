package com.ecquaria.cloud.moh.iais.common.helper;

import com.ecquaria.cloud.ServerConfig;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.util.StringUtil;
import com.ecquaria.egov.core.agency.Agency;
import com.ecquaria.egov.core.agency.AgencyService;
import com.ecquaria.egov.core.common.constants.AppConstants;
import com.ecquaria.egov.core.svcreg.ServiceRegistry;
import com.ecquaria.egov.mc.service.JsonLabel;
import com.ecquaria.egp.api.Applicant;
import com.ecquaria.egp.api.EGPConstants;
import com.ecquaria.egp.api.MessageCenterHelper;
import com.ecquaria.egp.api.Submitter;
import com.ecquaria.egp.core.application.Application;
import com.ecquaria.egp.core.bat.AppStatusHelper;
import com.ecquaria.egp.core.bat.FormHelper;
import com.ecquaria.egp.core.forms.instance.FormInstance;
import com.ecquaria.egp.core.service.security.Base64;
import ecq.commons.exception.BaseException;
import ecq.commons.exception.BaseRuntimeException;
import sop.config.ConfigUtil;
import sop.i18n.MultiLangUtil;
import sop.webflow.eservice.EGPCase;
import sop.webflow.eservice.EGPCaseData;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class IaisFormHelper extends FormHelper {
    public static final String ATTR_APP_DRAFT_NO = "egp.app.draft.no";
    public static final String FORM_NAME = "formName";
    public static final String DRAFT = "Draft";
    public static void doSaveDraft(BaseProcessClass bpc,String projectName,String processName,String callStepName)throws BaseException{
        String draftAppNo = getApplicationDraftNo(bpc.currentCase);
        boolean flag = false;
        Application app = bindApplication(bpc);

        if (StringUtil.isEmpty(draftAppNo)) {
            flag = true;
            setApplicationDraftNo(bpc.currentCase, app.getAppNo());
        } else {
            app.setAppNo(draftAppNo);
        }

        IaisEGPCaseHelper.saveEGPCase(bpc.currentCase);
        if (flag) {
            saveAppToMC(bpc, app,projectName,processName,callStepName);
        } else {
            String formDetailUrl = getFormDetailUrl(bpc);
            String tinyCallback = null;
            String formName = bpc.request.getParameter(FORM_NAME);
            StringBuilder callback = new StringBuilder(
                    ServerConfig.getInstance().getFrontendURL() + EngineHelper.getContextPath());
            callback.append("/eservice/").append(projectName).append("/").append(processName).append("/").append(callStepName)
                    .append("?caseid=").append(bpc.currentCase.getCaseId()).append("&formname=").append(formName);

            tinyCallback = IaisEGPHelper.getTinyUrl(callback.toString());

            Map<String, Object> map = new HashMap<>();
            map.put(JsonLabel.APP_STATUSMSG, DRAFT);
            map.put(JsonLabel.APP_STATUS, DRAFT);
            map.put(JsonLabel.SVC_CALLBACK_URL, tinyCallback);
            map.put(JsonLabel.FORM_HTML, getBase64FormHtml(formName, bpc));
            map.put(JsonLabel.FORM_DETAILS_URL, formDetailUrl);
            try {
                MessageCenterHelper.updateApplication(app, map);
            } catch (Exception e) {
                throw new BaseException(e);
            }
        }

        bpc.request.setAttribute("successmsg", MultiLangUtil.translate(bpc.request, AppConstants.KEY_TRANSLATION_MODULE_MESSAGE, "DraftSaveSuccess", "Draft Form data Saved Successfully."));

    }
    public static void deleteDraft(BaseProcessClass bpc)throws BaseException{
        String draftAppNo = getApplicationDraftNo(bpc.currentCase);
        Applicant applicant = getApplication(bpc.currentCase);
        if (applicant != null && !StringUtil.isEmpty(draftAppNo)) {
            try {
                MessageCenterHelper.deleteApplication(applicant.getUserId(), draftAppNo);
            } catch (Exception e) {
                throw new BaseException(e);
            }
        }
    }
    private static Applicant getApplication(EGPCase egpcase) {
        EGPCaseData caseData = egpcase.getCaseData(EGPConstants.APPLICANT);
        if (null == caseData || !(caseData.getValue() instanceof Applicant)) {
            return null;
        }
        return (Applicant) caseData.getValue();
    }
    public static void saveAppToMC(BaseProcessClass bpc,Application app,String projectName,String processName,String callStepName)  {
        if (!MessageCenterHelper.isMCEnabled()) {
            return;
        }

        String tinyCallback = null;
        String formName = bpc.request.getParameter(FORM_NAME);

        StringBuilder callback = new StringBuilder(
                ServerConfig.getInstance().getFrontendURL() + EngineHelper.getContextPath());
        callback.append("/eservice/").append(projectName).append("/").append(processName).append("/").append(callStepName)
                .append("?caseid=").append(bpc.currentCase.getCaseId());

        tinyCallback = IaisEGPHelper.getTinyUrl(callback.toString());
        String formDetailUrl = getFormDetailUrl(bpc);
        Map<String, Object> propMap = new HashMap<String, Object>();
        propMap.put(JsonLabel.SVC_CALLBACK_URL, tinyCallback);
        propMap.put(JsonLabel.APP_STATUS, DRAFT);

        propMap.put(JsonLabel.FORM_DETAILS_URL, formDetailUrl);
        propMap.put(JsonLabel.FORM_HTML, getBase64FormHtml(formName,bpc));
        MessageCenterHelper.ResponseInfo resInfo = null;
        try {
            resInfo = MessageCenterHelper.saveApplication(app, propMap);
        } catch (Exception e) {
            throw new BaseRuntimeException(
                    "Can't save application to message center: "
                            + e.getMessage(), e);
        }

        if (!resInfo.isSuccessful()) {
            throw new BaseRuntimeException(
                    "Error occurs when saving application to message center: "
                            + resInfo.getDevMessage());
        }
    }
    private static String getBase64FormHtml(String formName,BaseProcessClass bpc) {
        FormInstance formIns = IaisFormHelper.getFormInstanceFromCase(bpc.currentCase, formName);
        if (null == formIns) {
            // no submitted form found.
            return null;
        }

        try {
            return Base64.encodeToString(IaisFormHelper.getViewFormHtml(formIns).getBytes(), false);
        } catch (Exception e) {
            throw new BaseRuntimeException("Form html can not be generated: "
                    + e.getMessage(), e);
        }
    }
    public static String getFormDetailUrl(BaseProcessClass bpc) {
        StringBuilder mcCallback = new StringBuilder();
        mcCallback.append(ServerConfig.getInstance().getFrontendURL() + ConfigUtil.getString(AppConstants.CONFIG_FORM_DETAILS_URL));
        if (mcCallback.indexOf("?") > 0) {
            mcCallback.append("&");
        } else {
            mcCallback.append("?");
        }
        mcCallback.append("caseid=").append(bpc.currentCase.getCaseId())
                .append("&formname=").append(bpc.request.getParameter(FORM_NAME));
        return mcCallback.toString();
    }
    public static void setApplicationDraftNo(EGPCase egpcase, String appDraftNo) {
        if (egpcase != null) {
            egpcase.putCaseData(new EGPCaseData(ATTR_APP_DRAFT_NO,
                    appDraftNo));
        }
    }
    public static String getApplicationDraftNo(EGPCase egpcase) {
        if (egpcase != null) {
            EGPCaseData ecd = egpcase.getCaseData(ATTR_APP_DRAFT_NO);
            if (ecd == null)
                return null;
            else
                return ecd.getValue() instanceof String ? (String) ecd.getValue() : null;
        }
        return null;
    }
    private static Application bindApplication(BaseProcessClass bpc)  {
        ServiceRegistry svc = loadServiceInfo(bpc);

        Application app = new Application();
        app.setAppNo(generateAppNo());
        app.setSvcName(svc.getServiceName());
        app.setSvcId(svc.getServiceId());
        Agency agency = AgencyService.getInstance().retrieveByShortName(
                svc.getAgencyShortName());
        app.setAgcId(agency.getAgencyId());
        app.setAgcName(agency.getName());
        app.setDateSubmitted(new Date());
        app.setCreatedDate(new Date());
        app.setUpdatedDate(new Date());
        app.setAppStatus(AppStatusHelper.getInstance().getStartStatus()
                .getCode());

        EGPCaseData submitterData = bpc.currentCase
                .getCaseData(EGPConstants.SUBMITTER);
        if (submitterData != null
                && submitterData.getValue() instanceof Submitter) {
            Submitter submitter = (Submitter) submitterData.getValue();
            app.setSubmitterDomain(submitter.getUserDomain());
            app.setSubmitterId(submitter.getUserId());
            app.setSubmitterEmail(submitter.getEmail());
            app.setSubmitterMobile(submitter.getMobileNo());
            app.setSubmitterTel(submitter.getTel());
            app.setSubmitterName(submitter.getUserName());
            app.setSubmitterAddress(submitter.getMailAddress());
            app.setSubmitterMailAddress(submitter.getMailAddress());
        }

        EGPCaseData applicantData = bpc.currentCase
                .getCaseData(EGPConstants.APPLICANT);
        if (applicantData != null
                && applicantData.getValue() instanceof Applicant) {
            Applicant applicant = (Applicant) applicantData.getValue();
            app.setApplicantDomain(applicant.getUserDomain());
            app.setApplicantId(applicant.getUserId());
            app.setApplicantEmail(applicant.getEmail());
            app.setApplicantMobile(applicant.getMobileNo());
            app.setApplicantTel(applicant.getTel());
            app.setApplicantName(applicant.getUserName());
            app.setApplicantAddress(applicant.getMailAddress());
            app.setApplicantMailAddress(applicant.getMailAddress());
        }
        return app;
    }
    private static String generateAppNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MMddHHmmssSSS", Locale.US);
        return "draft-suocheng-test-app-" + format.format(new Date());
    }
    private static ServiceRegistry loadServiceInfo(BaseProcessClass bpc) {
        EGPCaseData caseData = bpc.currentCase.getCaseData(EGPConstants.ATTR_SERVICE_INFO);
        if (null == caseData) {
            throw new BaseRuntimeException("The eService has not been registered.");
        }

        return IaisServiceRegistryHelper.getServiceRegistryFromCase(bpc.currentCase);
    }


}
