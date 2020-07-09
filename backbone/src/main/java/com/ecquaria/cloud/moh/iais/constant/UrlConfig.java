package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.helper.EngineHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * UrlConfig
 *
 * @author suocheng
 * @date 7/8/2020
 */
@Component
public class UrlConfig {
    @Value("${moh.internet.websit}")
    private String internetWebSite;
    @Value("${moh.internet.inbox}")
    private String internetInbox;
    @Value("${moh.intranet.websit}")
    private String intranetWebSite;
    @Value("${moh.intranet.inbox}")
    private String intranetInbox;

    public UrlConfig() {
    }

    public static UrlConfig getInstance() {
        return (UrlConfig)EngineHelper.getSpringContext().getBean(UrlConfig.class);
    }
    public String getInternetWebSite() {
        return this.internetWebSite;
    }
    public String getInternetInbox() {
        return this.internetInbox;
    }
    public String getIntranetWebSite() {
        return this.intranetWebSite;
    }
    public String getIntranetInbox() {
        return this.intranetInbox;
    }
}
