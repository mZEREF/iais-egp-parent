package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SaMasterCodeClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author hc
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {

    @Autowired
    private SaMasterCodeClient saMasterCodeClient;
    @Autowired
    private EicGatewayClient eicGatewayClient;
    @Autowired
    private EicRequestTrackingHelper requestTrackingHelper;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    @SearchTrack(catalog = MasterCodeConstants.MSG_TEMPLATE_FILE, key = MasterCodeConstants.MSG_TEMPLATE_SQL)
    public SearchResult<MasterCodeQueryDto> doQuery(SearchParam param) {
        return saMasterCodeClient.doQuery(param).getEntity();
    }

    @Override
    public List<MasterCodeToExcelDto> findAllMasterCode() {
        return saMasterCodeClient.findAllMasterCode().getEntity();
    }

    @Override
    public MasterCodeDto saveMasterCode(MasterCodeDto masterCode) {
        return saMasterCodeClient.saveMasterCode(masterCode).getEntity();
    }

    @Override
    public MasterCodeDto updateMasterCode(MasterCodeDto masterCode) {
        return saMasterCodeClient.updateMasterCode(masterCode).getEntity();
    }

    @Override
    public void deleteMasterCodeById(String id) {
        saMasterCodeClient.delMasterCode(id).getEntity();
    }

    @Override
    public List<String> suggestCodeDescription(String codeDescription) {
        return saMasterCodeClient.suggestCodeDescription(codeDescription).getEntity();
    }

    @Override
    public MasterCodeDto findMasterCodeByMcId(String id) {
        FeignResponseEntity<MasterCodeDto> masterCodeById = saMasterCodeClient.getMasterCodeById(id);
        if (masterCodeById == null){
            return null;
        }
        return masterCodeById.getEntity();
    }

    @Override
    public String findCodeCategoryByDescription(String description) {
        return saMasterCodeClient.getCodeCategoryByDescription(description).getEntity();
    }

    @Override
    public List<MasterCodeCategoryDto> getAllCodeCategory() {
        return saMasterCodeClient.getAllMasterCodeCategory().getEntity();
    }

    @Override
    public List<MasterCodeCategoryDto> getCodeCategoryIsEdit() {
        return saMasterCodeClient.getMasterCodeCategoryIsEdit().getEntity();
    }

    @Override
    public MasterCodeCategoryDto getMasterCodeCategory(String categoryId) {
        return saMasterCodeClient.getMasterCodeCategory(categoryId).getEntity();
    }

    @Override
    public boolean masterCodeKeyIsExist(String masterCodekey) {
        return saMasterCodeClient.masterCodeKeyIsExist(masterCodekey).getEntity();
    }

    @Override
    public List<MasterCodeDto> saveMasterCodeList(List<MasterCodeToExcelDto> masterCodeToExcelDtoList) {
        return saMasterCodeClient.saveMasterCodeExcel(masterCodeToExcelDtoList).getEntity();
    }

    @Override
    public String findCodeKeyByDescription(String description) {
        return saMasterCodeClient.getCodeKeyByDescription(description).getEntity();
    }

    @Override
    public void inactiveMasterCode(AuditTrailDto auditTrailDto) {
        saMasterCodeClient.inactiveMasterCode(auditTrailDto);
    }

    @Override
    public void activeMasterCode(AuditTrailDto auditTrailDto) {
        saMasterCodeClient.activeMasterCode(auditTrailDto);
    }

    @Override
    public MasterCodeDto getMaxVersionMsDto(String masterCodeKey) {
        return saMasterCodeClient.getMaxVersionMsDto(masterCodeKey).getEntity();
    }

    @Override
    public List<MasterCodeDto> inactiveMsterCode(String masterCodeKey) {
        return saMasterCodeClient.inactiveMasterCodeByKey(masterCodeKey).getEntity();
    }

    @Override
    public void syncMasterCodeFe(List<MasterCodeDto> masterCodeDtos) {
        if (IaisCommonUtils.isEmpty(masterCodeDtos)) {
            return;
        }

        eicGatewayClient.callEicWithTrack(masterCodeDtos, "syncMasterCodeFe", this.getClass(),"syncMasterCodeFeFromTrack");

       /* // 1) Create and save the tracking record into DB before everything
        EicRequestTrackingDto track = requestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.SYSTEM_ADMIN_CLIENT,
                MasterCodeService.class.getName(), "syncMasterCodeFeFromTrack", currentApp + "-" + currentDomain,
                String.class.getName(), JsonUtil.parseToJson(masterCodeDtos));
        //2) Before executing the EIC function set the running data
        track.setProcessNum(track.getProcessNum() + 1);
        Date now = new Date();
        if (track.getFirstActionAt() == null) {
            track.setFirstActionAt(now);
        }
        track.setLastActionAt(now);
        try {
            syncMasterCodeFeWithoutTrack(masterCodeDtos);
            // 4a) If success then update the tracking status to complete
            track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            // 4b) If failed, still needs to update the running data to DB.
            track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
            log.error(e.getMessage(), e);
        }
        // 5) save track
        requestTrackingHelper.saveEicTrack(EicClientConstant.SYSTEM_ADMIN_CLIENT, track);*/
    }

    private void syncMasterCodeFeWithoutTrack(List<MasterCodeDto> masterCodeDtos) {
        if (IaisCommonUtils.isEmpty(masterCodeDtos)) {
            return;
        }
        eicGatewayClient.syncMasterCodeFe(masterCodeDtos);
    }

    @Override
    public void syncMasterCodeFeFromTrack(String jsonList) {
        if (StringUtil.isEmpty(jsonList)) {
            return;
        }
        List<MasterCodeDto> masterCodeDtos = JsonUtil.parseToList(jsonList, MasterCodeDto.class);
        syncMasterCodeFeWithoutTrack(masterCodeDtos);
    }
}
