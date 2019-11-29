package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * LicenceApproveBatchjob
 *
 * @author suocheng
 * @date 11/26/2019
 */
@Delegator("licenceApproveBatchjob")
@Slf4j
public class LicenceApproveBatchjob {
    public void doBatchJob(){
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is start ..."));
       //
        List<ApplicationLicenceDto> applicationLicenceDtos = new ArrayList<>();
        for(ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos ){
            ApplicationGroupDto applicationGroupDto = applicationLicenceDto.getApplicationGroupDto();
            int isGrpLic = applicationGroupDto.getIsGrpLic();
            if(AppConsts.YES.equals(String.valueOf(isGrpLic))){

            }else{
                List<ApplicationListDto> applicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                for(ApplicationListDto applicationListDto : applicationListDtoList){

                }
            }
        }
        log.debug(StringUtil.changeForLog("The LicenceApproveBatchjob is end ..."));
    }

    private LicenceDto getLicenceDto(ApplicationListDto applicationListDto){
        LicenceDto licenceDto = new LicenceDto();
        //licenceDto.set
        return  licenceDto;
    }

}
