package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Shicheng
 * @date 2020/5/12 10:17
 **/
@Component
public class InspDateSvcHoursSndValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto) ParamUtil.getSessionAttr(request, "inspecTaskCreAndAssDto");
        String manHours = inspecTaskCreAndAssDto.getInspManHours();
        if(StringUtil.isEmpty(manHours) || manHours.length() > 3){
            return null;
        }
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        Pattern pattern = compile("[0-9]*");
        boolean hoursFlag = pattern.matcher(manHours).matches();
        if(!hoursFlag) {
            errMap.put("inspManHours", "GENERAL_ERR0068");
        } else {
            try {
                int hour = Integer.parseInt(manHours);
                if (hour <= 0) {
                    errMap.put("inspManHours", "GENERAL_ERR0068");
                }
            } catch (Exception e) {
                errMap.put("inspManHours", "GENERAL_ERR0068");
            }
        }
        return errMap;
    }
}
