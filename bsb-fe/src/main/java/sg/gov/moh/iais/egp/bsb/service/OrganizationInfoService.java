package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
@Service
@Slf4j
public class OrganizationInfoService {
    @Autowired
    private OrganizationInfoClient organizationInfoClient;

    public OrgAddressInfo getAddressByUen(String uen) {
        Assert.hasLength(uen, "uen cannot be null");
        OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
        try {
            LicenseeDto licenseeDto = organizationInfoClient.getLicenseeByUenNo(uen);
            if(licenseeDto ==null){
                licenseeDto = new LicenseeDto();
            }
            orgAddressInfo.setAddressType(licenseeDto.getAddrType());
            orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
            orgAddressInfo.setFloor(licenseeDto.getFloorNo());
            orgAddressInfo.setCompName(licenseeDto.getName());
            orgAddressInfo.setUen(licenseeDto.getUenNo());
            orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
            orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
            orgAddressInfo.setStreet(licenseeDto.getStreetName());
            orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("addressInfoByuen api exception"), e);
        }
        return orgAddressInfo;
    }
}
