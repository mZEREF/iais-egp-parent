package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;

import javax.servlet.http.HttpServletRequest;


@Service
@Slf4j
public class OrganizationInfoService {
    public static final String KEY_ORG_ADDRESS = "organizationAddress";

    private final OrganizationInfoClient organizationInfoClient;

    @Autowired
    public OrganizationInfoService(OrganizationInfoClient organizationInfoClient) {
        this.organizationInfoClient = organizationInfoClient;
    }

    /**
     * Retrieve organization address by UEN, and return a good-looking JSON.
     * @return company and address info if found; null if not found
     */
    public OrgAddressInfo getAddressByUen(String uen) {
        Assert.hasLength(uen, "uen cannot be null");
        OrgAddressInfo orgAddressInfo = null;
        try {
            LicenseeDto licenseeDto = organizationInfoClient.getLicenseeByUenNo(uen);
            if (licenseeDto != null) {
                orgAddressInfo = new OrgAddressInfo();
                orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
                orgAddressInfo.setFloor(licenseeDto.getFloorNo());
                orgAddressInfo.setCompName(licenseeDto.getName());
                orgAddressInfo.setUen(licenseeDto.getUenNo());
                orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
                orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
                orgAddressInfo.setStreet(licenseeDto.getStreetName());
                orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
            }
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("retrieve address by UEN API hit error"), e);
        }
        return orgAddressInfo;
    }

    public void retrieveOrgAddressInfo(HttpServletRequest request) {
        AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        assert auditTrailDto != null;
        LicenseeDto licenseeDto = organizationInfoClient.getLicenseeByUenNo(auditTrailDto.getUenId());
        OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
        orgAddressInfo.setUen(auditTrailDto.getUenId());
        orgAddressInfo.setCompName(licenseeDto.getName());
        orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
        orgAddressInfo.setAddressType(licenseeDto.getAddrType());
        orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
        orgAddressInfo.setFloor(licenseeDto.getFloorNo());
        orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
        orgAddressInfo.setStreet(licenseeDto.getStreetName());
        orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
        ParamUtil.setSessionAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);
    }
}
