package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * EndCycleStageDtoValidator
 *
 * @author zhixing
 * @date 2021/11/8
 */

@Component
@Slf4j
public class EndCycleStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        String cycleAbandoned = ParamUtil.getString(httpServletRequest, "cycleAbandoned");
        String abandonReason = ParamUtil.getRequestString(httpServletRequest, "abandonReasonSelect");
        if(cycleAbandoned ==null){
            errorMap.put("cycleAbandoned" ,"GENERAL_ERR0006");
        }
        String errMsg006 = null;
        if (Boolean.parseBoolean(cycleAbandoned)) {
            if ( abandonReason == null){
                 errMsg006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Reason for Abandonment (Others)", "field");
                errorMap.put("abandonReason", errMsg006);
            }
        }
        if (!StringUtil.isEmpty(abandonReason) && "ENDRA005".equals(abandonReason) && Boolean.parseBoolean(cycleAbandoned)) {
            String otherAbandonReason = ParamUtil.getRequestString(httpServletRequest, "otherAbandonReason");
            if (StringUtil.isEmpty(otherAbandonReason)) {
                errorMap.put("otherAbandonReason", errMsg006 == null ? MessageUtil.replaceMessage("GENERAL_ERR0006", "Reason for Abandonment (Others)", "field") :  errMsg006);
            }else if (otherAbandonReason.length() > 20) {
               Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("maxlength", "20");
                repMap.put("field", "Reason for Abandonment (Others)");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041", repMap);
                errorMap.put("otherAbandonReason", errMsg);

            }
        }
        return errorMap;
    }
}
