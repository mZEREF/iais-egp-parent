package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AddressInfo;
import sg.gov.moh.iais.egp.bsb.service.AddressRetrieveService;

/**
 * @author Fanghao
 * @version 2022/3/30
 **/


@Slf4j
@RestController
@RequestMapping("/address-info")
public class AddressRetrieveAjaxController {
    @Autowired
    private AddressRetrieveService addressRetrieveService;
    /**
     * This method is used to retrieve address information according to postal code
     * queryAddressInfo
     * @return AddressInfo
     * */
    @GetMapping(value = "/{postal-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressInfo retrieveAddress(@PathVariable("postal-code") String postalCode) {
        log.info(StringUtil.changeForLog("retrieveAddress is start postalCode is"+ LogUtil.escapeCrlf(postalCode)));
        AddressInfo addressInfo =null;
        try {
            addressInfo = addressRetrieveService.getAddressByPostalCode(postalCode);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("addressInfoByPostalCode api exception"),e);
        }
        return addressInfo;
    }
}