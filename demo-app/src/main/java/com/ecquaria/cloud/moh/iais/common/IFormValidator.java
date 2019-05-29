package com.ecquaria.cloud.moh.iais.common;

import com.ecquaria.cloud.moh.iais.common.helper.IaisFormHelper;
import com.ecquaria.cloud.moh.iais.common.util.StringUtil;
import sop.webflow.rt.api.BaseProcessClass;
import java.util.List;

public abstract class IFormValidator {
    public BaseProcessClass process;
    public String formName;
    private List required;
    public int errorCount = 0;
    public abstract void validateProject();
    public void validate(){
        validateRequired();
        validateProject();
        if(errorCount==0){
            process.request.setAttribute("validate","true");
        }else{
            process.request.setAttribute("validate","false");
        }
    }

    public abstract void addRequired(List required);
    private void validateRequired(){
         if (required!=null && required.size()>0){
             for (int i = 0;i<required.size();i++){
                 String column = (String) required.get(i);
                 String value = IaisFormHelper.getFormFieldData(process,formName,column);
                 if(StringUtil.isEmpty(value)){
                     errorCount++;
                     // todo  get the errormessage from DB.
                     IaisFormHelper.addFieldErrorMessage(process.request,column,"age is empty!!!");
                 }
             }
         }
    }
}

