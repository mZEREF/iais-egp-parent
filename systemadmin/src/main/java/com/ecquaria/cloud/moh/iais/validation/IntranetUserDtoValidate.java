package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/31 15:03
 */
@Component
public class IntranetUserDtoValidate implements CustomizeValidator {

    @Autowired
    private IntranetUserService intranetUserService;

    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String userId = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_USERID);
        String startDateStr = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_STARTDATE);
        String endDateStr = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_ENDDATE);
        String mobileNo = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_MOBILENO);
        String officeNo = ParamUtil.getRequestString(httpServletRequest, IntranetUserConstant.INTRANET_OFFICETELNO);
        //userId
        if(!StringUtil.isEmpty(userId)){
            if(!userId.matches("^[A-Za-z0-9]+$")){
                errorMap.put("userId","USER_ERR002");
            }else{
                OrgUserDto intranetUserByUserId = intranetUserService.findIntranetUserByUserId(userId);
                if(intranetUserByUserId!=null){
                    String valiuserId = intranetUserByUserId.getUserId();
                    if(userId.equals(valiuserId)){
                        errorMap.put("userId","USER_ERR003");
                    }
                }
            }
        }
        //date
        if(!StringUtil.isEmpty(startDateStr)&&!StringUtil.isEmpty(endDateStr)){
            String[] eftStartDateStr = startDateStr.split("/");
            String[] eftEndDateStr = endDateStr.split("/");
            StringBuilder nStr = new StringBuilder();
            StringBuilder eStr = new StringBuilder();
            int len = Math.min(eftStartDateStr.length, eftEndDateStr.length);
            for (int i = len - 1; i >= 0; i--){
                nStr.append(eftStartDateStr[i]);
                eStr.append(eftEndDateStr[i]);
            }
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate startDate = LocalDate.parse(nStr.toString(), formatter);
            LocalDate endDate = LocalDate.parse(eStr.toString(), formatter);
            int comparatorValue = endDate.compareTo(startDate);
            if (comparatorValue < 0){
                errorMap.put("accountDeactivateDatetime", "USER_ERR006");
            }
        }
        if(!StringUtil.isEmpty(mobileNo)){
            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                errorMap.put("mobileNo", "Please key in a valid mobile number.");
            }
        }

        if(!StringUtil.isEmpty(officeNo)) {
            if (!officeNo.matches("^[6][0-9]{7}$")) {
                errorMap.put("officeNo", "Please key in a valid phone number.");
            }
        }
        return errorMap;
    }
}
