package com.ecquaria.cloud.moh.iais.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author yichen
 * @Date:2020/8/24
 */

@Slf4j
public class PropertiesUtil {
    private static final String DEFAULT_PROPERTIES = "application.properties";

    public static String prop(String basePath, String str){
        try {
            Resource resource = new ClassPathResource(basePath);
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            return props.getProperty(str);
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String prop(String str){
        return prop(DEFAULT_PROPERTIES, str);
    }
}
