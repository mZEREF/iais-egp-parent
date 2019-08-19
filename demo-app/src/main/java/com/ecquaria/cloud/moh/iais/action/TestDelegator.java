package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FormTestDto;
import com.ecquaria.cloud.moh.iais.helper.IFormValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisFormHelper;
import com.ecquaria.egov.core.common.constants.AppConstants;
import com.ecquaria.egp.core.forms.util.FormRuntimeUtil;
import ecq.commons.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sop.i18n.MultiLangUtil;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator
public final class TestDelegator {
    public static final String PROJECT_NAME = "IAIS";
    public static final String PROCESS_NAME = "suochengTest";
    public static final String STEP_NAME = "SaveContinueURL";
    private static final String FORM_NAME = "ApplicationForm";

    private static final Logger logger = LoggerFactory.getLogger(new Throwable().getStackTrace()[1].getClassName());

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

    public  void validate(BaseProcessClass base) throws Exception {
        ValidationResult result = IFormValidatorHelper.validateForm(base,FORM_NAME,FormTestDto.class,true);
        if(result.isHasErrors()){
            base.request.setAttribute("validate",false);
        }else{
            base.request.setAttribute("validate",true);
        }
    }

    public void saveDraft(BaseProcessClass bpc) throws BaseException {
        IaisFormHelper.doSaveDraft(bpc,PROJECT_NAME,PROCESS_NAME,STEP_NAME);
    }
    public void initSaveDraftData(BaseProcessClass bpc) {
        String caseid = bpc.request.getParameter("caseid");
        bpc.copyCaseData(caseid);
        IaisEGPHelper.belongToUser(bpc);
        String continueId = bpc.request.getParameter("continueId");
        bpc.getSession().setAttribute("continueId", continueId);
    }
    public void bATStep0_OnApplicationCreate(BaseProcessClass bpc) throws BaseException{
        IaisFormHelper.deleteDraft(bpc);

    }

//    public void test(BaseProcessClass bpc) throws Exception {
//       String sql = "select *  from SINGPOST_ADDRESS";
//        ResultSet result = DBToolService.getInstance().query(sql);
//        int columnCount = 0;
//        List<String> columnNames = new ArrayList<>();
//        if (result != null) {
//            ResultSetMetaData resultSetMetaData = result.getMetaData();
//            if (resultSetMetaData != null) {
//                columnCount = resultSetMetaData.getColumnCount();
//            }
//            for (int i = 0; i < columnCount; i++) {
//                columnNames.add(resultSetMetaData.getColumnName(i + 1));
//            }
//        }
//    }

}
