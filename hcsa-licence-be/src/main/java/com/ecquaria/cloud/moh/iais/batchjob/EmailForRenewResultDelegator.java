package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.EmailToResultService;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

/**
 * @author weilu
 * @date 2020/1/16 19:07
 */
@Delegator("emailToRenewResult")
@Log4j
public class EmailForRenewResultDelegator {

    @Autowired
    private EmailToResultService emailToResultService;
    public void start(BaseProcessClass bpc){
        log.info("aaaaaaaaaaaaaaaaaaa");
    }

    public void action(BaseProcessClass bpc) throws Exception {
        emailToResultService.sendRenewResultEmail();
    }
}
