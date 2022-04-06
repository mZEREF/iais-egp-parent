package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.singpostAddress.SingpostAddressDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.AddressInfoClient;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AddressInfo;


@Service
@Slf4j
public class AddressRetrieveService {

    @Autowired
    private AddressInfoClient addressInfoClient;

    public AddressInfo getAddressByPostalCode(String postalCode) {
        Assert.hasLength(postalCode, "postalCode cannot be null");
        AddressInfo addressInfo = new AddressInfo();
        try {
            SingpostAddressDto addressDto = addressInfoClient.getAddressInfoByPostalCode(postalCode);
            if(addressDto ==null){
                addressDto = new SingpostAddressDto();
            }
            addressInfo.setAddressType(addressDto.getAddressType());
            addressInfo.setPostalCode(addressDto.getPostalCode());
            addressInfo.setBlockNo(addressDto.getBlkHseNo());
            addressInfo.setBuilding(addressDto.getBuildingName());
            addressInfo.setStreet(addressDto.getStreetName());
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("addressInfoByPostalCode api exception"),e);
        }
        return addressInfo;
    }


}


