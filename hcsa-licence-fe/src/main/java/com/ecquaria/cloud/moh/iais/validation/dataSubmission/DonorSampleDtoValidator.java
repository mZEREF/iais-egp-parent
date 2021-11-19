package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator start ..."));
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        DonorSampleDto donorSampleDto = (DonorSampleDto)obj;
        ValidationResult result = null;
        ValidationResult donorIdentityKnownResult = null;
        ValidationResult sampleFromOthersResult = null;
        if(donorSampleDto.isDirectedDonation()){
             result = WebValidationHelper.validateProperty(donorSampleDto, "directedDonationY");
        }else{
            result = WebValidationHelper.validateProperty(donorSampleDto, "directedDonationN");

            String donorIdentityKnown = donorSampleDto.getDonorIdentityKnown();
            if(DataSubmissionConsts.DONOR_IDENTITY_KNOWN.equals(donorIdentityKnown)){
                donorIdentityKnownResult = WebValidationHelper.validateProperty(donorSampleDto, "donorIdentityKnown");
                String sampleFromHciCode = donorSampleDto.getSampleFromHciCode();
                if("other".equals(sampleFromHciCode)){
                    sampleFromOthersResult = WebValidationHelper.validateProperty(donorSampleDto, "sampleFromOthers");
                }
            }else{
                donorIdentityKnownResult = WebValidationHelper.validateProperty(donorSampleDto, "donorIdentityAnonymous");
            }
        }
        if (result != null) {
            map.putAll(result.retrieveAll());
        }
        if (donorIdentityKnownResult != null) {
            map.putAll(donorIdentityKnownResult.retrieveAll());
        }
        if (sampleFromOthersResult != null) {
            map.putAll(sampleFromOthersResult.retrieveAll());
        }
        //validate the ages
        String[] ages = donorSampleDto.getAges();
        for(int i =0 ;i<ages.length;i++){
            String age = ages[i];
            if(StringUtil.isEmpty(age)){
                map.put("ages"+i,"GENERAL_ERR0006");
            }else if(!StringUtil.isNumber(age)){
                map.put("ages"+i,"GENERAL_ERR0002");
            }else if(age.length()>2){
                map.put("ages"+i,"GENERAL_ERR0041");
            }
        }

        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator start ..."));
        return map;
    }
}
