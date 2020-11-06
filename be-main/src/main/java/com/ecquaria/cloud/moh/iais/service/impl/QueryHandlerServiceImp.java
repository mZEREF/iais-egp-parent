package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.QueryHandlerService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryHandlerServiceImp implements QueryHandlerService {
    @Autowired
    EmailClient emailClient;
    @Autowired
    EventBeMainClient eventBeMainClient;
    @Autowired
    BelicationClient belicationClient;
    @Autowired
    AuditTrailMainBeClient auditTrailMainBeClient;
    @Autowired
    AppointmentBeMainClient appointmentBeMainClient;
    @Autowired
    LicenceClient licenceClient;
    @Autowired
    OrganizationMainClient organizationMainClient;
    @Autowired
    HcsaConfigMainClient hcsaConfigMainClient;
    @Autowired
    SystemBeLicMainClient systemBeLicMainClient;

    @Override
    public QueryHelperResultDto getQueryHelperResultDtoList(String querySql, String moduleName) {
        QueryHelperResultDto resultDto = null;
        if(StringUtil.isEmpty(querySql) || StringUtil.isEmpty(moduleName)){
            return resultDto;
        }
        if("email-sms".equals(moduleName)){
            resultDto  = emailClient.doQuery(querySql).getEntity();
        }else if("event-bus".equals(moduleName)){
            resultDto = eventBeMainClient.doQuery(querySql).getEntity();
        }else if("hsca-application-be".equals(moduleName)){
            resultDto = belicationClient.doQuery(querySql).getEntity();
        }else if("audit-trail".equals(moduleName)){
            resultDto = auditTrailMainBeClient.doQuery(querySql).getEntity();
        }else if("iais-appointment".equals(moduleName)){
            resultDto = appointmentBeMainClient.doQuery(querySql).getEntity();
        }else if("hcsa-licence-be".equals(moduleName)){
            resultDto = licenceClient.doQuery(querySql).getEntity();
        }else if("organization-be".equals(moduleName)){
            resultDto = organizationMainClient.doQuery(querySql).getEntity();
        }else if("hcsa-config".equals(moduleName)){
            resultDto = hcsaConfigMainClient.doQuery(querySql).getEntity();
        }else if("system-admin".equals(moduleName)){
            resultDto = systemBeLicMainClient.doQuery(querySql).getEntity();
        }
        return resultDto;
    }
}
