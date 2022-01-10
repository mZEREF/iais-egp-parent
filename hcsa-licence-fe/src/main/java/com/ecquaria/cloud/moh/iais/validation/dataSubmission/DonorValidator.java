package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import java.util.List;
import java.util.Map;

public class DonorValidator {
     public static void validateDonors(List<DonorDto> arDonorDtos, Map<String, String> errorMap,boolean usedDonorOocyte ){
         arDonorDtos.forEach( arDonorDto -> {
                     if(usedDonorOocyte && StringUtil.isEmpty(arDonorDto.getAge())){
                         errorMap.put("age"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }

                     if(!arDonorDto.validateDirectedDonationYesNotNull(arDonorDto.getIdNumber())){
                         errorMap.put("idNumber"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }

                     if(!arDonorDto.validateSourceOtherNotNull(arDonorDto.getOtherSource())){
                         errorMap.put("otherSource"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
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
                     if(!arDonorDto.validateRelation()){
                         errorMap.put("relation"+ arDonorDto.getArDonorIndex() ,"GENERAL_ERR0006");
                     }
                 }
         );
     }
}
