package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.QueryHandlerFeService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryHandlerFeServiceImpl implements QueryHandlerFeService {

    @Autowired
    private AppInboxClient appInboxClient;

    @Autowired
    private InboxClient inboxClient;

    @Autowired
    private FeAdminClient feAdminClient;

    @Autowired
    private FePaymentClient fePaymentClient;

    @Autowired
    private LicenceInboxClient licenceInboxClient;

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private SystemAdminMainFeClient systemAdminMainFeClient;

    @Autowired
    private AuditTrailMainClient auditTrailMainClient;

    @Autowired
    private EventFeInboxClient eventFeInboxClient;

    @Override
    public QueryHelperResultDto getQueryHelperResultDtoList(String querySql, String moduleName) {
        QueryHelperResultDto resultDto = null;
        if(StringUtil.isEmpty(querySql) || StringUtil.isEmpty(moduleName)){
            return resultDto;
        }
        if("hsca-application-fe".equals(moduleName)){
            resultDto  = appInboxClient.doQuery(querySql).getEntity();
        }else if("event-bus".equals(moduleName)){
            resultDto = eventFeInboxClient.doQuery(querySql).getEntity();
        }else if("audit-trail".equals(moduleName)){
            resultDto = auditTrailMainClient.doQuery(querySql).getEntity();
        }else if("inter-inbox".equals(moduleName)){
            resultDto = inboxClient.doQuery(querySql).getEntity();
        }else if("hcsa-licence-fe".equals(moduleName)){
            resultDto = licenceInboxClient.doQuery(querySql).getEntity();
        }else if("organization-fe".equals(moduleName)){
            resultDto = feAdminClient.doQuery(querySql).getEntity();
        }else if("hcsa-config".equals(moduleName)){
            resultDto = configInboxClient.doQuery(querySql).getEntity();
        }else if("system-admin".equals(moduleName)){
            resultDto = systemAdminMainFeClient.doQuery(querySql).getEntity();
        }else if("payment".equals(moduleName)){
            resultDto = fePaymentClient.doQuery(querySql).getEntity();
        }
        return resultDto;
    }

    @Override
    public LicenseeDto getLicenseeByUserAccountInfo(String userAccountString) {
        LicenseeDto licenseeDto = null;
        if(!StringUtil.isEmpty(userAccountString)){
            licenseeDto = feAdminClient.getLicenseeByUserAccountInfo(userAccountString).getEntity();
        }
        return licenseeDto;
    }
}
