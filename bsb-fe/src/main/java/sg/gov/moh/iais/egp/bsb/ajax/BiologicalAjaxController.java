package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.action.BsbSubmissionCommon;
import sg.gov.moh.iais.egp.bsb.action.BsbTransferNotificationDelegator;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.dto.submission.BiologicalDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Slf4j
@Controller
@RequestMapping("/bio-info")
public class BiologicalAjaxController {
    @Autowired
    private BsbSubmissionCommon subCommon;


    /**
     * this ajax method is used to get biological info by schedule from FacListDto
     * queryBiologicalBySchedule
     * @return Map<String, Object>
     * */
    @PostMapping(value = "bio.do")
    public @ResponseBody
    Map<String, Object> queryBiologicalBySchedule(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
         String schedule = ParamUtil.getString(request,"schedule");
         if(StringUtils.isEmpty(schedule)){schedule="null";}
         log.info(StringUtil.changeForLog("ajax query schedule"+schedule));
         //call delegator method to get biological info from facListDto
         List<Biological>biologicalList = subCommon.getBiologicalById(request);
        if(biologicalList != null && !biologicalList.isEmpty()) {
            log.info(StringUtil.changeForLog("ajax biologicalList "+biologicalList));
            List<String> strings = new ArrayList<>();
            for (Biological b : biologicalList) {
                if(b.getSchedule().equals(schedule)){
                    strings.add(b.getName());
                }
            }
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",strings);
        } else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }

}
