package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaPrimiseWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * ConfigureDelegator
 *
 * @author Guyin
 * @date 2019/11/26 14:07
 */

@Delegator("premiseWorkload")
@Slf4j
public class HcsaPremiseWorkload {

    @Autowired
    HcsaConfigClient hcsaConfigClient;
    /**
     * StartStep: start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){

    }

    /**
     * AutoStep: preLoad
     *
     * @param bpc
     * @throws
     */
    public void preLoad(BaseProcessClass bpc){
        List<SelectOption> primiseSelection = IaisCommonUtils.genNewArrayList();
        primiseSelection.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_ON_SITE,ApplicationConsts.PREMISES_TYPE_ON_SITE_SHOW));
        primiseSelection.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_OFF_SITE,ApplicationConsts.PREMISES_TYPE_OFF_SITE_SHOW));
        primiseSelection.add(new SelectOption(ApplicationConsts.PREMISES_TYPE_CONVEYANCE,ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW));
        ParamUtil.setRequestAttr(bpc.request,"primiseType",primiseSelection);
    }

    /**
     * AutoStep: premiseWorkload
     *
     * @param bpc
     * @throws
     */
    public void premiseWorkload(BaseProcessClass bpc){
        String type = ParamUtil.getRequestString(bpc.request,"stageSelect");
        if(StringUtil.isEmpty(type)){
            Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("stageSelect", MessageUtil.replaceMessage("GENERAL_ERR0006","Types of Premise","field"));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }else{
            List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = hcsaConfigClient.getHcsaPremisesWorkload(type).getEntity();
            ParamUtil.setSessionAttr(bpc.request,"hcsaPrimiseWorkloadDtos",(Serializable) hcsaPrimiseWorkloadDtos);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }
        }

    public void goToPremise(BaseProcessClass bpc){

    }

    /**
     * AutoStep: save
     *
     * @param bpc
     * @throws
     */
    public void save(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        List<HcsaPrimiseWorkloadDto> saveDtos = IaisCommonUtils.genNewArrayList();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = (List<HcsaPrimiseWorkloadDto>)ParamUtil.getSessionAttr(bpc.request,"hcsaPrimiseWorkloadDtos");
        for (HcsaPrimiseWorkloadDto item:hcsaPrimiseWorkloadDtos) {
            HcsaPrimiseWorkloadDto hcsaPrimiseWorkloadDto = new HcsaPrimiseWorkloadDto();
            String name = item.getStageId();
            String manhour = ParamUtil.getString(request, name);
            if(!StringUtil.isEmpty(manhour) && StringUtils.isNumeric(manhour)){
                item.setManhourCount(manhour);
                hcsaPrimiseWorkloadDto.setId(item.getId());
                hcsaPrimiseWorkloadDto.setStageDesc(item.getStageDesc());
                hcsaPrimiseWorkloadDto.setManhourCount(manhour);
                hcsaPrimiseWorkloadDto.setPremisesType(item.getPremisesType());
                hcsaPrimiseWorkloadDto.setStageDesc(item.getStageDesc());
                hcsaPrimiseWorkloadDto.setStageId(item.getStageId());
                hcsaPrimiseWorkloadDto.setStatus(item.getStatus());
                saveDtos.add(hcsaPrimiseWorkloadDto);
            }else{
                if(StringUtil.isEmpty(manhour)){
                    item.setManhourCount(null);
                    errMap.put(name,MessageUtil.replaceMessage("GENERAL_ERR0006","Workload Manhours","field"));
                }else if(!StringUtils.isNumeric(manhour)){
                    item.setManhourCount(manhour);
                    errMap.put(name,MessageUtil.getMessageDesc("GENERAL_ERR0027"));
                }else{
                    item.setManhourCount(manhour);
                    errMap.put(name,MessageUtil.replaceMessage("GENERAL_ERR0006","Workload Manhours","field"));
                }
            }
        }
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
            hcsaConfigClient.savePremiseWorkload(saveDtos);
        }else{
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(bpc.request,"hcsaPrimiseWorkloadDtos",(Serializable) hcsaPrimiseWorkloadDtos);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }

    }
}
