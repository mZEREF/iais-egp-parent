package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DonorSampleDtoValidator
 *
 * @author suocheng
 * @date 11/15/2021
 */
@Slf4j
public class DonorSampleDtoValidator implements CustomizeValidator {
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator start ..."));
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        DonorSampleDto donorSampleDto = (DonorSampleDto)obj;
        ValidationResult result = null;
        ValidationResult donorIdentityKnownResult = null;
        ValidationResult sampleFromOthersResult = null;

        //sampleKey if exit set the old eles generate new
        DonorSampleDto donorSampleDtoFromDb = null;
        String sampleKey = donorSampleDto.getSampleKey();
        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator sampleKey is -->:"+sampleKey));
        if(StringUtil.isEmpty(sampleKey)){
             donorSampleDtoFromDb =  arDataSubmissionService.getDonorSampleDto(
                     donorSampleDto.getIdType()
                    ,donorSampleDto.getIdNumber()
                    ,donorSampleDto.getDonorSampleCode()
                    ,donorSampleDto.getSampleFromHciCode()
                    ,donorSampleDto.getSampleFromOthers());
            if(donorSampleDtoFromDb != null){
                List<DonorSampleAgeDto> donorSampleAgeDtos =  arDataSubmissionService.getDonorSampleAgeDtoBySampleKey(sampleKey);
                donorSampleDtoFromDb.setDonorSampleAgeDtos(donorSampleAgeDtos);
            }
        }else{
            donorSampleDto.setSampleKey(generateIdClient.getSeqId().getEntity());
        }

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
            log.info(StringUtil.changeForLog("The age is -->:"+age));
            boolean repetition = isRepetition(age,ages,donorSampleDtoFromDb);
            //empty
            if(StringUtil.isEmpty(age)){
                map.put("ages"+i,"GENERAL_ERR0006");
            //Number
            }else if(!StringUtil.isNumber(age)){
                map.put("ages"+i,"GENERAL_ERR0002");
            //length
            }else if(age.length()>2){
                map.put("ages"+i,"GENERAL_ERR0041");
            //donor sample
            }else if(!donorSampleDto.isDirectedDonation()){
                String sampleType = donorSampleDto.getSampleType();
                log.info(StringUtil.changeForLog("The sampleType is -->:"+sampleType));
                int ageInt = Integer.valueOf(age);
                if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM.equals(sampleType)){
                    if(ageInt<=21 || ageInt>=40 ){
                        map.put("ages"+i,"DS_ERR044");
                    }
                }else if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_OOCYTE.equals(sampleType)
                        ||DataSubmissionConsts.DONOR_SAMPLE_TYPE_EMBRYO.equals(sampleType)){
                    if(ageInt<=21 || ageInt>=35 ){
                        map.put("ages"+i,"DS_ERR045");
                    }
                }
             //Repetition
            }else if(repetition){
                    map.put("ages"+i,"DS_ERR046");
             //
            }else{

            }
        }

        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator end ..."));
        return map;
    }
    private boolean isRepetition(String age,String[] ages,DonorSampleDto donorSampleDto){
        boolean result = false;
        if(StringUtil.isNotEmpty(age) && ages != null && ages.length >1){
           int count = 0;
           for(String everyAge:ages){
               if(age.equals(everyAge)){
                  count ++;
               }
           }
           if(count >1){
               result = true;
           }
           log.info(StringUtil.changeForLog("The count is -->:"+count));
        }
        if(donorSampleDto != null && !result){
            List<DonorSampleAgeDto> donorSampleAgeDtos = donorSampleDto.getDonorSampleAgeDtos();
            if(IaisCommonUtils.isNotEmpty(donorSampleAgeDtos)){
                for(DonorSampleAgeDto donorSampleAgeDto : donorSampleAgeDtos){
                    if(Integer.parseInt(age) == donorSampleAgeDto.getAge()){
                        result = true;
                        log.info(StringUtil.changeForLog("The isRepetition exit in the old DonorSampleAgeDto"));
                        break;
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The isRepetition result is -->:"+result));
        return result;
    }

}
