package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * OrgUserHelper
 *
 * @author suocheng
 * @date 9/2/2022
 */
@Slf4j
public class OrgUserHelper {
    public static  String getSystemAdminUserId (List<OrgUserDto> orgUserDtos){
        String result = "";
        for(OrgUserDto orgUserDto : orgUserDtos){
            if(orgUserDto.getAvailable() && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDto.getStatus())){
                result = orgUserDto.getId();
                break;
            }
        }
        if(StringUtil.isEmpty(result)){
            for(OrgUserDto orgUserDto : orgUserDtos){
                if( AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDto.getStatus())){
                    result = orgUserDto.getId();
                    break;
                }
            }
        }
        if(StringUtil.isEmpty(result)){
            result = orgUserDtos.get(0).getId();
        }
        return result;
    }
}
