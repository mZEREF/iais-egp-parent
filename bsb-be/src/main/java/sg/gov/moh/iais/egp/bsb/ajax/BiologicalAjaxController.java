package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.dto.enquiry.BiologicalDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/6 14:32
 **/

@Slf4j
@Controller
@RequestMapping("/bio-info")
public class BiologicalAjaxController {
    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    @RequestMapping(value = "bio.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> queryBiologicalBySchedule(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
         String schedule = ParamUtil.getString(request,"schedule");
         if(StringUtils.isEmpty(schedule)){schedule="null";}
         log.info(StringUtil.changeForLog("ajax query schedule"+schedule));
         List<BiologicalDto> biologicalDtoList = biosafetyEnquiryClient.queryBiologicalBySchedule(schedule).getEntity();
        if(biologicalDtoList != null && !biologicalDtoList.isEmpty()) {
            log.info(StringUtil.changeForLog("ajax biologicalDtoList "+biologicalDtoList));
            List<String> strings = IaisCommonUtils.genNewArrayList();
            for (BiologicalDto biologicalDto : biologicalDtoList) {
                strings.add(biologicalDto.getName());
            }
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",strings);
        } else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }

}
