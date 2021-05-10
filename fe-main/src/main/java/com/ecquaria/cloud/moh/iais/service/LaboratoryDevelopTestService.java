package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface LaboratoryDevelopTestService {
    void sendLDTTestEmailAndSMS(String orgId,String licenseeId) throws IOException, TemplateException;
}
