package sg.gov.moh.iais.common.helper;

import com.ecquaria.egp.core.bat.FormHelper;
import org.apache.log4j.Logger;
import sg.gov.moh.iais.common.util.StringUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.Map;


public class IaisFormHelper implements Serializable {
    private static final Logger logger = Logger.getLogger(IaisFormHelper.class);
    public IaisFormHelper(BaseProcessClass process, String formName) {
        this.setProcess(process);
        this.setFormName(formName);
    }

    private BaseProcessClass process;
    private String formName;


    public BaseProcessClass getProcess() {
        return process;
    }

    public void setProcess(BaseProcessClass process) {
        this.process = process;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFieldValue(String fieldName){
        if(process == null || formName == null){
            logger.error(StringUtil.encodeLogger("process or formName is null"));
            return null;
        }
        return FormHelper.getFormFieldData(process, formName, fieldName);
    }

    public String getFieldValue(String fieldName, String controlName, int rowIndex){
        if(process == null || formName == null){
            logger.error(StringUtil.encodeLogger("process or formName is null"));
            return null;
        }
        return FormHelper.getFormFieldData(process, formName, fieldName, controlName, rowIndex);
    }

    public String[] getFieldValues(String fieldName){
        if(process == null || formName == null){
            logger.error(StringUtil.encodeLogger("process or formName is null"));
            return null;
        }
        return FormHelper.getFormFieldArrayData(process, formName, fieldName);
    }

    public String[] getFieldValues(String fieldName, String controlName, int rowIndex){
        if(process == null || formName == null){
            logger.error(StringUtil.encodeLogger("process or formName is null"));
            return null;
        }
        return FormHelper.getFormFieldArrayData(process, formName, fieldName, controlName, rowIndex);
    }

    public void setFieldData(Map<String, Object> map){
        if(process != null && formName != null){
            FormHelper.fillFormData(process, formName, map);
        }
    }

    public void setFieldErrorMessage(String fieldName, String errorMessage){
        if(process != null){
            FormHelper.addFieldErrorMessage(process.request, fieldName, errorMessage);
        }
    }

    public void setFieldErrorMessage(String fieldName, String errorMessage, int rowIndex){
        if(process != null){
            FormHelper.addFieldErrorMessage(process.request, fieldName, errorMessage, rowIndex);
        }
    }
}
