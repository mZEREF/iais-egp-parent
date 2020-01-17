package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.RenewEmailToResultService;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

/**
 * @author weilu
 * @date 2020/1/16 19:07
 */
@Delegator("emailToResult")
@Log4j
public class EmailForRenewResultDelegator {

    @Autowired
    private RenewEmailToResultService renewEmailToResultService;
    public void start(BaseProcessClass bpc){
        log.info("aaaaaaaaaaaaaaaaaaa");
    }

    public void action(BaseProcessClass bpc) throws IOException, TemplateException {
        renewEmailToResultService.sendEmail();
    }
}
