package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.SystemParamHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;

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
            List<AppSvcPersonnelDto> appSvcSectionLeaderList = (List<AppSvcPersonnelDto>) request.getAttribute(
                    ClinicalLaboratoryDelegator.SECTION_LEADER_LIST);
            if (appSvcSectionLeaderList != null && appSvcSectionLeaderList.size() > 1) {
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
        }
        String profile = (String) request.getAttribute(this.getClass().getSimpleName() + "_profile");
        if (ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(profile) && StringUtil.isDigit(svcPersonnel.getWrkExpYear())) {
            int workExpYear = Integer.parseInt(svcPersonnel.getWrkExpYear());
            String svcCode = (String) request.getAttribute(NewApplicationConstant.CURRENT_SVC_CODE);
            SystemParamConfig systemParamConfig = SystemParamUtil.getSystemParamConfig();
            if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getClbSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear", MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            } else if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getRdsSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear", MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getNmaSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear", MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getNmiSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear", MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            }
        }
        return errorMap;
    }

}
