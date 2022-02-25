package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/bio-info")
public class BiologicalAjaxController {
    @Autowired
    private BiosafetyEnquiryClient biosafetyEnquiryClient;

    @PostMapping(value = "bio.do")
    public @ResponseBody
    Map<String, Object> queryBiologicalBySchedule(HttpServletRequest request) {
        Map<String, Object> jsonMap = Maps.newHashMapWithExpectedSize(3);
         String schedule = ParamUtil.getString(request,"schedule");
         if(StringUtils.isEmpty(schedule)){schedule="null";}
         log.info(StringUtil.changeForLog("ajax query schedule"+schedule));
         List<BatBasicInfo> batInfoList = biosafetyEnquiryClient.queryBiologicalBySchedule(schedule).getEntity();
        if(batInfoList != null && !batInfoList.isEmpty()) {
            log.info(StringUtil.changeForLog("ajax biologicalDtoList "+batInfoList));
            List<String> strings = new ArrayList<>(batInfoList.size());
            for (BatBasicInfo batBasicInfo : batInfoList) {
                strings.add(batBasicInfo.getName());
            }
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",strings);
        } else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }

}
