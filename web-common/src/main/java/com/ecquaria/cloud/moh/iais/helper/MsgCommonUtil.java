package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public void setRecriptByLicenseeId(List<String> roles ,String refId, InspectionEmailTemplateDto inspectionEmailTemplateDto){
        Set<String> emailSet = new HashSet<>();
        if(roles != null && roles.contains(NotificationHelper.RECEIPT_ROLE_LICENSEE_ALL)){
            //this refId is licensee id
            List<String> emailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(refId);
            emailSet.addAll(emailAddrs);
        }else{
            //this refId is license id
            LicenseeDto licensee = getLicenseeById(refId);
            if (licensee != null && AppConsts.COMMON_STATUS_ACTIVE.equals(licensee.getStatus())){
                String emailAddr = licensee.getEmilAddr();
                emailSet.add(emailAddr);
            }
        }
        inspectionEmailTemplateDto.setReceiptEmails(new ArrayList<>(emailSet));
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
