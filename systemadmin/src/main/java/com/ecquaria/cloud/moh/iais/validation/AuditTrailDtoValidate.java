/*
package com.ecquaria.cloud.moh.iais.validation;

*/
/*
 *author: yichen
 *date time:9/19/2019 1:54 PM
 *description:
 *//*


import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AuditTrailDtoValidate implements CustomizeValidator {
    private static final int LIMITEDDATE = 90;


    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        long currentTimeMillis = System.currentTimeMillis();
        int bit = 1000 * 60 * 60 * 24;
        long from = 0;
        long to = 0;
       */
/* Date startDate = IaisEGPHelper.parseToDate(ParamUtil.getDate(request, AuditTrailConstant.PARAM_STARTDATE));
        Date endDate = IaisEGPHelper.parseToDate(ParamUtil.getDate(request, AuditTrailConstant.PARAM_ENDDATE));

        if (endDate != null){
            to = endDate.getTime();
            if(to > currentTimeMillis){
                errMap.put(AuditTrailConstant.PARAM_ENDDATE, "The end date cannot be greater than the current time");
            }
        }

        if(startDate != null && endDate == null){
            from = startDate.getTime();
            if(endDate == null){
                to = currentTimeMillis;
            }else{
                to = endDate.getTime();
            }

            int day = (int) (to - from) / bit;
            if (day > LIMITEDDATE){
                errMap.put(AuditTrailConstant.PARAM_STARTDATE, "The Start time and end time cannot differ more than three months");
            }
        }*//*


        return errMap;
    }

}
*/
