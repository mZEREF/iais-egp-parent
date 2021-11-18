package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yichen
 * @Date:2021/4/15
 */
@Component
public class MsgCommonUtil {
    @Autowired
    private LicenseeClient licenseeClient;

    public LicenseeDto getLicenseeById(String licenseeId){
        return licenseeClient.getLicenseeDtoById(licenseeId).getEntity();
    }

    public List<LicenseeKeyApptPersonDto> getPersonById(String licenseeId){
        return licenseeClient.getPersonByid(licenseeId).getEntity();
    }

    public void setRecriptByLicenseeId(String refId, InspectionEmailTemplateDto inspectionEmailTemplateDto){
        LicenseeDto licensee = getLicenseeById(refId);
        if (licensee != null && AppConsts.COMMON_STATUS_ACTIVE.equals(licensee.getStatus())){
            String emailAddr = licensee.getEmilAddr();
            List<String> emailList = new ArrayList<>();
            emailList.add(emailAddr);
            inspectionEmailTemplateDto.setReceiptEmails(emailList);
        }
    }

    public List<String> getEmailAddressListByLicenseeId(List<String> licenseeIdList){
        return licenseeClient.getEmailAddressListByLicenseeId(licenseeIdList).getEntity();
    }

    public void addLicenseeMobile(String licenseeId, List<String> mobileNoList){
        LicenseeDto licensee = getLicenseeById(licenseeId);
        if (Optional.ofNullable(licensee).isPresent()){
            mobileNoList.add(licensee.getOfficeTelNo());
        }
    }
}
