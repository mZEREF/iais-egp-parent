package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
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
        GenerateIdClient generateIdClient = SpringContextHelper.getContext().getBean(GenerateIdClient .class);
        ArDataSubmissionService arDataSubmissionService = SpringContextHelper.getContext().getBean(ArDataSubmissionService .class);
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
            String donorSampleCodeType = StringUtil.isEmpty(donorSampleDto.getIdType()) ? donorSampleDto.getIdType() : StringUtil.isIn(donorSampleDto.getIdType(),new String[]{DataSubmissionConsts.AR_ID_TYPE_PINK_IC,DataSubmissionConsts.AR_ID_TYPE_BLUE_IC,DataSubmissionConsts.AR_ID_TYPE_FIN_NO,DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO}) ? donorSampleDto.getIdType() : DataSubmissionConsts.AR_ID_TYPE_CODE;
             donorSampleDtoFromDb =  arDataSubmissionService.getDonorSampleDto(
                     donorSampleDto.getIdType()
                    ,donorSampleDto.getIdNumber()
                     ,donorSampleCodeType
                    ,DataSubmissionConsts.AR_ID_TYPE_CODE.equalsIgnoreCase(donorSampleCodeType) ? donorSampleDto.getDonorSampleCode() : donorSampleDto.getIdNumber()
                    ,donorSampleDto.getSampleFromHciCode()
                    ,donorSampleDto.getSampleFromOthers());

        }else{
            List<DonorSampleDto> donorSampleDtos =  arDataSubmissionService.getDonorSampleDtoBySampleKey(sampleKey);
            if(IaisCommonUtils.isNotEmpty(donorSampleDtos)){
                donorSampleDtoFromDb = donorSampleDtos.get(0);
            }
        }
        if(donorSampleDtoFromDb != null){
            sampleKey = donorSampleDtoFromDb.getSampleKey();
            List<DonorSampleAgeDto> donorSampleAgeDtos =  arDataSubmissionService.getDonorSampleAgeDtoBySampleKey(sampleKey);
            donorSampleDtoFromDb.setDonorSampleAgeDtos(donorSampleAgeDtos);
        }
        if(StringUtil.isEmpty(sampleKey)){
            log.info(StringUtil.changeForLog("Generated a ned samplekey"));
            donorSampleDto.setSampleKey(generateIdClient.getSeqId().getEntity());
        }else{
            donorSampleDto.setSampleKey(sampleKey);
        }
        //countLive
        if(countLive(donorSampleDtoFromDb) >3){
            map.put("directedDonationYesDonorLive","DS_ERR053");
            map.put("donorSampleCodeRowDonorLive","DS_ERR053");
            map.put("donorDetailDonorLive","DS_ERR053");
        }
        if(donorSampleDto.isDirectedDonation()){
             result = WebValidationHelper.validateProperty(donorSampleDto, "directedDonationY");
        }else{
            result = WebValidationHelper.validateProperty(donorSampleDto, "directedDonationN");

            String donorIdentityKnown = donorSampleDto.getDonorIdentityKnown();
            if(DataSubmissionConsts.DONOR_IDENTITY_KNOWN.equals(donorIdentityKnown)){
                donorIdentityKnownResult = WebValidationHelper.validateProperty(donorSampleDto, "donorIdentityKnown");
            }else{
                donorIdentityKnownResult = WebValidationHelper.validateProperty(donorSampleDto, "donorIdentityAnonymous");
                String sampleFromHciCode = donorSampleDto.getSampleFromHciCode();
                if(DataSubmissionConsts.AR_SOURCE_OTHER.equals(sampleFromHciCode)){
                    sampleFromOthersResult = WebValidationHelper.validateProperty(donorSampleDto, "sampleFromOthers");
                }
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
        if(ages != null){
            for(int i =0 ;i<ages.length;i++){
                String age = ages[i];
                log.info(StringUtil.changeForLog("The age is -->:"+age));
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
                        if(ageInt<21 || ageInt>40 ){
                            map.put("ages"+i,"DS_ERR044");
                        }
                    }else if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_OOCYTE.equals(sampleType)
                            ||DataSubmissionConsts.DONOR_SAMPLE_TYPE_EMBRYO.equals(sampleType)){
                        if(ageInt<21 || ageInt>35 ){
                            map.put("ages"+i,"DS_ERR045");
                        }
                    }
                }
                //Repetition
                if(IaisCommonUtils.isEmpty(map)&&isRepetition(age,ages,donorSampleDtoFromDb)){
                    map.put("ages"+i,"DS_ERR046");
                }
            }
        }else{
//            map.put("oldAges","GENERAL_ERR0006");
//            log.info(StringUtil.changeForLog("The Ages is null"));
        }

        //RFC
        if(DataSubmissionConsts.DS_APP_TYPE_RFC .equals(donorSampleDto.getAppType())){
            ValidationResult amendReasonResult = WebValidationHelper.validateProperty(donorSampleDto, "donorSampleRFC");
            if (amendReasonResult != null) {
                map.putAll(amendReasonResult.retrieveAll());
            }
            if(DataSubmissionConsts.DONOR_SAMPLE_AMEND_REASON_OTHERS.equals(donorSampleDto.getAmendReason())){
                ValidationResult amendReasonOtherResult = WebValidationHelper.validateProperty(donorSampleDto, "donorSampleRFCOther");
                if (amendReasonOtherResult != null) {
                    map.putAll(amendReasonOtherResult.retrieveAll());
                }
            }
        }

        log.info(StringUtil.changeForLog("The DonorSampleDtoValidator end ..."));
        return map;
    }
    private int countLive(DonorSampleDto donorSampleDtoFromDb ){
        int result = 0;
        if(donorSampleDtoFromDb != null){
            List<DonorSampleAgeDto> donorSampleAgeDtos = donorSampleDtoFromDb.getDonorSampleAgeDtos();
            if(IaisCommonUtils.isNotEmpty(donorSampleAgeDtos)){
                for(DonorSampleAgeDto donorSampleAgeDto : donorSampleAgeDtos){
                    if(DataSubmissionConsts.DONOR_AGE_STATUS_LIVE_BIRTH.equals(donorSampleAgeDto.getStatus())){
                        result ++;
                    }
                }
            }
        }

        return result;

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
                    if(StringUtil.isNotEmpty(age) && Integer.parseInt(age) == donorSampleAgeDto.getAge()){
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
