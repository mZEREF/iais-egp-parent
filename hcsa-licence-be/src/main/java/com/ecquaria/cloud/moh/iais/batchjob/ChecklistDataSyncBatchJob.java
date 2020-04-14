package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareData;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataRequest;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataResponse;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SyncDataBody;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yichen
 * @Description: This is synchronization job that use  for checklist data(regulation, item, config, section, section item),  Synchronize data to datasource of Fe
 * @Date:2020/4/13
 **/

@Delegator("checklistDataSyncBatchJob")
@Slf4j
public class ChecklistDataSyncBatchJob {

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private HcsaChklClient chklClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    /**
     * StartStep: run
     * @param bpc
     * @throws IllegalAccessException
     */
    public void run(BaseProcessClass bpc) throws IllegalAccessException {
        compareSyncRegulation();

        compareSyncItem();

        compareSyncConfig();

        compareSyncSection();

        compareSyncSectionItem();
    }

    /**
     * StartStep: stop
     * @param bpc
     * @throws IllegalAccessException
     */
    public void stop(BaseProcessClass bpc) throws IllegalAccessException {
        log.info("=======>>>> checklist compare sync data end");
    }

    private void compareSyncRegulation(){
        List<HcsaChklSvcRegulationDto> data =  chklClient.getAllRegulation().getEntity();
        BeSyncCompareDataRequest compareData = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        data.forEach(reg -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(reg.getId());
            syncData.setStatus(reg.getStatus());
            beSyncCompareData.add(syncData);
        });

        compareData.setCompareTable(HcsaChecklistConstants.BATCH_JOB_REGULATION_COMPARE_TABLE);
        compareData.setBeSyncCompareData(beSyncCompareData);
        List<String> syncRequisiteData = compareFeData(compareData);
        data = data.stream().filter(i ->
                syncRequisiteData.contains(i.getId())).collect(Collectors.toList());
        saveSyncData(HcsaChecklistConstants.BATCH_JOB_REGULATION_COMPARE_TABLE, data);
    }

    private void compareSyncItem(){
        List<ChecklistItemDto> data = chklClient.getAllChecklistItem().getEntity();

        BeSyncCompareDataRequest compareData = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        data.forEach(i -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(i.getItemId());
            syncData.setStatus(i.getStatus());
            beSyncCompareData.add(syncData);
        });

        compareData.setCompareTable(HcsaChecklistConstants.BATCH_JOB_ITEM_COMPARE_TABLE);
        compareData.setBeSyncCompareData(beSyncCompareData);

        List<String> syncRequisiteData = compareFeData(compareData);
        data = data.stream().filter(i ->
                syncRequisiteData.contains(i.getItemId())).collect(Collectors.toList());
        saveSyncData(HcsaChecklistConstants.BATCH_JOB_ITEM_COMPARE_TABLE, data);
    }

    private void compareSyncConfig(){
        List<ChecklistConfigDto> data = chklClient.getAllChecklistConfig().getEntity();
        BeSyncCompareDataRequest compareData = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        data.forEach(i -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(i.getId());
            syncData.setStatus(i.getStatus());
            beSyncCompareData.add(syncData);
        });

        compareData.setCompareTable(HcsaChecklistConstants.BATCH_JOB_CONFIG_COMPARE_TABLE);
        compareData.setBeSyncCompareData(beSyncCompareData);

        List<String> syncRequisiteData = compareFeData(compareData);
        data = data.stream().filter(i ->
                syncRequisiteData.contains(i.getId())).collect(Collectors.toList());
        saveSyncData(HcsaChecklistConstants.BATCH_JOB_CONFIG_COMPARE_TABLE, data);
    }

    private void compareSyncSection(){
        List<ChecklistSectionDto> data = chklClient.getAllSection().getEntity();
        BeSyncCompareDataRequest compareData = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        data.forEach(i -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(i.getId());
            syncData.setStatus(i.getStatus());
            beSyncCompareData.add(syncData);
        });

        compareData.setCompareTable(HcsaChecklistConstants.BATCH_JOB_SECTION_COMPARE_TABLE);
        compareData.setBeSyncCompareData(beSyncCompareData);

        List<String> syncRequisiteData = compareFeData(compareData);
        data = data.stream().filter(i ->
                syncRequisiteData.contains(i.getId())).collect(Collectors.toList());
        saveSyncData(HcsaChecklistConstants.BATCH_JOB_SECTION_COMPARE_TABLE, data);
    }

    private void compareSyncSectionItem(){
        List<ChecklistSectionItemDto> data = chklClient.getAllSectionItem().getEntity();
        BeSyncCompareDataRequest compareData = new BeSyncCompareDataRequest();
        List<BeSyncCompareData> beSyncCompareData = IaisCommonUtils.genNewArrayList();

        data.forEach(i -> {
            BeSyncCompareData syncData = new BeSyncCompareData();
            syncData.setGuid(i.getId());
            syncData.setStatus(i.getStatus());
            beSyncCompareData.add(syncData);
        });

        compareData.setCompareTable(HcsaChecklistConstants.BATCH_JOB_SECTION_ITEM_COMPARE_TABLE);
        compareData.setBeSyncCompareData(beSyncCompareData);

        List<String> syncRequisiteData = compareFeData(compareData);
        data = data.stream().filter(i ->
                syncRequisiteData.contains(i.getId())).collect(Collectors.toList());
        saveSyncData(HcsaChecklistConstants.BATCH_JOB_SECTION_ITEM_COMPARE_TABLE, data);
    }

    private List<String> compareFeData(BeSyncCompareDataRequest itemCompare){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        List<BeSyncCompareDataResponse> compareDataResponses = beEicGatewayClient.compareFeData(itemCompare, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        return filterSyncData(compareDataResponses);
    }

    /**
     * if breakOperation is false , It means that this is a piece of data that needs to be synchronized
     * @param compareResult
     * @return
     */
    private List<String> filterSyncData(List<BeSyncCompareDataResponse> compareResult){
        return compareResult.stream().filter(i -> !i.isBreakOperation())
                .map(BeSyncCompareDataResponse::getGuid)
                .collect(Collectors.toList());
    }

    private <T> void saveSyncData(Integer affectTable, List<T> list){
        SyncDataBody syncDataBody = new SyncDataBody();
        syncDataBody.setAffectTable(affectTable);
        syncDataBody.setEntity(list);

        String toJson = JsonUtil.parseToJson(syncDataBody);
        log.info("sync checklist data from be " + toJson);

        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            beEicGatewayClient.saveSyncData(syncDataBody, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.info("sync checklist data from be failure" + e.getMessage());
        }
    }
}
