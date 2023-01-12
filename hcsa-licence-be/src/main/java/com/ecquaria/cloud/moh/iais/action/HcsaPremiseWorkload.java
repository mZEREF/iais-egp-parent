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
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Comparator;
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

    public static final String HCSA_PRIMISE_WORKLOAD_DTOS                     = "hcsaPrimiseWorkloadDtos";
    public static final String FIELD                     = "field";

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
        List<SelectOption> premisesType = LicenceUtil.getPremisesType();
        premisesType.sort(Comparator.comparing(SelectOption::getText));
        ParamUtil.setRequestAttr(bpc.request,"primiseType",premisesType);
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
            errMap.put("stageSelect", MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Types of Premises",FIELD));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }else{
            List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = hcsaConfigClient.getHcsaPremisesWorkload(type).getEntity();
            ParamUtil.setSessionAttr(bpc.request,HCSA_PRIMISE_WORKLOAD_DTOS,(Serializable) hcsaPrimiseWorkloadDtos);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
            ParamUtil.setSessionAttr(bpc.request, "premiseWorkloadType", getType(type));
        }
    }

    private String getType(String type){
        String res = "";
        switch (type){
            /*case ApplicationConsts.PREMISES_TYPE_ON_SITE:
                res = ApplicationConsts.PREMISES_TYPE_ON_SITE_SHOW;
                break;
            case ApplicationConsts.PREMISES_TYPE_OFF_SITE:
                res = "Offsite";
                break;*/
            case ApplicationConsts.PREMISES_TYPE_CONVEYANCE:
                res = "Conveyance";
                break;
            default:
                break;
        }
        return res;
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
        List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos = (List<HcsaPrimiseWorkloadDto>)ParamUtil.getSessionAttr(bpc.request,HCSA_PRIMISE_WORKLOAD_DTOS);
        for (HcsaPrimiseWorkloadDto item:hcsaPrimiseWorkloadDtos) {
            HcsaPrimiseWorkloadDto hcsaPrimiseWorkloadDto = new HcsaPrimiseWorkloadDto();
            String name = item.getStageId();
            String manhour = ParamUtil.getString(request, name);
            if(!StringUtil.isEmpty(manhour) && StringUtils.isNumeric(manhour) && manhour.length() <= 2){
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
                    errMap.put(name,MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Workload Manhours",FIELD));
                } else if (manhour.length() > 2) {
                    item.setManhourCount(manhour);
                    Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
                    map.put(FIELD, "Workload Manhours");
                    map.put("maxlength", "2");
                    errMap.put(name,MessageUtil.getMessageDesc("GENERAL_ERR0041", map));
                } else if(!StringUtils.isNumeric(manhour)){
                    item.setManhourCount(manhour);
                    errMap.put(name,MessageUtil.getMessageDesc("SC_ERR007"));
                }else{
                    item.setManhourCount(manhour);
                    errMap.put(name,MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Workload Manhours",FIELD));
                }
            }
        }
        if(errMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
            hcsaConfigClient.savePremiseWorkload(saveDtos);
        }else{
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(bpc.request,HCSA_PRIMISE_WORKLOAD_DTOS,(Serializable) hcsaPrimiseWorkloadDtos);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }

    }
}
