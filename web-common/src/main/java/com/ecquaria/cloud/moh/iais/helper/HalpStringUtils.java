package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HalpStringUtils {
    /**
     * @param serviceCode
     * @return SERVICE_NAME
     * @description Get the value of SERVICE_NAME from SERVICE_CODE
     */
    public static String splitServiceName(String serviceCode){
        if(StringUtil.isEmpty(serviceCode) || StringUtil.isIn(serviceCode, HalpSearchResultHelper.allDsTypes)){
            return "N/A";
        }
        StringBuilder draftServiceName = new StringBuilder();
        String[] serviceName = serviceCode.split("@");
        for (int i=0;i<serviceName.length;i++){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceName[i]);
            if (hcsaServiceDto != null){
                if (i>0){
                    draftServiceName.append("<br/>")
                            .append(hcsaServiceDto.getSvcName());
                }else{
                    draftServiceName.append(hcsaServiceDto.getSvcName());
                }
            }
        }
        return draftServiceName.toString();
    }
}
