package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * DonorSampleDtoValidator
 *
 * @author suocheng
 * @date 11/15/2021
 */
@Slf4j
public class DonorSampleDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        DonorSampleDto donorSampleDto = (DonorSampleDto)obj;
        if(donorSampleDto.isDirectedDonation()){

        }
        return result;
    }
}
