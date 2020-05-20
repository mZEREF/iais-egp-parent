package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
public class BlastValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request, "edit");
        if(blastManagementDto.getEmailAddress() != null){
            for (String item :blastManagementDto.getEmailAddress()
                 ) {
                if(!item.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    errMap.put("addr","Please key in a valid email address");
                }
            }
        }
        if(blastManagementDto.getSchedule() == null){
            errMap.put("date","The field is mandatory.");
        }
        String HH = blastManagementDto.getHH();
        String MM = blastManagementDto.getMM();
        if(HH == null){
            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(HH) &&  Integer.parseInt(HH) < 24)){
            errMap.put("HH","Field format is wrong");
        }
        if(MM == null){
            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(MM) &&  Integer.parseInt(MM) < 60)){
            errMap.put("HH","Field format is wrong");
        }
        return errMap;
    }
}
