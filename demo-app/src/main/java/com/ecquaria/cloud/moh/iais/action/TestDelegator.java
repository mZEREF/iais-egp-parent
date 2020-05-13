package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.egov.core.common.constants.AppConstants;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.i18n.MultiLangUtil;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator
@Slf4j
public final class TestDelegator {
    public static final String PROJECT_NAME = "IAIS";
    public static final String PROCESS_NAME = "suochengTest";
    public static final String STEP_NAME = "SaveContinueURL";
    private static final String FORM_NAME = "ApplicationForm";


    public  void prepareData(BaseProcessClass base) {
        log.info("The prepareData start ... ");
        FormRuntimeUtil.addButton("<i class=\"fa fa-floppy-o\"></i> " + MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Save Draft"),
                "draft", "btn btn-round-lg btn-lg btn-oblue2 btn-st", false);
        FormRuntimeUtil.addButton("<i class=\"fa fa-envelope-o\"></i> " + MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Submit"),
                "submit", "btn btn-round-lg btn-blue2 btn-st", true);
      /*  FormButton fb = new FormButton(
                MultiLangUtil.translate(base.request, AppConstants.KEY_TRANSLATION_MODULE_LABEL, "Next")+" <i class=\"fa fa-arrow-circle-o-right\"></i>", "next");
        fb.setCssClass("saas-btn");
        FormRuntimeUtil.addButton(fb);*/
        log.info("The prepareData end ... ");
    }

    public  void validate(BaseProcessClass base) {

    }

    public void saveDraft(BaseProcessClass bpc) {
    }
    public void initSaveDraftData(BaseProcessClass bpc) {
        String caseid = bpc.request.getParameter("caseid");
        bpc.copyCaseData(caseid);
        IaisEGPHelper.belongToUser(bpc);
        String continueId = bpc.request.getParameter("continueId");
        bpc.getSession().setAttribute("continueId", continueId);
    }
    public void bATStep0_OnApplicationCreate(BaseProcessClass bpc) {

    }


}
