package com.ecquaria.cloud.moh.iais.validate.serviceInfo;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/29 15:30
 */
@Component
@Slf4j
public class ValidateCharges implements ValidateFlow {

    @Override
    public void doValidateCharges(Map<String, String> map, AppSvcChargesPageDto appSvcClinicalDirectorDto) {
        if (appSvcClinicalDirectorDto == null) {
            return;
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcChargesDto> generalChargesDtos = appSvcClinicalDirectorDto.getGeneralChargesDtos();
        doValidateGeneralCharges(errorMap, generalChargesDtos);
        List<AppSvcChargesDto> otherChargesDtos = appSvcClinicalDirectorDto.getOtherChargesDtos();
        doValidateOtherCharges(errorMap, otherChargesDtos);

        if (map != null) {
            map.putAll(errorMap);
        }
        log.info(StringUtil.changeForLog("====> ValidateCharges->" + JsonUtil.parseToJson(errorMap)));
    }

    protected void doValidateGeneralCharges(Map<String, String> map,List<AppSvcChargesDto> generalChargesDtos){
        if(generalChargesDtos==null || generalChargesDtos.isEmpty()){
            return;
        }

        for(int i=0;i< generalChargesDtos.size() ; i++){
            String chargesType = generalChargesDtos.get(i).getChargesType();
            if(StringUtil.isEmpty(chargesType)){
                map.put("chargesType"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Charge", "field"));
            }
            boolean flag=false;
            String minAmount = generalChargesDtos.get(i).getMinAmount();
            if(StringUtil.isEmpty(minAmount)){
                map.put("minAmount"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
            }else {
                if(minAmount.length()>4){
                    String general_err0041= NewApplicationHelper.repLength("Amount","4");
                    map.put("minAmount"+i,general_err0041);
                }else if(!minAmount.matches("^[0-9]+$")){
                    map.put("minAmount"+i,"GENERAL_ERR0002");
                }else {
                    flag=true;
                }

            }
            String maxAmount = generalChargesDtos.get(i).getMaxAmount();
            if(!StringUtil.isEmpty(maxAmount)){
                if(maxAmount.length() > 4){
                    String general_err0041= NewApplicationHelper.repLength("Amount","4");
                    map.put("maxAmount"+i,general_err0041);
                }else if(!maxAmount.matches("^[0-9]+$")){
                    map.put("maxAmount"+i,"GENERAL_ERR0002");
                }else if(flag) {
                    int min = Integer.parseInt(minAmount);
                    int max = Integer.parseInt(maxAmount);
                    if(min> max){
                        map.put("maxAmount"+i,MessageUtil.getMessageDesc("NEW_ERR0027"));
                    }
                }
            }
            String remarks = generalChargesDtos.get(i).getRemarks();
            if(!StringUtil.isEmpty(remarks)&&remarks.length() > 150){
                String general_err0041= NewApplicationHelper.repLength("Remarks","150");
                map.put("remarks"+i,general_err0041);
            }
        }
    }
    protected void doValidateOtherCharges(Map<String, String> map,List<AppSvcChargesDto> otherChargesDtos){
        if(otherChargesDtos==null || otherChargesDtos.isEmpty()){
            return;
        }
        for(int i=0;i < otherChargesDtos.size();i++){
            String chargesType = otherChargesDtos.get(i).getChargesType();
            if(StringUtil.isEmpty(chargesType)){
                map.put("otherChargesType"+i,MessageUtil.replaceMessage("GENERAL_ERR0006", "Category", "field"));
            }
            String chargesCategory = otherChargesDtos.get(i).getChargesCategory();
            if(StringUtil.isEmpty(chargesCategory)){
                map.put("otherChargesCategory"+i,MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Charge", "field"));
            }
            boolean flag=false;
            String minAmount = otherChargesDtos.get(i).getMinAmount();
            String errMsg=MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field");
            if(StringUtil.isEmpty(minAmount)){
                map.put("otherAmountMin"+i,errMsg);
            }else {
                if(minAmount.length()>4){
                    String general_err0041= NewApplicationHelper.repLength("Amount","4");
                    map.put("otherAmountMin"+i,general_err0041);
                }else if(!minAmount.matches("^[0-9]+$")){
                    map.put("otherAmountMin"+i,"GENERAL_ERR0002");
                }else {
                    flag=true;
                }
            }
            String maxAmount = otherChargesDtos.get(i).getMaxAmount();
            if(!StringUtil.isEmpty(maxAmount)){
                if(maxAmount.length()>4){
                    String general_err0041= NewApplicationHelper.repLength("Amount","4");
                    map.put("otherAmountMax"+i,general_err0041);
                }else if(!maxAmount.matches("^[0-9]+$")){
                    map.put("otherAmountMax"+i,"GENERAL_ERR0002");
                }else if(flag){
                    int min = Integer.parseInt(minAmount);
                    int max = Integer.parseInt(maxAmount);
                    if(min > max){
                        map.put("otherAmountMax"+i,MessageUtil.getMessageDesc("NEW_ERR0027"));
                    }
                }
            }
            String remarks = otherChargesDtos.get(i).getRemarks();
            if(!StringUtil.isEmpty(remarks) && remarks .length() >150){
                String general_err0041= NewApplicationHelper.repLength("Remarks","150");
                map.put("otherRemarks"+i,general_err0041);
            }
        }
    }

    private  boolean isMoney(String amount){
        //Only integers are allowed (mingde say)
        try {
            Integer.parseInt(amount);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
