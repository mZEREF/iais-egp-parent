package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * don't catch NullPointerException
 * @author yichen
 * @Date:2020/7/8
 */

@Slf4j
public class SysParamUtil {
    private SysParamUtil(){}

    public static int getDefaultPageSize(){
        ApplicationContext applicationContext = SpringContextHelper.getContext();
        if (applicationContext == null){
            return 10;
        }

        SystemParamConfig systemParamConfig = applicationContext.getBean(SystemParamConfig.class);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        return Integer.parseInt(defaultValue);
    }

    public static int[] toPageSizeArray(){
        ApplicationContext applicationContext = SpringContextHelper.getContext();
        if (applicationContext == null){
            return null;
        }

        SystemParamConfig systemParamConfig = applicationContext.getBean(SystemParamConfig.class);
        String p = systemParamConfig.getPagingSize();
        String[] from = IaisEGPHelper.getPageSizeByStrings(p);

        int[] to = new int[from.length];

        for (int i = 0; i < from.length; i++){
            to[i] = Integer.parseInt(from[i]);
        }

        return to;
    }
}
