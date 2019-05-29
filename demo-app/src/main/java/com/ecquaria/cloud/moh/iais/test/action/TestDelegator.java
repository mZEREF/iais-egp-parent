package com.ecquaria.cloud.moh.iais.test.action;

import com.ecquaria.cloud.ServerConfig;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.helper.IaisEGPCaseHelper;
import com.ecquaria.cloud.moh.iais.common.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.common.helper.IaisFormHelper;
import com.ecquaria.cloud.moh.iais.common.helper.IaisServiceRegistryHelper;
import com.ecquaria.cloud.moh.iais.common.util.StringUtil;
import com.ecquaria.cloud.moh.iais.test.validate.TestValidate;
import com.ecquaria.cloud.payment.PaymentService;
import com.ecquaria.egov.core.agency.Agency;
import com.ecquaria.egov.core.agency.AgencyService;
import com.ecquaria.egov.core.common.constants.AppConstants;
import com.ecquaria.egov.core.egpcase.EGPCaseWrapper;
import com.ecquaria.egov.core.svcreg.ServiceRegistry;
import com.ecquaria.egov.mc.service.JsonLabel;
import com.ecquaria.egp.api.Applicant;
import com.ecquaria.egp.api.EGPConstants;
import com.ecquaria.egp.api.MessageCenterHelper;
import com.ecquaria.egp.api.Submitter;
import com.ecquaria.egp.core.application.Application;
import com.ecquaria.egp.core.bat.AppStatusHelper;
import com.ecquaria.egp.core.forms.instance.FormInstance;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import com.ecquaria.egp.core.payment.Payment;
import com.ecquaria.egp.core.payment.PaymentData;
import com.ecquaria.egp.core.service.security.Base64;
import ecq.commons.exception.BaseException;
import ecq.commons.exception.BaseRuntimeException;
import com.ecquaria.cloud.moh.iais.common.IFormValidator;
import sop.config.ConfigUtil;
import sop.i18n.MultiLangUtil;
import sop.webflow.eservice.EGPCase;
import sop.webflow.eservice.EGPCaseData;
import sop.webflow.rt.api.BaseProcessClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Delegator
public final class TestDelegator {
    public static final String PROCESS_NAME = "suochengTest";
    public static final String PROJECT_NAME = "IAIS";
    public static final String STEP_NAME = "SaveContinueURL";

    public static final String ATTR_APP_DRAFT_NO = "egp.app.draft.no";

    private static final Logger logger = LoggerFactory.getLogger(new Throwable().getStackTrace()[1].getClassName());
    private static String PATTERN_YEAR = "{YYYY}";
    private static String PATTERN_MONTH = "{MM}";
    private static String PATTERN_DAY = "{DD}";
    private static String PAYMENT_SVC_SEQ = "PAYMENT_SVC_SEQ";
    private static String PAYMENT_NO_REG = "SVC-{YYYY}{MM}{SEQNO}";
    private static String PATTERN_SEQ_PRE = "{SEQNO}";

    public  void prepareData(BaseProcessClass base) {
        logger.info("The prepareData start ... ");
        FormRuntimeUtil.addButton("<i class=\"fa fa-floppy-o\"></i> " + MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Save Draft"),
                "draft", "btn btn-round-lg btn-lg btn-oblue2 btn-st", false);
        FormRuntimeUtil.addButton("<i class=\"fa fa-envelope-o\"></i> " + MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Submit"),
                "submit", "btn btn-round-lg btn-blue2 btn-st", true);
      /*  FormButton fb = new FormButton(
                MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Next")+" <i class=\"fa fa-arrow-circle-o-right\"></i>", "next");
        fb.setCssClass("saas-btn");
        FormRuntimeUtil.addButton(fb);*/
        logger.info("The prepareData end ... ");
    }

    public  void validate(BaseProcessClass base) {
        IFormValidator testValidate = new TestValidate(base);
        testValidate.validate();
    }

    public void saveDraft(BaseProcessClass bpc) throws BaseException {
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
            saveAppToMC(bpc, app);
        } else {
            String formDetailUrl = getFormDetailUrl(bpc);
            String tinyCallback = null;
            String formName = bpc.request.getParameter("formName");
            StringBuffer callback = new StringBuffer(
                    ServerConfig.getInstance().getFrontendURL() + EngineHelper.getContextPath());
            callback.append("/eservice/").append(PROJECT_NAME).append("/").append(PROCESS_NAME).append("/").append(STEP_NAME)
                    .append("?caseid=").append(bpc.currentCase.getCaseId()).append("&formname=").append(formName);

            tinyCallback = IaisEGPHelper.getTinyUrl(callback.toString());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(JsonLabel.APP_STATUSMSG, "Draft");
            map.put(JsonLabel.APP_STATUS, "Draft");
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
    public void saveAppToMC(BaseProcessClass bpc,Application app)  {
        if (!MessageCenterHelper.isMCEnabled()) {
            return;
        }

        String tinyCallback = null;
        String formName = bpc.request.getParameter("formName");

        StringBuffer callback = new StringBuffer(
                ServerConfig.getInstance().getFrontendURL() + EngineHelper.getContextPath());
        callback.append("/eservice/").append(PROJECT_NAME).append("/").append(PROCESS_NAME).append("/").append(STEP_NAME)
                .append("?caseid=").append(bpc.currentCase.getCaseId());

        tinyCallback = IaisEGPHelper.getTinyUrl(callback.toString());
        String formDetailUrl = getFormDetailUrl(bpc);
        Map<String, Object> propMap = new HashMap<String, Object>();
        propMap.put(JsonLabel.SVC_CALLBACK_URL, tinyCallback);
        propMap.put(JsonLabel.APP_STATUS, "Draft");

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
    private String getBase64FormHtml(String formName,BaseProcessClass bpc) {
        FormInstance formIns = IaisFormHelper.getFormInstanceFromCase(bpc.currentCase, formName);
        ;
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
    public String getFormDetailUrl(BaseProcessClass bpc) {
        StringBuffer mcCallback = new StringBuffer();
        mcCallback.append(ServerConfig.getInstance().getFrontendURL() + ConfigUtil.getString(AppConstants.CONFIG_FORM_DETAILS_URL));
        if (mcCallback.indexOf("?") > 0) {
            mcCallback.append("&");
        } else {
            mcCallback.append("?");
        }
        mcCallback.append("caseid=").append(bpc.currentCase.getCaseId())
                .append("&formname=").append(bpc.request.getParameter("formName"));
        return mcCallback.toString();
    }
    public void setApplicationDraftNo(EGPCase egpcase, String appDraftNo) {
        if (egpcase != null) {
            egpcase.putCaseData(new EGPCaseData(ATTR_APP_DRAFT_NO,
                    appDraftNo));
        }
    }
    private String getApplicationDraftNo(EGPCase egpcase) {
        if (egpcase != null) {
            EGPCaseData ecd = egpcase.getCaseData(ATTR_APP_DRAFT_NO);
            if (ecd == null)
                return null;
            else
                return ecd.getValue() instanceof String ? (String) ecd.getValue() : null;
        }
        return null;
    }
    private Application bindApplication(BaseProcessClass bpc)  {
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
    private String generateAppNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MMddHHmmssSSS", Locale.US);
        return "draft-suocheng-test-app-" + format.format(new Date());
    }
    private ServiceRegistry loadServiceInfo(BaseProcessClass bpc) {
        EGPCaseData caseData = bpc.currentCase.getCaseData(EGPConstants.ATTR_SERVICE_INFO);
        if (null == caseData) {
            throw new BaseRuntimeException("The eService has not been registered.");
        }

        return IaisServiceRegistryHelper.getServiceRegistryFromCase(bpc.currentCase);
    }
    private void bindData(BaseProcessClass bpc) {

        logger.debug("-------------------------------- bindData start -----------------------------------------");

        EGPCaseWrapper<String> wrapper = new EGPCaseWrapper<String>(bpc.currentCase, String.class);
        String continueId = wrapper.get("continueId");
        logger.debug("continueId =" + continueId);
        bpc.request.setAttribute("continueId", continueId);
        logger.debug("---------------------------- bindData end ---------------------------------------------");

        logger.debug("---------------------------- save Payment History end ---------------------------------------------");
        Application application = IaisEGPCaseHelper.getApplication(bpc.currentCase);
        PaymentData paymentCaseData = IaisEGPCaseHelper.getPaymentCaseData(bpc.currentCase);
        Payment p = new Payment();
        p.setAppId(application.getAppId());
        p.setPaymentDesc(paymentCaseData.getPaymentDescription());
        p.setPaymentStatus(paymentCaseData.getPaymentTrans().getTransStatus());
        p.setPaymentAmount(paymentCaseData.getAmount());
        p.setTxRefNo(paymentCaseData.getPaymentTrans().getTransNo());
        p.setPaymentType(paymentCaseData.getMech().getMechType());
        PaymentService.getInstance().create(p);
        logger.debug("---------------------------- save Payment History end ---------------------------------------------");

    }
}
