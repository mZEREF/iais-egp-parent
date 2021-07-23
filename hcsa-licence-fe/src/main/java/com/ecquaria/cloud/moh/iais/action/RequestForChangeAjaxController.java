package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * RequestForChangeAjaxController
 *
 * @author suocheng
 * @date 7/23/2021
 */
@Controller
@Slf4j
public class RequestForChangeAjaxController {
    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private LicenceViewService licenceViewService;

    @PostMapping(value = "/check-uen")
    public @ResponseBody AjaxResDto checkUen(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do checkUen start ...."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        String uen = ParamUtil.getString(request, "uen");
        log.info(StringUtil.changeForLog("uen is -->:"+uen));
        if (!StringUtil.isEmpty(uen)) {
            ajaxResDto.setResCode("200");
            Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
            chargesTypeAttr.put("name", "subLicensee");
            chargesTypeAttr.put("id", "subLicensee");
            //chargesTypeAttr.put("style", "display: none;");
            String chargeTypeSelHtml = NewApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr, getSelect(uen), NewApplicationDelegator.FIRESTOPTION, null, false);
            ajaxResDto.setResultJson(chargeTypeSelHtml);
        }
        log.info(StringUtil.changeForLog("the do checkUen end ...."));
        return ajaxResDto;
    }


    private List<SelectOption> getSelect(String uen){
        log.info(StringUtil.changeForLog("the getSelect start ...."));
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(uen)){
            OrganizationDto organizationDto = serviceConfigService.findOrganizationByUen(uen);
            if(organizationDto != null){
                List<SubLicenseeDto>  subLicenseeDtos = licenceViewService.getSubLicenseeDto(organizationDto.getId());
                if(!IaisCommonUtils.isEmpty(subLicenseeDtos)){
                    for(SubLicenseeDto subLicenseeDto : subLicenseeDtos){
                        result.add(new SelectOption(subLicenseeDto.getId(),subLicenseeDto.getDisplayName()));
                    }
                }else{
                    log.error(StringUtil.changeForLog("This orgId can not find out the subLicenseeDtos -->:"+organizationDto.getId()));
                }
            }else{
                log.info(StringUtil.changeForLog("This uen can not OrganizationDto -->:" +uen));
            }
        }else {
            log.info(StringUtil.changeForLog("The uen is null"));
        }
        result.add(new SelectOption("new","Add a new individual licensee"));
        log.info(StringUtil.changeForLog("the getSelect end ...."));
        return result;
    }
}
