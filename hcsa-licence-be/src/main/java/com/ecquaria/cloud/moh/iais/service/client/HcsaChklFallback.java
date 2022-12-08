package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: yichen
 *date time:11/28/2019 2:45 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class HcsaChklFallback implements HcsaChklClient{

    @Override
    public FeignResponseEntity<String> inActiveConfig(String confId) {
        return IaisEGPHelper.getFeignResponseEntity("inActiveConfig", confId);
    }

    @Override
    public FeignResponseEntity<Boolean> inActiveItem(String itemId) {
        return IaisEGPHelper.getFeignResponseEntity("inActiveItem", itemId);
    }

    @Override
    public FeignResponseEntity<SearchResult<ChecklistConfigQueryDto>> listChecklistConfig(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("listChecklistConfig", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<CheckItemQueryDto>> listChklItem(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("listChklItem", searchParam);
    }

    @Override
    public FeignResponseEntity<List<ChecklistItemDto>> listChklItemByItemId(List<String> itemIds) {
        return IaisEGPHelper.getFeignResponseEntity("listChklItemByItemId", itemIds);
    }

    @Override
    public FeignResponseEntity<ChecklistItemDto> getChklItemById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getChklItemById", id);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<ChecklistItemDto>> saveChklItem(ChecklistItemDto itemDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveChklItem", itemDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaChklSvcRegulationDto>> listRegulationClauseNo() {
        return IaisEGPHelper.getFeignResponseEntity("listRegulationClauseNo");
    }

    @Override
    public FeignResponseEntity<IaisApiResult<HcsaChklSvcRegulationDto>> createRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return IaisEGPHelper.getFeignResponseEntity("createRegulation", regulationDto);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<HcsaChklSvcRegulationDto>> updateRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateRegulation", regulationDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaChklSvcRegulationDto>> getAllRegulation() {
        return IaisEGPHelper.getFeignResponseEntity("getAllRegulation");
    }

    @Override
    public FeignResponseEntity<List<ChecklistItemDto>> getAllChecklistItem() {
        return IaisEGPHelper.getFeignResponseEntity("getAllChecklistItem");
    }

    @Override
    public FeignResponseEntity<List<ChecklistConfigDto>> getAllChecklistConfig() {
        return IaisEGPHelper.getFeignResponseEntity("getAllChecklistConfig");
    }

    @Override
    public FeignResponseEntity<List<ChecklistSectionDto>> getAllSection() {
        return IaisEGPHelper.getFeignResponseEntity("getAllSection");
    }

    @Override
    public FeignResponseEntity<List<ChecklistSectionItemDto>> getAllSectionItem() {
        return IaisEGPHelper.getFeignResponseEntity("getAllSectionItem");
    }

    @Override
    public FeignResponseEntity<Boolean> deleteRegulation(String regulationId) {
        return IaisEGPHelper.getFeignResponseEntity("deleteRegulation", regulationId);
    }

    @Override
    public FeignResponseEntity<Boolean> submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos) {
        return IaisEGPHelper.getFeignResponseEntity("submitCloneItem", hcsaChklItemDtos);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> saveUploadItems(List<ChecklistItemExcel> checklistItemList) {
        return IaisEGPHelper.getFeignResponseEntity("saveUploadItems", checklistItemList);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> updateUploadItems(List<ChecklistItemExcel> checklistItemList) {
        return IaisEGPHelper.getFeignResponseEntity("updateUploadItems", checklistItemList);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> submitConfig(ChecklistConfigDto checklistConfigDto) {
        return IaisEGPHelper.getFeignResponseEntity("submitConfig", checklistConfigDto);
    }

    @Override
    public FeignResponseEntity<List<String>> listSubTypeName() {
        return IaisEGPHelper.getFeignResponseEntity("listSubTypeName");
    }

    @Override
    public FeignResponseEntity<List<String>> listSpecName() {
        return IaisEGPHelper.getFeignResponseEntity("listSpecName");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceSubTypeDto>> listSubTypePhase1() {
        return IaisEGPHelper.getFeignResponseEntity("listSubTypePhase1");
    }

    @Override
    public FeignResponseEntity<List<HcsaChklSvcRegulationDto>> getRegulationClauseListIsActive() {
        return IaisEGPHelper.getFeignResponseEntity("getRegulationClauseListIsActive");
    }

    @Override
    public FeignResponseEntity<List<String>> listServiceName(String type) {
        return IaisEGPHelper.getFeignResponseEntity("listServiceName", type);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getChecklistConfigById(String configId) {
        return IaisEGPHelper.getFeignResponseEntity("getChecklistConfigById", configId);
    }

    @Override
    public FeignResponseEntity<List<ChecklistQuestionDto>> getcheckListQuestionDtoList(String svcCode, String svcType) {
        return IaisEGPHelper.getFeignResponseEntity("getcheckListQuestionDtoList", svcCode, svcType);
    }

    @Override
    public FeignResponseEntity<HcsaChklSvcRegulationDto> getRegulationDtoById(String svcCode) {
        return IaisEGPHelper.getFeignResponseEntity("getRegulationDtoById", svcCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceByIds(List<String> serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceByIds", serviceId);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(String svcCode, String type, String module) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionConfigByParams", svcCode, type, module);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(String svcCode, String type, String module, String subTypeName) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionConfigByParams", svcCode, type, module, subTypeName);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionInspectionEntityConfig(String svcCode, String type, String module,
            String inspectionEntity) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionInspectionEntityConfig", svcCode, type, module, inspectionEntity);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(String svcCode, String type, String module, String subTypeName, String hciCode) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionConfigByParams", svcCode, type, module, subTypeName);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionCommonConfig() {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionCommonConfig");
    }

    @Override
    public FeignResponseEntity<Boolean> isExistsRecord(ChecklistConfigDto configDto) {
        return IaisEGPHelper.getFeignResponseEntity("isExistsRecord", configDto);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> submitHcsaChklSvcRegulation(List<HcsaChklSvcRegulationDto> regulationList) {
        return IaisEGPHelper.getFeignResponseEntity("submitHcsaChklSvcRegulation", regulationList);
    }

    @Override
    public FeignResponseEntity<SearchResult<RegulationQueryDto>> searchRegulation(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchRegulation", searchParam);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getMaxVersionServiceConfigByParams(String svcCode, String type, String module, String subTypeName, String hciCode) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxVersionServiceConfigByParams", svcCode, type, module, subTypeName);
    }

    @Override
    public FeignResponseEntity<List<ErrorMsgContent>> saveConfigByTemplate(ChecklistConfigDto excelTemplate) {
        return IaisEGPHelper.getFeignResponseEntity("saveConfigByTemplate", excelTemplate);
    }

    @Override
    public FeignResponseEntity<String> callProceduresGenUUID() {
        return IaisEGPHelper.getFeignResponseEntity("callProceduresGenUUID");
    }

    @Override
    public FeignResponseEntity<List<HcsaChklSvcRegulationDto>> listRegulationByIds(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("listRegulationByIds", ids);
    }
}
