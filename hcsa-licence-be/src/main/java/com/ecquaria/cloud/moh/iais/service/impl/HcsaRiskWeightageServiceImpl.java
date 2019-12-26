package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskWeightageService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 13:29
 */
@Service
@Slf4j
public class HcsaRiskWeightageServiceImpl implements HcsaRiskWeightageService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;

    @Override
    public HcsaRiskWeightageShowDto getWeightage() {
        List<HcsaServiceDto> serviceDtoList =  hcsaConfigClient.getActiveServices().getEntity();
        HcsaRiskWeightageShowDto showDto = hcsaConfigClient.getWeightageShow(serviceDtoList).getEntity();
        return showDto;
    }

    @Override
    public void getOneWdto(HcsaRiskWeightageDto wDto, String lastInp, String secLastInp, String finan, String leadership, String legislative, String inStartDate, String inEndDate) {
        int editNum = 0;
        if(!StringUtil.isEmpty(wDto.getId())){
            if(!wDto.getBaseLastInp().equals(lastInp)){
                editNum++;
            }
            wDto.setDoLastInp(lastInp);
            if(!wDto.getBaseSecLastInp().equals(secLastInp)){
                editNum++;
            }
            wDto.setDoSecLastInp(secLastInp);
            if(!wDto.getBaseFinancial().equals(finan)){
                editNum++;
            }
            wDto.setDoFinancial(finan);
            if(!wDto.getBaseLeadship().equals(leadership)){
                editNum++;
            }
            wDto.setDoLeadship(leadership);
            if(!wDto.getBaseLegislative().equals(legislative)){
                editNum++;
            }
            wDto.setDoLegislative(legislative);
            if(!wDto.getBaseEffectiveDate().equals(inStartDate)){
                editNum++;
            }
            wDto.setDoEffectiveDate(inStartDate);
            if(!wDto.getBaseEndDate().equals(inEndDate)){
                editNum++;
            }
            wDto.setDoEndDate(inEndDate);
            if(editNum>0){
                wDto.setEdit(true);
            }else{
                wDto.setEdit(false);
            }
        }else{
            boolean editfalg =isEdit(lastInp,secLastInp,finan,leadership,legislative,inStartDate,inEndDate);
            if(editfalg){
                wDto.setDoLastInp(lastInp);
                wDto.setDoSecLastInp(secLastInp);
                wDto.setDoFinancial(finan);
                wDto.setDoLeadship(leadership);
                wDto.setDoLegislative(legislative);
                wDto.setDoEffectiveDate(inStartDate);
                wDto.setDoEndDate(inEndDate);
            }
        }
    }
    boolean isEdit(String lastInp, String secLastInp, String finan, String leadership, String legislative, String inStartDate, String inEndDate){
        if(StringUtil.isEmpty(lastInp)&&StringUtil.isEmpty(secLastInp)&&StringUtil.isEmpty(finan)&&StringUtil.isEmpty(leadership)&&StringUtil.isEmpty(legislative)&&StringUtil.isEmpty(inStartDate)
                &&StringUtil.isEmpty(inEndDate)){
            return false;
        }else{
            return true;
        }
    }
}
