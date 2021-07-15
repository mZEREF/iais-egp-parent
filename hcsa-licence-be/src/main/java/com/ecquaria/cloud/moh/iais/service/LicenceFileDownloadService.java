package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/9 16:08
 */
public interface LicenceFileDownloadService {
    void initPath();

    boolean decompression() throws Exception;
    Boolean changeFeApplicationStatus();
    List<TaskDto> getTasksByRefNo(String refNo);

    void sendRfc008Email(ApplicationGroupDto applicationGroupDto,ApplicationDto application) throws IOException, TemplateException;
}
