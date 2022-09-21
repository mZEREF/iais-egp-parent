package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        List<String> allDataSubmissionRoleIds = getAllRoleIdsInUserRole(roles);
        if(roles != null && (roles.contains(NotificationHelper.RECEIPT_ROLE_LICENSEE_ALL)
                || IaisCommonUtils.isNotEmpty(allDataSubmissionRoleIds))){
            //this refId is licensee id
            List<String> emailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(refId);
            emailSet.addAll(emailAddrs);
        } else{
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

    public List<String> getAllRoleIdsInUserRole(List<String> roles){
        List<String> allRoleIds = new ArrayList<>();
        if(roles != null && !roles.isEmpty()){
            for (String role : roles) {
                switch (role){
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_AR_SUBMITTER:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_AR);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_DP_SUBMITTER:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_DP);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_VS_SUBMITTER:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_VSS);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_LDT_SUBMITTER:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_LDT);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_TOP_SUBMITTER:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_TOP);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_AR_SUPERVISOR:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_AR_SUPERVISOR);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_DP_SUPERVISOR:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_DP_SUPERVISOR);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_VS_SUPERVISOR:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_VSS_SUPERVISOR);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_LDT_SUPERVISOR:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_LDT_SUPERVISOR);
                        break;
                    case NotificationHelper.RECEIPT_ROLE_LICENSEE_TOP_SUPERVISOR:
                        allRoleIds.add(RoleConsts.USER_ROLE_DS_TOP_SUPERVISOR);
                        break;
                    default:
                        break;
                }
            }
        }

        return allRoleIds;
    }
}
