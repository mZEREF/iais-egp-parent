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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppPremSelfDeclServiceImpl implements AppPremSelfDeclService {

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return RestApiUtil.getListByPathParam("iais-application:8883/iais-application/application/results-by-groupid/{groupid}", "1C629C17-CB72-4892-8F31-87F6759C791A", ApplicationDto.class);
    }

    @Override
    public List<HcsaServiceDto> listHcsaService() {
        return RestApiUtil.getList(RestApiUrlConsts.ALL_HCSA_SERVICE, HcsaServiceDto.class);
    }

    @Override
    public SearchResult<ChecklistQuestionDto> listSelfDescConfig(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.CHECKLIST_SELF_DESC_CONFIG, searchParam);
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
