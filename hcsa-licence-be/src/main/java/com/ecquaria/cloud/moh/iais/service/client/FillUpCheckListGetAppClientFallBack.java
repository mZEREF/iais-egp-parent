package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.CheckListDraftAllDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditFillterDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 16:17
 */
public class FillUpCheckListGetAppClientFallBack implements FillUpCheckListGetAppClient {

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremiseseCorrDto(String appid) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseseCorrDto", appid);
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectChklDto> saveAppPreInspChkl(AppPremisesPreInspectChklDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppPreInspChkl", dto);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> saveAppRecom(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppRecom", appPremisesRecommendationDto);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> saveAppPreNc(AppPremPreInspectionNcDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppPreNc", dto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> saveAppPreNcItem(
            List<AppPremisesPreInspectionNcItemDto> dtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppPreNcItem", dtoList);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(String appPremId, String recomType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremRecordByIdAndType", appPremId, recomType);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesRecommendationDto>> getAppPremisesRecommendationHistoryDtosByIdAndType(String appPremId,
            String recomType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRecommendationHistoryDtosByIdAndType", appPremId, recomType);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getALlRecItem() {
        return IaisEGPHelper.getFeignResponseEntity("getALlRecItem");
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectChklDto> getAppPremInspeChlkByAppCorrIdAndConfigId(String appPremId,
            String configId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremInspeChlkByAppCorrIdAndConfigId", appPremId, configId);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> getAppNcByAppCorrId(String appCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppNcByAppCorrId", appCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getAppNcItemByNcId(String ncId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppNcItemByNcId", ncId);
    }

    @Override
    public FeignResponseEntity<AppPremInsDraftDto> saveAppInsDraft(AppPremInsDraftDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppInsDraft", dto);
    }

    @Override
    public FeignResponseEntity<List<AdhocDraftDto>> saveAdhocDraft(List<AdhocDraftDto> dtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveAdhocDraft", dtoList);
    }

    @Override
    public FeignResponseEntity<List<AdhocDraftDto>> getAdhocByItemId(String itemId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdhocByItemId", itemId);
    }

    @Override
    public FeignResponseEntity<AppPremInsDraftDto> getAppInsDraftByChkId(String chkId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppInsDraftByChkId", chkId);
    }

    @GetMapping(path = "/iais-apppreinsncitem-be/ncitemappdto", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public FeignResponseEntity<List<ApplicationViewDto>> getApplicationDtoByNcItem() {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationDtoByNcItem");
    }

    @Override
    public FeignResponseEntity<AppPremInsDraftDto> updateAppInsDraft(AppPremInsDraftDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppInsDraft", dto);
    }

    @Override
    public FeignResponseEntity<List<AdhocDraftDto>> updateAdhocDraft(List<AdhocDraftDto> dtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateAdhocDraft", dtoList);
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectChklDto> updateAppPreInspChkl(AppPremisesPreInspectChklDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPreInspChkl", dto);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> updateAppRecom(
            AppPremisesRecommendationDto appPremisesRecommendationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppRecom", appPremisesRecommendationDto);
    }

    @Override
    public FeignResponseEntity<AppPremPreInspectionNcDto> updateAppPreNc(AppPremPreInspectionNcDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPreNc", dto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklList(String appPremId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremInsChklList", appPremId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListFOrDraft(String appPremId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremInsChklListFOrDraft", appPremId);
    }

    @Override
    public FeignResponseEntity<AppPremisesPreInspectionNcItemDto> getNcItemByItemId(String itemId) {
        return IaisEGPHelper.getFeignResponseEntity("getNcItemByItemId", itemId);
    }

    @Override
    public FeignResponseEntity<Map<String, Boolean>> configUsageStatus(List<String> configId) {
        return IaisEGPHelper.getFeignResponseEntity("configUsageStatus", configId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklforDraftList(String appPremId, String configId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremInsChklforDraftList", appPremId, configId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListByPremIdAndVersion(String appPremId,
            String version) {
        return IaisEGPHelper.getFeignResponseEntity("getPremInsChklListByPremIdAndVersion", appPremId, version);
    }

    @Override
    public FeignResponseEntity<AppIntranetDocDto> getAppIntranetDocByPremIdAndStatus(String premId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getAppIntranetDocByPremIdAndStatus", premId, status);
    }

    @Override
    public FeignResponseEntity<List<AppIntranetDocDto>> getAppIntranetDocListByPremIdAndStatus(String premId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getAppIntranetDocListByPremIdAndStatus", premId, status);
    }

    @Override
    public FeignResponseEntity<String> deleteAppIntranetDocsByPremId(String premId) {
        return IaisEGPHelper.getFeignResponseEntity("deleteAppIntranetDocsByPremId", premId);
    }

    @Override
    public FeignResponseEntity<String> deleteAppIntranetDocsById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("deleteAppIntranetDocsByPremId", id);
    }

    @Override
    public FeignResponseEntity<String> saveAppIntranetDocByAppIntranetDoc(AppIntranetDocDto appIntranetDocDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppIntranetDocByAppIntranetDoc", appIntranetDocDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesSpecialDocDto>> getAppPremisesSpecialDocByPremId(String premId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesSpecialDocByPremId", premId);
    }

    @Override
    public FeignResponseEntity<String> deleteAppPremisesSpecialDocByPremId(String premId) {
        return IaisEGPHelper.getFeignResponseEntity("deleteAppPremisesSpecialDocByPremId", premId);
    }

    @Override
    public FeignResponseEntity<String> saveAppPremisesSpecialDoc(AppPremisesSpecialDocDto appIntranetDocDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppPremisesSpecialDoc", appIntranetDocDto);
    }

    @Override
    public FeignResponseEntity<AuditFillterDto> getAuditTaskDataDtoByAuditTaskDataDto(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getAuditTaskDataDtoByAuditTaskDataDto", licId);
    }

    @Override
    public FeignResponseEntity<AppIntranetDocDto> getAppIntranetDocByPremIdAndStatusAndAppDocType(String premId, String status,
            String appDocType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppIntranetDocByPremIdAndStatusAndAppDocType", premId, status, appDocType);
    }

    @Override
    public FeignResponseEntity<List<AppPremInsDraftDto>> getInspDraftAnswer(List<String> preChklIds) {
        return IaisEGPHelper.getFeignResponseEntity("getInspDraftAnswer", preChklIds);
    }

    @Override
    public FeignResponseEntity<List<AdhocDraftDto>> getAdhocChecklistDraftsByAdhocItemIdIn(List<String> itemList) {
        return IaisEGPHelper.getFeignResponseEntity("getAdhocChecklistDraftsByAdhocItemIdIn", itemList);
    }

    @Override
    public FeignResponseEntity<CheckListDraftAllDto> saveDraftAnswerForCheckList(CheckListDraftAllDto checkListDraftAllDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveDraftAnswerForCheckList", checkListDraftAllDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChkls(String appPremId, String configId) {
        return IaisEGPHelper.getFeignResponseEntity("getPremInsChkls", appPremId, configId);
    }

}
