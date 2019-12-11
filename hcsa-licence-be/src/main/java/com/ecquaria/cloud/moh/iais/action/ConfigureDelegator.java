package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ConfigureDelegator
 *
 * @author Guyin
 * @date 2019/11/26 14:07
 */

@Delegator("configureDelegator")
@Slf4j
public class ConfigureDelegator {

    @Autowired
    private ConfigureService configureService;

    private List<HcsaSvcRoutingStageDto> stageNames = new ArrayList<>();

    private List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtoList = new ArrayList<>();

    /**
     * StartStep: AssignedInspectionTask
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("hcsa inspection", "ConfigureDelegator");

        HttpServletRequest request = bpc.request;
        this.stageNames = configureService.listStage();
        List<SelectOption> stageSelect = new ArrayList<>();

        for(HcsaSvcRoutingStageDto sn : stageNames){
            stageSelect.add(new SelectOption(sn.getStageCode(), sn.getStageName()));
        }
        ParamUtil.setRequestAttr(request, "stageSelect", stageSelect);

    }

    /**
     * AutoStep: serviceInStage
     *
     * @param bpc
     * @throws
     */
    public void serviceInStage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String stageValue = ParamUtil.getString(request , "stageSelect");
        hcsaSvcStageWorkloadDtoList = configureService.serviceInStage(stageValue);
        ParamUtil.setRequestAttr(request, "stageValue", stageValue);
        ParamUtil.setRequestAttr(request, "stageList", hcsaSvcStageWorkloadDtoList);
    }

    /**
     * AutoStep: submit
     *
     * @param bpc
     * @throws
     */
    public void submit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailDto intr = AuditTrailHelper.getBatchJobDto("INTRANET");
        List<HcsaSvcSpecificStageWorkloadDto> stageSaveData = new ArrayList<>();
        List<HcsaSvcSpecificStageWorkloadDto> stageUpdateData = new ArrayList<>();
        for (HcsaSvcStageWorkloadDto item:hcsaSvcStageWorkloadDtoList) {
            HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
            HcsaSvcSpecificStageWorkloadDto inactivehcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
            String manhour = ParamUtil.getString(request, item.getServiceName());
            if(!manhour.equals(item.getManhourCount().toString())){
                hcsaSvcSpecificStageWorkloadDto.setAppType(item.getAppType());
                hcsaSvcSpecificStageWorkloadDto.setAuditTrailDto(intr);
                hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(manhour));
                hcsaSvcSpecificStageWorkloadDto.setServiceId(item.getServiceId());
                hcsaSvcSpecificStageWorkloadDto.setStageId(item.getStageId());
                hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                stageSaveData.add(hcsaSvcSpecificStageWorkloadDto);

                inactivehcsaSvcSpecificStageWorkloadDto.setAppType(item.getAppType());
                inactivehcsaSvcSpecificStageWorkloadDto.setAuditTrailDto(intr);
                inactivehcsaSvcSpecificStageWorkloadDto.setId(item.getId());
                inactivehcsaSvcSpecificStageWorkloadDto.setManhourCount(item.getManhourCount());
                inactivehcsaSvcSpecificStageWorkloadDto.setServiceId(item.getServiceId());
                inactivehcsaSvcSpecificStageWorkloadDto.setStageId(item.getStageId());
                inactivehcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
                stageUpdateData.add(inactivehcsaSvcSpecificStageWorkloadDto);
            }
        }
        Map<String , List<HcsaSvcSpecificStageWorkloadDto>> stageMap = new HashMap<String,List<HcsaSvcSpecificStageWorkloadDto>>();
        stageMap.put("update",stageUpdateData);
        stageMap.put("save",stageSaveData);
        configureService.saveStage(stageMap);
    }



}
