package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.AppealClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AppealServiceImpl
 *
 * @author suocheng
 * @date 2/6/2020
 */
@Service
@Slf4j
public class AppealServiceImpl implements AppealService {
    @Autowired
    private AppealClient appealClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Override
    public List<AppealApproveDto> getAppealApproveDtos() {
        List<AppealApproveDto> result = appealClient.getApproveAppeal().getEntity();
        if(!IaisCommonUtils.isEmpty(result)){
            List<String> licenceIds = new ArrayList<>();
            //get all licenceIds
            for (AppealApproveDto appealApproveDto : result){
                AppealDto appealDto = appealApproveDto.getAppealDto();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealDto.getAppealType())){
                    licenceIds.add(appealDto.getRelateRecId());
                }
            }
            //get licenceDto and set to AppealApproveDto
            if(!IaisCommonUtils.isEmpty(licenceIds)){
                List<LicenceDto> licenceDtos = hcsaLicenceClient.retrieveLicenceDtos(licenceIds).getEntity();
                if(!IaisCommonUtils.isEmpty(licenceDtos)){
                    for (AppealApproveDto appealApproveDto : result){
                        AppealDto appealDto = appealApproveDto.getAppealDto();
                        if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealDto.getAppealType())){
                            String licenceId = appealDto.getRelateRecId();
                            LicenceDto licenceDto = getLicenceDto(licenceDtos,licenceId);
                            if(licenceDto!=null) {
                                appealApproveDto.setLicenceDto(licenceDto);
                            }
                        }
                    }
                }
            }
            //
        }
        return  result;
    }

    private  LicenceDto getLicenceDto(List<LicenceDto> licenceDtos,String licenceId){
        LicenceDto result = null;
        if(!IaisCommonUtils.isEmpty(licenceDtos)&&!StringUtil.isEmpty(licenceId)){
          for(LicenceDto licenceDto :licenceDtos){
              if(licenceId.equals(licenceDto.getId())){
                  result = licenceDto;
                  break;
              }
          }
        }
        return result;
    }
}
