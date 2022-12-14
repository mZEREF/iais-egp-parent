package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * BelicationClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class BelicationClientFallback implements BelicationClient{
    
    @Override
    public FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(String correlationId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppViewByCorrelationId",correlationId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getAppByNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppByNo",appNo);
    }

    @Override
    public FeignResponseEntity<Void> getDownloadFile(ApplicationListFileDto applicationListDtos) {
        return IaisEGPHelper.getFeignResponseEntity("getDownloadFile",applicationListDtos);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getAppById(String appGroupId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppById",appGroupId);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> updateApplication(ApplicationGroupDto applicationGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplication",applicationGroupDto);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(String appGropId) {
        return IaisEGPHelper.getFeignResponseEntity("getGroupAppsByNo",appGropId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplication",applicationDto);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> create(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        return IaisEGPHelper.getFeignResponseEntity("create",appPremisesRoutingHistoryDto);
    }

    @Override
    public FeignResponseEntity<List<ApplicationLicenceDto>> getGroup(Integer day) {
        return IaisEGPHelper.getFeignResponseEntity("getGroup",day);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> updateApplications(List<ApplicationGroupDto> applicationGroupDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateApplications",applicationGroupDtos);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpsByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpsByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<BroadcastApplicationDto> createBroadcast(BroadcastApplicationDto broadcastApplicationDto) {
        return IaisEGPHelper.getFeignResponseEntity("createBroadcast",broadcastApplicationDto);
    }

    @Override
    public FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocChecklist(AdhocCheckListConifgDto adhocConfigDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAdhocChecklist",adhocConfigDto);
    }

    @Override
    public FeignResponseEntity<List<AdhocChecklistItemDto>> getAdhocByAppPremCorrId(String appremId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdhocByAppPremCorrId",appremId);
    }

    @Override
    public FeignResponseEntity<AdhocCheckListConifgDto> saveAppAdhocConfig(AdhocCheckListConifgDto adhocCheckListConifgDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppAdhocConfig",adhocCheckListConifgDto);
    }

    @Override
    public FeignResponseEntity<AdhocCheckListConifgDto> getAdhocConfigByAppPremCorrId(String appremId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdhocConfigByAppPremCorrId",appremId);
    }

    @Override
    public FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocItems(List<AdhocChecklistItemDto> itemDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveAdhocItems",itemDtoList);
    }

    @Override
    public FeignResponseEntity<Void> doDeleteBySql(String sql) {
        return IaisEGPHelper.getFeignResponseEntity("doDeleteBySql",sql);
    }
}
