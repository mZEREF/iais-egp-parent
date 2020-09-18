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
    private static ApplicationContext context = SpringContextHelper.getContext();

    private SystemParamUtil(){}

    public static int getFileMaxLimit(int defaultVal){
        SystemParamConfig systemParamConfig =  context.getBean(SystemParamConfig.class);
        if (systemParamConfig != null){
            return systemParamConfig.getUploadFileLimit();
        }

        return defaultVal;
    }
}
