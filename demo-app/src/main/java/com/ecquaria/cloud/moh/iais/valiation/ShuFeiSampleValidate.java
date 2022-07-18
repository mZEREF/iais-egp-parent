package com.ecquaria.cloud.moh.iais.valiation;

import com.ecquaria.cloud.moh.iais.common.dto.sample.*;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.*;
import com.ecquaria.cloud.moh.iais.service.*;
import org.springframework.beans.factory.annotation.*;

import javax.servlet.http.*;
import java.util.*;

public class ShuFeiSampleValidate implements CustomizeValidator {

    @Autowired
    private ShuFeiAdminPageService shuFeiAdminPageService;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ShuFeiCreateSampleDto shuFeiCreateSampleDto = (ShuFeiCreateSampleDto) ParamUtil.getSessionAttr(request,"ShuFeiCreateSampleDto");
        String displayName = shuFeiCreateSampleDto.getDisplayName();
        if (shuFeiCreateSampleDto == null || StringUtil.isEmpty(shuFeiCreateSampleDto.getMobileNo())){
            return errMap;
        }
        if (!StringUtil.isEmpty(displayName)){
            ShuFeiPersonSampleDto shuFeiPersonSampleDto = shuFeiAdminPageService.getByDisPlayName(displayName);
            if (shuFeiPersonSampleDto != null){
                String validateDisPlayName = shuFeiPersonSampleDto.getDisplayName();
                if (validateDisPlayName.equals(displayName)){
                    errMap.put("displayName","DisPlayName is repeat!!!!");
                }
            }
        }
        return errMap;
    }
}
