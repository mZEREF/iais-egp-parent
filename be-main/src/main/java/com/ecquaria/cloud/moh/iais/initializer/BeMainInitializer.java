package com.ecquaria.cloud.moh.iais.initializer;

import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicMainClient;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BeMainInitializer
 *
 * @author Jinhua
 * @date 2021/2/19 13:20
 */
public class BeMainInitializer {
    @Autowired
    private SystemBeLicMainClient systemBeLicMainClient;

    @PostConstruct
    public void initMethod() throws Exception {
        systemBeLicMainClient.initCachePostCodes();
    }
}
