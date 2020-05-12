package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
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
public class InspDateSvcHoursValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(request, "inspectionTaskPoolListDto");
        String manHours = inspectionTaskPoolListDto.getInspManHours();
        if(StringUtil.isEmpty(manHours) || manHours.length() > 3){
            return null;
        }
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        Pattern pattern = compile("[0-9]*");
        boolean hoursFlag = pattern.matcher(manHours).matches();
        if(!hoursFlag){
            errMap.put("inspManHours", "ERR0013");
        }
        return errMap;
    }
}
