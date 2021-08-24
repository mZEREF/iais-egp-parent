package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description AppSvcPersonnelValidator
 * @Auther chenlei on 8/17/2021.
 */
public class AppSvcPersonnelValidator implements CustomizeValidator {

    public Map<String, String> validate(Object obj, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (!(obj instanceof AppSvcPersonnelDto)) {
            return errorMap;
        }
        AppSvcPersonnelDto svcPersonnel = (AppSvcPersonnelDto) obj;
        String name = svcPersonnel.getName();
        if (StringUtil.isNotEmpty(name)) {
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = (List<AppSvcPersonnelDto>) request.getAttribute(ClinicalLaboratoryDelegator.SECTION_LEADER_LIST);
            if (appSvcSectionLeaderList == null || appSvcSectionLeaderList.size() <= 1) {
                return errorMap;
            }
            boolean duplicateName = false;
            for (AppSvcPersonnelDto dto : appSvcSectionLeaderList) {
                if (Objects.equals(dto.getIndexNo(), svcPersonnel.getIndexNo())) {
                    break;
                }
                if (name.equalsIgnoreCase(dto.getName())) {
                    duplicateName = true;
                    break;
                }
            }
            if (duplicateName) {
                errorMap.put("name", MessageUtil.getMessageDesc("NEW_ERR0012"));
            }
        }

        return errorMap;
    }
}
