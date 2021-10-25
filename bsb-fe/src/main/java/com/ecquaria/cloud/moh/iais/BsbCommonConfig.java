package com.ecquaria.cloud.moh.iais;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("sg.gov.moh.iais.egp")
@EnableFeignClients("sg.gov.moh.iais.egp.bsb.client")
public class BsbCommonConfig {
    /* This class is used for component scan.
     * Because the package of EGP main class is com.ecquaria.cloud, other packages won't be scanned by default.
     * Don't add configurations not related to package scan here */
}