package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;

/**
 * @author Fanghao
 * @version 2022/4/6
 **/


@Slf4j
@RestController
@RequestMapping("/org-address-info")
public class OrganizationInfoAjaxController {

    @Autowired
    private OrganizationInfoService organizationInfoService;
    /**
     * This method is used to retrieve organization information according to uen
     * orgAddressInfoByUen
     * @return OrgAddressInfo
     * */
    @GetMapping(value = "/uen/{uen}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrgAddressInfo getOrgAddressInfoByUen(@PathVariable("uen") String uen) {
        log.info(StringUtil.changeForLog("getOrgAddressInfoByUen is start uen is"+ LogUtil.escapeCrlf(uen)));
        OrgAddressInfo orgAddressInfo =null;
        try {
            orgAddressInfo = organizationInfoService.getAddressByUen(uen);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("orgAddressInfoByUen api exception"),e);
        }
        return orgAddressInfo;
    }
}
