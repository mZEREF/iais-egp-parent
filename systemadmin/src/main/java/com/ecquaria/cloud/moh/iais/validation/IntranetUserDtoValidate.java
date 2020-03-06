package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        //userId
        if(!StringUtil.isEmpty(userId)){
            if(!userId.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(.{1,64})$")){
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
            String nStr = "";
            String eStr = "";

            int len = Math.min(eftStartDateStr.length, eftEndDateStr.length);
            for (int i = len - 1; i >= 0; i--){
                nStr += eftStartDateStr[i];
                eStr += eftEndDateStr[i];
            }

            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate startDate = LocalDate.parse(nStr, formatter);
            LocalDate endDate = LocalDate.parse(eStr, formatter);

            int comparatorValue = endDate.compareTo(startDate);
            if (comparatorValue < 0){
                errorMap.put("accountDeactivateDatetime", "USER_ERR006");
            }
        }

        return errorMap;
    }
}
