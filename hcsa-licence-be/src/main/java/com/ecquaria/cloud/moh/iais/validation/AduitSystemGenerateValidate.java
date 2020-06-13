package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/26 17:16
 */
public class AduitSystemGenerateValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String postcode = ParamUtil.getString(request,"postcode");
        String genNum = ParamUtil.getString(request,"genNum");
        String inspectionStartDate = ParamUtil.getDate(request, "inspectionStartDate");
        String inspectionEndDate = ParamUtil.getDate(request, "inspectionEndDate");
        if( !StringUtil.isEmpty(postcode) && !StringUtil.stringIsFewDecimal(postcode,null)){
            errMap.put("postcode","GENERAL_ERR0002");
        }

        if( !StringUtil.isEmpty(genNum) && !StringUtil.stringIsFewDecimal(genNum,null)){
            errMap.put("genNum","GENERAL_ERR0002");
        }

        if(!StringUtil.isEmpty(inspectionStartDate) && !StringUtil.isEmpty(inspectionEndDate)){
           boolean isDateS;
           boolean isDateE;
           try{
               IaisEGPHelper.parseToDate(inspectionStartDate);
               isDateS = true;
           }catch (Exception e){
               errMap.put("inspectionStartDate","ERR0020");
               isDateS = false;
           }

           try {
               IaisEGPHelper.parseToDate(inspectionEndDate);
               isDateE = true;
           }catch (Exception e){
               errMap.put("inspectionEndDate","ERR0020");
               isDateE = false;
           }

           if(isDateS && isDateE && !IaisEGPHelper.isAfterDateSecondByStringDate(inspectionStartDate,inspectionEndDate,Boolean.TRUE)){
               errMap.put("inspectionStartDate","Last Inspection done before(Start) cannot be later than Last Inspection done before(End)");
           }
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }
}
