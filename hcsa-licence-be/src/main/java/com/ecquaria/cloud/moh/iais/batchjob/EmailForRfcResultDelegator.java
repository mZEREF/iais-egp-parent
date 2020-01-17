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
 * @date 2020/1/17 13:59
 */
@Delegator("emailToRfcResult")
@Log4j
public class EmailForRfcResultDelegator {

    @Autowired
    private EmailToResultService emailToResultService;
    public void start(BaseProcessClass bpc){
        log.info("bbbbbbbbbbbbbbb");
    }

    public void action(BaseProcessClass bpc) throws IOException, TemplateException {
        emailToResultService.sendEfcResultEmail();
    }
}
