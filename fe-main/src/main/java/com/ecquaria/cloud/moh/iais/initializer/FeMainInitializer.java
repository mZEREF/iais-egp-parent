package com.ecquaria.cloud.moh.iais.initializer;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author yichen
 * @Date:2020/10/21
 */

@Slf4j
@Component
public class FeMainInitializer {
    @Autowired
    private SystemParamConfig systemParamConfig;

    @PostConstruct
    public void initMethod() throws Exception {
        log.info("SpringInitializer start........");
        copySpConfigProperties();
        log.info("SpringInitializer end........");
    }

    private void copySpConfigProperties(){
        try {
            String separator = File.separator;
            String runtimeEnv = systemParamConfig.getIaisRuntimeEnv();

            log.info(StringUtil.changeForLog("fe main runtimeEnv" + runtimeEnv));

            String resourcePath = this.getClass().getResource(separator).getPath();
            File resource = new File(resourcePath);
            String srcPath = resource.getParentFile() + separator + "enviroment-files";

            FileUtils.copyFilesToOtherPosition(srcPath + separator + runtimeEnv, resourcePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
