package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InboxServiceImpl implements InboxService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Autowired
    private InboxClient inboxClient;

    @Autowired
    private LicenceInboxClient licenceInboxClient;

    @Autowired
    private FeUserClient feUserClient;

    @Override
    public String getServiceNameById(String serviceId) {
        return configInboxClient.getServiceNameById(serviceId).getEntity();
    }

    @Override
    public String getDraftNumber(String appNo) {
        return appInboxClient.getDraftNumber(appNo).getEntity();
    }

    @Override
    public InterInboxUserDto getUserInfoByUserId(String userId) {
        InterInboxUserDto interInboxUserDto = feUserClient.findUserInfoByUserId(userId).getEntity();
        List<String> appGrpIdList = appInboxClient.getAppGrpIdsByLicenseeIs(interInboxUserDto.getLicenseeId()).getEntity();
        interInboxUserDto.setAppGrpIds(appGrpIdList);
        return  interInboxUserDto;
    }

    @Override
    public List<ApplicationDraftDto> getDraftList() {
        return appInboxClient.getDraftList().getEntity();
    }

    @Override
    public SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam) {
        return appInboxClient.searchResultFromApp(searchParam).getEntity();
    }

    @Override
    public SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam) {
        return inboxClient.searchInbox(searchParam).getEntity();
    }

    @Override
    public SearchResult<InboxLicenceQueryDto> licenceDoQuery(SearchParam searchParam) {
        return licenceInboxClient.searchResultFromLicence(searchParam).getEntity();
    }

    @Override
    public Integer licActiveStatusNum(String licenseeId) {
        return licenceInboxClient.getLicActiveStatusNum(licenseeId).getEntity();
    }

    @Override
    public Integer appDraftNum(List<String> appOrgIds) {
        return appInboxClient.getAppDraftNum(appOrgIds).getEntity();
    }

    @Override
    public Integer unreadAndUnresponseNum(String userId) {
        return inboxClient.searchUnreadAndUnresponseNum(userId).getEntity();
    }

    @Override
    public void updateDraftStatus(String draftNo, String status) {
        appInboxClient.updateDraftStatus(draftNo,status).getEntity();
    }
}