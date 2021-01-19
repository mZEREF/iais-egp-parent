package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import freemarker.template.TemplateException;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

/**
 * UenManagementService
 *
 * @author junyu
 * @date 2020/1/22
 */
public interface UenManagementService {
    boolean validityCheckforAcraissuedUen(MohUenDto mohUenDto);
    boolean generatesMohIssuedUen(BaseProcessClass bpc, MohUenDto mohUenDto) throws IOException, TemplateException;
    void sendUenEmail(BaseProcessClass bpc,String active) throws IOException, TemplateException;
}
