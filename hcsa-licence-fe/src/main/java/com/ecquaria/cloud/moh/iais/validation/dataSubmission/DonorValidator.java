package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;

import java.util.List;
import java.util.Map;

public class DonorValidator {
     public static void validateDonors(List<DonorDto> arDonorDtos, Map<String, String> errorMap,boolean usedDonorOocyte ){
         arDonorDtos.forEach( arDonorDto -> {
                     valCommonField(errorMap,arDonorDto,false);
                     if(!errorMap.isEmpty()) {
                         return;
                     }
//                     if(usedDonorOocyte && StringUtil.isEmpty(arDonorDto.getAge())){
//                         errorMap.put("age"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
//                     }
                     if(!arDonorDto.validateSourceOtherNotNull(arDonorDto.getOtherSource())){
                         errorMap.put("otherSource"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                     if(!arDonorDto.validateRelation()){
                         errorMap.put("relation"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }

                     if (arDonorDto.isDonorIndicateFresh() && StringUtil.isEmpty(arDonorDto.getAge())) {
                         errorMap.put("age"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                     if (arDonorDto.isDonorIndicateFrozen() && StringUtil.isEmpty(arDonorDto.getFrozenOocyteAge())){
                         errorMap.put("frozenOocyteAge"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                     if (arDonorDto.isDonorIndicateEmbryo() && StringUtil.isEmpty(arDonorDto.getFrozenEmbryoAge())){
                         errorMap.put("frozenEmbryoAge"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                     if (doValidateFreshOrFrozen(arDonorDto.getAgeType(),arDonorDto.getFreshSpermAge()
                             , DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM,arDonorDto.isDonorIndicateFreshSperm())){
                         errorMap.put("freshSpermAge"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                     if (doValidateFreshOrFrozen(arDonorDto.getAgeType(),arDonorDto.getFrozenSpermAge()
                             , DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM,arDonorDto.isDonorIndicateFrozenSperm())){
                         errorMap.put("frozenSpermAge"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                 }
         );
     }

     private static Boolean doValidateFreshOrFrozen(String ageType,String typeSpermAge,String type,Boolean flag){
         return StringUtil.isNotEmpty(ageType) && StringUtil.isEmpty(typeSpermAge) && type.equals(ageType) && flag;
     }

     public static Map<String, String> valCommonField(Map<String, String> errorMap,DonorDto arDonorDto,boolean needValIdType){
         if (arDonorDto.getDirectedDonation()==null) {
             errorMap.put("directedDonation"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
             return errorMap;
         }
         if(needValIdType && arDonorDto.getDirectedDonation()!=null && arDonorDto.getDirectedDonation() &&StringUtil.isEmpty(arDonorDto.getIdType())){
                 errorMap.put("idType"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
         }
         if(!arDonorDto.validateDirectedDonationYesNotNull(arDonorDto.getIdNumber())){
             errorMap.put("idNumber"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
         }else if(needValIdType && arDonorDto.getDirectedDonation()!=null && arDonorDto.getDirectedDonation() && !arDonorDto.validateIdNo(arDonorDto.getIdNumber())){
             errorMap.put("idNumber"+ arDonorDto.getArDonorIndex() ,"RFC_ERR0012");
         } else if(needValIdType && !arDonorDto.validateIdNo(arDonorDto.getDonorSampleCode())){
             errorMap.put("donorSampleCode"+ arDonorDto.getArDonorIndex() ,"RFC_ERR0012");
         }
         if(!arDonorDto.validateDirectedDonationNoNotNull(arDonorDto.getDonorSampleCode())){
             errorMap.put("donorSampleCode"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
         }
         if(!arDonorDto.validateDirectedDonationNoNotNull(arDonorDto.getIdType())){
             errorMap.put("idTypeSample"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
         }else if(arDonorDto.validateDirectedDonationNoNotNull(arDonorDto.getIdType()) &&
                 StringUtil.isNotEmpty(arDonorDto.getDonorSampleCode())
                 && !SgNoValidator.validateIdNoForDataSubmission(arDonorDto.getIdType(),arDonorDto.getDonorSampleCode())){
             errorMap.put("donorSampleCode"+ arDonorDto.getArDonorIndex() ,"RFC_ERR0012");
         }
         return errorMap;
     }
}
