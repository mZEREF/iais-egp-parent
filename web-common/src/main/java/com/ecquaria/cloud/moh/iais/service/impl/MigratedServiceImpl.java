package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.MigratedService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MigratedServiceImpl
 *
 * @author suocheng
 * @date 7/21/2021
 */
@Service
@Slf4j
public class MigratedServiceImpl implements MigratedService {

    @Autowired
    private SystemParamConfig systemParamConfig;
    @Override
    public boolean isActiveMigrated() {
        log.info(StringUtil.changeForLog("The isActiveMigrated start ..."));
        String startDateStr = systemParamConfig.getMigratedLicenceStart();
        String endDateStr = systemParamConfig.getMigratedLicenceEnd();
        Date nowDate = new Date();
        log.info(StringUtil.changeForLog("The isActiveMigrated startDateStr -->:"+startDateStr));
        log.info(StringUtil.changeForLog("The isActiveMigrated endDateStr -->:"+endDateStr));
        log.info(StringUtil.changeForLog("The isActiveMigrated nowDate -->:"+nowDate));
        boolean result = false;
        try {
            if(!StringUtil.isEmpty(endDateStr)){
                SimpleDateFormat df = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
                Date endDate = df.parse(endDateStr);
                if(nowDate.before(endDate)){
                    result =  true;
                }
                log.info(StringUtil.changeForLog("The isActiveMigrated endDateStr result-->:"+result));
                if(!StringUtil.isEmpty(startDateStr)&&result){
                    Date startDate = df.parse(startDateStr);
                    if(nowDate.before(startDate)){
                        result = false;
                    }
                }
                log.info(StringUtil.changeForLog("The isActiveMigrated startDate result-->:"+result));
            }
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        log.info(StringUtil.changeForLog("The isActiveMigrated end ..."));
        return  result;
    }
}
