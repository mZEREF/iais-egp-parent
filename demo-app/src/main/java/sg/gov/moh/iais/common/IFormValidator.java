package sg.gov.moh.iais.common;

import sg.gov.moh.iais.common.helper.IaisFormHelper;
import sg.gov.moh.iais.common.util.StringUtil;
import sop.webflow.rt.api.BaseProcessClass;
import java.util.List;

public abstract class IFormValidator {
    public BaseProcessClass process;
    public String formName;
    private List required;
    public int errorCount = 0;
    public abstract void validateProject(IaisFormHelper helper);
    public void validate(){
        IaisFormHelper helper = new IaisFormHelper(process,formName);
        validateRequired(helper);
        validateProject(helper);
        if(errorCount==0){
            process.request.setAttribute("validate","true");
        }else{
            process.request.setAttribute("validate","false");
        }
    }

    public abstract void addRequired(List required);
    private void validateRequired(IaisFormHelper helper){
         if (required!=null && required.size()>0){
             for (int i = 0;i<required.size();i++){
                 String column = (String) required.get(i);
                 String value = helper.getFieldValue(column);
                 if(StringUtil.isEmpty(value)){
                     errorCount++;
                     // todo  get the errormessage from DB.
                     helper.setFieldErrorMessage(column,"age is empty!!!");
                 }
             }
         }
    }
}

