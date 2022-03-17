package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Slf4j
public class DsLaboratoryDevelopTestValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        if(isRfc(httpServletRequest)){
            LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(httpServletRequest);
            LdtSuperDataSubmissionDto oldLdtSuperDataSubmissionDto = DataSubmissionHelper.getOldLdtSuperDataSubmissionDto(httpServletRequest);
            DataSubmissionDto dataSubmissionDto = ldtSuperDataSubmissionDto.getDataSubmissionDto();
            DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = ldtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
            DsLaboratoryDevelopTestDto oldDsLaboratoryDevelopTestDto = oldLdtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
            if (StringUtil.isEmpty(dataSubmissionDto.getAmendReason())) {
                map.put("amendReason", "GENERAL_ERR0006");
            } else if ("LDTRE_002".equals(dataSubmissionDto.getAmendReason())) {
                if (StringUtil.isEmpty(dataSubmissionDto.getAmendReasonOther())) {
                    map.put("amendReasonOther", "GENERAL_ERR0006");
                } else if (dataSubmissionDto.getAmendReasonOther().length() > 50) {
                    map.put("amendReasonOther", NewApplicationHelper.repLength("Reason for Amendment (Others)", "50"));
                }
            }
            if ("0".equals(oldDsLaboratoryDevelopTestDto.getTestStatus()) && "1".equals(dsLaboratoryDevelopTestDto.getTestStatus())){
                map.put("testStatus", "You can not change \"Status of Test\" from \"Active\" to \"Inactive\".");
            }
        }
        return map;
    }

    private boolean isRfc(HttpServletRequest request) {
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
        return ldtSuperDataSubmissionDto != null && ldtSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(ldtSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }
}
