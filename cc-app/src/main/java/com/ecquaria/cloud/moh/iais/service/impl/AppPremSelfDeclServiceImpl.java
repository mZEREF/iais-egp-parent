package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:11/20/2019 1:56 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclTabView;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppPremSelfDeclServiceImpl implements AppPremSelfDeclService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public List<HcsaServiceDto> listHcsaService() {
        return appConfigClient.allHcsaService().getEntity();
    }

    @Override
    public SearchResult<ChecklistQuestionDto> listSelfDescConfig(SearchParam searchParam) {
        return appConfigClient.listSelfDescConfig(searchParam).getEntity();
    }

    @Override
    public List<AppGrpPremisesDto> listAppGrpPremisesDto(String appId) {
        return RestApiUtil.getListByPathParam(RestApiUrlConsts.IAIS_APPLICATION + "/application/app-group-premises-results/{appid}", appId, AppGrpPremisesDto.class);
    }

    @Override
    public SearchResult<SelfDeclTabView> getSelfDeclTab(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.HCSA_CONFIG_SERVICE + "/self-decl/tab", searchParam);
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDto(String appId) {
        return RestApiUtil.getByPathParam(RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.IAIS_APPLICATION + "/application-premises-by-app-id/{applicationId}", appId, AppGrpPremisesDto.class);
    }


}
