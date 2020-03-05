package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
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
        stageNames = configureService.listStage();
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
        String stageId = "";
        for(HcsaSvcRoutingStageDto sn : stageNames){
            if(sn.getStageCode().equals(stageValue)){
                stageId = sn.getId();
            }
        }
        hcsaSvcStageWorkloadDtoList = configureService.serviceInStage(stageValue);
        List<HcsaServiceDto> hcsaServiceDtoList = configureService.getActiveServices();
        Map<String,HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtoMap = new HashMap<>();
        List<HcsaSvcStageWorkloadDto> newlist = new ArrayList<>();
        if(hcsaSvcStageWorkloadDtoList != null){
            for (HcsaSvcStageWorkloadDto stage:hcsaSvcStageWorkloadDtoList
            ) {
                hcsaSvcStageWorkloadDtoMap.put(stage.getServiceId(),stage);
            }
        }

        for (HcsaServiceDto item:hcsaServiceDtoList
             ) {
            if(hcsaSvcStageWorkloadDtoMap.get(item.getId()) != null){
                newlist.add(hcsaSvcStageWorkloadDtoMap.get(item.getId()));
            }else{
                HcsaSvcStageWorkloadDto hcsa = new HcsaSvcStageWorkloadDto();
                hcsa.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsa.setManhourCount(0);
                hcsa.setServiceId(item.getId());
                hcsa.setServiceName(item.getSvcName());
                hcsa.setStageId(stageId);
                hcsa.setAppType("APTY001");
                newlist.add(hcsa);
            }
        }
        hcsaSvcStageWorkloadDtoList = newlist;
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
        List<HcsaSvcSpecificStageWorkloadDto> stageSaveData = new ArrayList<>();
        int index = 1;
        for (HcsaSvcStageWorkloadDto item:hcsaSvcStageWorkloadDtoList) {
            HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
            String name = "service"+index;
            String manhour = ParamUtil.getString(request, name);
            if(manhour != null && !manhour.equals(item.getManhourCount().toString())){
                if(item.getId() != null){
                    hcsaSvcSpecificStageWorkloadDto.setId(item.getId());
                }
                hcsaSvcSpecificStageWorkloadDto.setAppType(item.getAppType());
                hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(manhour));
                hcsaSvcSpecificStageWorkloadDto.setServiceId(item.getServiceId());
                hcsaSvcSpecificStageWorkloadDto.setStageId(item.getStageId());
                hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                stageSaveData.add(hcsaSvcSpecificStageWorkloadDto);
            }
            index ++;
        }
        configureService.saveStage(stageSaveData);
    }



}
