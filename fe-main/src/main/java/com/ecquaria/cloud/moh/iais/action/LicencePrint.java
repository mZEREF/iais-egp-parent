package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author weilu
 * @Date 2020/7/24 11:14
 */
@Delegator("licencePrint")
@Slf4j
public class LicencePrint {
    public void action(BaseProcessClass bpc) throws IOException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        List<String> ids = (List<String>) ParamUtil.getRequestAttr(bpc.request, "licIds");
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>download");
        byte[] bytes = null;
        File tempFile = File.createTempFile("temp", ".xml");
        bytes = FileUtils.readFileToByteArray(tempFile);
        bpc.request.setAttribute("pdf", bytes);

    }
}
