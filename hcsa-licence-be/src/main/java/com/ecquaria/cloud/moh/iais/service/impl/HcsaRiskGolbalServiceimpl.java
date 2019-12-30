package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskGolbalService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 9:44
 */
@Service
@Slf4j
public class HcsaRiskGolbalServiceimpl implements HcsaRiskGolbalService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Override
    public GolbalRiskShowDto getGolbalRiskShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        GolbalRiskShowDto showDto = hcsaConfigClient.getgolbalshow(serviceDtoList).getEntity();
        return showDto;
    }

    @Override
    public List<SelectOption> getAutoOp() {
        List<SelectOption> autoRenew = new ArrayList<>();
        SelectOption op = new SelectOption();
        op.setValue("Y");
        op.setText("Yes");
        SelectOption op2 = new SelectOption();
        op2.setValue("N");
        op2.setText("No");
        autoRenew.add(op);
        autoRenew.add(op2);
        return autoRenew;
    }
}
