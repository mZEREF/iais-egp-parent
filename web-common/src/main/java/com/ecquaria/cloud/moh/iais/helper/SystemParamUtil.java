package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import org.springframework.context.ApplicationContext;

/**
 * @author yichen
 * @Date:2020/9/18
 */


public final class SystemParamUtil {

    public static int getFileMaxLimit(){
       return SpringContextHelper.getContext().getBean(SystemParamConfig.class).getUploadFileLimit();
    }

    public static int getAuditTrailSearchWeek() {
        SystemParamConfig spc = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
        return spc.getAuditTrailSearchWeek();
    }

    public static String getInterServerName(){
        SystemParamConfig spc = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
       return spc.getInterServerName();
    }

    public static String getUploadFileType() {
        SystemParamConfig spc = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
        return spc.getUploadFileType();
    }

    public static int getDefaultPageSize(){
        ApplicationContext applicationContext = SpringContextHelper.getContext();
        if (applicationContext == null){
            return 10;
        }

        SystemParamConfig spc = applicationContext.getBean(SystemParamConfig.class);
        String p = spc.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        return Integer.parseInt(defaultValue);
    }

    public static int[] toPageSizeArray(){
        ApplicationContext applicationContext = SpringContextHelper.getContext();
        if (applicationContext == null){
            return null;
        }

        SystemParamConfig spc = applicationContext.getBean(SystemParamConfig.class);
        String p = spc.getPagingSize();
        String[] from = IaisEGPHelper.getPageSizeByStrings(p);

        int[] to = new int[from.length];

        for (int i = 0; i < from.length; i++){
            to[i] = Integer.parseInt(from[i]);
        }

        return to;
    }

    private SystemParamUtil() {
    }
}
