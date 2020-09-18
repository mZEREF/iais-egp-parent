package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author yichen
 * @Date:2020/9/18
 */

@Slf4j
public class SystemParamUtil {
    private static ApplicationContext context;
    private static SystemParamConfig systemParamConfig;
    static {
        context = SpringContextHelper.getContext();

        if (context != null){
            systemParamConfig = context.getBean(SystemParamConfig.class);

            if (systemParamConfig == null){
                log.info("systemParamConfig  is null .........");
            }
        }else {
            log.info("application context is null .........");
        }
    }

    private SystemParamUtil(){}

    public static int getFileMaxLimit(){
       return systemParamConfig.getUploadFileLimit();
    }

    public static String getIntraServerName(){
       return systemParamConfig.getIntraServerName();
    }

    public static String getInterServerName(){
       return systemParamConfig.getInterServerName();
    }

    public static int getReminderRectification(){
        return systemParamConfig.getReminderRectification();
    }
}
