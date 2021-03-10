package com.ecquaria.cloud.moh.iais.method.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.method.RfiCanCheck;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Wenkang
 * @date 2021/3/9 16:07
 */
@Component
public class RfiCanCheckImpl implements RfiCanCheck {
    @Autowired
    private ApplicationService applicationService;
    @Override
    public List<AppEditSelectDto> getAppEditSelectDtoSForRfi(String appNo){
        List<ApplicationDto> applicationDtosByApplicationNo = applicationService.getApplicationDtosByApplicationNo(appNo);
        List<String> list = IaisCommonUtils.genNewArrayList();
        if (applicationDtosByApplicationNo != null) {
            for (ApplicationDto applicationDto1 : applicationDtosByApplicationNo) {
                list.add(applicationDto1.getId());
            }
        }
        List<AppEditSelectDto> appEditSelectDtosByAppIds = applicationService.getAppEditSelectDtosByAppIds(list);

        return appEditSelectDtosByAppIds;
    }
}
