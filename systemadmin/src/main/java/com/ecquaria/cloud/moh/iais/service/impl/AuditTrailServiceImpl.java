package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/16/2019 3:46 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.dto.AuditLogRecView;
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    @Autowired
    private AuditTrailClient trailClient;


    @SearchTrack(catalog = "systemAdmin", key = "queryFullModeAuditTrail")
    @Override
    public SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam) {
        return trailClient.listAuditTrailDto(searchParam).getEntity();
    }

    @Override
    public AuditTrailDto getAuditTrailById(String auditId) {
        return trailClient.getAuditTrailById(auditId).getEntity();
    }

    @Override
    public void addAuditLogRevToList(ArrayList<AuditLogRecView> list, Map<String, Object> map) {
        for (Map.Entry<String, Object> ent : map.entrySet()) {
            AuditLogRecView arv = new AuditLogRecView();
            arv.setColName(ent.getKey());
            arv.setColDetail(String.valueOf(ent.getValue()));
            arv.setLongText(String.valueOf(ent.getValue()));
            list.add(arv);
        }
    }

    @Override
    public ArrayList<AuditLogRecView> genAuditLogRecList(String detail) {
        ArrayList<AuditLogRecView> list = IaisCommonUtils.genNewArrayList();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        try {
            JsonNode jn = mapper.readTree(detail);
            if (jn.isArray()) {
                map = IaisCommonUtils.genNewHashMap(jn.size());
                for (JsonNode js : jn) {
                    Iterator<Map.Entry<String, JsonNode>> it = js.fields();
                    while (it.hasNext()) {
                        Map.Entry<String, JsonNode> ent = it.next();
                        map.put(ent.getKey(), ent.getValue());
                    }
                }
                if(map.isEmpty()){
                    map.put(detail,detail);
                }
            }else {
                map.put(detail,detail);
            }
        } catch (JsonProcessingException e) {
            map.put(detail,detail);
        }
        addAuditLogRevToList(list, map);
        return list;
    }

    @Override
    public void doSetAuditTrailExcel(AuditTrailExcelDto trailExcel, AuditTrailQueryDto trailQuery) {
        int domain = trailQuery.getDomain();
        int loginType = trailQuery.getLoginType();
        String nricNum = trailQuery.getNricNumber();
        String uenId = trailQuery.getUenId();
        String mohUserId = trailQuery.getMohUserId();
        String entityId = trailQuery.getEntityId();
        if (AuditTrailConsts.OPERATION_TYPE_BATCH_JOB == domain){
            trailExcel.setBatchjobId(entityId);
            trailExcel.setCreateBy(nricNum);
        }else if (AuditTrailConsts.OPERATION_TYPE_INTRANET == domain){
            trailExcel.setMohUserId(mohUserId);
            trailExcel.setCreateBy(mohUserId);
            trailExcel.setAdId(mohUserId);
        }else if (AuditTrailConsts.OPERATION_TYPE_INTERNET == domain){
            if (AuditTrailConsts.LOGIN_TYPE_SING_PASS == loginType){
                trailExcel.setSingpassId(nricNum);
            }else if (AuditTrailConsts.LOGIN_TYPE_CORP_PASS == loginType){
                trailExcel.setCorppassId(nricNum);
                trailExcel.setCorppassNric(nricNum);
                trailExcel.setUen(uenId);
            }
            trailExcel.setCreateBy(nricNum);
        }
        trailExcel.setOperation(trailQuery.getOperationDesc());
        trailExcel.setOperationType(trailQuery.getDomainDesc());
        trailExcel.setActionTime(trailQuery.getActionTime());
        trailExcel.setClientIp(trailQuery.getClientIp());
        trailExcel.setUserAgent(trailQuery.getUserAgent());
        trailExcel.setSessionId(trailQuery.getSessionId());
        trailExcel.setTotalSessionDuration(trailQuery.getTotalSessionDuration());
        trailExcel.setApplicationId(trailQuery.getAppNum());
        trailExcel.setLicenseNum(trailQuery.getLicenseNum());
        trailExcel.setModule(trailQuery.getModule());
        trailExcel.setFunctionName(trailQuery.getFunctionName());
        trailExcel.setProgrammeName(trailQuery.getProgrammeName());
        trailExcel.setDataActivities(trailQuery.getOperationDesc());
        trailExcel.setCreateDate(trailQuery.getActionTime());
    }
}
