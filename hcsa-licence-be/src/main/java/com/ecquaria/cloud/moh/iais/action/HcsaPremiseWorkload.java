package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaPrimiseWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;


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
        List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = hcsaConfigClient.getHcsaPremisesWorkload(type).getEntity();
        ParamUtil.setSessionAttr(bpc.request,"hcsaPrimiseWorkloadDtos",(Serializable) hcsaPrimiseWorkloadDtos);
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
        List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = (List<HcsaPrimiseWorkloadDto>)ParamUtil.getSessionAttr(bpc.request,"hcsaPrimiseWorkloadDtos");
        for (HcsaPrimiseWorkloadDto item:hcsaPrimiseWorkloadDtos) {
            HcsaPrimiseWorkloadDto hcsaPrimiseWorkloadDto = new HcsaPrimiseWorkloadDto();
            String name = item.getStageId();
            String manhour = ParamUtil.getString(request, name);
            if(manhour != null && !manhour.equals(Integer.toString(item.getManhourCount()))){
                hcsaPrimiseWorkloadDto.setId(item.getId());
                hcsaPrimiseWorkloadDto.setStageDesc(item.getStageDesc());
                hcsaPrimiseWorkloadDto.setManhourCount(Integer.parseInt(manhour));
                hcsaPrimiseWorkloadDto.setPremisesType(item.getPremisesType());
                hcsaPrimiseWorkloadDto.setStageDesc(item.getStageDesc());
                hcsaPrimiseWorkloadDto.setStageId(item.getStageId());
                hcsaPrimiseWorkloadDto.setStatus(item.getStatus());
                saveDtos.add(hcsaPrimiseWorkloadDto);
            }
        }
        hcsaConfigClient.savePremiseWorkload(saveDtos);
    }
}
