package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * UenManagementService
 *
 * @author junyu
 * @date 2020/1/22
 */
public interface UenManagementService {
    boolean validityCheckforAcraissuedUen(MohUenDto mohUenDto);
    boolean generatesMohIssuedUen(MohUenDto mohUenDto) throws IOException, TemplateException;

}
